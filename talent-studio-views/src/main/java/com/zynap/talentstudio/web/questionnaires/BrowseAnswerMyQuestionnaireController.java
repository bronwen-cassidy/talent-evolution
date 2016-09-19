/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires;

import com.zynap.domain.admin.User;
import com.zynap.exception.PessimisticLockingFailureException;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.messages.MessageItem;
import com.zynap.talentstudio.organisation.subjects.NoSubjectForUserException;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.questionnaires.Questionnaire;
import com.zynap.talentstudio.questionnaires.QuestionnaireDTO;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.common.UrlBeanPair;
import com.zynap.talentstudio.web.common.exceptions.InvalidSubmitException;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.workflow.WorkflowConstants;
import com.zynap.talentstudio.mail.IMailNotification;

import javax.servlet.http.HttpServletRequest;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 02-Dec-2008 14:03:56
 */
public class BrowseAnswerMyQuestionnaireController extends BrowseAnswerQuestionnaireController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        final Long questionnaireId = RequestUtils.getLongParameter(request, ParameterConstants.QUESTIONNAIRE_ID);
        final Long workflowId = RequestUtils.getLongParameter(request, WorkflowConstants.WORKFLOW_ID_PARAM_PREFIX);
        final Long subjectId = RequestUtils.getLongParameter(request, ParameterConstants.NODE_ID_PARAM);
        final User user = ZynapWebUtils.getUser(request);

        if(subjectId == null) {
            // throw an exception to indicate people are using the browser back potentially
            logger.error("Throwing a new InvalidSubmitException from BrowseAnswerMyQuestionnaireController line 50 as subjectId is null -> should not be possible");
            throw new InvalidSubmitException(request.getSession(), null, request.getRequestURI(), true, this.getClass().getName());
        }

        // get all the questionnaires for navigation for the viewed subject
        final Collection<QuestionnaireDTO> questionnaires = questionnaireService.getPersonalPortfolioQuestionnaires(subjectId, user.getId());
        List<QuestionnaireDTO> results = QuestionnaireHelper.sortResults(questionnaires);
        List<QuestionnaireDTO> filteredResults = new ArrayList<QuestionnaireDTO>();

        // filter the results removing individual not writeable entries
        for (QuestionnaireDTO result : results) {
            if (result.isIndividualWrite() && !QuestionnaireWorkflow.STATUS_COMPLETED.equals(result.getStatus())) {
                filteredResults.add(result);
            }
        }

        QuestionnaireDTO current = QuestionnaireHelper.findCurrent(workflowId, filteredResults);
        QuestionnaireWrapper questionnaireWrapper;
        // if the current is null, hacking has occurred and we will error!
        try {
            questionnaireWrapper = questionnaireHelper.getQuestionnaireWrapper(questionnaireId, workflowId, user, subjectId, true);

        } catch (PessimisticLockingFailureException e) {
            logger.error("PessimisticLockingFailureException seen: " + e.getMessage());
            questionnaireWrapper = buildErrorWrapper(questionnaireId, workflowId, subjectId, e.getKey());
        }

        questionnaireWrapper.setUserId(user.getId());
        BrowseQuestionnaireWrapper wrapper = new BrowseQuestionnaireWrapper(filteredResults, questionnaireWrapper, current, true);
        wrapper.setSubjectId(subjectId);
        try {
            Subject subject = subjectService.findByUserId(user.getId());
            if (subject != null) {
                wrapper.setUserManagers(subject.getManagers());
            }
        } catch (NoSubjectForUserException nsue) {
            wrapper.setUserManagers(new LinkedList());
        }
        return wrapper;
    }

    protected void processInbox(HttpServletRequest request, Object command) throws TalentStudioException {

        final BrowseQuestionnaireWrapper wrapper = (BrowseQuestionnaireWrapper) command;
        final QuestionnaireWrapper questionnaireWrapper = wrapper.getQuestionnaireWrapper();
        boolean sendToInbox = RequestUtils.getBooleanParameter(request, "sendToInbox", false);
        boolean sendEmail = RequestUtils.getBooleanParameter(request, "sendEmail", false);

        boolean process = sendEmail || sendToInbox;
        // clear any send messages before we start
        wrapper.resetSendState();
        
        if (process) {
            Long subjectId = wrapper.getSubjectId();
            List<User> participants = new ArrayList<User>();

            // getting all the managers of the subject
            Subject subject = subjectService.findById(subjectId);
            participants.addAll(subject.getManagers());

            if (participants.size() > 1) {
                //if there is more then one manager then do the following filter
                //filter managers to only one rather then all -i.e the manager selected
                Iterator<User> participant = participants.iterator();
                while (participant.hasNext()) {
                    User user = participant.next();
                    if (!questionnaireWrapper.containsManagerSelection(user.getId())) {
                        participant.remove();
                    }
                }
            }
            if (!participants.isEmpty()) {
                wrapper.setSendSuccess(true);
                final Questionnaire questionnaire = wrapper.getQuestionnaire();
                if (sendToInbox) messageService.create(questionnaire, MessageItem.MANAGER_VIEW, ZynapWebUtils.getUser(request), participants);

                if (sendEmail) {
                    UrlBeanPair pair;
                    if (sendToInbox) pair = mailNotifications.get(INBOX_MAIL);
                    else {
                        pair = mailNotifications.get(NO_INBOX_MAIL_INDIVIDUAL);
                    }
                    IMailNotification mailNotification = pair.getRef();
                    String url = pair.getUrl();
                    try {
                        mailNotification.send(url, ZynapWebUtils.getUser(request), questionnaire, participants.toArray(new User[participants.size()]));
                    } catch (Exception e) {
                        wrapper.resetSendState();
                        wrapper.setSendFail(true);
                        wrapper.setSendErrorMessage("send.fail");
                    }
                }
            } else {
                wrapper.resetSendState();
                wrapper.setSendFail(true);
                wrapper.setSendErrorMessage("no.manager.to.send.to");
            }
        }
    }
}

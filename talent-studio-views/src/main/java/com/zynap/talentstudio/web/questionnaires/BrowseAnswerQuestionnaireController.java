package com.zynap.talentstudio.web.questionnaires;

import com.zynap.domain.admin.User;
import com.zynap.exception.PessimisticLockingFailureException;
import com.zynap.exception.TalentStudioException;
import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.talentstudio.mail.IMailNotification;
import com.zynap.talentstudio.messages.IMessageService;
import com.zynap.talentstudio.organisation.IOrganisationUnitService;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.questionnaires.IQuestionnaireService;
import com.zynap.talentstudio.questionnaires.Questionnaire;
import com.zynap.talentstudio.questionnaires.QuestionnaireDTO;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.talentstudio.web.common.DefaultWizardFormController;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.common.UrlBeanPair;
import com.zynap.talentstudio.web.common.exceptions.InvalidSubmitException;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.tree.TreeBuilderHelper;
import com.zynap.talentstudio.web.workflow.WorkflowConstants;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: amark
 * Date: 27-Sep-2006
 * Time: 10:04:11
 */
public class BrowseAnswerQuestionnaireController extends DefaultWizardFormController implements WorkflowConstants {

    /**
     * Load questionnaire from business layer.
     *
     * @param request the servlet request
     * @return QuestionnaireWrapper
     * @throws Exception
     */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        final Long questionnaireId = RequestUtils.getLongParameter(request, ParameterConstants.QUESTIONNAIRE_ID);
        final Long workflowId = RequestUtils.getLongParameter(request, WorkflowConstants.WORKFLOW_ID_PARAM_PREFIX);
        final Long subjectId = RequestUtils.getLongParameter(request, ParameterConstants.NODE_ID_PARAM);
        final User user = ZynapWebUtils.getUser(request);

        // get all the questionnaires for navigation for the viewed subject
        final Collection<QuestionnaireDTO> questionnaires = questionnaireService.getPortfolioQuestionnaires(subjectId);
        List<QuestionnaireDTO> results = QuestionnaireHelper.sortResults(questionnaires);
        List<QuestionnaireDTO> filteredResults = new ArrayList<QuestionnaireDTO>();
        // filter the results removing manager not writeable entries
        for (QuestionnaireDTO result : results) {
            if (result.isManagerWrite() && !QuestionnaireWorkflow.STATUS_COMPLETED.equals(result.getStatus())) {
                filteredResults.add(result);
            }
        }
        User ownerUser = null;
        try {
            ownerUser = userService.findBySubjectId(subjectId);
        } catch (DomainObjectNotFoundException e) {
        }
        QuestionnaireDTO current = QuestionnaireHelper.findCurrent(workflowId, filteredResults);
        BrowseQuestionnaireWrapper wrapper;
        QuestionnaireWrapper questionnaireWrapper;
        try {
            questionnaireWrapper = questionnaireHelper.getQuestionnaireWrapper(questionnaireId, workflowId, user, subjectId, true);

        } catch (PessimisticLockingFailureException e) {
            logger.error("PessimisticLockingFailureException seen: " + e.getMessage());
            questionnaireWrapper = buildErrorWrapper(questionnaireId, workflowId, subjectId, e.getKey());
        }
        questionnaireWrapper.setUserId(user.getId());
        wrapper = new BrowseQuestionnaireWrapper(filteredResults, questionnaireWrapper, current, false);
        wrapper.setSubjectId(subjectId);
        wrapper.setSubjectUser(ownerUser);
        return wrapper;
    }

    protected QuestionnaireWrapper buildErrorWrapper(Long questionnaireId, Long workflowId, Long subjectId, String key) {

        QuestionnaireWrapper questionnaireWrapper = new QuestionnaireWrapper();
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setQuestionnaireWorkflowId(workflowId);
        questionnaire.setSubjectId(subjectId);
        questionnaire.setId(questionnaireId);
        questionnaireWrapper.setQuestionnaire(questionnaire);
        questionnaireWrapper.setErrorKey(key);
        return questionnaireWrapper;
    }

    public void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

        BrowseQuestionnaireWrapper wrapper = (BrowseQuestionnaireWrapper) command;
        Long workflowId = RequestUtils.getLongParameter(request, ParameterConstants.QUESTIONNAIRE_WF_ID);
        QuestionnaireWrapper questionnaireWrapper;
        QuestionnaireDTO dto;

        int target = getTargetPage(request, page);
        switch (target) {
            case COMBO_CHANGE:
                break;
            case NEXT_BUTTON_PRESS:
                workflowId = wrapper.getNext();
                break;
            case PREVIOUS_BUTTON_PRESS:
                workflowId = wrapper.getPrevious();
                break;
            case FIRST_BUTTON_PRESS:
                workflowId = wrapper.getFirst();
                break;
            case LAST_BUTTON_PRESS:
                workflowId = wrapper.getLast();
                break;
        }
        // unlock any previous questionnaires being edited
        final QuestionnaireDTO current = wrapper.getCurrent();
        Long questionnaireId = wrapper.getQuestionnaire().getId();
        if (current != null && questionnaireId != null) {
            questionnaireService.unlockQuestionnaire(questionnaireId, ZynapWebUtils.getUserId(request));
        }
        if (target == SEND_EMAIL) {
            processInbox(request, command);
            // rebuild the current
            workflowId = wrapper.getWorkflowId();
        }
        dto = wrapper.findQuestionnaireDTO(workflowId);

        if(dto == null) {
            /* there may be a possibility here that if the workflow id is not null we can go back to the database to fetch it */
            final InvalidSubmitException invalidSubmitException = new InvalidSubmitException(request.getSession(false), wrapper, request.getRequestURI(), false, getClass().getName());
            logger.error("error unable to find the dto for the given workflow: " + workflowId, invalidSubmitException);            
            throw invalidSubmitException;
        }

        try {
            questionnaireWrapper = questionnaireHelper.getQuestionnaireWrapper(dto.getId(), workflowId, ZynapWebUtils.getUser(request), wrapper.getSubjectId(), true);
            wrapper.updateState(questionnaireWrapper, dto);
        } catch (PessimisticLockingFailureException e) {
            logger.error("PessimisticLockingFailureException seen: " + e.getMessage());
            questionnaireWrapper = buildErrorWrapper(dto.getId(), workflowId, wrapper.getSubjectId(), e.getKey());
            wrapper.updateState(questionnaireWrapper, dto);
        }
    }

    /**
     * Load data for pickers.
     *
     * @param refData the map containing any data needed by the web page
     * @throws com.zynap.exception.TalentStudioException
     *          on error
     */
    protected void dataForPickers(Map<String, Object> refData) throws TalentStudioException {
        refData.put("orgUnitTree", TreeBuilderHelper.buildOrgUnitTree(organisationUnitService.findOrgUnitTree(OrganisationUnit.ROOT_ORG_UNIT_ID)));
    }

    /**
     * Load ref. data.
     * <br/> Adds error messages to display if the save failed.
     * <br/> Also loads org unit picker data.
     *
     * @param request the servlet request
     * @param command the command object {@link QuestionnaireWrapper}
     * @param errors  the web page errors
     * @param page    the current page
     * @return Map
     * @throws Exception
     */
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

        final Map<String, Object> refData = new HashMap<String, Object>();
        dataForPickers(refData);
        return refData;
    }

    /**
     * Save modified questionnaire.
     *
     * @param request  the servlet request
     * @param response the servlet response
     * @param command  the command object {@link QuestionnaireWrapper}
     * @param errors   the web page errors
     * @return ModelAndView
     * @throws Exception
     */
    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        return null;
    }

    protected void processInbox(HttpServletRequest request, Object command) throws TalentStudioException {

        final BrowseQuestionnaireWrapper wrapper = (BrowseQuestionnaireWrapper) command;
        boolean sendToInbox = RequestUtils.getBooleanParameter(request, "sendToInbox", false);
        boolean sendEmail = RequestUtils.getBooleanParameter(request, "sendEmail", false);

        boolean process = sendEmail || sendToInbox;
        // set defaultSate
        wrapper.resetSendState();
        
        if (process) {
            Long subjectId = wrapper.getSubjectId();
            List<User> participants = new ArrayList<User>();
            // manager view sending to the individual
            User toUser = null;
            try {
                toUser = userService.findBySubjectId(subjectId);
            } catch (DomainObjectNotFoundException e) {
            }
            if (toUser != null) participants.add(toUser);
            if (!participants.isEmpty()) {
                wrapper.setSendSuccess(true);
                final Questionnaire questionnaire = wrapper.getQuestionnaire();
                if (sendToInbox) messageService.create(questionnaire, true, ZynapWebUtils.getUser(request), participants);
                if (sendEmail) {
                    UrlBeanPair pair;
                    if (sendToInbox) pair = mailNotifications.get(INBOX_MAIL);
                    else {
                        pair = mailNotifications.get(NO_INBOX_MAIL_MANAGER);
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
            }
        }
    }

    /**
     * Redirect to previous page.
     *
     * @param request  the servlet request
     * @param response the servlet response
     * @param command  the command object {@link QuestionnaireWrapper}
     * @param errors   the errors object containing binding errors
     * @return ModelAndView
     * @throws Exception
     */
    protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        final BrowseQuestionnaireWrapper wrapper = (BrowseQuestionnaireWrapper) command;
        return buildModelAndView(getCancelView(), wrapper);
    }

    /**
     * Build model and view for cancel or save.
     *
     * @param url     the redirect page url
     * @param wrapper the command object {@link QuestionnaireWrapper}
     * @return ModelAndView
     */
    private ModelAndView buildModelAndView(final String url, final BrowseQuestionnaireWrapper wrapper) {

        final RedirectView redirectView = HistoryHelper.buildRedirectView(url);
        redirectView.addStaticAttribute(ParameterConstants.QUESTIONNAIRE_ID, wrapper.getQuestionnaireId());
        redirectView.addStaticAttribute(WORKFLOW_ID_PARAM_PREFIX, wrapper.getWorkflowId());
        redirectView.addStaticAttribute(ParameterConstants.NODE_ID_PARAM, wrapper.getSubjectId());
        redirectView.addStaticAttribute(MY_PORTFOLIO, wrapper.isMyPortfolio());
        return new ModelAndView(redirectView);
    }

    /**
     * Set questionnaire data on wrapper.
     *
     * @param wrapper       QuestionnaireWrapper
     * @param questionnaire questionnaire to be refreshed
     */
    protected final void refreshQuestionnaire(QuestionnaireWrapper wrapper, Questionnaire questionnaire) {
        final QuestionnaireDefinition questionnaireDefinition = questionnaire.getQuestionnaireWorkflow().getQuestionnaireDefinition();
        questionnaireHelper.setQuestionnaireState(wrapper, questionnaireDefinition, questionnaire);
    }

    public void setQuestionnaireHelper(QuestionnaireHelper questionnaireHelper) {
        this.questionnaireHelper = questionnaireHelper;
    }

    public void setMessageService(IMessageService messageService) {
        this.messageService = messageService;
    }

    public void setMailNotifications(Map<String, UrlBeanPair> mailNotification) {
        this.mailNotifications = mailNotification;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    public void setSubjectService(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    public final void setQuestionnaireService(IQuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    public final void setOrganisationUnitService(IOrganisationUnitService organisationUnitService) {
        this.organisationUnitService = organisationUnitService;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    protected String imageURL;

    protected IQuestionnaireService questionnaireService;
    protected volatile QuestionnaireHelper questionnaireHelper;
    protected Map<String, UrlBeanPair> mailNotifications;
    private IOrganisationUnitService organisationUnitService;
    IMessageService messageService;
    protected IUserService userService;
    protected ISubjectService subjectService;

    protected final String MY_PORTFOLIO = "myPortfolio";
    protected final String INBOX_MAIL = "INBOX";
    protected final String NO_INBOX_MAIL_MANAGER = "NO_INBOX_MANAGER";
    protected final String NO_INBOX_MAIL_INDIVIDUAL = "NO_INBOX_STAFF";

    protected static final int COMBO_CHANGE = 1;
    protected static final int NEXT_BUTTON_PRESS = 2;
    protected static final int PREVIOUS_BUTTON_PRESS = 3;
    protected static final int FIRST_BUTTON_PRESS = 4;
    protected static final int LAST_BUTTON_PRESS = 5;
    protected static final int SEND_EMAIL = 6;

    /**
     * Constants for ref data.
     */
    public static final String MESSAGE_KEY = "message";
}
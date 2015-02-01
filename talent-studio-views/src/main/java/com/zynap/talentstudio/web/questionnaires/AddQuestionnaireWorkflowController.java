/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires;

import com.zynap.domain.UserSession;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.analysis.IAnalysisService;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.common.groups.IGroupService;
import com.zynap.talentstudio.mail.IMailNotification;
import com.zynap.talentstudio.questionnaires.DefinitionDTO;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.common.UrlBeanPair;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.history.SavedURL;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.talentstudio.web.questionnaires.definition.ViewQuestionnaireDefinitionController;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Controller that creates and publishes a questionnaire workflow.
 *
 * @author bcassidy
 * @version $Revision: $
 */
public final class AddQuestionnaireWorkflowController extends BaseQuestionnaireController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        SavedURL currentURL = HistoryHelper.getCurrentURL(request);
        String cancelUrl = getCancelView();

        if (currentURL != null) {
            final Map<String, Object> parameters = ZynapWebUtils.getParametersStartingWith(currentURL.getParameters(), ControllerConstants.DISPLAY_TAG_PREFIX);
            String fullUrl = ZynapWebUtils.buildURL(cancelUrl, parameters, false);
            setCancelView(fullUrl);
        }

        final QuestionnaireWorkflow questionnaireWorkflow = new QuestionnaireWorkflow();
        QuestionnaireDefinition questionnaireDefinition = new QuestionnaireDefinition();
        List<DefinitionDTO> definitions = null;
        Long id = RequestUtils.getLongParameter(request, ParameterConstants.QUESTIONNAIRE_DEF_ID);
        if (id != null) {
            questionnaireDefinition = questionnaireDefinitionService.findDefinition(id);
        } else {
            definitions = questionnaireDefinitionService.listDefinitions();
        }
        questionnaireWorkflow.setQuestionnaireDefinition(questionnaireDefinition);
        final QuestionnaireWorkflowWrapperBean questionnaireWorkflowWrapperBean = new QuestionnaireWorkflowWrapperBean(questionnaireWorkflow);
        questionnaireWorkflowWrapperBean.setDefinitions(definitions);
        final Collection populations = analysisService.findAll(IPopulationEngine.P_SUB_TYPE_, ZynapWebUtils.getUserId(request), null);
        questionnaireWorkflowWrapperBean.setPopulations(populations);
        questionnaireWorkflowWrapperBean.setGroups(groupService.find(Group.TYPE_QUESTIONNAIRE));
        return questionnaireWorkflowWrapperBean;
    }

    protected ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {

        final QuestionnaireWorkflowWrapperBean wrapper = (QuestionnaireWorkflowWrapperBean) command;

        //TS-2297 Enforcing permissions logic (to write you MUST have read permissions also).
        if (wrapper.isManagerWrite()) wrapper.setManagerRead(true);
        if (wrapper.isIndividualWrite()) wrapper.setIndividualRead(true);

        final QuestionnaireWorkflow questionnaireWorkflow = wrapper.getModifiedQuestionnaireWorkflow();
        final UserSession userSession = ZynapWebUtils.getUserSession(request);
        final Long userId = userSession.getId();
        questionnaireWorkflow.setUserId(userId);

        // set the workflow type
        if (wrapper.useInfoForm()) questionnaireWorkflow.setWorkflowType(QuestionnaireWorkflow.TYPE_INFO_FORM);
        else questionnaireWorkflow.setWorkflowType(QuestionnaireWorkflow.TYPE_QUESTIONNAIRE);

        try {
            questionnaireWorkflowService.create(questionnaireWorkflow);
            questionnaireWorkflowService.startWorkflow(questionnaireWorkflow);

            // TS-2299 Field 'sendEmail' added to wrapper bean
            if (wrapper.isSendEmail()) {
                // different templates and urls. If the questionnaire is an info form the url is to my details/protfolio tab, otherwise it goes to your to-do list
                Collection<User> participants = questionnaireWorkflowService.getParticipants(questionnaireWorkflow);
                UrlBeanPair pair = mailNotifications.get(questionnaireWorkflow.isInfoForm() ? INFOFORM_FORM : QUESTIONNAIRE_FORM);
                IMailNotification mailNotification = pair.getRef();
                String url = pair.getUrl();
                mailNotification.send(url, ZynapWebUtils.getUser(request), questionnaireWorkflow, (User[]) participants.toArray(new User[participants.size()]));
            }

        } catch (DataIntegrityViolationException e) {
            errors.rejectValue("label", "error.duplicate.name", "The label must be unique the one provided is already in use");
            return showForm(request, response, errors);
        }

        return new ModelAndView(new ZynapRedirectView(getSuccessView(), ParameterConstants.QUESTIONNAIRE_ID, questionnaireWorkflow.getId()));
    }

    protected ModelAndView onCancelInternal(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {

        QuestionnaireWorkflowWrapperBean wrapper = (QuestionnaireWorkflowWrapperBean) command;

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(ParameterConstants.QUESTIONNAIRE_DEF_ID, wrapper.getQuestionnaireDefinition().getId());
        parameters.put(ParameterConstants.ACTIVE_TAB, ViewQuestionnaireDefinitionController.QUESTIONNAIRES_TAB);

        ZynapRedirectView view = new ZynapRedirectView(getCancelView(), parameters);
        return new ModelAndView(view);
    }

    public void setMailNotifications(Map<String, UrlBeanPair> mailNotifications) {
        this.mailNotifications = mailNotifications;
    }

    public void setGroupService(IGroupService groupService) {
        this.groupService = groupService;
    }

    public void setAnalysisService(IAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    private IAnalysisService analysisService;
    private IGroupService groupService;

    private Map<String, UrlBeanPair> mailNotifications;
    protected final String QUESTIONNAIRE_FORM = "QUESTIONNAIRE";
    protected final String INFOFORM_FORM = "INFOFORM";
}

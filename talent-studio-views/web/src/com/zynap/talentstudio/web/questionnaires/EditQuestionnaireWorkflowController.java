/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires;

import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;

import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.history.SavedURL;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.common.groups.IGroupService;

import org.springframework.dao.DataAccessException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class EditQuestionnaireWorkflowController extends BaseQuestionnaireController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        final SavedURL currentURL = HistoryHelper.getCurrentURL(request);
        String cancelUrl = getCancelView();

        if (currentURL != null) {
            Map<String, Object> parameters = currentURL.buildParameters();
            String fullUrl = ZynapWebUtils.buildURL(cancelUrl, parameters);
            setCancelView(fullUrl);
        }

        Long id = RequestUtils.getRequiredLongParameter(request, ParameterConstants.QUESTIONNAIRE_ID);
        QuestionnaireWorkflow questionnaireWorkflow = (QuestionnaireWorkflow) questionnaireWorkflowService.findById(id);
        QuestionnaireWorkflowWrapperBean workflowWrapperBean = new QuestionnaireWorkflowWrapperBean(questionnaireWorkflow);
        workflowWrapperBean.setGroups(groupService.find(Group.TYPE_QUESTIONNAIRE));
        return workflowWrapperBean;
    }

    protected ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {
        
        QuestionnaireWorkflowWrapperBean wrapper = (QuestionnaireWorkflowWrapperBean) command;
        QuestionnaireWorkflow questionnaireWorkflow = wrapper.getUpdatedQuestionnaireWorkflow();
        try {
            questionnaireWorkflowService.update(questionnaireWorkflow);
        } catch (DataAccessException e) {
            errors.rejectValue("label", "error.duplicate.name", "The label must be unique the one provided is already in use");
            return showForm(request, response, errors);
        }
        return new ModelAndView(new ZynapRedirectView(getSuccessView(), ParameterConstants.QUESTIONNAIRE_ID, questionnaireWorkflow.getId()));
    }

    protected ModelAndView onCancelInternal(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {
        QuestionnaireWorkflowWrapperBean wrapper = (QuestionnaireWorkflowWrapperBean) command;
        return new ModelAndView(new ZynapRedirectView(getCancelView(), ParameterConstants.QUESTIONNAIRE_ID, wrapper.getId()));
    }

    public void setGroupService(IGroupService groupService) {
        this.groupService = groupService;
    }

    private IGroupService groupService;
}

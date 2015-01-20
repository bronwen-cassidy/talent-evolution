/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.questionnaires.IQuestionnaireService;
import com.zynap.talentstudio.questionnaires.QuestionnaireDTO;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.DefaultWizardFormController;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.common.exceptions.InvalidSubmitException;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.workflow.WorkflowConstants;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 01-Dec-2008 08:58:13
 */
public class BrowseQuestionnaireController extends DefaultWizardFormController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        final Long questionnaireId = RequestUtils.getLongParameter(request, ParameterConstants.QUESTIONNAIRE_ID);
        final Long workflowId = RequestUtils.getLongParameter(request, WorkflowConstants.WORKFLOW_ID_PARAM_PREFIX);
        final Long subjectId = RequestUtils.getLongParameter(request, ParameterConstants.NODE_ID_PARAM);
        final User user = ZynapWebUtils.getUser(request);

        QuestionnaireWrapper questionnaireWrapper = questionnaireHelper.getQuestionnaireWrapper(questionnaireId, workflowId, user, subjectId, false);
        final Collection<QuestionnaireDTO> questionnaires = questionnaireService.getPortfolioQuestionnaires(subjectId);        
        List<QuestionnaireDTO> results = QuestionnaireHelper.sortResults(questionnaires);
        QuestionnaireDTO current = QuestionnaireHelper.findCurrent(questionnaireWrapper.getWorkflowId(), results);
        final BrowseQuestionnaireWrapper wrapper = new BrowseQuestionnaireWrapper(results, questionnaireWrapper, current, false);
        wrapper.setSubjectId(subjectId);
        return wrapper;
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

        dto = wrapper.findQuestionnaireDTO(workflowId);
        if(dto == null) {
            throw new InvalidSubmitException(request.getSession(), command, request.getRequestURI(), true, getClass().getName());   
        }
        questionnaireWrapper = questionnaireHelper.getQuestionnaireWrapper(dto.getId(), workflowId, ZynapWebUtils.getUser(request), wrapper.getSubjectId(), false);
        wrapper.updateState(questionnaireWrapper, dto);
    }

    protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

        final Map<String, Object> refData = new HashMap<String, Object>();
        BrowseQuestionnaireWrapper wrapper = (BrowseQuestionnaireWrapper) command;
        final Long subjectId = wrapper.getSubjectId();

        String completeURL = ZynapWebUtils.buildURL(imageURL, ParameterConstants.NODE_ID_PARAM, subjectId);
        refData.put("imageUrl", completeURL);
        refData.put(ControllerConstants.EDIT_VIEW, editURL);

        return refData;
    }

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        return null;
    }

    public void setQuestionnaireHelper(QuestionnaireHelper questionnaireHelper) {
        this.questionnaireHelper = questionnaireHelper;
    }

    public void setQuestionnaireService(IQuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    public void setEditURL(String editURL) {
        this.editURL = editURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    private String editURL;
    private String imageURL;
    QuestionnaireHelper questionnaireHelper;
    IQuestionnaireService questionnaireService;

    protected static final int COMBO_CHANGE = 1;
    protected static final int NEXT_BUTTON_PRESS = 2;
    protected static final int PREVIOUS_BUTTON_PRESS = 3;
    protected static final int FIRST_BUTTON_PRESS = 4;
    protected static final int LAST_BUTTON_PRESS = 5;
}

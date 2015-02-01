/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.questionnaires.IQueDefinitionService;
import com.zynap.talentstudio.questionnaires.IQueWorkflowService;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class QuestionnaireMultiController extends MultiActionController {

    public ModelAndView listQuestionnaireDefinitions(HttpServletRequest request, HttpServletResponse response) throws Exception {

        final Map<String, Object> model = new HashMap<String, Object>();
        model.put(QUE_DEFINITIONS_KEY, questionnaireDefinitionService.listDefinitions());
        return new ModelAndView(LIST_DEFINITIONS_VIEW, ControllerConstants.MODEL_NAME, model);
    }

    public ModelAndView viewQuestionnaireWorkflow(HttpServletRequest request, HttpServletResponse response) throws Exception {

        final QuestionnaireWorkflow workflow = getWorkflow(request);
        final Map<String, Object> model = new HashMap<String, Object>();
        model.put(QUESTIONNAIRE_WORKFLOW_KEY, workflow);
        model.put(QUESTIONNAIRES_KEY, workflow.getQuestionnaires());
        model.put("numItems", new Integer(workflow.getQuestionnaires().size()));
        return new ModelAndView(VIEW_WORKFLOW_VIEW, ControllerConstants.MODEL_NAME, model);
    }

    public ModelAndView archiveQuestionnaireWorkflow(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        final QuestionnaireWorkflow workflow = getWorkflow(request);
        workflow.toggleArchived();
        questionnaireWorkflowService.update(workflow);
        return new ModelAndView(new ZynapRedirectView("viewquestionnaireworkflow.htm", ParameterConstants.QUESTIONNAIRE_ID, workflow.getId()));
    }

    private QuestionnaireWorkflow getWorkflow(HttpServletRequest request) throws TalentStudioException {
        final Long id = RequestUtils.getLongParameter(request, ParameterConstants.QUESTIONNAIRE_ID);
        return (QuestionnaireWorkflow) questionnaireWorkflowService.findById(id);
    }


    public void setQuestionnaireWorkflowService(IQueWorkflowService questionnaireWorkflowService) {
        this.questionnaireWorkflowService = questionnaireWorkflowService;
    }

    public void setQuestionnaireDefinitionService(IQueDefinitionService questionnaireDefinitionService) {
        this.questionnaireDefinitionService = questionnaireDefinitionService;
    }

    private IQueWorkflowService questionnaireWorkflowService;
    private IQueDefinitionService questionnaireDefinitionService;

    private static final String QUE_DEFINITIONS_KEY = "questionnaireDefinitions";
    private static final String QUESTIONNAIRE_WORKFLOW_KEY = "questionnaire";
    private static final String QUESTIONNAIRES_KEY = "questionnaires";

    private static final String LIST_DEFINITIONS_VIEW = "listquestionnairedefinition";
    private static final String VIEW_WORKFLOW_VIEW = "viewquestionnaireworkflow";
}

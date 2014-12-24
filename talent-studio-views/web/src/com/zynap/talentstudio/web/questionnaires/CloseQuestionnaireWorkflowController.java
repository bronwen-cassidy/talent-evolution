/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires;

import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;

import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller that handles deletion of questionnaire workflows.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class CloseQuestionnaireWorkflowController extends BaseQuestionnaireController {

    protected ModelAndView onCancelInternal(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {
        QuestionnaireWorkflow workflow = (QuestionnaireWorkflow) command;

        final RedirectView view = new ZynapRedirectView(getCancelView());
        view.addStaticAttribute(ParameterConstants.QUESTIONNAIRE_ID, workflow.getId());

        return new ModelAndView(view);
    }

    protected ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {

        QuestionnaireWorkflow workflow = (QuestionnaireWorkflow) command;
        questionnaireWorkflowService.closeWorkflow(workflow);
        questionnaireWorkflowService.reload(workflow.getId());
        final ZynapRedirectView redirectView = new ZynapRedirectView(getSuccessView(), ParameterConstants.QUESTIONNAIRE_ID, workflow.getId());
        return new ModelAndView(redirectView);
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        Long queId = RequestUtils.getLongParameter(request, ParameterConstants.QUESTIONNAIRE_ID);
        return questionnaireWorkflowService.findById(queId);
    }
}

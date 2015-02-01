/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires.definition;

import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.questionnaires.BaseQuestionnaireController;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.dao.DataAccessException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class DeleteQuestionnaireDefinitionController extends BaseQuestionnaireController {

    protected ModelAndView onCancelInternal(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {
        QuestionnaireDefinition questionnaireDefinition = (QuestionnaireDefinition) command;
        final RedirectView view = new ZynapRedirectView(getCancelView());
        view.addStaticAttribute(ParameterConstants.QUESTIONNAIRE_DEF_ID, questionnaireDefinition.getId());
        return new ModelAndView(view);
    }

    protected ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {
        QuestionnaireDefinition questionnaireDefinition = (QuestionnaireDefinition) command;
        try {
            questionnaireDefinitionService.delete(questionnaireDefinition);
        } catch (DataAccessException e) {
            logger.error(e.getMessage(), e);
            errors.reject("error.delete.questionnaire.definition.dependencies", "Cannot delete the questionnaire definition as it is in use in the system");
            return showForm(request, response, errors);
        }
        return new ModelAndView(new ZynapRedirectView(getSuccessView()));
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        Long queDefId = RequestUtils.getLongParameter(request, ParameterConstants.QUESTIONNAIRE_DEF_ID);
        return questionnaireDefinitionService.findDefinition(queDefId);
    }
}

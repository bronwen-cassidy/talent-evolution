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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class EditQuestionnaireDefinitionController extends BaseQuestionnaireController {

    protected ModelAndView onCancelInternal(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {
        QuestionnaireDefinitionWrapper wrapper = (QuestionnaireDefinitionWrapper) command;
        return new ModelAndView(new ZynapRedirectView(getCancelView(), ParameterConstants.QUESTIONNAIRE_DEF_ID, wrapper.getId()));
    }

    protected ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {

        QuestionnaireDefinitionWrapper wrapper = (QuestionnaireDefinitionWrapper) command;
        Long questionnaireId;

        try {
            QuestionnaireDefinition originalQuestionnaireDefinition = wrapper.getQuestionnaireDefinition();
            questionnaireDefinitionService.update(originalQuestionnaireDefinition);
            questionnaireId = originalQuestionnaireDefinition.getId();
        } catch (DataAccessException e) {
            errors.rejectValue("label", "error.duplicate.questionnaire.name", e.getMessage());
            return showForm(request, errors, getFormView());
        }
        return new ModelAndView(new ZynapRedirectView(getSuccessView(), ParameterConstants.QUESTIONNAIRE_DEF_ID, questionnaireId));
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        final Long queDefId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.QUESTIONNAIRE_DEF_ID);
        final QuestionnaireDefinition questionnaireDefinition = questionnaireDefinitionService.findDefinition(queDefId);
        final QuestionnaireDefinitionWrapper questionnaireDefinitionWrapper = new QuestionnaireDefinitionWrapper(questionnaireDefinition);
        questionnaireHelper.setQuestionnaireState(questionnaireDefinitionWrapper, questionnaireDefinition);

        return questionnaireDefinitionWrapper;
    }
    
}

/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires.definition;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;
import com.zynap.talentstudio.questionnaires.DefinitionDTO;
import com.zynap.talentstudio.questionnaires.QuestionnaireXmlData;
import com.zynap.talentstudio.questionnaires.support.InvalidQuestionException;
import com.zynap.talentstudio.questionnaires.support.InvalidXmlDefinitionException;
import com.zynap.talentstudio.questionnaires.support.QuestionnaireDefinitionFactory;
import com.zynap.talentstudio.questionnaires.support.SchemaValidationFailedException;
import com.zynap.talentstudio.questionnaires.support.InvalidQuestionReferenceException;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.questionnaires.BaseQuestionnaireController;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.history.SavedURL;

import org.springframework.dao.DataAccessException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collection;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class AddQuestionnaireDefinitionController extends BaseQuestionnaireController {

    protected ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {
        QuestionnaireDefinitionWrapper wrapper = (QuestionnaireDefinitionWrapper) command;
        QuestionnaireDefinition questionnaireDefinition;

        final byte[] xmlData;
        try {
            if(wrapper.isToClone()) {                
                final Long queDefinitionId = wrapper.getSelectedDefinitionId();
                final QuestionnaireXmlData data = questionnaireDefinitionService.findQuestionnaireDefinitionXml(queDefinitionId);
                xmlData = data.getXmlDefinition();
                
            } else {
                xmlData = wrapper.getDefinitionBytes();
            }

            questionnaireDefinition = QuestionnaireDefinitionFactory.parse(xmlData);
        } catch (InvalidQuestionReferenceException e) {
            errors.rejectValue("definitionBytes", e.getKey(), e.getArgs(), e.getMessage());
            return showForm(request, response, errors);
        } catch (InvalidQuestionException e) {
            errors.rejectValue("definitionBytes", e.getKey(), e.getArgs(), e.getMessage());
            return showForm(request, response, errors);
        } catch (InvalidXmlDefinitionException e) {
            errors.rejectValue("definitionBytes", "error.invalid.xml.definition", new Object[]{new Integer(e.getLineNumber()), e.getMessage(), new Integer(e.getColumnNumber())}, "The XML has failed validation. Please validate it against the Questionnaire schema.");
            return showForm(request, response, errors);
        } catch (SchemaValidationFailedException e) {
            errors.rejectValue("definitionBytes", "error.xml.failed.schema.validation", "The XML has failed validation. Please validate it against the Questionnaire schema.");
            return showForm(request, response, errors);
        }
        try {
            wrapper.setState(questionnaireDefinition);
            questionnaireDefinitionService.create(questionnaireDefinition);
            final QuestionnaireXmlData data = new QuestionnaireXmlData();
            data.setId(questionnaireDefinition.getId());
            data.setXmlDefinition(xmlData);
            questionnaireDefinitionService.createXml(data);
        } catch (TalentStudioException e) {
            logger.error(e.getMessage(), e);
            errors.rejectValue("definitionBytes", "error.xml.invalid", e.getMessage());
            return showForm(request, response, errors);
        } catch (DataAccessException e) {
            logger.error(e.getMessage(), e);
            errors.rejectValue("definitionBytes", "error.data.integrity.issue", new Object[]{e.getMessage()}, "Questionnaire definitions must all have unique names");
            return showForm(request, response, errors);
        }
        return new ModelAndView(new ZynapRedirectView(getSuccessView()), ParameterConstants.QUESTIONNAIRE_DEF_ID, questionnaireDefinition.getId());
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        final SavedURL currentURL = HistoryHelper.getCurrentURL(request);
        String cancelUrl = getCancelView();
        if (currentURL != null) {
            Map<String, Object> parameters = currentURL.buildParameters();
            String fullUrl = ZynapWebUtils.buildURL(cancelUrl, parameters, false);
            setCancelView(fullUrl);
        }
        Collection<DefinitionDTO> allDefinitions = questionnaireDefinitionService.listDefinitions();
        QuestionnaireDefinitionWrapper questionnaireDefinitionWrapper = new QuestionnaireDefinitionWrapper();
        questionnaireDefinitionWrapper.setQuestionnaireDefinitions(allDefinitions);
        return questionnaireDefinitionWrapper;
    }

    protected ModelAndView onCancelInternal(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {
        return new ModelAndView(new ZynapRedirectView(getCancelView()));
    }

    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors) throws Exception {
        QuestionnaireDefinitionWrapper wrapper = (QuestionnaireDefinitionWrapper) command;
        if (!wrapper.isToClone()) {
            if (wrapper.getDefinitionBytes() == null || wrapper.getDefinitionBytes().length == 0) {
                errors.rejectValue("definitionBytes", "error.invalid.file", "Please upload a valid questionnaire");
            }
        }
    }
}

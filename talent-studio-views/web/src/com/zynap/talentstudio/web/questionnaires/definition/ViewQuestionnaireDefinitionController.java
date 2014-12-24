package com.zynap.talentstudio.web.questionnaires.definition;

import com.zynap.talentstudio.questionnaires.IQueDefinitionService;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.history.SavedURL;
import com.zynap.talentstudio.web.questionnaires.QuestionnaireHelper;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.web.controller.ZynapDefaultFormController;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;

/**
 * Controller that handles display of questionnaire definition.
 * <p/>
 * User: amark
 * Date: 04-Sep-2006
 * Time: 12:27:11
 */
public class ViewQuestionnaireDefinitionController extends ZynapDefaultFormController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        final Long qId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.QUESTIONNAIRE_DEF_ID);
        final QuestionnaireDefinition questionnaireDefinition = questionnaireDefinitionService.findDefinition(qId);
        final QuestionnaireDefinitionWrapper questionnaireDefinitionWrapper = new QuestionnaireDefinitionWrapper(questionnaireDefinition);

        questionnaireHelper.setQuestionnaireState(questionnaireDefinitionWrapper, questionnaireDefinition);

        String activeTab = request.getParameter(ParameterConstants.ACTIVE_TAB);
        if (!StringUtils.hasText(activeTab)) {
            activeTab = DEFINITION_TAB;
        }

        questionnaireDefinitionWrapper.setActiveTab(activeTab);

        SavedURL savedURL = HistoryHelper.getLastUrl(request);
        if (savedURL != null) {
            Map<String, Object> parameters = savedURL.buildParameters();
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                String key = entry.getKey();
                if(key != null && key.startsWith(ControllerConstants.DISPLAY_TAG_PREFIX)) {
                    questionnaireDefinitionWrapper.addDisplayTagValue(key, entry.getValue());
                }
            }
        }
        return questionnaireDefinitionWrapper;
    }


    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors) throws Exception {
        QuestionnaireDefinitionWrapper definitionWrapper = (QuestionnaireDefinitionWrapper) command;
        definitionWrapper.setActiveTab(PREVIEW_TAB);
        if (errors.hasErrors()) {
            definitionWrapper.setEditing(true);
        }
    }

    protected ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {
        QuestionnaireDefinitionWrapper definitionWrapper = (QuestionnaireDefinitionWrapper) command;
        definitionWrapper.setActiveTab(PREVIEW_TAB);
        definitionWrapper.setEditing(false);

        QuestionnaireDefinition definition = definitionWrapper.getQuestionnaireDefinition();
        questionnaireDefinitionService.update(definition);

        ModelAndView view = showForm(request, response, errors);
        final QuestionnaireDefinition questionnaireDefinition = questionnaireDefinitionService.findDefinition(definition.getId());
        definitionWrapper.setQuestionnaireDefinition(questionnaireDefinition);
        questionnaireHelper.setQuestionnaireState(definitionWrapper, questionnaireDefinition);

        return new ModelAndView(view.getViewName(), "command", definitionWrapper);
    }

    protected ModelAndView onCancelInternal(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {
        QuestionnaireDefinitionWrapper definitionWrapper = (QuestionnaireDefinitionWrapper) command;

        QuestionnaireDefinition questionnaireDefinition = questionnaireDefinitionService.findDefinition(definitionWrapper.getId());
        definitionWrapper.setQuestionnaireDefinition(questionnaireDefinition);

        questionnaireHelper.setQuestionnaireState(definitionWrapper, questionnaireDefinition);
        definitionWrapper.setActiveTab(PREVIEW_TAB);
        definitionWrapper.setEditing(false);
        return new ModelAndView(getFormView(), "command", definitionWrapper);
    }

    public void setQuestionnaireDefinitionService(IQueDefinitionService questionnaireDefinitionService) {
        this.questionnaireDefinitionService = questionnaireDefinitionService;
    }

    public void setQuestionnaireHelper(QuestionnaireHelper questionnaireHelper) {
        this.questionnaireHelper = questionnaireHelper;
    }

    private IQueDefinitionService questionnaireDefinitionService;
    private QuestionnaireHelper questionnaireHelper;

    public static final String DEFINITION_TAB = "definition";
    public static final String QUESTIONNAIRES_TAB = "questionnaires";
    private static final String PREVIEW_TAB = "preview";
}

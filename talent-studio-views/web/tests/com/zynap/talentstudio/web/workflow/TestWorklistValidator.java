package com.zynap.talentstudio.web.workflow;

/**
 * User: amark
 * Date: 06-Sep-2006
 * Time: 17:15:06
 */

import com.zynap.talentstudio.questionnaires.IQuestionnaireService;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;
import com.zynap.talentstudio.questionnaires.IQueDefinitionService;

import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.questionnaires.DynamicQuestionAttributeWrapperBean;
import com.zynap.talentstudio.web.questionnaires.QuestionAttributeWrapperBean;
import com.zynap.talentstudio.web.questionnaires.QuestionnaireHelper;
import com.zynap.talentstudio.web.utils.ZynapDbUnitMockControllerTestCase;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;

import org.springframework.validation.BindException;

import java.util.List;

public class TestWorklistValidator extends ZynapDbUnitMockControllerTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        worklistValidator = (WorklistValidator) getBean("worklistValidator");
        questionnaireDefinitionService = (IQueDefinitionService) getBean("questionnaireDefinitionService");
        questionnaireHelper = (QuestionnaireHelper) getBean("questionnaireHelper");
    }

    protected String getDataSetFileName() {
        return "test-validator-data.xml";
    }

    public void testSupports() throws Exception {
        assertTrue(worklistValidator.supports(WorklistWrapper.class));
    }

    public void testValidate() throws Exception {

        final WorklistWrapper wrapper = new WorklistWrapper(null, null, null, false);
        final BindException errors = new BindException(wrapper, ControllerConstants.COMMAND_NAME);

        worklistValidator.validate(wrapper, errors);
        assertEquals(0, errors.getErrorCount());
    }

    public void testValidateAnswersOnComplete() throws Exception {

        final WorklistWrapper wrapper = new WorklistWrapper(null, null, null, false);
        QuestionnaireDefinition questionnaireDefinition = questionnaireDefinitionService.findDefinition(QUE_DEFINITION_ID);
        questionnaireHelper.setQuestionnaireState(wrapper, questionnaireDefinition);

        final String value = "A";
        final List wrappedDynamicAttributes = wrapper.getWrappedDynamicAttributes();
        for (int i = 0; i < wrappedDynamicAttributes.size(); i++) {
            QuestionAttributeWrapperBean questionAttributeWrapperBean = (QuestionAttributeWrapperBean) wrappedDynamicAttributes.get(i);
            if (questionAttributeWrapperBean instanceof DynamicQuestionAttributeWrapperBean) {
                final DynamicQuestionAttributeWrapperBean dynamicQuestionAttributeWrapperBean = (DynamicQuestionAttributeWrapperBean) questionAttributeWrapperBean;
                dynamicQuestionAttributeWrapperBean.addDynamicLineItem();
                final List questionWrappers = dynamicQuestionAttributeWrapperBean.getQuestionWrappers();
                ((AttributeWrapperBean) questionWrappers.get(0)).setValue(value);
                ((AttributeWrapperBean) questionWrappers.get(1)).setValue(value);
            }
        }

        final BindException errors = new BindException(wrapper, ControllerConstants.COMMAND_NAME);
        worklistValidator.validateAnswersOnComplete(wrapper, errors);

        // 4 expected
        assertEquals(4, errors.getErrorCount());

        // 2 of the dynamic line items are number questions and we have set value to be text
        assertEquals("not.a.number", errors.getFieldError("wrappedDynamicAttributes[9].questionWrappers[0].value").getCode());
        assertEquals("not.a.number", errors.getFieldError("wrappedDynamicAttributes[9].questionWrappers[1].value").getCode());

        // 2 of the dynamic line items are date questions and the value is not set
        assertEquals("mandatory.has.no.value", errors.getFieldError("wrappedDynamicAttributes[11].questionWrappers[0].date").getCode());
        assertEquals("mandatory.has.no.value", errors.getFieldError("wrappedDynamicAttributes[11].questionWrappers[1].date").getCode());
    }

    private WorklistValidator worklistValidator;
    private IQueDefinitionService questionnaireDefinitionService;
    private QuestionnaireHelper questionnaireHelper;

    private static final Long QUE_DEFINITION_ID = new Long(43);
}
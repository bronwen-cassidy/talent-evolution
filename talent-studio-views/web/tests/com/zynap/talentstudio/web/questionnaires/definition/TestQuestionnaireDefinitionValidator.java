package com.zynap.talentstudio.web.questionnaires.definition;

/**
 * User: amark
 * Date: 15-Aug-2005
 * Time: 14:09:39
 */

import com.zynap.talentstudio.questionnaires.LineItem;
import com.zynap.talentstudio.questionnaires.MultiQuestionItem;
import com.zynap.talentstudio.questionnaires.QuestionGroup;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;
import com.zynap.talentstudio.questionnaires.QuestionAttribute;
import com.zynap.talentstudio.web.AbstractValidatorTestCase;
import com.zynap.talentstudio.web.questionnaires.QuestionGroupWrapper;
import com.zynap.talentstudio.web.questionnaires.LineItemWrapper;
import com.zynap.talentstudio.web.questionnaires.QuestionAttributeWrapperBean;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.util.spring.BindUtils;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.List;

public class TestQuestionnaireDefinitionValidator extends AbstractValidatorTestCase {

    QuestionnaireDefinitionWrapper definitionWrapper;
    QuestionnaireDefinitionValidator questionnaireDefinitionValidator;

    protected void setUp() throws Exception {
        super.setUp();

        questionnaireDefinitionValidator = new QuestionnaireDefinitionValidator();
        definitionWrapper = new QuestionnaireDefinitionWrapper();
        definitionWrapper.setQuestionnaireState(null, new ArrayList(), new ArrayList(), null);
    }

    public void testValidate() throws Exception {

        final DataBinder dataBinder = createDataBinder(definitionWrapper);
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        BindUtils.addPropertyValue(propertyValues, LABEL_FIELD_NAME, "test");
        dataBinder.bind(propertyValues);

        final Errors errors = getErrors(dataBinder);
        questionnaireDefinitionValidator.validate(definitionWrapper, errors);

        assertNoErrors(errors);
    }

    public void testValidateNoValues() throws Exception {

        final DataBinder dataBinder = createDataBinder(definitionWrapper);

        final Errors errors = getErrors(dataBinder);
        questionnaireDefinitionValidator.validate(definitionWrapper, errors);

        assertErrorCount(1, errors);
        assertErrorCode(errors, "error.label.required", LABEL_FIELD_NAME);
    }

    public void testValidateLineItemLength () throws Exception {
        List groups = new ArrayList();
        List wrappedAttributes = new ArrayList();

        StringBuffer longLabel = new StringBuffer(2001);
        for(int i = 0; i < 2002; i++) {
            longLabel.append("a");
        }

        QuestionGroup group = new QuestionGroup("test", true);
        QuestionGroupWrapper groupWrapper = new QuestionGroupWrapper(group);
        MultiQuestionItem multiQuestion = new MultiQuestionItem("multiquestion");

        LineItem lineItem = new LineItem(longLabel.toString(), false);
        multiQuestion.addLineItem(lineItem);
        group.addMultiQuestion(multiQuestion);

        LineItemWrapper lineItemWrapper = new LineItemWrapper(multiQuestion);
        QuestionAttribute attribute = new QuestionAttribute(new DynamicAttribute("1", DynamicAttribute.DA_TYPE_TEXTFIELD), 12, null, null, null, "TEXT", false);
        lineItem.addQuestion(attribute);
        QuestionAttributeWrapperBean questionWrapper = new QuestionAttributeWrapperBean(attribute);
        lineItemWrapper.addLineItemQuestion(questionWrapper);
        lineItemWrapper.initialiseState();
        
        wrappedAttributes.add(lineItemWrapper);

        groupWrapper.setWrappedDynamicAttributes(wrappedAttributes);
        groups.add(groupWrapper);
        final Long queDefId = new Long(12);
        definitionWrapper.setQuestionnaireDefinition(new QuestionnaireDefinition(queDefId, "hello world"));
        definitionWrapper.setQuestionnaireState(null, groups, wrappedAttributes, queDefId);

        final DataBinder dataBinder = createDataBinder(definitionWrapper);
        final Errors errors = getErrors(dataBinder);
        questionnaireDefinitionValidator.validate(definitionWrapper, errors);
        // should have errors
        assertEquals(1, errors.getErrorCount());
        assertEquals("questionnaireGroups[0].wrappedDynamicAttributes[0].grid[0][0].lineItemLabel", errors.getFieldError().getField());

    }

    public void testSupports() throws Exception {
        assertTrue(questionnaireDefinitionValidator.supports(QuestionnaireDefinitionWrapper.class));
    }

    private static final String LABEL_FIELD_NAME = "label";
}
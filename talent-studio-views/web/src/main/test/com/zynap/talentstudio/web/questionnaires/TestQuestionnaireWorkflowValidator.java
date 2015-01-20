package com.zynap.talentstudio.web.questionnaires;

/**
 * User: amark
 * Date: 15-Aug-2005
 * Time: 13:32:20
 */

import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.questionnaires.QuestionnaireDTO;
import com.zynap.talentstudio.questionnaires.DefinitionDTO;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;
import com.zynap.talentstudio.web.AbstractValidatorTestCase;
import com.zynap.util.spring.BindUtils;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class TestQuestionnaireWorkflowValidator extends AbstractValidatorTestCase {

    QuestionnaireWorkflowValidator questionnaireWorkflowValidator;
    QuestionnaireWorkflowWrapperBean questionnaireWorkflowWrapperBean;

    protected void setUp() throws Exception {
        super.setUp();

        questionnaireWorkflowValidator = new QuestionnaireWorkflowValidator();
        final QuestionnaireWorkflow workflow = new QuestionnaireWorkflow();
        workflow.setQuestionnaireDefinition(new QuestionnaireDefinition(new Long(-3), "label"));
        questionnaireWorkflowWrapperBean = new QuestionnaireWorkflowWrapperBean(workflow);
    }

    public void testSupports() throws Exception {
        assertTrue(questionnaireWorkflowValidator.supports(QuestionnaireWorkflowWrapperBean.class));
    }

    public void testValidate() throws Exception {

        final DataBinder binder = createDataBinder(questionnaireWorkflowWrapperBean);
        questionnaireWorkflowWrapperBean.setLabel("test");
        questionnaireWorkflowWrapperBean.setExpiryDate(new Date());
        questionnaireWorkflowWrapperBean.setPopulationId(new Long(-1));
        

        Errors errors = binder.getBindingResult();
        questionnaireWorkflowValidator.validate(questionnaireWorkflowWrapperBean, errors);

        assertNoErrors(errors);
    }

    public void testValidateNoValues() throws Exception {

        final DataBinder binder = createDataBinder(questionnaireWorkflowWrapperBean);

        final MutablePropertyValues propertyValues = new MutablePropertyValues();
        binder.bind(propertyValues);

        Errors errors = binder.getBindingResult();
        questionnaireWorkflowValidator.validate(questionnaireWorkflowWrapperBean, errors);
        assertErrorCount(2, errors);

        assertErrorCode(errors, "error.label.required", LABEL_FIELD_NAME);
        assertErrorCode(errors, "error.required.field", POPULATION_FIELD_NAME);
    }

    public void testValidateInvalidExpiryDate() throws Exception {

        final DataBinder binder = createDataBinder(questionnaireWorkflowWrapperBean);

        final MutablePropertyValues propertyValues = new MutablePropertyValues();
        BindUtils.addPropertyValue(propertyValues, LABEL_FIELD_NAME, "test");
        BindUtils.addPropertyValue(propertyValues, POPULATION_FIELD_NAME, new Long(-1));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 1900);
        final Date date = calendar.getTime();
        BindUtils.addPropertyValue(propertyValues, EXPIRY_DATE_FIELD_NAME, date);
        binder.bind(propertyValues);

        // check date was set properly
        assertEquals(date, questionnaireWorkflowWrapperBean.getExpiryDate());

        Errors errors = binder.getBindingResult();
        questionnaireWorkflowValidator.validate(questionnaireWorkflowWrapperBean, errors);
        assertErrorCount(1, errors);

        assertErrorCode(errors, "error.complete.date.before.today", EXPIRY_DATE_FIELD_NAME);
    }

    private static final String LABEL_FIELD_NAME = "label";
    private static final String POPULATION_FIELD_NAME = "populationId";
    private static final String EXPIRY_DATE_FIELD_NAME = "expiryDate";
}
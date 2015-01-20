package com.zynap.talentstudio.web.perfomance;

/**
 * User: amark
 * Date: 04-Oct-2006
 * Time: 10:44:54
 */

import com.zynap.talentstudio.web.AbstractValidatorTestCase;
import com.zynap.talentstudio.web.common.ControllerConstants;

import org.springframework.validation.BindException;

import java.util.Calendar;
import java.util.Date;

public class TestPerformanceReviewValidator extends AbstractValidatorTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        performanceReviewValidator = new PerformanceReviewValidator();
    }

    public void testSupports() throws Exception {
        assertTrue(performanceReviewValidator.supports(PerformanceReviewWrapper.class));
    }

    public void testValidate() throws Exception {

        final PerformanceReviewWrapper performanceReviewWrapper = new PerformanceReviewWrapper(null, null);

        BindException errors = new BindException(performanceReviewWrapper, ControllerConstants.COMMAND_NAME);
        performanceReviewValidator.validate(performanceReviewWrapper, errors);
        assertEquals(4, errors.getErrorCount());

        assertErrorCode(errors, "error.label.required", "label");
        assertErrorCode(errors, "error.required.field", "populationId");
        assertErrorCode(errors, "error.required.field", "managerQuestionnaireDefinitionId");
        assertErrorCode(errors, "error.required.field", "generalQuestionnaireDefinitionId");
    }

    public void testValidateExpiryDate() throws Exception {

        final PerformanceReviewWrapper performanceReviewWrapper = new PerformanceReviewWrapper(null, null);
        performanceReviewWrapper.setLabel("label");
        performanceReviewWrapper.setPopulationId(new Long(-1));
        performanceReviewWrapper.setManagerQuestionnaireDefinitionId(new Long(-1));
        performanceReviewWrapper.setGeneralQuestionnaireDefinitionId(new Long(-1));

        final Calendar instance = Calendar.getInstance();
        instance.set(Calendar.YEAR, 1977);
        Date expiryDate = instance.getTime();
        performanceReviewWrapper.setExpiryDate(expiryDate);

        BindException errors = new BindException(performanceReviewWrapper, ControllerConstants.COMMAND_NAME);
        performanceReviewValidator.validate(performanceReviewWrapper, errors);
        assertEquals(1, errors.getErrorCount());

        assertErrorCode(errors, "error.complete.date.before.today", "expiryDate");
    }

    private PerformanceReviewValidator performanceReviewValidator;
}
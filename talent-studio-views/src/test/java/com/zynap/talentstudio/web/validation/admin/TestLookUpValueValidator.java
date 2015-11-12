package com.zynap.talentstudio.web.validation.admin;

/**
 * User: amark
 * Date: 14-Jun-2005
 * Time: 17:11:57
 */

import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.web.common.ControllerConstants;
import junit.framework.TestCase;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

public class TestLookUpValueValidator extends TestCase {

    protected void setUp() throws Exception {
        lookUpValueValidator = new LookUpValueValidator();
    }

    public void testSupports() throws Exception {
        assertTrue(lookUpValueValidator.supports(LookupValue.class));
    }

    public void testValidate() throws Exception {

        LookupValue lookupValue = new LookupValue();
        Errors errors = new BindException(lookupValue, ControllerConstants.COMMAND_NAME);
        lookUpValueValidator.validate(lookupValue, errors);

        assertEquals(2, errors.getErrorCount());
        assertEquals("error.required.field", errors.getFieldError("label").getCode());
        assertEquals("error.description.required", errors.getFieldError("description").getCode());
    }

    public void testBlankValues() throws Exception {

        // try with blank fields
        LookupValue lookupValue = new LookupValue();
        lookupValue.setLabel("");
        lookupValue.setDescription("");

        Errors errors = new BindException(lookupValue, ControllerConstants.COMMAND_NAME);
        lookUpValueValidator.validate(lookupValue, errors);

        assertEquals(2, errors.getErrorCount());
        assertEquals("error.required.field", errors.getFieldError("label").getCode());
        assertEquals("error.description.required", errors.getFieldError("description").getCode());
    }

    public void testLengthValidation() throws Exception {

        // try with fields that are too long
        LookupValue lookupValue = new LookupValue();
        final String label = StringUtils.rightPad("", 101, "*");
        lookupValue.setLabel(label);
        final String desc = StringUtils.rightPad("", 4001, "*");
        lookupValue.setDescription(desc);

        Errors errors = new BindException(lookupValue, ControllerConstants.COMMAND_NAME);
        lookUpValueValidator.validate(lookupValue, errors);

        assertEquals(2, errors.getErrorCount());
        assertEquals("error.max.length.exceeded.100", errors.getFieldError("label").getCode());
        assertEquals("error.max.length.exceeded.4000", errors.getFieldError("description").getCode());
    }

    public void testNegativeSortOrder() throws Exception {

        LookupValue lookupValue = new LookupValue();
        lookupValue.setLabel("label");
        lookupValue.setDescription("description");
        lookupValue.setSortOrder(-1);

        Errors errors = new BindException(lookupValue, ControllerConstants.COMMAND_NAME);
        lookUpValueValidator.validate(lookupValue, errors);

        assertEquals(1, errors.getErrorCount());
        assertEquals("field.is.numeric", errors.getFieldError("sortOrder").getCode());

        // set sort order to zero - should now pass validation
        lookupValue.setSortOrder(0);
        errors = new BindException(lookupValue, ControllerConstants.COMMAND_NAME);
        lookUpValueValidator.validate(lookupValue, errors);

        assertFalse(errors.hasErrors());
    }

    LookUpValueValidator lookUpValueValidator;
}
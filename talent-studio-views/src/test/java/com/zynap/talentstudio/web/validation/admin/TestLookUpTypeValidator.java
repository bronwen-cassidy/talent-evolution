package com.zynap.talentstudio.web.validation.admin;

/**
 * User: amark
 * Date: 14-Jun-2005
 * Time: 17:11:47
 */

import com.zynap.talentstudio.common.lookups.LookupType;
import com.zynap.talentstudio.web.common.ControllerConstants;
import junit.framework.TestCase;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

public class TestLookUpTypeValidator extends TestCase {

    protected void setUp() throws Exception {
        lookUpTypeValidator = new LookUpTypeValidator();
    }

    public void testSupports() throws Exception {
        assertTrue(lookUpTypeValidator.supports(LookupType.class));
    }

    public void testValidate() throws Exception {

        LookupType lookupType = new LookupType();
        Errors errors = new BindException(lookupType, ControllerConstants.COMMAND_NAME);
        lookUpTypeValidator.validate(lookupType, errors);

        assertEquals(2, errors.getErrorCount());
        assertEquals("error.label.required", errors.getFieldError("label").getCode());
        assertEquals("error.description.required", errors.getFieldError("description").getCode());
    }

    public void testBlankValues() throws Exception {

        // try with blank fields
        LookupType lookupType = new LookupType();
        lookupType.setLabel("");
        lookupType.setDescription("");

        Errors errors = new BindException(lookupType, ControllerConstants.COMMAND_NAME);
        lookUpTypeValidator.validate(lookupType, errors);

        assertEquals(2, errors.getErrorCount());
        assertEquals("error.label.required", errors.getFieldError("label").getCode());
        assertEquals("error.description.required", errors.getFieldError("description").getCode());
    }

    public void testLengthValidation() throws Exception {

        // try with fields that are too long
        LookupType lookupType = new LookupType();
        final String label = StringUtils.rightPad("", 81, "*");
        lookupType.setLabel(label);
        final String desc = StringUtils.rightPad("", 4001, "*");
        lookupType.setDescription(desc);

        Errors errors = new BindException(lookupType, ControllerConstants.COMMAND_NAME);
        lookUpTypeValidator.validate(lookupType, errors);

        assertEquals(2, errors.getErrorCount());
        assertEquals("error.max.length.exceeded.80", errors.getFieldError("label").getCode());
        assertEquals("error.max.length.exceeded.4000", errors.getFieldError("description").getCode());
    }

    LookUpTypeValidator lookUpTypeValidator;
}
/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.utils;

import junit.framework.TestCase;

import com.zynap.web.mocks.MockErrors;

import org.apache.commons.lang.StringUtils;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

public class TestZynapValidationUtils extends TestCase {

    public void testValidateNumbersOnly() throws Exception {
        String value = "12345678";
        Errors errors = new MockErrors(FIELD_NAME, value);
        ZynapValidationUtils.validateNumbersOnly(errors, FIELD_NAME, "test", "nothing");
        assertEquals(0, errors.getAllErrors().size());
    }

    public void testValidateNumbersOnly_Fails() throws Exception {
        String value = "123ee678";
        Errors errors = new MockErrors(FIELD_NAME, value);
        ZynapValidationUtils.validateNumbersOnly(errors, FIELD_NAME, "test", "nothing");
        assertEquals(2, errors.getAllErrors().size());
    }

    public void testValidateNumbersOnly_EmptyString() throws Exception {
        String value = "";
        Errors errors = new MockErrors(FIELD_NAME, value);
        ZynapValidationUtils.validateNumbersOnly(errors, FIELD_NAME, "test", "nothing");
        assertEquals(0, errors.getAllErrors().size());
    }

    public void testValidateNumbersOnlyNullString() throws Exception {
        String value = null;
        Errors errors = new MockErrors(FIELD_NAME, value);
        ZynapValidationUtils.validateNumbersOnly(errors, FIELD_NAME, "test", "nothing");
        assertEquals(0, errors.getAllErrors().size());
    }

    public void testRejectGreater80() throws Exception {

        final String defaultMessage = "message";
        final String errorCode = "expected";

        Errors errors = new MockErrors(FIELD_NAME, StringUtils.repeat("*", 81));
        ZynapValidationUtils.rejectGreater80(errors, FIELD_NAME, errorCode, defaultMessage);
        final FieldError fieldError = errors.getFieldError(FIELD_NAME);
        assertEquals(errorCode, fieldError.getCode());
        assertEquals(defaultMessage, fieldError.getDefaultMessage());

        errors = new MockErrors(FIELD_NAME, StringUtils.repeat("*", 80));
        ZynapValidationUtils.rejectGreater80(errors, FIELD_NAME, errorCode, defaultMessage);
        assertEquals(0, errors.getErrorCount());

        // test with null
        errors = new MockErrors(FIELD_NAME, null);
        ZynapValidationUtils.rejectGreater80(errors, FIELD_NAME, errorCode, defaultMessage);
        assertEquals(0, errors.getErrorCount());
    }

    private static final String FIELD_NAME = "telephone";
}
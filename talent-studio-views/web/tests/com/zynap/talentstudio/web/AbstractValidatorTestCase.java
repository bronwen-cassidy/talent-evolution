/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web;

import com.zynap.talentstudio.ZynapTestCase;
import com.zynap.util.spring.BindUtils;
import com.zynap.web.controller.ZynapDefaultFormController;

import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public abstract class AbstractValidatorTestCase extends ZynapTestCase {

    protected final void assertErrorCode(Errors errors, String expectedCode, String fieldName) {
        assertEquals(expectedCode, getFieldErrorCode(errors, fieldName));
    }

    protected final void assertErrorCode(Errors errors, String expectedCode) {
        assertEquals(expectedCode, errors.getGlobalError().getCode());
    }

    protected final String getFieldErrorCode(Errors errors, String fieldName) {
        return getFieldError(errors, fieldName).getCode();
    }

    protected final FieldError getFieldError(Errors errors, String fieldName) {
        return errors.getFieldError(fieldName);
    }

    protected final void assertNoErrors(Errors errors) {
        assertEquals(0, errors.getErrorCount());
    }

    protected final void assertErrorCount(int count, Errors errors) {
        assertEquals(count, errors.getErrorCount());
    }

    protected final DataBinder createDataBinder(Object o) {
        return BindUtils.createBinder(o, ZynapDefaultFormController.DEFAULT_COMMAND_NAME);
    }
}

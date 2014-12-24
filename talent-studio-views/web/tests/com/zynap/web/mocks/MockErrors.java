/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.web.mocks;

import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class or Interface description.
 * 
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class MockErrors implements Errors {

    public MockErrors(String objectName) {
        _objectName = objectName;
        _errors = new ArrayList();
    }

    public MockErrors(String objectName, String fieldValue) {
        _objectName = objectName;
        _fieldValue = fieldValue;
        _errors = new ArrayList();
    }    

    public String getObjectName() {
        return _objectName;
    }

    public void pushNestedPath(String subPath) {
    }

    public void popNestedPath() throws IllegalStateException {
    }

    public void reject(String errorCode, String defaultMessage) {
    }

    public void reject(String errorCode, Object[] errorArgs, String defaultMessage) {
    }


    public void reject(String errorCode) {

    }

    public void rejectValue(String field, String errorCode) {

    }

    public void addAllErrors(Errors errors) {

    }

    public boolean hasFieldErrors() {
        return false;
    }

    public int getFieldErrorCount() {
        return 0;
    }

    public List getFieldErrors() {
        return null;
    }

    public FieldError getFieldError() {
        return null;
    }

    public Class getFieldType(String field) {
        return null;
    }

    public void rejectValue(String field, String errorCode, String defaultMessage) {
        FieldError fieldError = new FieldError(_objectName, field, field, false, new String[]{errorCode}, null, defaultMessage);
        _errors.add(fieldError);
    }

    public void rejectValue(String field, String errorCode, Object[] errorArgs, String defaultMessage) {
    }

    public boolean hasErrors() {
        return !_errors.isEmpty();
    }

    public int getErrorCount() {
        return _errors.size();
    }

    public List getAllErrors() {
        return _errors;
    }

    public boolean hasGlobalErrors() {
        return false;
    }

    public int getGlobalErrorCount() {
        return 0;
    }

    public List getGlobalErrors() {
        return null;
    }

    public ObjectError getGlobalError() {
        return null;
    }

    public boolean hasFieldErrors(String field) {
        return false;
    }

    public int getFieldErrorCount(String field) {
        return 0;
    }

    public List getFieldErrors(String field) {
        return null;
    }

    public FieldError getFieldError(String field) {
		for (Iterator it = _errors.iterator(); it.hasNext();) {
			Object error = it.next();
			if (error instanceof FieldError) {
				FieldError fe = (FieldError) error;
				if (isMatchingFieldError(field, fe)) {
					return fe;
				}
			}
		}
		return null;
    }

    protected boolean isMatchingFieldError(String field, FieldError fieldError) {
		return (field.equals(fieldError.getField()) ||
				(field.endsWith("*") && fieldError.getField().startsWith(field.substring(0, field.length() - 1))));
	}

    public Object getFieldValue(String field) {
        return _fieldValue;
    }

    public void setNestedPath(String nestedPath) {
    }

    public String getNestedPath() {
        return null;
    }

    private String _objectName;
    private List _errors;
    private String _fieldValue;
}

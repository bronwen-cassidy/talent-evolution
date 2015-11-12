/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.utils;

import com.zynap.domain.IDomainObject;
import com.zynap.talentstudio.util.IDomainUtils;

import org.apache.commons.beanutils.BeanUtils;

import org.springframework.validation.Errors;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public final class ZynapValidationUtils {

    public static void validateNumbersOnly(Errors errors, String field, String errorCode, String defaultMessage) {
        Object value = errors.getFieldValue(field);
        if (value == null) return;
        char[] numbers = value.toString().toCharArray();
        for (int i = 0; i < numbers.length; i++) {
            final char character = numbers[i];
            if (!Character.isDigit(character)) {
                errors.rejectValue(field, errorCode, defaultMessage);
            }
        }
    }

    public static void rejectGreater200(Errors errors, String field, String errorCode, String defaultMessage) {
        rejectIfGreater(errors, field, 200, errorCode, defaultMessage);
    }

    public static void rejectGreater100(Errors errors, String field, String errorCode, String defaultMessage) {
        rejectIfGreater(errors, field, 100, errorCode, defaultMessage);
    }

    public static void rejectGreater80(Errors errors, String field, String errorCode, String defaultMessage) {
        rejectIfGreater(errors, field, 80, errorCode, defaultMessage);
    }

    public static void rejectGreater4000(Errors errors, String field, String errorCode, String defaultMessage) {
        rejectIfGreater(errors, field, 4000, errorCode, defaultMessage);
    }

    public static void rejectGreater1000(Errors errors, String field, String errorCode, String defaultMessage) {
        rejectIfGreater(errors, field, 1000, errorCode, defaultMessage);
    }

    public static void rejectGreater255(Errors errors, String field, String errorCode, String defaultMessage) {
        rejectIfGreater(errors, field, 255, errorCode, defaultMessage);
    }

    public static boolean rejectIfNull(Errors errors, String field, Object value, String errorCode, String defaultMessage) {
        if (value == null) {
            errors.rejectValue(field, errorCode, defaultMessage);
            return false;
        }

        return true;
    }

    /**
     * Checks a field on the command object for nullness, if it is null it is rejected.
     *
     * @param errors The errors object which gets the rejected value if it is null
     * @param field The field is the field which we are checking for nullness
     * @param errorCode The error code to return, found in messages.properties
     * @param defaultMessage The message to use if the message.properties does not contain the errorcode
     * @param command The object for whcih we are determining the value of the field
     * @return false if the value extracted from the command object is null, true is it is not null.
     */
    public static boolean rejectIfNull(Errors errors, String field, String errorCode, String defaultMessage, Object command) {
        Object value = getValue(command, field);
        return rejectIfNull(errors, field, value, errorCode, defaultMessage);
    }

    public static void rejectIfUnassigned(Errors errors, String field, String errorCode, String defaultMessage, IDomainObject domainObject) {

        boolean rejected = false;
        final Object value = getValue(domainObject, field);
        if (value != null) {
            Long id = Long.valueOf(value.toString());
            if (!IDomainUtils.isValidId(id)) {
                rejected = true;
            }
        } else {
            rejected = true;
        }

        if (rejected) errors.rejectValue(field, errorCode, defaultMessage);
    }

    private static void rejectIfGreater(Errors errors, String field, final int threshold, String errorCode, String defaultMessage) {
        Object value = errors.getFieldValue(field);
        if (value == null) return;
        if (value.toString().length() > threshold) {
            errors.rejectValue(field, errorCode, defaultMessage);
        }
    }

    private static Object getValue(Object command, String field) {
        Object value = null;
        try {
            value = BeanUtils.getNestedProperty(command, field);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }
}

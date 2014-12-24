/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.subjects;

import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.web.common.validation.NodeValidator;
import com.zynap.talentstudio.web.organisation.attributes.validators.ErrorMessageHandler;
import com.zynap.talentstudio.web.organisation.attributes.validators.ImageAttributeValueValidator;
import com.zynap.web.utils.ZynapValidationUtils;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class SubjectValidator extends NodeValidator {

    public final boolean supports(Class clazz) {
        return SubjectWrapperBean.class.isAssignableFrom(clazz);
    }

    public void validate(Object obj, Errors errors) {
        validateRequiredValues((SubjectWrapperBean) obj, errors);
        validateCoreValues((SubjectWrapperBean) obj, errors);
        validateLoginValues((SubjectWrapperBean) obj, errors);
        validateSubjectPrimaryAssociations((SubjectWrapperBean) obj, errors);
        validateSubjectSecondaryAssociations((SubjectWrapperBean) obj, errors);
    }

    public void validateCoreValues(SubjectWrapperBean obj, Errors errors) {
        if (obj.getFile() != null) {
            ErrorMessageHandler errorMessageHandler = ImageAttributeValueValidator.validateFileEnding(obj.getFile().getFileExtension());
            if (errorMessageHandler != null) {
                errors.rejectValue(PICTURE_FIELD_NAME, errorMessageHandler.getErrorKey(), "The file provided is not an image");
            }
        }

        // todo verify the size of the image file in bytes

        Date dateOfBirth = obj.getDateOfBirth();

        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        Date today = calendar.getTime();

        if (dateOfBirth != null && (today.before(dateOfBirth) || today.equals(dateOfBirth))) {
            errors.rejectValue(AnalysisAttributeHelper.DOB_ATTR, "error.date.before.today", "Error date must be before today");
        }
    }

    public void validateRequiredValues(SubjectWrapperBean obj, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, FIRST_NAME_FIELD, "error.firstname.missing", "'First Name' is a required field.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, SECOND_NAME_FIELD, "error.lastname.missing", "'Last Name' is a required field.");
        ZynapValidationUtils.rejectGreater100(errors, FIRST_NAME_FIELD, "error.firstname.maxlength", "Maximum length for 'First Name' is 100 characters.");
        ZynapValidationUtils.rejectGreater100(errors, SECOND_NAME_FIELD, "error.lastname.maxlength", "Maximum length for 'Last Name' is 100 characters.");
        ZynapValidationUtils.rejectGreater100(errors, PREF_NAME_FIELD, "error.givenname.maxlength", "Maximum length for 'Preferred Given Name' is 100 characters.");
    }

    public void validateLoginValues(SubjectWrapperBean wrapper, Errors errors) {
        if (wrapper.isCanLogIn() && wrapper.isLoginChange()) {
            validateUserName(wrapper, errors);
            validatePassword(wrapper, errors);
        }
    }

    protected void validatePassword(SubjectWrapperBean wrapper, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, PASSWORD_FIELD, "error.password.missing", "'Password' is required if the subject can login.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, REPEATED_PASSWORD_FIELD, "error.repeat.password.missing", "'Repeat Password' is a required field.");

        // Validate that the two password fields match
        final String password = wrapper.getUserWrapper().getLoginInfo().getPassword();
        final String repeatedPassword = wrapper.getUserWrapper().getLoginInfo().getRepeatedPassword();
        if (StringUtils.hasText(password) && StringUtils.hasText(repeatedPassword) && !password.equals(repeatedPassword)) {
            errors.rejectValue(REPEATED_PASSWORD_FIELD, "error.password.mismatch", null, "Passwords do not match.");
        }
    }

    protected void validateUserName(SubjectWrapperBean subjectWrapperBean, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, USER_NAME_FIELD, "error.username.missing", "'User Name' is required if the subject can login.");
    }

    public static final String TITLE_FIELD = "coreDetail.title";
    public static final String FIRST_NAME_FIELD = "coreDetail.firstName";
    public static final String SECOND_NAME_FIELD = "coreDetail.secondName";
    public static final String PREF_NAME_FIELD = "coreDetail.prefGivenName";
    public static final String USER_NAME_FIELD = "userWrapper.loginInfo.username";
    public static final String PASSWORD_FIELD = "userWrapper.loginInfo.password";
    public static final String REPEATED_PASSWORD_FIELD = "userWrapper.loginInfo.repeatedPassword";
    private static final String PICTURE_FIELD_NAME = "file";
}

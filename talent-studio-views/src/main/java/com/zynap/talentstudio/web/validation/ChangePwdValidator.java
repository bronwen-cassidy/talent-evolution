package com.zynap.talentstudio.web.validation;

import com.zynap.domain.admin.UserPassword;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Created by IntelliJ IDEA.
 * User: aandersson
 * Date: 18-Mar-2004
 * Time: 11:47:06
 * Validator that checks if user has supplied their old password, and the new password (repeated).
 * <br> Does not check that the old password is valid - merely checks that the value have been provided.
 * <br> Does however check that the new password and the repeated new password are the same if they have both been provided.
 */
public class ChangePwdValidator implements Validator {

    public boolean supports(Class aClass) {
        return aClass.equals(UserPassword.class);
    }

    /**
     * Check that the old password has been provided,
     * and that the new password and the repeated new password have been supplied and that they match.
     *
     * @param o The object to validate.
     * @param errors The Errors collection to add error messages to
     */
    public void validate(Object o, Errors errors) {
        UserPassword pwd = (UserPassword) o;

        validateNewPassword(pwd, errors);

    }

    public void validateOldPassword(UserPassword pwd, Errors errors) {
        final String oldPassword = pwd.getOldPassword();
        final boolean oldPasswordProvided = StringUtils.hasText(oldPassword);
        if (!oldPasswordProvided) {
            errors.rejectValue("oldPassword", "error.oldpassword.missing", null, "'Old Password' is a required field.");
        }
    }

    /**
     * Check that the new password and the repeated new password have been supplied and that they match.
     * @param pwd The UserPassword
     * @param errors The Errors collection to add error messages to
     */
    public void validateNewPassword(UserPassword pwd, Errors errors) {
        final String newPassword = pwd.getNewPassword();
        final boolean newPasswordProvided = StringUtils.hasText(newPassword);
        if (!newPasswordProvided) {
            errors.rejectValue("newPassword", "error.newpassword.missing", null, "'New Password' is a required field.");
        }

        final String newRepeatPassword = pwd.getNewPasswordAgain();
        final boolean newRepeatPasswordProvided = StringUtils.hasText(newRepeatPassword);
        if (!newRepeatPasswordProvided) {
            errors.rejectValue("newPasswordAgain", "error.repeat.newpassword.missing", null, "'Repeat New Password' is a required field.");
        }

        if (newPasswordProvided && newRepeatPasswordProvided && !newPassword.equals(newRepeatPassword)) {
            errors.rejectValue("newPassword", "error.password.mismatch", null, "Passwords do not match.");
        }
    }
}

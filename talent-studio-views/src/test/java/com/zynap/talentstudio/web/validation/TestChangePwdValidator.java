package com.zynap.talentstudio.web.validation;

/**
 * User: amark
 * Date: 23-Mar-2005
 * Time: 15:10:04
 */

import com.zynap.domain.admin.UserPassword;
import com.zynap.talentstudio.web.common.ControllerConstants;
import junit.framework.TestCase;
import org.springframework.validation.BindException;

public class TestChangePwdValidator extends TestCase {

    protected void setUp() throws Exception {
        changePwdValidator = new ChangePwdValidator();
    }

    public void testValidate() throws Exception {

        UserPassword pwd = new UserPassword();
        final BindException errors = new BindException(pwd, ControllerConstants.COMMAND_NAME);
        changePwdValidator.validate(pwd, errors);

        // should fail validation due to missing old password, new password and confirm password
        assertEquals(2, errors.getErrorCount());        
        assertEquals(1, errors.getFieldErrorCount(NEW_PWD_FIELD_NAME));
        assertEquals(1, errors.getFieldErrorCount(CONFIRM_NEW_PWD_FIELD_NAME));
    }

    public void testValidateNewPasswordMismatch() throws Exception {

        UserPassword pwd = new UserPassword();
        pwd.setOldPassword("oldpwd");
        pwd.setNewPassword("newpwd");
        pwd.setNewPasswordAgain("newpwd2");
        final BindException errors = new BindException(pwd, ControllerConstants.COMMAND_NAME);
        changePwdValidator.validate(pwd, errors);

        // should fail validation due to new passwords being different
        assertEquals(1, errors.getErrorCount());
        assertEquals(1, errors.getFieldErrorCount(NEW_PWD_FIELD_NAME));
    }

    ChangePwdValidator changePwdValidator;

    private static final String OLD_PWD_FIELD_NAME = "oldPassword";
    private static final String NEW_PWD_FIELD_NAME = "newPassword";
    private static final String CONFIRM_NEW_PWD_FIELD_NAME = "newPasswordAgain";
}
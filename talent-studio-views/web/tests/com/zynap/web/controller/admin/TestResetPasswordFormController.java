package com.zynap.web.controller.admin;

import com.zynap.domain.admin.LoginInfo;
import com.zynap.domain.admin.User;
import com.zynap.domain.admin.UserPassword;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.CoreDetail;
import com.zynap.talentstudio.security.roles.IRoleManager;
import com.zynap.talentstudio.security.roles.Role;
import com.zynap.talentstudio.security.rules.Config;
import com.zynap.talentstudio.security.rules.Rule;
import com.zynap.talentstudio.security.users.IUserDao;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Tests for reset password controller.
 * User: amark
 * Date: 29-Mar-2005
 * Time: 08:29:14
 */
public class TestResetPasswordFormController extends ZynapMockControllerTest {

    protected void setUp() throws Exception {

        super.setUp();
        resetPasswordFormController = (ResetPasswordFormController) applicationContext.getBean("resetMyPasswordController");
        IRoleManager roleManager = (IRoleManager) applicationContext.getBean("roleManager");

        // create user for testing purposes
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(USER_NAME);
        loginInfo.setPassword(PASSWORD);

        CoreDetail coreDetail = new CoreDetail("Mrs", "Bandera", "Nadara");

        Collection roles = roleManager.getActiveAccessRoles();
        testUser = new User(loginInfo, coreDetail);
        for (Iterator iterator = roles.iterator(); iterator.hasNext();) {
            Role role = (Role) iterator.next();
            testUser.addRole(role);
        }

        IUserService userService = resetPasswordFormController.getUserService();
        userService.create(testUser);

        // check user has password history
        checkPasswordHistory(testUser.getId(), 1);
    }

    public void testFormBackingObject() throws Exception {

        UserPassword userPassword = getFormBackingObject();
        assertEquals(testUser.getId(), userPassword.getUserId());
    }

    public void testReferenceData() throws Exception {

        final String cancelUrl = "cancelUrl";
        mockRequest.addParameter(ControllerConstants.CANCEL_URL, cancelUrl);

        final Map refData = resetPasswordFormController.referenceData(mockRequest);
        assertEquals(cancelUrl, refData.get(ControllerConstants.CANCEL_URL));
    }

    public void testOnBindAndValidate() throws Exception {

        UserPassword userPassword = getFormBackingObject();

        // do not set either password
        BindException errors = new BindException(userPassword, resetPasswordFormController.getCommandName());
        resetPasswordFormController.onBindAndValidate(mockRequest, userPassword, errors);

        // should ony be 2 validation errors - one for new password and one for repeated new password
        assertEquals(3, errors.getErrorCount());
        assertEquals(1, errors.getFieldErrors(NEW_PWD_FIELD_NAME).size());
        assertEquals(1, errors.getFieldErrors(RPT_NEW_PWD_FIELD_NAME).size());

        userPassword.setOldPassword(OLD_PASSWORD);
        // set both passwords but ensure they don't match
        userPassword.setNewPassword(NEW_PASSWORD);
        userPassword.setNewPasswordAgain(NEW_PASSWORD2);
        errors = getErrors(userPassword);
        resetPasswordFormController.onBindAndValidate(mockRequest, userPassword, errors);

        // should ony be 1 validation errors as pwds do not match
        assertEquals(1, errors.getErrorCount());
        assertEquals(1, errors.getFieldErrors(NEW_PWD_FIELD_NAME).size());

        // set both passwds to match - should be no validation errors
        userPassword.setNewPassword(NEW_PASSWORD);
        userPassword.setNewPasswordAgain(NEW_PASSWORD);
        errors = getErrors(userPassword);
        resetPasswordFormController.onBindAndValidate(mockRequest, userPassword, errors);
        assertFalse(errors.hasErrors());
    }

    /**
     * Test supplying a password that is too short and check that validation rejects it.
     *
     * @throws Exception
     */
    public void testMinLengthValidation() throws Exception {

        StringBuffer newPwd = new StringBuffer();
        IUserDao userDao = (IUserDao) applicationContext.getBean("hibUserManDao");
        Config passwordConfig = userDao.findConfig(Config.PASSWORD_CONFIG_ID);
        final Rule minLengthPwdRule = passwordConfig.getRule(Rule.PASSWORD_MIN_LENGTH);
        final int minPwdLength = Integer.parseInt(minLengthPwdRule.getValue());
        for (int i = 1; i < minPwdLength; i++) {
            newPwd.append("a");
        }

        UserPassword userPassword = getFormBackingObject();
        userPassword.setNewPassword(newPwd.toString());
        userPassword.setNewPasswordAgain(newPwd.toString());

        BindException errors = getErrors(userPassword);
        resetPasswordFormController.onSubmit(mockRequest, mockResponse, userPassword, errors);
        FieldError fieldError = errors.getFieldError(NEW_PWD_FIELD_NAME);
        assertNotNull(fieldError);
        assertEquals("error.minlength.rule.violated", fieldError.getCode());

        // check password history is unchanged
        checkPasswordHistory(testUser.getId(), 1);
    }

    /**
     * Test change pwd for new testUser.
     * <br> New users are obliged to change the password but do not have any password history so the validation is different.
     *
     * @throws Exception
     */
    public void testNewUser() throws Exception {

        // attempt to use same password again - must fail
        UserPassword userPassword = getFormBackingObject();
        userPassword.setNewPassword(PASSWORD);
        userPassword.setNewPasswordAgain(PASSWORD);

        BindException errors = getErrors(userPassword);
        resetPasswordFormController.onSubmit(mockRequest, mockResponse, userPassword, errors);
        FieldError fieldError = errors.getFieldError(NEW_PWD_FIELD_NAME);
        assertNotNull(fieldError);
        assertEquals("error.uniquepwd.rule.violated", fieldError.getCode());

        // check password history
        final Long userId = testUser.getId();
        checkPasswordHistory(userId, 1);

        // choose a different password and try again
        userPassword.setNewPassword(NEW_PASSWORD);
        userPassword.setNewPasswordAgain(NEW_PASSWORD);
        errors = getErrors(userPassword);
        resetPasswordFormController.onSubmit(mockRequest, mockResponse, userPassword, errors);
        assertFalse(errors.hasErrors());

        // check password history
        checkPasswordHistory(userId, 2);

        // try again with the same password - must fail
        resetPasswordFormController.onSubmit(mockRequest, mockResponse, userPassword, errors);
        fieldError = errors.getFieldError(NEW_PWD_FIELD_NAME);
        assertNotNull(fieldError);
        assertEquals("error.uniquepwd.rule.violated", fieldError.getCode());

        // choose a different password and try again
        userPassword.setNewPassword(NEW_PASSWORD2);
        userPassword.setNewPasswordAgain(NEW_PASSWORD2);
        errors = getErrors(userPassword);
        resetPasswordFormController.onSubmit(mockRequest, mockResponse, userPassword, errors);
        assertFalse(errors.hasErrors());

        // check password history        
        checkPasswordHistory(userId, 3);
    }

    private void checkPasswordHistory(final Long userId, int expected) throws TalentStudioException {
        User newUser;
        newUser = (User) resetPasswordFormController.getUserService().findById(userId);
        assertEquals(expected, newUser.getLoginInfo().getPasswordsHistory().size());
    }

    /**
     * Simple test to check that basic process works - does nto test validation, etc.
     *
     * @throws Exception
     */
    public void testOnSubmit() throws Exception {

        final UserPassword userPassword = getFormBackingObject();
        userPassword.setNewPassword(NEW_PASSWORD);
        userPassword.setNewPasswordAgain(NEW_PASSWORD);

        BindException errors = getErrors(userPassword);
        final ModelAndView modelAndView = resetPasswordFormController.onSubmit(mockRequest, mockResponse, userPassword, errors);

        // check view is correct
        final UserRedirectView view = (UserRedirectView) getRedirectView(modelAndView);
        assertEquals(resetPasswordFormController.getSuccessView(), view.getUrl());

        // check user id is set in model
        Map model = view.getStaticAttributes();
        final Long userId = (Long) model.get(ParameterConstants.USER_ID);
        assertEquals(testUser.getId(), userId);

        // check password history
        checkPasswordHistory(userId, 2);
    }

    private BindException getErrors(UserPassword userPassword) {
        BindException errors;
        errors = new BindException(userPassword, resetPasswordFormController.getCommandName());
        return errors;
    }

    private UserPassword getFormBackingObject() throws Exception {
        mockRequest.addParameter(ParameterConstants.USER_ID, testUser.getId().toString());
        return (UserPassword) resetPasswordFormController.formBackingObject(mockRequest);
    }

    private static final String NEW_PWD_FIELD_NAME = "newPassword";
    private static final String RPT_NEW_PWD_FIELD_NAME = "newPasswordAgain";

    private static final String USER_NAME = "pincher3";
    private static final String PASSWORD = "pincher3";
    private static final String NEW_PASSWORD = "bandalos7";
    private static final String NEW_PASSWORD2 = "bandalos8";
    private static final String OLD_PASSWORD = "oldOne";

    private ResetPasswordFormController resetPasswordFormController;
    private User testUser;
}
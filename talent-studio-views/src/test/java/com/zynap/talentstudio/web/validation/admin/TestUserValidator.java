package com.zynap.talentstudio.web.validation.admin;

/**
 * User: amark
 * Date: 14-Mar-2005
 * Time: 14:27:11
 */

import junit.framework.TestCase;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.web.security.admin.UserWrapperBean;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.List;

public class TestUserValidator extends TestCase {

    public void setUp() {
        _userValidator = new UserValidator();
    }

    public void testValidateCoreValues() throws Exception {

        final String fieldName = "loginInfo.username";

        UserWrapperBean bean = new UserWrapperBean(new User());
        DataBinder binder = new DataBinder(bean, "user");
        MutablePropertyValues values = new MutablePropertyValues();
        values.addPropertyValue(fieldName, "username");

        // check that the binding works for the UserWrapperBean
        binder.bind(values);
        final Errors errors = binder.getBindingResult();
        assertFalse(errors.hasErrors());

        _userValidator.validateCoreValues(bean, errors);
        assertTrue(errors.hasErrors());

        checkCoreDetailErrors(errors, fieldName);
    }

    public void testFirstNameValidation() throws Exception {

        final String fieldName = "secondName";

        UserWrapperBean bean = new UserWrapperBean(new User());
        DataBinder binder = new DataBinder(bean, "user");
        MutablePropertyValues values = new MutablePropertyValues();
        values.addPropertyValue(fieldName, "secondName");
        values.addPropertyValue("loginInfo.username", "userName");

        // check that the binding works for the UserWrapperBean
        binder.bind(values);
        final Errors errors = binder.getBindingResult();
        assertFalse(errors.hasErrors());

        _userValidator.validateCoreValues(bean, errors);
        assertEquals(1, errors.getErrorCount());

        checkFirstNameErrors(errors);
    }

    public void testSecondNameValidation() throws Exception {

        final String fieldName = "firstName";

        UserWrapperBean bean = new UserWrapperBean(new User());
        DataBinder binder = new DataBinder(bean, "user");
        MutablePropertyValues values = new MutablePropertyValues();
        values.addPropertyValue(fieldName, "firstName");
        values.addPropertyValue("loginInfo.username", "userName");

        // check that the binding works for the UserWrapperBean
        binder.bind(values);
        final Errors errors = binder.getBindingResult();
        assertFalse(errors.hasErrors());

        _userValidator.validateCoreValues(bean, errors);
        assertEquals(1, errors.getErrorCount());

        checkSecondNameErrors(errors);
    }

    public void testNoPassword() throws Exception {

        final String fieldName = "loginInfo.password";

        UserWrapperBean bean = new UserWrapperBean(new User());
        DataBinder binder = new DataBinder(bean, "user");
        MutablePropertyValues values = new MutablePropertyValues();

        // check that the binding works for the UserWrapperBean
        binder.bind(values);
        Errors errors = binder.getBindingResult();
        assertFalse(errors.hasErrors());

        // check how many errors - should be 2 as the password and repeated password are both required
        _userValidator.validatePassword(bean, errors);
        assertTrue(errors.hasErrors());
        assertEquals(2, errors.getErrorCount());

        // check that there are validation errors for the field as we have not yet set a value
        List fieldErrors = errors.getFieldErrors(fieldName);
        assertFalse(fieldErrors.isEmpty());
        assertEquals(1, fieldErrors.size());
    }

    public void testNoRepeatPassword() throws Exception {

        final String fieldName = "loginInfo.password";

        UserWrapperBean bean = new UserWrapperBean(new User());
        DataBinder binder = new DataBinder(bean, "user");
        MutablePropertyValues values = new MutablePropertyValues();
        values.addPropertyValue(fieldName, "password");

        // check that the binding works for the UserWrapperBean
        binder.bind(values);
        Errors errors = binder.getBindingResult();
        assertFalse(errors.hasErrors());

        // check how many errors - should be 1 as the repeated password is both required
        _userValidator.validatePassword(bean, errors);
        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getErrorCount());

        // check that there no validation errors for the field
        List fieldErrors = errors.getFieldErrors(fieldName);
        assertTrue(fieldErrors.isEmpty());
    }

    public void testNonMatchingPasswords() throws Exception {

        final String fieldName = "loginInfo.password";

        UserWrapperBean bean = new UserWrapperBean(new User());
        DataBinder binder = new DataBinder(bean, "user");
        MutablePropertyValues values = new MutablePropertyValues();
        values.addPropertyValue(fieldName, "password");
        values.addPropertyValue("loginInfo.repeatedPassword", "newpassword");

        // check that the binding works for the UserWrapperBean
        binder.bind(values);
        Errors errors = binder.getBindingResult();
        assertFalse(errors.hasErrors());

        // check how many errors - should be 1 as passwords do not match
        _userValidator.validatePassword(bean, errors);
        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getErrorCount());

        // check that there are validation errors for the field as the discrepancy in passwords is indicated as
        // a problem with the password
        List fieldErrors = errors.getFieldErrors(fieldName);
        assertEquals(1, fieldErrors.size());
    }

    public void testSupports() throws Exception {
       assertTrue(_userValidator.supports(UserWrapperBean.class));
    }

    public void testValidate() throws Exception {

        // check that validate method only checks core details
        // must not validate password as password can only be supplied when adding a user
        // When editing a user the password cannot be changed but the 2 controllers (AddUserFormController and EditUserFormController)
        // both use the same validator.
        final String fieldName = "loginInfo.username";

        UserWrapperBean bean = new UserWrapperBean(new User());
        DataBinder binder = new DataBinder(bean, "user");
        MutablePropertyValues values = new MutablePropertyValues();
        values.addPropertyValue(fieldName, "username");

        // check that the binding works for the UserWrapperBean
        binder.bind(values);
        Errors errors = binder.getBindingResult();
        assertFalse(errors.hasErrors());

        _userValidator.validate(bean, errors);
        assertTrue(errors.hasErrors());

        checkCoreDetailErrors(errors, fieldName);
    }

    private void checkCoreDetailErrors(final Errors errors, final String fieldName) {

        // returns 2 as the validator requires first name and last name
        assertEquals(2, errors.getErrorCount());

        // check that the field that was set has no errors associated with it
        final List fieldErrors = errors.getFieldErrors(fieldName);
        assertTrue(fieldErrors.isEmpty());

        // check errors
        checkFirstNameErrors(errors);

        checkSecondNameErrors(errors);
    }

    private void checkFirstNameErrors(final Errors errors) {
        final FieldError firstNameError = errors.getFieldError("firstName");
        assertNotNull(firstNameError);
        assertEquals("error.firstname.missing", firstNameError.getCode());
    }

    private void checkSecondNameErrors(final Errors errors) {
        final FieldError secondNameError = errors.getFieldError("secondName");
        assertNotNull(secondNameError);
        assertEquals("error.lastname.missing", secondNameError.getCode());
    }

    UserValidator _userValidator;
}
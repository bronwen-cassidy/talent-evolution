package com.zynap.talentstudio.web.organisation.subjects;

/**
 * User: amark
 * Date: 30-Mar-2006
 * Time: 14:31:21
 */

import com.zynap.domain.admin.LoginInfo;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.CoreDetail;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.web.AbstractValidatorTestCase;
import com.zynap.talentstudio.web.controller.ZynapDefaultFormController;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;

import java.util.Date;


public class TestSubjectUserValidator extends AbstractValidatorTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        subjectUserValidator = new SubjectUserValidator();

        Subject subject = new Subject();

        CoreDetail coreDetail = new CoreDetail("dr", "fred", "flintstone");
        coreDetail.setPrefGivenName("fred");
        subject.setActive(true);
        subject.setDateOfBirth(new Date());

        LoginInfo loginInfo = new LoginInfo();
        user = new User(loginInfo, coreDetail);

        subject.setUser(user);
        subjectWrapperBean = new SubjectWrapperBean(subject);

        // set username and check there are no errors
        binder = new DataBinder(subjectWrapperBean, ZynapDefaultFormController.DEFAULT_COMMAND_NAME);
    }

    public void testValidateExistingUserNoValues() throws Exception {

        // create user with id - this simulates a user who already exists and is being edited - important as the validation is different
        user.setId(new Long(-10));

        Errors errors = binder.getBindingResult();
        subjectUserValidator.validate(subjectWrapperBean, errors);
        assertErrorCount(1, errors);
        assertErrorCode(errors, "error.username.missing", SubjectValidator.USER_NAME_FIELD);
    }

    public void testValidateExistingUser() throws Exception {

        // create user with id - this simulates a user who already exists and is being edited - important as the validation is different
        user.setId(new Long(-10));

        Errors errors = binder.getBindingResult();
        MutablePropertyValues pvs = new MutablePropertyValues();
        pvs.addPropertyValue(SubjectValidator.USER_NAME_FIELD, "username");
        binder.bind(pvs);
        subjectUserValidator.validate(subjectWrapperBean, errors);
        assertFalse(errors.hasErrors());
    }

    public void testValidateNewUser() throws Exception {

        Errors errors = binder.getBindingResult();
        subjectUserValidator.validate(subjectWrapperBean, errors);

        assertErrorCount(3, errors);
        assertErrorCode(errors, "error.username.missing", SubjectValidator.USER_NAME_FIELD);
        assertErrorCode(errors, "error.password.missing", SubjectValidator.PASSWORD_FIELD);
        assertErrorCode(errors, "error.repeat.password.missing", SubjectValidator.REPEATED_PASSWORD_FIELD);
    }

    SubjectUserValidator subjectUserValidator;
    private SubjectWrapperBean subjectWrapperBean;
    private DataBinder binder;
    private User user;
}
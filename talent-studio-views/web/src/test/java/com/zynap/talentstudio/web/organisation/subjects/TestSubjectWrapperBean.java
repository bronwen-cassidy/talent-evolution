package com.zynap.talentstudio.web.organisation.subjects;

/**
 * User: amark
 * Date: 31-Mar-2005
 * Time: 12:15:33
 */

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.ZynapTestCase;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.organisation.subjects.SubjectAssociation;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;

public class TestSubjectWrapperBean extends ZynapTestCase {

    protected void setUp() throws Exception {

        subject = new Subject();
        subjectWrapperBean = new SubjectWrapperBean(subject);
    }

    public void testResetIds() throws Exception {

        User user = new User(new Long(-1));
        subject.setUser(user);
        subjectWrapperBean = new SubjectWrapperBean(subject);

        // set ids
        subjectWrapperBean.setId(new Long(-10));
        subjectWrapperBean.getCoreDetail().setId(new Long(-11));
        subjectWrapperBean.getLoginInfo().setId(new Long(-13));

        // add 1 primary and 1 secondary association
        final LookupValue secondaryAssociationQualifier = new LookupValue(ILookupManager.LOOKUP_TYPE_SECONDARY_SUBJECT_ASSOC);
        final LookupValue primaryAssociationQualifier = new LookupValue(ILookupManager.LOOKUP_TYPE_PRIMARY_SUBJECT_ASSOC);

        final SubjectAssociation secondaryAssoc = new SubjectAssociation(new Long(-14), secondaryAssociationQualifier, null, DEFAULT_POSITION);
        final SubjectAssociation primaryAssociation = new SubjectAssociation(new Long(-15), primaryAssociationQualifier, null, DEFAULT_POSITION);
        subjectWrapperBean.addSubjectAssociation(secondaryAssoc);
        subjectWrapperBean.addSubjectAssociation(primaryAssociation);

        // clear ids
        subjectWrapperBean.resetIds();
        final Subject modifiedSubject = subjectWrapperBean.getModifiedSubject(new User(ADMINISTRATOR_USER_ID));

        // check ids are cleared
        assertNull(modifiedSubject.getId());
        assertNull(user.getId());
        assertNull(user.getLoginInfo().getId());
        assertNull(modifiedSubject.getCoreDetail().getId());
        assertNull(user.getCoreDetail().getId());
        assertNull(primaryAssociation.getId());
        assertNull(secondaryAssoc.getId());        
    }

    public void testGetModifiedSubjectNoUser() throws Exception {

        // check that user is null to start with
        checkUserIsNull();

        // check that binding is ok
        DataBinder binder = new DataBinder(subjectWrapperBean, "command");
        MutablePropertyValues values = new MutablePropertyValues();
        values.addPropertyValue(TITLE_FIELD_NAME, "title");
        values.addPropertyValue(FIRST_NAME_FIELD_NAME, "firstname");
        values.addPropertyValue(SECOND_NAME_FIELD_NAME, "secondname");
        values.addPropertyValue(GIVEN_NAME_FIELD_NAME, "given name");
        binder.bind(values);
        Errors errors = checkForBindErrors(binder);

        // make sure validation is ok
        validate(subjectWrapperBean, errors);
        assertFalse(errors.hasErrors());

        // check that user was not set
        final Subject newSubject = subjectWrapperBean.getModifiedSubject(new User(ADMINISTRATOR_USER_ID));
        assertNull(newSubject.getUser());

        // check that core details are set
        assertEquals(values.getPropertyValue(FIRST_NAME_FIELD_NAME).getValue(), newSubject.getCoreDetail().getFirstName());
    }

    public void testGetModifiedSubject() throws Exception {

        // check that user is null to start with
        checkUserIsNull();

        // check that binding is ok
        DataBinder binder = new DataBinder(subjectWrapperBean, "command");
        MutablePropertyValues values = new MutablePropertyValues();
        values.addPropertyValue(TITLE_FIELD_NAME, "title");
        values.addPropertyValue(FIRST_NAME_FIELD_NAME, "firstname");
        values.addPropertyValue(SECOND_NAME_FIELD_NAME, "secondname");
        values.addPropertyValue(GIVEN_NAME_FIELD_NAME, "given name");
        values.addPropertyValue(USER_NAME_FIELD_NAME, ROOT_USERNAME);
        values.addPropertyValue(PWD_FIELD_NAME, "password");
        values.addPropertyValue(REPEATED_PWD_FIELD_NAME, "password");
        binder.bind(values);
        Errors errors = checkForBindErrors(binder);

        // make sure validation is ok
        validate(subjectWrapperBean, errors);
        assertFalse(errors.hasErrors());

        // check that user is set and that core details match and that user name is set
        final Subject newSubject = subjectWrapperBean.getModifiedSubject(new User(ADMINISTRATOR_USER_ID));
        final User newUser = newSubject.getUser();
        assertNotNull(newUser);
        assertEquals(values.getPropertyValue(USER_NAME_FIELD_NAME).getValue(), newUser.getLoginInfo().getUsername());
        assertEquals(newSubject.getCoreDetail(), newUser.getCoreDetail());
    }

    /**
     * Test that simulates admin user adding a subject user, failing validation and then clearing the user details.
     * <br> Should produce a new subject with no user at all.
     *
     * @throws Exception
     */
    public void testGetModifiedSubjectClearUser() throws Exception {

        // check that user is null to start with
        checkUserIsNull();

        // check that binding is ok - leave out REPEATED_PWD_FIELD_NAME so as to force validation failure
        DataBinder binder = new DataBinder(subjectWrapperBean, "command");
        MutablePropertyValues values = new MutablePropertyValues();
        values.addPropertyValue(TITLE_FIELD_NAME, "title");
        values.addPropertyValue(FIRST_NAME_FIELD_NAME, "firstname");
        values.addPropertyValue(SECOND_NAME_FIELD_NAME, "secondname");
        values.addPropertyValue(GIVEN_NAME_FIELD_NAME, "given name");
        values.addPropertyValue(USER_NAME_FIELD_NAME, ROOT_USERNAME);
        values.addPropertyValue(PWD_FIELD_NAME, "password");
        binder.bind(values);
        Errors errors = checkForBindErrors(binder);

        // check for validation errors
        validate(subjectWrapperBean, errors);
        assertTrue(errors.hasFieldErrors(REPEATED_PWD_FIELD_NAME));

        // check that binding is ok - this time leave out all fields relating to user (username, password, etc)
        subjectWrapperBean = new SubjectWrapperBean(subjectWrapperBean.getOriginalSubject());
        binder = new DataBinder(subjectWrapperBean, "command");
        values = new MutablePropertyValues();
        values.addPropertyValue(TITLE_FIELD_NAME, "title");
        values.addPropertyValue(FIRST_NAME_FIELD_NAME, "firstname");
        values.addPropertyValue(SECOND_NAME_FIELD_NAME, "secondname");
        values.addPropertyValue(GIVEN_NAME_FIELD_NAME, "given name");
        binder.bind(values);
        errors = checkForBindErrors(binder);

        // make sure validation is ok
        validate(subjectWrapperBean, errors);
        assertFalse(errors.hasErrors());

        final Subject newSubject = subjectWrapperBean.getModifiedSubject(new User(new Long(0)));

        // check that core details are set
        assertEquals(values.getPropertyValue(TITLE_FIELD_NAME).getValue(), newSubject.getCoreDetail().getTitle());

        // check that new subject has no user
        final User newUser = newSubject.getUser();
        assertNull(newUser);
    }

    private void checkUserIsNull() {
        assertNull(subjectWrapperBean.getModifiedSubject(new User(new Long(0))).getUser());
    }

    private Errors checkForBindErrors(DataBinder binder) {
        Errors errors = binder.getBindingResult();
        assertFalse(errors.hasErrors());
        return errors;
    }

    private void validate(SubjectWrapperBean subjectWrapperBean, Errors errors) {

        final SubjectValidator validator = new SubjectValidator();
        validator.validateRequiredValues(subjectWrapperBean, errors);
        validator.validateLoginValues(subjectWrapperBean, errors);
    }

    private SubjectWrapperBean subjectWrapperBean;

    private Subject subject;

    private static final String GIVEN_NAME_FIELD_NAME = "prefGivenName";
    private static final String SECOND_NAME_FIELD_NAME = "secondName";
    private static final String FIRST_NAME_FIELD_NAME = "firstName";
    private static final String TITLE_FIELD_NAME = "title";
    private static final String USER_NAME_FIELD_NAME = "userWrapper.loginInfo.username";
    private static final String PWD_FIELD_NAME = "userWrapper.loginInfo.password";
    private static final String REPEATED_PWD_FIELD_NAME = "userWrapper.loginInfo.repeatedPassword";
}
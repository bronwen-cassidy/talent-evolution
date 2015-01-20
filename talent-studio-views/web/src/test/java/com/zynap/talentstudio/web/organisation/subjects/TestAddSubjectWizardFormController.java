package com.zynap.talentstudio.web.organisation.subjects;

/**
 * User: amark
 * Date: 30-Mar-2005
 * Time: 14:41:20
 */

import com.zynap.domain.UserPrincipal;
import com.zynap.domain.UserSession;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

public class TestAddSubjectWizardFormController extends ZynapMockControllerTest {

    public void setUp() throws Exception {
        super.setUp();

        UserPrincipal principal = getAdminUserPrincipal();
        UserSession userSession = new UserSession(principal, getArenaMenuHandler());
        ZynapWebUtils.setUserSession(mockRequest, userSession);
        addSubjectWizardFormController = (AddSubjectWizardFormController) applicationContext.getBean("addSubjectController");
    }

    public void testCancel() throws Exception {

        SubjectWrapperBean formBackingObject = getFormBackingObject();
        final ModelAndView modelAndView = addSubjectWizardFormController.processCancelInternal(mockRequest, mockResponse, formBackingObject, getErrors(formBackingObject));
        final ZynapRedirectView expected = addSubjectWizardFormController.getCancelViewConfig().getRedirectView();
        final ZynapRedirectView actual = (ZynapRedirectView) modelAndView.getView();
        assertEquals(expected.getUrl(), actual.getUrl());
    }

    public void testAddSubjectWithUser() throws Exception {

        SubjectWrapperBean formBackingObject = getFormBackingObject();

        // add subject with user
        DataBinder binder = new DataBinder(formBackingObject, addSubjectWizardFormController.getCommandName());
        MutablePropertyValues values = new MutablePropertyValues();
        values.addPropertyValue(SubjectValidator.TITLE_FIELD, null);
        values.addPropertyValue(SubjectValidator.FIRST_NAME_FIELD, "firstname");
        values.addPropertyValue(SubjectValidator.SECOND_NAME_FIELD, "secondname");
        values.addPropertyValue(SubjectValidator.PREF_NAME_FIELD, null);

        values.addPropertyValue(SubjectValidator.USER_NAME_FIELD, "username");
        values.addPropertyValue(SubjectValidator.PASSWORD_FIELD, "password");
        values.addPropertyValue(SubjectValidator.REPEATED_PASSWORD_FIELD, "password");
        binder.bind(values);
        Errors errors = getErrors(binder);
        assertFalse(errors.hasErrors());

        // make user inactive (as opposed to subject)
        formBackingObject.getUserWrapper().setActive(false);
        addSubjectWizardFormController.processFinishInternal(mockRequest, mockResponse, formBackingObject, getErrors(formBackingObject));

        // look up subject and check they are active, etc
        Subject newSubject = addSubjectWizardFormController.subjectService.findById(formBackingObject.getModifiedSubject(new User(new Long(0))).getId());
        assertNotNull(newSubject.getCoreDetail());
        assertEquals(formBackingObject.getTitle(), newSubject.getTitle());
        assertEquals(formBackingObject.getFirstName(), newSubject.getFirstName());
        assertEquals(formBackingObject.getSecondName(), newSubject.getSecondName());
        assertEquals(formBackingObject.getPrefGivenName(), newSubject.getPrefGivenName());
        assertTrue(newSubject.isActive());

        // check that user is inactive and that core details match
        User newUser = newSubject.getUser();
        assertNotNull(newUser);
        assertEquals(newUser.getCoreDetail(), newSubject.getCoreDetail());
        assertFalse(newUser.isActive());
    }

    public void testAddSubjectOnly() throws Exception {

        SubjectWrapperBean formBackingObject = getFormBackingObject();

        // add subject without user
        DataBinder binder = new DataBinder(formBackingObject, addSubjectWizardFormController.getCommandName());
        MutablePropertyValues values = new MutablePropertyValues();
        values.addPropertyValue(SubjectValidator.TITLE_FIELD, null);
        values.addPropertyValue(SubjectValidator.FIRST_NAME_FIELD, "firstname");
        values.addPropertyValue(SubjectValidator.SECOND_NAME_FIELD, "secondname");
        values.addPropertyValue(SubjectValidator.PREF_NAME_FIELD, null);

        binder.bind(values);
        Errors errors = getErrors(binder);
        assertFalse(errors.hasErrors());

        // make new subject inactive
        formBackingObject.setActive(false);
        final ModelAndView modelAndView = addSubjectWizardFormController.processFinishInternal(mockRequest, mockResponse, formBackingObject, getErrors(formBackingObject));

       // check that the view returned is correct and that the subject id parameter is set
        final ZynapRedirectView redirectView = (ZynapRedirectView) modelAndView.getView();
        assertEquals(addSubjectWizardFormController.getSuccessView(), redirectView.getUrl());
        assertNotNull(redirectView.getStaticAttributes().get(ParameterConstants.SUBJECT_ID_PARAM));
        assertEquals(formBackingObject.getModifiedSubject(new User(new Long(0))).getId(), redirectView.getStaticAttributes().get(ParameterConstants.SUBJECT_ID_PARAM));

        // look up subject and check they are inactive, etc
        Subject newSubject = addSubjectWizardFormController.subjectService.findById(formBackingObject.getModifiedSubject(new User(new Long(0))).getId());
        assertNull(newSubject.getUser());
        assertNotNull(newSubject.getCoreDetail());
        assertEquals(formBackingObject.getTitle(), newSubject.getTitle());
        assertEquals(formBackingObject.getFirstName(), newSubject.getFirstName());
        assertEquals(formBackingObject.getSecondName(), newSubject.getSecondName());
        assertEquals(formBackingObject.getPrefGivenName(), newSubject.getPrefGivenName());
        assertFalse(newSubject.isActive());
    }

    public void testFormBackingObject() throws Exception {
        final SubjectWrapperBean subjectWrapperBean = getFormBackingObject();
        assertNull(subjectWrapperBean.getOriginalSubject().getId());
    }

    public void testCoreDetailsValidation() throws Exception {

        SubjectWrapperBean formBackingObject = getFormBackingObject();

        // set all fields to blank - should bind ok but fail validation
        DataBinder binder = new DataBinder(formBackingObject, addSubjectWizardFormController.getCommandName());
        MutablePropertyValues values = new MutablePropertyValues();

        values.addPropertyValue(SubjectValidator.TITLE_FIELD, null);
        values.addPropertyValue(SubjectValidator.FIRST_NAME_FIELD, null);
        values.addPropertyValue(SubjectValidator.SECOND_NAME_FIELD, null);
        values.addPropertyValue(SubjectValidator.PREF_NAME_FIELD, null);

        binder.bind(values);
        Errors errors = getErrors(binder);
        assertFalse(errors.hasErrors());

        addSubjectWizardFormController.onBindAndValidateInternal(mockRequest, formBackingObject, errors, 0);
        assertEquals(2, errors.getErrorCount());
        assertEquals(1, errors.getFieldErrorCount(SubjectValidator.FIRST_NAME_FIELD));
        assertEquals(1, errors.getFieldErrorCount(SubjectValidator.SECOND_NAME_FIELD));
    }

    public void testReferenceData() throws Exception {

        final SubjectWrapperBean formBackingObject = getFormBackingObject();
        final Errors errors = getErrors(formBackingObject);

        // check that titles are returned on first page
        Map refData = addSubjectWizardFormController.referenceData(mockRequest, formBackingObject, errors, 0);
        assertNotNull(refData.get(ControllerConstants.TITLES));
        assertNotNull(refData.get(ControllerConstants.TITLE));

        // check that titles are not returned on first page but positions are returned
        refData = addSubjectWizardFormController.referenceData(mockRequest, formBackingObject, errors, 1);
        assertNull(refData.get(ControllerConstants.TITLES));
        assertNotNull(refData.get(ControllerConstants.TITLE));
    }

    private SubjectWrapperBean getFormBackingObject() throws Exception {
        return (SubjectWrapperBean) addSubjectWizardFormController.formBackingObject(mockRequest);
    }

    private AddSubjectWizardFormController addSubjectWizardFormController;
}
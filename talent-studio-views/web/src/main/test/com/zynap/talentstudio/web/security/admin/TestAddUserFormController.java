package com.zynap.talentstudio.web.security.admin;

/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */

import com.zynap.domain.UserPrincipal;
import com.zynap.domain.UserSession;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

public class TestAddUserFormController extends ZynapMockControllerTest {

    protected void setUp() throws Exception {
        super.setUp();
        addUserFormController = (AddUserFormController) applicationContext.getBean("addUserFormController");
        userValidator = (Validator) applicationContext.getBean("userValidator");
        UserPrincipal principal = getAdminUserPrincipal();
        UserSession userSession = new UserSession(principal, getArenaMenuHandler());
        ZynapWebUtils.setUserSession(mockRequest, userSession);
    }

    public void testFormBackingObject() throws Exception {
        UserWrapperBean bean = (UserWrapperBean) addUserFormController.formBackingObject(mockRequest);
        assertNotNull(bean.getModifiedUser());
        assertNotNull(bean.getAccessRoles());
        assertTrue(bean.getUserRoles().isEmpty());
        assertNotNull(bean.getCoreDetail());
        assertNotNull(bean.getLoginInfo());
    }

    public void testReferenceData() throws Exception {
        UserWrapperBean bean = (UserWrapperBean) addUserFormController.formBackingObject(mockRequest);
        Map refData = addUserFormController.referenceData(mockRequest, bean, null);
        assertNotNull(refData.get(ControllerConstants.TITLES));
        assertEquals(Boolean.TRUE, refData.get(AddUserFormController.SET_PASSWORD));
    }

    public void testOnBindAndValidate() throws Exception {
        UserWrapperBean bean = (UserWrapperBean) addUserFormController.formBackingObject(mockRequest);
        // populate the bean
        Errors errors = bindUserAssertNoErrors(bean);
        assertFalse(errors.hasErrors());
        addUserFormController.onBindAndValidateInternal(mockRequest, bean, errors);
        assertFalse(errors.hasErrors());
    }

    public void testOnSubmitInternal() throws Exception {
        UserWrapperBean bean = (UserWrapperBean) addUserFormController.formBackingObject(mockRequest);
        // populate the bean
        Errors errors = bindUserAssertNoErrors(bean);
        ModelAndView successView = addUserFormController.onSubmitInternal(mockRequest, mockResponse, bean, errors);

        // check that user is not a root user
        assertFalse(bean.isRoot());

        // check that user is active
        assertTrue(bean.isActive());

        // check we have a userId
        assertNotNull(((RedirectView) successView.getView()).getStaticAttributes().get(ParameterConstants.USER_ID));
    }

    private Errors bindUserAssertNoErrors(UserWrapperBean bean) {

        DataBinder binder = new DataBinder(bean, "user");
        MutablePropertyValues values = new MutablePropertyValues();
        values.addPropertyValue("loginInfo.username", "bandogers");
        values.addPropertyValue("loginInfo.password", "hahah56t");
        values.addPropertyValue("loginInfo.repeatedPassword", "hahah56t");
        values.addPropertyValue("coreDetail.title", "Mr");
        values.addPropertyValue("coreDetail.prefGivenName", "Banderas");
        values.addPropertyValue("coreDetail.firstName", "Badger");
        values.addPropertyValue("coreDetail.secondName", "Bear");
        values.addPropertyValue("coreDetail.contactTelephone", "11111111111");
        values.addPropertyValue("coreDetail.contactEmail", "a.b@haha.com");
        values.addPropertyValue("accessRoleIds", new Long[]{new Long(1), new Long(3), new Long(5)});
        binder.bind(values);
        BindingResult errors = binder.getBindingResult();
        userValidator.validate(bean, errors);
        try {
            binder.close();
        } catch (BindException e) {
            fail("No Exception Expected");
        }

        assertEquals(0, errors.getErrorCount());

        return errors;
    }

    private AddUserFormController addUserFormController;
    private Validator userValidator;
}

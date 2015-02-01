/**
 * Class or Interface description.
 *
 * @author taulant bajraktari
 * @since 09-Mar-2009 12:59:03
 * @version 0.1
 */
package com.zynap.talentstudio.web.organisation.subjects;

import com.zynap.talentstudio.web.utils.ZynapDbUnitMockControllerTestCase;
import static com.zynap.talentstudio.web.common.ParameterConstants.*;
import com.zynap.talentstudio.arenas.IArenaMenuHandler;
import com.zynap.talentstudio.security.roles.Role;
import com.zynap.talentstudio.security.permits.IPermit;
import com.zynap.talentstudio.audit.SessionLog;
import com.zynap.domain.UserPrincipal;
import com.zynap.domain.UserSession;
import com.zynap.domain.admin.User;

import java.util.Collection;
import java.util.ArrayList;

/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
public class TestSubjectUserController extends ZynapDbUnitMockControllerTestCase {


    protected void setUp() throws Exception {
        super.setUp();
        subjectUserController = (SubjectUserController) getBean("editSubjectUserController");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        subjectUserController = null;
    }

    protected String getDataSetFileName() throws Exception {
        return "subject-data.xml";
    }

    /**
     * is the logged in user has the admin rights should be able
     * to make a user have the admin rights
     *
     * @throws Exception
     */
    public void testFormBackingObjectSubjectHasAdminPermissions() throws Exception {


        final UserPrincipal principal = new UserPrincipal(new User(new Long(USER_ID_WITHOUT_PERMISSION_DOMAIN), "Subject X", "Subject N", "Subject Sur"), new ArrayList<IPermit>(), new SessionLog());
        final IArenaMenuHandler menuHandler = getArenaMenuHandler();
        final UserSession userSession = new UserSession(principal, menuHandler);
        setUserSession(userSession, mockRequest);

        mockRequest.addParameter(SUBJECT_ID_PARAM, USER_ID_WITH_PERMISSION_DOMAIN.toString());
        SubjectWrapperBean subjectWrapperBean = (SubjectWrapperBean) subjectUserController.formBackingObject(mockRequest);

        final Collection<Role> userRoles = subjectWrapperBean.getUserRoles();
        assertNotNull(userRoles);
        boolean hasAdminRole = false;
        for (Role userRole : userRoles) {

            if (userRole.isAdminRole()) {
                hasAdminRole = true;
            }
        }
        assertTrue(hasAdminRole);
    }

    /**
     * if the user does not have admin rights should not be able to assign admin rights to a user
     *
     * @throws Exception
     */
    public void testFormBackingObjectSubjectDoesNotHaveAdminPermissions() throws Exception {


        final UserPrincipal principal = new UserPrincipal(new User(new Long(USER_ID_WITH_PERMISSION_DOMAIN), "Subject X", "Subject N", "Subject Sur"), new ArrayList<IPermit>(), new SessionLog());
        final IArenaMenuHandler menuHandler = getArenaMenuHandler();
        final UserSession userSession = new UserSession(principal, menuHandler);
        setUserSession(userSession, mockRequest);

        //test against subject without admin permission
        mockRequest.addParameter(SUBJECT_ID_PARAM, USER_ID_WITHOUT_PERMISSION_DOMAIN.toString());
        SubjectWrapperBean subjectWrapperBean = (SubjectWrapperBean) subjectUserController.formBackingObject(mockRequest);

        final Collection<Role> userRoles = subjectWrapperBean.getUserRoles();
        assertNotNull(userRoles);
        boolean hasAdminRole = false;
        for (Role userRole : userRoles) {

            if (userRole.isAdminRole()) {
                hasAdminRole = true;
            }
        }
        assertFalse(hasAdminRole);


    }


    private static final Long USER_ID_WITHOUT_PERMISSION_DOMAIN = new Long(-35);
    private static final Long USER_ID_WITH_PERMISSION_DOMAIN = new Long(-33);

    private SubjectUserController subjectUserController;
}
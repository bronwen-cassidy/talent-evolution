/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.utils.tree;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 20-Oct-2006 13:01:07
 * @version 0.1
 */

import com.zynap.talentstudio.web.utils.ZynapDbUnitMockControllerTestCase;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.domain.UserSession;
import com.zynap.domain.UserPrincipal;
import com.zynap.domain.admin.User;

import java.util.List;

public class DBUnitTestPositionPrimaryAssociationPickerController extends ZynapDbUnitMockControllerTestCase {

    protected String getDataSetFileName() {
        return "position-test-data.xml";
    }

    public void setUp() throws Exception {
        super.setUp();
        positionPrimaryAssociationPickerController = (PositionPrimaryAssociationPickerController) getBean("positionPrimaryAssociationPickerController");
    }

    public void testFormBackingObject() throws Exception {
        final Long userId = new Long(-135);
        mockRequest.addParameter(PickerController.POPUP_ID_PARAM, "securepositionpicker");
        UserSessionFactory.setUserSession(new UserSession(new UserPrincipal(new User(userId, "username", "firstName", "lastName"))));
        final TreeWrapperBean wrapper = (TreeWrapperBean) positionPrimaryAssociationPickerController.formBackingObject(mockRequest);
        final List tree = wrapper.getTree();
        // todo find out what we have here
    }

    private PositionPrimaryAssociationPickerController positionPrimaryAssociationPickerController;
}
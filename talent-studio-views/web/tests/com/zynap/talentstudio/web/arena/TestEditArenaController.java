package com.zynap.talentstudio.web.arena;

/**
 * User: amark
 * Date: 14-Nov-2005
 * Time: 14:22:28
 */

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.arenas.IArenaManager;
import com.zynap.talentstudio.security.roles.IRoleManager;
import com.zynap.talentstudio.security.roles.Role;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;

public class TestEditArenaController extends ZynapMockControllerTest {

    protected void setUp() throws Exception {
        super.setUp();

        editArenaController = (EditArenaController) getBean("editArenaController");
    }

    public void testFormBackingObjectNoArena() throws Exception {
        try {
            editArenaController.formBackingObject(mockRequest);
            fail("Form backing object requires value for parameter: " + ParameterConstants.ARENA_ID_PARAM);
        } catch (Exception expected) {
        }
    }

    public void testFormBackingObject() throws Exception {
        String id = IArenaManager.ADMIN_MODULE;
        final ArenaWrapperBean command = getFormBackingObject(id);
        assertEquals(id, command.getId());
        assertNotNull(command.getArenaDisplayConfigItems());
    }

    public void testEditArenaRoleUpdated() throws Exception {
        setUserSession(ROOT_USER, mockRequest);
        IRoleManager roleManager = (IRoleManager) getBean("roleManager");
        String id = IArenaManager.PERFORMANCE_MANAGEMENT_MODULE;
        final ArenaWrapperBean command = getFormBackingObject(id);

        command.setLabel("Pandy Bear");
        editArenaController.onSubmitInternal(mockRequest, mockResponse, command, getErrors(command));

        Role role = roleManager.findArenaRole(IArenaManager.PERFORMANCE_MANAGEMENT_MODULE);
        assertEquals(command.getLabel() + " Arena", role.getLabel());
    }

    public void testOnBindInternal() throws Exception {
        String id = IArenaManager.TALENT_IDENTIFICATION_MODULE;
        final ArenaWrapperBean command = getFormBackingObject(id);

        // binding will set active to false because there is no parameter in the request
        command.setActive(true);
        editArenaController.onBindInternal(mockRequest, command);
        assertFalse(command.isActive());

        // set parameter in request - active should now be true
        mockRequest.addParameter(ParameterConstants.ACTIVE, Boolean.TRUE.toString());
        editArenaController.onBindInternal(mockRequest, command);
        assertTrue(command.isActive());
    }

    public void testOnSubmitInternal() throws Exception {

        final String testLabel = "testLabel";

        String id = IArenaManager.TALENT_IDENTIFICATION_MODULE;
        final ArenaWrapperBean command = getFormBackingObject(id);
        command.setLabel(testLabel);

        setUserSession(new UserSession(getAdminUserPrincipal(), getArenaMenuHandler()), mockRequest);

        editArenaController.onSubmitInternal(mockRequest, mockResponse, command, getErrors(command));

        final ArenaWrapperBean newCommand = getFormBackingObject(id);
        assertEquals(testLabel, newCommand.getLabel());
        assertEquals(id, newCommand.getId());
    }

    private ArenaWrapperBean getFormBackingObject(String id) throws Exception {
        mockRequest.addParameter(ParameterConstants.ARENA_ID_PARAM, id);
        return (ArenaWrapperBean) editArenaController.formBackingObject(mockRequest);
    }

    private EditArenaController editArenaController;
}
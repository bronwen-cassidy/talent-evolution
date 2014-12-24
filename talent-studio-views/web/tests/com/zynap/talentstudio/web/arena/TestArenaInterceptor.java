package com.zynap.talentstudio.web.arena;

/**
 * User: amark
 * Date: 17-Nov-2005
 * Time: 10:25:47
 */

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.arenas.Arena;
import com.zynap.talentstudio.arenas.IArenaManager;
import com.zynap.talentstudio.arenas.IArenaMenuHandler;

import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.mock.web.MockHttpServletRequest;

public class TestArenaInterceptor extends ZynapMockControllerTest {

    protected void setUp() throws Exception {
        super.setUp();

        arenaInterceptor = (ArenaInterceptor) getBean("arenaInterceptor");
        arenaManager = (IArenaManager) getBean("arenaManager");
        arenaMenuHandler = (IArenaMenuHandler) getBean("arenaMenuHandler");

        setUserSession(new UserSession(getAdminUserPrincipal(), arenaMenuHandler), mockRequest);
    }

    /**
     * Check that acessing a url outside an arena returns true always.
     * @throws Exception
     */
    public void testPreHandle() throws Exception {

        final String currentArenaId = "currentarena";
        setCurrentArenaId(currentArenaId);

        setURI(mockRequest, "/home.htm");
        assertTrue(arenaInterceptor.preHandle(mockRequest, mockResponse, null));

        // check that user session has not lost its current arena
        final UserSession newUserSession = ZynapWebUtils.getUserSession(mockRequest);
        assertEquals(currentArenaId, newUserSession.getCurrentArenaId());
    }

    /**
     * Check that changing arena changes current arena id in UserSession.
     * @throws Exception
     */
    public void testPreHandleCurrentArenaIdChange() throws Exception {

        final String currentArenaId = "currentarena";
        setCurrentArenaId(currentArenaId);

        // go to admin arena
        setURI(mockRequest, "/admin/");
        assertTrue(arenaInterceptor.preHandle(mockRequest, mockResponse, null));

        // check that current arena has changed
        final UserSession newUserSession = ZynapWebUtils.getUserSession(mockRequest);
        assertEquals(IArenaManager.ADMIN_MODULE, newUserSession.getCurrentArenaId());
    }

    /**
     * Test that you cannot access an inactive arena.
     * @throws Exception
     */
    public void testPreHandleInactiveArena() throws Exception {

        final String currentArenaId = "currentarena";
        setCurrentArenaId(currentArenaId);

        // disable admin arena
        final Arena adminArena = arenaManager.getArena(IArenaManager.ADMIN_MODULE);
        adminArena.setActive(false);
        arenaManager.updateArena(adminArena);
        arenaMenuHandler.reload();

        // go to admin arena - should fail
        setURI(mockRequest, "/admin/home.htm");
        assertFalse(arenaInterceptor.preHandle(mockRequest, mockResponse, null));

        final UserSession newUserSession = ZynapWebUtils.getUserSession(mockRequest);
        //assertNull(newUserSession.getCurrentArenaId());
        assertEquals("when authorization fails your current arena stays the same as the one you came from", currentArenaId, newUserSession.getCurrentArenaId());
    }

    private void setURI(MockHttpServletRequest mockRequest, String uri) {
        mockRequest.setServletPath(uri);
        mockRequest.setRequestURI(BASE_URL + uri);
    }

    private void setCurrentArenaId(final String currentArenaId) {
        // get user session and set current arena id
        final UserSession userSession = ZynapWebUtils.getUserSession(mockRequest);
        assertNotNull(userSession);
        userSession.setCurrentArenaId(currentArenaId);
    }

    private ArenaInterceptor arenaInterceptor;
    private IArenaManager arenaManager;
    private IArenaMenuHandler arenaMenuHandler;
}
package com.zynap.domain;

import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.ZynapDatabaseTestCase;
import com.zynap.talentstudio.security.permits.IPermitManagerDao;
import com.zynap.talentstudio.security.users.IUserService;

import java.util.Collection;
import java.util.Iterator;

/**
 * User: amark
 * Date: 03-May-2006
 * Time: 09:56:11
 */
public class TestUserSession extends ZynapDatabaseTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        arenaMenuHandler = new ArenaMenuHandler();
        arenaManager = (IArenaManager) getBean("arenaManager");
        arenaMenuHandler.setArenaManager(arenaManager);
        arenaMenuHandler.afterPropertiesSet();

        userService = (IUserService) getBean("userService");
        permitManagerDao = (IPermitManagerDao) getBean("permitManDao");
    }

    protected String getDataSetFileName() {
        return "test-data.xml";
    }

    public void testGetArenas() throws Exception {

        // home user only has 1 arena
        final UserSession homeUserSession = getUserSession(HOME_USER_ID);
        Collection arenas = homeUserSession.getArenas();
        assertEquals(1, arenas.size());
        assertEquals(IArenaManager.MYZYNAP_MODULE, ((Arena) arenas.iterator().next()).getArenaId());

        // talent identifier has 2 arenas
        final UserSession userSession = getUserSession(TALENT_IDENTIFIER_USER_ID);
        arenas = userSession.getArenas();
        assertEquals(2, arenas.size());
        final Iterator iterator = arenas.iterator();
        assertEquals(IArenaManager.MYZYNAP_MODULE, ((Arena) iterator.next()).getArenaId());
        assertEquals(IArenaManager.TALENT_IDENTIFICATION_MODULE, ((Arena) iterator.next()).getArenaId());
    }

    /**
     * Test that home arena menu sections that hold reports are hidden when users do not have access to associated arena.
     *
     * @throws Exception
     */
    public void testHomeMenuSectionFiltering() throws Exception {

        final String arenaId = IArenaManager.MYZYNAP_MODULE;
        final Arena homeArena = arenaManager.getArena(arenaId);
        final Collection menuSections = homeArena.getMenuSections();

        // check that home user has no report menu sections in home arena
        final UserSession homeUserSession = getUserSession(HOME_USER_ID);
        homeUserSession.setCurrentArenaId(arenaId);

        Collection filteredMenuSections = homeUserSession.getMenuSections();
        assertTrue(menuSections.containsAll(filteredMenuSections));

        // check that there are no report short-cut menu sections for home user
        for (Iterator iterator = filteredMenuSections.iterator(); iterator.hasNext();) {
            MenuSection menuSection = (MenuSection) iterator.next();
            assertFalse(menuSection.isHomeArenaReportMenuSection());
        }
    }

    /**
     * Test that home arena menu sections that hold reports are hidden when users has access to associated arena.
     * @throws Exception
     */
    public void testHomeMenuSectionFilteringTalentIdentifier() throws Exception {

        final String arenaId = IArenaManager.MYZYNAP_MODULE;
        final Arena homeArena = arenaManager.getArena(arenaId);
        final Collection menuSections = homeArena.getMenuSections();

        // check that talent identifier user has talent identifier report menu section only in home arena
        final UserSession userSession = getUserSession(TALENT_IDENTIFIER_USER_ID);
        userSession.setCurrentArenaId(arenaId);

        Collection filteredMenuSections = userSession.getMenuSections();
        assertTrue(menuSections.containsAll(filteredMenuSections));

        // check report menu sections - should only be 1
        for (Iterator iterator = filteredMenuSections.iterator(); iterator.hasNext();) {
            MenuSection menuSection = (MenuSection) iterator.next();

            if (IArenaManager.TALENT_IDENTIFICATION_MODULE.equals(menuSection.getId())) {
                assertTrue(menuSection.isHomeArenaReportMenuSection());
            } else {
                assertFalse(menuSection.isHomeArenaReportMenuSection());
            }
        }
    }

    public void testHomeMenuSectionFilteringAllArenas() throws Exception {

        final String arenaId = IArenaManager.MYZYNAP_MODULE;
        final Arena homeArena = arenaManager.getArena(arenaId);
        final Collection menuSections = homeArena.getMenuSections();

        // check that all arenas user has all report menu sections in home arena
        final UserSession userSession = getUserSession(ALL_ARENAS_USER_ID);
        userSession.setCurrentArenaId(arenaId);

        Collection filteredMenuSections = userSession.getMenuSections();
        assertTrue(menuSections.containsAll(filteredMenuSections));
        // no longer valid as reports has been added by default but if no reports then this will not appear
        //assertEquals(menuSections.size(), filteredMenuSections.size());
    }

    public void testReloadMenus() throws Exception {
        
        UserSession userSession = getUserSession(HOME_USER_ID);

        // clear arenas
        userSession.getArenas().clear();

        // reload and check
        userSession.reloadMenus();
        final Collection arenas = userSession.getArenas();
        assertFalse(arenas.isEmpty());
    }

    /**
     * Test get menu items with unspecified arena and menu section ids.
     *
     * @throws Exception
     */
    public void testGetMenuItemsNoIds() throws Exception {
        UserSession userSession = getUserSession(HOME_USER_ID);

        final Collection menuItems = userSession.getMenuItems();
        assertNotNull(menuItems);
        assertTrue(menuItems.isEmpty());
    }

    /**
     * Test get menu sections with unspecified arena id.
     *
     * @throws Exception
     */
    public void testGetMenuSectionsNoArenaId() throws Exception {
        UserSession userSession = getUserSession(HOME_USER_ID);

        final Collection menuSections = userSession.getMenuSections();
        assertNotNull(menuSections);
        assertTrue(menuSections.isEmpty());
    }

    public void testGetMenuItems() throws Exception {

        final String arenaId = IArenaManager.MYZYNAP_MODULE;

        final Arena selectedArena = arenaManager.getArena(arenaId);
        final MenuSection selectedMenuSection = selectedArena.getMenuSections().iterator().next();
        final String menuSectionId = selectedMenuSection.getId();

        UserSession userSession = getUserSession(HOME_USER_ID);
        userSession.setCurrentArenaId(arenaId);
        userSession.setCurrentMenuSectionId(menuSectionId);

        final Collection menuItems = userSession.getMenuItems();
        assertFalse(menuItems.isEmpty());

        for (Iterator iterator = menuItems.iterator(); iterator.hasNext();) {
            final MenuItem menuItem = (MenuItem) iterator.next();
            final Arena arena = menuItem.getArena();
            assertEquals(arenaId, arena.getArenaId());
            assertEquals(menuSectionId, menuItem.getMenuSection().getId());
        }
    }

    public void testGetMenuSections() throws Exception {

        final String arenaId = IArenaManager.MYZYNAP_MODULE;

        UserSession userSession = getUserSession(HOME_USER_ID);
        userSession.setCurrentArenaId(arenaId);

        final Collection menuSections = userSession.getMenuSections();
        assertFalse(menuSections.isEmpty());

        for (Iterator iterator = menuSections.iterator(); iterator.hasNext();) {
            final MenuSection menuSection = (MenuSection) iterator.next();
            final Arena arena = menuSection.getArena();
            assertEquals(arenaId, arena.getArenaId());
        }
    }

    /**
     * Test inactive arenas are removed from list of arenas and also
     *
     * @throws Exception
     */
    public void testInactiveArenaFiltering() throws Exception {

        // make talent identifier arena inactive and reload in arena menu handler
        final Arena selectedArena = arenaManager.getArena(IArenaManager.TALENT_IDENTIFICATION_MODULE);
        selectedArena.setActive(false);
        arenaManager.updateArena(selectedArena);
        arenaMenuHandler.reload();

        // check number of inactive arenas has increased
        Collection newInactiveArenas = arenaMenuHandler.getInactiveArenas();
        assertEquals(1, newInactiveArenas.size());

        // get user session and check arenas
        final UserSession userSession = getUserSession(TALENT_IDENTIFIER_USER_ID);
        final Collection arenas = userSession.getArenas();
        assertEquals(1, arenas.size());

        // check we have the home arena
        final Arena homeArena = (Arena) arenas.iterator().next();
        final String arenaId = homeArena.getArenaId();
        assertEquals(IArenaManager.MYZYNAP_MODULE, arenaId);

        // get menu sections
        userSession.setCurrentArenaId(arenaId);
        final Collection filteredMenuSections = userSession.getMenuSections();

        // check that all the filtered menu sections belng to the home arena
        assertTrue(homeArena.getMenuSections().containsAll(filteredMenuSections));

        // check that there are no report short-cut menu sections
        for (Iterator iterator = filteredMenuSections.iterator(); iterator.hasNext();) {
            MenuSection menuSection = (MenuSection) iterator.next();
            assertFalse(menuSection.isHomeArenaReportMenuSection());
        }
    }

    private UserSession getUserSession(Long userId) throws TalentStudioException {

        User user = (User) userService.findById(userId);
        final Collection accessPermits = permitManagerDao.getAccessPermits(userId);

        UserPrincipal userPrincipal = new UserPrincipal(user, accessPermits);
        UserSession userSession = new UserSession(userPrincipal, arenaMenuHandler);

        // check arenas have been set by constructor
        assertFalse(userSession.getArenas().isEmpty());

        return userSession;
    }

    private IArenaManager arenaManager;
    private IUserService userService;
    private IPermitManagerDao permitManagerDao;
    private ArenaMenuHandler arenaMenuHandler;

    private static final Long HOME_USER_ID = new Long(-81);
    private static final Long TALENT_IDENTIFIER_USER_ID = new Long(-82);
    private static final Long ALL_ARENAS_USER_ID = new Long(-83);
}

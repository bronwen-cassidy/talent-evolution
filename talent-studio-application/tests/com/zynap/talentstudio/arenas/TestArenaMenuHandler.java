package com.zynap.talentstudio.arenas;

/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */

import com.zynap.domain.UserPrincipal;
import com.zynap.domain.admin.User;
import com.zynap.domain.admin.UserType;
import com.zynap.talentstudio.ZynapDatabaseTestCase;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.TabularReport;
import com.zynap.talentstudio.audit.SessionLog;
import com.zynap.talentstudio.security.permits.IPermit;
import com.zynap.talentstudio.security.permits.IPermitManagerDao;
import com.zynap.talentstudio.security.users.IUserService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class TestArenaMenuHandler extends ZynapDatabaseTestCase {

    protected String getDataSetFileName() {
        return "users-test-data.xml";
    }

    protected void setUp() throws Exception {
        super.setUp();

        arenaMenuHandler = new ArenaMenuHandler();
        IArenaManager arenaManager = (IArenaManager) getBean("arenaManager");
        arenaMenuHandler.setArenaManager(arenaManager);
        arenaMenuHandler.reload();

        permitManagerDao = (IPermitManagerDao) applicationContext.getBean("permitManDao");
    }

    public void testAfterPropertiesSet() throws Exception {
        // to test this we will get a list of menus back
        Collection arenas = arenaMenuHandler.getActiveArenas();
        assertNotNull(arenas);
    }

    /**
     * Create a collection of permits by using a subset from the permit manager dao
     * then assert that the menus return contain only that subset.
     *
     * @throws Exception
     */
    public void testGetSecuredArenas() throws Exception {

        IUserService userService = (IUserService) getBean("userService");
        User user = (User) userService.findById(new Long(DANIEL_USER_ID));
        List<IPermit> permits = permitManagerDao.getActiveAccessPermits();

        UserPrincipal userPrincipal = new UserPrincipal(user, permits, new SessionLog(new Long(-22), "queyrtntbsjsisurhf23jfgdhsjksks"));
        // create a subset of these the first 5 for example
        //Collection subset = permits.subList(7, 13);
        // only 5 menuItems should be in the result        
        Collection<Arena> actualMenus = arenaMenuHandler.getSecuredArenas(userPrincipal);
        // get the menuItems into a temp
        int count = 0;
        for (Iterator iterator = actualMenus.iterator(); iterator.hasNext();) {
            Arena arena = (Arena) iterator.next();
            Collection menuSections = arena.getMenuSections();
            for (Iterator iterator1 = menuSections.iterator(); iterator1.hasNext();) {
                MenuSection menuSection = (MenuSection) iterator1.next();
                count += menuSection.getMenuItems().size();
            }
        }

        assertEquals(5, count);        
    }

    public void testGetSecuredArenasUserType() throws Exception {
        List<IPermit> permits = permitManagerDao.getActiveAccessPermits();

        IUserService userService = (IUserService) getBean("userService");
        User user = (User) userService.findById(AGATHA_USER_ID);

        UserPrincipal userPrincipal = new UserPrincipal(user, permits, new SessionLog(new Long(-22), "queyrtntbsjsisurhf23jfgdhsjksks"));
        // create a subset of these the first 5 for example
        //Collection subset = permits.subList(7, 13);
        // only 5 menuItems should be in the result
        Collection<Arena> actualMenus = arenaMenuHandler.getSecuredArenas(userPrincipal);

        int count = 0;
        for (Iterator iterator = actualMenus.iterator(); iterator.hasNext();) {
            Arena arena = (Arena) iterator.next();
            Collection menuSections = arena.getMenuSections();
            for (Iterator iterator1 = menuSections.iterator(); iterator1.hasNext();) {
                MenuSection menuSection = (MenuSection) iterator1.next();
                count += menuSection.getMenuItems().size();
            }
        }
        // user get less menuitems than a subject user when they have the home arena role
        assertEquals(4, count);
    }

    public void testGetSecuredArenasWithReportSorted() throws Exception {
        addReportToHomeArena();
        List<IPermit> permits = permitManagerDao.getActiveAccessPermits();
        User user = new User(new Long(12), "nanny", "penny", "lovett");
        user.setUserType(UserType.USER);
        UserPrincipal userPrincipal = new UserPrincipal(user, permits, new SessionLog(new Long(-22), "queyrtntbsjsisurhf23jfgdhsjksks"));
        Collection actualMenus = arenaMenuHandler.getSecuredArenas(userPrincipal);
        int previousSortOrder = 0;
        // find the home arena and assert the sort_orders
        for (Iterator iterator = actualMenus.iterator(); iterator.hasNext();) {
            Arena arena = (Arena) iterator.next();
            if("MYZYNAPMODULE".equals(arena.getArenaId())) {
                Collection menuSections = arena.getMenuSections();
                for (Iterator iterator1 = menuSections.iterator(); iterator1.hasNext();) {
                    MenuSection menuSection = (MenuSection) iterator1.next();
                    if(!"ACCOUNT".equals(menuSection.getId()) || !"TODO".equals(menuSection.getId())) {
                        assertTrue(previousSortOrder < menuSection.getSortOrder());
                        previousSortOrder = menuSection.getSortOrder();
                    }
                }
            }
        }
    }

    public void testGetAttributableArenas() throws Exception {
        final Collection arenasAllowedAttributes = arenaMenuHandler.getActiveArenas();
        assertNotNull(arenasAllowedAttributes);
        assertFalse(arenasAllowedAttributes.contains(IArenaManager.ADMIN_MODULE));
    }

    public void testReload() throws Exception {

        final Collection originalArenas = arenaMenuHandler.getActiveArenas();
        assertNotNull(originalArenas);

        arenaMenuHandler.reload();
        final Collection newArenas = arenaMenuHandler.getActiveArenas();
        assertNotNull(newArenas);

        assertEquals(originalArenas, newArenas);
    }

    private void addReportToHomeArena() throws Exception {
            Report report = new TabularReport("Test sorted on home", "description", "PUBLIC");
            report.addColumn(new Column("test home", "coreDetail.firstname", new Integer(0), "TEXT", "N/A"));
            IArenaManager arenaManager = (IArenaManager) applicationContext.getBean("arenaManager");
            Arena arena = arenaManager.getArena(IArenaManager.MYZYNAP_MODULE);
            report.addMenuItem(new MenuItem("test sorted", getMenuSection(IArenaManager.PERFORMANCE_MANAGEMENT_MODULE, arena.getMenuSections()), "home.htm"));
            report.addMenuItem(new MenuItem("test sorted", getMenuSection(IArenaManager.TALENT_IDENTIFICATION_MODULE, arena.getMenuSections()), "home.htm"));
            report.addMenuItem(new MenuItem("test sorted", getMenuSection(IArenaManager.SUCCESSION_MODULE, arena.getMenuSections()), "home.htm"));
            IReportService reportService = (IReportService) applicationContext.getBean("reportService");
            reportService.create(report);
        }

        private MenuSection getMenuSection(final String menuSectionId, final Collection menuSections) {
            return (MenuSection) CollectionUtils.find(menuSections, new Predicate() {
                public boolean evaluate(Object object) {
                    return menuSectionId.equals(((MenuSection) object).getId());
                }
            });
        }
    


    private ArenaMenuHandler arenaMenuHandler;
    private IPermitManagerDao permitManagerDao;    
    private static final Long DANIEL_USER_ID = new Long(-133);
    private static final Long AGATHA_USER_ID = new Long(-135);
}
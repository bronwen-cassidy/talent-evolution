package com.zynap.talentstudio.web.analysis.reports;

/**
 * User: amark
 * Date: 05-Sep-2005
 * Time: 17:17:17
 */

import com.zynap.talentstudio.ZynapTestCase;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.TabularReport;
import com.zynap.talentstudio.arenas.Arena;
import com.zynap.talentstudio.arenas.IArenaManager;
import com.zynap.talentstudio.arenas.MenuItem;
import com.zynap.talentstudio.arenas.MenuSection;
import com.zynap.talentstudio.arenas.MenuSectionPK;
import com.zynap.talentstudio.web.arena.MenuItemWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class TestReportWrapperBean extends ZynapTestCase {

    public void testSetHomePageMenuItems() throws Exception {

        final String label = "test label";

        // create some fake menu sections
        List originalMenuSections = buildMenuSections();
        MenuSection menuSection1 = (MenuSection) originalMenuSections.get(0);
        MenuSection menuSection2 = (MenuSection) originalMenuSections.get(1);

        Collection homePageMenuSections = new ArrayList(1);
        MenuSection homePageMenuSection1 = new MenuSection(new MenuSectionPK(menuSection1.getPrimaryKey().getArenaId(), IArenaManager.MYZYNAP_MODULE), "home page reports", 0);
        homePageMenuSections.add(homePageMenuSection1);

        // create a fake report with 1 menu item corresponding to the first menu section
        final Report originalReport = new TabularReport();
        originalReport.addMenuItem(new MenuItem(label, menuSection1, getRunReportURL()));
        reportWrapperBean = new ReportWrapperBean(originalReport, originalMenuSections, homePageMenuSections, getRunReportURL());

        assertTrue(reportWrapperBean.hasAssignedMenuItems());

        // check number and state of menuItemWrappers
        final Collection menuItemWrappers = reportWrapperBean.getMenuItemWrappers();
        int count = 0;
        assertEquals(originalMenuSections.size(), menuItemWrappers.size());
        for (Iterator iterator = menuItemWrappers.iterator(); iterator.hasNext();count++) {
            MenuItemWrapper menuItemWrapper = (MenuItemWrapper) iterator.next();
            assertNotNull(menuItemWrapper.getLabel());

            if (count==0) {
                assertEquals(menuSection1, menuItemWrapper.getMenuSection());
                assertEquals(homePageMenuSection1, menuItemWrapper.getHomePageMenuSection());
                assertTrue(menuItemWrapper.isSupportsHomePage());
            }
            else {
                assertEquals(menuSection2, menuItemWrapper.getMenuSection());
                assertNull(menuItemWrapper.getHomePageMenuSection());
                assertFalse(menuItemWrapper.isSupportsHomePage());
            }
        }

        // clear all menu sections
        reportWrapperBean.setActiveMenuItems(null);

        // check they are cleared
        reportWrapperBean.setHomePageMenuItems(null);
        assertTrue(reportWrapperBean.getModifiedReport().getMenuItems().isEmpty());

        // select the first one and check that the report now has that as the selected menu item
        reportWrapperBean.setHomePageMenuItems(new String[]{"0"});
        assertTrue(reportWrapperBean.hasAssignedMenuItems());

        final Set assignedMenuItems = reportWrapperBean.getModifiedReport().getMenuItems();
        assertEquals(1, assignedMenuItems.size());
        final MenuItem assignedMenuItem = (MenuItem) assignedMenuItems.iterator().next();
        assertEquals(homePageMenuSection1, assignedMenuItem.getMenuSection());
        assertEquals(originalReport, assignedMenuItem.getReport());

    }

    public void testReportWrapperBean() throws Exception {

        final String label = "test label";

        // create some fake menu sections
        List originalMenuSections = buildMenuSections();
        MenuSection menuSection1 = (MenuSection) originalMenuSections.get(0);

        // create a fake report with 1 menu item corresponding to the first menu section
        final Report originalReport = new TabularReport();
        originalReport.addMenuItem(new MenuItem(label, menuSection1, getRunReportURL()));
        reportWrapperBean = new ReportWrapperBean(originalReport, originalMenuSections, null, getRunReportURL());

        final Collection menuItemWrappers = reportWrapperBean.getMenuItemWrappers();
        assertEquals(originalMenuSections.size(), menuItemWrappers.size());

        // check that only the first menu section is selected
        for (Iterator iterator = menuItemWrappers.iterator(); iterator.hasNext();) {
            MenuItemWrapper menuItemWrapper = (MenuItemWrapper) iterator.next();
            final MenuSection value = menuItemWrapper.getMenuSection();
            assertTrue(value != null);
            assertEquals(menuItemWrapper.getLabel(), value.getArena().getLabel());

            if (value.equals(menuSection1)) {
                assertTrue(menuItemWrapper.isSelected());
            } else {
                assertFalse(menuItemWrapper.isSelected());
            }
        }

        assertTrue(reportWrapperBean.hasAssignedMenuItems());

        // checks assigned menu items
        final Report modifiedReport = reportWrapperBean.getModifiedReport();
        final Set assignedMenuItems = modifiedReport.getMenuItems();
        assertEquals(1, assignedMenuItems.size());
        for (Iterator iterator = assignedMenuItems.iterator(); iterator.hasNext();) {
            MenuItem menuItem = (MenuItem) iterator.next();
            assertEquals(getRunReportURL(), menuItem.getUrl());
            assertEquals(modifiedReport, menuItem.getReport());
            assertEquals(menuSection1, menuItem.getMenuSection());
            assertTrue(menuItem.isReportMenuItem());
        }
    }

    public void testSetActiveMenuItems() throws Exception {

        final String url = "runreport.htm";
        final String label = "test label";        

        // create some fake menu sections
        List originalMenuSections = buildMenuSections();
        MenuSection menuSection1 = (MenuSection) originalMenuSections.get(0);
        MenuSection menuSection2 = (MenuSection) originalMenuSections.get(1);

        // create a fake report with 1 menu item corresponding to the first menu section
        final Report originalReport = new TabularReport();
        originalReport.addMenuItem(new MenuItem(label, menuSection1, url));
        reportWrapperBean = new ReportWrapperBean(originalReport, originalMenuSections, null, getRunReportURL());

        // set 2nd menu item to be active
        reportWrapperBean.setActiveMenuItems(new String[]{"1"});
        assertTrue(reportWrapperBean.hasAssignedMenuItems());

        // check that correct menu items are active
        final Collection menuItemWrappers = reportWrapperBean.getMenuItemWrappers();
        for (Iterator iterator = menuItemWrappers.iterator(); iterator.hasNext();) {
            MenuItemWrapper menuItemWrapper = (MenuItemWrapper) iterator.next();
            if (menuItemWrapper.getMenuSection().equals(menuSection1)) {
                assertFalse(menuItemWrapper.isSelected());
            } else {
                assertTrue(menuItemWrapper.isSelected());
            }
        }

        // check that modified report has 1 menu item
        final Report modifiedReport = reportWrapperBean.getModifiedReport();
        final Set modifiedMenuItems = modifiedReport.getMenuItems();
        assertEquals(1, modifiedMenuItems.size());

        final MenuItem newMenuItem = (MenuItem) modifiedMenuItems.iterator().next();
        assertEquals(menuSection2, newMenuItem.getMenuSection());
    }

    public void testHasAssignedMenuItemsNoItems() throws Exception {

        reportWrapperBean = new ReportWrapperBean(new TabularReport(), null, null, getRunReportURL());
        assertFalse(reportWrapperBean.hasAssignedMenuItems());
    }

    public void testGetNewMenuSections() throws Exception {

        reportWrapperBean = new ReportWrapperBean(new TabularReport(), null, null, getRunReportURL());
        final String[] activeMenuItems = reportWrapperBean.getActiveMenuItems();
        assertEquals(0, activeMenuItems.length);
    }

    public void testSetNewMenuSections() throws Exception {

        // create some fake menu sections
        List originalMenuSections = buildMenuSections();

        // create a fake report with no menu items
        final Report originalReport = new TabularReport();
        reportWrapperBean = new ReportWrapperBean(originalReport, originalMenuSections, null, getRunReportURL());

        // should have no menu sections assigned
        assertFalse(reportWrapperBean.hasAssignedMenuItems());

        // test with zero and out of bounds element
        reportWrapperBean.setActiveMenuItems(new String[]{"0", Integer.toString(originalMenuSections.size())});

        // should still be considered as having menu sections
        assertTrue(reportWrapperBean.hasAssignedMenuItems());

        final Report modifiedReport = reportWrapperBean.getModifiedReport();
        assertEquals(1, modifiedReport.getMenuItems().size());
    }

    private String getRunReportURL() {
        return "runreport.htm";
    }

    private List buildMenuSections() {

        List originalMenuSections = new ArrayList();

        MenuSection menuSection1 = new MenuSection(new MenuSectionPK("id", "arena 1"), "section 1", 0);
        menuSection1.setArena(new Arena(null, "arena 1"));
        originalMenuSections.add(menuSection1);

        MenuSection menuSection2 = new MenuSection(new MenuSectionPK("id", "arena 2"), "section 2", 1);
        menuSection2.setArena(new Arena(null, "arena 2"));
        originalMenuSections.add(menuSection2);

        return originalMenuSections;
    }

    ReportWrapperBean reportWrapperBean;
}
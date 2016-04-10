/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.arenas;

/**
 * Class or Interface description.
 * 
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */

import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.common.SecurityConstants;
import com.zynap.domain.admin.UserType;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class TestHibernateArenaManagerDao extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        hibernateArenaManagerDao = (HibernateArenaManagerDao) applicationContext.getBean("arenaManDao");
    }

    public void testGetActiveArenas() throws Exception {
        Collection arenas = hibernateArenaManagerDao.getActiveArenas();

        // check ordered by sorted order and that all arenas are active
        Integer prevSortOrder = null;
        for (Iterator iterator = arenas.iterator(); iterator.hasNext();) {
            Arena arena = (Arena) iterator.next();
            assertTrue(arena.isActive());

            final Integer currentArenaSortOrder = new Integer(arena.getSortOrder());
            if (prevSortOrder != null) {
                final int i = prevSortOrder.compareTo(currentArenaSortOrder);
                assertTrue(i < 0);
            }

            prevSortOrder = currentArenaSortOrder;
        }
    }

    public void testGetArenas() throws Exception {
        final Collection<Arena> arenas = hibernateArenaManagerDao.getArenas();
        checkArenaOrder(arenas);

        for (Iterator iterator = arenas.iterator(); iterator.hasNext();) {
            final Arena arena = (Arena) iterator.next();
            final Collection<MenuSection> menuSections = arena.getMenuSections();
            checkMenuSectionOrder(menuSections);

            for (Iterator iterator1 = menuSections.iterator(); iterator1.hasNext();) {
                final MenuSection menuSection = (MenuSection) iterator1.next();
                final Collection<MenuItem> menuItems = menuSection.getMenuItems();
                checkMenuItemOrder(menuItems);
            }
        }
    }

    public void testGetArena() throws Exception {
        Arena arena = hibernateArenaManagerDao.findById(IArenaManager.ADMIN_MODULE);
        assertEquals(IArenaManager.ADMIN_MODULE, arena.getArenaId());
    }

    public void testGetArena_label() throws Exception {
        Arena arena = hibernateArenaManagerDao.findById(IArenaManager.ADMIN_MODULE);
        assertEquals("Administration", arena.getLabel());
    }

    public void testGetArena_Sections() throws Exception {
        Arena arena = hibernateArenaManagerDao.findById(IArenaManager.ADMIN_MODULE);
        Collection<MenuSection> menuSections = arena.getMenuSections();

        checkMenuSectionOrder(menuSections);
    }

    public void testGetArena_SectionMenuItems() throws Exception {
        Arena arena = hibernateArenaManagerDao.findById(IArenaManager.ADMIN_MODULE);
        Collection menuSections = arena.getMenuSections();
        MenuSection menusection = (MenuSection) menuSections.iterator().next();
        Collection menuItems = menusection.getMenuItems();
        assertFalse(menuItems.isEmpty());

        for (Iterator iterator = menuItems.iterator(); iterator.hasNext();) {
            MenuItem menuItem = (MenuItem) iterator.next();
            assertNotNull(menuItem.getPermit());
        }
    }

    public void testGetMenuSections() throws Exception {
        Collection sections = hibernateArenaManagerDao.getMenuSections(IArenaManager.ORGANISATION_MODULE);
        assertFalse(sections.isEmpty());
    }

    public void testGetArenas_SessionTimeout() throws Exception {
        Collection arenas = hibernateArenaManagerDao.getArenas();
        for (Iterator iterator = arenas.iterator(); iterator.hasNext();) {
            Arena arena = (Arena) iterator.next();
            assertNotSame(new Integer(0), new Integer(arena.getSessionTimeout()));
        }
    }

    public void testUpdate() throws Exception {

        final int newSessionTimeout = 112;

        final Arena arena = hibernateArenaManagerDao.findById(IArenaManager.ADMIN_MODULE);
        arena.setSessionTimeout(newSessionTimeout);

        hibernateArenaManagerDao.update(arena);

        Collection actual = hibernateArenaManagerDao.getArenas();
        for (Iterator iterator = actual.iterator(); iterator.hasNext();) {
            Arena foundArena = (Arena) iterator.next();
            if (foundArena.getArenaId().equals(IArenaManager.ADMIN_MODULE)) {
                assertEquals(newSessionTimeout, foundArena.getSessionTimeout());
            } else {
                assertFalse(foundArena.getSessionTimeout() == newSessionTimeout);
            }
        }
    }

    public void testFindByInvalidID() throws Exception {
        try {
            hibernateArenaManagerDao.findById("no such arena");
            fail("Should have thrown an Exception");
        } catch (DomainObjectNotFoundException ex) {
        }
    }

    public void testGetReportMenuSections() throws Exception {
        final Collection menuSections = hibernateArenaManagerDao.getReportMenuSections();
        assertFalse(menuSections.isEmpty());

        for (Iterator iterator = menuSections.iterator(); iterator.hasNext();) {
            MenuSection menuSection = (MenuSection) iterator.next();
            assertEquals(SecurityConstants.REPORTS_CONTENT, menuSection.getPrimaryKey().getId());
        }
    }

    public void testGetReportMenuSectionsSortedByArena() throws Exception {
        final Collection menuSections = hibernateArenaManagerDao.getReportMenuSections();
        assertFalse(menuSections.isEmpty());

        int lastSortOrder = -1;

        for (Iterator iterator = menuSections.iterator(); iterator.hasNext();) {
            MenuSection menuSection = (MenuSection) iterator.next();
            int sortOrder = menuSection.getArena().getSortOrder();
            assertTrue(lastSortOrder < sortOrder);
            lastSortOrder = sortOrder;
        }
    }

    public void testFindByID() throws Exception {
        final Arena arena = hibernateArenaManagerDao.findById(IArenaManager.ADMIN_MODULE);
        assertNotNull(arena);
        assertEquals(IArenaManager.ADMIN_MODULE, arena.getArenaId());
    }

    public void testGetHideableArenas() throws Exception {

        final Collection<Arena> hideableArenas = hibernateArenaManagerDao.getHideableArenas();
        assertFalse(hideableArenas.isEmpty());
        for (Iterator iterator = hideableArenas.iterator(); iterator.hasNext();) {
            Arena arena = (Arena) iterator.next();
            assertTrue(arena.isHideable());
        }

        checkArenaOrder(hideableArenas);
    }

    public void testGetHomePageReportMenuSections() throws Exception {

        final Collection homePageMenuSections = hibernateArenaManagerDao.getHomePageReportMenuSections();
        assertFalse(homePageMenuSections.isEmpty());
        for (Iterator iterator = homePageMenuSections.iterator(); iterator.hasNext();) {
            MenuSection menuSection = (MenuSection) iterator.next();
            assertEquals(IArenaManager.MYZYNAP_MODULE, menuSection.getPrimaryKey().getArenaId());
        }
    }

    public void testGetHomePageReportMenuSectionsSorted() throws Exception {

        final Collection homePageMenuSections = hibernateArenaManagerDao.getHomePageReportMenuSections();
        assertFalse(homePageMenuSections.isEmpty());
        int lastSortOrder = -1;

        for (Iterator iterator = homePageMenuSections.iterator(); iterator.hasNext();) {
            MenuSection menuSection = (MenuSection) iterator.next();
            int sortOrder = menuSection.getArena().getSortOrder();
            assertTrue(lastSortOrder <= sortOrder);
            lastSortOrder = sortOrder;
        }
    }

    public void testGetSortedArenas() throws Exception {
        final Collection sortedArenas = hibernateArenaManagerDao.getSortedArenas();
        assertFalse(sortedArenas.isEmpty());

        Arena prevArena = null;
        for (Iterator iterator = sortedArenas.iterator(); iterator.hasNext();) {
            Arena arena = (Arena) iterator.next();

            if (prevArena != null) {
                assertTrue(arena.getSortOrder() >= prevArena.getSortOrder());
            }

            prevArena = arena;
        }
    }

    /**
     * Checks that arenas are ordered by label.
     *
     * @param arenas
     */
    private void checkArenaOrder(Collection<Arena> arenas) {

        assertFalse(arenas.isEmpty());

        String prevArenaLabel = null;
        for (Arena arena : arenas) {

            if (prevArenaLabel != null) {
                final int i = prevArenaLabel.compareTo(arena.getLabel());
                assertTrue(i < 0);
            }

            prevArenaLabel = arena.getLabel();
        }
    }

    /**
     * Checks that menu sections are ordered by sort order.
     *
     * @param menuSections
     */
    private void checkMenuSectionOrder(Collection<MenuSection> menuSections) {

        assertFalse(menuSections.isEmpty());

        Integer prevSortOrder = null;
        for (MenuSection menuSection : menuSections) {

            final Integer currentArenaSortOrder = new Integer(menuSection.getSortOrder());
            if (prevSortOrder != null) {
                final int i = prevSortOrder.compareTo(currentArenaSortOrder);
                assertTrue(i < 0);
            }

            prevSortOrder = currentArenaSortOrder;
        }
    }

    /**
     * Checks that menu items are ordered by sort order.
     *
     * @param menuItems
     */
    private void checkMenuItemOrder(Collection<MenuItem> menuItems) {

        Integer prevSortOrder = null;
        for (MenuItem menuItem : menuItems) {

            final Integer currentArenaSortOrder = new Integer(menuItem.getSortOrder());
            if (prevSortOrder != null) {
                final int i = prevSortOrder.compareTo(currentArenaSortOrder);
                assertTrue(i < 0);
            }

            prevSortOrder = currentArenaSortOrder;
        }

    }

    public void testGetMenuItems() throws Exception {
        final List<MenuItem> permitList = hibernateArenaManagerDao.getMenuItems(new Long(1), UserType.USER.toString());
    }

    private HibernateArenaManagerDao hibernateArenaManagerDao;
}

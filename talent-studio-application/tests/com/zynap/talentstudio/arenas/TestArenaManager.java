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

import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.display.DisplayConfig;
import com.zynap.talentstudio.display.DisplayConfigItem;
import com.zynap.talentstudio.display.IDisplayConfigService;
import com.zynap.talentstudio.organisation.Node;

import java.util.Collection;
import java.util.Iterator;

public class TestArenaManager extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        arenaManager = (IArenaManager) applicationContext.getBean("arenaManager");
        displayConfigService = (IDisplayConfigService) getBean("displayConfigService");
    }

    public void testGetMenuSections() throws Exception {
        Collection menuSections = arenaManager.getMenuSections(IArenaManager.ADMIN_MODULE);
        for (Iterator iterator = menuSections.iterator(); iterator.hasNext();) {
            MenuSection menuSection = (MenuSection) iterator.next();
            assertNotNull(menuSection.getPrimaryKey().getId());
            assertNotNull(menuSection.getLabel());
            for (Iterator iterator1 = menuSection.getMenuItems().iterator(); iterator1.hasNext();) {
                MenuItem menuItem = (MenuItem) iterator1.next();
                assertNotNull(menuItem.getUrl());
                assertNotNull(menuItem.getLabel());
                assertNotNull(menuItem.getPermit());
            }
        }
    }

    public void testGetArenas() throws Exception {
        assertNotNull(arenaManager.getArenas());
    }

    public void testDisplayConfigItemCreation() throws Exception {

        // get a display config item - the first one for the position
        final DisplayConfig selectedDisplayConfig = displayConfigService.find(Node.POSITION_UNIT_TYPE_, DisplayConfig.VIEW_TYPE);
        final DisplayConfigItem selectedDisplayConfigItem = selectedDisplayConfig.getDisplayConfigItems().get(0);

        // add it to the arena
        final Arena adminArena = arenaManager.getArena(IArenaManager.ADMIN_MODULE);
        final Collection<ArenaDisplayConfigItem> displayConfigItems = adminArena.getArenaDisplayConfigItems();
        assertNotNull(displayConfigItems);
        displayConfigItems.add(new ArenaDisplayConfigItem(null, adminArena, selectedDisplayConfigItem));
        arenaManager.updateArena(adminArena);
    }

    public void testUpdateArena() throws Exception {
        final String arenaId = IArenaManager.PERFORMANCE_MANAGEMENT_MODULE;
        final String label = "test label";

        // disable arena and change its label
        final Arena arena = arenaManager.getArena(arenaId);
        arena.setActive(false);
        arena.setLabel(label);
        arenaManager.updateArena(arena);

        // check that changes were persisted
        final Arena found = arenaManager.getArena(arenaId);
        assertEquals(arena, found);
        assertEquals(label, found.getLabel());
        assertFalse(found.isActive());

        // check that menu section has been changed as well
        final String homeArenaId = IArenaManager.MYZYNAP_MODULE;
        MenuSection menuSection = arenaManager.getMenuSection(homeArenaId, arenaId);
        assertEquals(label, menuSection.getLabel());
        assertEquals(arenaId, menuSection.getPrimaryKey().getId());
        assertEquals(homeArenaId, menuSection.getPrimaryKey().getArenaId());
    }

    public void testGetArena() throws Exception {
        final String expected = IArenaManager.ADMIN_MODULE;

        final Arena arena = arenaManager.getArena(expected);
        assertNotNull(arena);
        assertEquals(expected, arena.getArenaId());
    }

    private IArenaManager arenaManager;
    private IDisplayConfigService displayConfigService;
}

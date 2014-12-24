package com.zynap.talentstudio.arenas;

/**
 * User: amark
 * Date: 10-Mar-2005
 * Time: 11:42:06
 */

import com.zynap.talentstudio.ZynapTestCase;
import com.zynap.talentstudio.display.DisplayConfig;
import com.zynap.talentstudio.display.DisplayConfigItem;

import java.util.Collection;

public class TestArena extends ZynapTestCase {

    public void testShallowCopy() throws Exception {

        final String id = "arenaId";
        final String label = "label";
        final String url = "url";
        final String description = "description";
        final int sessionTimeout = 100;
        final int sortOrder = 5;
        final boolean active = false;
        final boolean hideable = false;

        final Arena arena = new Arena(id, label, url, description, sessionTimeout, sortOrder, active, hideable);
        final Arena copiedArena = arena.shallowCopy();

        assertNotNull(copiedArena);
        assertFalse(arena == copiedArena);
        assertTrue(copiedArena.getMenuSections().isEmpty());
        assertEquals(id, copiedArena.getArenaId());
        assertEquals(label, copiedArena.getLabel());
        assertEquals(description, copiedArena.getDescription());
        assertEquals(url, copiedArena.getUrl());
        assertEquals(sessionTimeout, copiedArena.getSessionTimeout());
        assertEquals(sortOrder, copiedArena.getSortOrder());
        assertFalse(copiedArena.isActive());
        assertFalse(copiedArena.isHideable());
    }

    public void testAddMenuSection() throws Exception {
        final Arena arena = new Arena();
        assertTrue(arena.getMenuSections().isEmpty());

        arena.addMenuSection(new MenuSection());
        assertEquals(1, arena.getMenuSections().size());
    }

    public void testGetMenuSections() throws Exception {
        final Arena arena = new Arena();
        final Collection menuSections = arena.getMenuSections();
        assertNotNull(menuSections);
        assertTrue(menuSections.isEmpty());
    }

    public void testGetArenaDisplayConfigItems() throws Exception {
        final Arena arena = new Arena();
        final Collection arenaDisplayConfigItems = arena.getArenaDisplayConfigItems();
        assertNotNull(arenaDisplayConfigItems);
        assertTrue(arenaDisplayConfigItems.isEmpty());
    }

    public void testGetArenaDisplayConfigItem() throws Exception {

        final DisplayConfig displayConfig = new DisplayConfig(new Long(-99), "test");
        final DisplayConfigItem displayConfigItem = new DisplayConfigItem();
        displayConfigItem.setDisplayConfig(displayConfig);

        final Arena arena = new Arena("TEST", "test arena");
        final ArenaDisplayConfigItem newItem = new ArenaDisplayConfigItem(new Long(-10), null, displayConfigItem);
        arena.addArenaDisplayConfigItem(newItem);
        assertEquals(arena, newItem.getArena());
        assertEquals(1, arena.getArenaDisplayConfigItems().size());

        final ArenaDisplayConfigItem foundItem = arena.getArenaDisplayConfigItem(displayConfig.getId());
        assertEquals(newItem, foundItem);
    }
}
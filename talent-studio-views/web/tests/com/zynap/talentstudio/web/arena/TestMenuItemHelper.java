package com.zynap.talentstudio.web.arena;

/**
 * User: amark
 * Date: 07-Apr-2006
 * Time: 14:04:58
 */

import com.zynap.talentstudio.ZynapTestCase;
import com.zynap.talentstudio.arenas.Arena;
import com.zynap.talentstudio.arenas.MenuItem;
import com.zynap.talentstudio.arenas.MenuSection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TestMenuItemHelper extends ZynapTestCase {

    public void testGetAssignedArenasNull() throws Exception {

        final List assignedArenas = MenuItemHelper.getAssignedArenas(null);
        assertNotNull(assignedArenas);
        assertEquals(0, assignedArenas.size());
    }

    public void testGetAssignedMenuItems() throws Exception {

        final Arena arena1 = new Arena("ARENA1", "arena1");
        arena1.setSortOrder(9);

        final Arena arena2 = new Arena("ARENA2", "arena2");
        arena2.setSortOrder(-1);

        MenuSection menuSection1 = new MenuSection();
        menuSection1.setArena(arena2);

        MenuSection menuSection2 = new MenuSection();
        menuSection2.setArena(arena1);

        MenuSection menuSection3 = new MenuSection();
        menuSection3.setArena(arena2);

        final Collection<MenuItem> menuItems = new ArrayList<MenuItem>();
        MenuItem menuItem1 = new MenuItem("item1", menuSection1, null);
        menuItems.add(menuItem1);
        MenuItem menuItem2 = new MenuItem("item2", menuSection2, null);
        menuItems.add(menuItem2);
        MenuItem menuItem3 = new MenuItem("item3", menuSection2, null);
        menuItems.add(menuItem3);
        MenuItem menuItem4 = new MenuItem("item4", menuSection1, null);
        menuItems.add(menuItem4);
        MenuItem menuItem5 = new MenuItem("item5", menuSection3, null);
        menuItems.add(menuItem5);

        // check number of arenas - should be 2 as there are two unique arenas
        final List assignedArenas = MenuItemHelper.getAssignedArenas(menuItems);
        assertEquals(2, assignedArenas.size());

        // check order
        assertEquals(arena2, assignedArenas.get(0));
        assertEquals(arena1, assignedArenas.get(1));
    }

    MenuItemHelper menuItemHelper;
}
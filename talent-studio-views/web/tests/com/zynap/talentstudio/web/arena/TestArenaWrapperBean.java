package com.zynap.talentstudio.web.arena;

/**
 * User: amark
 * Date: 18-Nov-2005
 * Time: 13:26:13
 */

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.arenas.Arena;
import com.zynap.talentstudio.arenas.ArenaDisplayConfigItem;
import com.zynap.talentstudio.display.DisplayConfig;
import com.zynap.talentstudio.display.DisplayConfigItem;
import com.zynap.talentstudio.display.IDisplayConfigService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class TestArenaWrapperBean extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        displayConfigService = (IDisplayConfigService) getBean("displayConfigService");

        final List all = getViewDisplayConfigs();

        final Arena newArena = new Arena();
        newArena.setHideable(true);
        newArena.setLabel("label");
        newArena.setSessionTimeout(10);

        // add an ArenaDisplayConfigItem for each available DisplayConfig - save in array for checking later
        selectedDisplayConfigItems = new DisplayConfigItem[all.size()];
        for (int i = 0; i < all.size(); i++) {
            final DisplayConfig displayConfig = (DisplayConfig) all.get(i);
            final DisplayConfigItem displayConfigItem = (DisplayConfigItem) displayConfig.getDisplayConfigItems().get(0);
            selectedDisplayConfigItems[i] = displayConfigItem;
            newArena.addArenaDisplayConfigItem(new ArenaDisplayConfigItem(null, newArena, displayConfigItem));
        }

        arenaWrapperBean = new ArenaWrapperBean(newArena, all);
    }

    public void testGetArenaDisplayConfigItems() throws Exception {

        // check number of items
        final List items = arenaWrapperBean.getArenaDisplayConfigItems();
        assertFalse(items.isEmpty());
        assertEquals(getViewDisplayConfigs().size(), items.size());

        // check state of wrappers
        for (int i = 0; i < items.size(); i++) {
            ArenaDisplayConfigItemWrapper itemWrapper = (ArenaDisplayConfigItemWrapper) items.get(i);
            assertEquals(selectedDisplayConfigItems[i].getId(), itemWrapper.getSelectedItemId());
            assertEquals(selectedDisplayConfigItems[i].getDisplayConfig().getLabel(), itemWrapper.getLabel());
            assertEquals(selectedDisplayConfigItems[i].getDisplayConfig().getDisplayConfigItems(), itemWrapper.getDisplayConfigItems());
        }
    }

    private List getViewDisplayConfigs() throws TalentStudioException {
        return displayConfigService.findByType(DisplayConfig.VIEW_TYPE);
    }

    public void testGetModifiedArena() throws Exception {

        final List all = getViewDisplayConfigs();

        // set selected display config items to be the second in each collection
        final List ids = new ArrayList(all.size());
        for (int i = 0; i < all.size(); i++) {
            final DisplayConfig displayConfig = (DisplayConfig) all.get(i);
            final DisplayConfigItem displayConfigItem = (DisplayConfigItem) displayConfig.getDisplayConfigItems().get(1);
            ids.add(displayConfigItem.getId());
        }
        arenaWrapperBean.setArenaDisplayConfigItemIds((Long[]) ids.toArray(new Long[ids.size()]));

        // check that new display config items are present and the old ones are gone
        final Arena modifiedArena = arenaWrapperBean.getModifiedArena();
        final Collection newItems = modifiedArena.getArenaDisplayConfigItems();
        assertEquals(all.size(), newItems.size());
        for (Iterator iterator = newItems.iterator(); iterator.hasNext();) {
            ArenaDisplayConfigItem newItem = (ArenaDisplayConfigItem) iterator.next();
            assertTrue(ids.contains(newItem.getDisplayConfigItem().getId()));
        }
    }

    public void testGetArenaDisplayConfigItemIds() throws Exception {

        final Long[] arenaDisplayConfigItemIds = arenaWrapperBean.getArenaDisplayConfigItemIds();
        assertNotNull(arenaDisplayConfigItemIds);
        assertEquals(0, arenaDisplayConfigItemIds.length);
    }

    ArenaWrapperBean arenaWrapperBean;
    private IDisplayConfigService displayConfigService;
    private DisplayConfigItem[] selectedDisplayConfigItems;
}
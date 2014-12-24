/*
 * Copyright (c) 2005 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.arena;

import com.zynap.talentstudio.arenas.Arena;
import com.zynap.talentstudio.arenas.ArenaDisplayConfigItem;
import com.zynap.talentstudio.display.DisplayConfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public final class ArenaWrapperBean implements Serializable {

    public ArenaWrapperBean() {
    }

    public ArenaWrapperBean(Arena arena, List displayConfigs) {
        this.arena = arena;

        this.arenaDisplayConfigItems = new ArrayList();

        for (Iterator iterator = displayConfigs.iterator(); iterator.hasNext();) {
            DisplayConfig displayConfig = (DisplayConfig) iterator.next();

            ArenaDisplayConfigItemWrapper arenaDisplayConfigItemWrapper = new ArenaDisplayConfigItemWrapper(arena, displayConfig);
            arenaDisplayConfigItems.add(arenaDisplayConfigItemWrapper);
        }
    }

    public Arena getModifiedArena() {

        Collection modifiedItems = new LinkedHashSet();
        for (Iterator iterator = arenaDisplayConfigItems.iterator(); iterator.hasNext();) {
            ArenaDisplayConfigItemWrapper itemWrapper = (ArenaDisplayConfigItemWrapper) iterator.next();
            if (itemWrapper.getSelectedItemId() != null) {
                final ArenaDisplayConfigItem modifiedItem = itemWrapper.getModifiedItem();
                modifiedItems.add(modifiedItem);
            }
        }

        arena.assignNewArenaDisplayConfigItems(modifiedItems);

        return arena;
    }

    public String getId() {
        return arena.getArenaId();
    }

    public String getLabel() {
        return arena.getLabel();
    }

    public void setLabel(String label) {
        arena.setLabel(label);
    }

    public void setSessionTimeout(int sessionTimeout) {
        arena.setSessionTimeout(sessionTimeout);
    }

    public int getSessionTimeout() {
        return arena.getSessionTimeout();
    }

    public boolean isActive() {
        return arena.isActive();
    }

    public void setActive(boolean active) {
        arena.setActive(active);
    }

    public int getSortOrder() {
        return arena.getSortOrder();
    }

    public void setSortOrder(int value) {
        arena.setSortOrder(value);
    }

    public boolean isHideable() {
        return arena.isHideable();
    }

    public String getDescription() {
        return arena.getDescription();
    }

    public void setDescription(String desc) {
        arena.setDescription(desc);
    }

    public List getArenaDisplayConfigItems() {
        return arenaDisplayConfigItems;
    }

    public Long[] getArenaDisplayConfigItemIds() {
        return new Long[0];
    }

    public void setArenaDisplayConfigItemIds(Long[] arenaDisplayConfigItemIds) {

        resetArenaDisplayConfigItems();

        for (int i = 0; i < arenaDisplayConfigItemIds.length; i++) {
            final ArenaDisplayConfigItemWrapper arenaDisplayConfigItemWrapper = (ArenaDisplayConfigItemWrapper) arenaDisplayConfigItems.get(i);
            final Long arenaDisplayConfigItemId = arenaDisplayConfigItemIds[i];
            if (arenaDisplayConfigItemId != null) {
                arenaDisplayConfigItemWrapper.setSelectedItemId(arenaDisplayConfigItemId);
            }
        }
    }

    private void resetArenaDisplayConfigItems() {
        for (Iterator iterator = arenaDisplayConfigItems.iterator(); iterator.hasNext();) {
            ArenaDisplayConfigItemWrapper arenaDisplayConfigItemWrapper = (ArenaDisplayConfigItemWrapper) iterator.next();
            arenaDisplayConfigItemWrapper.clearDisplayConfigItem();
        }
    }

    private Arena arena;
    private List arenaDisplayConfigItems;
}

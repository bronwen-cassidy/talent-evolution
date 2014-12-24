/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.display;

import com.zynap.talentstudio.arenas.IArenaManager;
import com.zynap.talentstudio.arenas.Arena;
import com.zynap.talentstudio.arenas.ArenaDisplayConfigItem;
import com.zynap.talentstudio.display.DisplayConfigItem;
import com.zynap.talentstudio.display.DisplayConfig;

import java.io.Serializable;
import java.util.List;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Class or Interface description.
 *
 * @author taulant bajraktari
 * @version 0.1
 * @since 27-May-2009 10:15:31
 */
public class TabItemWrapper implements Serializable {

    public TabItemWrapper(Arena arena) {

        this.arena = arena;

    }


    public String getLabel() {
        return arena.getLabel();
    }


    public String getArenaId() {
        return arena.getArenaId();
    }


    public boolean isHomeArena() {
        return getArenaId().equals(IArenaManager.MYZYNAP_MODULE);
    }

    public Collection<DisplayConfig> getDisplayConfigs() {
        return displayConfigs;
    }

    public void setDisplayConfig(List displayConfigs) {
        this.displayConfigs = displayConfigs;
    }


    public void setSelectTabIds(Long[] selectTabIds) {
        this.selectTabIds = selectTabIds;
    }

    public Long[] getSelectTabIds() {
        return selectTabIds;
    }

    public Arena getArena() {
        return arena;
    }

    public Collection<ArenaDisplayConfigItem> getConfigItems() {
        return arena.getArenaDisplayConfigItems();
    }


    private List<DisplayConfig> displayConfigs;
    private Arena arena;
    private Long[] selectTabIds = new Long[]{new Long(0), new Long(0), new Long(0)};
}

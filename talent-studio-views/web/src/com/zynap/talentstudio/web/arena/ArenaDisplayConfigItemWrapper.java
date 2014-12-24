package com.zynap.talentstudio.web.arena;

import com.zynap.talentstudio.arenas.Arena;
import com.zynap.talentstudio.arenas.ArenaDisplayConfigItem;
import com.zynap.talentstudio.display.DisplayConfig;
import com.zynap.talentstudio.display.DisplayConfigItem;

import java.io.Serializable;
import java.util.List;

/**
 * User: amark
 * Date: 18-Nov-2005
 * Time: 11:31:26
 */
public class ArenaDisplayConfigItemWrapper implements Serializable {

    private DisplayConfig displayConfig;

    private ArenaDisplayConfigItem arenaDisplayConfigItem;

    public ArenaDisplayConfigItemWrapper(Arena arena, DisplayConfig displayConfig) {
        this.displayConfig = displayConfig;

        // create a new item if none found
        ArenaDisplayConfigItem item = arena.getArenaDisplayConfigItem(displayConfig.getId());
        if (item == null) {
            item = new ArenaDisplayConfigItem(null, arena, new DisplayConfigItem());
        }

        this.arenaDisplayConfigItem = item;
    }

    public String getLabel() {
        return displayConfig.getLabel();
    }

    public List getDisplayConfigItems() {
        return displayConfig.getDisplayConfigItems();
    }

    public Long getSelectedItemId() {
        final DisplayConfigItem displayConfigItem = arenaDisplayConfigItem.getDisplayConfigItem();
        return displayConfigItem != null ? displayConfigItem.getId() : null;
    }

    void clearDisplayConfigItem() {
        arenaDisplayConfigItem.setDisplayConfigItem(null);
    }

    void setSelectedItemId(Long itemId) {
        final DisplayConfigItem displayConfigItem = displayConfig.getDisplayConfigItem(itemId);
        if (displayConfigItem != null) {
            arenaDisplayConfigItem.setDisplayConfigItem(displayConfigItem);
        }
    }

    ArenaDisplayConfigItem getModifiedItem() {
        return arenaDisplayConfigItem;
    }
}

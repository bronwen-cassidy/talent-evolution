package com.zynap.talentstudio.arenas;

import com.zynap.talentstudio.display.DisplayConfigItem;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;


/**
 * @author amark
 */
public class ArenaDisplayConfigItem implements Serializable {


    /**
     * default constructor.
     */
    public ArenaDisplayConfigItem() {
    }

    /**
     * full constructor.
     */
    public ArenaDisplayConfigItem(Long id, Arena arena, DisplayConfigItem displayConfigItem) {
        this.id = id;
        this.arena = arena;
        this.displayConfigItem = displayConfigItem;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Arena getArena() {
        return this.arena;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public DisplayConfigItem getDisplayConfigItem() {
        return this.displayConfigItem;
    }

    public void setDisplayConfigItem(DisplayConfigItem displayConfigItem) {
        this.displayConfigItem = displayConfigItem;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .toString();
    }

    public boolean equals(Object object) {
        if (object.getClass() != this.getClass())
            return false;
        else {
            ArenaDisplayConfigItem other = (ArenaDisplayConfigItem) object;
            if (this.getId() == null || other.getId() == null) return (this == object);
            return (this.getId().longValue() == other.getId().longValue());
        }
    }

    public int hashCode() {
        return id == null ? super.hashCode() : id.hashCode();
    }

    /**
     * identifier field.
     */
    private Long id;


    /**
     * persistent field.
     */
    private Arena arena;

    /**
     * persistent field.
     */
    private DisplayConfigItem displayConfigItem;

}
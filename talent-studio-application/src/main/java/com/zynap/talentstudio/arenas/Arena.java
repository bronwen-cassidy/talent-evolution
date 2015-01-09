/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.arenas;

import com.zynap.domain.IDomainObject;
import com.zynap.talentstudio.display.DisplayConfigItem;

import org.apache.commons.lang.builder.ToStringBuilder;

import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class Arena implements IDomainObject, Comparable<Arena> {

    public Arena() {
    }

    public Arena(String arenaId, String label) {
        this.arenaId = arenaId;
        this.label = label;
    }

    public Arena(String arenaId, String label, String url, String description, int sessionTimeout, int sortOrder, boolean active, boolean hideable) {
        this.arenaId = arenaId;
        this.label = label;
        this.url = url;
        this.description = description;
        this.sessionTimeout = sessionTimeout;
        this.sortOrder = sortOrder;
        this.active = active;
        this.hideable = hideable;
    }

    public Long getId() {
        return new Long(arenaId.hashCode());
    }

    public String getArenaId() {
        return arenaId;
    }

    public void setArenaId(String arenaId) {
        this.arenaId = arenaId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isHideable() {
        return hideable;
    }

    public void setHideable(boolean hideable) {
        this.hideable = hideable;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Collection<MenuSection> getMenuSections() {
        return menuSections;
    }

    public void setMenuSections(Collection<MenuSection> menuSections) {
        this.menuSections = menuSections;
    }

    public int compareTo(Arena o) {
        return new Integer(this.sortOrder).compareTo((new Integer(o.sortOrder)));
    }

    /**
     * Find ArenaDisplayConfigItem for DisplayConfig (NOT DisplayConfigItem!).
     * @param displayConfigId
     * @return ArenaDisplayConfigItem or null
     */
    public ArenaDisplayConfigItem getArenaDisplayConfigItem(Long displayConfigId) {

        for (Iterator iterator = arenaDisplayConfigItems.iterator(); iterator.hasNext();) {
            final ArenaDisplayConfigItem item = (ArenaDisplayConfigItem) iterator.next();
            final DisplayConfigItem displayConfigItem = item.getDisplayConfigItem();
            if (displayConfigItem != null && displayConfigItem.getDisplayConfig().getId().equals(displayConfigId)) {
                return item;
            }
        }

        return null;
    }

    public void assignNewArenaDisplayConfigItems(Collection<ArenaDisplayConfigItem> newItems) {
        arenaDisplayConfigItems.clear();
        if (newItems !=null) {
            for (Iterator iterator = newItems.iterator(); iterator.hasNext();) {
                ArenaDisplayConfigItem newItem = (ArenaDisplayConfigItem) iterator.next();
                addArenaDisplayConfigItem(newItem);
            }
        }
    }

    public void addArenaDisplayConfigItem(ArenaDisplayConfigItem item) {
        item.setArena(this);
        arenaDisplayConfigItems.add(item);
    }

    public Collection<ArenaDisplayConfigItem> getArenaDisplayConfigItems() {
        return arenaDisplayConfigItems;
    }

    public void setArenaDisplayConfigItems(Collection<ArenaDisplayConfigItem> arenaDisplayConfigItems) {
        this.arenaDisplayConfigItems = arenaDisplayConfigItems;
    }

    public Arena shallowCopy() {
        Arena arena = new Arena(arenaId, label, url, description, sessionTimeout, sortOrder, active, hideable);
        arena.setMenuSections(new LinkedHashSet<MenuSection>());
        return arena;
    }

    public void addMenuSection(MenuSection menuSection) {
        menuSection.setArena(this);
        getMenuSections().add(menuSection);
    }

    public void addMenuItem(MenuItem menuItem) {
        MenuSection menuSection = menuItem.getMenuSection();

        final Collection<MenuSection> temp = getMenuSections();
        if (temp.contains(menuSection)) {
            for (Iterator iterator = temp.iterator(); iterator.hasNext();) {
                MenuSection section = (MenuSection) iterator.next();
                if (section.equals(menuSection)) {
                    section.addMenuItem(menuItem);
                }
            }
        } else {
            menuSection = menuSection.shallowCopy();
            menuSection.addMenuItem(menuItem);
            menuSection.setArena(this);
            temp.add(menuSection);
        }
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getArenaId())
                .append("label", getLabel())
                .append("url", getUrl())
                .append("description", getDescription())
                .append("active", isActive())
                .append("hideable", isHideable())
                .append("sortOrder", getSortOrder())
                .append("sessionTimeout", getSessionTimeout())
                .toString();
    }

    public boolean equals(Object object) {
        if (object.getClass() != this.getClass())
            return false;
        else {
            Arena otherArena = (Arena) object;
            if (this.getId() == null || otherArena.getId() == null) return (this == object);
            return (this.getId().longValue() == otherArena.getId().longValue());
        }
    }

    public int hashCode() {
        return arenaId == null ? super.hashCode() : arenaId.hashCode();
    }

    public String getBaseURL() {

        String baseURL = this.url;

        if (StringUtils.hasText(baseURL)) {
            int i = baseURL.lastIndexOf("/");
            if (i > 0 && ++i < baseURL.length()) {
                baseURL = baseURL.substring(0, i);
            }
        }

        return baseURL;
    }

    /**
     * Force a lazy initialisation of lazy collections for this class.
     * Currently not implemented
     */
    public void initLazy() {}

    private String arenaId;
    private String label;
    private String url;
    private String description;
    private int sessionTimeout;
    private int sortOrder;
    private boolean active = true;
    private boolean hideable = false;

    private Collection<MenuSection> menuSections = new LinkedHashSet<MenuSection>();
    private Collection<ArenaDisplayConfigItem> arenaDisplayConfigItems = new LinkedHashSet<ArenaDisplayConfigItem>();
}

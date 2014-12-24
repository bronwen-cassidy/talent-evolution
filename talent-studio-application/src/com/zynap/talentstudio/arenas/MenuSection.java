package com.zynap.talentstudio.arenas;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: aandersson
 * Date: 12-Feb-2004
 * Time: 15:08:00
 * To change this template use Options | File Templates.
 */
public class MenuSection implements Serializable, Comparable<MenuSection> {

    public MenuSection() {
    }

    public MenuSection(MenuSectionPK sectionPK, String label) {
        primaryKey = sectionPK;
        this.label = label;
    }

    public MenuSection(MenuSectionPK primaryKey, String label, int sortOrder) {
        this(primaryKey, label);
        this.sortOrder = sortOrder;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Collection<MenuItem> getMenuItems() {
        return menuItems;
    }

    public Collection<MenuItem> getSortedMenuItems() {
        List<MenuItem> sortedItems = new ArrayList<MenuItem>(menuItems);
        Collections.sort(sortedItems);
        return sortedItems;
    }

    public void setMenuItems(Collection<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public MenuSectionPK getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(MenuSectionPK primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Arena getArena() {
        return arena;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public int compareTo(MenuSection other) {
        return new Integer(this.sortOrder).compareTo(new Integer(other.sortOrder));
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("primaryKey", getPrimaryKey())
                .append("sectionLabel", getLabel())
                .append("sortOrder", getSortOrder())
                .toString();
    }

    public boolean equals(Object other) {
        if ((this == other)) return true;
        if (!(other instanceof MenuSection)) return false;
        MenuSection castOther = (MenuSection) other;
        return new EqualsBuilder()
                .append(this.getPrimaryKey(), castOther.getPrimaryKey())
                .append(this.getLabel(), castOther.getLabel())
                .append(this.getSortOrder(), castOther.getSortOrder())
                .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
                .append(getPrimaryKey())
                .append(getLabel())
                .append(getSortOrder())
                .toHashCode();
    }

    public MenuSection shallowCopy() {
        MenuSection menuSection = new MenuSection(primaryKey, label, sortOrder);
        menuSection.setUrl(this.url);
        menuSection.setMenuItems(new LinkedHashSet<MenuItem>());
        return menuSection;
    }

    public void addMenuItem(MenuItem menuItem) {
        menuItem.setMenuSection(this);
        menuItems.add(menuItem);
    }

    public boolean isOrgUnitHierarchy() {
        return getId().endsWith(ORG_UNIT_SUFFIX);
    }

    /**
     * is this a home page report section.
     * <br/> Return true if arena is home arena
     * and section id matches any of IArenaManager.PERFORMANCE_MANAGEMENT_MODULE, IArenaManager.SUCCESSION_MODULE or IArenaManager.TALENT_IDENTIFICATION_MODULE.
     * @return true or false
     */
    public boolean isHomeArenaReportMenuSection() {
        final String id = getId();

        return arena != null
                && IArenaManager.MYZYNAP_MODULE.equals(arena.getArenaId())
                && (IArenaManager.PERFORMANCE_MANAGEMENT_MODULE.equals(id) || IArenaManager.SUCCESSION_MODULE.equals(id) || IArenaManager.TALENT_IDENTIFICATION_MODULE.equals(id));
    }

    public String getId() {
        return getPrimaryKey().getId();
    }

    private MenuSectionPK primaryKey;
    private int sortOrder;
    private Collection<MenuItem> menuItems;
    private Arena arena;
    private String label;
    private String url;

    private static final String ORG_UNIT_SUFFIX = "_ORG_UNIT_TREE_";
}

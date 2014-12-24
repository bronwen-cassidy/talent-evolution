package com.zynap.talentstudio.preferences;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.arenas.MenuItem;
import com.zynap.talentstudio.preferences.domain.DomainObjectPreferenceCollection;
import com.zynap.talentstudio.common.groups.Group;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.HashSet;


/**
 * @author Hibernate CodeGenerator
 */
public class Preference implements Serializable {

    /**
     * full constructor
     */
    public Preference(Long id, String hashcode, String viewName, String preferenceType, DomainObjectPreferenceCollection preferenceValues, Set<MenuItem> menuItems) {
        this.id = id;
        this.hashcode = hashcode;
        this.viewName = viewName;
        this.type = preferenceType;
        this.preferenceCollection = preferenceValues;
        this.menuItems = menuItems;
    }

    /**
     * default constructor
     */
    public Preference() {
    }

    /**
     * convenience constructor
     */
    public Preference(String viewName, String preferenceType, DomainObjectPreferenceCollection preferenceValues, Long userId) {
        this(null, Integer.toString(preferenceValues.hashCode()), viewName, preferenceType, preferenceValues, new LinkedHashSet<MenuItem>());
        this.userId = userId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHashcode() {
        return this.hashcode;
    }

    public void setHashcode(String hashcode) {
        this.hashcode = hashcode;
    }

    public String getViewName() {
        return this.viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DomainObjectPreferenceCollection getPreferenceCollection() {
        return this.preferenceCollection;
    }

    public void setPreferenceCollection(DomainObjectPreferenceCollection preferenceCollection) {
        this.preferenceCollection = preferenceCollection;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<MenuItem> getMenuItems() {
        return this.menuItems;
    }

    public void setMenuItems(Set<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    /**
     * Clear old menu items and add new ones.
     * @param newMenuItems
     */
    public void assignNewMenuItems(Set<MenuItem> newMenuItems) {
        this.menuItems.clear();

        for (Iterator iterator = newMenuItems.iterator(); iterator.hasNext();) {
            MenuItem menuItem = (MenuItem) iterator.next();
            addMenuItem(menuItem);
        }
    }

    public void addMenuItem(MenuItem menuItem) {
        menuItem.setPreference(this);
        menuItems.add(menuItem);
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public boolean containsGroup(Group userGroup) {
        return groups != null && groups.contains(userGroup);
    }

    public boolean isHasGroups() {
        return (groups != null && !groups.isEmpty());
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("hashcode", getHashcode())
                .append("viewName", getViewName())
                .append("type", getType())
                .append("preferenceCollection", getPreferenceCollection())
                .toString();
    }

    public boolean equals(Object command) {
        if (this == command) return true;
        if (!(command instanceof Preference)) return false;

        final Preference preference = (Preference) command;
        if (id != null ? !id.equals(preference.id) : preference.id != null) return false;

        return true;
    }

    public int hashCode() {
        return (id != null ? id.hashCode() : super.hashCode());
    }

    public void assignNewGroups(Set<Group> newGroups) {
        getGroups().clear();
        setGroups(newGroups);
    }

    /**
     * identifier field
     */
    private Long id;

    /**
     * persistent field
     */
    private String hashcode;

    /**
     * persistent field
     */
    private String viewName;

    /**
     * nullable persistent field
     */
    private String description;

    /**
     * nullable persistent field
     */
    private String type;

    /**
     * persistent field
     */
    private DomainObjectPreferenceCollection preferenceCollection;

    private Long userId;

    /**
     * persistent field
     */
    private Set<MenuItem> menuItems = new LinkedHashSet<MenuItem>();

    private boolean secure;
    private Set<Group> groups = new HashSet<Group>();
}

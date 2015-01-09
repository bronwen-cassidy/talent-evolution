package com.zynap.talentstudio.security.roles;

import com.zynap.domain.ZynapDomainObject;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


/**
 * @author Hibernate CodeGenerator
 */
public abstract class Role extends ZynapDomainObject implements Serializable {

    /**
     * Basic constructor.
     */
    public Role() {
    }


    /**
     * full constructor.
     */
    public Role(Long id, String type, String label, Boolean isPositionDefault, Boolean isOunitDefault, String description, boolean isActive, boolean isSystem, Boolean isSubDefault, Set permits) {
        this.id = id;
        this.type = type;
        this.positionDefault = isPositionDefault;
        this.oUnitDefault = isOunitDefault;
        this.description = description;
        this.active = isActive;
        this.system = isSystem;
        this.label = label;
        this.subDefault = isSubDefault;
        this.permits = permits;
    }

    /**
     * minimal constructor.
     */
    public Role(Long id, String type, String label, String description, boolean isActive, boolean isSystem, Set permits) {
        this(id, type, label, Boolean.FALSE, Boolean.FALSE, description, isActive, isSystem, Boolean.FALSE, permits);
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsPositionDefault() {
        return this.positionDefault;
    }

    public void setIsPositionDefault(Boolean isPositionDefault) {
        this.positionDefault = isPositionDefault;
    }

    public Boolean getIsOunitDefault() {
        return this.oUnitDefault;
    }

    public void setIsOunitDefault(Boolean isOunitDefault) {
        this.oUnitDefault = isOunitDefault;
    }

    public boolean getIsSystem() {
        return this.system;
    }

    public void setIsSystem(boolean isSystem) {
        this.system = isSystem;
    }

    public Boolean getIsSubDefault() {
        return this.subDefault;
    }

    public void setIsSubDefault(Boolean isSubDefault) {
        this.subDefault = isSubDefault;
    }

    public Set getPermits() {
        if (permits == null) permits = new HashSet();
        return this.permits;
    }

    public void setPermits(Set permits) {
        this.permits = permits;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoleType() {
        return type;
    }

    public void setRoleType(String roleType) {
        type = roleType.toUpperCase();
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public void setSystem(boolean isSystem) {
        system = isSystem;
    }

    public boolean isSystem() {
        return system;
    }

    /**
     * Check if this is a ResourceRole.
     *
     * @return true if this is a resource role
     */
    public boolean isResourceRole() {
        return isResourceRole(getRoleType());
    }

    /**
     * Clear permits.
     */
    public void clearPermits() {
        if (permits != null) {
            permits.clear();
        }
    }

    public boolean isAccessRole() {
        return ACCESS_TYPE.equalsIgnoreCase(type);
    }

    /**
     * Check if the type indicates a resource role.
     *
     * @param roleType The role type
     * @return true if this is a resource role
     */
    public static boolean isResourceRole(String roleType) {
        return RESOURCE_TYPE.equalsIgnoreCase(roleType);
    }

    public String getArenaId() {
        return arenaId;
    }

    public void setArenaId(String arenaId) {
        this.arenaId = arenaId;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("label", getLabel())
                .append("type", getType())
                .append("description", getDescription())
                .append("active", isActive())
                .append("system", isSystem())
                .append("isPositionDefault", getIsPositionDefault())
                .append("isOunitDefault", getIsOunitDefault())
                .append("isSubDefault", getIsSubDefault())
                .toString();
    }

    public boolean isArenaRole() {
        return arenaRole;
    }

    public void setArenaRole(boolean arenaRole) {
        this.arenaRole = arenaRole;
    }/*
     * Returns true if this is the home role
     */

    public boolean isHomeRole() {
        return HOME_ROLE_ID.equals(id);
    }

    /**
     * Checks to see if this a role that only the administrator(superuser) can assign to anyone
     *
     * @return true
     */
    public boolean isAdminRole() {
        return adminRole;
    }

    public void setAdminRole(boolean adminRole) {
        this.adminRole = adminRole;
    }

    /**
     * The administration role is a very special role it is the only role with global access, you cannot remove
     * this role from seeing all views.
     *
     * @return true if this is the particular role, there is only 1
     */
    public boolean isAdministrationRole() {
        return ADMIN_ROLE_ID.equals(id);
    }

    public static final String RESOURCE_TYPE = "RESOURCE";
    public static final String ACCESS_TYPE = "ACCESS";
    public static final Long HOME_ROLE_ID = new Long(8);
    public static final Long ADMIN_ROLE_ID = new Long(1);

    /**
     * Non-persistent field used by web-tier to indicate if a role is assigned or not.
     */
    private boolean assigned;

    /* admin roles can only be assigned by a person who has the administration(superuser) role */
    private boolean adminRole;

    /* field that lets us know when a role label needs to be changed when the arena label is changed (it is directly linked to the arena) */
    private boolean arenaRole;

    /**
     * Persistent field.
     */
    private boolean system;

    /**
     * Persistent field.
     */
    private String type;

    private String arenaId;

    /**
     * Nullable persistent field.
     */
    private Boolean positionDefault;

    /**
     * Nullable persistent field.
     */
    private Boolean oUnitDefault;

    /**
     * Persistent field.
     */
    private String description;

    /**
     * Nullable persistent field.
     */
    private Boolean subDefault;

    /**
     * Persistent field.
     */
    private Set permits;

}

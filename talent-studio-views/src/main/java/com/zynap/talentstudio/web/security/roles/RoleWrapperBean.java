package com.zynap.talentstudio.web.security.roles;

import com.zynap.talentstudio.security.permits.IPermit;
import com.zynap.talentstudio.security.permits.Permit;
import com.zynap.talentstudio.security.roles.Role;

import java.util.*;
import java.io.Serializable;


/**
 * User: amark
 * Date: 20-Jan-2005
 * Time: 17:20:21
 */
public class RoleWrapperBean implements Serializable {

    private Role role;

    private List allPermits;

    private Collection selectedPermitIds = new ArrayList();

    private String roleType;

    public RoleWrapperBean(Role role, List allPermits) {
        this.role = role;
        this.allPermits = allPermits;
        if (role != null) {
            Set permits = role.getPermits();
            if (permits != null) {
                initSelectedPermitIds(permits);
            }
        }
    }

    private void initSelectedPermitIds(Set permits) {
        for (Iterator iterator = permits.iterator(); iterator.hasNext();) {
            Permit permit = (Permit) iterator.next();
            selectedPermitIds.add(permit.getId().toString());
        }
    }

    public boolean isResourceRole() {
        return role.isResourceRole();
    }

    public boolean isActive() {
        return role.isActive();
    }

    public void setActive(boolean active) {
        role.setActive(active);
    }

    public Long getId() {
        return role.getId();
    }

    public void setId(Long id) {
        role.setId(id);
    }

    public String getType() {
        return roleType;
    }

    public void setType(String type) {
        this.roleType = type;
    }

    public Boolean getIsPositionDefault() {
        return role.getIsPositionDefault();
    }

    public void setIsPositionDefault(Boolean isPositionDefault) {
        role.setIsPositionDefault(isPositionDefault);
    }

    public Boolean getIsOunitDefault() {
        return role.getIsOunitDefault();
    }

    public void setIsOunitDefault(Boolean isOunitDefault) {
        role.setIsOunitDefault(isOunitDefault);
    }

    public boolean getIsSystem() {
        return role.getIsSystem();
    }

    public String getLabel() {
        return role.getLabel();
    }

    public void setLabel(String label) {
        role.setLabel(label);
    }

    public Boolean getIsSubDefault() {
        return role.getIsSubDefault();
    }

    public void setIsSubDefault(Boolean isSubDefault) {
        role.setIsSubDefault(isSubDefault);
    }

    public String getDescription() {
        return role.getDescription();
    }

    public void setDescription(String description) {
        role.setDescription(description);
    }

    public String getRoleType() {
        return role.getRoleType();
    }

    public void setRoleType(String roleType) {
        role.setRoleType(roleType);
    }

    public boolean isAssigned() {
        return role.isAssigned();
    }

    public void setAssigned(boolean assigned) {
        role.setAssigned(assigned);
    }

    public void setSystem(boolean isSystem) {
        role.setSystem(isSystem);
    }

    public boolean isSystem() {
        return role.isSystem();
    }

    public Collection getCurrentPermits() {
        return role.getPermits();
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setAllPermits(List allPermits) {
        this.allPermits = allPermits;
    }

    public void initSelectedPermitIds() {
        if (role.isResourceRole()) {
            // initSelectedPermitIds
            initSelectedPermitIds(new HashSet(allPermits));
        }
    }

    /**
     * Get permits.
     * <br> Returns the "master list" of permits - if this bean is being used to edit
     * the underlying Role, then the permits currently assigned to the role are marked as selected.
     * <br> If the bean is in add mode and the Role is a ResourceRole then all the permits are selected by default.
     * Otherwise none of them are.
     *
     * @return allPermits
     */
    public List getAllPermits() {

        for (int i = 0; i < allPermits.size(); i++) {
            IPermit permit = (IPermit) allPermits.get(i);
            if (selectedPermitIds.contains(permit.getId().toString())) {
                permit.setSelected(true);
            } else {
                permit.setSelected(false);
            }
        }
        return allPermits;
    }

    public Role getOriginalRole() {
        return role;
    }

    public Role getModifiedRole() {

        // use selectedPermitIds to get the list of permits selected for the role
        Set selectedPermits = new HashSet();
        for (Iterator iterator = allPermits.iterator(); iterator.hasNext();) {
            IPermit permit = (IPermit) iterator.next();
            if (selectedPermitIds.contains(permit.getId().toString())) {
                selectedPermits.add(permit);
            }
        }

        role.setPermits(selectedPermits);
        return role;
    }

    public String[] getSelectedPermitIds() {
        return new String[0];
    }

    public void setSelectedPermitIds(String[] selectedPermitIds) {
        if (selectedPermitIds != null) {
            this.selectedPermitIds = Arrays.asList(selectedPermitIds);
        }
    }
}
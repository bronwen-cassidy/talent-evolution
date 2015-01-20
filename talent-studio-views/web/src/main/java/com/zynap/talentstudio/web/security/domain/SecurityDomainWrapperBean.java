package com.zynap.talentstudio.web.security.domain;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.security.SecurityDomain;
import com.zynap.talentstudio.security.areas.Area;
import com.zynap.talentstudio.security.roles.Role;
import com.zynap.talentstudio.web.security.UserHelper;
import com.zynap.talentstudio.web.utils.SelectionNodeHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * User: amark
 * Date: 16-Mar-2005
 * Time: 16:09:27
 */
public class SecurityDomainWrapperBean implements Serializable {

    public SecurityDomainWrapperBean(SecurityDomain securityDomain) {
        this.securityDomain = securityDomain;
        this.areaId = securityDomain.getNode() != null ? securityDomain.getNode().getId() : null;
    }

    public SecurityDomain getModifiedSecurityDomain() {

        securityDomain.setNode(getAssignedArea());
        securityDomain.setRoles(getAssignedRoles());
        securityDomain.setUsers(UserHelper.retainRootUsers(securityDomain.getUsers(), getAssignedUsers()));
        return securityDomain;
    }


    private Node getAssignedArea() {
        Area assignedArea = null;

        if (areaId != null) {
            for (Iterator iterator = areas.iterator(); iterator.hasNext();) {
                Area area = (Area) iterator.next();
                if (areaId.equals(area.getId())) {
                    assignedArea = area;
                    break;
                }
            }
        }

        return assignedArea;
    }

    private Set<User> getAssignedUsers() {
        return selectedUsers != null ? selectedUsers : new HashSet<User>();
    }

    private Set<Role> getAssignedRoles() {
        return this.selectedRoles != null ? selectedRoles : new HashSet<Role>();
    }

    /**
     * Resets security domain id to null.
     * <br> Required if there is a problem adding a security domain to ensure that hibernate
     * realises that the domain and the node are both new objects to be saved.
     */
    public void resetIds() {
        securityDomain.setId(null);
    }

    public Long getId() {
        return securityDomain.getId();
    }

    public String getComments() {
        return securityDomain.getComments();
    }

    public void setComments(String comments) {
        securityDomain.setComments(comments);
    }

    public Node getNode() {
        return securityDomain.getNode();
    }

    public void setNode(Node newNode) {
        securityDomain.setNode(newNode);
    }

    public String getLabel() {
        return securityDomain.getLabel();
    }

    public void setLabel(String label) {
        securityDomain.setLabel(label);
    }

    public void setActive(boolean active) {
        securityDomain.setActive(active);
    }

    public boolean isActive() {
        return securityDomain.isActive();
    }

    public void setExclusive(boolean exclusive) {
        securityDomain.setExclusive(exclusive);
    }

    public boolean isExclusive() {
        return securityDomain.isExclusive();
    }

    /**
     * Get roles.
     * <br> Before returning checks which of the roles are assigned to the SecurityDomain
     * and sets the assigned flag on the front-end.
     *
     * @return Collection of Role objects
     */
    public Collection getRoles() {
        return roles;
    }

    public void setRoles(Collection roles) {
        this.roles = SelectionNodeHelper.createDomainObjectSelections(roles, securityDomain.getRoles());
    }

    public void setRoleIds(Long[] roleIds) {
        selectedRoles = new HashSet<Role>();
        SelectionNodeHelper.enableDomainObjectSelections(roles, roleIds, selectedRoles);
    }

    public Long[] getRoleIds() {
        return new Long[0];
    }

    public boolean isAssigned(Role role) {
        return securityDomain.getRoles().contains(role);
    }

    /**
     * Returns list of app users only - removes root users from list for display purposes.
     *
     * @return Collection of User objects
     */
    public Collection getUsers() {
        return users;
    }

    public void setUsers(Collection users) {
        this.users = SelectionNodeHelper.createDomainObjectSelections(users, UserHelper.removeRootUsers(securityDomain.getUsers()));
    }

    public void setUserIds(Long[] userIds) {
        selectedUsers = new HashSet<User>();
        SelectionNodeHelper.enableDomainObjectSelections(users, userIds, selectedUsers);
    }

    public Long[] getUserIds() {
        return new Long[0];
    }

    public boolean isAssigned(User user) {
        return securityDomain.getUsers().contains(user);
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Collection getAreas() {
        return areas;
    }

    public void setAreas(Collection areas) {
        this.areas = areas;
    }

    private SecurityDomain securityDomain;

    private Collection roles;

    private HashSet<Role> selectedRoles;

    private Collection users;

    private HashSet<User> selectedUsers;

    private Collection areas = new ArrayList();

    private Long areaId;
}
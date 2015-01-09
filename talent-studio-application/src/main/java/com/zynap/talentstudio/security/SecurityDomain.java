/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.security;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.security.roles.Role;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class SecurityDomain extends ZynapDomainObject implements Serializable {

    /**
     * full constructor.
     */
    public SecurityDomain(Long id, boolean isExclusive, String label, String comments, boolean isActive, Node node, Set<User> users, Set<Role> roles) {
        setId(id);
        this.exclusive = isExclusive;
        this.label = label;
        this.comments = comments;
        setActive(isActive);
        this.node = node;
        this.users = users;
        this.roles = roles;
    }

    /**
     * default constructor.
     */
    public SecurityDomain() {
    }

    /**
     * minimal constructor.
     */
    public SecurityDomain(Long id, boolean isExclusive, String label, boolean isActive, Node node, Set<User> securityDomainsUsers, Set<Role> rolesSecurityDomains) {
        this.id = id;
        this.exclusive = isExclusive;
        this.label = label;
        setActive(isActive);
        this.node = node;
        this.users = securityDomainsUsers;
        this.roles = rolesSecurityDomains;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Node getNode() {
        return this.node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Set<User> getUsers() {
        if (users == null) users = new HashSet<User>();
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        getUsers().add(user);
    }

    public Set<Role> getRoles() {
        if (roles == null) roles = new HashSet<Role>();
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }


    /**
     * Add a role.
     * <br> Only accepts {@link com.zynap.talentstudio.security.roles.ResourceRole} roles.
     *
     * @param role The Role
     * @throws IllegalArgumentException If the role is not a resource role.
     */
    public void addRole(Role role) {

        if (!role.isResourceRole()) throw new IllegalArgumentException();
        getRoles().add(role);
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("exclusive", isExclusive())
                .append("label", getLabel())
                .append("comments", getComments())
                .append("isActive", isActive())
                .toString();
    }

    /**
     * persistent field.
     */
    private boolean exclusive;

    /**
     * nullable persistent field.
     */
    private String comments;

    /**
     * persistent field.
     */
    private Node node;

    /**
     * persistent field.
     */
    private Set<User> users;

    /**
     * persistent field.
     */
    private Set<Role> roles;

}

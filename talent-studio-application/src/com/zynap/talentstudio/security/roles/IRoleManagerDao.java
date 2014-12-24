/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.security.roles;

import com.zynap.exception.TalentStudioException;

import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface IRoleManagerDao {

    /**
     * Add a new Role.
     *
     * @param role
     * @param principalId The id of the user adding this role
     * @throws com.zynap.exception.TalentStudioException
     *
     */
    void addRole(Role role, int principalId) throws TalentStudioException;

    /**
     * Disable the role.
     *
     * @param id          The unique id of the role
     * @param principalId The id of the user disabling this role
     * @throws com.zynap.exception.TalentStudioException
     *
     */
    void disableRole(Long id, int principalId) throws TalentStudioException;

    /**
     * Update a role.
     *
     * @param role
     * @param principalId The id of the user updating this role
     * @throws com.zynap.exception.TalentStudioException
     *
     */
    void updateRole(Role role, int principalId) throws TalentStudioException;

    /**
     * Get the AccessRole associated with the label.
     *
     * @param label The role label
     * @return AccessRole The role if found; otherwise a <code>RoleNotFoundException</code> will be thrown
     * @throws com.zynap.exception.TalentStudioException
     *
     */
    Role findAccessRole(String label) throws TalentStudioException;

    /**
     * Get the ResourceRole associated with the label.
     *
     * @param label The role label
     * @return ResourceRole The role if found; otherwise a <code>RoleNotFoundException</code> will be thrown
     * @throws com.zynap.exception.TalentStudioException
     *
     */
    Role findResourceRole(String label) throws TalentStudioException;

    /**
     * Get all the active roles (includes both ResourceRoles and AccessRoles.)
     *
     * @return List consisting of {@link com.zynap.talentstudio.security.roles.Role} objects
     */
    List getAllActiveRoles();

    /**
     * Get all the roles (active and inactive, includes both ResourceRoles and AccessRoles)
     *
     * @return List consisting of {@link com.zynap.talentstudio.security.roles.Role} objects
     */
    List getAllRoles();

    /**
     * Get the active access roles.
     *
     * @return List consisting of {@link com.zynap.talentstudio.security.roles.AccessRole} objects
     */
    List<Role> getActiveAccessRoles();

    List<Role> getAllAccessRoles();

    /**
     * Get the active resource roles.
     *
     * @return List consisting of {@link com.zynap.talentstudio.security.roles.ResourceRole } objects
     */
    List getActiveResourceRoles();

    /**
     * Find Role by primary key.
     *
     * @param id The id
     * @return Role
     * @throws com.zynap.exception.TalentStudioException
     *
     */
    Role findRole(Long id) throws TalentStudioException;

    Role findRoleByArena(String arenaId) throws TalentStudioException;

    void update(Role role) throws TalentStudioException;
}

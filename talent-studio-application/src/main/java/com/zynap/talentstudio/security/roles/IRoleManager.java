/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.security.roles;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.security.permits.IPermit;

import java.util.List;

/**
 * Class or Interface description.
 * 
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface IRoleManager {

    /**
     * Get all the active roles (includes both ResourceRoles and AccessRoles.)
     *
     * @return List consisting of {@link com.zynap.talentstudio.security.roles.Role} objects
     */
    List getAllActiveRoles();

    /**
     * Saves a role with its permissions.
     * <p/>
     * <p>invariants:
     * <ol>
     * <li>role must be added before the permissions</li>
     * <li>if the addition of the role fails the permissions will not be inserted</li>
     * <li>if the addition of the permissions fails the role addition will be rolled back</li>
     * </ol>
     * </p>
     *
     * @param role The new Role
     * @param principalId The id of the user adding the role
     */
    void create(Role role, int principalId) throws TalentStudioException;

    /**
     * Get all the roles (active and inactive, includes both ResourceRoles and AccessRoles)
     *
     * @return List consisting of {@link com.zynap.talentstudio.security.roles.Role} objects
     */
    List getAllRoles();

    /**
     * Disable the role.
     * <br> Also removes the permissions associated with the role.
     *
     * @param id    The unique id of the role
     * @param principalId The id of the user disabling this role
     * @throws TalentStudioException
     */
    void disable(Long id, int principalId) throws TalentStudioException;

    /**
     * Update a role.
     *
     * @param role
     * @param principalId The id of the user updating this role
     * @throws com.zynap.exception.TalentStudioException
     */
    void update(Role role, int principalId) throws TalentStudioException;

    void update(Role role) throws TalentStudioException;

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
     * Get all the active permissions relating to access roles.
     *
     * @return List of active access permissions.
     */
    List getActiveAccessPermits();

    /**
     * Get all the active permissions relating to resource roles.
     *
     * @return List of active resource permissions.
     */
    List getActiveDomainObjectPermits();

    /**
     * Get all active permits (DomainObjectPermits or AccessPermits.)
     *
     * @return The List of active permits.
     */
    List<IPermit> getActivePermits();

    /**
     * Find Role by primary key.
     * @param id The id
     * @return Role
     */
    Role findRole(Long id) throws TalentStudioException;

    Role findArenaRole(String arenaId) throws TalentStudioException;
}

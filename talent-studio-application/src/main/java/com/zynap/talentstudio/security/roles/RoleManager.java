/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.security.roles;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.security.permits.IPermit;
import com.zynap.talentstudio.security.permits.IPermitManagerDao;

import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class RoleManager implements IRoleManager {

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
    public void create(Role role, int principalId) throws TalentStudioException {
        getRoleManagerDao().addRole(role, principalId);
    }

    /**
     * Get all the active roles (includes both ResourceRoles and AccessRoles.)
     *
     * @return List consisting of {@link com.zynap.talentstudio.security.roles.Role} objects
     */
    public List getAllActiveRoles() {
        return getRoleManagerDao().getAllActiveRoles();
    }

    /**
     * Get all the roles (active and inactive, includes both ResourceRoles and AccessRoles)
     *
     * @return List consisting of {@link com.zynap.talentstudio.security.roles.Role} objects
     */
    public List getAllRoles() {
        return getRoleManagerDao().getAllRoles();
    }

    /**
     * Disable the role.
     *
     * @param id The unique id of the role
     * @param principalId The id of the user disabling this role
     * @throws TalentStudioException
     *
     */
    public void disable(Long id, int principalId) throws TalentStudioException {
        getRoleManagerDao().disableRole(id, principalId);
    }

    /**
     * Update a role.
     *
     * @param role
     * @param principalId The id of the user updating this role
     * @throws com.zynap.exception.TalentStudioException
     *
     */
    public void update(Role role, int principalId) throws TalentStudioException {
        getRoleManagerDao().updateRole(role, principalId);
    }

    public void update(Role role) throws TalentStudioException {
        roleManagerDao.update(role);
    }

    /**
     * Get the active access roles.
     *
     * @return List consisting of {@link com.zynap.talentstudio.security.roles.AccessRole} objects
     */
    public List<Role> getActiveAccessRoles() {
        return getRoleManagerDao().getActiveAccessRoles();
    }

    public List<Role> getAllAccessRoles() {
        return getRoleManagerDao().getAllAccessRoles();
    }

    /**
     * Get the active resource roles.
     *
     * @return List consisting of {@link com.zynap.talentstudio.security.roles.ResourceRole } objects
     */
    public List getActiveResourceRoles() {
        return getRoleManagerDao().getActiveResourceRoles();
    }

    /**
     * Get all the active permissions relating to access roles.
     *
     * @return List of active permissions.
     */
    public List getActiveAccessPermits() {
        return getPermitManagerDao().getActiveAccessPermits();
    }

    /**
     * Get all the active permissions relating to resource roles.
     *
     * @return List of active permissions.
     */
    public List getActiveDomainObjectPermits() {
        return getPermitManagerDao().getActiveDomainObjectPermits();
    }

    /**
     * Get all active permits (DomainObjectPermits or AccessPermits.)
     *
     * @return The List of active permits.
     */
    public List<IPermit> getActivePermits() {
        return getPermitManagerDao().getActivePermits();
    }

    /**
     * Find Role by primary key.
     * @param id The id
     * @return Role
     */
    public Role findRole(Long id) throws TalentStudioException {
        return getRoleManagerDao().findRole(id);
    }

    public Role findArenaRole(String arenaId) throws TalentStudioException {
        return roleManagerDao.findRoleByArena(arenaId);
    }

    public void setRoleManagerDao(IRoleManagerDao roleManagerDao) {
        this.roleManagerDao = roleManagerDao;
    }

    public IRoleManagerDao getRoleManagerDao() {
        return roleManagerDao;
    }

    public IPermitManagerDao getPermitManagerDao() {
        return permitManagerDao;
    }

    public void setPermitManagerDao(IPermitManagerDao permitManagerDao) {
        this.permitManagerDao = permitManagerDao;
    }

    private IRoleManagerDao roleManagerDao;
    private IPermitManagerDao permitManagerDao;
}

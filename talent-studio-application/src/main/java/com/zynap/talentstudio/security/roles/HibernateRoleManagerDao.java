package com.zynap.talentstudio.security.roles;

import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.exception.TalentStudioException;

import org.springframework.orm.hibernate.HibernateObjectRetrievalFailureException;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * User: amark
 * Date: 18-Jan-2005
 * Time: 15:34:17
 */
public class HibernateRoleManagerDao extends HibernateDaoSupport implements IRoleManagerDao {

    /**
     * Add a new Role.
     *
     * @param role        The Role to add
     * @param principalId The id of the user adding this role
     * @throws com.zynap.exception.TalentStudioException
     *
     */
    public void addRole(Role role, int principalId) throws TalentStudioException {
        getHibernateTemplate().save(role);
    }

    /**
     * Disable the role.
     *
     * @param id          The role id
     * @param principalId The id of the user disabling this role
     * @throws com.zynap.exception.TalentStudioException
     *
     */
    public void disableRole(Long id, int principalId) throws TalentStudioException {

        Role role = findRole(id);
        role.setActive(false);
        role.clearPermits();
        updateRole(role, principalId);
    }

    /**
     * Update a role.
     *
     * @param role        The Role
     * @param principalId The id of the user updating this role
     * @throws com.zynap.exception.TalentStudioException
     *
     */
    public void updateRole(Role role, int principalId) throws TalentStudioException {
        getHibernateTemplate().update(role);
    }

    /**
     * Get the AccessRole associated with the label.
     *
     * @param label The role label
     * @return AccessRole The role if found; otherwise a <code>RoleNotFoundException</code> will be thrown
     * @throws com.zynap.exception.TalentStudioException
     *
     */
    public Role findAccessRole(String label) throws TalentStudioException {
        return findByLabel(label);
    }

    /**
     * Get the ResourceRole associated with the label.
     *
     * @param label The role label
     * @return ResourceRole The role if found; otherwise a <code>RoleNotFoundException</code> will be thrown
     * @throws com.zynap.exception.TalentStudioException
     *
     */
    public Role findResourceRole(String label) throws TalentStudioException {
        return findByLabel(label);
    }

    /**
     * Get all the active roles (includes both ResourceRoles and AccessRoles.)
     *
     * @return List consisting of {@link com.zynap.talentstudio.security.roles.Role} objects
     */
    public List getAllActiveRoles() {
        return getHibernateTemplate().findByNamedQuery("findActiveRoles");
    }

    /**
     * Get all the roles (active and inactive, includes both ResourceRoles and AccessRoles.)
     *
     * @return List consisting of {@link com.zynap.talentstudio.security.roles.Role} objects
     */
    public List getAllRoles() {
        return getHibernateTemplate().findByNamedQuery("findAllRoles");
    }

    /**
     * Get the active access roles.
     *
     * @return List consisting of {@link com.zynap.talentstudio.security.roles.AccessRole} objects
     */
    public List<Role> getActiveAccessRoles() {
        return getHibernateTemplate().findByNamedQuery("findActiveAccessRoles");
    }
    public List<Role> getAllAccessRoles() {
        return getHibernateTemplate().findByNamedQuery("findAllAccessRoles");
    }
    /**
     * Get the active resource roles.
     *
     * @return List consisting of {@link com.zynap.talentstudio.security.roles.ResourceRole } objects
     */
    public List getActiveResourceRoles() {
        return getHibernateTemplate().findByNamedQuery("findActiveResourceRoles");
    }

    /**
     * Find Role by primary key.
     *
     * @param id The id
     * @return Role
     * @throws DomainObjectNotFoundException If the role cannot be found
     */
    public Role findRole(Long id) throws DomainObjectNotFoundException {
        try {
            return (Role) getHibernateTemplate().load(Role.class, id);
        } catch (HibernateObjectRetrievalFailureException e) {
            throw new RoleNotFoundException(id, e);
        }
    }

    public Role findRoleByArena(String arenaId) throws TalentStudioException {
        if(!StringUtils.hasText(arenaId)) throw new IllegalArgumentException("Arena ID cannot be null or an empty string you gave: " + arenaId);
        List results = getHibernateTemplate().find("from com.zynap.talentstudio.security.roles.AccessRole role where role.arenaId='" + arenaId + "' and role.arenaRole = 'T'");
        if(results == null || results.isEmpty()) throw new DomainObjectNotFoundException(Role.class, "arenaId", arenaId);
        return (Role) results.get(0);
    }

    public void update(Role role) throws TalentStudioException {
        getHibernateTemplate().update(role);
    }

    /**
     * Find Role by label.
     *
     * @param label The role label
     * @return Role The role if found
     * @throws DomainObjectNotFoundException If the role cannot be found
     */
    private Role findByLabel(String label) throws DomainObjectNotFoundException {

        List list = getHibernateTemplate().findByNamedQuery("findByLabel", label);
        if (list.isEmpty()) {
            throw new RoleNotFoundException(label);
        } else {
            return (Role) list.get(0);
        }
    }
}

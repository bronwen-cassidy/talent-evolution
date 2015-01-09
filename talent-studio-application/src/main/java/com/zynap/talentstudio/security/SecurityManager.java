/*
 * Copyright (c) 2004 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.security;

import com.zynap.domain.UserPrincipal;
import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.SecurityConstants;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.positions.PositionService;
import com.zynap.talentstudio.security.areas.Area;
import com.zynap.talentstudio.security.areas.AreaElement;
import com.zynap.talentstudio.security.permits.IPermit;

import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * Business component for system security.
 *
 * @author Andreas Andersson
 * @see com.zynap.talentstudio.security.ISecurityManager
 * @see com.zynap.talentstudio.security.ISecurityManagerDao
 * @since 02/03/2004
 */
public class SecurityManager implements ISecurityManager {

    /**
     * Check if user has the specified permit to the specified Node.
     * <br> The node is used for checking domain permits rather than access permits.
     * <br> Sets has access field correctly.
     *
     * @param permit
     * @param user The user
     * @param node The node id
     */
    public void checkAccess(IPermit permit, UserPrincipal user, Node node) throws DomainObjectNotFoundException {
        if (checkDeletePermissions(permit, node.getId()))
            node.setHasAccess(false);
        else
            getSecurityManagerDao().checkAccess(permit, user.getUserId(), node);
    }

    /**
     * Check if the user has the specified permit.
     * <br> The nodeId is used for checking domain permits rather than access permits.
     *
     * @param permit
     * @param user The user
     * @param nodeId The node id
     * @return true if the user has access, otherwise false.
     */
    public boolean checkAccess(IPermit permit, UserPrincipal user, Long nodeId) throws DomainObjectNotFoundException {
        return !checkDeletePermissions(permit, nodeId) && getSecurityManagerDao().checkAccess(permit, user.getUserId(), nodeId);
    }

    /**
     * Get all domains in the system including inactive ones.
     *
     * @return Collection of {@link SecurityDomain} objects.
     */
    public Collection getAllDomains() {
        return getSecurityManagerDao().getAllDomains();
    }

    /**
     * Find selected domain.
     *
     * @param domainId The domain id
     * @return A {@link SecurityDomain} object.
     */
    public SecurityDomain findDomain(Long domainId) throws TalentStudioException {
        return getSecurityManagerDao().findDomain(domainId);
    }

    /**
     * Check if user is member of specified domain.
     *
     * @param domainId
     * @param userId
     * @return true or false
     * @throws TalentStudioException
     */
    public boolean isDomainMember(Long domainId, Long userId) throws TalentStudioException {
        return getSecurityManagerDao().isDomainMember(domainId, userId);
    }

    /**
     * Add a new domain.
     *
     * @param domain The new {@link SecurityDomain}
     * @throws TalentStudioException
     */
    public void createDomain(SecurityDomain domain) throws TalentStudioException {
        getSecurityManagerDao().createDomain(domain);
    }

    /**
     * Edit the domain.
     *
     * @param domain The modified {@link SecurityDomain} object
     * @throws com.zynap.exception.TalentStudioException
     */
    public void updateDomain(SecurityDomain domain) throws TalentStudioException {
        getSecurityManagerDao().updateDomain(domain);
    }

    /**
     * Delete the selected domain.
     *
     * @param domainId The domain id
     */
    public void deleteDomain(Long domainId) throws TalentStudioException {
        getSecurityManagerDao().deleteDomain(domainId);
    }

    /**
     * Find the selected area.
     *
     * @param areaId The area id
     * @return A {@link com.zynap.talentstudio.security.areas.Area} object
     * @throws com.zynap.exception.TalentStudioException
     */
    public Area findArea(Long areaId) throws TalentStudioException {
        return getSecurityManagerDao().findArea(areaId);
    }

    /**
     * Get all areas.
     *
     * @return Collection of {@link Area} objects.
     */
    public Collection<Area> getAreas() {
        return getSecurityManagerDao().getAreas();
    }

    public Collection<Area> getPopulationAreas() {
        return getSecurityManagerDao().getPopulationAreas();
    }

    /**
     * Add a new area.
     *
     * @param area The new {@link com.zynap.talentstudio.security.areas.Area}
     */
    public void createArea(Area area) {     
        getSecurityManagerDao().createArea(area);
    }

    /**
     * Edit the area.
     *
     * @param area The modified {@link Area} object
     * @throws TalentStudioException
     */
    public void updateArea(Area area) throws TalentStudioException {
        getSecurityManagerDao().updateArea(area);
    }

    /**
     * Delete the selected area.
     *
     * @param areaId The area id
     */
    public void deleteArea(Long areaId) throws TalentStudioException {
        getSecurityManagerDao().deleteArea(areaId);
    }

    public List<SecurityDomain> getUserDomains(Long userId) {
        return  getSecurityManagerDao().getUserDomains(userId);
    }

    public Set<AreaElement> getAreaElements(Long areaId) {
        return securityManagerDao.getAreaElements(areaId);
    }

    public void removeAreaElements(Area area) {
        securityManagerDao.removeAreaElements(area);
    }

    public void createAreaElements(Area updatedArea, Set<AreaElement> popElements) throws TalentStudioException {
        //Area area = findArea(updatedArea.getId());
        for (AreaElement popElement : popElements) {
            popElement.setArea(new Area(updatedArea.getId()));
            securityManagerDao.updateAreaElement(popElement);
        }
    }

    public ISecurityManagerDao getSecurityManagerDao() {
        return securityManagerDao;
    }

    public void setSecurityManagerDao(ISecurityManagerDao securityManagerDao) {
        this.securityManagerDao = securityManagerDao;
    }

    /**
     * Check if we are using the permit to delete a system Node such as the default position.
     *
     * @param permit The permit
     * @param nodeId The node id
     * @return true if the permit's action is delete and the node is a system node.
     */
    private boolean checkDeletePermissions(IPermit permit, Long nodeId) {
        return permit.getAction().equals(SecurityConstants.DELETE_ACTION) && isSystemNode(nodeId);
    }

    private boolean isSystemNode(Long id) {
        if (id == PositionService.POSITION_DEFAULT || id.equals(OrganisationUnit.ROOT_ORG_UNIT_ID))
            return true;
        else
            return false;
    }

    private ISecurityManagerDao securityManagerDao;
    
}

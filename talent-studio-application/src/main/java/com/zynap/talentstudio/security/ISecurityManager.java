/*
 * Copyright (c) 2004 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.security;

import com.zynap.domain.UserPrincipal;
import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.security.areas.Area;
import com.zynap.talentstudio.security.areas.AreaElement;
import com.zynap.talentstudio.security.permits.IPermit;

import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * Interface to be implemented by business components responsible for the security.
 *
 * @author  Andreas Andersson
 * @since   02/03/2004
 * @version $Revision: $
 *          $Id: $
 */
public interface ISecurityManager {

    /**
     * Check if user has the specified permit to the specified Node.
     * <br> The node is used for checking domain permits rather than access permits.
     * <br> Sets has access field correctly.
     *
     * @param permit
     * @param user The user id
     * @param node The node id
     */
    void checkAccess(IPermit permit, UserPrincipal user, Node node) throws DomainObjectNotFoundException;

    /**
     * Check if the user has the specified permit.
     * <br> The nodeId is used for checking domain permits rather than access permits.
     *
     * @param permit
     * @param user The user
     * @param nodeId The node id
     * @return true if the user has access, otherwise false.
     */
    boolean checkAccess(IPermit permit, UserPrincipal user, Long nodeId) throws DomainObjectNotFoundException;

    /**
     * Get all domains in the system including inactive ones.
     *
     * @return Collection of {@link SecurityDomain} objects.
     */
    Collection getAllDomains();

    /**
     * Find selected domain.
     * @param domainId The domain id
     * @return A {@link SecurityDomain} object.
     */
    SecurityDomain findDomain(Long domainId) throws TalentStudioException;

    /**
     * Check if user is member of specified domain.
     * @param domainId
     * @param userId
     * @return true or false
     * @throws TalentStudioException
     */
    public boolean isDomainMember(Long domainId, Long userId) throws TalentStudioException;

    /**
     * Add a new domain.
     *
     * @param domain The new {@link SecurityDomain}
     * @throws TalentStudioException
     */
    public void createDomain(SecurityDomain domain) throws TalentStudioException;

    /**
     * Edit the domain.
     *
     * @param domain The modified {@link SecurityDomain} object
     * @throws TalentStudioException
     */
    public void updateDomain(SecurityDomain domain) throws TalentStudioException;

    /**
     * Delete the selected domain.
     * @param domainId The domain id
     */
    void deleteDomain(Long domainId) throws TalentStudioException;

    /**
     * Find the selected area.
     * 
     * @param areaId The area id
     * @return A {@link com.zynap.talentstudio.security.areas.Area} object
     * @throws TalentStudioException
     */
    Area findArea(Long areaId) throws TalentStudioException;

    /**
     * Get all areas.
     * @return Collection of {@link Area} objects.
     */
    Collection<Area> getAreas();

    /**
     * Get all areas based on a population.
     * @return Collection of {@link Area} objects.
     */
    Collection<Area> getPopulationAreas();

    /**
     * Add a new area.
     * @param area  The new {@link com.zynap.talentstudio.security.areas.Area}
     */
    void createArea(Area area);

    /**
     * Edit the area.
     *
     * @param area The modified {@link Area} object
     * @throws TalentStudioException
     */
    void updateArea(Area area) throws TalentStudioException;

    /**
     * Delete the selected area.
     * @param areaId The area id
     */
    void deleteArea(Long areaId) throws TalentStudioException;

    List<SecurityDomain> getUserDomains(Long userId);

    void createAreaElements(Area updatedArea, Set<AreaElement> popElements) throws TalentStudioException;

    Set<AreaElement> getAreaElements(Long areaId);
    void removeAreaElements(Area area);

    Long ORPHANS_AREA_ID = new Long(-2) ;
    Long ORPHANS_DOMAIN_ID = new Long(-2) ;
}

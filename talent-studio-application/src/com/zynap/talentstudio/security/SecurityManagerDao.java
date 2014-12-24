package com.zynap.talentstudio.security;

import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.HibernateCrudAdaptor;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.security.areas.Area;
import com.zynap.talentstudio.security.areas.AreaElement;
import com.zynap.talentstudio.security.permits.IPermit;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * DAO that checks a user's access to secure resources.
 * <p/>
 * User: amark
 * Date: 25-Jan-2005
 * Time: 16:51:05
 */
public class SecurityManagerDao extends HibernateCrudAdaptor implements ISecurityManagerDao {

    public Class getDomainObjectClass() {
        return SecurityDomain.class;
    }

    /**
     * Check if user has the specified permit to the specified Node.
     * <br> The node is used for checking domain permits rather than access permits.
     * <br> Sets has access field correctly.
     *
     * @param permit The permit
     * @param userId The user id
     * @param node   The node id
     */
    public void checkAccess(IPermit permit, Long userId, Node node) throws DomainObjectNotFoundException {
        if (!node.isHasAccess()) {
            boolean access = checkAccess(permit, userId, node.getId());
            node.setHasAccess(access);
        }
    }

    /**
     * Check if user has the specified permit to the specified Node.
     * <br> The node is used for checking domain permits rather than access permits.
     *
     * @param permit The permit
     * @param userId The user id
     * @param nodeId The node id
     * @return true if the user has access, otherwise false.
     */
    public boolean checkAccess(IPermit permit, Long userId, Long nodeId) throws DomainObjectNotFoundException {

        List domainPermits = getHibernateTemplate().findByNamedQuery("findDomainObjectPermitForUserAndNode", new Object[]{permit.getId(), userId, nodeId});
        boolean empty = domainPermits.isEmpty();

        if (empty) {
            List nodeResult = getHibernateTemplate().find("from Node node where node.id=?", new Object[]{nodeId});
            if (nodeResult.isEmpty()) throw new DomainObjectNotFoundException(Node.class, nodeId);
        }

        return !empty;
    }

    /**
     * Get all domains in the system including inactive ones ordered by label.
     *
     * @return Collection of {@link SecurityDomain} objects.
     */
    public Collection getAllDomains() {
        return getHibernateTemplate().find("from SecurityDomain domain order by upper(domain.label)");
    }

    /**
     * Find selected domain.
     *
     * @param domainId The domain id
     * @return A {@link SecurityDomain} object.
     */
    public SecurityDomain findDomain(Long domainId) throws TalentStudioException {
        return (SecurityDomain) super.findByID(SecurityDomain.class, domainId);
    }

    /**
     * Check if user is member of specified domain.
     *
     * @param domainId
     * @param userId
     * @return true or false
     * @throws com.zynap.exception.TalentStudioException
     *
     */
    public boolean isDomainMember(Long domainId, Long userId) throws TalentStudioException {

        final String query = "select sd from SecurityDomain sd where sd.id=? and ? in elements(sd.users)";
        final List list = getHibernateTemplate().find(query, new Object[]{domainId, userId});

        return (list.size() > 0);
    }

    /**
     * Add a new domain.
     *
     * @param domain The new {@link SecurityDomain}
     * @throws com.zynap.exception.TalentStudioException
     *
     */
    public void createDomain(SecurityDomain domain) throws TalentStudioException {
        getHibernateTemplate().save(domain);
    }

    /**
     * Edit the domain.
     *
     * @param domain The modified {@link SecurityDomain} object
     * @throws com.zynap.exception.TalentStudioException
     *
     */
    public void updateDomain(SecurityDomain domain) throws TalentStudioException {
        getHibernateTemplate().update(domain);
    }

    /**
     * Delete the selected domain.
     *
     * @param domainId The domain id
     */
    public void deleteDomain(Long domainId) throws TalentStudioException {
        getHibernateTemplate().delete(findDomain(domainId));
    }

    /**
     * Find the selected area.
     *
     * @param areaId The area id
     * @return A {@link Area} object
     * @throws TalentStudioException
     */
    public Area findArea(Long areaId) throws TalentStudioException {
        return (Area) findByID(Area.class, areaId);
    }

    /**
     * Get all areas ordered by label.
     *
     * @return Collection of {@link Area} objects.
     */
    public Collection<Area> getAreas() {
        return getHibernateTemplate().find("from Area area order by upper(area.label)");
    }

    public Collection<Area> getPopulationAreas() {
        return getHibernateTemplate().find("from Area area where (area.subjectPopulationId is not null or area.positionPopulationId is not null)");
    }

    /**
     * Add a new area.
     *
     * @param area The new {@link Area}
     */
    public void createArea(Area area) {
        getHibernateTemplate().save(area);
    }

    /**
     * Edit the area.
     *
     * @param area The modified {@link Area} object
     * @throws TalentStudioException
     */
    public void updateArea(Area area) throws TalentStudioException {
        getHibernateTemplate().update(area);
    }

    public void updateAreaElement(AreaElement areaElement) {
        getHibernateTemplate().save(areaElement);
    }

    public Set<AreaElement> getAreaElements(Long areaId) {
        List<AreaElement> results = getHibernateTemplate().find("from AreaElement e where e.area.id = ?", new Object[]{areaId});
        return new HashSet<AreaElement>(results);
    }

    public void removeAreaElements(Area area) {
        getHibernateTemplate().delete("from AreaElement ae where ae.area.id= "  +area.getId()+" and ae.fromPopulation = 'T'");
    }

    /**
     * Delete the selected area.
     *
     * @param areaId The area id
     */
    public void deleteArea(Long areaId) throws TalentStudioException {
        getHibernateTemplate().delete(findArea(areaId));
    }

    public List<SecurityDomain> getUserDomains(Long userId) {
       final String query = "select sd from SecurityDomain sd where ? in elements(sd.users)";
        return getHibernateTemplate().find(query, new Object[]{userId});
    }
}

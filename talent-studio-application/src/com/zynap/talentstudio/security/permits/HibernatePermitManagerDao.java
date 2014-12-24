package com.zynap.talentstudio.security.permits;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.HibernateCrudAdaptor;
import com.zynap.talentstudio.common.SecurityConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: amark
 * Date: 20-Jan-2005
 * Time: 13:24:09
 */
public class HibernatePermitManagerDao extends HibernateCrudAdaptor implements IPermitManagerDao {

    public Class getDomainObjectClass() {
        return Permit.class;
    }

    /**
     * Get all the active permissions relating to access roles.
     *
     * @return List of active permissions.
     */
    public List<IPermit> getActiveAccessPermits() {
        return getHibernateTemplate().findByNamedQuery("findActiveAccessPermits");
    }

    /**
     * Get all the active permissions relating to resource roles.
     *
     * @return List of active permissions.
     */
    public List<IPermit> getActiveDomainObjectPermits() {
        return getHibernateTemplate().findByNamedQuery("findActiveDomainObjectPermits");
    }

    /**
     * Get all active permits (DomainObjectPermits or AccessPermits.)
     *
     * @return The List of active permits.
     */
    public List<IPermit> getActivePermits() {
        //noinspection unchecked
        return getHibernateTemplate().findByNamedQuery("findActivePermits");
    }

    public IPermit getPermit(String content, String action) {
        List result = getHibernateTemplate().findByNamedQuery("findDomainObjectPermit", new Object[]{content, action});
        if (result.isEmpty()) {
            throw new PermitNotFoundException("No permit found for content " + content + " and action " + action);
        }
        return (Permit) result.get(0);
    }

    public Collection<IPermit> getAccessPermits(Long userId) {
        return getHibernateTemplate().findByNamedQuery("findAccessPermitsByUser", userId);
    }

    public Collection<IPermit> getContentTypePermits(Long principalId, String content, String action) {
        return getHibernateTemplate().findByNamedQuery("findContentTypePermits", new Object[]{principalId, content, action});
    }

    /**
     * Get ids of nodes of the specified type(s) the user has access to.
     *
     * @param userId The user id
     * @param nodeTypes The node types
     * @param action The permit action
     * @return Collection of Longs
     */
    public Collection getNodeIds(Long userId, String[] nodeTypes, String action) {

        // build query string dynamically
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("select node.id from com.zynap.talentstudio.organisation.Node node,");
        stringBuffer.append(" com.zynap.talentstudio.security.UserDomainPermit udp,");
        stringBuffer.append(" com.zynap.talentstudio.security.permits.Permit permit");
        stringBuffer.append(" where node.id = udp.nodeId and udp.userId = ?");
        stringBuffer.append(" and permit.id = udp.permitId and permit.action = ? and node.nodeType in (");

        // add node types to query
        List<Object> temp = new ArrayList<Object>();
        temp.add(userId);
        temp.add(action);
        for (int i = 0; i < nodeTypes.length; i++) {
            String nodeType = nodeTypes[i];
            stringBuffer.append("'").append(nodeType).append("'");
            if (i < nodeTypes.length - 1) stringBuffer.append(",");
        }

        // close bracket round IN clause
        stringBuffer.append(")");

        return getHibernateTemplate().find(stringBuffer.toString(), temp.toArray());
    }

    /**
     * Get all permits for the specified action and content.
     *
     * @param action
     * @param content
     * @return Collection (never null)
     * @throws com.zynap.exception.TalentStudioException
     */
    public Collection getPermits(String action, String content) throws TalentStudioException {
        String query = "from Permit permit where permit.action=? and permit.content=? ";
        return getHibernateTemplate().find(query, new Object[]{action, content});
    }

    public Long getViewOrgUnitPermitId() {
        return getPermit(SecurityConstants.ORGANISATION_CONTENT, SecurityConstants.VIEW_ACTION).getId();
    }
}

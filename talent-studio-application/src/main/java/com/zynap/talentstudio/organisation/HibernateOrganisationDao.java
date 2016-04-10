/**
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation;

import com.zynap.common.persistence.ZynapPersistenceSupport;
import com.zynap.domain.QueryParameter;
import com.zynap.domain.UserSession;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.security.UserSessionFactory;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 1.0
 * @since 08-Jul-2005 16:30:30
 */
public class HibernateOrganisationDao extends ZynapPersistenceSupport implements IOrganisationDao {

    public Class getDomainObjectClass() {
        return OrganisationUnit.class;
    }

    public OrganisationUnit findById(Long organisationUnitId) throws TalentStudioException {
        Long id = organisationUnitId != null ? organisationUnitId : OrganisationUnit.ROOT_ORG_UNIT_ID;
        return (OrganisationUnit) super.findById(id);
    }

    public List<OrganisationUnit> findOrgUnitTree(Long organisationUnitId) {
        String sql = "select ouh.descendent from OuHierarchyInc ouh  where ouh.root.id = "
                + organisationUnitId + " and ouh.descendent.active = 'T' order by ouh.hlevel, ouh.descendent.label";
        return getHibernateTemplate().find(sql);
    }

    public List<OrganisationUnit> findSecureOrgUnitTree(Long userId, Long permitId) {

        StringBuffer sql = new StringBuffer();
        StringBuffer sql2 = new StringBuffer();
        sql.append("select ouh.descendent, ouh.hlevel from OuHierarchyInc ouh  where ouh.root.id = ").append(OrganisationUnit.ROOT_ORG_UNIT_ID).append(" and ouh.descendent.active = 'T' ");
        sql2.append(sql);
        sql.append("AND EXISTS ");
        sql2.append("AND NOT EXISTS ");
        sql.append("( SELECT dp.nodeId FROM UserDomainPermit dp where ouh.descendent.id =  dp.nodeId and dp.userId = :userId AND dp.permitId = :permitId ) ");
        sql2.append("( SELECT dp.nodeId FROM UserDomainPermit dp where ouh.descendent.id =  dp.nodeId and dp.userId = :userId AND dp.permitId = :permitId ) ");
        List list = getHibernateTemplate().findByNamedParam(sql.toString(), new String[]{"userId", "permitId"}, new Object[]{userId, permitId});
        List list2 = getHibernateTemplate().findByNamedParam(sql2.toString(), new String[]{"userId", "permitId"}, new Object[]{userId, permitId});
        com.zynap.talentstudio.util.collections.CollectionUtils.hasAccessTransformer(list, list2);
        com.zynap.talentstudio.util.collections.CollectionUtils.sortByLevel(list);
        com.zynap.talentstudio.util.collections.CollectionUtils.transformFromObjectArray(list);
        return list;


    }

    public List findOrgUnitsBySubjectId(Long subjectId) {
        String sql = "select ou from OrganisationUnit ou join ou.positions p join p.subjectAssociations sa where sa.subject.id = ? ";
        return getHibernateTemplate().find(sql, subjectId);
    }

    public List<OrganisationUnit> findValidParents(Long orgUnitId, Long userId, Long permitId) {

        final List<Object> params = new ArrayList<Object>();

        final StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("select ouh1.descendent from OuHierarchyInc ouh1, UserDomainPermit dp where ")
                .append(" ouh1.root.id = ").append(OrganisationUnit.ROOT_ORG_UNIT_ID)
                .append(" and ouh1.descendent.active = 'T' ");

        if (orgUnitId != null) {
            stringBuffer.append(" and not exists ");
            stringBuffer.append(" (select ouh.root.id from OuHierarchyInc ouh where ouh.root.id = ? and ouh.descendent.id = ouh1.descendent.id) ");
            params.add(orgUnitId);
        }

        stringBuffer.append(" and ouh1.descendent.id = dp.nodeId and dp.userId=? and dp.permitId=?").append(" order by ouh1.hlevel");

        params.add(userId);
        params.add(permitId);

        final String sql = stringBuffer.toString();
        return getHibernateTemplate().find(sql, params.toArray());
    }

    public List<OrganisationUnit> findAllSecure(Long userId, Long permitId) {
        StringBuffer sql = new StringBuffer("SELECT ou ");
        sql.append(" FROM OrganisationUnit ou, UserDomainPermit dp ");
        sql.append(" WHERE ou.active ='T'and ou.id = dp.nodeId and  dp.userId =? and dp.permitId=? order by upper(ou.label)");
        return getHibernateTemplate().find(sql.toString(), new Object[]{userId, permitId});
    }

    public List search(OrgUnitSearchQuery searchQuery, Long userId, Long permitId) {
        if (searchQuery != null && !searchQuery.getMappedResults().isEmpty()) {

            StringBuffer sql = new StringBuffer("SELECT ou FROM OrganisationUnit ou, UserDomainPermit dp ");
            sql.append(" WHERE  ou.active ='T'and ou.id = dp.nodeId and dp.userId =? and dp.permitId=?");

            Map mappedValues = searchQuery.getMappedResults();
            for (Iterator iterator = mappedValues.entrySet().iterator(); iterator.hasNext();) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = (String) entry.getKey();
                QueryParameter qp = (QueryParameter) entry.getValue();
                qp.buildClause("ou", key, sql);
            }
            return getHibernateTemplate().find(sql.toString(), new Object[]{userId, permitId});
        } else {
            return findAllSecure(userId, permitId);
        }

    }

    public OrganisationUnit findOrgUnitBySubjectUserId(Long id) {
        final List orgUnits = getHibernateTemplate().find("select ou from OrganisationUnit ou join ou.positions p join p.subjectAssociations sa where sa.subject.user.id = ? ", id);

        return (OrganisationUnit) (orgUnits.isEmpty() ? null : orgUnits.get(0));
    }

    public OrganisationUnit findOrgUnitByUserId(Long id, Long permitId) {
        final List orgUnits = findValidParents(null, id, permitId);

        return (OrganisationUnit) (orgUnits.isEmpty() ? null : orgUnits.get(0));
    }
    public OrganisationUnit findOrgUnitByPositionId(Long positionId) {
        String sql = "select p.organisationUnit from Position p where p.id = ? ";
        final List list = getHibernateTemplate().find(sql, positionId);

        return (OrganisationUnit) (list.isEmpty() ? null : list.get(0));
    }

    public List<OrganisationUnit> findDecendants(Long organisationUnitId) {
        String queryString = "select ph.descendent from OuHierarchy ph where ph.root.id = :organisationUnitId order by hlevel desc ";
        return getHibernateTemplate().findByNamedParam(queryString, "organisationUnitId", organisationUnitId);
    }

    public void deleteOrganisationCascade(OrganisationUnit domainObject) throws TalentStudioException {
        final UserSession userSession = UserSessionFactory.getUserSession();
        new DeleteOrganisationCascade(getJdbcTemplate()).execute(domainObject.getId(), userSession.getId(), userSession.getUserName());
    }

    private class DeleteOrganisationCascade extends StoredProcedure {

        public DeleteOrganisationCascade(JdbcTemplate jdbcTemplate) {
            super(jdbcTemplate, "zynap_org_unit_sp.delete_org_unit");
            declareParameter(new SqlParameter("org_unit_id_", Types.INTEGER));
            declareParameter(new SqlParameter("user_id_", Types.INTEGER));
            declareParameter(new SqlParameter("username_", Types.VARCHAR));
        }

        public void execute(Long organisationUnitId, Long id, String userName) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("org_unit_id_", organisationUnitId);
            params.put("user_id_", id);
            params.put("username_", userName);
            super.execute(params);
        }
    }
}

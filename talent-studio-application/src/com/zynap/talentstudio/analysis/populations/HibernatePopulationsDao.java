package com.zynap.talentstudio.analysis.populations;

import com.zynap.domain.IDomainObject;
import com.zynap.talentstudio.common.AccessType;
import com.zynap.talentstudio.common.HibernateCrudAdaptor;

import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

/**
 * Class or Interface description.
 *
 * @author jsuiras
 * @version 0.1
 * @since 23-Feb-2005 13:55:02
 */
public class HibernatePopulationsDao extends HibernateCrudAdaptor implements IPopulationDao {

    public Class getDomainObjectClass() {
        return Population.class;
    }

    public Collection<PopulationDto> findPopulations(String type, Long userId, boolean publicOnly) {

        final String prefix = "pop";

        String whereClause = "";
        // apply the where clause of public scope if the user is not the root user, su gets to see everything
        final boolean isNotRootUser = !IDomainObject.ROOT_USER_ID.equals(userId);
        if (isNotRootUser) {
            whereClause = "WHERE " + getScopeCriteria(prefix, publicOnly, userId);
        }

        if (type != null && type.length() > 0) {
            if (StringUtils.hasText(whereClause)) {
                whereClause = whereClause + " and";
            } else {
                whereClause = "WHERE ";
            }
            whereClause = whereClause + " pop.type = '" + type + "'";
        }

        String sql = "select new " + PopulationDto.class.getName() + " ( pop.id, pop.label, pop.type, pop.scope, pop.description ) FROM Population pop  " + whereClause + getOrderByClause(prefix);
        return getHibernateTemplate().find(sql);
    }

    /**
     * Finds populations valid for the logged in user given their userId and groupId.
     *
     * @param nodeType - can never be null as we only select these from created views, running reports from the home page or in person, position searches
     * @param userId   - the logged in user
     * @param groupId  - the group the user blongs to (if su this is ignored)
     * @return all populationDto objects where groupId = groupId or groupId is null.
     */
    public Collection<PopulationDto> findPopulations(String nodeType, Long userId, Long groupId) {
        // groups do not apply to the root user
        if(IDomainObject.ROOT_USER_ID.equals(userId)) return findPopulations(nodeType, userId, false);

        StringBuffer sql = new StringBuffer("select new ");
        sql.append(PopulationDto.class.getName())
                .append(" ( pop.populationId, pop.label, pop.type, pop.scope, pop.description ) FROM PopulationGroupView pop")
                .append(" WHERE pop.type = '").append(nodeType).append("'");

        sql.append(" AND ").append(getScopeCriteria("pop", false, userId));
        sql.append(" AND (pop.groupId=").append(groupId).append(" OR pop.groupId is null)");
        sql.append(getOrderByClause("pop"));
        return getHibernateTemplate().find(sql.toString());
    }

    public boolean populationInPublicReport(Long populationId) {
        String query = new StringBuilder().append("select count(*) from Report r where r.defaultPopulation.id = :id and r.accessType = '").append(AccessType.PUBLIC_ACCESS.toString()).append("'").toString();
        List list = getHibernateTemplate().findByNamedParam(query, "id", populationId);
        return ((Number) list.get(0)).intValue() > 0;
    }

    private String getScopeCriteria(String prefix, boolean publicOnly, Long userId) {
        String criteria = "(" + prefix + ".scope = '" + AccessType.PUBLIC_ACCESS + "'";
        if (!publicOnly) criteria += " or " + prefix + ".userId = " + userId;
        criteria += " )";
        return criteria;
    }

    private String getOrderByClause(String prefix) {
        return " order by upper(" + prefix + ".label)";
    }
}

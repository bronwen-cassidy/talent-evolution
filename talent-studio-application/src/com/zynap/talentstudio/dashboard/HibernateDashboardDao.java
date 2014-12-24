/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.dashboard;

import com.zynap.talentstudio.common.HibernateCrudAdaptor;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.security.roles.Role;

import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 17-May-2010 17:42:52
 */
public class HibernateDashboardDao extends HibernateCrudAdaptor implements IDashboardDao {

    @Override
    public Class getDomainObjectClass() {
        return Dashboard.class;
    }

    public void addParticipants(Dashboard dashboard, Collection<Subject> subjects) {
        for (Subject subject : subjects) {
            getHibernateTemplate().save(new DashboardParticipant(dashboard.getId(), subject.getId()));
        }
    }

    public void deleteParticipants(Serializable dashboardId) {
        getHibernateTemplate().delete("from DashboardParticipant participant where participant.dashboardId = " + dashboardId);
    }

    public List<Dashboard> findSubjectDashboards(Group userGroup, Collection<Role> userRoles, Subject subject) {
        StringBuffer buffer = new StringBuffer("from Dashboard d, DashboardParticipant dp ");
        buffer.append(" where dp.dashboardId = d.id")
                .append(" and dp.subjectId = ").append(subject.getId());

        List<Object[]> results = getHibernateTemplate().find(buffer.toString());
        List<Dashboard> filtered = new ArrayList<Dashboard>();
        for (Object[] object : results) {
            Dashboard dashboard = (Dashboard) object[0];
            final boolean containsAny = CollectionUtils.containsAny(dashboard.getRoles(), userRoles);
            if (dashboard.getGroups().contains(userGroup) || containsAny) {
                filtered.add(dashboard);
            }
        }
        return filtered;
    }

    public List<Dashboard> findPersonalDashboards(Subject subject) {
        StringBuffer buffer = new StringBuffer("from Dashboard d, DashboardParticipant dp ");
        buffer.append(" where dp.dashboardId = d.id")
                .append(" and dp.subjectId = ").append(subject.getId())
                .append(" and d.type='").append(Dashboard.PERSONAL_TYPE).append("'");

        List<Object[]> results = getHibernateTemplate().find(buffer.toString());
        List<Dashboard> filtered = new ArrayList<Dashboard>();
        for (Object[] object : results) {
            Dashboard dashboard = (Dashboard) object[0];
            filtered.add(dashboard);
        }
        return filtered;
    }
}

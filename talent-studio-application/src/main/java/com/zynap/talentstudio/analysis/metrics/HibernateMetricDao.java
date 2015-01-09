/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.analysis.metrics;

import com.zynap.talentstudio.common.AccessType;
import com.zynap.talentstudio.common.HibernateCrudAdaptor;

import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class HibernateMetricDao extends HibernateCrudAdaptor implements IMetricDao {

    public Class getDomainObjectClass() {
        return Metric.class;
    }

    public List<Metric> findAll(Long userId) {
        String query = "from Metric metric where metric.artefactType is not null and (metric.accessType=? or metric.userId=?) order by upper(metric.label)";
        return getHibernateTemplate().find(query, new Object[]{AccessType.PUBLIC_ACCESS.toString(), userId});
    }

    public List<Metric> findAll(Long userId, String type, boolean publicOnly) {

        Object[] params = new Object[]{type, AccessType.PUBLIC_ACCESS.toString()};

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("from Metric metric where metric.artefactType=? and (metric.accessType=? ");

        // if not public include user id in query
        if (!publicOnly) {
            params = new Object[]{type, AccessType.PUBLIC_ACCESS.toString(), userId};
            stringBuffer.append("or metric.userId=? ");
        }

        // close bracket and add order by clause
        stringBuffer.append(") order by upper(metric.label)");

        return getHibernateTemplate().find(stringBuffer.toString(), params);
    }

    public boolean metricInPublicReport(Long id) {
        StringBuffer query = new StringBuffer("select count(*) from Report r where ");
        query.append(" r.accessType = '").append(AccessType.PUBLIC_ACCESS.toString()).append("'");
        query.append(" and (r.defaultMetric.id = :id or exists ( select c.id from Column c where c.report.id = r.id and c.metric.id =:id ))");
        List list = getHibernateTemplate().findByNamedParam(query.toString(), "id", id);

        return ((Number) list.get(0)).intValue() > 0;
    }
}

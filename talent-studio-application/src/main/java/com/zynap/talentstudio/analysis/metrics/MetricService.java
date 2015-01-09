/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.analysis.metrics;

import com.zynap.domain.IDomainObject;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.AccessType;
import com.zynap.talentstudio.common.IFinder;
import com.zynap.talentstudio.common.IModifiable;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class MetricService implements IMetricService {

    /**
     * Creates a metric.
     *
     * @param object
     * @throws com.zynap.exception.TalentStudioException
     */
    public void create(IDomainObject object) throws TalentStudioException {
        Metric metric = (Metric) object;
        if(!IPopulationEngine.COUNT.equals(metric.getOperator())) {
            metric.setValue(null);
        }
        metricDao.create(object);
    }

    /**
     * Finds a given metric.
     *
     * @param id
     * @return the specified report.
     */
    public IDomainObject findById(Serializable id) throws TalentStudioException {
        return metricDao.findByID(id);
    }

    /**
     * Updates the given metric.
     *
     * @param domainObject
     * @throws com.zynap.exception.TalentStudioException
     */
    public void update(IDomainObject domainObject) throws TalentStudioException {
        Metric metric = (Metric) domainObject;
        if(!IPopulationEngine.COUNT.equals(metric.getOperator())) {
            metric.setValue(null);
        }
        metricDao.update(domainObject);
    }

    /**
     * deletes the metric.
     *
     * @param metric
     * @throws com.zynap.exception.TalentStudioException
     */
    public void delete(IDomainObject metric) throws TalentStudioException {
        metricDao.delete(metric);
    }

    /**
     * Finds all metrics.
     *
     * @return Collection containing {@link com.zynap.talentstudio.analysis.metrics.Metric }
     * @param userId
     */
    public List findAll(Long userId) {
        return metricDao.findAll(userId);
    }


    public List findAll() throws TalentStudioException {
        return findAll(ROOT_USER_ID);
    }

    /**
     * Either find all public plus private belonging to user, or all public only.
     * @param userId
     * @param type
     * @param reportAccess
     * @return
     * @throws TalentStudioException
     */
    public List<Metric> findAll(Long userId, String type, String reportAccess) throws TalentStudioException {
        return metricDao.findAll(userId, type, AccessType.PUBLIC_ACCESS.toString().equals(reportAccess));
    }

    /**
     * Finds position or subject metrics that can be run.
     * <br> Includes private metrics created by this user.
     *
     * @param userId
     * @param type Being either {@link com.zynap.talentstudio.analysis.populations.IPopulationEngine#P_POS_TYPE_} or {@link com.zynap.talentstudio.analysis.populations.IPopulationEngine#P_SUB_TYPE_}
     * @return Collection of metrics
     */
    public Collection findAvailableMetrics(Long userId, String type) {
        return metricDao.findAll(userId, type, false);
    }

    public boolean metricInPublicReport(Long id) {
        return metricDao.metricInPublicReport(id);
    }

    public void disable(IDomainObject domainObject) throws TalentStudioException {
        throw new UnsupportedOperationException("Cannot disble metrics");
    }

    public void updateStateInfo(IDomainObject domainObject) {}

    protected IFinder getFinderDao() {
        return metricDao;
    }

    protected IModifiable getModifierDao() {
        return metricDao;
    }

    public IMetricDao getMetricDao() {
        return metricDao;
    }

    public void setMetricDao(IMetricDao metricDao) {
        this.metricDao = metricDao;
    }

    private IMetricDao metricDao;
}

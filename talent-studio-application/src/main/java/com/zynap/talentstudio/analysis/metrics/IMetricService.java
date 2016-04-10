/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.analysis.metrics;

import com.zynap.domain.IDomainObject;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.IZynapService;

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
public interface IMetricService extends IZynapService {

    /**
     * Creates a metric.
     *
     * @param object
     * @throws com.zynap.exception.TalentStudioException
     */
    void create(IDomainObject object) throws TalentStudioException;

    /**
     * Updates the given metric.
     *
     * @param domainObject
     * @throws TalentStudioException
     */
    void update(IDomainObject domainObject) throws TalentStudioException;

    /**
     * Finds all metrics ordered by label.
     *
     * @return Collection containing {@link com.zynap.talentstudio.analysis.metrics.Metric } obejcts
     * @param userId
     */
    List findAll(Long userId);

    /**
     * Finds position or subject metrics ordered by label.
     *
     * @param userId
     * @param type Being either {@link com.zynap.talentstudio.analysis.populations.IPopulationEngine#P_POS_TYPE_} or {@link com.zynap.talentstudio.analysis.populations.IPopulationEngine#P_SUB_TYPE_}
     * @param reportAccess The access setting for the metrics eg: {@link com.zynap.talentstudio.common.AccessType#PUBLIC_ACCESS}
     * @return Collection of metrics matching the given type
     */
    List<Metric> findAll(Long userId, String type, String reportAccess) throws TalentStudioException;

    /**
     * Finds position or subject metrics that can be run.
     * <br> Includes private metrics created by this user.
     *
     * @param userId
     * @param type Being either {@link com.zynap.talentstudio.analysis.populations.IPopulationEngine#P_POS_TYPE_} or {@link com.zynap.talentstudio.analysis.populations.IPopulationEngine#P_SUB_TYPE_}
     * @return Collection of metrics
     */
    Collection findAvailableMetrics(Long userId, String type);

    boolean metricInPublicReport(Long id);
}

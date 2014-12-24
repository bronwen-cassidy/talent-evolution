/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.analysis.metrics;

import com.zynap.talentstudio.common.IFinder;
import com.zynap.talentstudio.common.IModifiable;

import java.util.Collection;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface IMetricDao extends IFinder, IModifiable {

    List<Metric> findAll(Long userId);

    List<Metric> findAll(Long userId, String type, boolean publicOnly);

    boolean metricInPublicReport(Long id);
}

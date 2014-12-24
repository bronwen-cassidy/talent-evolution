package com.zynap.talentstudio.web.analysis.reports.support;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.web.analysis.reports.views.RunReportWrapperBean;

/**
 * User: amark
 * Date: 01-Mar-2006
 * Time: 11:13:12
 * Base class for report runner delegate components.
 */
public abstract class ReportRunner {

    /**
     * Run the report.
     * @param wrapper
     * @param userId
     * @throws TalentStudioException
     */
    public abstract void run(RunReportWrapperBean wrapper, Long userId) throws TalentStudioException;

    public void setPopulationEngine(IPopulationEngine populationEngine) {
        this.populationEngine = populationEngine;
    }

    protected IPopulationEngine populationEngine;
}

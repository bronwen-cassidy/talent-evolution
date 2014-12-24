/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.security.areas;

import com.zynap.talentstudio.analysis.IAnalysisService;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.security.ISecurityManager;

import java.util.Collection;
import java.util.Set;

/**
 * <p>Sceduled Task to refresh the area element based on populations.</p>
 * This is a scheduled task to run at midnight server time to update any of the area elements that have been built
 * using a population. This method has been chosen to avoid having to update the area elements everytime a subject, position
 * or organisation unit is edited. It does mean though that if someone moves jobs that are included in a security area
 * they will have to wait at most 24 hours before they will be able to access their new settings.
 *
 * @author bcassidy
 * @version 0.1
 * @since 08-Dec-2009 14:52:49
 */
public class AreaElementTask {


    public void run() throws Exception {

        final Collection<Area> areas = securityManager.getPopulationAreas();

        for (Area area : areas) {

            securityManager.removeAreaElements(area);
            Area temp = new Area(area.getId());
            temp.setPositionPopulationId(area.getPositionPopulationId());
            temp.setSubjectPopulationId(area.getSubjectPopulationId());

            Set<AreaElement> areaElements = securityManager.getAreaElements(area.getId());
            temp.setAreaElements(areaElements);
            // area needed for update
            final Set<AreaElement> newAreaElements = AreaUtils.assignPopulationElements(temp, populationEngine, analysisService);
            if (!newAreaElements.isEmpty()) {
                securityManager.createAreaElements(area, newAreaElements);
            }
        }
    }

    public void setAnalysisService(IAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    public void setPopulationEngine(IPopulationEngine populationEngine) {
        this.populationEngine = populationEngine;
    }

    public void setSecurityManager(ISecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    protected IAnalysisService analysisService;
    protected IPopulationEngine populationEngine;
    protected ISecurityManager securityManager;
}

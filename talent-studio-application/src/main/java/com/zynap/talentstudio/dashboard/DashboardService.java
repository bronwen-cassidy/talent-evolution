/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.dashboard;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.IAnalysisService;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.common.DefaultService;
import com.zynap.talentstudio.common.IFinder;
import com.zynap.talentstudio.common.IModifiable;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.security.roles.Role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 17-May-2010 17:36:19
 */
public class DashboardService extends DefaultService implements IDashboardService {

    @Override
    protected IFinder getFinderDao() {
        return dashboardDao;
    }

    @Override
    protected IModifiable getModifierDao() {
        return dashboardDao;
    }

    public void delete(Long dashboardId) throws TalentStudioException {
        dashboardDao.deleteParticipants(dashboardId);
        Dashboard dashboard = findById(dashboardId);
        if (dashboard != null) {
            delete(dashboard);
        }
    }

    public void createOrUpdate(Dashboard dashboard) throws TalentStudioException {
	    saveDashboard(dashboard);
	    Collection results = determineParticipants(ROOT_USER_ID, false, dashboard.getPopulationId(), populationEngine, analysisService);
        dashboardDao.addParticipants(dashboard, results);
    }

	public void createOrUpdate(Dashboard dashboard, Long subjectId) throws TalentStudioException {
		saveDashboard(dashboard);
		List<Subject> results = new ArrayList<>(); 
		results.add(new Subject(subjectId, null));
		dashboardDao.addParticipants(dashboard, results);
	}

	private void saveDashboard(Dashboard dashboard) throws TalentStudioException {
		if (dashboard.getId() == null) {
			dashboardDao.create(dashboard);
		} else {
			dashboardDao.deleteParticipants(dashboard.getId());
			dashboardDao.update(dashboard);
		}
	}

	public List<Dashboard> findSubjectDashboards(Group userGroup, Collection<Role> userRoles, Subject subject) {
        return dashboardDao.findSubjectDashboards(userGroup, userRoles, subject);
    }

    public List<Dashboard> findPersonalDashboards(Subject subject) {
        return dashboardDao.findPersonalDashboards(subject);
    }

    public void setDashboardDao(IDashboardDao dashboardDao) {
        this.dashboardDao = dashboardDao;
    }

    public void setPopulationEngine(IPopulationEngine populationEngine) {
        this.populationEngine = populationEngine;
    }

    public void setAnalysisService(IAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    private IDashboardDao dashboardDao;
    private IPopulationEngine populationEngine;
    private IAnalysisService analysisService;
}

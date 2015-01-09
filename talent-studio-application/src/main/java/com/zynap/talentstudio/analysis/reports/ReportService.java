/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports;

import com.zynap.domain.IDomainObject;
import com.zynap.domain.QueryParameter;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.IAnalysisService;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.reports.jasper.MetricReportDesigner;
import com.zynap.talentstudio.analysis.reports.jasper.TabularReportDesigner;
import com.zynap.talentstudio.arenas.IArenaManagerDao;
import com.zynap.talentstudio.arenas.MenuItem;
import com.zynap.talentstudio.arenas.MenuSection;
import com.zynap.talentstudio.common.AccessType;
import com.zynap.talentstudio.common.DefaultService;
import com.zynap.talentstudio.common.IFinder;
import com.zynap.talentstudio.common.IModifiable;
import com.zynap.talentstudio.common.SecurityConstants;
import com.zynap.talentstudio.security.permits.IPermitManagerDao;
import com.zynap.talentstudio.security.permits.PermitHelper;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class ReportService extends DefaultService implements IReportService {

    public Collection<ReportDto> findAllStandardReports(Long userId) {
        return reportDao.findAll(userId, Report.TABULAR_REPORT);
    }

    public Collection<ReportDto> findAllCrossTabReports(Long userId) {
        return reportDao.findAll(userId, Report.CROSSTAB_REPORT);
    }

    public Collection<ReportDto> findAllMetricReports(Long userId) {
        return reportDao.findAll(userId, Report.METRIC_REPORT);
    }

    public Collection<ReportDto> findAll(String reportType, Long userId) {
        return reportDao.findAll(userId, reportType);
    }

    public Collection<ReportDto> findAllChartReports(String chartType, Long userId) {
        return reportDao.findChartReports(userId, Report.CHART_REPORT, chartType);
    }

    public Collection<ReportDto> findCompatibleReports(final Report report, Long userId) {

        final String populationType = report.getPopulationType();
        final String accessType = report.getAccessType();

        return reportDao.findDrilldownReports(report.getId(), populationType, userId, AccessType.PUBLIC_ACCESS.toString().equals(accessType));
    }

    public Collection<ReportDto> findAllReports(boolean publicOnly, Long userId, String artefactType, String... reportTypes) throws TalentStudioException {
        return reportDao.findAll(publicOnly, artefactType, userId, reportTypes);
    }

    public Report findById(Long reportId, Class reportObjectClass) throws TalentStudioException {
        return (Report) reportDao.findByID(reportObjectClass, reportId);
    }

    public Collection<MenuSection> getMenuSections() {
        return arenaManagerDao.getReportMenuSections();
    }

    public Collection<MenuSection> getHomePageReportMenuSections() {
        return arenaManagerDao.getHomePageReportMenuSections();
    }

    public void updateStateInfo(IDomainObject domainObject) {}

    /**
     * Build jasper report definition for report.
     * <br/> Only applies to tabular and metric reports.
     *
     * @param report
     * @throws TalentStudioException
     */
    public void compileReportDesign(Report report) throws TalentStudioException {
        if (report.isTabularReport() || report.isProgressReport()) {
            report.setJasperDefinition(new TabularReportDesigner().getDesign(report, reportDao));
        } else if (report.isMetricReport())  {
            report.setJasperDefinition(new MetricReportDesigner().getDesign(report));
        }
    }

    public List<Report> findAll(String reportType) {
        return reportDao.findAll(reportType);
    }

    /**
     * Finds all personalReports
     * @param userId
     * @param reportTypes
     * @param isPersonal
     * @return
     */
    public List<ReportDto> findReports(Long userId, String[] reportTypes, boolean isPersonal) {
        Map<String, QueryParameter> parameters = new HashMap<String, QueryParameter>();
        parameters.put("userId", new QueryParameter(userId, QueryParameter.NUMBER));
        parameters.put("reportType", new QueryParameter(reportTypes, QueryParameter.STRINGARRAY));
        if(isPersonal) parameters.put("personal", new QueryParameter("T", QueryParameter.STRING));
        return findReports(parameters);
    }

    public List<ReportDto> findReports(Map<String, QueryParameter> parameters) {
        return reportDao.findAll(parameters);
    }


    public List<Report> findProgressReports(Long subjectId) {
        return reportDao.findProgressReports(subjectId);
    }

    public List<Report> findAppraisalReports(Long subjectId, String status) {
        return reportDao.findAppraisalReports(subjectId, status);
    }

    /**
     * Creates a report.
     * <br/> Also sets the permits on any menu items.
     *
     * @param object
     * @throws com.zynap.exception.TalentStudioException
     */
    public void create(IDomainObject object) throws TalentStudioException {
        Report report = (Report) object;
        checkMenuItemPermits(report);

        // do create first so that ids get set on columns
        reportDao.create(report);

        if (report.isJasperCompileReport()) {
            // then compile the report design and save it again
            compileReportDesign(report);
            reportDao.update(report);
        }
        if(report.isProgressReport()) {
            // populate the table for participants
            addParticipants(report);
        }
    }

    private void addParticipants(Report report) throws TalentStudioException {
        Collection results = determineParticipants(ROOT_USER_ID, false, report.getDefaultPopulation().getId(), populationEngine, analysisService);
        reportDao.addParticipants(report, results);
    }

    /**
     * Finds a given report.
     *
     * @param id
     * @return the specified report.
     */
    public IDomainObject findById(Serializable id) throws TalentStudioException {
        return reportDao.findByID(id);
    }

    /**
     * Update a report.
     * <br/> Also sets the permits on any menu items.
     *
     * @param domainObject
     * @throws com.zynap.exception.TalentStudioException
     */
    public void update(IDomainObject domainObject) throws TalentStudioException {
        Report report = (Report) domainObject;
        checkMenuItemPermits(report);

        // do update first so that ids get set on new columns
        reportDao.update(report);

        if (!report.isAppraisalReport()) {
            // then compile the report design and save it again
            compileReportDesign(report);
            reportDao.update(report);
        }
        if (report.isProgressReport()) {
            reportDao.deleteParticipants(report.getId());
            addParticipants(report);

        }
    }

    public void delete(IDomainObject report) throws TalentStudioException {
        reportDao.delete(report);
    }

    public List findAll() throws TalentStudioException {
        return reportDao.findAll();
    }

    public void disable(IDomainObject domainObject) throws TalentStudioException {
        throw new UnsupportedOperationException("reports cannot be disabled");
    }

    @Override
    protected IFinder getFinderDao() {
        return reportDao;
    }

    @Override
    protected IModifiable getModifierDao() {
        return reportDao;
    }

    public void setReportDao(IReportDao reportDao) {
        this.reportDao = reportDao;
    }

    public void setPermitManagerDao(IPermitManagerDao permitManagerDao) {
        this.permitManagerDao = permitManagerDao;
    }

    public void setArenaManagerDao(IArenaManagerDao arenaManagerDao) {
        this.arenaManagerDao = arenaManagerDao;
    }

    public void setPopulationEngine(IPopulationEngine populationEngine) {
        this.populationEngine = populationEngine;
    }

    public void setAnalysisService(IAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    private void checkMenuItemPermits(Report report) throws TalentStudioException {
        final Set<MenuItem> items = report.getMenuItems();
        if (items != null && !items.isEmpty()) {
            PermitHelper.checkMenuItemPermits(items, permitManagerDao, SecurityConstants.RUN_ACTION, SecurityConstants.REPORTS_CONTENT);
        }
    }

    private IReportDao reportDao;
    private IPermitManagerDao permitManagerDao;
    private IArenaManagerDao arenaManagerDao;
    private IPopulationEngine populationEngine;
    private IAnalysisService analysisService;

}

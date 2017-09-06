/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports;

import net.sf.hibernate.HibernateException;

import com.zynap.domain.QueryParameter;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.arenas.MenuSection;
import com.zynap.talentstudio.common.IZynapService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface IReportService extends IZynapService {

    public Collection<ReportDto> findAllStandardReports(Long userId);

    Collection<ReportDto> findAllCrossTabReports(Long userId);

    Collection<ReportDto> findAllMetricReports(Long userId);

    Collection<ReportDto> findCompatibleReports(Report report, Long userId);

    Collection<ReportDto> findAll(String reportType, Long userId);

    Collection<ReportDto> findAllChartReports(String chartType, Long userId);

    /**
     * Get all menu sections that reports can be published to.
     *
     * @return Collection of {@link com.zynap.talentstudio.arenas.MenuSection} objects
     */
    Collection<MenuSection> getMenuSections();

    Collection<MenuSection> getHomePageReportMenuSections();

    void compileReportDesign(Report report) throws TalentStudioException;

    List<Report> findAll(String reportType);

    List<Report> findAppraisalReports(Long subjectId, String status);

    Collection<ReportDto> findAllReports(boolean publicOnly, Long userId, String artefactType, String... reportTypes) throws TalentStudioException;

    Report findById(Long reportId, Class reportObjectClass) throws TalentStudioException;

    List<ReportDto> findReports(Long userId, String[] reportTypes, boolean isPersonal);

    List<ReportDto> findReports(Map<String, QueryParameter> parameters);

    List<Report> findProgressReports(Long subjectId);

	List<ProgressReport> findProgressReportDefinitions(Long questionnaireDefinitionId) throws HibernateException;
}

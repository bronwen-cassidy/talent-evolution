/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports;

import com.zynap.domain.QueryParameter;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.IFinder;
import com.zynap.talentstudio.common.IModifiable;
import com.zynap.talentstudio.organisation.subjects.Subject;

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
public interface IReportDao extends IFinder, IModifiable {

    Collection<ReportDto> findAll(Long userId, String reportType);
    
    Collection<ReportDto> findChartReports(Long userId, String reportType, String chartType);

    Collection<ReportDto> findDrilldownReports(Long reportId, String populationType, Long userId, boolean publicOnly);

    List<Report> findAll(String reportType);

    List<ReportDto> findAll(Map<String, QueryParameter> parameterMap);

    boolean isAppraisal(Long questionnaireWorkflowId);

    List<Report> findAppraisalReports(Long subjectId, String status);

    Collection<ReportDto> findAll(boolean isPublic, String artefactType, Long userId, String... reportTypes);

    void addParticipants(Report report, Collection<Subject> results) throws TalentStudioException;

    void deleteParticipants(Long reportId) throws TalentStudioException;

    List<Report> findProgressReports(Long subjectId);
}

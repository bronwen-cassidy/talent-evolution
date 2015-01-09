/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports;

import com.zynap.domain.IDomainObject;
import com.zynap.domain.QueryParameter;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.arenas.MenuItem;
import com.zynap.talentstudio.common.AccessType;
import com.zynap.talentstudio.common.HibernateCrudAdaptor;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
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
public class HibernateReportDao extends HibernateCrudAdaptor implements IReportDao {

    public Class getDomainObjectClass() {
        return Report.class;
    }

    public Collection<ReportDto> findAll(Long userId, String reportType) {
        Object[] params = {reportType};
        StringBuffer query = new StringBuffer("select new ");
        query.append(ReportDto.class.getName());

        if (Report.CHART_REPORT.equals(reportType)) {
            query.append("(report.label, report.description, report.defaultPopulation.label, report.reportType, report.accessType, report.id, report.defaultPopulation.id, report.populationType, report.chartType) ");
            query.append("from ChartReport report where report.reportType=? ");
        } else {
            query.append("(report.label, report.description, report.defaultPopulation.label, report.reportType, report.accessType, report.id, report.defaultPopulation.id, report.populationType) ");
            query.append("from Report report where report.reportType=? ");
        }
        if (!IDomainObject.ROOT_USER_ID.equals(userId)) {
            params = new Object[]{reportType, userId, AccessType.PUBLIC_ACCESS.toString()};
            query.append("and (report.userId = ? or report.accessType=?) ");
        }
        query.append("order by upper(report.label)");

        return getHibernateTemplate().find(query.toString(), params);
    }

    public Collection<ReportDto> findChartReports(Long userId, String reportType, String chartType) {
        Object[] params = {reportType, chartType};
        StringBuffer query = new StringBuffer("select new ");
        query.append(ReportDto.class.getName())
                .append("(report.label, report.description, report.defaultPopulation.label, report.reportType, report.accessType, report.id, report.defaultPopulation.id, report.populationType, report.chartType) ")
                .append("from ChartReport report where report.reportType=? and report.chartType=? ");
        if (!IDomainObject.ROOT_USER_ID.equals(userId)) {
            params = new Object[]{reportType, chartType, userId, AccessType.PUBLIC_ACCESS.toString()};
            query.append("and (report.userId = ? or report.accessType=?) ");
        }
        query.append("order by upper(report.label)");

        return getHibernateTemplate().find(query.toString(), params);
    }

    public List<Report> findAppraisalReports(Long subjectId, String status) {
        StringBuffer query = new StringBuffer("from AppraisalSummaryReport report, PerformanceEvaluator pe where pe.performanceReview.id=report.appraisalId and report.reportType='");
        query.append(Report.APPRAISAL_REPORT).append("' and pe.subject.id=").append(subjectId);
        query.append(" and report.status='").append(status).append("'");
        Set<Object[]> results = new HashSet<Object[]>(getHibernateTemplate().find(query.toString()));
        List<Report> reportResults = new ArrayList<Report>();
        for (Object[] result : results) {
            Report report = (Report) result[0];
            if (!reportResults.contains(report)) reportResults.add(report);
        }
        return reportResults;
    }

    public List<Report> findProgressReports(Long subjectId) {
        StringBuffer query = new StringBuffer("from ProgressReport report, ReportParticipant pe where pe.reportId=report.id and report.reportType='");
        query.append(Report.PROGRESS_REPORT).append("' and pe.subjectId=").append(subjectId);

        Set<Object[]> results = new HashSet<Object[]>(getHibernateTemplate().find(query.toString()));
        List<Report> reportResults = new ArrayList<Report>();
        for (Object[] result : results) {
            Report report = (Report) result[0];
            if (!reportResults.contains(report)) reportResults.add(report);
        }
        return reportResults;
    }

    public Collection<ReportDto> findDrilldownReports(Long reportId, String populationType, Long userId, boolean publicOnly) {

        // check report id is not null - will happen with new reports
        final boolean hasReportId = (reportId != null);

        StringBuffer query = new StringBuffer("select new ");
        query.append(ReportDto.class.getName())
                .append("(report.label, report.description, report.defaultPopulation.label, report.reportType, report.accessType, report.id, report.defaultPopulation.id, report.populationType) ")
                .append("from Report report where report.reportType not in ('CHART', 'PROGRESS') and ");
        final Object[] params;

        if (publicOnly) {
            if (hasReportId) {
                query.append("report.accessType=? and report.populationType = ? and report.id != ?");
                params = new Object[]{AccessType.PUBLIC_ACCESS.toString(), populationType, reportId};
            } else {
                query.append("report.accessType=? and report.populationType = ?");
                params = new Object[]{AccessType.PUBLIC_ACCESS.toString(), populationType};
            }
        } else {
            if (hasReportId) {
                query.append("(report.userId = ? or report.accessType=?) and report.populationType = ? and report.id != ?");
                params = new Object[]{userId, AccessType.PUBLIC_ACCESS.toString(), populationType, reportId};
            } else {
                query.append("(report.userId = ? or report.accessType=?) and report.populationType = ?");
                params = new Object[]{userId, AccessType.PUBLIC_ACCESS.toString(), populationType};
            }
        }

        // append order by
        query.append(" order by upper(report.label)");

        return getHibernateTemplate().find(query.toString(), params);
    }

    public Collection<ReportDto> findAll(boolean isPublic, String artefactType, Long userId, String... reportTypes) {
        StringBuffer query = new StringBuffer("select new ");
        query.append(ReportDto.class.getName())
                .append("(report.label, report.description, report.defaultPopulation.label, report.reportType, report.accessType, report.id, report.defaultPopulation.id, report.populationType) ")
                .append("from Report report where report.reportType in (").append(ArrayUtils.arrayToString(reportTypes, ",", "'")).append(")");
        Object[] params;
        if (isPublic) {
            query.append(" and report.accessType=? and report.populationType = ?");
            params = new Object[]{AccessType.PUBLIC_ACCESS.toString(), artefactType};
        } else {
            query.append(" and (report.userId = ? or report.accessType=?) and report.populationType = ?");
            params = new Object[]{userId, AccessType.PUBLIC_ACCESS.toString(), artefactType};
        }

        // append order by
        query.append(" order by upper(report.label)");

        return getHibernateTemplate().find(query.toString(), params);
    }

    public List<Report> findAll(String reportType) {
        return getHibernateTemplate().find("from Report report where report.reportType='" + reportType + "'");
    }

    public List<ReportDto> findAll(Map<String, QueryParameter> parameterMap) {
        StringBuffer query = new StringBuffer("select new ");
        query.append(ReportDto.class.getName())
                .append("(report.label, report.description, report.defaultPopulation.label, report.reportType, report.accessType, report.id, report.defaultPopulation.id, report.populationType) ")
                .append("from Report report where ");
        String prefix = "report.";
        int index = 0;
        for (Map.Entry<String, QueryParameter> entry : parameterMap.entrySet()) {
            query.append(prefix).append(entry.getKey()).append(" ").append(entry.getValue().buildValue());
            if (index < (parameterMap.size() - 1)) query.append(" and ");
            index++;
        }

        return getHibernateTemplate().find(query.toString());
    }

    public boolean isAppraisal(Long questionnaireWorkflowId) {
        final List results = getHibernateTemplate().find("select count(*) from QuestionnaireWorkflow workflow where workflow.id = " + questionnaireWorkflowId + " and workflow.performanceReview.id is not null");
        Integer count = (Integer) (!results.isEmpty() ? results.get(0) : new Integer(0));
        return count.intValue() > 0;
    }

    public void delete(IDomainObject domainObject) throws TalentStudioException {

        final Report report = (Report) domainObject;

        // afm 2005 - DO NOT REMOVE - required to make sure that menu items get reloaded properly by the ArenaMenuHandler        
        final Set menuItems = report.getMenuItems();
        for (Iterator iterator = menuItems.iterator(); iterator.hasNext();) {
            MenuItem menuItem = (MenuItem) iterator.next();
            menuItem.getMenuSection().getMenuItems().remove(menuItem);
        }

        super.delete(domainObject);
    }

    public void addParticipants(Report report, Collection<Subject> subjects) throws TalentStudioException {
        for (Subject subject : subjects) {
            getHibernateTemplate().save(new ReportParticipant(report.getId(), subject.getId()));
        }
    }

    public void deleteParticipants(Long reportId) throws TalentStudioException {
        getHibernateTemplate().delete("from ReportParticipant participant where participant.reportId = " + reportId);
    }
}

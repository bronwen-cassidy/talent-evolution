/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.talentstudio.analysis.BasicAnalysisAttribute;
import com.zynap.talentstudio.analysis.reports.ChartReport;
import com.zynap.talentstudio.analysis.reports.ChartReportAttribute;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.ReportDto;
import com.zynap.talentstudio.arenas.MenuSection;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class ChartReportWrapperBean extends DrilldownReportWrapperBean {

    public ChartReportWrapperBean(Report report, Collection<MenuSection> reportMenuSections, Collection<MenuSection> homePageMenuSections, String runReportURL) {
        super(report, reportMenuSections, homePageMenuSections, runReportURL);
        List<ChartReportAttribute> chartAttributes = ((ChartReport) report).getChartReportAttributes();
        // load the columns
        List<Column> columnList = report.getColumns();
        if(columnList != null) columnList.size();
        if(chartAttributes != null) chartAttributes.size();
        initState(chartAttributes, columnList);
    }

    private void initState(List<ChartReportAttribute> chartAttributes, List<Column> columns) {
        this.reportColumns = columns;
        for (ChartReportAttribute chartAttribute : chartAttributes) {
            Column col = new Column(chartAttribute.getLabel(), chartAttribute.getDynamicAttributeId().toString(), chartAttribute.getPosition(), DynamicAttribute.DA_TYPE_STRUCT, "NA");
            col.setQuestionnaireWorkflowId(chartAttribute.getQuestionnaireWorkflowId());
            this.columns.add(new ColumnWrapperBean(col));
        }
    }

    @Override
    public Report getModifiedReport() {
        // build menu items - for each selected menu section build a new menu item
        assignNewMenuItems(report);

        assignNewGroups(report);
        
        ChartReport rep = (ChartReport) report;
        rep.getChartReportAttributes().clear();
        
        for (ColumnWrapperBean column : columns) {
            BasicAnalysisAttribute analysisAttribute = column.getAnalysisAttribute();
            rep.addChartReportAttribute(new ChartReportAttribute(analysisAttribute.getLabel(), analysisAttribute.getDynamicAttributeId(), analysisAttribute.getQuestionnaireWorkflowId()));
        }
        List<Column> newColumns = new ArrayList<Column>();
        for (Column reportColumn : reportColumns) {
            if(StringUtils.isNotBlank(reportColumn.getValue())) {                         
                newColumns.add(reportColumn);
            }
        }
        rep.assignNewColumns(newColumns);
        return rep;
    }

    public Set<String> getAnswers() {
        return answers;
    }
    
    public void setAnswers(Set<String> newAnswers) {
        //need to remove the difference between new and old
        if(newAnswers.size() < answers.size()) {
            // need to remove
            Collection are = CollectionUtils.subtract(answers,newAnswers);
            answers.removeAll(are);
            for(int i = 0 ; i < are.size(); i++) {
                reportColumns.remove(reportColumns.size() - 1);
            }
        }

        this.answers.addAll(newAnswers);
        int size = this.answers.size() + 1;
        if(size > reportColumns.size()) {
            // make up the difference
            int difference = size - reportColumns.size();
            for(int i = 0; i < difference; i++) {
                reportColumns.add(new Column());
            }
        }
    }

    public void setReports(Collection<ReportDto> reports) {
        this.reports = reports;
    }

    public Collection<ReportDto> getReports() {
        return reports;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public List<Column> getReportColumns() {
        return reportColumns;
    }

    public Long[] collectSelectedAttributes() {
        Set<Long> attributeIds = new HashSet<Long>();
        for (ColumnWrapperBean column : columns) {
            attributeIds.add(column.getDynamicAttributeId());
        }
        return attributeIds.toArray(new Long[attributeIds.size()]);
    }

    public int getNumColumns() {
        return columns == null ? 0 : columns.size();
    }

    public String getChartType() {
        return ((ChartReport) report).getChartType();
    }

    public void setChartType(String value) {
        ((ChartReport) report).setChartType(value);
    }

    private Set<String> answers = new LinkedHashSet<String>();
    private Collection<ReportDto> reports = new ArrayList<ReportDto>();
    /* temporary field used to determine if we are basing this chart report on an existing tabular report */
    private Long reportId;
    private List<Column> reportColumns;
}
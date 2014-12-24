/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.views;

import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.Page;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.GroupingAttribute;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.ReportConstants;
import com.zynap.talentstudio.analysis.reports.ReportUtils;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.web.analysis.AnalysisAttributeWrapperBean;
import com.zynap.talentstudio.web.analysis.reports.ColumnWrapperBean;
import com.zynap.talentstudio.web.analysis.reports.data.FilledReport;
import com.zynap.talentstudio.web.arena.MenuItemHelper;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class RunReportWrapperBean implements Serializable {

    public RunReportWrapperBean(Report reportDefinition) {
        report = reportDefinition;
        reportId = report.getId();
        reportUserId = report.getUserId();
        population = report.getDefaultPopulation();
        report.getGroups().size();

        // load criterias to avoid lazy initialisation problems
        if (population != null && population.getPopulationCriterias() != null) population.getPopulationCriterias().size();
        if (population != null) populationIds = new Long[] {population.getId()};

        initColumns(report);
    }

    public String getLabel() {
        return report.getLabel();
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
        reportId = this.report.getId();
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public Long getPopulationId() {
        return population != null ? population.getId() : null;
    }

    public void setPopulationId(Long populationId) {
        this.population = new Population();
        this.population.setId(populationId);
    }

    public Long[] getPopulationIds() {
        return populationIds;
    }

    public void setPopulationIds(Long[] populationIds) {
        this.populationIds = populationIds;
    }

    public String getPopulationIdsString() {
        return StringUtils.arrayToDelimitedString(populationIds, ",");
    }

    public String getDelimiter() {
        return this.delimiter;
    }

    public void setDelimiter(String delimiter) {
        if (ReportConstants.TAB.equals(delimiter)) {
            this.delimiter = "\t";
        } else {
            this.delimiter = delimiter;
        }
    }

    public Set<Group> getAssignedGroups() {
        return report.getGroups();
    }

    public void setOriginalPopulation(Population originalPopulation) {
        isDrillDown = true;
        this.setPopulation(originalPopulation);
        report.setDefaultPopulation(originalPopulation);

        // set drill-down report to null so you cannot drill down again
        report.setDrillDownReport(null);
    }

    public boolean isDrillDown() {
        return isDrillDown;
    }

    public void setVerticalCriteria(String verticalCriteria) {
        this.verticalCriteria = verticalCriteria;
    }

    public String getVerticalCriteria() {
        return verticalCriteria;
    }

    /**
     * Get vertical drill down label.
     *
     * @return String
     */
    public String getVerticalCriteriaLabel() {
        return verticalCriteriaLabel;
    }

    /**
     * Set vertical drill down label
     *
     * @param verticalCriteriaLabel
     */
    public void setVerticalCriteriaLabel(String verticalCriteriaLabel) {
        this.verticalCriteriaLabel = verticalCriteriaLabel;
    }

    public void setVerticalCriteriaValue(String verticalCriteriaValue) {
        this.verticalCriteriaValue = verticalCriteriaValue;
    }

    public String getVerticalCriteriaValue() {
        return verticalCriteriaValue;
    }

    /**
     * Get horizontal drill down label.
     *
     * @return String
     */
    public String getHorizontalCriteriaLabel() {
        return horizontalCriteriaLabel;
    }

    /**
     * Set horizontal drill down label
     *
     * @param horizontalCriteriaLabel
     */
    public void setHorizontalCriteriaLabel(String horizontalCriteriaLabel) {
        this.horizontalCriteriaLabel = horizontalCriteriaLabel;
    }

    public void setHorizontalCriteriaValue(String horizontalCriteriaValue) {
        this.horizontalCriteriaValue = horizontalCriteriaValue;
    }

    public String getHorizontalCriteriaValue() {
        return horizontalCriteriaValue;
    }

    /**
     * Drilldown horizontal attribute
     *
     * @param horizontalCriteria
     */
    public void setHorizontalCriteria(String horizontalCriteria) {
        this.horizontalCriteria = horizontalCriteria;
    }

    public String getHorizontalCriteria() {
        return horizontalCriteria;
    }

    public void setSelectedColumn(Long columnId) {
        for (int i = 0; i < getColumns().size(); i++) {
            ColumnWrapperBean column = (ColumnWrapperBean) getColumns().get(i);
            if (columnId.equals(column.getId())) {
                selectedColumn = column;
                break;
            }
        }
    }

    public ColumnWrapperBean getSelectedColumn() {
        return selectedColumn;
    }

    public Population getPopulation() {
        return population;
    }

    public void setPopulation(Population population) {
        this.population = population;
        // call to force lazy load of the criterias
        population.getPopulationCriterias().size();
    }

    public List<AnalysisParameter> getQuestionnaireAttributes() {
        return report.getQuestionnaireAttributes();
    }

    public List<AnalysisParameter> getNonAssociatedQuestionnaireAttributes() {
        return ReportUtils.getNonAssociatedQuestionnaireAttributes(report);
    }

    public List getAssignedArenas() {
        return MenuItemHelper.getAssignedArenas(report.getMenuItems());
    }

    public void setDisplayConfigArena(String displayArenaId) {
        this.displayConfigArena = displayArenaId;
    }

    public String getDisplayConfigArena() {
        return displayConfigArena;
    }

    public int getSize() {
        return artefacts.size();
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getActiveTab() {
        return activeTab;
    }

    public void setActiveTab(String activeTab) {
        this.activeTab = activeTab;
    }

    public List getColumns() {
        return columns;
    }

    public boolean isResultsDisplayable() {
        return getFilledReport() != null;
    }

    public Collection getArtefacts() {
        return artefacts;
    }

    public void setArtefacts(Collection artefacts) {
        this.artefacts = artefacts;
    }

    public FilledReport getFilledReport() {
        return filledReport;
    }

    public void setFilledReport(FilledReport filledReport) {
        this.filledReport = filledReport;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String attributeName) {
        orderBy = attributeName;
    }

    public boolean isDrillDownReportSet() {
        final Report drillDownReport = report.getDrillDownReport();
        return drillDownReport != null;
    }

    public Long getDrillDownReportId() {
        final Report drillDownReport = report.getDrillDownReport();
        return drillDownReport != null ? drillDownReport.getId() : null;
    }

    protected void initColumns(Report report) {
        final List temp = report.getColumns();
        // sigh we need this as the ou branch is not yet consistant across all reports and pickers, reason being 1 attribute for most :-(
        boolean orgUnitBranch = report.isTabularReport();
        if (temp != null) {
            for (Iterator iterator = temp.iterator(); iterator.hasNext();) {
                Column column = (Column) iterator.next();
                ColumnWrapperBean columnWrapper = new ColumnWrapperBean(column);
                columnWrapper.setIsOrgunitBranch(orgUnitBranch && AnalysisAttributeHelper.isOrgUnitAttribute(columnWrapper.getAttributeName()));
                this.columns.add(columnWrapper);
                if (column.isGrouped()) {
                    groups.add(columnWrapper);
                }
                if (columnWrapper.isOrderable()) {
                    orderableColumns.add(columnWrapper);
                }
            }
        }
    }

    public List<ColumnWrapperBean> getOrderableColumns() {
        return orderableColumns;
    }

    public List<ColumnWrapperBean> getGroups() {
        return groups;
    }

    public List<GroupingAttribute> getGroupedAttributes() {
        List<GroupingAttribute> groupingAttributes = new ArrayList<GroupingAttribute>();

        for (ColumnWrapperBean columnWrapperBean : groups) {
            final Column column = (Column) columnWrapperBean.getAnalysisAttribute();
            AnalysisParameter attribute = column.getAnalysisParameter();
            String attributeName = AnalysisAttributeHelper.getName(attribute);
            groupingAttributes.add(new GroupingAttribute(attributeName, columnWrapperBean.getSortOrder(), column));
        }

        return groupingAttributes;
    }

    public boolean isHasGroups() {
        return !groups.isEmpty();
    }

    public int getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(int decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public List<AnalysisAttributeWrapperBean> getSettableColumns() {
        return columns;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public int getPageStart() {
        return pageStart;
    }

    public void setPageStart(int pageStart) {
        this.pageStart = pageStart;
    }

    public boolean isCsvExport() {
        return csvExport;
    }

    public void setCvsExport(boolean csvExport) {
        this.csvExport = csvExport;
    }

    public boolean isRecount() {
        return recount;
    }

    public void setRecount(boolean recount) {
        this.recount = recount;
    }

    public void setChartId(String chartId) {
        this.chartId = chartId;
    }

    public String getChartId() {
        return chartId;
    }

    public void setPopulationPersonId(Long populationPersonId) {
        this.populationPersonId = populationPersonId;
    }

    public Long getPopulationPersonId() {
        return populationPersonId;
    }

    public void setLockDown(boolean lockDown) {
        this.lockDown = lockDown;
    }

    public boolean isLockDown() {
        return lockDown;
    }

    public Long getReportUserId() {
        return reportUserId;
    }

    public void setReportUserId(Long reportUserId) {
        this.reportUserId = reportUserId;
    }

    private boolean isDrillDown;
    private String verticalCriteriaLabel;
    private String horizontalCriteriaLabel;
    protected Report report;

    private Collection artefacts;
    protected List<AnalysisAttributeWrapperBean> columns = new ArrayList<AnalysisAttributeWrapperBean>();
    private List<ColumnWrapperBean> groups = new ArrayList<ColumnWrapperBean>();
    private List<ColumnWrapperBean> orderableColumns = new ArrayList<ColumnWrapperBean>();
    private Long[] populationIds = new Long[0];

    protected FilledReport filledReport;

    public static final int DESCENDING = -1;
    public static final int ASCENDING = 1;

    private Long reportId;
    private Population population;
    private String delimiter;

    private ColumnWrapperBean selectedColumn;
    private String displayConfigArena;
    private String activeTab = DETAILS_TAB;

    public static final String DETAILS_TAB = "details";
    public static final String RUN_TAB = "runoptions";
    public static final String RESULTS_TAB = "results";
    private int sortOrder = 1;
    private String orderBy;
    protected int decimalPlaces = 0;
    private String horizontalCriteriaValue;
    private String verticalCriteriaValue;
    private String horizontalCriteria;
    private String verticalCriteria;
    private Page page;
    private int pageStart;
    private boolean csvExport;
    private boolean recount;
    private String chartId;
    private Long populationPersonId;
    private boolean lockDown;
    private Long reportUserId;
}

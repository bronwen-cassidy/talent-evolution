/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.metrics.Metric;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.reports.jasper.JasperReportDesign;
import com.zynap.talentstudio.arenas.MenuItem;
import com.zynap.talentstudio.calculations.Expression;
import com.zynap.talentstudio.common.groups.Group;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Iterator;
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
public abstract class Report extends ZynapDomainObject {

    public Report() {
    }

    public Report(String label) {
        this.label = label;
    }

    public Report(String label, String description, String access) {
        this.label = label;
        this.description = description;
        this.accessType = access;
    }

    public Report(Long id, String label, String description, String access) {
        this(label, description, access);
        this.id = id;
    }

    public Report(Long id) {
        this.id = id;
    }

    public Set<MenuItem> getMenuItems() {
        if (menuItems == null) menuItems = new LinkedHashSet<MenuItem>();
        return menuItems;
    }

    public void setMenuItems(Set<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public boolean hasMenuItems() {
        return menuItems != null && !menuItems.isEmpty();
    }

    /**
     * Clear the list of current columns and add the ones in the collection passed in to the underlying collection.
     *
     * @param newMenuItems Collection of {@link Column} objects
     */
    public void assignNewMenuItems(Set newMenuItems) {
        getMenuItems().clear();

        if (newMenuItems != null) {
            for (Iterator iterator = newMenuItems.iterator(); iterator.hasNext();) {
                MenuItem newMenuItem = (MenuItem) iterator.next();
                addMenuItem(newMenuItem);
            }
        }
    }

    public void assignNewGroups(Set<Group> newGroups) {
        getGroups().clear();
        setGroups(newGroups);
    }

    public void addMenuItem(MenuItem menuItem) {
        menuItem.setReport(this);
        getMenuItems().add(menuItem);
    }

    public Population getDefaultPopulation() {
        return defaultPopulation;
    }

    public void setDefaultPopulation(Population defaultPopulation) {
        this.defaultPopulation = defaultPopulation;
    }

    /**
     * Get all the questionnaire attributes.
     * <br/> Returns empty list if there are no question columns.
     *
     * @return List of {@link com.zynap.talentstudio.analysis.AnalysisParameter} objects.
     */
    public List<AnalysisParameter> getQuestionnaireAttributes() {

        List<AnalysisParameter> attributes = new ArrayList<AnalysisParameter>();

        if (columns != null && !columns.isEmpty()) {
            for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
                Column column = (Column) iterator.next();
                if (column.isFormula()) {
                    List<Expression> expressions = column.getCalculation().getExpressions();
                    for (Expression exp : expressions) {
                        // all questionnaire attributes are dynamic attributes with the workflow id as an extra
                        if (exp.isQuestionnaireAttribute()) {
                            attributes.add(new AnalysisParameter(String.valueOf(exp.getAttribute().getId()), exp.getQuestionnaireWorkflowId(), exp.getRole()));
                        }
                    }

                } else if (column.getAnalysisParameter().isQuestionnaireAttribute()) {
                    attributes.add(column.getAnalysisParameter());
                }
            }
        }

        return attributes;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    /**
     * Clear the list of current columns and add the ones in the collection passed in to the underlying collection.
     *
     * @param newColumns Collection of {@link Column} objects
     */
    public void assignNewColumns(List newColumns) {
        getColumns().clear();

        if (newColumns != null) {
            for (Iterator iterator = newColumns.iterator(); iterator.hasNext();) {
                Column newColumn = (Column) iterator.next();
                addColumn(newColumn);
            }
        }
    }

    public void addColumn(Column column) {
        column.setReport(this);
        column.setPosition(columns.size());
        getColumns().add(column);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setPopulationType(String populationType) {
        this.populationType = populationType;
    }

    public String getPopulationType() {
        return populationType;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public boolean hasMetric() {
        return getDefaultMetric() != null;
    }

    public boolean isMetricReport() {
        return false;
    }

    public boolean isTabularReport() {
        return false;
    }

    public boolean isChartReport() {
        return false;
    }

    public boolean isProgressReport() {
        return false;
    }

    public boolean isCrossTabReport() {
        return false;
    }

    public boolean isAppraisalReport() {
        return false;
    }

    public Metric getDefaultMetric() {
        return defaultMetric;
    }

    public void setDefaultMetric(Metric defaultMetric) {
        this.defaultMetric = defaultMetric;
    }

    public Report getDrillDownReport() {
        return drillDownReport;
    }

    public void setDrillDownReport(Report drillDownReport) {
        this.drillDownReport = drillDownReport;
    }

    public JasperReportDesign getJasperDefinition() {
        return jasperDefinition;
    }

    public void setJasperDefinition(JasperReportDesign jasperDefinition) {
        this.jasperDefinition = jasperDefinition;
    }

    public Report getDisplayReport() {
        return displayReport;
    }

    public void setDisplayReport(Report displayReport) {
        this.displayReport = displayReport;
    }

    public Integer getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(Integer decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public Integer getDisplayOption() {
        return displayOption;
    }

    public void setDisplayOption(Integer displayOption) {
        this.displayOption = displayOption;
    }

    public Set<Group> getGroups() {
        if (groups == null) groups = new LinkedHashSet<Group>();
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("label", getLabel())
                .append("accessType", getAccessType())
                .append("description", getDescription())
                .append("populationType", getPopulationType())
                .append("reportType", getReportType())
                .toString();
    }

    public boolean containsGroup(Group userGroup) {
        return groups != null && groups.contains(userGroup);
    }

    public boolean isHasGroups() {
        return (groups != null && !groups.isEmpty());
    }

    public boolean isPersonal() {
        return personal;
    }

    public void setPersonal(boolean personal) {
        this.personal = personal;
    }

    public static Report create(Long reportId, String type) {
        if (APPRAISAL_REPORT.equals(type)) {
            return new AppraisalSummaryReport(reportId);
        } else if (METRIC_REPORT.equals(type)) {
            return new MetricReport(reportId);
        } else if (CHART_REPORT.equals(type)) {
            return new ChartReport(reportId);
        } else if (CROSSTAB_REPORT.equals(type)) {
            return new CrossTabReport(reportId);
        } else if (TABULAR_REPORT.equals(type)) {
            return new TabularReport(reportId);
        } else {
            return new ProgressReport(reportId);
        }
    }

    /**
     * Is this report to display the last line item only.
     * true indicates only the last line item is to be rendered
     * false indicates that all line item rows are to be rendered (this is the default behaviour)
     *
     * @return true for only last item to be displayed, false for all line items to be displayed
     */
    public boolean isLastLineItem() {
        return lastLineItem;
    }

    public void setLastLineItem(boolean lastLineItem) {
        this.lastLineItem = lastLineItem;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public boolean isJasperCompileReport() {
        return isTabularReport() || isMetricReport() || isProgressReport();
    }

    public static boolean hasValue(String value) {
        return !(ReportConstants.BLANK_VALUE.equals(value) || ReportConstants.NO_VALUE.equals(value));
    }

    public static boolean hasNoValue(String value) {
        return ReportConstants.BLANK_VALUE.equals(value) || ReportConstants.NO_VALUE.equals(value);
    }

    /* used in metric and appraisal summary reports to determine whether the metric is avg, sum or count */
    private String operator;

    private Population defaultPopulation;
    private Metric defaultMetric;

    private List<Column> columns = new ArrayList<Column>();
    private String description;

    /* determines whether this report is private or public */
    private String accessType;
    private Long userId;
    private String populationType;
    private String reportType;

    private Report drillDownReport;

    /* the menu items to which this report has been published to */
    private Set<MenuItem> menuItems;

    /* the groups this report is published for, may be empty, if this report is PRIVATE groups must be empty or null */
    private Set<Group> groups;
    private JasperReportDesign jasperDefinition;
    private Report displayReport;

    /* indicates whether this report can double up as a personal report or not */
    private boolean personal;
    private boolean lastLineItem;

    private Integer decimalPlaces = new Integer(0);
    private Integer displayOption = new Integer(0);

    public static final String CROSSTAB_REPORT = "CROSSTAB";
    public static final String TABULAR_REPORT = "STANDARD";
    public static final String METRIC_REPORT = "METRIC";
    public static final String APPRAISAL_REPORT = "APPRAISAL";
    public static final String CHART_REPORT = "CHART";
    public static final String PROGRESS_REPORT = "PROGRESS";
}

/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.talentstudio.analysis.metrics.Metric;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.arenas.MenuSection;
import com.zynap.talentstudio.web.analysis.reports.functions.FunctionWrapperBean;
import com.zynap.talentstudio.web.analysis.reports.functions.OperandWrapperBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class MetricReportWrapperBean extends DrilldownReportWrapperBean {

    public MetricReportWrapperBean(Report report, Collection<MenuSection> reportMenuSections, Collection<MenuSection> homePageMenuSections, String runReportURL) {
        super(report, reportMenuSections, homePageMenuSections, runReportURL);
        initColumns(report);

        // create new column wrapper for grouping column if none found when looking through columns
        if (groupingColumn == null) {
            groupingColumn = new ColumnWrapperBean(new Column());
        }
    }

    public ColumnWrapperBean getGroupingColumn() {
        return groupingColumn;
    }

    public Report getModifiedReport() {

        // ensures that columns have correct associated dynamic attribute
        checkColumnAttributes();

        List<Column> newColumns = new ArrayList<Column>();

        // if grouped column has been set add to columns - must always be first column
        final boolean groupingColumnSelected = groupingColumn.isAttributeSet();
        if (groupingColumnSelected) {
            final Column newGroupingColumn = groupingColumn.getModifiedColumn();
            newGroupingColumn.setGrouped(true);
            newGroupingColumn.setPosition(new Integer(0));
            newColumns.add(newGroupingColumn);
        }

        // add all other columns after
        int index = groupingColumnSelected ? 1 : 0;
        for (Iterator iterator = columns.iterator(); iterator.hasNext(); index++) {
            ColumnWrapperBean columnWrapperBean = (ColumnWrapperBean) iterator.next();
            final Column newColumn = columnWrapperBean.getModifiedColumn();
            newColumn.setPosition(new Integer(index));
            newColumns.add(newColumn);
        }

        report.assignNewColumns(newColumns);

        // build menu items - for each selected menu section build a new menu item
        assignNewMenuItems(report);
        assignNewGroups(report);

        return report;
    }

    /**
     * Overwritten to only check the grouping column attribute.
     */


    public void checkColumnAttributes() {
        super.checkColumnAttributes();
        super.checkColumnAttribute(groupingColumn);
    }

    /**
     * Checks the column for state validity by finding the correct attribute selected and setting it on the columnWrapper.
     *
     * @param column
     */

    protected void checkColumnAttribute(ColumnWrapperBean column) {
        if (column.isFormula())
            checkFunctionAttributes(column.getFunctionWrapperBean());
    }

    public int getDecimalPlaces() {
        return report.getDecimalPlaces();
    }

    public void setDecimalPlaces(int decimalPlaces) {
        report.setDecimalPlaces(decimalPlaces);
    }

    public boolean isMetricReport() {
        return report.isMetricReport();
    }

    protected void checkFunctionAttributes(FunctionWrapperBean functionWrapperBean) {
        List<OperandWrapperBean> operands = functionWrapperBean.getOperands();
        for (OperandWrapperBean operandWrapperBean : operands) {

            final Long currentMetricId = operandWrapperBean.getMetricId();

            if (currentMetricId == null || currentMetricId.intValue() == -1) {
                operandWrapperBean.setMetric(IPopulationEngine.COUNT_METRIC);
                operandWrapperBean.setMetricId(new Long(-1));
            } else {
                for (Metric metric : metrics) {
                    if (metric.getId().equals(currentMetricId)) {
                        operandWrapperBean.setMetric(metric);                        
                        break;
                    }
                }
            }
        }
    }

    /**
     * Build column wrappers for each column in the report.
     *
     * @param report
     */
    private void initColumns(Report report) {
        final List<Column> cols = report.getColumns();
        for (Column column : cols) {

            if (column.isGrouped()) {
                this.groupingColumn = new ColumnWrapperBean(column);
            } else {
                this.columns.add(new ColumnWrapperBean(column));
            }
        }
    }


    public void addColumn(Column column) {
        column.setReport(report);
        this.columns.add(new ColumnWrapperBean(column));
    }


    public void setMetrics(List<Metric> metrics) {
        this.metrics = metrics;
    }

    public List<Metric> getMetrics() {
        return metrics;
    }

    private ColumnWrapperBean groupingColumn;
    private List<Metric> metrics;
}

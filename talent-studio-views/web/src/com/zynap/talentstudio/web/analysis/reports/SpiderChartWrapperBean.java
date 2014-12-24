/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.talentstudio.analysis.reports.ChartColumnAttribute;
import com.zynap.talentstudio.analysis.reports.ChartReport;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.arenas.MenuSection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class SpiderChartWrapperBean extends ReportWrapperBean {

    public SpiderChartWrapperBean(Report report, Collection<MenuSection> reportMenuSections, Collection<MenuSection> homePageMenuSections, String runReportURL) {
        super(report, reportMenuSections, homePageMenuSections, runReportURL);
        // load the columns
        List<Column> columnList = report.getColumns();
        if(columnList != null) columnList.size();
        initState(columnList);
    }

    private void initState(List<Column> columns) {
        for (Column col : columns) {
            Set<ChartColumnAttribute> columnAttributes = col.getChartColumnAttributes();
            for (ChartColumnAttribute attribute : columnAttributes) {
                this.columnAttributes.add(new ChartColumnAttributeWrapper(attribute));
            }
            this.columns.add(new ColumnWrapperBean(col));
        }
    }

    @Override
    public Report getModifiedReport() {
        // build menu items - for each selected menu section build a new menu item
        assignNewMenuItems(report);

        assignNewGroups(report);

        ChartReport rep = (ChartReport) report;

        List<Column> newColumns = new ArrayList<Column>();
        for (ColumnWrapperBean column : columns) {
            Column col = (Column) column.getAnalysisAttribute();
            String label = col.getLabel();
            Collection<ChartColumnAttribute> attrs = findAttributes(label);
            col.assignNewColumnAttributes(attrs);
            if (!attrs.isEmpty()) {
                newColumns.add(col);
            } 
        }
        rep.assignNewColumns(newColumns);
        return rep;
    }

    private Collection<ChartColumnAttribute> findAttributes(final String label) {
        Collection<ChartColumnAttribute> c = new ArrayList<ChartColumnAttribute>();
        for (ChartColumnAttributeWrapper columnAttribte : columnAttributes) {
            if(label.equals(columnAttribte.getColumnLabel())) {
                ChartColumnAttribute chartColumnAttribute = columnAttribte.getModifiedAttribute();
                chartColumnAttribute.setColumn(null);
                chartColumnAttribute.setId(null);
                c.add(chartColumnAttribute);
            }
        }
        return c;
    }

    public List<ChartColumnAttributeWrapper> getColumnAttributes() {
        return columnAttributes;
    }

    public void setColumnAttributes(List<ChartColumnAttributeWrapper> columnAttribtes) {
        this.columnAttributes = columnAttribtes;
    }

    public void addColumnAttribute() {
        columnAttributes.add(new ChartColumnAttributeWrapper(new ChartColumnAttribute()));
    }

    public Collection<String> getChartGroups() {
        List<String> groupVals = new ArrayList<String>();
        for (ColumnWrapperBean column : columns) {
            groupVals.add(column.getLabel());
        }
        return groupVals;
    }

    @Override
    public void checkColumnAttributes() {
        for (ChartColumnAttributeWrapper columnAttribte : columnAttributes) {
            attributeCollection.setAttributeDefinition(columnAttribte);
        }
    }

    public void removeColumnAttribute(int index) {
        if(columnAttributes.size() >= index) {
            columnAttributes.remove(index);
        }
    }

    public String getChartType() {
        return ((ChartReport)report).getChartType();
    }

    public void setChartType(String value) {
        ((ChartReport)report).setChartType(Report.CHART_REPORT);
    }

    private List<ChartColumnAttributeWrapper> columnAttributes = new ArrayList<ChartColumnAttributeWrapper>();
}
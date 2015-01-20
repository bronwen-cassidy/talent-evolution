/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.arenas.MenuSection;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TabularReportWrapperBean extends ReportWrapperBean {

    public TabularReportWrapperBean(Report report, Collection<MenuSection> reportMenuSections, Collection<MenuSection> homePageMenuSections, String runReportURL) {
        super(report, reportMenuSections, homePageMenuSections, runReportURL);
        initColumns(report);
    }

    public int getNumColumns() {
        return columns == null || columns.isEmpty() ? 0 : columns.size() - 1;
    }

    /**
     * Correctly assigns the correct indexes to the columns in a tabularReport.
     *
     * @return List of ordered columns with the correct indexes applied.
     */
    protected List<Column> assignColumnIndexes() {
        // order first by users choice
        Collections.sort(columns, new Comparator<ColumnWrapperBean>() {
            public int compare(ColumnWrapperBean o1, ColumnWrapperBean o2) {
                return o1.getColumnPosition().compareTo(o2.getColumnPosition());
            }
        });
        List<ColumnWrapperBean> groupedOrderedColumns = orderGroupsFirst(new ArrayList<ColumnWrapperBean>(columns));
        columns = reOrderColumns(0, groupedOrderedColumns);
        return super.assignColumnIndexes();
    }

    /**
     * Reorders the columns so that sub report definitions are collected together.
     *
     * @param level
     * @param elements
     * @return List of reordered columns
     */
    protected List<ColumnWrapperBean> reOrderColumns(int level, List<ColumnWrapperBean> elements) {

        Map<String, List<ColumnWrapperBean>> subReports = new HashMap<String, List<ColumnWrapperBean>>();
        for (Iterator iterator = elements.iterator(); iterator.hasNext();) {
            ColumnWrapperBean columnWrapperBean = (ColumnWrapperBean) iterator.next();
            String[] nameParts = StringUtils.delimitedListToStringArray(columnWrapperBean.getAttributeName(), AnalysisAttributeHelper.DELIMITER);
            if (nameParts.length > level && isSubReport(nameParts[level])) {
                List<ColumnWrapperBean> subReportColums = subReports.get(nameParts[level]);
                if (subReportColums == null) {
                    subReportColums = new ArrayList<ColumnWrapperBean>();
                    subReports.put(nameParts[level], subReportColums);
                } else {
                    iterator.remove();
                }
                subReportColums.add(columnWrapperBean);
            }
        }
        
        for (Map.Entry<String, List<ColumnWrapperBean>> entry : subReports.entrySet()) {
            List<ColumnWrapperBean> list = entry.getValue();
            entry.setValue(reOrderColumns(level + 1, list));
        }

        List<ColumnWrapperBean> results = new ArrayList<ColumnWrapperBean>(elements);
        for (int i = 0; i < elements.size(); i++) {
            ColumnWrapperBean columnWrapperBean = elements.get(i);
            String[] nameParts = StringUtils.delimitedListToStringArray(columnWrapperBean.getAttributeName(), AnalysisAttributeHelper.DELIMITER);
            if (nameParts.length > level && isSubReport(nameParts[level])) {
                List<ColumnWrapperBean> subReportColums = subReports.get(nameParts[level]);
                if (subReportColums != null) {
                    int index = results.indexOf(columnWrapperBean);
                    results.remove(index);
                    results.addAll(index, subReportColums);
                }
            }
        }
        return results;

    }

    /**
     * Orders the columns so that the grouped columns are first, then the first level attributes, then second level and third level etc.
     *
     * @param columns
     */
    private List<ColumnWrapperBean> orderGroupsFirst(List<ColumnWrapperBean> columns) {
        List<ColumnWrapperBean> groupOrderedList = new ArrayList<ColumnWrapperBean>(columns.size());
        int index = 0;

        // grouped always first
        for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
            ColumnWrapperBean columnWrapperBean = (ColumnWrapperBean) iterator.next();
            if (columnWrapperBean.isGrouped()) {
                groupOrderedList.add(index, columnWrapperBean);
                index++;
                iterator.remove();
            }
        }

        // sort the rest of the columns by depth note: coreDetails. is considered depth 0
        //List<ColumnWrapperBean> nestedOrderedList = new ArrayList<ColumnWrapperBean>(columns.size());
        //sortByDepth(columns, nestedOrderedList, 0, 0);
        //groupOrderedList.addAll(nestedOrderedList);
        groupOrderedList.addAll(columns);
        return groupOrderedList;
    }

    private void sortByDepth(List<ColumnWrapperBean> columns, List<ColumnWrapperBean> orderedColumns, int level, int index) {
        // nothing left in the columns we have finished
        if(columns.isEmpty()) return;
        // go through the columns anything at level 0 goes to index
        for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
            ColumnWrapperBean columnWrapperBean = (ColumnWrapperBean) iterator.next();
            // leave formulaes where they are
            final String attribute = columnWrapperBean.getAttribute();
            if(columnWrapperBean.isFormula() || (level == 0 && AnalysisAttributeHelper.isCoreAttribute(attribute)) || ( AnalysisAttributeHelper.splitAttributeNames(attribute).length == level)) {
                orderedColumns.add(index, columnWrapperBean);
                index++;
                iterator.remove();
            }
        }
        sortByDepth(columns, orderedColumns, ++level, index);
    }

    public boolean isMetricReport() {
        return report.isMetricReport();
    }

    private boolean isSubReport(String namePart) {
        return namesToLookFor.contains(namePart);
    }

    /**
     * Build column wrappers for each column in the report.
     *
     * @param report
     */
    private void initColumns(Report report) {
        final List temp = report.getColumns();
        if (temp != null) {
            for (Iterator iterator = temp.iterator(); iterator.hasNext();) {
                Column column = (Column) iterator.next();
                this.columns.add(new ColumnWrapperBean(column));
            }
        }
    }

    private static List<String> namesToLookFor = IPopulationEngine.ASSOCIATION_PROPERTIES_NAMES_LIST;

    static {
        namesToLookFor.addAll(IPopulationEngine.RELATED_ARTEFACT_PROPERTIES_NAMES_LIST);
        namesToLookFor.add(AnalysisAttributeHelper.DOCUMENT_ATTR);
    }

}

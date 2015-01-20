/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.display;

import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.display.DisplayItemReport;
import com.zynap.talentstudio.web.analysis.reports.ColumnWrapperBean;
import com.zynap.talentstudio.web.analysis.reports.ReportWrapperBean;

import java.util.Iterator;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class DisplayConfigReportWrapper extends ReportWrapperBean {

    public DisplayConfigReportWrapper(DisplayItemReport itemReport) {
        super(itemReport.getReport());

        final List temp = report.getColumns();
        if (temp != null) {
            for (Iterator iterator = temp.iterator(); iterator.hasNext();) {
                Column column = (Column) iterator.next();
                this.columns.add(new ColumnWrapperBean(column));
            }
        }

        this.itemReport = itemReport;
    }

    public Report getModifiedReport() {
        checkColumnAttributes();
        // removes all columns that reference a deleted extended attribute
        removeInvalidItems();
        List newColumns = assignColumnIndexes();
        report.assignNewColumns(newColumns);
        return report;
    }    

    public String getReportType() {
        return report.getReportType();
    }

    public DisplayItemReport getModifiedItemReport() {
        Report modifed = getModifiedReport();
        itemReport.setReport(modifed);
        return itemReport;
    }

    public DisplayItemReport getItemReport() {
        return itemReport;
    }

    private DisplayItemReport itemReport;
}

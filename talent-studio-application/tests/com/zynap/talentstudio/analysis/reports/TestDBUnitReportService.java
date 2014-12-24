/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 14-Feb-2011 08:52:33
 * @version 0.1
 */

import com.zynap.talentstudio.ZynapDatabaseTestCase;

import java.util.List;

public class TestDBUnitReportService extends ZynapDatabaseTestCase {

    protected String getDataSetFileName() {
        return "progress-report-test-data.xml";
    }

    protected void setUp() throws Exception {
        super.setUp();
        reportService = (IReportService) getBean("reportService");
    }

    public void testFindById_SpiderChartReport() throws Exception {
        Report report = (Report) reportService.findById(new Long(-800));
        assertTrue(report instanceof ChartReport);
        ChartReport chartReport = (ChartReport) report;
        String chartType = chartReport.getChartType();
        assertEquals(ChartReport.SPIDER_CHART, chartType);

        List<Column> columns = chartReport.getColumns();
        for (Column column : columns) {
            String label = column.getLabel();
            // represents the spider groups
            if(label.equals("High")) {
                assertEquals(3, column.getChartColumnAttributes().size());
            } else if(label.equals("Medium")) {
                assertEquals(2, column.getChartColumnAttributes().size());
            } else {
                assertEquals(2, column.getChartColumnAttributes().size());
            }
        }

    }

    private IReportService reportService;
}
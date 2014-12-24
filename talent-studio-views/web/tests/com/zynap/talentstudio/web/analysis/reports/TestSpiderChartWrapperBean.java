/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 15-Mar-2011 08:43:21
 * @version 0.1
 */
package com.zynap.talentstudio.web.analysis.reports;

import junit.framework.TestCase;

import com.zynap.talentstudio.analysis.reports.ChartColumnAttribute;
import com.zynap.talentstudio.analysis.reports.ChartReport;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.arenas.MenuSection;

import java.util.ArrayList;

/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
public class TestSpiderChartWrapperBean extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSpiderChartWrapperBean() throws Exception {
        // walk through the jsps
        // page 1 = groups, population etc etc
        ChartReport report = new ChartReport();
        report.setChartType(ChartReport.SPIDER_CHART);
        SpiderChartWrapperBean bean = new SpiderChartWrapperBean(report, new ArrayList<MenuSection>(), new ArrayList<MenuSection>(), "runreport.???.htm");
        assertTrue(bean.getColumnAttributes().isEmpty());
        assertTrue(bean.getMenuItemWrappers().isEmpty());
        assertTrue(bean.getColumns().isEmpty());
    }

    public void testSpiderChartWrapperBeanEditMode() throws Exception {
        // walk through the jsps
        // page 1 = groups, population etc etc
        ChartReport report = new ChartReport();
        for(int i = 0; i < 5; i++) {
            Column c1 = new Column(new Long(i), "a" + i);
            for(int j = 0; j < 3; j++) {
                ChartColumnAttribute chartColumnAttribute = new ChartColumnAttribute();
                chartColumnAttribute.setLabel("rr" + i);
                chartColumnAttribute.setId(new Long(12 + i));
                c1.addColumnAttribute(chartColumnAttribute);
            }
            report.addColumn(c1);
        }
        report.setChartType(ChartReport.SPIDER_CHART);

        SpiderChartWrapperBean bean = new SpiderChartWrapperBean(report, new ArrayList<MenuSection>(), new ArrayList<MenuSection>(), "runreport.???.htm");
        assertEquals(5, bean.getColumns().size());
        assertEquals(15, bean.getColumnAttributes().size());
    }
}
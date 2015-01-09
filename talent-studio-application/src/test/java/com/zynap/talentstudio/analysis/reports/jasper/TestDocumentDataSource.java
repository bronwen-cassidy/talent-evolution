/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports.jasper;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 06-Jul-2009 13:52:13
 * @version 0.1
 */

import junit.framework.TestCase;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.MockBeanFactory;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.TabularReport;
import com.zynap.talentstudio.organisation.portfolio.PortfolioItem;
import com.zynap.talentstudio.util.FormatterFactory;
import com.zynap.talentstudio.util.IFormatter;

import java.util.ArrayList;
import java.util.List;


public class TestDocumentDataSource extends TestCase {

    protected void setUp() throws Exception {

        MockBeanFactory factory = new MockBeanFactory();
        final FormatterFactory formatterFactory = new FormatterFactory("EN", "dd MMM yyyy");
        factory.addBean("formatterFactory", formatterFactory);
        formatterFactory.setBeanFactory(factory);
        final IFormatter formatter = FormatterFactory.getDateFormatter();

        Report report = new TabularReport();
        report.addColumn(new Column("col 1", "coreDetail.label", "TEXT"));
        report.addColumn(new Column("col 2", "portfolioItems.lastModifiedBy.label", "TEXT"));
        report.addColumn(new Column("col 3", "portfolioItems.lastModified", "DATE"));

        List<Object> items = new ArrayList<Object>();
        PortfolioItem pi2 = new PortfolioItem();
        pi2.setLastModified(formatter.getDateValue("2001-05-21"));
        pi2.setLastModifiedBy(new User(new Long(22), "testtest", "test2", "test2"));
        items.add(pi2);
        PortfolioItem pi3 = new PortfolioItem();
        pi3.setLastModified(formatter.getDateValue("2003-06-15"));
        pi3.setLastModifiedBy(new User(new Long(22), "testtest", "test3", "test3"));
        items.add(pi3);
        PortfolioItem pi1 = new PortfolioItem();
        pi1.setLastModified(formatter.getDateValue("2006-03-12"));
        pi1.setLastModifiedBy(new User(new Long(22), "testtest", "test1", "test1"));
        items.add(pi1);
        documentDataSource = new DocumentDataSource(report, items, new JasperDataSourceFactory(), new User());
    }

    protected void tearDown() throws Exception {
        documentDataSource = null;
    }

    public void testGetFieldValueNested() throws Exception {
        documentDataSource.next();
        final Object value = documentDataSource.getFieldValue(new MockJRField("lastModifiedBy.label"));
        // this is the answer for the first column
        assertEquals("test2 test2", value.toString());
    }

    public void testGetFieldValue() throws Exception {
        documentDataSource.next();
        final Object value = documentDataSource.getFieldValue(new MockJRField("lastModified"));
        // this is the answer for the first column
        assertEquals("21 May 2001", value.toString());
    }

    private DocumentDataSource documentDataSource;
}
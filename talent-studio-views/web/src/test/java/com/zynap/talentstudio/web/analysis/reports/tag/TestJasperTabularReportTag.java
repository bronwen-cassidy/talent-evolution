/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.tag;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 25-Jan-2006 10:20:40
 * @version 0.1
 */

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.ColumnDisplayImage;
import com.zynap.talentstudio.analysis.reports.IReportDao;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.ReportConstants;
import com.zynap.talentstudio.analysis.reports.TabularReport;
import com.zynap.talentstudio.analysis.reports.jasper.JRCollectionDataSource;
import com.zynap.talentstudio.analysis.reports.jasper.JasperDataSourceFactory;
import com.zynap.talentstudio.analysis.reports.jasper.TabularReportDesign;
import com.zynap.talentstudio.analysis.reports.jasper.TabularReportDesigner;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.organisation.positions.IPositionService;
import com.zynap.talentstudio.web.utils.ZynapMockTagLibTest;

import javax.servlet.jsp.tagext.TagSupport;

import java.util.HashMap;
import java.util.List;

public class TestJasperTabularReportTag extends ZynapMockTagLibTest {

    protected TagSupport getTabLibrary() {
        jasperTabularReportTag = new JasperTabularReportTag();
        return jasperTabularReportTag;
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        jasperTabularReportTag = null;
    }

    public void testDoStartTagInternal() throws Exception {
        IPositionService positionService = (IPositionService) applicationContext.getBean("positionService");
        List positions = positionService.findAll();

        Report report = new TabularReport("test design", "", "Public");
        report.addColumn(new Column(new Long(1), "Label", "label", new Integer(0), DynamicAttribute.DA_TYPE_TEXTFIELD, null));

        JasperDataSourceFactory jasperDataSourceFactory = (JasperDataSourceFactory) getBean("dataSourceFactory");
        JRDataSource dataSource = new JRCollectionDataSource(report, positions, null, jasperDataSourceFactory, null, null, 0, USER);
        JasperPrint jasperPrint = getJasperPrint(report, dataSource);
        jasperTabularReportTag.setJasperPrint(jasperPrint);
        jasperTabularReportTag.setReport(report);
        jasperTabularReportTag.setViewPositionUrl("viewPosition.htm");
        jasperTabularReportTag.setViewPositionUrl("viewSubject.htm");
        jasperTabularReportTag.doInternalStartTag();

        String output = getOutput();
        assertTextPresent(output, "Label");
    }

    public void testColumnHeaderLinksCorrectlyDisplayed() throws Exception {

        IPositionService positionService = (IPositionService) applicationContext.getBean("positionService");
        IDynamicAttributeService attributeService = (IDynamicAttributeService) applicationContext.getBean("dynamicAttrService");
        List positions = positionService.findAll();
        DynamicAttribute da = attributeService.findById(new Long("39"));

        Report report = new TabularReport("test design colors", "", "Public");
        report.addColumn(new Column(new Long(1), "Label", "label", new Integer(0), DynamicAttribute.DA_TYPE_TEXTFIELD, null));
        final Column column = new Column(new Long(2), "Colours", da.getId().toString(), new Integer(1), da.getType(), null);
        ColumnDisplayImage columnDisplayImage = new ColumnDisplayImage((LookupValue) da.getRefersToType().getLookupValues().iterator().next(), "FFFF");
        column.addColumnDisplayImage(columnDisplayImage);
        column.setColorDisplayable(true);
        report.addColumn(column);

        JasperDataSourceFactory jasperDataSourceFactory = (JasperDataSourceFactory) getBean("dataSourceFactory");
        JRDataSource dataSource = new JRCollectionDataSource(report, positions, null, jasperDataSourceFactory, null, null, 0, USER);
        JasperPrint jasperPrint = getJasperPrint(report, dataSource);
        jasperTabularReportTag.setJasperPrint(jasperPrint);
        jasperTabularReportTag.setReport(report);
        jasperTabularReportTag.setViewPositionUrl("viewPosition.htm");
        jasperTabularReportTag.setViewPositionUrl("viewSubject.htm");

        jasperTabularReportTag.doInternalStartTag();
        String output = getOutput();
        assertTextPresent(output, "javascript:");
    }

    private JasperPrint getJasperPrint(Report positionReport, JRDataSource dataSource) throws Exception {
        final TabularReportDesign tabularReportDesign = (TabularReportDesign) new TabularReportDesigner().getDesign(positionReport, (IReportDao) getBean("reportDao"));
        JasperReport jasperReport = tabularReportDesign.getJasperReport();
        HashMap parameters = new HashMap();
        parameters.putAll(tabularReportDesign.getParameters());
        parameters.put(ReportConstants.REPORT_PARAM, positionReport);

        return JasperFillManager.fillReport(jasperReport, parameters, dataSource);
    }

    private JasperTabularReportTag jasperTabularReportTag;

    private static final User USER = new User(new Long(0));
}
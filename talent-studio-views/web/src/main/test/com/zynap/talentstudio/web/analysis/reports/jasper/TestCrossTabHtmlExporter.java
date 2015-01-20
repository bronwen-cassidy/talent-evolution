package com.zynap.talentstudio.web.analysis.reports.jasper;

/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;

import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.organisation.IOrganisationUnitService;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.web.analysis.reports.support.CrossTabReportRunner;
import com.zynap.talentstudio.web.analysis.reports.support.TabularReportRunner;
import com.zynap.talentstudio.web.analysis.reports.views.RunReportWrapperBean;
import com.zynap.talentstudio.web.utils.ZynapDbUnitMockControllerTestCase;

import java.util.Collection;

public class TestCrossTabHtmlExporter extends ZynapDbUnitMockControllerTestCase {

    protected String getDataSetFileName() {
        return "test-data.xml";
    }

    protected void setUp() throws Exception {
        super.setUp();
        reportService = (IReportService) applicationContext.getBean("reportService");
        organisationUnitService = (IOrganisationUnitService) applicationContext.getBean("organisationUnitService");
        dynamicAttributeService = (IDynamicAttributeService) applicationContext.getBean("dynamicAttrService");

        runner = (TabularReportRunner) getBean("tabularReportRunner");
    }

    public void testCrossTabHtmlExporter() throws Exception {

        // get the reports
        Report crosstabReport = (Report) reportService.findById(CROSS_TAB_REPORT_ID);
        Report artefactReport = (Report) reportService.findById(ARTEFACT_REPORT_ID);
        crosstabReport.setDisplayReport(artefactReport);

        // create the tabular report wrapper and fill the results
        RunReportWrapperBean runReportWrapperBean = new RunReportWrapperBean(artefactReport);
        runner.run(runReportWrapperBean, ADMINISTRATOR_USER_ID);

        // get headers
        Collection columnHeaders = CrossTabReportRunner.getColumnHeaders(crosstabReport, dynamicAttributeService, organisationUnitService);
        Collection rowHeaders = CrossTabReportRunner.getRowHeaders(crosstabReport, dynamicAttributeService, organisationUnitService);

        StringBuffer results = new StringBuffer();
        final JasperPrint jasperPrint = runReportWrapperBean.getFilledReport().getJasperPrint();

        // do the export
        String drillDownAltText = "drill down text";
        final JRHtmlExporter exporter = new CrossTabHtmlExporter("viewSubjectUrl", "viewPositionUrl", "drillDownUrl", "viewQuestionnaireUrl", drillDownAltText, crosstabReport, columnHeaders, rowHeaders);
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER, results);
        exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "");
        exporter.exportReport();
        assertTrue(results.length() > 0);
    }

    private IReportService reportService;
    private TabularReportRunner runner;
    private IOrganisationUnitService organisationUnitService;
    private IDynamicAttributeService dynamicAttributeService;

    private static final Long CROSS_TAB_REPORT_ID = new Long(-102);
    private static final Long ARTEFACT_REPORT_ID = new Long(-101);
}
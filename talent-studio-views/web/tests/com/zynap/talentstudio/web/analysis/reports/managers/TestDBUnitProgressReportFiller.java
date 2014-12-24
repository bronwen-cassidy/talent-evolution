/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.managers;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 28-Jun-2010 08:11:44
 * @version 0.1
 */

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;

import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.web.analysis.reports.data.FilledReport;
import com.zynap.talentstudio.web.analysis.reports.jasper.JasperHtmlExporter;
import com.zynap.talentstudio.web.analysis.reports.support.ProgressReportRunner;
import com.zynap.talentstudio.web.analysis.reports.views.RunProgressReportWrapper;
import com.zynap.talentstudio.web.utils.ZynapDbUnitMockControllerTestCase;

import java.io.StringWriter;

public class TestDBUnitProgressReportFiller extends ZynapDbUnitMockControllerTestCase {

    protected String getDataSetFileName() {
        return "progress-test-data.xml";
    }

    public void testFillReport() throws Exception {
        // get the report and compile it
        IReportService reportService = (IReportService) getBean("reportService");
        ISubjectService subjectService = (ISubjectService) getBean("subjectService");
        // this is a progress report no worries
        Report report = (Report) reportService.findById(TEST_REPORT_ID);
        reportService.compileReportDesign(report);

        Subject subject = subjectService.findById(TEST_SUBJECT_ID);

        ProgressReportRunner runner = (ProgressReportRunner) getBean("progressReportRunner");
        RunProgressReportWrapper wrapper = new RunProgressReportWrapper(report, subject);
        runner.run(wrapper, ROOT_USER_ID);

        // we now have a filled compile report
        FilledReport filledReport = wrapper.getFilledReport();
        JasperPrint print = filledReport.getJasperPrint();
        assertNotNull(print);
        // build to output the print??
        StringWriter stringWriter = new StringWriter();

        final JRHtmlExporter exporter;
        exporter = new JasperHtmlExporter("", "", "", "viewSubjectQuestionnaire.htm", report);
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
        exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, stringWriter);
        exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "");
        exporter.exportReport();
        String result = stringWriter.toString();
        assertTrue(result.indexOf("Hello Grand Finale") != -1);
        assertTrue(result.indexOf("Hello 3") != -1);
        assertTrue(result.indexOf("viewSubjectQuestionnaire.htm") != -1);
        System.out.print(result);

    }

    private static final Long TEST_REPORT_ID = new Long(4002);
    private static final Long TEST_SUBJECT_ID = new Long(23679);
}
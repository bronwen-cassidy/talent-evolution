package com.zynap.talentstudio.web.analysis.reports.support;

import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.web.analysis.reports.views.RunReportWrapperBean;
import com.zynap.talentstudio.web.utils.ZynapDbUnitMockControllerTestCase;

import java.io.Serializable;
import java.util.List;

/**
 * User: amark
 * Date: 04-May-2006
 * Time: 11:11:36
 */
public class TestTabularReportRunner extends ZynapDbUnitMockControllerTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        reportService = (IReportService) applicationContext.getBean("reportService");
        tabularReportRunner = (TabularReportRunner) getBean("tabularReportRunner");
    }

    public void testRun() throws Exception {

        final Report report = (Report) reportService.findById(POSITION_TABULAR_REPORT_ID);
        reportService.compileReportDesign(report);        
        final Long userId = ADMINISTRATOR_USER_ID;

        final RunReportWrapperBean runReportWrapperBean = new RunReportWrapperBean(report);
        tabularReportRunner.run(runReportWrapperBean, userId);

        assertNotNull(runReportWrapperBean.getFilledReport());
    }

    public void testRunWithQuestionnaireColumns() throws Exception {

        final Report report = (Report) reportService.findById(TABULAR_REPORT_WITH_QUESTIONNAIRES_ID);
        reportService.compileReportDesign(report);
        final Long userId = ADMINISTRATOR_USER_ID;

        final RunReportWrapperBean runReportWrapperBean = new RunReportWrapperBean(report);
        tabularReportRunner.run(runReportWrapperBean, userId);

        // sanity check that dbunit has loaded columns based on questionnaire attributes correctly
        final List questionnaireAttributes = runReportWrapperBean.getQuestionnaireAttributes();
        assertFalse(questionnaireAttributes.isEmpty());

        assertNotNull(runReportWrapperBean.getFilledReport());
    }

    protected String getDataSetFileName() throws Exception {
        return "test-tabular-report-data.xml";
    }

    private IReportService reportService;
    private TabularReportRunner tabularReportRunner;

    private static final Serializable POSITION_TABULAR_REPORT_ID = new Long(-101);
    private static final Serializable TABULAR_REPORT_WITH_QUESTIONNAIRES_ID = new Long(81);
}

package com.zynap.talentstudio.web.questionnaires;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;

import com.zynap.common.util.StringUtil;
import com.zynap.talentstudio.analysis.IAnalysisService;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.TabularReport;
import com.zynap.talentstudio.questionnaires.AbstractQuestion;
import com.zynap.talentstudio.questionnaires.IQueWorkflowService;
import com.zynap.talentstudio.questionnaires.LineItem;
import com.zynap.talentstudio.questionnaires.MultiQuestionItem;
import com.zynap.talentstudio.questionnaires.QuestionAttribute;
import com.zynap.talentstudio.questionnaires.QuestionGroup;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.web.analysis.reports.jasper.JasperCsvExporter;
import com.zynap.talentstudio.web.analysis.reports.support.ReportRunner;
import com.zynap.talentstudio.web.analysis.reports.views.RunReportWrapperBean;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;

import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;

import javax.servlet.ServletContext;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * User: Bronwen Cassidy
 * Date: 20/01/14
 * Time: 13:51
 * AZ live database properties
 * -Dtest.db.url=jdbc:oracle:thin:@31.222.181.54:1521:orcl
 * -Dtest.db.username=az
 * -Dtest.db.password=az
 * -Dtest.base.url=http://localhost:7001/talent-studio
 * -Dautonomy.host=zynap89
 * -Dautonomy.indexport=9001
 * -Dautonomy.aciport=9002
 * -Dautonomy.position.database=positiondata
 * -Dautonomy.subject.database=subjectdata
 * -Dautonomy.retries=3
 * -Dautonomy.connection.timeout=5000
 * -Dautonomy.searcher=mockAutonomySearcher
 * -Dsender.email=bcassidy@zynap.com
 * -Dtest.webservice.username=webserviceuser
 * -Dtest.webservice.password=webserviceuser
 * -Dsearch.engine=autonomy
 * -Dserver.url=
 * -Dsearch.gateway=autonomyGateway
 * -Dresult.mapper=xmlMapper
 */
public class TestQuestionnaireReportCSVExport extends ZynapMockControllerTest {

    private IAnalysisService analysisService;
    private IReportService reportService;
    private ReportRunner reportRunner;

    protected void setUp() throws Exception {
        super.setUp();
        workflowService = (IQueWorkflowService) getBean("queWorkflowService");
        analysisService = (IAnalysisService) getBean("analysisService");
        reportService = (IReportService) getBean("reportService");
        reportRunner = (ReportRunner) getBean("tabularReportRunner");
    }

    public void testBuildCSVContent() throws Exception {

        Population population = (Population) analysisService.findById(new Long(-2));
        population.setActiveCriteria(Population.ALL_ACTIVE);
        // 102,1,2,62,64,103,81,101,121,122,123,329,328,330
        long[] ids = new long[]{62, 64, 103, 81, 101, 121, 122, 123, 329, 328, 330};
        for (int i = 0; i < ids.length; i++) {
            long id = ids[i];

            QuestionnaireWorkflow workflow = workflowService.findWorkflowById(new Long(id));
            Report report = new TabularReport(workflow.getLabel(), "Report export for all people on " + workflow.getLabel(), "Public");
            List<AbstractQuestion> questions = workflow.getQuestionnaireDefinition().getQuestionnaireDefinitionModel().getQuestions();
            Column cname = new Column("Name", "coreDetail.name", "TEXT");
            report.addColumn(cname);

            for (AbstractQuestion question : questions) {
                if (!question.isNarrativeType()) {
                    if (question.isMultiQuestion()) {
                        MultiQuestionItem q = (MultiQuestionItem) question;
                        List<QuestionAttribute> baseQuestions = q.getQuestions();
                        for (QuestionAttribute baseQuestion : baseQuestions) {
                            report.addColumn(createColumn(baseQuestion, workflow));
                        }
                    } else {
                        report.addColumn(createColumn((QuestionAttribute) question, workflow));
                    }
                }
            }

            //=========================================================================================
            System.out.println("******** compiling report *********");
            reportService.compileReportDesign(report);
            RunReportWrapperBean wrapper = new RunReportWrapperBean(report);

            wrapper.setReport(report);
            wrapper.setCvsExport(true);
            wrapper.setPopulation(population);
            wrapper.setCvsExport(true);

            System.out.println("******** running report **********");
            reportRunner.run(wrapper, ROOT_USER_ID);

            StringBuffer output = new StringBuffer();
            JRCsvExporter exporter = new JasperCsvExporter();
            exporter.setParameter(JRCsvExporterParameter.FIELD_DELIMITER, ",");
            exporter.setParameter(JRCsvExporterParameter.RECORD_DELIMITER, StringUtil.LINE_SEPARATOR_WINDOWS);
            exporter.setParameter(JRCsvExporterParameter.OUTPUT_STRING_BUFFER, output);
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, wrapper.getFilledReport().getJasperPrint());

            System.out.println("******** exporting report **********");
            exporter.exportReport();

            byte[] data = output.toString().getBytes();
            // post process the results
            String path = "exports/REPORTS/";

            String name = String.format("%s.csv", workflow.getLabel());
            File f = new File(path + name);
            FileOutputStream fos = new FileOutputStream(f);
            System.out.println("******** writing report **********");
            fos.write(data);
            fos.flush();
            fos.close();
            // hit the csv exporter
        }

    }

    private Column createColumn(QuestionAttribute question, QuestionnaireWorkflow workflow) {
        QuestionGroup group = question.getQuestionGroup();
        LineItem lineItem = question.getLineItem();
        String label;
        if (group != null) {
            label = group.getLabel();
        } else {
            label = lineItem.getLabel();
        }
        Column col = new Column(label + " " + question.getLabel(), question.getDynamicAttribute().getId(), workflow.getId());
        col.setColumnType(question.getQuestionType());
        return col;
    }

    private IQueWorkflowService workflowService;
}

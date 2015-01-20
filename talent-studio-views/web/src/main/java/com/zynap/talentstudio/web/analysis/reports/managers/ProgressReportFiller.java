package com.zynap.talentstudio.web.analysis.reports.managers;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.QuestionAttributeValuesCollection;
import com.zynap.talentstudio.analysis.reports.ProgressReport;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.ReportConstants;
import com.zynap.talentstudio.analysis.reports.jasper.JasperDataSourceFactory;
import com.zynap.talentstudio.analysis.reports.jasper.TabularReportDesign;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.web.analysis.reports.data.FilledReport;
import com.zynap.talentstudio.web.analysis.reports.data.FilledTabularReport;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: bcassidy
 * Date: 31-Jan-2006
 * Time: 11:49:21
 */
public class ProgressReportFiller {

    public FilledReport fillReport(Report report, Subject subject, Map<Long, QuestionAttributeValuesCollection> questionnaireAnswers) throws TalentStudioException {

        TabularReportDesign tabularReportDesign = (TabularReportDesign) report.getJasperDefinition();

        Map<Object, Object> parameters = new HashMap<Object, Object>();
        parameters.putAll(tabularReportDesign.getParameters());
        parameters.put(ReportConstants._DS_FACTORY_PARAM, dataSourceFactory);
        parameters.put(ReportConstants.REPORT_PARAM, report);
        final User user = UserSessionFactory.getUserSession().getUser();
        parameters.put(ReportConstants._USER_PARAM, user);

        // use -1 so that we have no formatting
        parameters.put(ReportConstants.DECIMAL_PLACES_PARAM, new Integer(-1));

        JRDataSource jrCollectionDataSource = dataSourceFactory.getProgressDataSource(report, ((ProgressReport) report).getWorkflows(), questionnaireAnswers, subject, user);
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(tabularReportDesign.getJasperReport(), parameters, jrCollectionDataSource);
            return new FilledTabularReport(jasperPrint);
        } catch (JRException e) {
            throw new TalentStudioException(e);
        }
    }

    public void setDataSourceFactory(JasperDataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }

    private JasperDataSourceFactory dataSourceFactory;
}
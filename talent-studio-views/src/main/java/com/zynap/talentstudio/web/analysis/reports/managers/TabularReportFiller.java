package com.zynap.talentstudio.web.analysis.reports.managers;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.QuestionAttributeValuesCollection;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.ReportConstants;
import com.zynap.talentstudio.analysis.reports.jasper.JasperDataSourceFactory;
import com.zynap.talentstudio.analysis.reports.jasper.TabularReportDesign;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.web.analysis.reports.data.FilledReport;
import com.zynap.talentstudio.web.analysis.reports.data.FilledTabularReport;
import com.zynap.talentstudio.analysis.reports.GroupingAttribute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 31-Jan-2006
 * Time: 11:49:21
 */
public class TabularReportFiller {

    public FilledReport fillReport(Report report, List nodes, Map<Long, QuestionAttributeValuesCollection> questionnaireAnswers, List<GroupingAttribute> groupedAttributeNames, String orderAttributeName, int orderBy, boolean forCsv) throws TalentStudioException {

        TabularReportDesign tabularReportDesign = (TabularReportDesign) report.getJasperDefinition();

        Map<Object, Object> parameters = new HashMap<Object, Object>();
        parameters.putAll(tabularReportDesign.getParameters());
        parameters.put(ReportConstants._DS_FACTORY_PARAM, dataSourceFactory);
        parameters.put(ReportConstants.REPORT_PARAM, report);
        final User user = UserSessionFactory.getUserSession().getUser();
        parameters.put(ReportConstants._USER_PARAM, user);

        // use -1 so that we have no formatting
        parameters.put(ReportConstants.DECIMAL_PLACES_PARAM, new Integer(-1));

        JRDataSource jrCollectionDataSource;
        if (forCsv) {
            jrCollectionDataSource = dataSourceFactory.getCSVDataSource(report, nodes, questionnaireAnswers, groupedAttributeNames, orderAttributeName, orderBy, user);
        } else {
            jrCollectionDataSource = dataSourceFactory.getDataSource(report, nodes, questionnaireAnswers, groupedAttributeNames, orderAttributeName, orderBy, user);
        }
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

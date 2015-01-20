package com.zynap.talentstudio.web.analysis.reports.managers;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.ReportConstants;
import com.zynap.talentstudio.analysis.reports.jasper.JasperCrossTabDataSource;
import com.zynap.talentstudio.analysis.reports.jasper.JasperDataSourceFactory;
import com.zynap.talentstudio.analysis.reports.jasper.TabularReportDesign;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.web.analysis.reports.data.FilledReport;
import com.zynap.talentstudio.web.analysis.reports.data.FilledTabularReport;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: amark
 * Date: 16-May-2006
 * Time: 10:03:34
 */
public class CrossTabReportFiller {

    public FilledReport fillReport(Report report, List nodes, Map questionnaireAnswers, String[] groupedAttributeNames, Collection rowHeaders, Collection columnHeaders) throws TalentStudioException {

        TabularReportDesign tabularReportDesign = (TabularReportDesign) report.getJasperDefinition();
        final User user = UserSessionFactory.getUserSession().getUser();

        Map<Object, Object> parameters = new HashMap<Object, Object>();
        parameters.putAll(tabularReportDesign.getParameters());
        parameters.put(ReportConstants._DS_FACTORY_PARAM, dataSourceFactory);
        parameters.put(ReportConstants.REPORT_PARAM, report);
        parameters.put(ReportConstants._USER_PARAM, user);
        
        // no formatting
        parameters.put(ReportConstants.DECIMAL_PLACES_PARAM, new Integer(-1));

        // build data source
        JasperCrossTabDataSource dataSource = (JasperCrossTabDataSource) dataSourceFactory.getCrossTabDataSource(report, nodes, questionnaireAnswers, groupedAttributeNames, rowHeaders, columnHeaders, user);

        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(tabularReportDesign.getJasperReport(), parameters, dataSource);
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

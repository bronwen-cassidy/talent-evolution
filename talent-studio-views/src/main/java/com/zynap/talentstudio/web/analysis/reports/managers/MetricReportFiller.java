package com.zynap.talentstudio.web.analysis.reports.managers;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.ReportConstants;
import com.zynap.talentstudio.analysis.reports.jasper.JasperDataSourceFactory;
import com.zynap.talentstudio.analysis.reports.jasper.MetricReportDataSource;
import com.zynap.talentstudio.analysis.reports.jasper.MetricReportDesign;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.web.analysis.reports.data.FilledMetricReport;
import com.zynap.talentstudio.web.analysis.reports.data.FilledReport;

import java.util.HashMap;
import java.util.Map;

/**
 * User: amark
 * Date: 23-Feb-2006
 * Time: 11:00:23
 */
public class MetricReportFiller {

    public MetricReportFiller() {
    }

    public FilledReport fillReport(Map results, Report report, int decimalPlaces) throws TalentStudioException {
        MetricReportDesign metricReportDesign = (MetricReportDesign) report.getJasperDefinition();
        Map parameters = new HashMap();
        parameters.putAll(metricReportDesign.getParameters());
        parameters.put(ReportConstants.REPORT_PARAM, report);
        parameters.put(ReportConstants._USER_PARAM, UserSessionFactory.getUserSession().getUser());
        parameters.put(ReportConstants.DECIMAL_PLACES_PARAM, new Integer(decimalPlaces));

        // build datasource
        JRDataSource jrCollectionDataSource = new MetricReportDataSource(results, report, dataSourceFactory);
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(metricReportDesign.getJasperReport(), parameters, jrCollectionDataSource);
            FilledMetricReport filledMetricReport = new FilledMetricReport(jasperPrint);

            return filledMetricReport;
        } catch (JRException e) {
            throw new TalentStudioException(e);
        }
    }

    public void setDataSourceFactory(JasperDataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }

    private JasperDataSourceFactory dataSourceFactory;
}

package com.zynap.talentstudio.web.analysis.reports.jasper;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 04-Jan-2006
 * Time: 11:11:16
 */

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.IReportDao;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.ReportConstants;
import com.zynap.talentstudio.analysis.reports.TabularReport;
import com.zynap.talentstudio.analysis.reports.jasper.JRCollectionDataSource;
import com.zynap.talentstudio.analysis.reports.jasper.JasperDataSourceFactory;
import com.zynap.talentstudio.analysis.reports.jasper.TabularReportDesign;
import com.zynap.talentstudio.analysis.reports.jasper.TabularReportDesigner;
import com.zynap.talentstudio.analysis.reports.jasper.TestTabularReportDesigner;
import com.zynap.talentstudio.calculations.Calculation;
import com.zynap.talentstudio.calculations.Expression;
import com.zynap.talentstudio.calculations.MixedCalculation;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.organisation.positions.IPositionService;

import java.io.InputStream;
import java.util.HashMap;

public class TestJRCollectionDataSource extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        attributeService = (IDynamicAttributeService) applicationContext.getBean("dynamicAttrService");
        positionService = (IPositionService) getBean("positionService");
        jasperDataSourceFactory = (JasperDataSourceFactory) getBean("dataSourceFactory");
    }

    public void testTabularReportDesign() throws Exception {

        DynamicAttribute da = attributeService.findById(new Long("39"));
        final Report report = TestTabularReportDesigner.createPositionReport(da);
        final TabularReportDesign tabularReportDesign = (TabularReportDesign) new TabularReportDesigner().getDesign(report, (IReportDao) getBean("reportDao"));
        JasperReport jasperReport = tabularReportDesign.getJasperReport();

        HashMap<Object, Object> parameters = createParameterMap(report);
        parameters.putAll(tabularReportDesign.getParameters());

        JRDataSource jrCollectionDataSource = new JRCollectionDataSource(report, positionService.findAll(), null, jasperDataSourceFactory, null, null, 0, USER);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrCollectionDataSource);
        StringBuffer output = executeReport(jasperPrint, report);

        System.out.println(output);
        assertTrue(output.length() > 0);
    }

    public void testTabularReportFormulaDesign() throws Exception {

        Report report = new TabularReport("test design", "", "Public");
        report.addColumn(new Column(new Long(1), "Label", "label", new Integer(0), DynamicAttribute.DA_TYPE_TEXTFIELD, null));
        final Column column = new Column(new Long(4), "DerivedAttr 1", "", new Integer(1), DynamicAttribute.DA_TYPE_NUMBER, null);
        Calculation calc = new MixedCalculation();
        calc.addExpression(new Expression("sourceDerivedAttributes[20]", "+"));
        calc.addExpression(new Expression("sourceDerivedAttributes[19]", null));
        column.setCalculation(calc);
        column.setFormula(true);

        report.addColumn(column);

        final TabularReportDesign tabularReportDesign = (TabularReportDesign) new TabularReportDesigner().getDesign(report, (IReportDao) getBean("reportDao"));
        JasperReport jasperReport = tabularReportDesign.getJasperReport();

        HashMap parameters = createParameterMap(report);
        parameters.putAll(tabularReportDesign.getParameters());

        JRDataSource jrCollectionDataSource = new JRCollectionDataSource(report, positionService.findAll(), null, jasperDataSourceFactory, null, null, 0, USER);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrCollectionDataSource);
        StringBuffer output = executeReport(jasperPrint, report);

        System.out.println(output);
        assertTrue(output.length() > 0);
    }

    public void testTabularCsv() throws Exception {

        DynamicAttribute da = attributeService.findById(new Long("39"));
        final Report report = TestTabularReportDesigner.createPositionReport(da);
        final TabularReportDesign tabularReportDesign = (TabularReportDesign) new TabularReportDesigner().getDesign(report, (IReportDao) getBean("reportDao"));
        JasperReport jasperReport = tabularReportDesign.getJasperReport();

        HashMap parameters = createParameterMap(report);
        parameters.putAll(tabularReportDesign.getParameters());

        JRDataSource jrCollectionDataSource = new JRCollectionDataSource(report, positionService.findAll(), null, jasperDataSourceFactory, null, null, 0, USER);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrCollectionDataSource);

        StringBuffer result = new StringBuffer();

        JRCsvExporter exporter = new JRCsvExporter();
        exporter.setParameter(JRCsvExporterParameter.FIELD_DELIMITER, ",");
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER, result);
        exporter.exportReport();

        assertTrue(result.length() > 0);
    }

    public void testGroupingTestDesign() throws Exception {

        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(path + "grouping.jrxml");
        JasperDesign jasperDesign = JRXmlLoader.load(resourceAsStream);

        DynamicAttribute da = attributeService.findById(new Long("39"));
        final Report report = TestTabularReportDesigner.createPositionReport(da);

        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        HashMap parameters = createParameterMap(report);

        JRDataSource jrCollectionDataSource = new JRCollectionDataSource(report, positionService.findAll(), null, jasperDataSourceFactory, null, null, 0, USER);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrCollectionDataSource);

        assertNotNull(jasperPrint);
    }

    private HashMap<Object, Object> createParameterMap(final Report report) {
        HashMap<Object, Object> parameters = new HashMap<Object, Object>();
        parameters.put(ReportConstants._DS_FACTORY_PARAM, jasperDataSourceFactory);
        parameters.put(ReportConstants._USER_PARAM, USER);
        parameters.put(ReportConstants.REPORT_PARAM, report);
        return parameters;
    }

    private StringBuffer executeReport(JasperPrint jasperPrint, Report report) throws JRException {
        StringBuffer output = new StringBuffer();

        JRHtmlExporter exporter = new JasperHtmlExporter("subjectUrl", "posUrl", "drilldownUrl", "viewQuestionnaireUrl", report);

        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER, output);
        exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "");

        exporter.exportReport();
        return output;
    }

    private String path = "com/zynap/talentstudio/web/analysis/reports/jasper/";

    private IDynamicAttributeService attributeService;
    private IPositionService positionService;
    private JasperDataSourceFactory jasperDataSourceFactory;

    private static final User USER = new User(new Long(0));
}

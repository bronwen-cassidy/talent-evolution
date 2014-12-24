package com.zynap.talentstudio.analysis.reports.jasper;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 27-Jan-2006
 * Time: 16:57:17
 */

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.ColumnDisplayImage;
import com.zynap.talentstudio.analysis.reports.IReportDao;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.ReportConstants;
import com.zynap.talentstudio.analysis.reports.TabularReport;
import com.zynap.talentstudio.calculations.Calculation;
import com.zynap.talentstudio.calculations.Expression;
import com.zynap.talentstudio.calculations.MixedCalculation;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.organisation.positions.IPositionService;
import com.zynap.talentstudio.security.users.IUserService;

import java.util.HashMap;

public class TestTabularReportDesigner extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        attributeService = (IDynamicAttributeService) applicationContext.getBean("dynamicAttrService");
        positionService = (IPositionService) getBean("positionService");
        userService = (IUserService) getBean("userService");
        jasperDataSourceFactory = (JasperDataSourceFactory) getBean("dataSourceFactory");
        tabularReportDesigner = new TabularReportDesigner();
    }

    public void testGetDesign() throws Exception {

        final DynamicAttribute dynamicAttribute = attributeService.findById(new Long("39"));
        final Report report = createPositionReport(dynamicAttribute);
        compileAndRunReport(report);
    }

    public void testAddFormula() throws Exception {
        JasperReportDesign design = new TabularReportDesign(null, null);
        design.jasperDesign = new JasperDesign();
        design.detail = new JRDesignBand();

        Column column = new Column("Test Formula", " 17392 + 17393 ", "formula");
        Calculation calc = new MixedCalculation();
        calc.addExpression(new Expression(new DynamicAttribute(new Long(17392)), "+"));
        calc.addExpression(new Expression(new DynamicAttribute(new Long(17393)), null));
        column.setCalculation(calc);
        column.setReport(new TabularReport());
        TabularReportDesigner designer = new TabularReportDesigner();
        designer.addFormula(column, design, new JRBaseStyle(), 0, 0);
    }

    public void testAddFormula2() throws Exception {
        JasperReportDesign design = new TabularReportDesign(null, null);
        design.jasperDesign = new JasperDesign();
        design.detail = new JRDesignBand();

        Column column = new Column("Test Formula", " 34029_1082 / 34048_1082 + 22345 ", "formula");

        Calculation calc = new MixedCalculation();
        final Expression expression1 = new Expression(new DynamicAttribute(new Long(34029)), "/");
        expression1.setQuestionnaireWorkflowId(new Long(1082));
        calc.addExpression(expression1);
        final Expression expression2 = new Expression(new DynamicAttribute(new Long(34048)), "+");
        expression2.setQuestionnaireWorkflowId(new Long(1082));
        calc.addExpression(expression2);
        calc.addExpression(new Expression(new DynamicAttribute(new Long(22345)), null));
        column.setCalculation(calc);

        column.setReport(new TabularReport());
        TabularReportDesigner designer = new TabularReportDesigner();
        designer.addFormula(column, design, new JRBaseStyle(), 0, 0);

        final JRDesignBand designBand = design.detail;
        JRDesignTextField child = (JRDesignTextField) designBand.getChildren().get(0);
        final JRExpressionChunk[] expressionChunks = child.getExpression().getChunks();
        String actual = expressionChunks[1].getText();
        assertEquals("34029_1082", actual);
        actual = expressionChunks[3].getText();
        assertEquals("34048_1082", actual);
        actual = expressionChunks[5].getText();
        assertEquals("22345", actual);

    }

    public void testAddFormulaRefValue() throws Exception {
        JasperReportDesign design = new TabularReportDesign(null, null);
        design.jasperDesign = new JasperDesign();
        design.detail = new JRDesignBand();

        final Column column = new Column("formula1", " sourceDerivedAttributes[399] + sourceDerivedAttributes[400] ", null);
        column.setFormula(true);
        Calculation calc = new MixedCalculation();
        calc.addExpression(new Expression("sourceDerivedAttributes[399]", "+"));
        calc.addExpression(new Expression("sourceDerivedAttributes[400]", null));
        column.setCalculation(calc);

        column.setReport(new TabularReport());
        TabularReportDesigner designer = new TabularReportDesigner();
        designer.addFormula(column, design, new JRBaseStyle(), 0, 0);

        final JRDesignBand designBand = design.detail;
        JRDesignTextField child = (JRDesignTextField) designBand.getChildren().get(0);
        final JRExpressionChunk[] expressionChunks = child.getExpression().getChunks();
        String actual = expressionChunks[1].getText();
        assertEquals("sourceDerivedAttributes[399]", actual);
        assertEquals("sourceDerivedAttributes[400]", expressionChunks[3].getText());
    }

    public void testAddFormulaMixed() throws Exception {
        JasperReportDesign design = new TabularReportDesign(null, null);
        design.jasperDesign = new JasperDesign();
        design.detail = new JRDesignBand();

        final Column column = new Column("formula2", " ( 1275_21 + ( sourceDerivedAttributes[404] + 1276_21 ) + sourceDerivedAttributes[406] ) ", null);
        column.setFormula(true);

        Calculation calc = new MixedCalculation();
        Expression expression = new Expression(new DynamicAttribute(new Long(1275)), "+");
        expression.setQuestionnaireWorkflowId(new Long(21));
        expression.setLeftBracket("(");
        calc.addExpression(expression);

        Expression expression1 = new Expression("sourceDerivedAttributes[404]", "+");
        expression1.setLeftBracket("(");
        calc.addExpression(expression1);

        Expression expression2 = new Expression(new DynamicAttribute(new Long(1276)), "+");
        expression2.setQuestionnaireWorkflowId(new Long(21));
        expression2.setRightBracket(")");
        calc.addExpression(expression2);

        Expression expression3 = new Expression("sourceDerivedAttributes[406]", null);
        expression3.setRightBracket(")");
        calc.addExpression(expression3);

        column.setCalculation(calc);

        column.setReport(new TabularReport());
        TabularReportDesigner designer = new TabularReportDesigner();
        designer.addFormula(column, design, new JRBaseStyle(), 0, 0);

        final JRDesignBand designBand = design.detail;
        JRDesignTextField child = (JRDesignTextField) designBand.getChildren().get(0);
        final JRExpressionChunk[] expressionChunks = child.getExpression().getChunks();
        assertEquals("1275_21", expressionChunks[1].getText());
        assertEquals("sourceDerivedAttributes[404]", expressionChunks[3].getText());
        assertEquals("sourceDerivedAttributes[406]", expressionChunks[7].getText());
    }

    public void testGetDesignHasDocuments() throws Exception {
        final Report report = new TabularReport("test documents", "description", "Public");
        Column column1 = new Column("Name", "coreDetail.name", "TEXT");
        Column column2 = new Column("Label", "portfolioItems.label", "TEXT");
        Column column3 = new Column("Last Updated By", "portfolioItems.lastModifiedBy.label", "TEXT");
        report.addColumn(column1);
        report.addColumn(column2);
        report.addColumn(column3);
        compileAndRunReport(report);

    }

    public void testDesignWithFormula() throws Exception {

        final Report report = new TabularReport("test design", "desc", "Public");

        final Column formula1Column = new Column("formula1", " sourceDerivedAttributes[399] + sourceDerivedAttributes[400] ", null);
        formula1Column.setFormula(true);
        Calculation calc = new MixedCalculation();
        calc.addExpression(new Expression("sourceDerivedAttributes[399]", "+"));
        calc.addExpression(new Expression("sourceDerivedAttributes[400]", null));
        formula1Column.setCalculation(calc);
        report.addColumn(formula1Column);

        final Column formula2Column = new Column("formula2", " ( 1275_21 + ( sourceDerivedAttributes[404] + 1276_21 ) + sourceDerivedAttributes[406] ) ", null);
        formula2Column.setFormula(true);

        calc = new MixedCalculation();
        Expression expression = new Expression(new DynamicAttribute(new Long(1275)), "+");
        expression.setQuestionnaireWorkflowId(new Long(21));
        expression.setLeftBracket("(");
        calc.addExpression(expression);

        Expression expression1 = new Expression("sourceDerivedAttributes[400]", "+");
        expression1.setLeftBracket("(");
        calc.addExpression(expression1);

        Expression expression2 = new Expression(new DynamicAttribute(new Long(1276)), "+");
        expression2.setQuestionnaireWorkflowId(new Long(21));
        expression2.setRightBracket(")");
        calc.addExpression(expression2);

        Expression expression3 = new Expression("sourceDerivedAttributes[406]", null);
        expression3.setRightBracket(")");
        calc.addExpression(expression3);

        formula2Column.setCalculation(calc);
        report.addColumn(formula1Column);

        report.addColumn(formula2Column);

        final Column formula3Column = new Column("formula2", " ( 1275_21_12 + ( sourceDerivedAttributes[404] + 1276_21_13 ) + sourceDerivedAttributes[406] ) ", null);
        formula3Column.setFormula(true);
        calc = new MixedCalculation();

        Expression exp = new Expression(new DynamicAttribute(new Long(1275)), "+");
        exp.setQuestionnaireWorkflowId(new Long(21));
        exp.setRole("12");
        exp.setLeftBracket("(");
        calc.addExpression(exp);

        exp = new Expression("sourceDerivedAttributes[404]", "+");
        exp.setLeftBracket("(");
        calc.addExpression(exp);

        exp = new Expression(new DynamicAttribute(new Long(1276)), "+");
        exp.setQuestionnaireWorkflowId(new Long(21));
        exp.setRole("13");
        exp.setRightBracket(")");
        calc.addExpression(exp);

        exp = new Expression("sourceDerivedAttributes[406]", null);
        exp.setRightBracket(")");
        calc.addExpression(exp);
        formula3Column.setCalculation(calc);
        report.addColumn(formula3Column);

        compileAndRunReport(report);
    }

    public static Report createPositionReport(DynamicAttribute dynamicAttribute) {

        Report report = new TabularReport("test design", "desc", "Public");

        Column groupedColumn = new Column(new Long(4), "OrgUnit", "organisationUnit.label", new Integer(0), DynamicAttribute.DA_TYPE_OU, null);
        groupedColumn.setGrouped(true);
        report.addColumn(groupedColumn);

        report.addColumn(new Column(new Long(1), "Label", "label", new Integer(1), DynamicAttribute.DA_TYPE_TEXTFIELD, null));
        report.addColumn(new Column(new Long(7), "ChildChild label", "children.children.label", new Integer(2), DynamicAttribute.DA_TYPE_TEXTFIELD, null));
        report.addColumn(new Column(new Long(8), "Source ass", "sourceAssociations.target.label", new Integer(3), DynamicAttribute.DA_TYPE_TEXTFIELD, null));
        report.addColumn(new Column(new Long(9), "Source ass", "sourceAssociations.qualifier.label", new Integer(4), DynamicAttribute.DA_TYPE_TEXTFIELD, null));

        final Column column = new Column(new Long(10), "Colours", dynamicAttribute.getId().toString(), new Integer(5), dynamicAttribute.getType(), null);
        ColumnDisplayImage columnDisplayImage = new ColumnDisplayImage(dynamicAttribute.getRefersToType().getLookupValues().iterator().next(), "FFFF");
        column.addColumnDisplayImage(columnDisplayImage);
        column.setColorDisplayable(true);
        report.addColumn(column);

        return report;
    }

    private void compileAndRunReport(final Report report) throws Exception {

        final TabularReportDesign tabularReportDesign = (TabularReportDesign) tabularReportDesigner.getDesign(report, (IReportDao) getBean("reportDao"));
        JasperReport jasperReport = tabularReportDesign.getJasperReport();

        final User user = getAdminUser(userService);

        HashMap<Object, Object> parameters = new HashMap<Object, Object>();
        parameters.put(ReportConstants._DS_FACTORY_PARAM, jasperDataSourceFactory);
        parameters.putAll(tabularReportDesign.getParameters());
        parameters.put(ReportConstants.REPORT_PARAM, report);
        parameters.put(ReportConstants._USER_PARAM, user);

        JRDataSource jrCollectionDataSource = new JRCollectionDataSource(report, positionService.findAll(), null, jasperDataSourceFactory, null, null, 0, user);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrCollectionDataSource);
        assertNotNull(jasperPrint);
    }

    private IDynamicAttributeService attributeService;
    private IPositionService positionService;
    private JasperDataSourceFactory jasperDataSourceFactory;
    private TabularReportDesigner tabularReportDesigner;
    private IUserService userService;
}
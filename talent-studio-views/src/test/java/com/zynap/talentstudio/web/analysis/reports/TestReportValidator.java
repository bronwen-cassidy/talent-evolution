package com.zynap.talentstudio.web.analysis.reports;

/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */

import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.PopulationDto;
import com.zynap.talentstudio.analysis.reports.ChartReport;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.CrossTabReport;
import com.zynap.talentstudio.analysis.reports.MetricReport;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.ReportDto;
import com.zynap.talentstudio.analysis.reports.TabularReport;
import com.zynap.talentstudio.arenas.Arena;
import com.zynap.talentstudio.arenas.MenuSection;
import com.zynap.talentstudio.arenas.MenuSectionPK;
import com.zynap.talentstudio.calculations.MixedCalculation;
import com.zynap.talentstudio.common.AccessType;
import com.zynap.talentstudio.organisation.IOrganisationUnitService;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.web.analysis.reports.functions.FunctionWrapperBean;
import com.zynap.talentstudio.web.analysis.reports.functions.OperandWrapperBean;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;
import com.zynap.util.spring.BindUtils;
import com.zynap.talentstudio.web.controller.ZynapDefaultFormController;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TestReportValidator extends ZynapMockControllerTest {

    protected void setUp() throws Exception {

        super.setUp();

        reportValidator = (ReportValidator) getBean("reportValidator");

        // create some fake menu sections
        Collection<MenuSection> menuSections = new ArrayList<MenuSection>();
        MenuSection menuSection1 = new MenuSection(new MenuSectionPK("id", "arenaId"), "section 1", 0);
        menuSection1.setArena(new Arena(null, "arena 1"));
        menuSections.add(menuSection1);

        MenuSection menuSection2 = new MenuSection(new MenuSectionPK("id", "arenaId"), "section 2", 1);
        menuSection2.setArena(new Arena(null, "arena 2"));
        menuSections.add(menuSection2);

        reportWrapperBean = new TabularReportWrapperBean(new TabularReport(), menuSections, null, "runreport.htm");
        binder = BindUtils.createBinder(reportWrapperBean, ZynapDefaultFormController.DEFAULT_COMMAND_NAME);
    }

    public void testSupports() throws Exception {
        assertTrue(reportValidator.supports(ReportWrapperBean.class));
    }

    public void testSupportInheritors() throws Exception {
        assertTrue(reportValidator.supports(CrossTabReportWrapperBean.class));
    }

    public void testNotSupports() throws Exception {
        assertFalse(reportValidator.supports(Report.class));
    }

    public void testPublishReportValidation() throws Exception {

        // set access to private and select 1 menu section
        reportWrapperBean.setAccess(AccessType.PRIVATE_ACCESS.toString());
        reportWrapperBean.setActiveMenuItems(new String[]{"0"});

        // should only be 1 menu item
        final Report modifiedReport = reportWrapperBean.getModifiedReport();
        assertEquals(1, modifiedReport.getMenuItems().size());

        Errors errors = binder.getBindingResult();
        reportValidator.validateCoreValues(reportWrapperBean, errors);
        final ObjectError error = errors.getGlobalError();
        assertNotNull(error);
        assertEquals("error.cannot.publish.private.report", error.getCode());
    }

    /**
     * Test validation of the core values validates correctly.
     *
     * @throws Exception
     */
    public void testValidateCoreValuesType() throws Exception {
        reportWrapperBean.setPopulations(createPopulationCollection(new Long[]{new Long(1), new Long(6)}));
        binder.bind(BindUtils.createPropertyValues(new String[]{POPULATION_FIELD, CORE_LABEL_FIELD}, new Object[]{null, "my test label"}));
        reportValidator.validateCoreValues(reportWrapperBean, binder.getBindingResult());
        try {
            binder.close();
            fail("Should have thrown BindException");
        } catch (BindException ex) {
            FieldError labelError = ex.getFieldError(POPULATION_FIELD);
            assertEquals(null, labelError.getRejectedValue());
            assertEquals("error.population.required", labelError.getCode());
        }
    }

    public void testValidateCoreValuesLabel() throws Exception {
        reportWrapperBean.setPopulations(createPopulationCollection(new Long[]{new Long(1), new Long(6)}));
        final MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.addPropertyValue(POPULATION_FIELD, new Long(1));
        propertyValues.addPropertyValue(CORE_LABEL_FIELD, "");
        propertyValues.addPropertyValue(ACCESS_FIELD, AccessType.PUBLIC_ACCESS.toString());


        binder.bind(propertyValues);
        reportValidator.validateCoreValues(reportWrapperBean, binder.getBindingResult());
        try {
            binder.close();
            fail("Should have thrown BindException");
        } catch (BindException ex) {
            FieldError labelError = ex.getFieldError(CORE_LABEL_FIELD);
            assertEquals("error.label.required", labelError.getCode());
            assertEquals(1, ex.getErrorCount());
        }
    }

    public void testValidateCoreValues() throws Exception {

        // supply no values
        reportValidator.validateCoreValues(reportWrapperBean, binder.getBindingResult());
        try {
            binder.close();
            fail("Should have thrown BindException");
        } catch (BindException ex) {
            int errorCount = ex.getErrorCount();
            assertEquals(3, errorCount);
            assertNotNull(ex.getFieldError(POPULATION_FIELD));
            assertNotNull(ex.getFieldError(CORE_LABEL_FIELD));
            assertNotNull(ex.getFieldError(ACCESS_FIELD));
        }
    }

    public void testValidateCoreValuesNone() throws Exception {
        final Long populationId = new Long(6);
        reportWrapperBean.setPopulations(createPopulationCollection(new Long[]{new Long(1), populationId}));

        final MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.addPropertyValue(POPULATION_FIELD, populationId);
        propertyValues.addPropertyValue(CORE_LABEL_FIELD, "Test label");
        propertyValues.addPropertyValue(ACCESS_FIELD, AccessType.PUBLIC_ACCESS.toString());
        binder.bind(propertyValues);

        Errors errors = binder.getBindingResult();
        reportValidator.validateCoreValues(reportWrapperBean, errors);
        assertFalse(errors.hasErrors());
    }

    public void testValidateTabularColumns() throws Exception {
        MutablePropertyValues pvs = new MutablePropertyValues();
        for (int i = 0; i < 5; i++) {
            reportWrapperBean.addColumn(new Column());
            pvs.addPropertyValue("columns[" + i + "].label", "test_" + i);
            pvs.addPropertyValue("columns[" + i + "].attribute", Integer.toString(i));
        }
        binder.bind(pvs);
        reportValidator.validateTabularColumns(reportWrapperBean, binder.getBindingResult());
        try {
            binder.close();
        } catch (BindException ex) {
            fail("Should not have thrown BindException but got " + ex.getAllErrors());
        }
    }

    public void testValidateTabularColumnsNoAttributes() throws Exception {
        MutablePropertyValues pvs = new MutablePropertyValues();
        for (int i = 0; i < 5; i++) {
            reportWrapperBean.addColumn(new Column());
            pvs.addPropertyValue("columns[" + i + "].label", "test_" + i);
        }
        binder.bind(pvs);
        reportValidator.validateTabularColumns(reportWrapperBean, binder.getBindingResult());
        try {
            binder.close();
            fail("expected a bind exception");
        } catch (BindException ex) {
            // ok expected
        }
    }

    public void testValidateMetricColumns() throws Exception {
        Report report = new MetricReport();
        MetricReportWrapperBean wrapperBean = new MetricReportWrapperBean(report, null, null, "");
        wrapperBean.addColumn(new Column("one", "metric 1", DynamicAttribute.DA_TYPE_TEXTFIELD));
        Errors errors = getErrors(wrapperBean);
        reportValidator.validateMetricColumns(wrapperBean, errors);
        assertEquals(0, errors.getErrorCount());
    }

    public void testValidateMetricColumnsNoneSupplied() throws Exception {
        Report report = new MetricReport();
        MetricReportWrapperBean wrapperBean = new MetricReportWrapperBean(report, null, null, "");
        Errors errors = getErrors(wrapperBean);
        reportValidator.validateMetricColumns(wrapperBean, errors);

        assertEquals(1, errors.getErrorCount());
        assertEquals("metrics.required", errors.getGlobalError().getCode());
    }

    public void testValidateMetricColumnsNull() throws Exception {
        Report report = new MetricReport();
        MetricReportWrapperBean wrapperBean = new MetricReportWrapperBean(report, null, null, "");
        wrapperBean.addColumn(new Column("one", "metric 1", DynamicAttribute.DA_TYPE_TEXTFIELD));
        wrapperBean.addColumn(new Column("two", "metric 2", DynamicAttribute.DA_TYPE_TEXTFIELD));

        Errors errors = getErrors(wrapperBean);
        reportValidator.validateMetricColumns(wrapperBean, errors);

        assertEquals(0, errors.getErrorCount());
    }

    public void testValidateMetricColumnsOneNull() throws Exception {
        Report report = new MetricReport();
        MetricReportWrapperBean wrapperBean = new MetricReportWrapperBean(report, null, null, "");
        wrapperBean.addColumn(new Column("one", "metric 1", DynamicAttribute.DA_TYPE_TEXTFIELD));
        wrapperBean.addColumn(new Column("two", "metric 2", DynamicAttribute.DA_TYPE_TEXTFIELD));

        ColumnWrapperBean columnWrapperBean = wrapperBean.getColumns().get(1);
        columnWrapperBean.setMetricId(new Long(22));

        Errors errors = getErrors(wrapperBean);
        reportValidator.validateMetricColumns(wrapperBean, errors);
        assertEquals(0, errors.getErrorCount());
    }

    public void testValidateMetricColumnsNoneNullDifferent() throws Exception {
        Report report = new MetricReport();
        MetricReportWrapperBean wrapperBean = new MetricReportWrapperBean(report, null, null, "");
        wrapperBean.addColumn(new Column("one", "metric 1", DynamicAttribute.DA_TYPE_TEXTFIELD));
        wrapperBean.addColumn(new Column("two", "metric 2", DynamicAttribute.DA_TYPE_TEXTFIELD));

        ColumnWrapperBean columnWrapperBean0 = wrapperBean.getColumns().get(0);
        columnWrapperBean0.setMetricId(new Long(44));
        ColumnWrapperBean columnWrapperBean = wrapperBean.getColumns().get(1);
        columnWrapperBean.setMetricId(new Long(22));

        Errors errors = getErrors(wrapperBean);
        reportValidator.validateMetricColumns(wrapperBean, errors);
        assertEquals(0, errors.getErrorCount());
    }

    public void testValidateMetricColumnsBothSame() throws Exception {
        Report report = new MetricReport();
        MetricReportWrapperBean wrapperBean = new MetricReportWrapperBean(report, null, null, "");
        wrapperBean.addColumn(new Column("one", "metric 1", DynamicAttribute.DA_TYPE_TEXTFIELD));
        wrapperBean.addColumn(new Column("two", "metric 2", DynamicAttribute.DA_TYPE_TEXTFIELD));

        ColumnWrapperBean columnWrapperBean0 = wrapperBean.getColumns().get(0);
        columnWrapperBean0.setMetricId(new Long(22));
        ColumnWrapperBean columnWrapperBean = wrapperBean.getColumns().get(1);
        columnWrapperBean.setMetricId(new Long(22));

        Errors errors = getErrors(wrapperBean);
        reportValidator.validateMetricColumns(wrapperBean, errors);

        assertEquals(1, errors.getErrorCount());
        assertEquals("error.duplicate.metrics", errors.getGlobalError().getCode());
    }

    public void testValidateMetricGroupingColumnLabel() throws Exception {
        Report report = new MetricReport();
        MetricReportWrapperBean wrapperBean = new MetricReportWrapperBean(report, null, null, "");
        wrapperBean.addColumn(new Column("one", "metric 1", DynamicAttribute.DA_TYPE_TEXTFIELD));
        wrapperBean.getGroupingColumn().setAttribute(AnalysisAttributeHelper.ORG_UNIT_ID_ATTR);

        Errors errors = getErrors(wrapperBean);
        reportValidator.validateMetricColumns(wrapperBean, errors);

        assertEquals(1, errors.getErrorCount());
        assertEquals("error.grouping.column.label.required", errors.getFieldError("groupingColumn.label").getCode());
    }

    public void testValidateMetricGroupingColumnNotSet() throws Exception {

        // if grouping attribute not set, validation does not apply
        Report report = new MetricReport();
        MetricReportWrapperBean wrapperBean = new MetricReportWrapperBean(report, null, null, "");
        wrapperBean.addColumn(new Column("one", "metric 1", DynamicAttribute.DA_TYPE_TEXTFIELD));

        Errors errors = getErrors(wrapperBean);
        reportValidator.validateMetricColumns(wrapperBean, errors);

        assertEquals(0, errors.getErrorCount());
    }

    public void testDrillDownAndGrouping() throws Exception {

        // if drill down report set grouping column must be set
        Report report = new MetricReport();
        List<ReportDto> drillDowns = new ArrayList<ReportDto>();
        drillDowns.add(new ReportDto("label", "desc", "all people", "METRIC", "Public", new Long(-1), new Long(1), "S"));
        
        MetricReportWrapperBean wrapperBean = new MetricReportWrapperBean(report, null, null, "");
        wrapperBean.setDrillDownReports(drillDowns);
        wrapperBean.addColumn(new Column("one", "metric 1", DynamicAttribute.DA_TYPE_TEXTFIELD));
        wrapperBean.setDrillDownReportId(new Long(-1));

        Errors errors = getErrors(wrapperBean);
        reportValidator.validateMetricColumns(wrapperBean, errors);
        assertEquals(1, errors.getErrorCount());
        assertEquals("error.grouping.column.required", errors.getFieldError("drillDownReportId").getCode());

        // clear drill down report id - no validation errors expected
        wrapperBean.setDrillDownReportId(null);
        errors = getErrors(wrapperBean);
        reportValidator.validateMetricColumns(wrapperBean, errors);
        assertEquals(0, errors.getErrorCount());

        // set drill down report id and grouping column - no validation errors expected
        wrapperBean.getGroupingColumn().setAttribute(AnalysisAttributeHelper.ORG_UNIT_ID_ATTR);
        wrapperBean.setDrillDownReportId(new Long(-1));
        assertEquals(0, errors.getErrorCount());
    }

    public void testValidate() throws Exception {
        // test that validate method does nothing
        Errors errors = getErrors(reportWrapperBean);
        reportValidator.validate(reportWrapperBean, errors);
        assertFalse(errors.hasErrors());
    }

    public void testValidateFormulaTabular() throws Exception {

        final FunctionWrapperBean functionWrapperBean = new FunctionWrapperBean(new MixedCalculation());
        reportWrapperBean.setSelectedFunction(functionWrapperBean);

        Errors errors = binder.getBindingResult();
        reportValidator.validateFormula(reportWrapperBean, errors);

        assertEquals(3, errors.getErrorCount());

        assertEquals("error.attribute.required", errors.getFieldError("selectedFunction.operands[0].attribute").getCode());
        assertEquals("error.operator.required", errors.getFieldError("selectedFunction.operands[0].operator").getCode());
        assertEquals("error.attribute.required", errors.getFieldError("selectedFunction.operands[1].attribute").getCode());
    }

    public void testValidateFormulaWrongBrackets() throws Exception {

        final FunctionWrapperBean functionWrapperBean = new FunctionWrapperBean(new MixedCalculation());

        List<OperandWrapperBean> operands = functionWrapperBean.getOperands();

        final OperandWrapperBean operandWrapperBean1 = operands.get(0);

        operandWrapperBean1.setRightBracket(IPopulationEngine.RIGHT_BRCKT_);
        operandWrapperBean1.setAttribute("38");
        operandWrapperBean1.setOperator("+");

        final OperandWrapperBean operandWrapperBean2 = operands.get(1);
        operandWrapperBean2.setLeftBracket(IPopulationEngine.LEFT_BRCKT_);
        operandWrapperBean2.setAttribute("32");

        reportWrapperBean.setSelectedFunction(functionWrapperBean);

        Errors errors = binder.getBindingResult();
        reportValidator.validateFormula(reportWrapperBean, errors);

        assertEquals(2, errors.getErrorCount());

        assertEquals("function.brackets.notmatched", errors.getFieldError("selectedFunction.operands[0].leftBracket").getCode());
        assertEquals("function.brackets.notmatched", errors.getFieldError("selectedFunction.operands[1].leftBracket").getCode());
    }

    public void testValidateFormulaMismatchedBrackets() throws Exception {

        MetricReportWrapperBean metricWrapperBean = new MetricReportWrapperBean(new MetricReport(), new ArrayList(), null, "runreport.htm");
        binder = BindUtils.createBinder(metricWrapperBean, ZynapDefaultFormController.DEFAULT_COMMAND_NAME);
        final FunctionWrapperBean functionWrapperBean = new FunctionWrapperBean(new MixedCalculation());

        List<OperandWrapperBean> operands = functionWrapperBean.getOperands();

        final OperandWrapperBean operandWrapperBean1 = operands.get(0);
        operandWrapperBean1.setLeftBracket(IPopulationEngine.LEFT_BRCKT_);
        operandWrapperBean1.setMetricId(new Long(38));
        operandWrapperBean1.setOperator("+");

        final OperandWrapperBean operandWrapperBean2 = operands.get(1);
        operandWrapperBean2.setMetricId(new Long(32));

        metricWrapperBean.setSelectedFunction(functionWrapperBean);

        Errors errors = binder.getBindingResult();
        reportValidator.validateFormula(metricWrapperBean, errors);

        assertEquals(1, errors.getErrorCount());

        assertEquals("function.brackets.notmatched", errors.getFieldError("selectedFunction.operands[0].leftBracket").getCode());
    }

    public void testValidateCrosstabColumns() throws Exception {

        IDynamicAttributeService dynamicAttributeService = (IDynamicAttributeService) getBean("dynamicAttrService");
        IOrganisationUnitService organisationUnitService = (IOrganisationUnitService) getBean("organisationUnitService");

        CrossTabReportWrapperBean crossTabReportWrapperBean = new CrossTabReportWrapperBean(new CrossTabReport(), new ArrayList(), null, "runreport.htm");
        binder = BindUtils.createBinder(crossTabReportWrapperBean, ZynapDefaultFormController.DEFAULT_COMMAND_NAME);
        Errors errors = binder.getBindingResult();

        reportValidator.validateCrosstabColumns(crossTabReportWrapperBean, dynamicAttributeService, organisationUnitService, 400, errors);

        assertEquals(4, errors.getErrorCount());
        assertEquals("vertical.column.required", errors.getFieldError("verticalColumn.attribute").getCode());
        assertEquals("vertical.columnLabel.required", errors.getFieldError("verticalColumn.label").getCode());

        assertEquals("horizontal.column.required", errors.getFieldError("horizontalColumn.attribute").getCode());
        assertEquals("horizontal.columnLabel.required", errors.getFieldError("horizontalColumn.label").getCode());
    }

    public void testValidateCrosstabColumnsDuplicateAttributes() throws Exception {

        IDynamicAttributeService dynamicAttributeService = (IDynamicAttributeService) getBean("dynamicAttrService");
        IOrganisationUnitService organisationUnitService = (IOrganisationUnitService) getBean("organisationUnitService");

        CrossTabReportWrapperBean crossTabReportWrapperBean = new CrossTabReportWrapperBean(new CrossTabReport(), new ArrayList(), null, "runreport.htm");
        binder = BindUtils.createBinder(crossTabReportWrapperBean, ZynapDefaultFormController.DEFAULT_COMMAND_NAME);
        Errors errors = binder.getBindingResult();

        final Column verticalColumn = new Column("vertical");
        verticalColumn.setAttributeName(AnalysisAttributeHelper.FIRST_NAME_ATTR);
        crossTabReportWrapperBean.setVerticalColumn(verticalColumn);

        final Column horizontalColumn = new Column("horizontal");
        horizontalColumn.setAttributeName(AnalysisAttributeHelper.FIRST_NAME_ATTR);
        crossTabReportWrapperBean.setHorizontalColumn(horizontalColumn);

        reportValidator.validateCrosstabColumns(crossTabReportWrapperBean, dynamicAttributeService, organisationUnitService, 400, errors);

        assertEquals(1, errors.getErrorCount());
        assertEquals("crosstab.columns.identical", errors.getFieldError("verticalColumn.attribute").getCode());
    }

    public void testValidateCrosstabColumnsOverflow() throws Exception {

        IDynamicAttributeService dynamicAttributeService = (IDynamicAttributeService) getBean("dynamicAttrService");
        IOrganisationUnitService organisationUnitService = (IOrganisationUnitService) getBean("organisationUnitService");

        CrossTabReportWrapperBean crossTabReportWrapperBean = new CrossTabReportWrapperBean(new CrossTabReport(), new ArrayList(), null, "runreport.htm");
        binder = BindUtils.createBinder(crossTabReportWrapperBean, ZynapDefaultFormController.DEFAULT_COMMAND_NAME);
        Errors errors = binder.getBindingResult();

        // country extended attribute
        final Column verticalColumn = new Column("vertical");
        verticalColumn.setAttributeName("27");
        crossTabReportWrapperBean.setVerticalColumn(verticalColumn);

        // preferred language extended attribute
        final Column horizontalColumn = new Column("horizontal");
        horizontalColumn.setAttributeName("39");
        crossTabReportWrapperBean.setHorizontalColumn(horizontalColumn);

        reportValidator.validateCrosstabColumns(crossTabReportWrapperBean, dynamicAttributeService, organisationUnitService, 10, errors);

        assertEquals(1, errors.getErrorCount());
        assertEquals("crosstab.excesive.values", errors.getFieldError("verticalColumn.attribute").getCode());
    }

    public void testValidateCrosstabDisplayLimit() throws Exception {

        final Report report = new CrossTabReport();
        report.setReportType(Report.CROSSTAB_REPORT);

        CrossTabReportWrapperBean crossTabReportWrapperBean = new CrossTabReportWrapperBean(report, new ArrayList(), null, "runreport.htm");
        binder = BindUtils.createBinder(crossTabReportWrapperBean, ZynapDefaultFormController.DEFAULT_COMMAND_NAME);
        Errors errors = binder.getBindingResult();

        // blank page size is ok
        reportValidator.validateCoreValues(crossTabReportWrapperBean, errors);
        assertNull(errors.getFieldError("displayLimit"));

        // zero page size is not ok
        crossTabReportWrapperBean.setDisplayLimit(new Integer(0));
        reportValidator.validateCoreValues(crossTabReportWrapperBean, errors);
        assertEquals("error.display.limit.required", errors.getFieldError("displayLimit").getCode());

        // negative page size is not ok
        crossTabReportWrapperBean.setDisplayLimit(new Integer(-1));
        reportValidator.validateCoreValues(crossTabReportWrapperBean, errors);
        assertEquals("error.display.limit.required", errors.getFieldError("displayLimit").getCode());

        // page size of 1 is ok
        crossTabReportWrapperBean.setDisplayLimit(new Integer(1));
        reportValidator.validateCoreValues(crossTabReportWrapperBean, errors);
        assertEquals("error.display.limit.required", errors.getFieldError("displayLimit").getCode());
    }

    public void testValidateChartReport() {
        ChartReport report = new ChartReport();
        ChartReportWrapperBean wrapper = new ChartReportWrapperBean(report, new ArrayList<MenuSection>(), new ArrayList<MenuSection>(), "");
        Errors errors = getErrors(wrapper);
        reportValidator.validateCoreValues(wrapper, errors);
        // loads of errors label, access, populationId, chartType missing
        assertEquals(4, errors.getErrorCount());
        
    }

    private Collection<PopulationDto> createPopulationCollection(Long[] populationIds) {
        List<PopulationDto> mockPopulations = new ArrayList<PopulationDto>();
        for (int i = 0; i < populationIds.length; i++) {
            mockPopulations.add(new PopulationDto(populationIds[i], "name", IPopulationEngine.P_POS_TYPE_, AccessType.PUBLIC_ACCESS.toString(), ""));
        }
        return mockPopulations;
    }

    private ReportValidator reportValidator;
    private TabularReportWrapperBean reportWrapperBean;
    private DataBinder binder;

    private static final String POPULATION_FIELD = "populationId";
    private static final String CORE_LABEL_FIELD = "label";
    private static final String ACCESS_FIELD = "access";
}
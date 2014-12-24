/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 13-Apr-2010 16:22:25
 * @version 0.1
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.talentstudio.analysis.reports.ChartReport;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.web.utils.ZynapDbUnitMockControllerTestCase;
import com.zynap.web.beans.ColourEditor;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import java.util.List;

/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
public class DBUnitTestChartReportWizardController extends ZynapDbUnitMockControllerTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        chartReportWizardController = (ChartReportWizardController) getBean("addChartReportController");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        chartReportWizardController = null;
    }

    @Override
    protected String getDataSetFileName() throws Exception {
        return "chart-test-data.xml";
    }


    public void testFormBackingObjectAdd() throws Exception {
        ChartReportWrapperBean bean = newChartReportBean();
        assertEquals(5, bean.getReports().size());
        assertEquals(4, bean.getDrillDownReports().size());
        assertNull(bean.getReportId());
    }

    public void testFormBackingObjectEdit() throws Exception {
        ChartReportWrapperBean bean = existingChartReportBean();
        ChartReport rep = (ChartReport) bean.getReport();
        assertNotNull(rep);
        assertEquals(4, rep.getChartReportAttributes().size());
        assertEquals(2, rep.getColumns().size());

        for (Column column : rep.getColumns()) {
            assertNotNull(column.getDisplayColour());
        }

        List<ColumnWrapperBean> columnWrapperBeans = bean.getColumns();
        assertEquals(4, columnWrapperBeans.size());
        assertEquals(2, bean.getReportColumns().size());
    }


    public void testReferenceDataNewCoreInfo() throws Exception {
        ChartReportWrapperBean bean = newChartReportBean();
        assertEquals(0, bean.getPopulations().size());
        bean.setReportId(new Long(341));
        doPage0(bean);
        // happens after a scope select
        assertFalse(bean.getPopulations().isEmpty());
        // should not have any columns at this point
        assertEquals(0, bean.getColumns().size());

    }

    public void testReferenceDataExistingPage0() throws Exception {
        ChartReportWrapperBean bean = existingChartReportBean();
        assertEquals(0, bean.getPopulations().size());
        doPage0(bean);
        // happens after a scope select
        assertFalse(bean.getPopulations().isEmpty());
        // should have 4 columns these are the attributes used in the pickers
        assertEquals(4, bean.getColumns().size());
        assertEquals(2, bean.getReportColumns().size());
    }

    public void testReferenceDataNewPage1() throws Exception {
        ChartReportWrapperBean bean = newChartReportBean();
        doPage0(bean);
        doPage1(bean);
        // new report should have 3 columns if reportId is null otherwise shoudl have 2 columns
        assertEquals(3, bean.getColumns().size());
        assertTrue(bean.getReportColumns().isEmpty());
        assertTrue(bean.getAttributeCollection() != null);
    }

    public void testReferenceDataNewBasedOnPage1() throws Exception {
        ChartReportWrapperBean bean = newChartReportBean();
        doPage0(bean);
        bean.setReportId(new Long(341));
        doPage1(bean);
        // new report should have 3 columns if reportId is null otherwise shoudl have 2 columns
        assertEquals(2, bean.getColumns().size());
        assertTrue(bean.getReportColumns().isEmpty());
        assertTrue(bean.getAttributeCollection() != null);
    }

    public void testReferenceDataExistingPage1() throws Exception {
        ChartReportWrapperBean bean = existingChartReportBean();
        assertEquals(0, bean.getPopulations().size());
        doPage0(bean);
        doPage1(bean);
        assertEquals(4, bean.getColumns().size());
        assertEquals(2, bean.getReportColumns().size());
        assertTrue(bean.getAttributeCollection() != null);
    }

    public void testReferenceDataNewPage2() throws Exception {
        ChartReportWrapperBean bean = newChartReportBean();
        doPage0(bean);
        doPage1(bean);
        doPage2(bean);
        assertEquals(bean.getAnswers().size(), bean.getReportColumns().size());
    }

    public void testReferenceDataNewBasedOnPage2() throws Exception {
        ChartReportWrapperBean bean = newChartReportBean();
        doPage0(bean);
        bean.setReportId(new Long(341));
        doPage1(bean);
        doPage2(bean);
        assertEquals(bean.getAnswers().size(), bean.getReportColumns().size());
    }

    public void testReferenceDataExistingPage2() throws Exception {
        ChartReportWrapperBean bean = existingChartReportBean();
        assertEquals(0, bean.getPopulations().size());
        doPage0(bean);
        doPage1(bean);
        // add in the selected attributes the columnwrappers
        List<ColumnWrapperBean> columnWrapperBeans = bean.getColumns();
        columnWrapperBeans.clear();
        columnWrapperBeans.add(new ColumnWrapperBean(new Column(new Long(23581), new Long(682))));
        columnWrapperBeans.add(new ColumnWrapperBean(new Column(new Long(23581), new Long(684))));
        columnWrapperBeans.add(new ColumnWrapperBean(new Column(new Long(23594), new Long(682))));
        columnWrapperBeans.add(new ColumnWrapperBean(new Column(new Long(23594), new Long(684))));
        doPage2(bean);
        assertEquals(bean.getAnswers().size(), bean.getReportColumns().size());
        int colsWithValue = 0;
        List<Column> columns = bean.getReportColumns();
        for (Column column : columns) {
            if (StringUtils.hasText(column.getValue())) {
                colsWithValue++;
            }
        }
        assertEquals(2, colsWithValue);
    }

    public void testInitBinder() throws Exception {
        ChartReportWrapperBean bean = existingChartReportBean();

        BeanWrapper bw = new BeanWrapperImpl(bean);
        bw.registerCustomEditor(String.class, "reportColumns.displayColour", new ColourEditor());
        MutablePropertyValues pvs = new MutablePropertyValues();
        pvs.addPropertyValue("reportColumns[0].displayColour", ",#990000:white");
        bw.setPropertyValues(pvs);
        assertEquals("990000", bean.getReportColumns().get(0).getDisplayColour());

    }

    public void testReferenceDataFinished() throws Exception {
        ChartReportWrapperBean bean = newChartReportBean();
        doPage0(bean);
        doPage1(bean);
        List<ColumnWrapperBean> columnWrapperBeans = bean.getColumns();
        columnWrapperBeans.clear();
        columnWrapperBeans.add(new ColumnWrapperBean(new Column(new Long(23581), new Long(682))));
        columnWrapperBeans.add(new ColumnWrapperBean(new Column(new Long(23581), new Long(684))));
        columnWrapperBeans.add(new ColumnWrapperBean(new Column(new Long(23594), new Long(682))));
        columnWrapperBeans.add(new ColumnWrapperBean(new Column(new Long(23594), new Long(684))));
        doPage2(bean);
        // todo add some info into the columns


    }

    private void doPage2(ChartReportWrapperBean bean) throws Exception {
        mockRequest.setParameter("_target2", "_target2");
        mockRequest.removeParameter("_target1");
        Errors errors = getErrors(bean);
        chartReportWizardController.onBindAndValidateInternal(mockRequest, bean, errors, 1);
        chartReportWizardController.referenceData(mockRequest, bean, errors, 1);
    }

    private void doPage1(ChartReportWrapperBean bean) throws Exception {
        mockRequest.setParameter("_target1", "_target1");
        mockRequest.removeParameter("_target0");
        Errors errors = getErrors(bean);
        chartReportWizardController.onBindAndValidateInternal(mockRequest, bean, errors, 0);
        chartReportWizardController.referenceData(mockRequest, bean, errors, 0);
    }

    private void doPage0(ChartReportWrapperBean bean) throws Exception {
        bean.setDrillDownReportId(new Long(341));
        bean.setAccess("Public");
        mockRequest.setParameter("_target0", "_target0");
        Errors errors = getErrors(bean);
        chartReportWizardController.onBindAndValidateInternal(mockRequest, bean, errors, 0);
        chartReportWizardController.referenceData(mockRequest, bean, errors, 0);
    }

    private ChartReportWrapperBean existingChartReportBean() throws Exception {
        setUserSession(ROOT_USER_ID, mockRequest);
        mockRequest.addParameter("id", "352");
        return (ChartReportWrapperBean) chartReportWizardController.formBackingObject(mockRequest);
    }

    private ChartReportWrapperBean newChartReportBean() throws Exception {
        setUserSession(ROOT_USER_ID, mockRequest);
        return (ChartReportWrapperBean) chartReportWizardController.formBackingObject(mockRequest);
    }


    private ChartReportWizardController chartReportWizardController;
}
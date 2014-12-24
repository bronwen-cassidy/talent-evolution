/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 21-Sep-2009 15:16:44
 * @version 0.1
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.talentstudio.analysis.reports.AppraisalSummaryReport;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.performance.PerformanceReview;
import com.zynap.talentstudio.web.analysis.picker.AnalysisAttributeBranch;
import com.zynap.talentstudio.web.utils.ZynapDbUnitMockControllerTestCase;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
public class TestAddAppraisalReportWizardController extends ZynapDbUnitMockControllerTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        addAppraisalWizardController = (AddAppraisalReportWizardController) getBean("addAppraisalReportController");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        addAppraisalWizardController = null;
    }

    protected String getDataSetFileName() throws Exception {
        return "appraisal-test-data.xml";
    }

    public void testFormBackingObject() throws Exception {
        AppraisalReportWrapperBean bean = (AppraisalReportWrapperBean) addAppraisalWizardController.formBackingObject(mockRequest);
        assertEquals(null, bean.getAppraisalId());
        assertEquals(1, bean.getColumns().size());
        final AppraisalSummaryReport modifiedReport = (AppraisalSummaryReport) bean.getModifiedReport();
        assertEquals(Report.APPRAISAL_REPORT, modifiedReport.getReportType());
        assertEquals(AppraisalSummaryReport.STATUS_NEW, modifiedReport.getStatus());
    }

    public void testReferenceData() throws Exception {
        // no performance reviews initially
        final int appraisalSelectPage = AddAppraisalReportWizardController.SELECT_APPRAISAL_IDX;
        AppraisalReportWrapperBean bean = (AppraisalReportWrapperBean) addAppraisalWizardController.formBackingObject(mockRequest);
        final Errors errors = getErrors(bean);

        final Map refData = addAppraisalWizardController.referenceData(mockRequest, bean, errors, appraisalSelectPage);
        assertNotNull(refData);
        final List<PerformanceReview> appraisals = bean.getAppraisals();
        assertEquals(4, appraisals.size());
    }

    public void testOnBindAndValidateInternalFails() throws Exception {
        AppraisalReportWrapperBean bean = (AppraisalReportWrapperBean) addAppraisalWizardController.formBackingObject(mockRequest);
        final Errors errors = getErrors(bean);
        addAppraisalWizardController.referenceData(mockRequest, bean, errors, AddAppraisalReportWizardController.SELECT_APPRAISAL_IDX);
        bean.setAppraisalId(APPRAISAL_ID);

        addAppraisalWizardController.onBindAndValidateInternal(mockRequest, bean, errors, 1);
        // let us verify the attributes we get back
        final List<AnalysisAttributeBranch> analysisAttributeBranches = bean.getTree();
        assertNotNull(analysisAttributeBranches);
    }

    public void testValidatePage() throws Exception {
        AppraisalReportWrapperBean bean = (AppraisalReportWrapperBean) addAppraisalWizardController.formBackingObject(mockRequest);
        final Errors errors = getErrors(bean);
        addAppraisalWizardController.onBindAndValidateInternal(mockRequest, bean, errors, 0);
        // we should have errors
        assertEquals(1, errors.getErrorCount());
    }

    public void testProcessFinish() throws Exception {
        AppraisalReportWrapperBean bean = (AppraisalReportWrapperBean) addAppraisalWizardController.formBackingObject(mockRequest);
        final Errors errors = getErrors(bean);
        addAppraisalWizardController.referenceData(mockRequest, bean, errors, AddAppraisalReportWizardController.SELECT_APPRAISAL_IDX);
        bean.setAppraisalId(APPRAISAL_ID);
        addAppraisalWizardController.onBindAndValidateInternal(mockRequest, bean, errors, 1);

        mockRequest.addParameter("_target2", "2");

        addAppraisalWizardController.onBindAndValidateInternal(mockRequest, bean, errors, 2);
        addAppraisalWizardController.onBindAndValidateInternal(mockRequest, bean, errors, 2);
        addAppraisalWizardController.onBindAndValidateInternal(mockRequest, bean, errors, 2);

        setUpColumns(bean);
        final Report modifiedReport = bean.getModifiedReport();
        assertEquals(bean.getColumns().size(), modifiedReport.getColumns().size());
    }

    public void testProcessFinishReal() throws Exception {
        setUserSession(new Long(0), mockRequest);
        AppraisalReportWrapperBean bean = (AppraisalReportWrapperBean) addAppraisalWizardController.formBackingObject(mockRequest);
        final Errors errors = getErrors(bean);
        addAppraisalWizardController.referenceData(mockRequest, bean, errors, AddAppraisalReportWizardController.SELECT_APPRAISAL_IDX);
        bean.setAppraisalId(APPRAISAL_ID);
        addAppraisalWizardController.onBindAndValidateInternal(mockRequest, bean, errors, 1);
        // got the attributes we will add some columns
        for (int i = 0; i < 3; i++) {
            bean.addColumn(new Column());
        }

        setUpColumns(bean);

        final ModelAndView view = addAppraisalWizardController.processFinishInternal(mockRequest, mockResponse, bean, errors);
        // should be the success view
        assertEquals("viewappraisalreport.htm", ((ZynapRedirectView) view.getView()).getUrl());
    }

    private void setUpColumns(AppraisalReportWrapperBean bean) {
        final List<Column> columns = bean.getColumns();
        assertEquals(4, columns.size());

        columns.get(0).setAttributeName("26873");
        columns.get(1).setAttributeName("26874");
        columns.get(2).setAttributeName("26875");
        columns.get(3).setAttributeName("26876");

        columns.get(0).setWeighting(new Integer(12));
        columns.get(1).setWeighting(new Integer(10));
        columns.get(2).setWeighting(new Integer(5));
        columns.get(3).setWeighting(new Integer(20));
    }


    private AddAppraisalReportWizardController addAppraisalWizardController;
    private final Long APPRAISAL_ID = new Long(5228);
}
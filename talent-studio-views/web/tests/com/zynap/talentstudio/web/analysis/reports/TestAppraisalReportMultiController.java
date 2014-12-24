/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 21-Sep-2009 15:07:15
 * @version 0.1
 */

import junit.framework.*;

import com.zynap.talentstudio.web.analysis.reports.AppraisalReportMultiController;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;
import com.zynap.talentstudio.web.utils.ZynapDbUnitMockControllerTestCase;
import com.zynap.talentstudio.analysis.reports.Report;

import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.List;

public class TestAppraisalReportMultiController extends ZynapDbUnitMockControllerTestCase {

    protected String getDataSetFileName() throws Exception {
        return "appraisal-test-data.xml";
    }

    public void testListAppraisalReports() throws Exception {
        AppraisalReportMultiController appraisalReportMultiController = (AppraisalReportMultiController) getBean("appraisalReportMultiController");
        final ModelAndView modelAndView = appraisalReportMultiController.listAppraisalReports(mockRequest, mockResponse);
        final Map map = modelAndView.getModel();
        final Map model = (Map) map.get("model");
        List<Report> reports = (List<Report>) model.get("reports");
        assertEquals(4, reports.size());
    }
}
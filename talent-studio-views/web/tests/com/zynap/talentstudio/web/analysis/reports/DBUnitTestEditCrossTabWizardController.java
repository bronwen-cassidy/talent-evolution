/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 30-Apr-2008 17:15:59
 * @version 0.1
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.analysis.reports.CrossTabReport;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.crosstab.CrossTabCellInfo;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.ZynapDbUnitMockControllerTestCase;
import com.zynap.util.spring.BindUtils;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;

import java.util.List;

/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */

public class DBUnitTestEditCrossTabWizardController extends ZynapDbUnitMockControllerTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        editCrossTabWizardController = (EditCrossTabWizardController) getBean("editCrossTabReportController");
    }

    public void testProcessFinish() throws Exception {

        UserSession userSession = new UserSession(getAdminUserPrincipal(), getArenaMenuHandler());
        setUserSession(userSession, mockRequest);
        mockRequest.addParameter(ParameterConstants.REPORT_ID, REQUEST_REPORT_ID);
        CrossTabReportWrapperBean testObject = (CrossTabReportWrapperBean) editCrossTabWizardController.formBackingObject(mockRequest);
        List<List<CrossTabCellInfo>> list = testObject.getCellInfos();
        assertTrue(list.isEmpty());

        DataBinder binder = BindUtils.createBinder(testObject, "command");

        Errors errors = getErrors(binder);
        mockRequest.addParameter("_target3", "_target3");
        editCrossTabWizardController.onBindAndValidateInternal(mockRequest, testObject, errors, 2);
        list = testObject.getCellInfos();
        assertEquals(4, list.size());

        List<CrossTabCellInfo> infoList = list.get(0);
        CrossTabCellInfo crossTabCellInfo = infoList.get(0);
        crossTabCellInfo.setLabel("0_0");

        infoList = list.get(1);
        crossTabCellInfo = infoList.get(1);
        crossTabCellInfo.setLabel("1_1");

        infoList = list.get(2);
        crossTabCellInfo = infoList.get(2);
        crossTabCellInfo.setLabel("2_2");

        infoList = list.get(3);
        crossTabCellInfo = infoList.get(3);
        crossTabCellInfo.setLabel("3_3");

        CrossTabReport modifiedReport = (CrossTabReport) testObject.getModifiedReport();
        List<CrossTabCellInfo> infos = modifiedReport.getCrossTabCellInfos();
        assertEquals(16, infos.size());

        CrossTabCellInfo crossTabCellInfo1 = infos.get(0);
        assertEquals("0_0", crossTabCellInfo1.getLabel());
        crossTabCellInfo1 = infos.get(15);
        assertEquals("3_3", crossTabCellInfo1.getLabel());
    }

    public void testProcessFinish_SpringBinder() throws Exception {

        UserSession userSession = new UserSession(getAdminUserPrincipal(), getArenaMenuHandler());
        setUserSession(userSession, mockRequest);
        mockRequest.addParameter(ParameterConstants.REPORT_ID, REQUEST_REPORT_ID);
        CrossTabReportWrapperBean testObject = (CrossTabReportWrapperBean) editCrossTabWizardController.formBackingObject(mockRequest);
        List<List<CrossTabCellInfo>> list = testObject.getCellInfos();
        assertTrue(list.isEmpty());

        DataBinder binder = BindUtils.createBinder(testObject, "command");

        Errors errors = getErrors(binder);
        mockRequest.addParameter("_target3", "_target3");
        editCrossTabWizardController.onBindAndValidateInternal(mockRequest, testObject, errors, 2);
        list = testObject.getCellInfos();
        assertEquals(4, list.size());

        String[] names = {"cellInfos[0][0].label", "cellInfos[1][1].label", "cellInfos[2][2].label", "cellInfos[3][3].label"};
        Object[] values = {"0_0", "1_1", "2_2", "3_3"};
        MutablePropertyValues mpv = BindUtils.createPropertyValues(names, values);
        binder.bind(mpv);

        CrossTabReport modifiedReport = (CrossTabReport) testObject.getModifiedReport();
        List<CrossTabCellInfo> infos = modifiedReport.getCrossTabCellInfos();

        assertEquals(16, infos.size());

        CrossTabCellInfo crossTabCellInfo1 = infos.get(0);
        assertEquals("0_0", crossTabCellInfo1.getLabel());
        crossTabCellInfo1 = infos.get(15);
        assertEquals("3_3", crossTabCellInfo1.getLabel());
    }

    public void testProcessFinish_OnBindAndValidate() throws Exception {

        UserSession userSession = new UserSession(getAdminUserPrincipal(), getArenaMenuHandler());
        setUserSession(userSession, mockRequest);
        mockRequest.addParameter(ParameterConstants.REPORT_ID, REQUEST_REPORT_ID);
        CrossTabReportWrapperBean testObject = (CrossTabReportWrapperBean) editCrossTabWizardController.formBackingObject(mockRequest);
        List<List<CrossTabCellInfo>> list = testObject.getCellInfos();
        assertTrue(list.isEmpty());

        DataBinder binder = BindUtils.createBinder(testObject, "command");

        Errors errors = getErrors(binder);
        mockRequest.addParameter("_target3", "_target3");
        editCrossTabWizardController.onBindAndValidateInternal(mockRequest, testObject, errors, 2);
        list = testObject.getCellInfos();
        assertEquals(4, list.size());

        String[] names = {"cellInfos[0][0].label", "cellInfos[1][1].label", "cellInfos[2][2].label", "cellInfos[3][3].label"};
        Object[] values = {"0_0", "1_1", "2_2", "3_3"};
        MutablePropertyValues mpv = BindUtils.createPropertyValues(names, values);
        binder.bind(mpv);
        mockRequest.removeParameter("_target3");
        mockRequest.addParameter("_finish", "_finish");
        editCrossTabWizardController.onBindAndValidateInternal(mockRequest, testObject, errors, 3);
        editCrossTabWizardController.processFinish(mockRequest, mockResponse, testObject, new BindException(binder.getBindingResult()));

        CrossTabReport modifiedReport = (CrossTabReport) testObject.getModifiedReport();
        List<CrossTabCellInfo> infos = modifiedReport.getCrossTabCellInfos();

        assertEquals(16, infos.size());

        CrossTabCellInfo crossTabCellInfo1 = infos.get(0);
        assertEquals("0_0", crossTabCellInfo1.getLabel());
        crossTabCellInfo1 = infos.get(15);
        assertEquals("3_3", crossTabCellInfo1.getLabel());
    }

    protected String getDataSetFileName() throws Exception {
        return "ct_test_data.xml";
    }

    private EditCrossTabWizardController editCrossTabWizardController;
    private static final String REQUEST_REPORT_ID = "341";
}
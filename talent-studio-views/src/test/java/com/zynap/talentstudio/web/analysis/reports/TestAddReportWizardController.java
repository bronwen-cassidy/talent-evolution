/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.PopulationDto;
import com.zynap.talentstudio.common.AccessType;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestAddReportWizardController extends ZynapMockControllerTest {

    protected void setUp() throws Exception {
        super.setUp();
        addReportWizardController = (AddReportWizardController) applicationContext.getBean("addReportController");
        reportValidator = (ReportValidator) applicationContext.getBean("reportValidator");
        setUserSession(new UserSession(getAdminUserPrincipal(), getArenaMenuHandler()), mockRequest);
        reportWrapper = (ReportWrapperBean) addReportWizardController.formBackingObject(mockRequest);
        mockRequest.getSession().setAttribute(ControllerConstants.COMMAND_NAME, reportWrapper);
    }

    public void testReferenceDataInternalPage0() throws Exception {
        ReportWrapperBean reportWrapperBean = (ReportWrapperBean) mockRequest.getSession().getAttribute(ControllerConstants.COMMAND_NAME);

        // populations should not be set
        assertTrue(reportWrapperBean.getPopulations().isEmpty());

        Map referenceData = addReportWizardController.referenceData(mockRequest, reportWrapperBean, getErrors(reportWrapperBean), 0);

        // check for title
        assertTrue(referenceData.containsKey(ControllerConstants.TITLE));

        // check populations are set
        assertTrue(!reportWrapperBean.getPopulations().isEmpty());
    }

    public void testReferenceDataInternalPage1() throws Exception {
        ReportWrapperBean reportWrapperBean = (ReportWrapperBean) mockRequest.getSession().getAttribute(ControllerConstants.COMMAND_NAME);
        reportWrapperBean.setType(IPopulationEngine.P_POS_TYPE_);

        // check attribute collection has not been set
        assertNull(reportWrapperBean.getAttributeCollection());

        // refdata should set it on page 1
        final Map refData = addReportWizardController.referenceData(mockRequest, reportWrapperBean, getErrors(reportWrapperBean), 1);
        assertNotNull(reportWrapperBean.getAttributeCollection());

        // check for title
        assertTrue(refData.containsKey(ControllerConstants.TITLE));
    }

    public void testFormBackingObject() throws Exception {
        ReportWrapperBean reportWrapperBean = (ReportWrapperBean) addReportWizardController.formBackingObject(mockRequest);
        assertNotNull(reportWrapperBean.getModifiedReport());
    }

    public void testValidatePage0() throws Exception {
        List<PopulationDto> x = new ArrayList();
        x.add(new PopulationDto(new Long(9), "name", IPopulationEngine.P_POS_TYPE_, AccessType.PUBLIC_ACCESS.toString(), ""));
        x.add(new PopulationDto(new Long(7), "name", IPopulationEngine.P_POS_TYPE_, AccessType.PUBLIC_ACCESS.toString(), ""));
        reportWrapper.setPopulations(x);
        bindPageZero();
    }

    private void bindPageZero() {
        DataBinder binder = new DataBinder(reportWrapper, ControllerConstants.COMMAND_NAME);
        MutablePropertyValues pvs = new MutablePropertyValues();
        pvs.addPropertyValue("label", "No of Key Positions");
        pvs.addPropertyValue("populationId", new Long(9));
        pvs.addPropertyValue("access", AccessType.PUBLIC_ACCESS.toString());
        pvs.addPropertyValue("description", "Some long Description");
        binder.bind(pvs);
        reportValidator.validateCoreValues(reportWrapper, binder.getBindingResult());
        try {
            binder.close();
        } catch (BindException e) {
            fail("Should not have thrown a bind exception: " + e.getMessage() + " field: " + e.getAllErrors().toString());
        }
    }


    private AddReportWizardController addReportWizardController;
    private ReportValidator reportValidator;
    private ReportWrapperBean reportWrapper;
}

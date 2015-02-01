/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 24-May-2010 10:22:21
 * @version 0.1
 */
package com.zynap.talentstudio.web.dashboard;

import com.zynap.talentstudio.dashboard.Dashboard;
import com.zynap.talentstudio.web.AbstractValidatorTestCase;

import org.springframework.validation.DataBinder;

/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
public class TestDashboardBuilderValidator extends AbstractValidatorTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        dashboardBuilderValidator = new DashboardBuilderValidator();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        dashboardBuilderValidator = null;
    }

    public void testSupports() throws Exception {
        assertTrue(dashboardBuilderValidator.supports(DashboardBuilderWrapper.class));
        assertFalse(dashboardBuilderValidator.supports(Dashboard.class));
    }

    public void testValidate() {
        DashboardBuilderWrapper wrapper = new DashboardBuilderWrapper(new Dashboard());
        
    }

    private DashboardBuilderValidator dashboardBuilderValidator;
    private DataBinder binder;
}
/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.dashboard;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 05-Jun-2010 09:13:49
 * @version 0.1
 */

import junit.framework.TestCase;

public class TestDashboardItemWrapper extends TestCase {

    public void testGetReference() throws Exception {
        dashboardItemWrapper = new DashboardItemWrapper();
        String ref = dashboardItemWrapper.getReference();
        assertTrue(ref.indexOf("@") == -1);
    }

    private DashboardItemWrapper dashboardItemWrapper;
}
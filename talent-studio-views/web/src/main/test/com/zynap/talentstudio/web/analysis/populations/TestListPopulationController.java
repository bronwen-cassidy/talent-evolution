/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 14-Aug-2008 10:12:31
 * @version 0.1
 */
package com.zynap.talentstudio.web.analysis.populations;

import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;
import com.zynap.talentstudio.analysis.populations.PopulationDto;

import java.util.Collection;

/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
public class TestListPopulationController extends ZynapMockControllerTest {

    protected void setUp() throws Exception {
        super.setUp();
        listPopulationController = (ListPopulationController) getBean("listPopulationController");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        listPopulationController = null;
    }

    public void testFormBackingObject() throws Exception {
        setUserSession(ADMINISTRATOR_USER_ID, mockRequest);

        final ListPopulationWrapperBean wrapper = (ListPopulationWrapperBean) listPopulationController.formBackingObject(mockRequest);
        
        final Collection<PopulationDto> populations = wrapper.getPopulations();
        for (PopulationDto population : populations) {
            // description, type, scope, label, id
            assertNotNull(population.getDescription());
            assertNotNull(population.getId());
            assertNotNull(population.getType());
            assertNotNull(population.getLabel());
            assertNotNull(population.getScope());
        }
    }

    private ListPopulationController listPopulationController;
}
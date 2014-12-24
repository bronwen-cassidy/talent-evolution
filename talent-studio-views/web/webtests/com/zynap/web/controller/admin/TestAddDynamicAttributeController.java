/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.web.controller.admin;

/**
 * Class or Interface description.
 * 
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */

import com.zynap.talentstudio.web.ZynapWebTestCase;

import com.zynap.talentstudio.organisation.Node;

public class TestAddDynamicAttributeController extends ZynapWebTestCase {

    /**
     * Tests that the lead up to the add controller is setup correctly.
     * Check hidden elements are correctly set in this form.
     * @throws Exception
     */
    public void testAddDynamicAttributeController() throws Exception {
        beginAt("admin/listpositionDA.htm");
        assertFormPresent("_create");
        assertButtonPresent("add1");
        assertFormElementEquals("artefact", Node.POSITION_UNIT_TYPE_);
    }

    /**
     * Asserts the add form is returned together with the expected form elements.
     *
     * @throws Exception
     */
    public void testFormBackingObject() throws Exception {
        beginAt("admin/listpositionDA.htm");
        clickButton("add1");
        assertFormPresent("_selecttype");
        assertFormElementPresent("type");
    }
}

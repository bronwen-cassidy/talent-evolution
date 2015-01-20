/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 08-Mar-2007 11:13:21
 * @version 0.1
 */
package com.zynap.talentstudio.web.objectives;

import com.zynap.talentstudio.web.AbstractValidatorTestCase;

/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
public class TestObjectiveSetValidator extends AbstractValidatorTestCase {

    protected void setUp() throws Exception {
        objectivesValidator = new ObjectiveSetValidator();
    }

    protected void tearDown() throws Exception {
        objectivesValidator = null;
    }

    public void testSupports() throws Exception {
        assertTrue(objectivesValidator.supports(CorporateObjectivesFormBean.class));
    }    

    private ObjectiveSetValidator objectivesValidator;
}
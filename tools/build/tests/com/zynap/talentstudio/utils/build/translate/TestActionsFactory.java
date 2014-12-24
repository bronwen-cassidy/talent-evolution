/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.utils.build.translate;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 16-Jun-2006 12:28:34
 * @version 0.1
 */

import junit.framework.TestCase;

import com.zynap.talentstudio.utils.build.translate.elements.Actions;

public class TestActionsFactory extends TestCase {

    public void testParse() throws Exception {
        ActionsFactory actionsFactory = new ActionsFactory();
        Actions actions = actionsFactory.parse("com/zynap/talentstudio/utils/build/translate/testaddda.xml");
        assertEquals(15, actions.getActions().size());

    }
}
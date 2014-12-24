/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.domain;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 18-May-2010 15:26:18
 * @version 0.1
 */

import junit.framework.TestCase;

public class TestQueryParameter extends TestCase {

    public void testBuildValue() throws Exception {
        QueryParameter param = new QueryParameter(new String[] {"ONE", "TWO"}, QueryParameter.STRINGARRAY);
        String x = param.buildValue();
        assertEquals(" in ( 'ONE' , 'TWO' ) ", x);
    }
}
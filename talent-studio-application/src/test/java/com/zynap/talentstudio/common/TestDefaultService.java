/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.common;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 11-May-2007 15:16:21
 * @version 0.1
 */

import com.zynap.talentstudio.AbstractHibernateTestCase;

import java.util.Iterator;
import java.util.Map;

public class TestDefaultService extends AbstractHibernateTestCase {

    /**
     * runs and times find all for all the services.
     *
     * @throws Exception
     */
    public void testFindAll() throws Exception {

        Map allBeans = applicationContext.getBeansOfType(IZynapService.class, false, true);
        
        for (Iterator iterator = allBeans.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String name = (String) entry.getKey();
            if (name.indexOf("Target") != -1) continue;
            IZynapService zynapService = (IZynapService) entry.getValue();
            long start = System.currentTimeMillis();
            zynapService.findAll();
            long end = System.currentTimeMillis();
            System.out.println("Time taken for " + name + " > " + (end - start) + " millisecs");
        }
    }
}
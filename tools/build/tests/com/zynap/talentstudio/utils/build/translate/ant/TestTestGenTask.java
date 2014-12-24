/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.utils.build.translate.ant;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 20-Jun-2006 14:46:00
 * @version 0.1
 */

import junit.framework.TestCase;

import org.apache.tools.ant.BuildException;

public class TestTestGenTask extends TestCase {

    public void testExecute() throws Exception {
        try {
            TestGenTask testGenTask = new TestGenTask();
            //testGenTask.setBaseXmlDirectory("/zynap/TSIntegration1/talent-studio-views/web/webtests/testgen4web");
            //on clio
            testGenTask.setBaseXmlDirectory("/home/bcassidy/cruise/TSIntegration/talent-studio-views/web/webtests/testgen4web");
            //testGenTask.setGenSourceDirectory("/zynap/TSIntegration1/build/testgen4web");
            testGenTask.setGenSourceDirectory("/home/bcassidy/cruise/TSIntegration/build/test-classes");
            testGenTask.setFileExcludePattern(".*test-data.xml");
            testGenTask.execute();
        } catch (BuildException e) {
            e.printStackTrace();
            fail("No exception expected but got " + e.getMessage());
        }
    }
}
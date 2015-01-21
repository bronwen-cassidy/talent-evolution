/*
 * Copyright (C)  Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.integration.conversion;

import junit.framework.TestCase;

import com.zynap.talentstudio.integration.conversion.CsvXmlConverter;

/**
 * Class or Interface description.
 *
 * @author syeoh
 * @version 0.1
 * @since 11-Jul-2007 13:26:37
 */

public class TestCsvXmlConverter extends TestCase {

    public void testMain() throws Exception {
        String[] args = {CSV_FILE_NAME};
        System.setProperty("action", "create");
        CsvXmlConverter.main(args);
    }

    private static String CSV_FILE_NAME = "csv.properties";
}

/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.common.util;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 19-Jan-2009 13:34:10
 * @version 0.1
 */

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class FileUtilsTest {

    @Test
    public void testGetExtension() throws Exception {
        String filename = "c:program files/test/test again.pdf";
        final String extension = FileUtils.getExtension(filename);
        assertEquals("pdf", extension);
    }
}
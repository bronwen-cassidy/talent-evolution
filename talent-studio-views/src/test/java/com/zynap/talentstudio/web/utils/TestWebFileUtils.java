package com.zynap.talentstudio.web.utils;

/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */

import junit.framework.TestCase;

public class TestWebFileUtils extends TestCase {

    public void testHandleMultipartTransfer() {
        String originalFileName = "This is a file world.2000.dvd.doc";
        String firstPart = originalFileName.substring(0, originalFileName.lastIndexOf("."));
        assertEquals("This is a file world.2000.dvd", firstPart);
        String lastPart = originalFileName.substring(firstPart.length());
        assertEquals(".doc", lastPart);
    }

    public void testHandleMultipartTransfer2() {
        String originalFileName = "This is a file world.doc";
        String firstPart = originalFileName.substring(0, originalFileName.lastIndexOf("."));
        assertEquals("This is a file world", firstPart);
        String lastPart = originalFileName.substring(firstPart.length());
        assertEquals(".doc", lastPart);
    }

    public void testHandleMultipartTransfer3() {
        String originalFileName = "zynapWorld.txt";
        String firstPart = originalFileName.substring(0, originalFileName.lastIndexOf("."));
        assertEquals("zynapWorld", firstPart);
        String lastPart = originalFileName.substring(firstPart.length());
        assertEquals(".txt", lastPart);
    }    
}
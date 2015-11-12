package com.zynap.talentstudio.web.utils;

/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */

import junit.framework.TestCase;

public class TestHtmlUtils extends TestCase {

    public void testEscapeHex() throws Exception {
        String expected = "This%20is%20%26%20again";
        String original = "This is & again";
        String actual = HtmlUtils.escapeHex(original);
        assertEquals(expected, actual);
    }

    public void testHtmlEscapeSingleQuote() throws Exception {
        String expected = "This is with&#39;in";
        String data = "This is with'in";
        String actual = HtmlUtils.htmlEscape(data);
        assertEquals(expected, actual);
    }

    public void testHtmlEscape() throws Exception {

        String data = "";
        for (int i = 0; i < 10000; i++) {
            char theChar = (char) i;
            data += theChar;
        }

        String actual = HtmlUtils.htmlEscape(data);
        assertFalse(actual.length() == 0);
    }
}
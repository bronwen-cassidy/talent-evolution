package com.zynap.web.beans;

/**
 * User: amark
 * Date: 18-Oct-2006
 * Time: 13:08:11
 */

import com.zynap.talentstudio.ZynapTestCase;

public class TestZynapCustomNumberEditors extends ZynapTestCase {

    public void testIntegerEditor() throws Exception {

        ZynapCustomIntegerEditor editor = new ZynapCustomIntegerEditor();
        assertEquals("", editor.getAsText());

        try {
            final String value = Long.toString(Long.MAX_VALUE);
            editor.setAsText(value);
            fail(editor.getClass() + " should reject value: " + value);
        } catch (IllegalArgumentException expected) {
        }

        final String value = "1";
        editor.setAsText(value);
        assertEquals(Integer.valueOf(value), editor.getValue());
        assertEquals(value, editor.getAsText());
    }

    public void testLongEditor() throws Exception {

        ZynapCustomLongEditor editor = new ZynapCustomLongEditor();
        assertEquals("", editor.getAsText());

        try {
            final String value = Double.toString(Double.MAX_VALUE);
            editor.setAsText(value);
            fail(editor.getClass() + " should reject value: " + value);
        } catch (IllegalArgumentException expected) {
        }

        final String value = Long.toString(Long.MAX_VALUE);
        editor.setAsText(value);
        assertEquals(Long.valueOf(value), editor.getValue());
        assertEquals(value, editor.getAsText());
    }

    public void testDoubleEditor() throws Exception {

        ZynapCustomDoubleEditor editor = new ZynapCustomDoubleEditor();
        assertEquals("", editor.getAsText());

        try {
            final String value = "foo";
            editor.setAsText(value);
            fail(editor.getClass() + " should reject value: " + value);
        } catch (IllegalArgumentException expected) {
        }

        final String value = Double.toString(Double.MIN_VALUE);
        editor.setAsText(value);
        assertEquals(Double.valueOf(value), editor.getValue());
        assertEquals(value, editor.getAsText());
    }
}
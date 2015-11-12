package com.zynap.talentstudio.web.beans;

/**
 * User: amark
 * Date: 18-Oct-2006
 * Time: 12:53:11
 */

import com.zynap.talentstudio.ZynapTestCase;
import com.zynap.common.util.StringUtil;

public class TestZynapCustomBooleanEditor extends ZynapTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        zynapCustomBooleanEditor = new ZynapCustomBooleanEditor();
    }

    public void testSetAsText() throws Exception {

        final String[] trueValues = new String[] {"true", "on", StringUtil.TRUE};
        for (int i = 0; i < trueValues.length; i++) {
            String trueValue = trueValues[i];
            zynapCustomBooleanEditor.setAsText(trueValue);
            final boolean value = ((Boolean) zynapCustomBooleanEditor.getValue()).booleanValue();
            assertTrue(value);
        }

        final String[] falseValues = new String[] {null, "", "false", "off", StringUtil.FALSE};
        for (int i = 0; i < falseValues.length; i++) {
            String trueValue = falseValues[i];
            zynapCustomBooleanEditor.setAsText(trueValue);
            final boolean value = ((Boolean) zynapCustomBooleanEditor.getValue()).booleanValue();
            assertFalse(value);
        }

        try {
            zynapCustomBooleanEditor.setAsText("foobar");
            fail("Should reject foobar as value for boolean");
        } catch (IllegalArgumentException expected) {

        }
    }

    private ZynapCustomBooleanEditor zynapCustomBooleanEditor;
}
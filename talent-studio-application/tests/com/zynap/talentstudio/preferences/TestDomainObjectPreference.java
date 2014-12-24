package com.zynap.talentstudio.preferences;

/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */

import com.zynap.talentstudio.preferences.domain.DomainObjectPreference;
import com.zynap.talentstudio.preferences.format.FormattingAttribute;
import com.zynap.talentstudio.preferences.format.FormattingInfo;
import com.zynap.talentstudio.preferences.properties.AttributePreference;

public class TestDomainObjectPreference extends BasePreferenceTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        ouDomainObjectPreference = (DomainObjectPreference) applicationContext.getBean("orgUnitPref");
    }

    public void testGetPropertyPref() throws Exception {

        AttributePreference attributePreference = ouDomainObjectPreference.get(LABEL_PREF_NAME);
        assertEquals(LABEL_PREF_NAME, attributePreference.getAttributeName());
        FormattingInfo formattingInfo = attributePreference.getFormattingInfo();
        assertEquals(2, formattingInfo.getFormattingAttributes().size());
        FormattingAttribute color = formattingInfo.get(COLOR_ATTR_NAME);
        assertNotNull(color);
        assertEquals("white", color.getValue());
    }

    public void testSetPropertyPref() {

        ouDomainObjectPreference.addOrUpdate(LABEL_PREF_NAME, null, false);
        AttributePreference labelPref = ouDomainObjectPreference.get(LABEL_PREF_NAME);
        assertEquals(false, labelPref.isDisplayable());

        final String newAttributeName = "newPref";
        ouDomainObjectPreference.addOrUpdate(newAttributeName, null, false);
        AttributePreference newPref = ouDomainObjectPreference.get(newAttributeName);
        assertNotNull(newPref);
        assertEquals(false, newPref.isDisplayable());

        // set it back again
        ouDomainObjectPreference.addOrUpdate(LABEL_PREF_NAME, null, true);
    }

}
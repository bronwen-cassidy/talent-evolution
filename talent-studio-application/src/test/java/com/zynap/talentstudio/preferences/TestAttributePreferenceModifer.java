package com.zynap.talentstudio.preferences;

/**
 * User: amark
 * Date: 02-Dec-2004
 * Time: 14:05:51
 */

import junit.framework.TestCase;

import com.zynap.talentstudio.preferences.domain.DomainObjectPreference;
import com.zynap.talentstudio.preferences.domain.DomainObjectPreferenceCollection;
import com.zynap.talentstudio.preferences.format.FormattingAttribute;
import com.zynap.talentstudio.preferences.format.FormattingInfo;
import com.zynap.talentstudio.preferences.properties.AttributePreference;
import com.zynap.talentstudio.preferences.properties.AttributeView;
import com.zynap.talentstudio.preferences.properties.ConditionalAttributePreference;
import com.zynap.talentstudio.preferences.properties.SwitchAttributePreference;

public class TestAttributePreferenceModifer extends TestCase {

    public void testSetPropertyToBeInvisible() throws Exception {

        DomainObjectPreferenceCollection domainObjectPreferenceCollection = new DomainObjectPreferenceCollection();

        DomainObjectPreference domainObjectPreference = new DomainObjectPreference();
        AttributePreference attributePreference = new AttributePreference();
        attributePreference.setAttributeName("name");
        attributePreference.setDisplayable(true);

        FormattingInfo formattingInfo = new FormattingInfo();
        formattingInfo.add(new FormattingAttribute("fgcolor", "white"));
        formattingInfo.add(new FormattingAttribute("bgcolor", "red"));
        attributePreference.setFormattingInfo(formattingInfo);
        domainObjectPreference.add(attributePreference);
        domainObjectPreferenceCollection.add(DomainObjectTestHelper.class.getName(), domainObjectPreference);

        DomainObjectPreference dp = domainObjectPreferenceCollection.get(DomainObjectTestHelper.class.getName());
        dp.addOrUpdate("name", null, false);

        DomainObjectTestHelper domainObjectTestHelper = new DomainObjectTestHelper();
        domainObjectTestHelper.setName("bronny");

        AttributeView attributeView = domainObjectPreferenceCollection.getPropertyValue(domainObjectTestHelper, "name");
        assertNull(attributeView);

        dp = domainObjectPreferenceCollection.get(DomainObjectTestHelper.class.getName());
        dp.addOrUpdate("name", null, true);

        attributeView = domainObjectPreferenceCollection.getPropertyValue(domainObjectTestHelper, "name");
        assertNotNull(attributeView);
        assertEquals("bronny", attributeView.getExpectedValue());

        dp.addOrUpdate("desc", null, true);
        AttributePreference desc = dp.get("desc");
        assertEquals(true, desc.isDisplayable());
        assertEquals("desc", desc.getAttributeName());

        dp.addOrUpdateFormattingInfo("desc", null, "fgColor", "green");
        AttributeView descView = domainObjectPreferenceCollection.getPropertyValue(domainObjectTestHelper, "desc");
        System.out.println("descView = " + descView);
    }

    public void testSetConditionalProperty() throws Exception {

        String expectedValue = "description1";
        String attributeName = "desc";

        DomainObjectTestHelper domainObjectTestHelper = new DomainObjectTestHelper();
        domainObjectTestHelper.setName("bronny");
        domainObjectTestHelper.setDesc("foobar");

        DomainObjectPreferenceCollection domainObjectPreferenceCollection = new DomainObjectPreferenceCollection();

        SwitchAttributePreference switchPropertyPref = new SwitchAttributePreference();
        switchPropertyPref.setDisplayable(true);
        switchPropertyPref.setAttributeName(attributeName);

        ConditionalAttributePreference conditionalPropertyPref = new ConditionalAttributePreference();
        conditionalPropertyPref.setExpectedValue(expectedValue);
        conditionalPropertyPref.setDisplayable(true);

        FormattingInfo formattingInfo = new FormattingInfo();
        formattingInfo.add(new FormattingAttribute("fgcolor", "white"));
        formattingInfo.add(new FormattingAttribute("bgcolor", "red"));
        conditionalPropertyPref.setFormattingInfo(formattingInfo);

        switchPropertyPref.add(conditionalPropertyPref);

        DomainObjectPreference domainObjectPreference = new DomainObjectPreference();
        domainObjectPreference.add(switchPropertyPref);
        domainObjectPreferenceCollection.add(DomainObjectTestHelper.class.getName(), domainObjectPreference);

        domainObjectPreference.addOrUpdate(attributeName, expectedValue, false);
        AttributeView attributeView = domainObjectPreferenceCollection.getPropertyValue(domainObjectTestHelper, attributeName);
        assertNull(attributeView);

        domainObjectTestHelper.setDesc(expectedValue);
        attributeView = domainObjectPreferenceCollection.getPropertyValue(domainObjectTestHelper, attributeName);
        assertNull(attributeView);

        domainObjectPreference.addOrUpdate(attributeName, expectedValue, true);
        attributeView = domainObjectPreferenceCollection.getPropertyValue(domainObjectTestHelper, attributeName);
        System.out.println("attributeView = " + attributeView);
        assertNotNull(attributeView);
    }

    public void testAddSwitchPropertyPref() throws Exception {

        String expectedValue = "description1";
        String attributeName = "desc";

        DomainObjectTestHelper domainObjectTestHelper = new DomainObjectTestHelper();
        domainObjectTestHelper.setName("bronny");
        domainObjectTestHelper.setDesc(expectedValue);

        DomainObjectPreferenceCollection domainObjectPreferenceCollection = new DomainObjectPreferenceCollection();
        DomainObjectPreference domainObjectPreference = new DomainObjectPreference();
        domainObjectPreferenceCollection.add(DomainObjectTestHelper.class.getName(), domainObjectPreference);

        domainObjectPreference.addOrUpdate(attributeName, expectedValue, true);
        AttributeView attributeView = domainObjectPreferenceCollection.getPropertyValue(domainObjectTestHelper, attributeName);
        System.out.println("attributeView = " + attributeView);
        assertNull(attributeView.getFormattingInfo());
        assertNotNull(attributeView.getExpectedValue());

        domainObjectPreference.addOrUpdateFormattingInfo(attributeName, expectedValue, "line-width", "1px");
        attributeView = domainObjectPreferenceCollection.getPropertyValue(domainObjectTestHelper, attributeName);
        System.out.println("attributeView = " + attributeView);
        assertNotNull(attributeView.getExpectedValue());
        FormattingInfo formattingInfo = attributeView.getFormattingInfo();
        assertNotNull(formattingInfo);
        String actual = formattingInfo.get("line-width").getValue();
        assertEquals("1px", actual);

        // add new condition and test that it is not returned
        domainObjectPreference.addOrUpdateFormattingInfo(attributeName, "foobar", "line-width", "10px");
        attributeView = domainObjectPreferenceCollection.getPropertyValue(domainObjectTestHelper, attributeName);
        actual = attributeView.getFormattingInfo().get("line-width").getValue();
        assertEquals("1px", actual);

        // set desc to "foobar" and check that new value of line-width is returned
        domainObjectTestHelper.setDesc("foobar");
        attributeView = domainObjectPreferenceCollection.getPropertyValue(domainObjectTestHelper, attributeName);
        actual = attributeView.getFormattingInfo().get("line-width").getValue();
        assertEquals("10px", actual);
    }
}
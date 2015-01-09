package com.zynap.talentstudio.preferences;

/**
 * User: amark
 * Date: 02-Dec-2004
 * Time: 11:17:43
 */

import junit.framework.TestCase;

import com.zynap.talentstudio.preferences.format.FormattingAttribute;
import com.zynap.talentstudio.preferences.format.FormattingInfo;

public class TestAttributePreference extends TestCase {

    public void testApply() throws Exception {

        final String name = "testName";
        final String expectedValue = "testName2";

        DomainObjectTestHelper domainObjectTestHelper = new DomainObjectTestHelper();
        domainObjectTestHelper.setName(name);

        AttributePreference attributePreference = new AttributePreference();
        attributePreference.setAttributeName(NAME_ATTRIBUTE);
        attributePreference.setDisplayable(true);
        AttributeView view = attributePreference.apply(domainObjectTestHelper);
        assertEquals(name, view.getExpectedValue());

        SwitchAttributePreference switchPropertyPref = new SwitchAttributePreference();
        switchPropertyPref.setAttributeName(NAME_ATTRIBUTE);
        ConditionalAttributePreference conditionalPropertyPref = new ConditionalAttributePreference();
        conditionalPropertyPref.setDisplayable(true);
        conditionalPropertyPref.setExpectedValue(expectedValue);

        switchPropertyPref.add(conditionalPropertyPref);
        view = switchPropertyPref.apply(domainObjectTestHelper);
        assertNull(view);
    }

    public void testFormattingInfo() throws Exception {

        final String name = "testName";

        DomainObjectTestHelper domainObjectTestHelper = new DomainObjectTestHelper();
        domainObjectTestHelper.setName(name);

        AttributePreference attributePreference = new AttributePreference();
        attributePreference.setAttributeName(NAME_ATTRIBUTE);
        attributePreference.setDisplayable(true);

        FormattingInfo formattingInfo = new FormattingInfo();
        formattingInfo.add(new FormattingAttribute(FORMATTING_FG_COLOR, "white"));
        formattingInfo.add(new FormattingAttribute(FORMATTING_BG_COLOR, "red"));
        attributePreference.setFormattingInfo(formattingInfo);

        AttributeView view = attributePreference.apply(domainObjectTestHelper);
        assertEquals(formattingInfo, view.getFormattingInfo());
    }

    public void testConditionalFormattingInfo() throws Exception {

        final String name = "testName";

        DomainObjectTestHelper domainObjectTestHelper = new DomainObjectTestHelper();
        domainObjectTestHelper.setName(name);

        SwitchAttributePreference switchPropertyPref = new SwitchAttributePreference();
        switchPropertyPref.setDisplayable(true);
        switchPropertyPref.setAttributeName(NAME_ATTRIBUTE);

        ConditionalAttributePreference propertyPref = new ConditionalAttributePreference();
        propertyPref.setDisplayable(true);
        propertyPref.setExpectedValue(name);

        FormattingInfo formattingInfo = new FormattingInfo();
        formattingInfo.add(new FormattingAttribute(FORMATTING_FG_COLOR, "white"));
        formattingInfo.add(new FormattingAttribute(FORMATTING_BG_COLOR, "red"));
        propertyPref.setFormattingInfo(formattingInfo);

        switchPropertyPref.add(propertyPref);

        AttributeView view = switchPropertyPref.apply(domainObjectTestHelper);
        assertEquals(formattingInfo, view.getFormattingInfo());
    }

    public void testCompositeSwitchPreference() throws Exception {

        final String name = "testName";
        final String desc = "testDescription";

        CompositeSwitchAttributePreference parentAttributePreference = new CompositeSwitchAttributePreference(ACTIVE_ATTRIBUTE, true);

        // switch preference for name
        SwitchAttributePreference nameAttributePreference = new SwitchAttributePreference();
        nameAttributePreference.setDisplayable(true);
        nameAttributePreference.setAttributeName(NAME_ATTRIBUTE);

        // build formatting info
        ConditionalAttributePreference expectedCondition = new ConditionalAttributePreference();
        expectedCondition.setDisplayable(true);
        expectedCondition.setExpectedValue(name);
        nameAttributePreference.add(expectedCondition);

        FormattingInfo formattingInfo = new FormattingInfo();
        formattingInfo.add(new FormattingAttribute(FORMATTING_FG_COLOR, "white"));
        formattingInfo.add(new FormattingAttribute(FORMATTING_BG_COLOR, "red"));
        expectedCondition.setFormattingInfo(formattingInfo);

        // add switch preference for name to be used if active is true
        parentAttributePreference.add(Boolean.TRUE.toString(), nameAttributePreference);

        // build the object to use
        DomainObjectTestHelper testObject = new DomainObjectTestHelper();
        testObject.setActive(true);
        testObject.setName(name);
        testObject.setDesc(desc);

        // apply the preference
        final AttributeView attributeView = parentAttributePreference.apply(testObject);
        assertNotNull(attributeView);

        // check that we got back the correct formatting info
        assertEquals(formattingInfo, attributeView.getFormattingInfo());

        // make preference non-displayable and apply - should return null view
        parentAttributePreference.setDisplayable(false);
        assertNull(parentAttributePreference.apply(testObject));
    }

    public void testCompositeSwitchPreferenceDefault() throws Exception {

        final String name = "testName";
        final String desc = "testDescription";

        CompositeSwitchAttributePreference parentAttributePreference = new CompositeSwitchAttributePreference(ACTIVE_ATTRIBUTE, true);

        // switch preference for name
        SwitchAttributePreference nameAttributePreference = new SwitchAttributePreference();
        nameAttributePreference.setDisplayable(true);
        nameAttributePreference.setAttributeName(NAME_ATTRIBUTE);

        // build formatting info
        ConditionalAttributePreference expectedCondition = new ConditionalAttributePreference();
        expectedCondition.setDisplayable(true);
        expectedCondition.setExpectedValue(name);
        nameAttributePreference.add(expectedCondition);

        FormattingInfo expectedFormattingInfo = new FormattingInfo();
        expectedFormattingInfo.add(new FormattingAttribute(FORMATTING_FG_COLOR, "white"));
        expectedFormattingInfo.add(new FormattingAttribute(FORMATTING_BG_COLOR, "red"));
        expectedCondition.setFormattingInfo(expectedFormattingInfo);

        // add switch preference for name to be used if active is true
        parentAttributePreference.add(Boolean.TRUE.toString(), nameAttributePreference);

        // add default preference
        SwitchAttributePreference defaultPreference = new SwitchAttributePreference();
        defaultPreference.setDisplayable(true);
        final ConditionalAttributePreference defaultCondition = new ConditionalAttributePreference(AttributePreference.DEFAULT, true);
        FormattingInfo defaultFormattingInfo = new FormattingInfo();
        defaultFormattingInfo.add(new FormattingAttribute(FORMATTING_FG_COLOR, "yellow"));
        defaultFormattingInfo.add(new FormattingAttribute(FORMATTING_BG_COLOR, "green"));
        defaultCondition.setFormattingInfo(defaultFormattingInfo);

        defaultPreference.add(defaultCondition);
        parentAttributePreference.add(AttributePreference.DEFAULT, defaultPreference);

        // build the object to use
        DomainObjectTestHelper testObject = new DomainObjectTestHelper();
        testObject.setActive(false);
        testObject.setName(name);
        testObject.setDesc(desc);

        // apply the preference - should return default as the object has the value of false for the
        final AttributeView attributeView = parentAttributePreference.apply(testObject);
        System.out.println("attributeView = " + attributeView);
        assertNotNull(attributeView);

        // check that we got back the correct formatting info
        assertEquals(defaultFormattingInfo, attributeView.getFormattingInfo());
    }

    private static final String ACTIVE_ATTRIBUTE = "active";
    private static final String NAME_ATTRIBUTE = "name";

    private static final String FORMATTING_FG_COLOR = "fgcolor";
    private static final String FORMATTING_BG_COLOR = "bgcolor";
}
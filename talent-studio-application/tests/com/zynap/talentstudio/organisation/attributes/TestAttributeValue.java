package com.zynap.talentstudio.organisation.attributes;

/**
 * User: amark
 * Date: 05-Sep-2005
 * Time: 10:48:19
 */

import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.common.lookups.LookupType;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.util.FormatterFactory;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TestAttributeValue extends AbstractHibernateTestCase {

    private ILookupManager lookupManager;

    protected void setUp() throws Exception {
        super.setUp();

        lookupManager = (ILookupManager) applicationContext.getBean("lookupManager");
    }

    public void testGetDisplayValueNoValue() throws Exception {
        DynamicAttribute dynamicAttribute = DynamicAttribute.DA_TYPE_NUMBER_O;
        testDisplayValue(dynamicAttribute, null, "");
    }

    public void testGetMultiSelectGetDisplayValue() throws Exception {

        final LookupType lookupType = lookupManager.findLookupType(ILookupManager.LOOKUP_TYPE_TITLE);
        final List<LookupValue> lookupValues = new ArrayList<LookupValue>(lookupType.getLookupValues());

        final DynamicAttribute dynamicAttribute = new DynamicAttribute();
        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_MULTISELECT);
        dynamicAttribute.setRefersToType(lookupType);
        assertTrue(dynamicAttribute.isMultiSelectionType());

        // select first and last lookup values
        List<String> expectedValues = new ArrayList<String>();

        final LookupValue firstValue = lookupValues.get(0);
        expectedValues.add(firstValue.getLabel());
        final AttributeValue attributeValue = AttributeValue.create(firstValue.getId().toString(), dynamicAttribute);

        final LookupValue lastValue = lookupValues.get(lookupValues.size() - 1);
        expectedValues.add(lastValue.getLabel());
        NodeExtendedAttribute lastNodeExtendedAttribute = new NodeExtendedAttribute(lastValue.getId().toString(), dynamicAttribute);
        attributeValue.addValue(lastNodeExtendedAttribute, true);

        // check display value has expected values - should be the labels of both selected lookup values
        String expected = StringUtils.collectionToDelimitedString(expectedValues, ",");
        assertEquals(expected, attributeValue.getDisplayValue());
    }

    public void testNumberGetDisplayValue() throws Exception {
        final String value = new String("1");
        DynamicAttribute dynamicAttribute = DynamicAttribute.DA_TYPE_NUMBER_O;
        testDisplayValue(dynamicAttribute, value, value);
    }

    public void testDateGetDisplayValue() throws Exception {
        final String value = "2005-09-01";
        final String expectedDisplayValue = FormatterFactory.getDateFormatter().formatDateAsString(value);
        DynamicAttribute dynamicAttribute = DynamicAttribute.DA_TYPE_DATE_O;
        testDisplayValue(dynamicAttribute, value, expectedDisplayValue);
    }

    public void testDateTimeGetDisplayValue() throws Exception {
        final String value = "2005-09-01 00:30";
        final String expectedDisplayValue = FormatterFactory.getDateFormatter().formatDateTimeAsString(value);
        DynamicAttribute dynamicAttribute = DynamicAttribute.DA_TYPE_DATETIMESTAMP_O;
        testDisplayValue(dynamicAttribute, value, expectedDisplayValue);
    }

    public void testTimeGetDisplayValue() throws Exception {
        final String value = "00:30";
        DynamicAttribute dynamicAttribute = DynamicAttribute.DA_TYPE_TIMESTAMP_O;
        testDisplayValue(dynamicAttribute, value, value);
    }

    public void testGetDisplayValue() throws Exception {
        final String value = "some text";
        DynamicAttribute dynamicAttribute = DynamicAttribute.DA_TYPE_TEXTFIELD_O;
        testDisplayValue(dynamicAttribute, value, value);
    }

    public void testTextAreaGetDisplayValue() throws Exception {
        final String value = "some text";
        DynamicAttribute dynamicAttribute = DynamicAttribute.DA_TYPE_TEXTAREA_O;
        testDisplayValue(dynamicAttribute, value, value);
    }

    public void testStructGetDisplayValue() throws Exception {

        final LookupType lookupType = lookupManager.findLookupType(ILookupManager.LOOKUP_TYPE_TITLE);
        final LookupValue lookupValue = lookupType.getLookupValues().iterator().next();

        final DynamicAttribute dynamicAttribute = new DynamicAttribute();
        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_STRUCT);
        dynamicAttribute.setRefersToType(lookupType);

        final AttributeValue attributeValue = AttributeValue.create(lookupValue.getId().toString(), dynamicAttribute);
        final String selectionValue = attributeValue.getDisplayValue();
        assertEquals(lookupValue.getLabel(), selectionValue);
    }

    public void testStructGetDisplayBlankValue() throws Exception {

        final AttributeValue attributeValue = AttributeValue.create(DynamicAttribute.DA_TYPE_STRUCT_O);
        final String displayValue = attributeValue.getDisplayValue();
        assertNotNull(displayValue);
        assertEquals(0, displayValue.length());
    }

    public void testImageGetDisplayValue() throws Exception {
        final String value = "some text";
        DynamicAttribute dynamicAttribute = DynamicAttribute.DA_TYPE_IMAGE_O;
        testDisplayValue(dynamicAttribute, value, value);
    }

    public void testLinkGetDisplayValue() throws Exception {
        final String value = "http://www.zynap.com";
        DynamicAttribute dynamicAttribute = DynamicAttribute.DA_TYPE_HTMLLINK_O;
        testDisplayValue(dynamicAttribute, value, value);
    }

    public void testOrgUnitDisplayValue() throws Exception {
        final String label = "org unit 1";
        DynamicAttribute dynamicAttribute = new DynamicAttribute("org unit", DynamicAttribute.DA_TYPE_OU);
        testNodeAttributeDisplayValue(dynamicAttribute, label, "-1");
    }

    public void testSubjectDisplayValue() throws Exception {
        final String label = "subject 1";
        DynamicAttribute dynamicAttribute = new DynamicAttribute(IPopulationEngine.SUBJECT_ATTR, DynamicAttribute.DA_TYPE_SUBJECT);

        testNodeAttributeDisplayValue(dynamicAttribute, label, "-1");
    }

    public void testPositionDisplayValue() throws Exception {
        final String label = "position 1";
        DynamicAttribute dynamicAttribute = new DynamicAttribute(IPopulationEngine.POSITION_ATTR, DynamicAttribute.DA_TYPE_POSITION);

        testNodeAttributeDisplayValue(dynamicAttribute, label, "-1");
    }

    public void testSetValue() throws Exception {

        final String firstValue = "origValue";
        final DynamicAttribute dynamicAttribute = DynamicAttribute.DA_TYPE_TEXTAREA_O;

        final Position node = new Position(DEFAULT_POSITION_ID);
        final AttributeValue attributeValue = AttributeValue.create(null, node, dynamicAttribute);
        assertNotNull(attributeValue.getNodeExtendedAttributes());
        assertNull(attributeValue.getValue());

        // set value and test
        assertNodeExtendedAttributes(attributeValue, firstValue);

        // set new value and test
        String secondValue = "newValue";
        assertNodeExtendedAttributes(attributeValue, secondValue);
    }

    public void testMultiSelectSetValue() throws Exception {

        final String firstValue = "origValue";
        final DynamicAttribute dynamicAttribute = DynamicAttribute.DA_TYPE_MULTISELECT_O;

        final Position node = new Position(DEFAULT_POSITION_ID);
        final AttributeValue attributeValue = AttributeValue.create(null, node, dynamicAttribute);
        assertNotNull(attributeValue.getNodeExtendedAttributes());
        assertNull(attributeValue.getValue());

        // set value and test
        assertNodeExtendedAttributes(attributeValue, firstValue);

        // set new value and test
        String secondValue = "newValue";
        assertNodeExtendedAttributes(attributeValue, secondValue);
    }

    private void assertNodeExtendedAttributes(final AttributeValue attributeValue, final String value) {

        attributeValue.setValue(value);
        assertEquals(value, attributeValue.getValue());

        attributeValue.initialiseNodeExtendedAttributes(value);
        List nodeExtendedAttributes = attributeValue.getNodeExtendedAttributes();
        assertEquals(1, nodeExtendedAttributes.size());
        assertEquals(value, ((NodeExtendedAttribute) nodeExtendedAttributes.get(0)).getValue());
    }

    private void testDisplayValue(DynamicAttribute dynamicAttribute, final String value, String expectedDisplayValue) {

        final AttributeValue attributeValue = AttributeValue.create(value, dynamicAttribute);
        final String displayValue = attributeValue.getDisplayValue();
        assertNotNull(displayValue);
        assertEquals(expectedDisplayValue, displayValue);
    }

    private void testNodeAttributeDisplayValue(DynamicAttribute dynamicAttribute, final String label, String value) {

        AttributeValue attributeValue = AttributeValue.create(value, dynamicAttribute);
        attributeValue.setLabel(label);
        attributeValue.setValue(value);

        final String displayValue = attributeValue.getDisplayValue();
        assertEquals(label, displayValue);
    }
}
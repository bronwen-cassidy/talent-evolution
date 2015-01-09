package com.zynap.talentstudio.organisation;

/**
 * User: amark
 * Date: 29-Aug-2006
 * Time: 10:20:24
 */

import com.zynap.talentstudio.ZynapTestCase;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.AttributeValuesCollection;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.organisation.positions.Position;

import org.springframework.util.StringUtils;

import java.util.*;

public class TestNode extends ZynapTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        node = new Position();
    }

    public void testAddNodeExtendedAttribute() throws Exception {

        // check extended attributes and attribute values are all not null but are empty
        Set extendedAttributes = node.getExtendedAttributes();
        assertNotNull(extendedAttributes);
        assertEquals(0, extendedAttributes.size());

        AttributeValuesCollection dynamicAttributeValues = node.getDynamicAttributeValues();
        assertNotNull(dynamicAttributeValues);
        assertEquals(0, dynamicAttributeValues.size());

        // add 1 node extended attribute
        final String value = "expectedValue";
        final NodeExtendedAttribute nodeExtendedAttribute = new NodeExtendedAttribute(value, node, DynamicAttribute.DA_TYPE_STRUCT_O);
        node.addNodeExtendedAttribute(nodeExtendedAttribute);

        assertNodeExtendedAttribute(value, nodeExtendedAttribute, 1);
    }

    public void testAddNodeExtendedAttributes() throws Exception {

        final String value = "expectedValue";
        final NodeExtendedAttribute nodeExtendedAttribute = new NodeExtendedAttribute(value, node, DynamicAttribute.DA_TYPE_STRUCT_O);

        // add 1
        Collection<NodeExtendedAttribute> newExtendedAttributes = new HashSet<NodeExtendedAttribute>();
        newExtendedAttributes.add(nodeExtendedAttribute);
        node.addNodeExtendedAttributes(newExtendedAttributes);
        assertNodeExtendedAttribute(value, nodeExtendedAttribute, 1);

        // add another
        newExtendedAttributes.clear();
        final String value2 = "expectedValue";
        final NodeExtendedAttribute nodeExtendedAttribute2 = new NodeExtendedAttribute(value2, node, DynamicAttribute.DA_TYPE_HTMLLINK_O);
        newExtendedAttributes.add(nodeExtendedAttribute2);
        node.addNodeExtendedAttributes(newExtendedAttributes);

        assertNodeExtendedAttribute(value, nodeExtendedAttribute, 2);
        assertNodeExtendedAttribute(value2, nodeExtendedAttribute2, 2);
    }

    public void testAddAttributeValue() throws Exception {

        final String value = "expectedValue";
        final DynamicAttribute dynamicAttribute = DynamicAttribute.DA_TYPE_TEXTAREA_O;
        final AttributeValue attributeValue = AttributeValue.create(value, node, dynamicAttribute);
        node.addAttributeValue(attributeValue);

        List<NodeExtendedAttribute> extendedAttributes = new ArrayList<NodeExtendedAttribute>(node.getExtendedAttributes());
        AttributeValuesCollection dynamicAttributeValues = node.getDynamicAttributeValues();

        assertEquals(1, extendedAttributes.size());
        assertEquals(1, dynamicAttributeValues.size());
        assertEquals(value, extendedAttributes.get(0).getValue());
        assertEquals(value, dynamicAttributeValues.get(dynamicAttribute).getValue());

        final String numberValue = "12345";
        final DynamicAttribute numberDynamicAttribute = DynamicAttribute.DA_TYPE_NUMBER_O;
        final AttributeValue numberAttributeValue = AttributeValue.create(numberValue, node, numberDynamicAttribute);
        node.addAttributeValue(numberAttributeValue);

        extendedAttributes = new ArrayList<NodeExtendedAttribute>(node.getExtendedAttributes());
        dynamicAttributeValues = node.getDynamicAttributeValues();
        assertEquals(2, extendedAttributes.size());
        assertEquals(2, dynamicAttributeValues.size());

        for (Iterator iterator = extendedAttributes.iterator(); iterator.hasNext();) {
            NodeExtendedAttribute nodeExtendedAttribute = (NodeExtendedAttribute) iterator.next();
            if (nodeExtendedAttribute.getDynamicAttribute().isNumericType()) {
                assertEquals(numberValue, nodeExtendedAttribute.getValue());
            } else {
                assertEquals(value, nodeExtendedAttribute.getValue());
            }
        }

        assertEquals(value, dynamicAttributeValues.get(dynamicAttribute).getValue());
        assertEquals(numberValue, dynamicAttributeValues.get(numberDynamicAttribute).getValue());
    }

    public void testRemoveAttributeValue() throws Exception {

        // add 2 attribute values to node and check set ok
        Collection<AttributeValue> attributeValues = new ArrayList<AttributeValue>();

        final String value = "expectedValue";
        final DynamicAttribute dynamicAttribute = DynamicAttribute.DA_TYPE_TEXTAREA_O;
        final AttributeValue attributeValue = AttributeValue.create(value, node, dynamicAttribute);
        attributeValues.add(attributeValue);

        final String numberValue = "12345";
        final DynamicAttribute numberDynamicAttribute = DynamicAttribute.DA_TYPE_NUMBER_O;
        final AttributeValue numberAttributeValue = AttributeValue.create(numberValue, node, numberDynamicAttribute);
        attributeValues.add(numberAttributeValue);

        node.addAttributeValues(attributeValues);
        List<NodeExtendedAttribute> extendedAttributes = new ArrayList<NodeExtendedAttribute>(node.getExtendedAttributes());
        AttributeValuesCollection dynamicAttributeValues = node.getDynamicAttributeValues();
        assertEquals(2, extendedAttributes.size());
        assertEquals(2, dynamicAttributeValues.size());

        for (Iterator iterator = extendedAttributes.iterator(); iterator.hasNext();) {
            NodeExtendedAttribute nodeExtendedAttribute = (NodeExtendedAttribute) iterator.next();
            if (nodeExtendedAttribute.getDynamicAttribute().isNumericType()) {
                assertEquals(numberValue, nodeExtendedAttribute.getValue());
            } else {
                assertEquals(value, nodeExtendedAttribute.getValue());
            }
        }

        assertEquals(value, dynamicAttributeValues.get(dynamicAttribute).getValue());
        assertEquals(numberValue, dynamicAttributeValues.get(numberDynamicAttribute).getValue());

        // remove last one and check 1st one is unaffected
        node.removeAttributeValue(numberAttributeValue);
        extendedAttributes = new ArrayList<NodeExtendedAttribute>(node.getExtendedAttributes());
        dynamicAttributeValues = node.getDynamicAttributeValues();
        assertEquals(1, extendedAttributes.size());
        assertEquals(1, dynamicAttributeValues.size());

        assertEquals(value, extendedAttributes.get(0).getValue());
        assertEquals(value, dynamicAttributeValues.get(dynamicAttribute).getValue());
    }

    public void testDynamicLineItemAttributeValue() throws Exception {

        final DynamicAttribute dynamicLineItemDynamicAttribute = new DynamicAttribute("dynamicLineItem", DynamicAttribute.DA_TYPE_NUMBER);
        dynamicLineItemDynamicAttribute.setDynamic(true);

        final Collection<NodeExtendedAttribute> nodeExtendedAttributes = new ArrayList<NodeExtendedAttribute>();
        final NodeExtendedAttribute nodeExtendedAttribute1 = new NodeExtendedAttribute("1", node, dynamicLineItemDynamicAttribute);
        nodeExtendedAttribute1.setDynamicPosition(new Integer(2));
        nodeExtendedAttributes.add(nodeExtendedAttribute1);

        final NodeExtendedAttribute nodeExtendedAttribute2 = new NodeExtendedAttribute("2", node, dynamicLineItemDynamicAttribute);
        nodeExtendedAttribute2.setDynamicPosition(new Integer(1));
        nodeExtendedAttributes.add(nodeExtendedAttribute2);
        node.addNodeExtendedAttributes(nodeExtendedAttributes);

        // check attribute value instantiated correctly
        final String expectedValue = "2,1";
        final AttributeValuesCollection dynamicAttributeValues = node.getDynamicAttributeValues();
        assertEquals(1, dynamicAttributeValues.size());
        final AttributeValue attributeValue = dynamicAttributeValues.get(dynamicLineItemDynamicAttribute);
        assertEquals(expectedValue, attributeValue.getValue());
        assertEquals(nodeExtendedAttributes.size(), attributeValue.getNodeExtendedAttributes().size());
    }

    public void testRemoveNodeExtendedAttribute() throws Exception {

        final String value = "expectedValue";
        final NodeExtendedAttribute nodeExtendedAttribute = new NodeExtendedAttribute(value, node, DynamicAttribute.DA_TYPE_STRUCT_O);

        final String value2 = "expectedValue";
        final NodeExtendedAttribute nodeExtendedAttribute2 = new NodeExtendedAttribute(value2, node, DynamicAttribute.DA_TYPE_HTMLLINK_O);

        // add 2
        node.addNodeExtendedAttribute(nodeExtendedAttribute);
        node.addNodeExtendedAttribute(nodeExtendedAttribute2);

        assertNodeExtendedAttribute(value, nodeExtendedAttribute, 2);
        assertNodeExtendedAttribute(value2, nodeExtendedAttribute2, 2);

        // remove 1st one
        node.removeNodeExtendedAttribute(nodeExtendedAttribute);
        assertNodeExtendedAttribute(value2, nodeExtendedAttribute2, 1);
    }

    public void testAddAttributeValues() throws Exception {

        // add 1
        Collection<AttributeValue> attributeValues = new ArrayList<AttributeValue>();
        final String value = "expectedValue";
        final DynamicAttribute dynamicAttribute = DynamicAttribute.DA_TYPE_TEXTAREA_O;
        final AttributeValue attributeValue = AttributeValue.create(value, node, dynamicAttribute);
        attributeValues.add(attributeValue);
        node.addAttributeValues(attributeValues);

        final AttributeValuesCollection dynamicAttributeValues = node.getDynamicAttributeValues();
        assertEquals(1, dynamicAttributeValues.size());

        final AttributeValue found = dynamicAttributeValues.get(dynamicAttribute);
        assertEquals(node, found.getNode());
        assertEquals(value, found.getValue());

        final Set extendedAttributes = node.getExtendedAttributes();
        assertEquals(1, extendedAttributes.size());
        final NodeExtendedAttribute nodeExtendedAttribute = (NodeExtendedAttribute) extendedAttributes.iterator().next();
        assertEquals(node, nodeExtendedAttribute.getNode());
        assertEquals(dynamicAttribute, nodeExtendedAttribute.getDynamicAttribute());
        assertEquals(value, nodeExtendedAttribute.getValue());
    }

    public void testRemoveAttributeValues() throws Exception {

        // add 2
        Collection<AttributeValue> attributeValues = new ArrayList<AttributeValue>();
        final String value = "expectedValue";
        final DynamicAttribute dynamicAttribute = DynamicAttribute.DA_TYPE_TEXTAREA_O;
        final AttributeValue attributeValue = AttributeValue.create(value, node, dynamicAttribute);
        attributeValues.add(attributeValue);

        final String value2 = "expectedValue2";
        final DynamicAttribute dynamicAttribute2 = DynamicAttribute.DA_TYPE_IMAGE_O;
        final AttributeValue attributeValue2 = AttributeValue.create(value2, node, dynamicAttribute2);
        attributeValues.add(attributeValue2);

        node.addAttributeValues(attributeValues);
        AttributeValuesCollection dynamicAttributeValues = node.getDynamicAttributeValues();
        assertEquals(2, dynamicAttributeValues.size());

        AttributeValue found = dynamicAttributeValues.get(dynamicAttribute);
        assertEquals(node, found.getNode());
        assertEquals(value, found.getValue());

        AttributeValue found2 = dynamicAttributeValues.get(dynamicAttribute2);
        assertEquals(node, found2.getNode());
        assertEquals(value2, found2.getValue());

        Set extendedAttributes = node.getExtendedAttributes();
        assertEquals(2, extendedAttributes.size());

        // remove first one
        attributeValues.remove(attributeValue2);
        node.removeAttributeValues(attributeValues);

        dynamicAttributeValues = node.getDynamicAttributeValues();
        assertEquals(1, dynamicAttributeValues.size());

        found = dynamicAttributeValues.get(dynamicAttribute);
        assertNull(found);

        found2 = dynamicAttributeValues.get(dynamicAttribute2);
        assertEquals(node, found2.getNode());
        assertEquals(value2, found2.getValue());

        extendedAttributes = node.getExtendedAttributes();
        assertEquals(1, extendedAttributes.size());

        final NodeExtendedAttribute nodeExtendedAttribute = (NodeExtendedAttribute) extendedAttributes.iterator().next();
        assertEquals(node, nodeExtendedAttribute.getNode());
        assertEquals(dynamicAttribute2, nodeExtendedAttribute.getDynamicAttribute());
        assertEquals(value2, nodeExtendedAttribute.getValue());
    }

    public void testAddOrUpdateAttributeValue() throws Exception {
        final String value = "expectedValue";
        final DynamicAttribute dynamicAttribute = DynamicAttribute.DA_TYPE_TEXTAREA_O;
        final AttributeValue attributeValue = AttributeValue.create(value, node, dynamicAttribute);
        node.addAttributeValue(attributeValue);

        AttributeValuesCollection dynamicAttributeValues = node.getDynamicAttributeValues();
        assertEquals(1, dynamicAttributeValues.size());

        AttributeValue found = dynamicAttributeValues.get(dynamicAttribute);
        assertEquals(node, found.getNode());
        assertEquals(value, found.getValue());

        final List<NodeExtendedAttribute> extendedAttributes = new ArrayList<NodeExtendedAttribute>(node.getExtendedAttributes());
        final NodeExtendedAttribute nodeExtendedAttribute = extendedAttributes.get(0);
        assertNodeExtendedAttribute(value, nodeExtendedAttribute, 1);

        // set new value
        final String newValue = "newValue";
        attributeValue.initialiseNodeExtendedAttributes(newValue);
        node.addOrUpdateAttributeValue(attributeValue);

        dynamicAttributeValues = node.getDynamicAttributeValues();
        assertEquals(1, dynamicAttributeValues.size());

        found = dynamicAttributeValues.get(dynamicAttribute);
        assertEquals(node, found.getNode());
        assertEquals(newValue, found.getValue());

        final NodeExtendedAttribute updatedNodeExtendedAttribute = extendedAttributes.get(0);
        assertEquals(nodeExtendedAttribute, updatedNodeExtendedAttribute);
        assertEquals(dynamicAttribute, updatedNodeExtendedAttribute.getDynamicAttribute());
        assertNodeExtendedAttribute(newValue, updatedNodeExtendedAttribute, 1);

        // clear value
        attributeValue.initialiseNodeExtendedAttributes("");
        node.addOrUpdateAttributeValue(attributeValue);
        dynamicAttributeValues = node.getDynamicAttributeValues();
        assertEquals(0, dynamicAttributeValues.size());
        assertNull(dynamicAttributeValues.get(dynamicAttribute));
    }

    public void testAddOrUpdateMultiAnswerAttributeValue() throws Exception {

        final String value = "expectedValue";
        final DynamicAttribute dynamicAttribute = DynamicAttribute.DA_TYPE_TEXTFIELD_O;
        dynamicAttribute.setDynamic(true);
        final AttributeValue attributeValue = AttributeValue.create(value, node, dynamicAttribute);
        node.addAttributeValue(attributeValue);

        AttributeValuesCollection dynamicAttributeValues = node.getDynamicAttributeValues();
        assertEquals(1, dynamicAttributeValues.size());

        AttributeValue found = dynamicAttributeValues.get(dynamicAttribute);
        assertEquals(node, found.getNode());
        assertEquals(value, found.getValue());

        List<NodeExtendedAttribute> extendedAttributes = new ArrayList<NodeExtendedAttribute>(node.getExtendedAttributes());
        final NodeExtendedAttribute nodeExtendedAttribute = extendedAttributes.get(0);
        assertNodeExtendedAttribute(value, nodeExtendedAttribute, 1);

        // set new value
        final String[] newValuesArray = new String[]{"newValue", "newValue2"};
        final String newValue = StringUtils.arrayToCommaDelimitedString(newValuesArray);
        attributeValue.initialiseNodeExtendedAttributes(newValue);
        attributeValue.getNodeExtendedAttribute(0).setDynamicPosition(new Integer(0));
        attributeValue.getNodeExtendedAttribute(1).setDynamicPosition(new Integer(1));
        node.addOrUpdateAttributeValue(attributeValue);

        // check values again
        extendedAttributes = new ArrayList<NodeExtendedAttribute>(node.getExtendedAttributes());
        assertEquals(2, extendedAttributes.size());
        dynamicAttributeValues = node.getDynamicAttributeValues();
        assertEquals(1, dynamicAttributeValues.size());

        found = dynamicAttributeValues.get(dynamicAttribute);
        assertEquals(node, found.getNode());
        assertEquals(newValue, found.getValue());

        final List nodeExtendedAttributes = found.getNodeExtendedAttributes();
        assertEquals(2, nodeExtendedAttributes.size());
        final NodeExtendedAttribute nodeExtendedAttribute1 = found.getNodeExtendedAttribute(0);
        final NodeExtendedAttribute nodeExtendedAttribute2 = found.getNodeExtendedAttribute(1);

        assertEquals(dynamicAttribute, nodeExtendedAttribute1.getDynamicAttribute());
        assertEquals(dynamicAttribute, nodeExtendedAttribute2.getDynamicAttribute());

        assertEquals(newValuesArray[0], nodeExtendedAttribute1.getValue());
        assertEquals(newValuesArray[1], nodeExtendedAttribute2.getValue());

        // clear value
        attributeValue.initialiseNodeExtendedAttributes("");
        node.addOrUpdateAttributeValue(attributeValue);
        dynamicAttributeValues = node.getDynamicAttributeValues();
        assertEquals(0, dynamicAttributeValues.size());
        assertNull(dynamicAttributeValues.get(dynamicAttribute));

        extendedAttributes = new ArrayList<NodeExtendedAttribute>(node.getExtendedAttributes());
        assertEquals(0, extendedAttributes.size());        
    }

    private void assertNodeExtendedAttribute(final String value, final NodeExtendedAttribute nodeExtendedAttribute, int expectedNodeExtendedAttributes) {

        // check extended attributes
        List<NodeExtendedAttribute> extendedAttributes = new ArrayList<NodeExtendedAttribute>(node.getExtendedAttributes());
        assertEquals(expectedNodeExtendedAttributes, extendedAttributes.size());
        final int index = extendedAttributes.indexOf(nodeExtendedAttribute);
        final NodeExtendedAttribute found = extendedAttributes.get(index);
        assertEquals(value, found.getValue());
        assertEquals(nodeExtendedAttribute.getDynamicAttribute(), found.getDynamicAttribute());
        assertEquals(nodeExtendedAttribute.getNode(), found.getNode());

        // check dynamic attributes
        AttributeValuesCollection dynamicAttributeValues = node.getDynamicAttributeValues();
        assertEquals(expectedNodeExtendedAttributes, dynamicAttributeValues.size());
        final AttributeValue attributeValue = dynamicAttributeValues.get(nodeExtendedAttribute.getDynamicAttribute());
        assertEquals(value, attributeValue.getValue());
        assertEquals(nodeExtendedAttribute.getDynamicAttribute(), attributeValue.getDynamicAttribute());
        assertEquals(nodeExtendedAttribute.getNode(), attributeValue.getNode());
    }

    public void testGetNodeType() throws Exception {

        final Long id = new Long(-99);
        Node pnode = new Position(id);
        assertEquals(Node.POSITION_UNIT_TYPE_, pnode.getNodeType());
    }

    private Node node;
}
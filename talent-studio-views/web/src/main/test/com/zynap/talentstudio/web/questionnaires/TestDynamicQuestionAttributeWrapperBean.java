package com.zynap.talentstudio.web.questionnaires;

/**
 * User: amark
 * Date: 04-Sep-2006
 * Time: 16:42:50
 */

import com.zynap.talentstudio.ZynapTestCase;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.questionnaires.QuestionAttribute;

import java.util.List;

public class TestDynamicQuestionAttributeWrapperBean extends ZynapTestCase {

    public void setUp() throws Exception {

        super.setUp();

        question = new QuestionAttribute();

        dynamicAttribute = new DynamicAttribute();
        dynamicAttribute.setLabel("test da");
        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_NUMBER);
        dynamicAttribute.setDynamic(true);
        question.setDynamicAttribute(dynamicAttribute);
    }

    public void testDynamicQuestionAttributeWrapperBeanNoNodeExtendedAttributes() throws Exception {

        final AttributeValue attributeValue = AttributeValue.create(dynamicAttribute);
        DynamicQuestionAttributeWrapperBean dynamicQuestionAttributeWrapperBean = new DynamicQuestionAttributeWrapperBean(question, attributeValue, 0);

        final List questionWrappers = dynamicQuestionAttributeWrapperBean.getQuestionWrappers();
        assertEquals(1, questionWrappers.size());
        final QuestionAttributeWrapperBean questionAttributeWrapperBean = (QuestionAttributeWrapperBean) questionWrappers.get(0);
        assertNotNull(questionAttributeWrapperBean);
    }

    public void testDynamicQuestionAttributeWrapperBean() throws Exception {

        final NodeExtendedAttribute nodeExtendedAttribute1 = new NodeExtendedAttribute(new Long(-1), "1", null, dynamicAttribute);
        nodeExtendedAttribute1.setDynamicPosition(new Integer(1));
        final NodeExtendedAttribute nodeExtendedAttribute2 = new NodeExtendedAttribute(new Long(-2), "2", null, dynamicAttribute);
        nodeExtendedAttribute2.setDynamicPosition(new Integer(2));
        final NodeExtendedAttribute nodeExtendedAttribute3 = new NodeExtendedAttribute(new Long(-3), "3", null, dynamicAttribute);
        nodeExtendedAttribute3.setDynamicPosition(new Integer(4));

        final AttributeValue attributeValue = AttributeValue.create(nodeExtendedAttribute1);
        attributeValue.addValue(nodeExtendedAttribute2, false);
        attributeValue.addValue(nodeExtendedAttribute3, false);

        final int maxItems = nodeExtendedAttribute3.getDynamicPosition().intValue();
        final DynamicQuestionAttributeWrapperBean dynamicQuestionAttributeWrapperBean = new DynamicQuestionAttributeWrapperBean(question, attributeValue, maxItems);
        assertEquals(dynamicAttribute, dynamicQuestionAttributeWrapperBean.getAttributeDefinition());
        assertEquals(attributeValue, dynamicQuestionAttributeWrapperBean.getAttributeValue());

        final List questionWrappers = dynamicQuestionAttributeWrapperBean.getQuestionWrappers();
        assertEquals(maxItems + 1, questionWrappers.size());

        QuestionAttributeWrapperBean questionAttributeWrapperBean = (QuestionAttributeWrapperBean) questionWrappers.get(0);
        assertNull(questionAttributeWrapperBean.getValue());
        assertEquals(0, questionAttributeWrapperBean.getDynamicPosition().intValue());

        questionAttributeWrapperBean = (QuestionAttributeWrapperBean) questionWrappers.get(1);
        assertEquals(nodeExtendedAttribute1.getValue(), questionAttributeWrapperBean.getValue());

        questionAttributeWrapperBean = (QuestionAttributeWrapperBean) questionWrappers.get(2);
        assertEquals(nodeExtendedAttribute2.getValue(), questionAttributeWrapperBean.getValue());

        questionAttributeWrapperBean = (QuestionAttributeWrapperBean) questionWrappers.get(3);
        assertNull(questionAttributeWrapperBean.getValue());
        assertEquals(3, questionAttributeWrapperBean.getDynamicPosition().intValue());

        questionAttributeWrapperBean = (QuestionAttributeWrapperBean) questionWrappers.get(4);
        assertEquals(nodeExtendedAttribute3.getValue(), questionAttributeWrapperBean.getValue());
    }

    public void testGetModifiedAttributeValue() throws Exception {

        final AttributeValue attributeValue = AttributeValue.create(dynamicAttribute);
        DynamicQuestionAttributeWrapperBean dynamicQuestionAttributeWrapperBean = new DynamicQuestionAttributeWrapperBean(question, attributeValue, 0);

        // add 2 - total will now be 3
        dynamicQuestionAttributeWrapperBean.addDynamicLineItem();
        dynamicQuestionAttributeWrapperBean.addDynamicLineItem();
        List questionWrappers = dynamicQuestionAttributeWrapperBean.getQuestionWrappers();
        assertEquals(3, questionWrappers.size());

        // set values on first and third
        final String value1 = "A";
        final String value2 = "B";
        final QuestionAttributeWrapperBean questionAttributeWrapperBean1 = (QuestionAttributeWrapperBean) questionWrappers.get(0);
        questionAttributeWrapperBean1.setValue(value1);
        final QuestionAttributeWrapperBean questionAttributeWrapperBean2 = (QuestionAttributeWrapperBean) questionWrappers.get(2);
        questionAttributeWrapperBean2.setValue(value2);

        // get modified value and check value and number of node extended attributes - will be 2 as second one was left blank
        final AttributeValue modifiedAttributeValue = dynamicQuestionAttributeWrapperBean.getModifiedAttributeValue();
        assertEquals("A,B", modifiedAttributeValue.getValue());
        final List nodeExtendedAttributes = modifiedAttributeValue.getNodeExtendedAttributes();
        assertEquals(2, nodeExtendedAttributes.size());

        // check details of node extended attribute
        assertNodeExtendedAttribute(nodeExtendedAttributes, 0, 0, value1);
        assertNodeExtendedAttribute(nodeExtendedAttributes, 1, 2, value2);
    }

    public void testAddDynamicLineItem() throws Exception {

        final AttributeValue attributeValue = AttributeValue.create(dynamicAttribute);
        DynamicQuestionAttributeWrapperBean dynamicQuestionAttributeWrapperBean = new DynamicQuestionAttributeWrapperBean(question, attributeValue, 0);

        // check initial size
        List questionWrappers = dynamicQuestionAttributeWrapperBean.getQuestionWrappers();
        assertNotNull(questionWrappers);
        assertEquals(1, questionWrappers.size());

        // add 1
        dynamicQuestionAttributeWrapperBean.addDynamicLineItem();
        questionWrappers = dynamicQuestionAttributeWrapperBean.getQuestionWrappers();
        assertEquals(2, questionWrappers.size());

        // check wrappers
        assertLineItem(questionWrappers, 0, 0);
        assertLineItem(questionWrappers, 1, 1);
    }

    public void testDeleteDynamicLineItem() throws Exception {

        final AttributeValue attributeValue = AttributeValue.create(dynamicAttribute);
        DynamicQuestionAttributeWrapperBean dynamicQuestionAttributeWrapperBean = new DynamicQuestionAttributeWrapperBean(question, attributeValue, 0);

        // add several and set value
        final int numToAdd = 5;
        for (int i = 0; i < numToAdd; i++) {
            dynamicQuestionAttributeWrapperBean.addDynamicLineItem();
            final int index = i + 1;
            ((AttributeWrapperBean) dynamicQuestionAttributeWrapperBean.getQuestionWrappers().get(index)).setValue(Integer.toString(index));
        }

        // check wrappers
        List questionWrappers = dynamicQuestionAttributeWrapperBean.getQuestionWrappers();
        assertEquals(numToAdd + 1, questionWrappers.size());
        for (int i = 0; i < numToAdd; i++) {
            assertLineItem(questionWrappers, i, i);
        }

        // remove second to last and then second
        dynamicQuestionAttributeWrapperBean.deleteDynamicLineItem(new Long(4));
        dynamicQuestionAttributeWrapperBean.deleteDynamicLineItem(new Long(1));

        // check wrappers again
        questionWrappers = dynamicQuestionAttributeWrapperBean.getQuestionWrappers();
        int expectedNumber = 4;
        assertEquals(expectedNumber, questionWrappers.size());
        assertLineItem(questionWrappers, 0, 0);
        assertLineItem(questionWrappers, 1, 1);
        assertLineItem(questionWrappers, 2, 2);
        assertLineItem(questionWrappers, 3, 3);

        // check modified value - will only have 3 values because first wrapper has no value
        final AttributeValue modifiedAttributeValue = dynamicQuestionAttributeWrapperBean.getModifiedAttributeValue();
        final List nodeExtendedAttributes = modifiedAttributeValue.getNodeExtendedAttributes();
        assertEquals(3, nodeExtendedAttributes.size());
        assertNodeExtendedAttribute(nodeExtendedAttributes, 0, 1, "2");
        assertNodeExtendedAttribute(nodeExtendedAttributes, 1, 2, "3");
        assertNodeExtendedAttribute(nodeExtendedAttributes, 2, 3, "5");
    }

    private void assertNodeExtendedAttribute(List nodeExtendedAttributes, int position, int dynamicPosition, String expectedValue) {

        final NodeExtendedAttribute nodeExtendedAttribute = (NodeExtendedAttribute) nodeExtendedAttributes.get(position);
        assertEquals(dynamicAttribute, nodeExtendedAttribute.getDynamicAttribute());
        assertEquals(expectedValue, nodeExtendedAttribute.getValue());
        assertEquals(new Integer(dynamicPosition), nodeExtendedAttribute.getDynamicPosition());
    }

    private void assertLineItem(List questionWrappers, int position, int dynamicPosition) {

        final QuestionAttributeWrapperBean questionAttributeWrapperBean = (QuestionAttributeWrapperBean) questionWrappers.get(position);
        assertEquals(dynamicAttribute, questionAttributeWrapperBean.getAttributeDefinition());
        final AttributeValue newAttributeValue = questionAttributeWrapperBean.getAttributeValue();
        assertEquals(new Integer(dynamicPosition), questionAttributeWrapperBean.getDynamicPosition());
        assertNotNull(newAttributeValue);
    }

    private QuestionAttribute question;
    private DynamicAttribute dynamicAttribute;
}
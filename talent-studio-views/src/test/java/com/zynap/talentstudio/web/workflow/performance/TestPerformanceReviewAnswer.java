package com.zynap.talentstudio.web.workflow.performance;

/**
 * User: amark
 * Date: 16-Oct-2006
 * Time: 16:25:12
 */

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.CoreDetail;
import com.zynap.talentstudio.ZynapTestCase;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;

public class TestPerformanceReviewAnswer extends ZynapTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        final DynamicAttribute dynamicAttribute = new DynamicAttribute("number", DynamicAttribute.DA_TYPE_NUMBER);
        dynamicAttribute.setDynamic(true);
        attributeValue = AttributeValue.create(dynamicAttribute);
        attributeValue.addValue(new NodeExtendedAttribute("1", dynamicAttribute), true);
        attributeValue.addValue(new NodeExtendedAttribute("2", dynamicAttribute), true);

        role = new LookupValue("Z", "z role", "desc", true, false, 2, null);
        user = new User();
        user.setCoreDetail(new CoreDetail("firstname", "lastname"));
        performanceReviewAnswer = new PerformanceReviewAnswer(user, role, attributeValue);
    }

    public void testGetDisplayValue() throws Exception {
        final String[] displayValue = performanceReviewAnswer.getDisplayValue();
        assertEquals(2, displayValue.length);
        assertEquals(attributeValue.getNodeExtendedAttribute(0).getValue(), displayValue[0]);
        assertEquals(attributeValue.getNodeExtendedAttribute(1).getValue(), displayValue[1]);
    }

    public void testGetRole() throws Exception {
        assertEquals(role.getLabel(), performanceReviewAnswer.getRole());
    }

    public void testGetUserName() throws Exception {
        assertEquals(user.getLabel(), performanceReviewAnswer.getUserName());
    }

    private PerformanceReviewAnswer performanceReviewAnswer;
    private LookupValue role;
    private User user;
    private AttributeValue attributeValue;
}
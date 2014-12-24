package com.zynap.talentstudio.security.rules;
/**
* Class or Interface description.
*
* @author syeoh
* @since 13-Dec-2006 16:09:07
* @version 0.1
*/

import junit.framework.TestCase;

public class TestRule extends TestCase {

    public void testEquals() throws Exception {
        Rule testRule = new Rule();
        testRule.setActive(true);
        testRule.setDescription("somedescription");
        testRule.setType("sometype");
        testRule.setClazz("someclazz");
        testRule.setLabel("somelabel");
        testRule.setId(new Long(123));

        Rule otherRule = new Rule();
        otherRule.setActive(testRule.isActive());
        otherRule.setDescription(testRule.getDescription());
        otherRule.setType(testRule.getType());
        otherRule.setClazz(testRule.getClazz());
        otherRule.setLabel(testRule.getLabel());
        otherRule.setId(testRule.getId());

        assertEquals(testRule, otherRule);
        assertFalse(testRule == otherRule);

        otherRule.setMaxValue(new Integer(12));

        assertEquals(testRule, otherRule);

        otherRule.setId(new Long(-33));
        assertFalse(testRule.equals(otherRule));
    }
}



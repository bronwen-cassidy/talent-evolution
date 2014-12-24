/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.common.lookups;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TestLookupType extends TestCase {

    protected void setUp() throws Exception {
        lookupType = new LookupType("testing", "test lookup type");
    }

    public void testGetLookupValue() throws Exception {

        lookupType.setLookupValues(createLookupValues(7));

        LookupValue newLookupValue = createNewLookupValue(1);
        LookupValue actual = lookupType.getLookupValue(newLookupValue.getId());
        assertEquals(newLookupValue, actual);
    }

    public void testGetLookupValueEmpty() throws Exception {

        lookupType.setLookupValues(new ArrayList<LookupValue>());
        LookupValue actual = lookupType.getLookupValue(new Long(5));
        assertNull(actual);
    }

    public void testAddLookupValue() throws Exception {

        LookupValue newLookupValue = createNewLookupValue(5);
        lookupType.addLookupValue(newLookupValue);

        LookupValue actual = lookupType.getLookupValue(newLookupValue.getId());
        assertEquals(newLookupValue, actual);
    }

    public void testGetActiveLookupValues() throws Exception {

        // add 2 lookup values - 1 active 1 inactive
        LookupValue activeLookupValue = createNewLookupValue(1);
        lookupType.addLookupValue(activeLookupValue);

        LookupValue inactiveLookupValue = createNewLookupValue(2);
        inactiveLookupValue.setActive(false);
        lookupType.addLookupValue(inactiveLookupValue);

        // collection of lookup values should contain both
        assertEquals(2, lookupType.getLookupValues().size());

        // collection of active lookup values should contain active one only
        final Collection activeLookupValues = lookupType.getActiveLookupValues();
        assertEquals(1, activeLookupValues.size());
        assertTrue(activeLookupValues.contains(activeLookupValue));
    }

    private List<LookupValue> createLookupValues(int qty) {

        List<LookupValue> list = new ArrayList<LookupValue>();
        for(int i = 0; i < qty; i++) {
            LookupValue lookupValue = createNewLookupValue(i);
            lookupValue.setId(new Long(i));
            list.add(lookupValue);
        }

        return list;
    }

    private LookupValue createNewLookupValue(int i) {

        LookupValue lookupValue = new LookupValue("VAL_" + i, lookupType.getTypeId(), "label", "description");
        lookupValue.setId(new Long(i));

        return lookupValue;
    }

    private LookupType lookupType;
}
package com.zynap.talentstudio.common.lookups;


import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.AbstractHibernateTestCase;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * User: amark
 * Date: 14-Jun-2005
 * Time: 12:10:22
 */
public class TestLookupManager extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        lookupManager = (ILookupManager) applicationContext.getBean("lookupManager");
    }

    public void testFindEditableLookupTypes() throws Exception {

        LookupType newLookupType = getNewLookupType();
        newLookupType.setActive(false);
        lookupManager.createLookupType(ROOT_USER_ID, newLookupType);

        final List modifiableLookupTypes = lookupManager.findEditableLookupTypes();
        boolean found = false;
        for (int i = 0; i < modifiableLookupTypes.size(); i++) {
            LookupType lookupType = (LookupType) modifiableLookupTypes.get(i);
            assertFalse(lookupType.isUneditable());
            if (!found) found = lookupType.equals(newLookupType);
        }

        assertTrue("New lookup type not found", found);
    }

    public void testFindActiveLookupTypes() throws Exception {

        // add inactive lookup type
        LookupType newLookupType = getNewLookupType();
        newLookupType.setActive(false);
        lookupManager.createLookupType(ROOT_USER_ID, newLookupType);

        // check that it is not in the list of active lookup types
        final List activeLookupTypes = lookupManager.findActiveLookupTypes();
        assertFalse(activeLookupTypes.isEmpty());
        assertFalse(activeLookupTypes.contains(newLookupType));
    }

    public void testFindLookupType() throws Exception {

        final LookupType lookupType = lookupManager.findLookupType(ILookupManager.LOOKUP_TYPE_TITLE);
        assertNotNull(lookupType);
        assertEquals(ILookupManager.LOOKUP_TYPE_TITLE, lookupType.getTypeId());
        assertFalse(lookupType.isUserDefined());
    }

    public void testFindInvalidLookupType() throws Exception {
        try {
            lookupManager.findLookupType("-342323");
            fail("Should have throw LookupTypeNotFoundException");
        } catch (LookupTypeNotFoundException expected) {

        }
    }

    public void testFindInactiveLookupType() throws Exception {

        // add inactive lookup type
        LookupType newLookupType = getNewLookupType();
        newLookupType.setActive(false);
        lookupManager.createLookupType(ROOT_USER_ID, newLookupType);

        // test find works even if look up type is inactive
        final LookupType lookupType = lookupManager.findLookupType(newLookupType.getTypeId());
        assertFalse(lookupType.isActive());
        assertEquals(newLookupType, lookupType);
    }

    public void testCreateLookupType() throws Exception {

        LookupType newLookupType = getNewLookupType();
        lookupManager.createLookupType(ROOT_USER_ID, newLookupType);

        assertNotNull(newLookupType.getTypeId());
        assertTrue(newLookupType.isUserDefined());
        assertTrue(newLookupType.isActive());
        assertFalse(newLookupType.isUneditable());
    }

    public void testUpdateLookupType() throws Exception {

        LookupType newLookupType = getNewLookupType();
        lookupManager.createLookupType(ROOT_USER_ID, newLookupType);

        // inactivate and update
        newLookupType.setActive(false);
        lookupManager.updateLookupType(ROOT_USER_ID, newLookupType);

        // check not in list of active lookup types
        final List activeLookupTypes = lookupManager.findActiveLookupTypes();
        assertFalse(activeLookupTypes.contains(newLookupType));
    }

    public void testCreateLookupValue() throws Exception {

        // create new lookup value
        LookupValue newLookupValue = getNewLookupValue();
        lookupManager.createLookupValue(ROOT_USER_ID, newLookupValue);

        // check lookup value
        LookupValue expected = lookupManager.findLookupValue(newLookupValue.getId());
        assertEquals(expected, newLookupValue);
        assertEquals(newLookupValue.getLookupType(), expected.getLookupType());

        // check is active and not system
        assertTrue(newLookupValue.isActive());
        assertFalse(newLookupValue.isSystem());
    }

    public void testCreateLookupValueTwoSameLabel() throws Exception {
        LookupValue newLookupValue = getNewLookupValue();
        newLookupValue.setLabel("5");
        LookupValue newLookupVale2 = getNewLookupValue();
        newLookupVale2.setLookupType(lookupManager.findLookupType(ILookupManager.LOOKUP_TYPE_CLASSIFICATION));
        newLookupVale2.setLabel("5");

        lookupManager.createLookupValue(ROOT_USER_ID, newLookupValue);

        try {
            lookupManager.createLookupValue(ROOT_USER_ID, newLookupVale2);
            //commitAndStartNewTx();
        } catch (Exception e) {
            e.printStackTrace();
            fail("should not have thrown an exception");
        }
    }

    public void testCreateInactiveLookupValue() throws Exception {

        // create new inactive lookup value
        LookupValue newLookupValue = getNewLookupValue();
        newLookupValue.setActive(false);
        lookupManager.createLookupValue(ROOT_USER_ID, newLookupValue);

        // check lookup value
        LookupValue expected = lookupManager.findLookupValue(newLookupValue.getId());
        assertEquals(expected, newLookupValue);
        assertEquals(newLookupValue.getLookupType(), expected.getLookupType());

        // check is active and not system
        assertFalse(newLookupValue.isActive());
        assertFalse(newLookupValue.isSystem());
    }

    public void testUpdateLookupValue() throws Exception {

        // create new lookup value
        LookupValue newLookupValue = getNewLookupValue();
        lookupManager.createLookupValue(ROOT_USER_ID, newLookupValue);

        // change value id
        newLookupValue.setValueId("Lord");
        lookupManager.updateLookupValue(ROOT_USER_ID, newLookupValue);

        // check has been changed
        LookupValue expected = lookupManager.findLookupValue(newLookupValue.getId());
        assertEquals(expected.getValueId(), newLookupValue.getValueId());
    }

    public void testDeleteLookupValue() throws Exception {

        // create new lookup value
        LookupValue newLookupValue = getNewLookupValue();
        lookupManager.createLookupValue(ROOT_USER_ID, newLookupValue);

        // delete
        lookupManager.deleteLookupValue(newLookupValue.getId());

        // check no longer found
        try {
            lookupManager.findLookupValue(newLookupValue.getId());
            fail("Incorrectly found deleted lookup value");
        } catch (LookupValueNotFoundException expected) {

        }
    }

    public void testHasValues() throws Exception {
        LookupType result = lookupManager.findLookupType(ILookupManager.LOOKUP_TYPE_TITLE);
        Collection values = result.getLookupValues();
        assertFalse(values.isEmpty());
    }

    public void testFindActiveLookupValues() throws Exception {

        final LookupType newLookupType = getNewLookupType();
        lookupManager.createLookupType(ROOT_USER_ID, newLookupType);

        // add two active and 1 inactive lookup values

        // sort order is 3 so will be last in sorted list
        final LookupValue firstNewLookupValue = new LookupValue("value1", "label1", "desc1", true, false, 3, newLookupType);
        lookupManager.createLookupValue(ROOT_USER_ID, firstNewLookupValue);

        // this is the inactive lookup value
        final LookupValue secondNewLookupValue = new LookupValue("value2", "label2", "desc2", false, false, 2, newLookupType);
        lookupManager.createLookupValue(ROOT_USER_ID, secondNewLookupValue);

        // sort order is 1 so will be first in sorted list
        final LookupValue thirdNewLookupValue = new LookupValue("value3", "label3", "desc3", true, false, 1, newLookupType);
        lookupManager.createLookupValue(ROOT_USER_ID, thirdNewLookupValue);

        final List activeLookupValues = lookupManager.findActiveLookupValues(newLookupType.getTypeId());
        assertEquals(2, activeLookupValues.size());
        assertTrue(activeLookupValues.contains(firstNewLookupValue));
        assertTrue(activeLookupValues.contains(thirdNewLookupValue));

        // check order in list - third lookup value comes 1st, then first comes 2nd due to sort order above
        final Iterator iterator = activeLookupValues.iterator();
        assertEquals(thirdNewLookupValue, iterator.next());
        assertEquals(firstNewLookupValue, iterator.next());
    }

    public void testFindPrimaryPositionAssociationQualifier() throws Exception {

        final LookupType lookupType = lookupManager.findLookupType(ILookupManager.LOOKUP_TYPE_PRIMARY_POSITION_ASSOC);
        final LookupValue lookupValue = (LookupValue) lookupType.getLookupValues().iterator().next();

        final LookupValue found = lookupManager.findPositionAssociationQualifier(lookupValue.getValueId());
        assertLookupValue(lookupValue, found);
    }

    public void testFindSecondaryPositionAssociationQualifier() throws Exception {

        final LookupType lookupType = lookupManager.findLookupType(ILookupManager.LOOKUP_TYPE_SECONDARY_POSITION_ASSOC);
        final LookupValue lookupValue = (LookupValue) lookupType.getLookupValues().iterator().next();

        final LookupValue found = lookupManager.findPositionAssociationQualifier(lookupValue.getValueId());
        assertLookupValue(lookupValue, found);
    }

    public void testFindSubjectAssociationQualifier() throws Exception {

        final LookupType lookupType = lookupManager.findLookupType(ILookupManager.LOOKUP_TYPE_PRIMARY_SUBJECT_ASSOC);
        final LookupValue lookupValue = (LookupValue) lookupType.getLookupValues().iterator().next();

        final LookupValue found = lookupManager.findSubjectAssociationQualifier(lookupValue.getValueId());
        assertLookupValue(lookupValue, found);
    }

    public void testFindLookupValue() throws Exception {

        final LookupType lookupType = lookupManager.findLookupType(ILookupManager.LOOKUP_TYPE_PRIMARY_POSITION_ASSOC);
        final LookupValue lookupValue = (LookupValue) lookupType.getLookupValues().iterator().next();

        final LookupValue found = lookupManager.findLookupValue(lookupValue.getId());
        assertLookupValue(lookupValue, found);
    }

    public void testFindInvalidLookupValue() throws Exception {

        try {
            lookupManager.findLookupValue(new Long(-342323));
            fail("Should have throw LookupValueNotFoundException");
        } catch (LookupValueNotFoundException expected) {

        }
    }

    public void testFindLookupValueByType() throws Exception {

        final LookupType lookupType = lookupManager.findLookupType(ILookupManager.LOOKUP_TYPE_TITLE);
        final LookupValue lookupValue = (LookupValue) lookupType.getLookupValues().iterator().next();

        final LookupValue found = lookupManager.findLookupValue(lookupValue.getValueId(), lookupType.getTypeId());
        assertLookupValue(lookupValue, found);
    }

    private void assertLookupValue(final LookupValue expected, final LookupValue found) {
        assertEquals(expected,  found);
        assertEquals(expected.getLookupType(), found.getLookupType());
    }

    private LookupType getNewLookupType() {
        return new LookupType("test", "test");
    }

    private LookupValue getNewLookupValue() throws TalentStudioException {
        LookupType lookupType = lookupManager.findLookupType(ILookupManager.LOOKUP_TYPE_TITLE);
        return new LookupValue("DOCTOR", "doctor", "title for doctor", lookupType);
    }

    private ILookupManager lookupManager;
}
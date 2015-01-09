/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.util;

import com.zynap.domain.IDomainObject;
import com.zynap.talentstudio.ZynapTestCase;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.positions.Position;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TestIDomainUtils extends ZynapTestCase {

    public void testHasValidIdNullObject() throws Exception {
        assertFalse(IDomainUtils.hasValidId(null));
    }

    public void testHasValidIdNullId() throws Exception {
        Node node = new Position();
        assertFalse(IDomainUtils.hasValidId(node));
    }

    public void testHasValidId() throws Exception {
        Node node = new Position(new Long(1));
        assertTrue(IDomainUtils.hasValidId(node));
    }

    public void testIsValidIdNullId() throws Exception {
        assertFalse(IDomainUtils.isValidId(null));
    }

    public void testIsValidIdUsingConstant() throws Exception {
        assertFalse(IDomainUtils.isValidId(IDomainObject.UNASSIGNED_VALUE));
    }

    public void testIsValidId() throws Exception {
        assertTrue(IDomainUtils.isValidId(new Long(10)));
    }

    public void testFindNullDomainObject() throws Exception {
        Collection domainObjects = new ArrayList();
        assertNull(IDomainUtils.findDomainObject(domainObjects, new Long(-1)));
    }

    public void testFindDomainObject() throws Exception {
        Collection domainObjects = new ArrayList();
        domainObjects.add(DEFAULT_ORG_UNIT);
        domainObjects.add(DEFAULT_POSITION);

        assertEquals(DEFAULT_POSITION, IDomainUtils.findDomainObject(domainObjects, DEFAULT_POSITION_ID));
    }
}
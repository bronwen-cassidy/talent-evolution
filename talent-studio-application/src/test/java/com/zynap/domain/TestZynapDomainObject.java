/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.domain;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 13-Aug-2008 16:13:59
 * @version 0.1
 */

import junit.framework.*;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.organisation.subjects.SubjectDTO;
import com.zynap.talentstudio.organisation.positions.Position;

public class TestZynapDomainObject extends TestCase {

    public void testEquals() throws Exception {
        ZynapDomainObject zynapDomainObject = new Subject();
        zynapDomainObject.setId(new Long(22));
        assertTrue(zynapDomainObject.equals(new SubjectDTO(new Long(22), "", "", "", "")));
    }

    public void testEqualsNot() throws Exception {
        ZynapDomainObject zynapDomainObject = new Subject();
        zynapDomainObject.setId(new Long(22));
        ZynapDomainObject position = new Position(new Long(22));
        assertFalse(zynapDomainObject.equals(position));
    }
}
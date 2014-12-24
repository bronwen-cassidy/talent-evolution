/**
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.util.collections;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 06-Jun-2005 11:05:28
 * @version ${VERSION}
 */

import junit.framework.TestCase;

import com.zynap.talentstudio.CoreDetail;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.subjects.Subject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TestDomainObjectCollectionHelper extends TestCase {

    public void testFindById() throws Exception {
        Node expected = new Position(new Long(4), "position title_4");
        Node actual = (Node) DomainObjectCollectionHelper.findById(createPositionNodes(), new Long(4));
        assertEquals(expected, actual);
    }

    public void testFindByIdNotFound() throws Exception {
        Node actual = (Node) DomainObjectCollectionHelper.findById(createPositionNodes(), new Long(7));
        assertNull(actual);
    }

    public void testFindByIdNullCollection() throws Exception {
        Node actual = (Node) DomainObjectCollectionHelper.findById(null, new Long(7));
        assertNull(actual);
    }

    public void testFindByIdEmptyCollection() throws Exception {
        Node actual = (Node) DomainObjectCollectionHelper.findById(new ArrayList(), new Long(7));
        assertNull(actual);
    }

    public void testFindByIdNullId() throws Exception {
        try {
            DomainObjectCollectionHelper.findById(createPositionNodes(), null);
            fail("should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testFindByLabel() throws Exception {
        Node expected = new Subject(new Long(4), new CoreDetail("first_4", "second_4"));
        Node actual = (Node) DomainObjectCollectionHelper.findByLabel(createSubjectNodes(), "first_4 second_4");
        assertEquals(expected, actual);
    }

    private Collection createPositionNodes() {
        List result = new ArrayList();
        for(int i = 0; i < 5; i++) {
            result.add(new Position(new Long(i), "position title_" + i));
        }
        return result;
    }

    private Collection createSubjectNodes() {
        List result = new ArrayList();
        for(int i = 0; i < 5; i++) {
            result.add(new Subject(new Long(i), new CoreDetail("first_" + i, "second_" + i)));
        }
        return result;
    }
}
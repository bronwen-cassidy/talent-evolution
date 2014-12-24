/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.organisation;
/**
 * Class or Interface description.
 *
 * @author taulant bajraktari
 * @since 31-Mar-2009 12:17:28
 * @version 0.1
 */

import junit.framework.TestCase;

import com.zynap.domain.orgbuilder.SubjectSearchQuery;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.web.organisation.subjects.SubjectSearchQueryWrapper;
import com.zynap.talentstudio.web.organisation.subjects.SubjectWrapperBean;

import java.util.LinkedList;

public class TestBrowseNodeWrapper extends TestCase {


    protected void setUp() throws Exception {
        super.setUp();
        for (int id : ids) {
            result.add(new Node(new Long(id)));
        }
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        result.clear();
    }

    public void testRemoveNode() throws Exception {

        final SubjectSearchQueryWrapper queryWrapper = new SubjectSearchQueryWrapper(new SubjectSearchQuery());

        final SubjectWrapperBean subjectWrapperBean = new SubjectWrapperBean(new Subject(new Long(12), null));
        BrowseNodeWrapper browseNodeWrapper = new SubjectBrowseNodeWrapper(queryWrapper, result, subjectWrapperBean);
        assertEquals(browseNodeWrapper.getCurrentNodes().size(), 8);
        Node node;

        node = (Node) browseNodeWrapper.getCurrentNodes().get(0);
        assertNotNull(node);
        browseNodeWrapper.removeNode(new Long(1));
        node = (Node) browseNodeWrapper.getCurrentNodes().get(0);
        assertTrue(node.getId() != 1);
        browseNodeWrapper.removeNode(new Long(3));
        assertEquals(browseNodeWrapper.getCurrentNodes().size(), 6);
        browseNodeWrapper.removeNode(new Long(6));
        assertEquals(browseNodeWrapper.getCurrentNodes().size(), 5);
        browseNodeWrapper.removeNode(new Long(-1));
        browseNodeWrapper.removeNode(new Long(3));
        assertEquals(browseNodeWrapper.getCurrentNodes().size(), 5);

    }

    private int[] ids = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
    private LinkedList result = new LinkedList();
}
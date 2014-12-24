/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.utils;

import com.zynap.talentstudio.CoreDetail;
import com.zynap.talentstudio.common.SelectionNode;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.security.roles.AccessRole;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TestSelectionNodeHelper extends TestCase {

	public void testCreateSelections() throws Exception {
		Collection x = createNodes();
		Collection y = SelectionNodeHelper.createSelections(x);
		for (Iterator iterator = y.iterator(); iterator.hasNext();) {
			Object o = iterator.next();
			assertTrue(o instanceof SelectionNode);
			assertTrue(((SelectionNode) o).getValue() instanceof Long);
		}
	}

	public void testCreateDomainObjectSelections() throws Exception {
		Collection actual = createAssignRoleSetup();

		assertEquals(4, actual.size());
		int trueCount = 0;
		int falseCount = 0;
		for (Iterator iterator = actual.iterator(); iterator.hasNext();) {
			SelectionNode selectionNode = (SelectionNode) iterator.next();
			if (selectionNode.isSelected()) {
				trueCount++;
			} else {
				falseCount++;
			}
		}

		assertEquals(2, trueCount);
		assertEquals(2, falseCount);
	}

	public void testEnableDomainObjectSelections() throws Exception {
		Collection actual = createAssignRoleSetup();
		Long[] roleIds = new Long[]{new Long(22), new Long(19)};
		List result = new ArrayList();
		SelectionNodeHelper.enableDomainObjectSelections(actual, roleIds, result);
		assertEquals(2, result.size());
		assertEquals(4, actual.size());
		int trueCount = 0;
		int falseCount = 0;
		for (Iterator iterator = actual.iterator(); iterator.hasNext();) {
			SelectionNode selectionNode = (SelectionNode) iterator.next();
			if (selectionNode.isSelected()) {
				trueCount++;
			} else {
				falseCount++;
			}
		}

		assertEquals(2, trueCount);
		assertEquals(2, falseCount);
	}

	private Collection createNodes() {
		Collection x = new ArrayList();
		x.add(new Subject(new Long(9), new CoreDetail("test", "test1")));
		x.add(new Subject(new Long(7), new CoreDetail("test3", "test4")));
		return x;
	}

	public void testEnableSelections() throws Exception {
		List contentTypes = new ArrayList();
		contentTypes.add(new SelectionNode("CV", "Curric"));
		contentTypes.add(new SelectionNode("TR", "Train"));
		contentTypes.add(new SelectionNode("MS", "Man"));
		contentTypes.add(new SelectionNode("ST", "Stand"));
		contentTypes.add(new SelectionNode("QT", "Quiet"));
		List result = new ArrayList();
		SelectionNodeHelper.enableSelections(contentTypes, new String[]{"CV", "QT"}, result);
		assertEquals(2, result.size());
		assertEquals("CV", ((SelectionNode) result.get(0)).getValue());
		assertEquals("QT", ((SelectionNode) result.get(1)).getValue());
	}

	private Collection createAssignRoleSetup() {
		List list = new ArrayList();
		list.add(new AccessRole(new Long(22), "Role 1", "description", true, true, null));
		list.add(new AccessRole(new Long(21), "Role 2", "description", true, true, null));
		list.add(new AccessRole(new Long(20), "Role 3", "description", true, true, null));
		list.add(new AccessRole(new Long(19), "Role 4", "description", true, true, null));

		List existing = new ArrayList();
		existing.add(new AccessRole(new Long(20), "Role 3", "description", true, true, null));
		existing.add(new AccessRole(new Long(19), "Role 4", "description", true, true, null));
		Collection actual = SelectionNodeHelper.createDomainObjectSelections(list, existing);
		return actual;
	}

	public void testDisableSelections() throws Exception {
		List contentTypes = new ArrayList();
		contentTypes.add(new SelectionNode("CV", "Curric", true));
		contentTypes.add(new SelectionNode("TR", "Train", true));
		contentTypes.add(new SelectionNode("MS", "Man", true));

		SelectionNodeHelper.disableSelections(contentTypes);
		assertAllFalse(contentTypes);
	}

	public void testHasSelectedItems() throws Exception {
		List contentTypes = new ArrayList();
		contentTypes.add(new SelectionNode("CV", "Curric"));
		contentTypes.add(new SelectionNode("TR", "Train"));
		contentTypes.add(new SelectionNode("MS", "Man"));

		assertFalse(SelectionNodeHelper.hasSelectedItems(contentTypes));

		contentTypes.add(new SelectionNode("Test", "Test", true));
		assertTrue(SelectionNodeHelper.hasSelectedItems(contentTypes));		
	}

	private void assertAllFalse(Collection collection) {
		for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
			SelectionNode selectionNode = (SelectionNode) iterator.next();
			assertEquals(false, selectionNode.isSelected());
		}
	}
}

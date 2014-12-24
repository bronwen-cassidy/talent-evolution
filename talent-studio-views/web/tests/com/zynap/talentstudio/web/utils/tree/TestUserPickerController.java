/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.utils.tree;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 11-Sep-2006 12:15:51
 * @version 0.1
 */

import com.zynap.talentstudio.web.utils.ZynapDbUnitMockControllerTestCase;

import java.util.List;

public class TestUserPickerController extends ZynapDbUnitMockControllerTestCase {

    protected String getDataSetFileName() {
        return "users-test-data.xml";
    }

    protected void setUp() throws Exception {
        super.setUp();
        userPickerController = (UserPickerController) getBean("userPickerController");
    }

    public void testGetTree() throws Exception {
        TreeWrapperBean treeWrapperBean = (TreeWrapperBean) userPickerController.formBackingObject(mockRequest);
        List tree = treeWrapperBean.getTree();
        Branch branch = (Branch) tree.get(0);
        assertNotNull(branch);
        assertFalse(branch.getChildren().isEmpty());
        // get the first child should be all persons with last names between a-f
        Branch childBranch = (Branch) branch.getChildren().get(0);
        assertEquals(2, childBranch.getLeaves().size());
    }

    private UserPickerController userPickerController;
}
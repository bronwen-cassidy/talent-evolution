/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.utils.tree;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.talentstudio.security.users.UserDTO;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 11-Sep-2006 09:03:06
 */
public class UserPickerController extends PickerController {

    public UserPickerController() {
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    protected TreeWrapperBean getTree(Map<String, Branch> branches, String popupId, HttpServletRequest request) throws TalentStudioException {
        List<UserDTO> allActiveUsers = userService.listAppUsers();
        String[] branchIds = {"A-E", "F-J", "K-O", "P-T", "U-Z"};
        char[][] nameBounds = {{'A', 'F'}, {'F', 'K'}, {'K', 'P'}, {'P', 'U'}, {'U', 'Z'}};
        return UserTreeHelper.buildTree(allActiveUsers, getLeafIcon(), branchIds, nameBounds);
    }

    protected void findBranchByLeafId(TreeWrapperBean treeWrapperBean, String leafId, HttpServletRequest request) throws TalentStudioException {

    }

    protected void updateBranch(Branch branch, TreeWrapperBean treeWrapperBean, HttpServletRequest request) throws TalentStudioException {

    }

    private IUserService userService;
}

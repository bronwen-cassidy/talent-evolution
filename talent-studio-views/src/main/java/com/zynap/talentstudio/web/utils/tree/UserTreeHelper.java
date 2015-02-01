/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.utils.tree;

import com.zynap.talentstudio.security.users.UserDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 31-Jul-2007 16:39:47
 */
public class UserTreeHelper {


    public static TreeWrapperBean buildTree(List<UserDTO> allActiveUsers, String leafIcon, String[] branchIds, char[][] branchIdBounds) {

        Map<String, Branch> branches = new HashMap<String, Branch>();
        // sort the users alphabetically
        Collections.sort(allActiveUsers, new Comparator<UserDTO>() {
            public int compare(UserDTO o1, UserDTO o2) {
                String secondName1 = o1.getSecondName().toUpperCase();
                String secondName2 = o2.getSecondName().toUpperCase();
                return secondName1.compareTo(secondName2);
            }
        });

        List<Branch> userBranches = createTree(allActiveUsers, branches, branchIds, branchIdBounds);
        return new TreeWrapperBean(userBranches, branches, "userTree", leafIcon);
    }

    private static List<Branch> createTree(List<UserDTO> users, Map<String, Branch> branches, String[] branchIds, char[][] nameBounds) {
        List<UserDTO> result = new ArrayList<UserDTO>(users);
        // create the users branch
        final String rootId = "Users";
        Branch rootBranch = new Branch(rootId, rootId);
        branches.put(rootId, rootBranch);

        for (int i = 0; i < branchIds.length; i++) {
            String branchId = branchIds[i];
            Branch branch = new Branch(branchId, branchId);
            branches.put(branchId, branch);
            rootBranch.addChild(branch);
            char[] bounds = nameBounds[i];
            result = appendLeaves(result, branch, bounds[0], bounds[1]);
        }

        List<Branch> userBranches = new ArrayList<Branch>();
        userBranches.add(rootBranch);
        return userBranches;
    }

    private static List<UserDTO> appendLeaves(List<UserDTO> users, Branch parent, char greaterThanCharacter, char lessThanCharacter) {
        List<UserDTO> temp = new ArrayList<UserDTO>(users);

        for (Iterator<UserDTO> iterator = temp.iterator(); iterator.hasNext();) {
            UserDTO user = iterator.next();
            if (user.isActive()) {
                String secondName = user.getSecondName();
                String firstName = user.getFirstName();
                
                final char value = secondName.toUpperCase().charAt(0);
                if(isGreaterThanOrEquals(value, greaterThanCharacter) && isLessThan(value, lessThanCharacter)) {
                    parent.addLeaf(new Leaf(user.getId().toString(), secondName + ", " + firstName));
                    iterator.remove();
                }
            }
        }
        return temp;
    }

    private static boolean isGreaterThanOrEquals(char firstLetter, char greaterThanOrEquals) {
        Character first = new Character(firstLetter);
        Character last = new Character(greaterThanOrEquals);
        return first.compareTo(last) >= 0;
    }

    private static boolean isLessThan(char firstLetter, char lessThanCharacter) {
        final Character character = new Character(firstLetter);
        final Character lessThan = new Character(lessThanCharacter);

        if(lessThanCharacter == 'Z') return character.compareTo(lessThan) <= 0;
        return character.compareTo(lessThan) < 0;
    }
}

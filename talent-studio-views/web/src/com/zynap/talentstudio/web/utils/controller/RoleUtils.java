/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.utils.controller;

import com.zynap.talentstudio.security.roles.Role;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 20-Jun-2007 13:48:12
 */
public class RoleUtils {

    public static void clearRoles(Collection accessRoles) {

        for (Iterator iterator = accessRoles.iterator(); iterator.hasNext();) {
            Role role = (Role) iterator.next();
            role.setAssigned(false);
        }
    }

    public static List initRoles(Collection<Role> allRoles, Collection<Role> assignedRoles) {

        List<Role> accessRoles = new ArrayList<Role>();
        if (allRoles != null) {
            for (Iterator iterator = allRoles.iterator(); iterator.hasNext();) {
                Role role = (Role) iterator.next();
                if (assignedRoles.contains(role)) {
                    role.setAssigned(true);
                }
                accessRoles.add(role);
            }
        }
        return accessRoles;
    }

    public static void sortRoles(List<Role> roles) {

        Collections.sort(roles, new Comparator<Role>() {
            public int compare(Role role1, Role role2) {

                final String label1 = role1.getLabel();
                final String label2 = role2.getLabel();

                return label1.compareToIgnoreCase(label2);
            }
        });
    }

    public static void updateAssigned(Long[] roleIds, Collection<Role> accessRoles) {
        // mark ones with ids in array as assigned
        for (int i = 0; i < roleIds.length; i++) {
            final Long roleId = roleIds[i];
            Role target = (Role) CollectionUtils.find(accessRoles, new Predicate() {
                public boolean evaluate(Object o) {
                    Role role = (Role) o;
                    if (role.getId().equals(roleId)) {
                        role.setAssigned(true);
                        return true;
                    }
                    return false;
                }
            });

            if (target != null) {
                target.setAssigned(true);
            }
        }
    }

    public static void removeAdminAssignableRoles(List<Role> roleList) {

        final Iterator<Role> roleIterator = roleList.iterator();
        while (roleIterator.hasNext()) {
            Role role = roleIterator.next();
            if (role.isAdminRole()) {
                roleIterator.remove();
            }
        }
    }

    public static boolean hasAdministrationRole(Collection<Role> roleList) {

        final Iterator<Role> roleIterator = roleList.iterator();
        while (roleIterator.hasNext()) {
            Role role = roleIterator.next();
            if (role.isAdministrationRole()) {
                return true;
            }
        }
        return false;
    }

}

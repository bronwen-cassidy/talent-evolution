/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.security.roles;

import com.zynap.talentstudio.security.permits.AccessPermit;
import com.zynap.talentstudio.security.permits.DomainObjectPermit;
import com.zynap.talentstudio.security.permits.IPermit;

import java.util.ArrayList;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class RoleTestUtils {

    public static List createResourceRightsList() {
        List<IPermit> resourcePermissions = new ArrayList<IPermit>();
        // write indicates both read and write permission is given
        final String[] actions = new String[]{"write", "read"};
        List<IPermit> cvRights = createResourcePermission("CV", actions, "Curriculum Vitae");
        List<IPermit> assementRights = createResourcePermission("TR", actions, "Training Records");
        List<IPermit> emailRights = createResourcePermission("WP", actions, "Work Products");
        List<IPermit> reviewRights = createResourcePermission("WC", actions, "Written Correspondence");
        resourcePermissions.addAll(cvRights);
        resourcePermissions.addAll(assementRights);
        resourcePermissions.addAll(emailRights);
        resourcePermissions.addAll(reviewRights);
        return resourcePermissions;
    }

    public static List createAccessRightsList() {
        List<IPermit> accessPermissions = new ArrayList<IPermit>();
        final String[] actions = new String[]{"add", "edit", "view", "external"};
        List<IPermit> companyPermissions = createAccessPermission("Company", actions);
        List<IPermit> subjectPermissions = createAccessPermission("Subject", actions);
        List<IPermit> templatePermissions = createAccessPermission("Template", actions);

        accessPermissions.addAll(companyPermissions);
        accessPermissions.addAll(subjectPermissions);
        accessPermissions.addAll(templatePermissions);

        return accessPermissions;
    }

    public static List<IPermit> createAccessPermission(String componentName, String[] actions) {
        List<IPermit> componentPermissions = new ArrayList<IPermit>();
        for (int i = 0; i < actions.length; i++) {
            String action = actions[i];
            IPermit permit = new AccessPermit();
            permit.setObjectName(componentName);
            permit.setAction(action);
            permit.setLabel(componentName);
            componentPermissions.add(permit);
        }
        return componentPermissions;
    }

    public static List<IPermit> createResourcePermission(String componentName, String[] actions, String label) {
        List<IPermit> componentPermissions = new ArrayList<IPermit>();
        for (int i = 0; i < actions.length; i++) {
            String action = actions[i];
            IPermit permit = new DomainObjectPermit();
            permit.setObjectName(componentName);
            permit.setAction(action);
            permit.setLabel(label);
            // all resources by default have read/write access
            permit.setActive(true);
            componentPermissions.add(permit);
        }

        return componentPermissions;
    }
}

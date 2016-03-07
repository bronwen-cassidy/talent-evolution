package com.zynap.talentstudio.web.security.domain;

/**
 * User: amark
 * Date: 17-Mar-2005
 * Time: 11:47:43
 */

import junit.framework.TestCase;

import com.zynap.talentstudio.common.SelectionNode;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.security.SecurityDomain;
import com.zynap.talentstudio.security.areas.Area;
import com.zynap.talentstudio.security.roles.ResourceRole;
import com.zynap.talentstudio.security.roles.Role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class TestSecurityDomainWrapperBean extends TestCase {

    public void testGetModifiedSecurityDomain() throws Exception {

        final String label = "label";
        final String comments = "comments";

        SecurityDomain securityDomain = new SecurityDomain(null, false, label, comments, false, null, null, null);
        securityDomainWrapperBean = new SecurityDomainWrapperBean(securityDomain);
        securityDomainWrapperBean.setActive(true);
        securityDomainWrapperBean.setExclusive(true);
        securityDomainWrapperBean.setComments(null);

        // check that modifications to security domain are reflected
        final SecurityDomain modifiedSecurityDomain = securityDomainWrapperBean.getModifiedSecurityDomain();
        assertEquals(securityDomain.getLabel(), modifiedSecurityDomain.getLabel());
        assertTrue(modifiedSecurityDomain.isActive());
        assertTrue(modifiedSecurityDomain.isExclusive());
        assertNull(modifiedSecurityDomain.getComments());
    }

    public void testClearRoles() throws Exception {

        final String label = "label";
        final String comments = "comments";

        SecurityDomain securityDomain = new SecurityDomain(null, false, label, comments, false, null, null, null);
        securityDomainWrapperBean = new SecurityDomainWrapperBean(securityDomain);
        List<Role> roles = new ArrayList<Role>();
        for (int i = 0; i < 3; i++) {
            final ResourceRole newRole = new ResourceRole(new Long(i), "role" + i, null, true, true, null);
            roles.add(newRole);
        }
        securityDomainWrapperBean.setRoles(roles);

        // clear roles
        securityDomainWrapperBean.setRoleIds(new Long[0]);
        assertTrue(securityDomainWrapperBean.getModifiedSecurityDomain().getRoles().isEmpty());
    }

    public void testGetRoles() throws Exception {

        final String label = "label";
        final String comments = "comments";

        SecurityDomain securityDomain = new SecurityDomain(null, false, label, comments, false, null, null, null);
        securityDomainWrapperBean = new SecurityDomainWrapperBean(securityDomain);
        List<Role> roles = new ArrayList<Role>();
        for (int i = 0; i < 3; i++) {
            final ResourceRole newRole = new ResourceRole(new Long(i), "role" + i, null, true, true, null);
            roles.add(newRole);
        }
        securityDomainWrapperBean.setRoles(roles);

        // set one role
        final Long roleId = new Long(0);
        securityDomainWrapperBean.setRoleIds(new Long[]{roleId});
        final Set modifiedRoles = securityDomainWrapperBean.getModifiedSecurityDomain().getRoles();
        assertFalse(modifiedRoles.isEmpty());
        final ResourceRole foundRole = (ResourceRole) modifiedRoles.iterator().next();
        assertEquals(roleId, foundRole.getId());

        // check that only the selected role is assigned
        checkForRole(roleId);

    }

    public void testResetIds() throws Exception {

        final String label = "label";
        final String comments = "comments";

        Long domainId = new Long(-999);

        SecurityDomain securityDomain = new SecurityDomain(domainId, false, label, comments, false, null, null, null);
        securityDomainWrapperBean = new SecurityDomainWrapperBean(securityDomain);

        List<Area> areas = new ArrayList<>();
        final Area area = new Area();
        Long areaId = -999L;
        area.setId(areaId);
        areas.add(area);

        securityDomainWrapperBean.setAreas(areas);
        securityDomainWrapperBean.setAreaId(areaId);

        final SecurityDomain modifiedSecurityDomain = securityDomainWrapperBean.getModifiedSecurityDomain();
        securityDomainWrapperBean.resetIds();
        assertNull(modifiedSecurityDomain.getId());
        assertNotNull(modifiedSecurityDomain.getArea().getId());
    }

    public void testAssignedRoles() throws Exception {

        final String label = "label";
        final String comments = "comments";

        Set<Role> assignedRoles = new LinkedHashSet<Role>();
        final Long roleId = new Long(0);
        assignedRoles.add(new ResourceRole(roleId, "role0", null, true, true, null));

        SecurityDomain securityDomain = new SecurityDomain(null, false, label, comments, false, null, null, assignedRoles);
        securityDomainWrapperBean = new SecurityDomainWrapperBean(securityDomain);

        List<Role> roles = new ArrayList<Role>();
        for (int i = 0; i < 3; i++) {
            final ResourceRole newRole = new ResourceRole(new Long(i), "role" + i, null, true, true, null);
            roles.add(newRole);
        }
        securityDomainWrapperBean.setRoles(roles);

        checkForRole(roleId);
    }

    private void checkForRole(final Long roleId) {
        final Collection currentRoles = securityDomainWrapperBean.getRoles();
        for (Iterator iterator = currentRoles.iterator(); iterator.hasNext();) {
            SelectionNode selectionNode = (SelectionNode) iterator.next();
            if (((Role) selectionNode.getValue()).getId().equals(roleId)) {
                assertTrue(selectionNode.isSelected());
            } else {
                assertFalse(selectionNode.isSelected());
            }
        }
    }

    SecurityDomainWrapperBean securityDomainWrapperBean;
}
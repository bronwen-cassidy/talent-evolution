package com.zynap.talentstudio.util;

/**
 * User: amark
 * Date: 05-Apr-2006
 * Time: 16:28:35
 */

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.ZynapTestCase;
import com.zynap.talentstudio.organisation.NodeAudit;
import com.zynap.talentstudio.organisation.OrganisationUnit;

import java.util.Date;

public class TestAuditUtils extends ZynapTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        node = new OrganisationUnit();
        user = new User(new Long(10));
    }


    public void testSetNodeAudit() throws Exception {

        final User user2 = new User(new Long(-789));

        node.setId(new Long(-99));
        node.setNodeAudit(new NodeAudit(new Date(), node, user2.getId()));
        AuditUtils.setNodeAudit(node, user);

        final NodeAudit newAudit = node.getNodeAudit();
        assertEquals(newAudit.getCreatedById(), user2.getId());
        assertNotNull(newAudit.getCreated());
        assertEquals(newAudit.getUpdatedById(), user.getId());
        assertNotNull(newAudit.getUpdated());
        assertEquals(newAudit.getNode(), node);
        assertEquals(newAudit.getNodeId(), node.getId());
    }

    public void testSetNewNodeAudit() throws Exception {

        AuditUtils.setNodeAudit(node, user);

        final NodeAudit newAudit = node.getNodeAudit();
        assertEquals(newAudit.getCreatedById(), user.getId());
        assertNotNull(newAudit.getCreated());
        assertNull(newAudit.getUpdated());
        assertNull(newAudit.getUpdatedById());
    }

    public void testSetFirstNodeAuditForExistingNode() throws Exception {

        node.setId(new Long(-99));        
        AuditUtils.setNodeAudit(node, user);

        final NodeAudit newAudit = node.getNodeAudit();
        assertEquals(newAudit.getCreatedById(), user.getId());
        assertNotNull(newAudit.getCreated());
        assertNull(newAudit.getUpdated());
        assertNull(newAudit.getUpdatedById());
    }

    private OrganisationUnit node;
    private User user;
}
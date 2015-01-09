package com.zynap.talentstudio.security.permits;

/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */

import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.util.FormatterFactory;
import com.zynap.talentstudio.common.SecurityConstants;
import com.zynap.talentstudio.organisation.Node;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class TestHibernatePermitManagerDao extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {

        super.setUp();
        hibernatePermitManagerDao = (HibernatePermitManagerDao) applicationContext.getBean("permitManDao");
    }

    public void testGetActiveAccessPermits() throws Exception {
        hibernatePermitManagerDao.getActiveAccessPermits();
    }

    public void testGetActiveDomainObjectPermits() throws Exception {
        hibernatePermitManagerDao.getActiveDomainObjectPermits();        
    }

    public void testGetPermit() throws Exception {
        IPermit domainPermit = hibernatePermitManagerDao.getPermit(SecurityConstants.POSITION_CONTENT, SecurityConstants.VIEW_ACTION);
        assertEquals(SecurityConstants.VIEW_ACTION, domainPermit.getAction());
        assertEquals(SecurityConstants.POSITION_CONTENT, domainPermit.getContent());
        assertTrue(domainPermit instanceof DomainObjectPermit);
    }

    public void testGetPermitSearchSubjects() throws Exception {
        IPermit domainPermit = hibernatePermitManagerDao.getPermit(SecurityConstants.SUBJECT_CONTENT, SecurityConstants.VIEW_ACTION);
        assertNotNull(domainPermit);
    }

    public void testGetActivePermits() throws Exception {
        List activePermits = hibernatePermitManagerDao.getActivePermits();

        boolean accessPermitsFound = false;
        boolean domainObjectPermitsFound = false;
        for (Iterator iterator = activePermits.iterator(); iterator.hasNext();) {
            IPermit permit = (IPermit) iterator.next();
            if (permit instanceof AccessPermit) {
                accessPermitsFound = true;
            } else if (permit instanceof DomainObjectPermit) {
                domainObjectPermitsFound = true;
            }
        }

        assertTrue("No access permits found", accessPermitsFound);
        assertTrue("No domain object permits found", domainObjectPermitsFound);
    }

    public void testGetNodeIds() throws Exception {
        final String[] nodeTypes = new String[]{Node.POSITION_UNIT_TYPE_, Node.SUBJECT_UNIT_TYPE_};
        final Collection nodeIds = hibernatePermitManagerDao.getNodeIds(ROOT_USER_ID, nodeTypes, SecurityConstants.VIEW_ACTION);
        assertNotNull(nodeIds);
        assertTrue(nodeIds.contains(DEFAULT_POSITION_ID));
    }

    public void testGetPermits() throws Exception {
        final Collection permits = hibernatePermitManagerDao.getPermits(SecurityConstants.RUN_ACTION, SecurityConstants.REPORTS_CONTENT);
        assertFalse(permits.isEmpty());

        for (Iterator iterator = permits.iterator(); iterator.hasNext();) {
            Permit permit = (Permit) iterator.next();
            assertEquals(SecurityConstants.RUN_ACTION, permit.getAction());
            assertEquals(SecurityConstants.REPORTS_CONTENT, permit.getContent());
            assertNotNull(permit.getId());
        }
    }

    public void testGetAccessPermits() throws Exception {
        hibernatePermitManagerDao.getAccessPermits(ADMINISTRATOR_USER_ID);
    }

    public void testGetContentTypePermits() throws Exception {
        hibernatePermitManagerDao.getContentTypePermits(ADMINISTRATOR_USER_ID, SecurityConstants.VIEW_ACTION, SecurityConstants.POSITION_CONTENT);
    }

    HibernatePermitManagerDao hibernatePermitManagerDao;
}

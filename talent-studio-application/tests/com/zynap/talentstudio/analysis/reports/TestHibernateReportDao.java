/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports;

import com.zynap.talentstudio.AbstractHibernateTestCase;

import java.util.Collection;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TestHibernateReportDao extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        hibernateReportDao = (HibernateReportDao) applicationContext.getBean("reportDao");
    }

    public void testFindAll() throws Exception {
        Collection reports = hibernateReportDao.findAll(ROOT_USER_ID, Report.TABULAR_REPORT);
        assertNotNull(reports);
    }

    public void testGetDomainObjectClass() throws Exception {
        assertEquals(Report.class, hibernateReportDao.getDomainObjectClass());
    }

    private HibernateReportDao hibernateReportDao;
}
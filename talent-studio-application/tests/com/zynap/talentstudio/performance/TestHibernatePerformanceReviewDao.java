/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.performance;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 05-Dec-2006 09:26:41
 * @version 0.1
 */

import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;

import java.util.Collection;

public class TestHibernatePerformanceReviewDao extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        hibernatePerformanceReviewDao = (HibernatePerformanceReviewDao) applicationContext.getBean("performanceReviewDao");
    }

    public void testGetManagers() throws Exception {
        QuestionnaireWorkflow questionnaireWorkflow = new QuestionnaireWorkflow();
        questionnaireWorkflow.setId(new Long(13));
        Collection results = hibernatePerformanceReviewDao.getManagers(questionnaireWorkflow);
        assertNotNull(results);
    }

    private HibernatePerformanceReviewDao hibernatePerformanceReviewDao;
}
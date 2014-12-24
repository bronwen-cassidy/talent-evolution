/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.security.rules;

import com.zynap.talentstudio.AbstractHibernateTestCase;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TestHibernateConfigRuleDao extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        hibernateConfigRuleDao = (HibernateConfigRuleDao) applicationContext.getBean("configRuleDao");
    }

    public void testGetDomainObjectClass() throws Exception {
        assertEquals(Config.class, hibernateConfigRuleDao.getDomainObjectClass());
    }

    public void testUpdatePasswordConfig() throws Exception {
        Config expected = (Config) hibernateConfigRuleDao.findByID(Config.PASSWORD_CONFIG_ID);
        Rule rule = expected.getRule(Rule.PASSWORD_NUMBER_OF_UNIQUE);
        rule.setValue("5");
        hibernateConfigRuleDao.update(expected);

        Config actual = (Config) hibernateConfigRuleDao.findByID(expected.getId());
        Rule actualRule = actual.getRule(Rule.PASSWORD_NUMBER_OF_UNIQUE);
        assertEquals(rule, actualRule);
    }

    public void testUpdateUsernameConfig() throws Exception {
        Config expected = (Config) hibernateConfigRuleDao.findByID(Config.USERNAME_CONFIG_ID);
        Rule rule = expected.getRule(Rule.USERNAME_MAX_LENGTH);
        rule.setValue("20");
        hibernateConfigRuleDao.update(expected);

        Config actual = (Config) hibernateConfigRuleDao.findByID(expected.getId());
        Rule actualRule = actual.getRule(Rule.USERNAME_MAX_LENGTH);
        assertEquals(rule, actualRule);
    }


    private HibernateConfigRuleDao hibernateConfigRuleDao;
}
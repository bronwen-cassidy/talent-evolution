/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.security.rules;

import junit.framework.AssertionFailedError;

import com.zynap.talentstudio.AbstractHibernateTestCase;

import java.util.Collection;
import java.util.Iterator;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TestConfigRuleService extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        configRuleService = (IConfigRuleService) applicationContext.getBean("configRuleService");
    }

    public void testFindAllConfigs() throws Exception {
        Collection configs = configRuleService.findAll();
        for (Iterator it = configs.iterator(); it.hasNext();) {
            Config config = (Config) it.next();
            boolean passed = false;
            assertNotNull(config);
            // Add your new configs here.
            try {
                assertEquals(Config.PASSWORD_CONFIG_ID, config.getId());
                passed = true;
                continue;
            } catch (AssertionFailedError ae) {
                //do nothing until all tests have failed i.e. passed = false
            }
            try {
                assertEquals(Config.USERNAME_CONFIG_ID, config.getId());
                passed = true;
                continue;
            } catch (AssertionFailedError ae) {
                //do nothing until all tests have failed i.e. passed = false
            }
            try {
                assertEquals(Config.AUTHORISATION_CONFIG_ID, config.getId());
                passed = true;
                continue;
            } catch (AssertionFailedError ae) {
                //do nothing until all tests have failed i.e. passed = false
            }
            if (!passed) {
                throw new AssertionFailedError("Not all configs were found.");
            }
        }
    }

    public void testFindPasswordConfig() throws Exception {
        Config config = configRuleService.findById(Config.PASSWORD_CONFIG_ID);
        assertNotNull(config);
        assertEquals(Config.PASSWORD_CONFIG_ID, config.getId());
    }

    public void testFindUserNameConfig() throws Exception {
        Config config = configRuleService.findById(Config.USERNAME_CONFIG_ID);
        assertNotNull(config);
        assertEquals(Config.USERNAME_CONFIG_ID, config.getId());
    }

    public void testUpdateUsernameRules() throws Exception {
        Config config = configRuleService.findById(Config.USERNAME_CONFIG_ID);
        Collection rules = config.getRules();
        assertEquals(3, rules.size());
        for (Iterator iterator = rules.iterator(); iterator.hasNext();) {
            Rule rule = (Rule) iterator.next();

            switch (((Long) rule.getId()).intValue()) {
                case MIN_USERNAME_LENGTH:
                    rule.setValue("22");
                    break;
                case MAX_USERNAME_LENGTH:
                    rule.setValue("2");
                    break;
                case USERNAME_ALPHA_NUMBERIC:
                    rule.setValue(rule.getValue().equals("F") ? "T" : "F");
                    break;
            }
        }
        configRuleService.update(config);

        Config actual = configRuleService.findById(Config.USERNAME_CONFIG_ID);
        for (Iterator iterator = actual.getRules().iterator(); iterator.hasNext();) {
            Rule rule = (Rule) iterator.next();
            switch (((Long) rule.getId()).intValue()) {
                case MIN_USERNAME_LENGTH:
                    assertEquals("22", rule.getValue());
                    break;
                case MAX_USERNAME_LENGTH:
                    assertEquals("2", rule.getValue());
                    break;
                case USERNAME_ALPHA_NUMBERIC:
                    assertNotNull(rule.getValue());
                    break;
            }
        }
    }

    public void testFindById() throws Exception {
        Config config = configRuleService.findById(Config.USERNAME_CONFIG_ID);
        assertNotNull(config);
    }

    private IConfigRuleService configRuleService;
    public static final int MIN_USERNAME_LENGTH = -20;
    public static final int MAX_USERNAME_LENGTH = -21;
    public static final int USERNAME_ALPHA_NUMBERIC = -22;
}
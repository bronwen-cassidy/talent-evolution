/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.security.rules;

import com.zynap.talentstudio.security.rules.Config;
import com.zynap.talentstudio.security.rules.Rule;
import com.zynap.talentstudio.web.AbstractValidatorTestCase;
import com.zynap.util.spring.BindUtils;
import com.zynap.talentstudio.web.controller.ZynapDefaultFormController;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TestConfigRuleValidator extends AbstractValidatorTestCase {

    protected void setUp() throws Exception {
        configRuleValidator = new ConfigRuleValidator();
        configRuleWrapper = new ConfigRuleWrapper();
        binder = new DataBinder(configRuleWrapper, ZynapDefaultFormController.DEFAULT_COMMAND_NAME);
    }

    protected void tearDown() throws Exception {
        configRuleValidator = null;
        configRuleWrapper = null;
        binder = null;
    }

    public void testSupportsSuccess() throws Exception {
        assertTrue(configRuleValidator.supports(configRuleWrapper.getClass()));
    }

    public void testSupportsFailed() throws Exception {
        assertFalse(configRuleValidator.supports(binder.getClass()));
    }

    public void testValidateSuccess() throws Exception {
        Config config = new Config(new Long(-1), "test label", "validation test");
        Collection<Rule> rules = new ArrayList<Rule>();
        createPasswordRules(rules);
        config.setRules(rules);
        configRuleWrapper.setTargetConfig(config);
        MutablePropertyValues pvs = BindUtils.createPropertyValues(new String[]{"targetConfig.rules[0].value", "targetConfig.rules[1].value",
                                                                                "targetConfig.rules[2].value", "targetConfig.rules[3].value",
                                                                                "targetConfig.rules[4].value", "targetConfig.rules[5].value"},
                new Object[]{"22", "7", "11", "F", "F", "4"});
        binder.bind(pvs);
        Errors errors = binder.getBindingResult();
        configRuleValidator.validate(configRuleWrapper, errors);
        assertFalse(errors.hasErrors());
    }

    public void testValidateNumberFailure() throws Exception {

        Config config = new Config(new Long(-1), "test label", "validation test");
        Collection<Rule> rules = new ArrayList<Rule>();
        createPasswordRules(rules);
        config.setRules(rules);
        configRuleWrapper.setTargetConfig(config);
        MutablePropertyValues pvs = BindUtils.createPropertyValues(new String[]{"targetConfig.rules[0].value", "targetConfig.rules[1].value",
                                                                                "targetConfig.rules[2].value", "targetConfig.rules[3].value",
                                                                                "targetConfig.rules[4].value", "targetConfig.rules[5].value"},
                new Object[]{"22", "7", "11", "F", "F", "yes"});
        binder.bind(pvs);
        Errors errors = binder.getBindingResult();
        configRuleValidator.validate(configRuleWrapper, errors);
        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getErrorCount());
        assertEquals("error.settings.field.is.required", errors.getFieldError("targetConfig.rules[5].value").getCode());
    }

    public void testValidateNumberRange() throws Exception {

        Config config = new Config(new Long(-1), "test label", "validation test");
        List<Rule> rules = new ArrayList<Rule>();
        createPasswordRules(rules);
        config.setRules(rules);
        configRuleWrapper.setTargetConfig(config);

        final Rule rule1 = rules.get(0);
        rule1.setValue(Long.toString(Long.MIN_VALUE));

        final Rule rule2 = rules.get(1);
        rule2.setValue(Long.toString(Long.MAX_VALUE));

        Errors errors = binder.getBindingResult();
        configRuleValidator.validate(configRuleWrapper, errors);
        assertTrue(errors.hasErrors());
        assertEquals(2, errors.getErrorCount());
        final FieldError fieldError = errors.getFieldError("targetConfig.rules[0].value");
        assertEquals("error.settings.field.is.required", fieldError.getCode());
    }

    public void testValidateFailsEmptyField() throws Exception {
        Config config = new Config(new Long(-1), "test label", "validation test");
        Collection<Rule> rules = new ArrayList<Rule>();
        createPasswordRules(rules);
        config.setRules(rules);
        configRuleWrapper.setTargetConfig(config);
        MutablePropertyValues pvs = BindUtils.createPropertyValues(new String[]{"targetConfig.rules[0].value", "targetConfig.rules[1].value",
                                                                                "targetConfig.rules[2].value", "targetConfig.rules[3].value",
                                                                                "targetConfig.rules[4].value", "targetConfig.rules[5].value"},
                new Object[]{"22", "7", "11", "F", "F", ""});
        binder.bind(pvs);
        Errors errors = binder.getBindingResult();
        configRuleValidator.validate(configRuleWrapper, errors);
        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getErrorCount());
        assertEquals("error.settings.field.is.required", errors.getFieldError("targetConfig.rules[5].value").getCode());
    }

    private void createPasswordRules(Collection<Rule> rules) {
        rules.add(new Rule(new Long(-10), "Password Expiry", "Days before passwords expire", Rule.NUMBER_TYPE, "20", true));
        rules.add(new Rule(new Long(-11), "Min Password Length", "Min Length", Rule.NUMBER_TYPE, "5", true));
        rules.add(new Rule(new Long(-12), "Max Password Length", "Max Length", Rule.NUMBER_TYPE, "10", true));

        rules.add(new Rule(new Long(-13), "Password Characters", "Chars", Rule.BOOLEAN_TYPE, "T", true));
        rules.add(new Rule(new Long(-14), "Force Password Change", "Force password change on first login", Rule.BOOLEAN_TYPE, "T", true));
        rules.add(new Rule(new Long(-15), "Password Reuse", "Reuse", Rule.NUMBER_TYPE, "3", true));
    }


    private DataBinder binder;
    private ConfigRuleValidator configRuleValidator;
    private ConfigRuleWrapper configRuleWrapper;
}
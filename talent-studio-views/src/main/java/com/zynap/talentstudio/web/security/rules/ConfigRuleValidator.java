/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.security.rules;

import com.zynap.talentstudio.security.rules.Rule;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Iterator;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class ConfigRuleValidator implements Validator {

    public boolean supports(Class clazz) {
        return (ConfigRuleWrapper.class.isAssignableFrom(clazz));
    }

    public void validate(Object obj, Errors errors) {
            ConfigRuleWrapper wrapper = (ConfigRuleWrapper) obj;
        final Collection rules = wrapper.getTargetConfig().getRules();
        int index = 0;
        for (Iterator iterator = rules.iterator(); iterator.hasNext(); index++) {
            Rule rule = (Rule) iterator.next();
            final String value = rule.getValue();

            final String field = "targetConfig.rules[" + index + "].value";
            if(!(StringUtils.hasText(value))) {
                errors.rejectValue(field, "error.settings.field.is.required", "Please enter a number.");
                wrapper.getTargetConfig().getRule(rule.getId()).setValue(null);
                continue;
            }

            if(rule.isNumber()) {
                if (!org.apache.commons.lang.StringUtils.isNumeric(value)) {
                    errors.rejectValue(field, "error.settings.field.is.required", "Please enter a number.");
                    wrapper.getTargetConfig().getRule(rule.getId()).setValue(null);
                    continue;
                }
                int intValue;
                try {
                    intValue = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    errors.rejectValue(field, "error.settings.field.is.numeric", "Please enter a number greater than zero.");
                    continue;
                }
                if ((rule.getMinValue() != null && intValue < rule.getMinValue().intValue()) || (rule.getMaxValue() != null && intValue > rule.getMaxValue().intValue())) {
                    //need dynamic message here so couldn't use message.properties
                    errors.rejectValue(field, "error.max.logins.field.out.of.range", new Object[] {rule.getMinValue(), rule.getMaxValue()}, "Please enter a number in the range 3 to 9");
                }
            }
        }
    }
}

/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.attributes;

import com.zynap.talentstudio.calculations.Calculation;
import com.zynap.talentstudio.calculations.Expression;
import com.zynap.talentstudio.calculations.DateCalculation;
import com.zynap.talentstudio.organisation.attributes.DaDefinitionValidationFactory;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.InvalidDynamicAttributeException;
import com.zynap.talentstudio.organisation.attributes.validators.IDaSpecification;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class DynamicAttributeValidator implements Validator {

    public boolean supports(Class clazz) {
        return DynamicAttributeWrapper.class.isAssignableFrom(clazz);
    }

    public void validate(Object obj, Errors errors) {
        DynamicAttributeWrapper attributeWrapper = (DynamicAttributeWrapper) obj;
        DynamicAttribute dynamicAttribute = attributeWrapper.getModifiedAttributeDefinition();
        validateRanges(attributeWrapper, errors, dynamicAttribute);
        if (dynamicAttribute.isCalculated()) {
            validateCalculation(errors, dynamicAttribute);
        }
    }

    private void validateCalculation(Errors errors, DynamicAttribute dynamicAttribute) {
        Calculation calculation = dynamicAttribute.getCalculation();
        List<Expression> list = calculation.getExpressions();
        final String prefix = "expressions[";
        final String postfix = "].";
        final String calculationFormat = "format";
        
        for (int i = 0; i < list.size(); i++) {
            Expression expression = list.get(i);
            // must have a selected attribute or value
            DynamicAttribute attribute = expression.getAttribute();
            String value = expression.getValue();

            if (StringUtils.hasText(value)) {
                try {
                    Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    errors.rejectValue(prefix + i + postfix + calculationFormat, "error.settings.field.is.numeric", "Must be a number");
                }
                int amount = Integer.parseInt(value);
                if(expression.getFormat() == DateCalculation.MONTHS && (amount < 0 || amount > 11)) {
                    errors.rejectValue(prefix + i + postfix + calculationFormat, "error.max.logins.field.out.of.range", new Object[] {0, 11}, "Must be a number");
                }
                if(expression.getFormat() == DateCalculation.NO_FORMAT) {
                    errors.rejectValue(prefix + i + postfix + calculationFormat, "error.required.field", "Please enter a value");
                }
            }
            String value1 = expression.getRefValue();
            if (attribute == null && !StringUtils.hasText(value) && !StringUtils.hasText(value1)) {
                errors.rejectValue(prefix + i + postfix + calculationFormat, "error.one.field.required", "at least one value must be selected");
            }
            if (i < (list.size() - 1)) {
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, prefix + i + postfix + "operator", "error.required.field", "required value");
            }
        }
    }

    /**
     * Validates the DynamicAttribute.
     *
     * @param wrapper          the dynamic attribute facade
     * @param errors           the errors object
     * @param dynamicAttribute the modifed wrapped object
     */
    public void validateRanges(DynamicAttributeWrapper wrapper, Errors errors, DynamicAttribute dynamicAttribute) {
        ValidationUtils.rejectIfEmpty(errors, "label", "error.dynamicattribute.label.required", "Label is a required field.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "error.dynamicattribute.description.required", "Description is a required field.");

        IDaSpecification specification = factory.getValidator(wrapper.getType());
        if (specification == null) return;
        try {
            specification.validate(dynamicAttribute);
        } catch (InvalidDynamicAttributeException e) {
            errors.rejectValue(e.getOffender(), e.getMessage().trim(), "Invalid attribute values");
        }
    }

    public void setFactory(DaDefinitionValidationFactory factory) {
        this.factory = factory;
    }

    private DaDefinitionValidationFactory factory;
}

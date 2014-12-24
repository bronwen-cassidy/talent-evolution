package com.zynap.talentstudio.web.analysis.populations;

import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.web.organisation.attributes.AttributeValueValidationFactory;
import com.zynap.talentstudio.web.organisation.attributes.AttributeValueValidationUtils;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;
import com.zynap.talentstudio.common.AccessType;

import org.apache.commons.lang.math.NumberUtils;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 01-Mar-2005
 * Time: 13:35:16
 * To change this template use File | Settings | File Templates.
 */
public class PopulationValidator implements Validator {

    /**
     * Return whether or not this object can validate objects
     * of the given class.
     */
    public boolean supports(Class clazz) {
        return (clazz == PopulationWrapperBean.class);
    }

    /**
     * Validate an object, which must be of a class for which
     * the supports() method returned true.
     *
     * @param obj    Populated object to validate
     * @param errors Errors object we're building. May contain
     *               errors for this field relating to types.
     */
    public void validate(Object obj, Errors errors) {

    }

    public void validateRequiredValues(PopulationWrapperBean wrapper, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "population.label", "error.label.required", "Population is a required field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "population.type", "error.artefact.type.required", "Type is a required field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "population.scope", "error.scope.required", "Scope is a required field");
        // make sure private reports are not assinged to groups
        if (wrapper.getPopulation().getScope().equals(AccessType.PRIVATE_ACCESS.toString()) && wrapper.hasAssignedGroups()) {
            errors.rejectValue("groupIds", "error.groups.private.scope.invalid");
        }
    }

    public void validateCriteria(PopulationWrapperBean wrapper, Errors errors) {
        final List populationCriterias = wrapper.getPopulationCriterias();
        if (populationCriterias != null) {
            int i = 0;
            for (Iterator it = populationCriterias.iterator(); it.hasNext(); i++) {

                CriteriaWrapperBean criteria = (CriteriaWrapperBean) it.next();

                final String criteriaPrefix = "populationCriterias";
                final String criteriaPrefixIndex = criteriaPrefix + "[" + i;

                ValidationUtils.rejectIfEmptyOrWhitespace(errors, criteriaPrefixIndex + "].attribute", "criteria.attribute.required", "Please select an attribute");
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, criteriaPrefixIndex + "].comparator", "criteria.comparator.required", "Please select an operator");
                if (i < populationCriterias.size() - 1)
                    ValidationUtils.rejectIfEmptyOrWhitespace(errors, criteriaPrefixIndex + "].operator", "criteria.operator.required", "Please select a value ");

                final AttributeWrapperBean attributeDefinition = criteria.getAttributeDefinition();
                // no further procesing if the attribute definition is null
                if (attributeDefinition == null) continue;

                if (!IPopulationEngine.ISNULL.equals(criteria.getComparator())) {

                    if (criteria.isDerivedAttribute() && !NumberUtils.isDigits(attributeDefinition.getValue())) {
                        final String fieldName = criteriaPrefixIndex + "].attributeDefinition.value";
                        errors.rejectValue(fieldName, "criteria.number.required", "Please enter a number");
                    } else {
                        AttributeValueValidationUtils.validateCriteriaAttribute(factory, attributeDefinition, errors, criteriaPrefix, i, "attributeDefinition", null);
                    }

                } else {
                    attributeDefinition.setValue(null);
                }
            }
        }
    }

    /**
     * Check that brackets are in pairs and that every right bracket must be preceeded by a left one.
     *
     * @param wrapper
     * @param errors
     */
    public void validateBrackets(PopulationWrapperBean wrapper, Errors errors) {
        final List populationCriterias = wrapper.getPopulationCriterias();
        if (populationCriterias != null) {

            Stack leftBrackets = new Stack();

            int i = 0;
            for (Iterator iterator = populationCriterias.iterator(); iterator.hasNext(); i++) {
                CriteriaWrapperBean criteriaWrapperBean = (CriteriaWrapperBean) iterator.next();

                // push left onto stack
                if (StringUtils.hasText(criteriaWrapperBean.getLeftBracket())) {
                    leftBrackets.push(Integer.toString(i));
                }

                // pop when you find a right - if none found or stack is empty there is a problem
                if (StringUtils.hasText(criteriaWrapperBean.getRightBracket())) {
                    Object popped = null;
                    if (!leftBrackets.empty()) {
                        popped = leftBrackets.pop();
                    }

                    if (popped == null) {
                        addBracketError(errors, i);
                    }
                }
            }

            // if brackets left over in the stack then the brackets are mismatched
            for (Iterator iterator = leftBrackets.iterator(); iterator.hasNext();) {
                String pos = (String) iterator.next();
                addBracketError(errors, Integer.parseInt(pos));
            }
        }
    }

    private void addBracketError(Errors errors, int i) {
        errors.rejectValue("populationCriterias[" + i + "].leftBracket", "criteria.brackets.notmatched", "Please make sure your brackets match.");
    }

    public void setFactory(AttributeValueValidationFactory factory) {
        this.factory = factory;
    }

    private AttributeValueValidationFactory factory;
}

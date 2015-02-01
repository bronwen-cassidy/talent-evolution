package com.zynap.talentstudio.web.organisation.attributes;

import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.validators.definition.MandatoryValidator;

import com.zynap.talentstudio.web.organisation.attributes.validators.ErrorMessageHandler;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import java.util.Collection;
import java.util.Iterator;

/**
 * Handles validation of dynamic attribute values.
 * <p/>
 * User: amark
 * Date: 09-Feb-2005
 * Time: 15:18:30
 */
public final class AttributeValueValidationUtils {

    /**
     * Private constructor to prevent instantiation.
     */
    private AttributeValueValidationUtils() {
    }

    public static void validateAttribute(AttributeValueValidationFactory factory, AttributeWrapperBean attributeWrapperBean, Errors errors, String prefix, int index, String suffix, boolean checkMandatory, Long nodeId) {

        final String fieldName = getFieldName(suffix, prefix, index, attributeWrapperBean);
        final DynamicAttribute attributeDefinition = attributeWrapperBean.getAttributeDefinition();
        final String value = attributeWrapperBean.getValue();

        ErrorMessageHandler errorCondition = null;
        if(StringUtils.hasText(value) && value.length() > 3800) errors.rejectValue(fieldName, "error.too.many.characters");
        if (checkMandatory) {

            // no validation required continue to the next
            if (!attributeDefinition.isMandatory() && !StringUtils.hasText(value)) return;

            // test if it has a value
            errorCondition = validateMandatory(attributeDefinition, value, errorCondition, errors, fieldName);

            // mandatory and we have no values no point to continue as the error has been recorded.
            if (errorCondition != null) return;
        }

        validateValue(factory, attributeDefinition, attributeWrapperBean, errors, fieldName, nodeId);
    }

    public static void validateCriteriaAttribute(AttributeValueValidationFactory factory, AttributeWrapperBean attributeWrapperBean, Errors errors, String prefix, int index, String suffix, Long nodeId) {

        final String fieldName = getFieldName(suffix, prefix, index, attributeWrapperBean);
        final DynamicAttribute attributeDefinition = attributeWrapperBean.getAttributeDefinition();
        final String value = attributeWrapperBean.getValue();

        ErrorMessageHandler errorCondition = null;
        // test if it has a value
        errorCondition = validateMandatory(attributeDefinition, value, errorCondition, errors, fieldName);

        // mandatory and we have no values no point to continue as the error has been recorded.
        if (errorCondition != null) return;

        validateValue(factory, attributeDefinition, attributeWrapperBean, errors, fieldName, nodeId);
    }

    /**
     * Validate attributes that have no suffix
     *
     * @param factory
     * @param formAttributes
     * @param errors
     * @param prefix
     * @param nodeId
     */
    public static void validateAttributes(AttributeValueValidationFactory factory, Collection formAttributes, Errors errors, String prefix, Long nodeId) {
        int i = 0;
        for (Iterator iterator = formAttributes.iterator(); iterator.hasNext(); i++) {
            final FormAttribute formAttribute = (FormAttribute) iterator.next();
            if (formAttribute.isEditable()) {
                final AttributeWrapperBean attributeWrapperBean = (AttributeWrapperBean) formAttribute;
                validateAttribute(factory, attributeWrapperBean, errors, prefix, i, null, true, nodeId);
            }
        }
    }

    private static ErrorMessageHandler validateMandatory(DynamicAttribute attributeDefinition, String value, ErrorMessageHandler errorCondition, Errors errors, String fieldName) {
        String errorKey = new MandatoryValidator().validate(attributeDefinition, value);
        if (errorKey != null) {
            errorCondition = new ErrorMessageHandler(errorKey);
        }

        verifyValidation(errorCondition, errors, fieldName);
        return errorCondition;
    }

    private static void verifyValidation(ErrorMessageHandler errorCondition, Errors errors, String fieldName) {
        if (errorCondition != null) {
            errors.rejectValue(fieldName, errorCondition.getErrorKey(), errorCondition.getErrorValues(), "Please enter a valid value");
        }
    }

    private static void validateValue(AttributeValueValidationFactory factory, DynamicAttribute attributeDefinition, AttributeWrapperBean attributeWrapperBean, Errors errors, String fieldName, Long nodeId) {
        // there is a value we need to check to see if it is valid as defined by the definition
        ErrorMessageHandler errorCondition = factory.getAttributeValidator(attributeDefinition.getType()).validate(attributeWrapperBean, nodeId);
        verifyValidation(errorCondition, errors, fieldName);
    }

    private static String getFieldName(String suffix, String prefix, int index, AttributeWrapperBean attributeWrapperBean) {
        suffix = suffix != null ? "." + suffix : "";
        return prefix + "[" + index + "]" + suffix + attributeWrapperBean.getFieldName();
    }
}

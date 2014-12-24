package com.zynap.talentstudio.organisation.attributes;

import org.apache.commons.collections.Predicate;

/**
 * Predicate that finds AttributeValues based on the label of the associated DynamicAttribute.
 * User: amark
 * Date: 19-Apr-2005
 * Time: 10:55:32
 */
public class AttributeValuePredicate implements Predicate {

    private String dynamicAttributeLabel;

    public AttributeValuePredicate(String dynamicAttributeLabel) {
        this.dynamicAttributeLabel = dynamicAttributeLabel;
    }

    /**
     * Return true if the object (which is a {@link AttributeValue} has the Dynamic Attribute with the specified label.
     * @param object The {@link AttributeValue} object
     * @return true or false
     */
    public boolean evaluate(Object object) {
        AttributeValue attributeValue = (AttributeValue) object;
        final DynamicAttribute dynamicAttribute = attributeValue.getDynamicAttribute();
        return dynamicAttribute != null && dynamicAttribute.getExternalRefLabel().equals(dynamicAttributeLabel);
    }
}

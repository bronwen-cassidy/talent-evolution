package com.zynap.talentstudio.preferences.properties;

import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.AttributeValuePredicate;
import com.zynap.talentstudio.preferences.format.FormattingInfo;
import com.zynap.talentstudio.common.lookups.LookupValue;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Collection;


/**
 * User: amark
 * Date: 02-Dec-2004
 * Time: 11:05:21
 * Class that holds the formatting data for a particular attribute of a domain object.
 * <br> Also contains the name of the attribute so that it can get the value using reflection.
 */
public class AttributePreference implements Serializable {

    public AttributePreference() {
    }

    public AttributePreference(String attributeName) {
        this(attributeName, false);
    }

    public AttributePreference(String attributeName, boolean displayable) {
        this.attributeName = attributeName;
        this.displayable = displayable;
    }

    public AttributePreference(String attributeName, String displayName, boolean displayable) {
        this(attributeName, displayable);
        this.displayName = displayName;
    }

    public boolean isDisplayable() {
        return displayable;
    }

    public void setDisplayable(boolean displayable) {
        this.displayable = displayable;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public FormattingInfo getFormattingInfo() {
        return formattingInfo;
    }

    public void setFormattingInfo(FormattingInfo formattingInfo) {
        this.formattingInfo = formattingInfo;
    }

    public AttributeView apply(Object domainObject) throws Exception {
        AttributeView value = null;
        if (displayable) {
            value = invoke(domainObject);
        }

        return value;
    }

    protected AttributeView invoke(Object domainObject) throws Exception {
        AttributeView result = null;
        if (StringUtils.hasText(attributeName)) {
            try {
                String value = BeanUtils.getNestedProperty(domainObject, attributeName);
                if (StringUtils.hasText(value)) return new AttributeView(value, value, getFormattingInfo(domainObject, value));
            } catch (NoSuchMethodException e) {
                // if the attribute name did not correspond to a method and the domain object is a Node check for a dynamic attribute value
                // with the specified attribute name
                if (domainObject instanceof Node) {

                    final Collection dynamicAttributeValues = ((Node) domainObject).getDynamicAttributeValues().getValues();
                    // nothing to check
                    if (dynamicAttributeValues == null || dynamicAttributeValues.isEmpty()) return result;

                    final AttributeValue attributeValue = (AttributeValue) CollectionUtils.find(dynamicAttributeValues, new AttributeValuePredicate(attributeName));
                    if (attributeValue != null) {
                        String expectedValue;
                        LookupValue answer = attributeValue.getSelectionLookupValue();
                        if (answer != null) expectedValue = answer.getValueId();
                        else expectedValue = attributeValue.getDynamicAttribute().getExternalRefLabel();

                        result = new AttributeView(expectedValue, attributeValue.getDisplayValue(), getFormattingInfo(domainObject, expectedValue));
                    }

                } else {
                    // otherwise throw the exception
                    throw e;
                }
            }
        }

        return result;
    }

    protected FormattingInfo getFormattingInfo(Object domainObject, String value) throws Exception {
        return formattingInfo;
    }

    public void addOrUpdateFormattingInfo(String expectedValue, String formattingAttributeName, String formattingAttributeValue) {
        if (formattingInfo == null) {
            formattingInfo = new FormattingInfo();
        }

        formattingInfo.addOrUpdate(formattingAttributeName, formattingAttributeValue);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AttributePreference)) return false;

        final AttributePreference attributePreference = (AttributePreference) o;

        if (displayable != attributePreference.displayable) return false;
        if (formattingInfo != null ? !formattingInfo.equals(attributePreference.formattingInfo) : attributePreference.formattingInfo != null)
            return false;
        if (attributeName != null ? !attributeName.equals(attributePreference.attributeName) : attributePreference.attributeName != null)
            return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (displayable ? 1 : 0);
        result = 29 * result + (attributeName != null ? attributeName.hashCode() : 0);
        result = 29 * result + (formattingInfo != null ? formattingInfo.hashCode() : 0);
        return result;
    }

    private static final long serialVersionUID = 8469419411064470152L;

    public static final String DEFAULT = "default";

    protected boolean displayable;

    protected String attributeName;
    protected String displayName;

    protected FormattingInfo formattingInfo;

}

/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports.crosstab;

import com.zynap.talentstudio.common.lookups.LookupType;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.util.CurrencyFormatter;
import com.zynap.talentstudio.util.FormatterFactory;

import org.springframework.util.StringUtils;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 12-Sep-2006 15:21:59
 *
 *        Component that is used to format extended attribute or question values based on the type of dynamic attribute.
 */
public final class ArtefactAttributeViewFormatter {

    /**
     * Format value based on dynamic attribute type.
     *
     * @param nodeExtendedAttribute
     * @param nodeLabel
     * @return the display value or an empty string (not null)
     */
    public static String formatValue(NodeExtendedAttribute nodeExtendedAttribute, String nodeLabel) {

        String output = "";
        if (nodeExtendedAttribute != null) {
            output = formatValue(nodeExtendedAttribute.getDynamicAttribute(), nodeExtendedAttribute, nodeLabel);
        }

        return output;
    }

    /**
     * Format value correctly based on dynamic attribute type.
     *
     * @param dynamicAttribute
     * @param nodeLabel
     * @return the display value or an empty string (not null)
     */
    public static String formatValue(DynamicAttribute dynamicAttribute, NodeExtendedAttribute nodeExtendedAttribute, String nodeLabel) {
        String value = nodeExtendedAttribute.getValue();
        String displayValue = value;

        if (StringUtils.hasText(value)) {
            if (dynamicAttribute.isDate()) {
                displayValue = FormatterFactory.getDateFormatter().formatDateAsString(value);
            } else if (dynamicAttribute.isDateTime()) {
                displayValue = FormatterFactory.getDateFormatter().formatDateTimeAsString(value);
            } else if (dynamicAttribute.isSelectionType() || dynamicAttribute.isMappingType()) {
                Long lookupValueId = new Long(value);
                displayValue = dynamicAttribute.getRefersToType().getLookupValue(lookupValueId).getLabel();
            } else if (dynamicAttribute.isMultiSelectionType()) {
                displayValue = getMultiSelectDisplayValue(dynamicAttribute, value);
            } else if (dynamicAttribute.isNodeType() || dynamicAttribute.isLastUpdatedByType()) {
                displayValue = nodeLabel;
            } else if(dynamicAttribute.isCurrencyType()) {
                displayValue = CurrencyFormatter.formatCurrency(nodeExtendedAttribute.getValue(), nodeExtendedAttribute.getCurrency());    
            }
        }

        // ensure that empty string is returned if value is not set
        if (displayValue == null) displayValue = "";

        return displayValue;
    }

    /**
     * Get display value for multiselect.
     *
     * @param dynamicAttribute
     * @param value
     * @return comma-delimited string for all values or null.
     */
    private static String getMultiSelectDisplayValue(DynamicAttribute dynamicAttribute, String value) {

        String displayValue = null;
        final String[] lookupValueIds = StringUtils.commaDelimitedListToStringArray(value);
        final LookupType lookupType = dynamicAttribute.getRefersToType();

        final String[] lookupValueLabels = new String[lookupValueIds.length];
        for (int i = 0; i < lookupValueIds.length; i++) {
            String lookupValueId = lookupValueIds[i];
            LookupValue lookupValue = lookupType.getLookupValue(new Long(lookupValueId));
            lookupValueLabels[i] = lookupValue.getLabel();
        }

        if (lookupValueLabels.length > 0) {
            displayValue = StringUtils.arrayToDelimitedString(lookupValueLabels, ", ");
        }

        return displayValue;
    }
}

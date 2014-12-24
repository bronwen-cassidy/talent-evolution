/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.attributes;

import org.springframework.util.StringUtils;
import com.zynap.talentstudio.util.FormatterFactory;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;


/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class AttributeWrapperMessageResolver {

    public static String[] getMessageParams(DynamicAttribute attributeDefinition) {
        String[] params = new String[0];
        String minSize = checkValue(attributeDefinition, attributeDefinition.getMinSize());
        String maxSize = checkValue(attributeDefinition, attributeDefinition.getMaxSize());
        if (StringUtils.hasText(minSize) && StringUtils.hasText(maxSize)) {
            if (minSize.equals(maxSize)) {
                params = new String[]{minSize};
            } else {
                params = new String[]{minSize, maxSize};
            }
        } else if (StringUtils.hasText(minSize) && !StringUtils.hasText(maxSize)) {
            params = new String[]{minSize};
        } else if (!StringUtils.hasText(minSize) && StringUtils.hasText(maxSize)) {
            params = new String[]{maxSize};
        }
        return params;
    }

    private static String checkValue(DynamicAttribute attributeDefinition, String value) {
        if (StringUtils.hasText(value)) {
            if (attributeDefinition.isDate()) {
                return FormatterFactory.getDateFormatter().formatDateAsString(value);
            } else if (attributeDefinition.isDateTime()) {
                return FormatterFactory.getDateFormatter().formatDateTimeAsString(value);
            }
        }
        return value;
    }

    public static String getRangeMessage(DynamicAttribute attributeDefinition) {
        String minSize = checkValue(attributeDefinition, attributeDefinition.getMinSize());
        String maxSize = checkValue(attributeDefinition, attributeDefinition.getMaxSize());
        if (StringUtils.hasText(minSize) && StringUtils.hasText(maxSize)) {
            if (minSize.equals(maxSize)) {
                return attributeDefinition.isTextAttribute() ? EQUAL_TEXT_MSG : BETWEEN_MSG;
            } else {
                return attributeDefinition.isTextAttribute() ? BETWEEN_TEXT_MSG : BETWEEN_MSG;
            }
        } else if (StringUtils.hasText(minSize) && !StringUtils.hasText(maxSize)) {
            return attributeDefinition.isTextAttribute() ? GREATER_THAN_TEXT_MSG : GREATER_THAN_MSG;
        } else if (!StringUtils.hasText(minSize) && StringUtils.hasText(maxSize)) {
            return attributeDefinition.isTextAttribute() ? LESS_THAN_TEXT_MSG : LESS_THAN_MSG;
        }
        return "";
    }

    public static final String BETWEEN_TEXT_MSG = "attr.between.text";
    public static final String EQUAL_TEXT_MSG = "attr.equal.text";
    public static final String BETWEEN_MSG = "attr.between";
    public static final String GREATER_THAN_TEXT_MSG = "attr.greater.than.text";
    public static final String GREATER_THAN_MSG = "attr.greater.than";
    public static final String LESS_THAN_TEXT_MSG = "attr.less.than.text";
    public static final String LESS_THAN_MSG = "attr.less.than";
}

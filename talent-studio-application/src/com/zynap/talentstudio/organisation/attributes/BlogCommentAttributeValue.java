/* 
 * Copyright (C)  Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.organisation.attributes;

import com.zynap.talentstudio.util.FormatterFactory;

import java.util.List;

/**
 * Class or Interface description.
 *
 * @author syeoh
 * @version 0.1
 * @since 16-Jan-2007 15:14:50
 */
public class BlogCommentAttributeValue extends AttributeValue {

    public BlogCommentAttributeValue(DynamicAttribute dynamicAttribute) {
        super(dynamicAttribute);
    }

    public String getDisplayValue() {
        StringBuffer result = new StringBuffer();

        List<NodeExtendedAttribute> nodeExtendedAttributes = getNodeExtendedAttributes();
        for (int i = 0; i < nodeExtendedAttributes.size(); i++) {
            NodeExtendedAttribute nodeExtendedAttribute = nodeExtendedAttributes.get(i);
            result.append(nodeExtendedAttribute.getAddedBy().getLabel());
            result.append("\n\t");
            result.append(nodeExtendedAttribute.getValue());
            result.append("\n\t");
            result.append(FormatterFactory.getDateFormatter().formatDateTimeAsString(nodeExtendedAttribute.getDateAdded()));
            if(i < nodeExtendedAttributes.size() - 1) {
                result.append(", \n");
            }
        }
        return result.toString();
    }
}

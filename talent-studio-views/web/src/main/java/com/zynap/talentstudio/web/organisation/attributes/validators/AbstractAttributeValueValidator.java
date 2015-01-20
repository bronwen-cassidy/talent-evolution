/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.attributes.validators;

import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.organisation.attributes.validators.definition.AbstractValidator;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;

import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public abstract class AbstractAttributeValueValidator extends AbstractValidator implements IAttributeValueSpecification {

    public ErrorMessageHandler validate(AttributeWrapperBean attributeWrapperBean, Long nodeId) {
        DynamicAttribute definition = attributeWrapperBean.getAttributeDefinition();
        final String value = attributeWrapperBean.getValue();

        // first call of validation would be to check if any mask exists
        if (StringUtils.hasText(definition.getMask())) {
            // validate
            Matcher matcher = Pattern.compile(definition.getMask()).matcher(value);
            if(!matcher.matches()) {
                return new ErrorMessageHandler("error.pattern.mismatch", value);
            }
        }
        // if we need to do a uniqueness check we do it here
        if (definition.isUnique() && StringUtils.hasText(value)) {
            boolean unique = attributeService.checkUniqueness(definition.getId(), value, nodeId);
            if (!unique) {
                return new ErrorMessageHandler("error.non.unique.field", value);
            }
        }

        if (!(StringUtils.hasText(value))) return null;
        if (!checkValueType(attributeWrapperBean))
            return new ErrorMessageHandler(getTypeErrorKey());
        if (StringUtils.hasText(definition.getMinSize()) && compareMinAttributeValue(definition.getMinSize(), attributeWrapperBean) > 0)
            return new ErrorMessageHandler(getLessThanErrorKey(), getErrorValue(definition.getMinSize()));
        if (StringUtils.hasText(definition.getMaxSize()) && compareMaxAttributeValue(definition.getMaxSize(), attributeWrapperBean) > 0)
            return new ErrorMessageHandler(getGreaterThanErrorKey(), getErrorValue(definition.getMaxSize()));
        if (value.length() > ORACLE_MAXIMUM_CHAR) {
            definition.setMaxSize(String.valueOf(ORACLE_MAXIMUM_CHAR));
            return new ErrorMessageHandler(getGreaterThanErrorKey(), getErrorValue(definition.getMaxSize()));
        }
        return null;
    }

    public void setDynamicAttributeService(IDynamicAttributeService attributeService) {
        this.attributeService = attributeService;
    }

    protected String getErrorValue(String minSize) {
        return minSize;
    }

    protected int compareMinAttributeValue(String minSize, AttributeWrapperBean wrapperBean) {
        try {
            Comparable minMaxValue = getRangeValue(minSize);
            Comparable attributeValue = getAttributeValue(wrapperBean.getValue());
            return minMaxValue.compareTo(attributeValue);
        } catch (ParseException e) {
            return 1;
        }
    }

    protected int compareMaxAttributeValue(String maxSize, AttributeWrapperBean wrapperBean) {
        try {
            Comparable minMaxValue = getRangeValue(maxSize);
            Comparable realValue = getAttributeValue(wrapperBean.getValue());
            return realValue.compareTo(minMaxValue);
        } catch (ParseException e) {
            return 1;
        }
    }

    protected String getTypeErrorKey() {
        return null;
    }

    protected Comparable getAttributeValue(String value) throws ParseException {
        return getRangeValue(value);
    }

    protected abstract String getLessThanErrorKey();

    protected abstract String getGreaterThanErrorKey();

    protected abstract Comparable getRangeValue(String value) throws ParseException;

    protected abstract boolean checkValueType(AttributeWrapperBean wrapper);

//    private static final int ORACLE_MAXIMUM_CHAR = 3800;
    public static final int ORACLE_MAXIMUM_CHAR = 3800;
    private IDynamicAttributeService attributeService;
}

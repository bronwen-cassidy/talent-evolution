package com.zynap.talentstudio.organisation.attributes.validators.definition;

import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.InvalidDynamicAttributeException;
import com.zynap.talentstudio.organisation.attributes.validators.IDaSpecification;

import org.springframework.util.StringUtils;

import java.text.ParseException;

/**
* Class or Interface description.
*
* @author bcassidy
* @since 30-Jun-2005 09:14:53
* @version 0.1
*/
public abstract class AbstractRangeValidator extends AbstractValidator implements IDaSpecification {

    public boolean validate(DynamicAttribute attribute) throws InvalidDynamicAttributeException {
        if (StringUtils.hasText(attribute.getMinSize()) && !checkType(attribute.getMinSize()))
            throw new InvalidDynamicAttributeException(getTypeErrorKey(), MIN_SIZE_FIELD, null);
        if (StringUtils.hasText(attribute.getMaxSize()) && !checkType(attribute.getMaxSize()))
            throw new InvalidDynamicAttributeException(getTypeErrorKey(), MAX_SIZE_FIELD, null);
        if (StringUtils.hasText(attribute.getMaxSize()) && StringUtils.hasText(attribute.getMinSize())
                && compareValues(attribute.getMinSize(), attribute.getMaxSize()) > 0)
            throw new InvalidDynamicAttributeException(INVALID_RANGE_ERROR_CODE, MAX_SIZE_FIELD, null);
        return true;
    }

    protected int compareValues(String min, String max) {

        try {
            Comparable minValue = getValue(min);
            Comparable maxValue = getValue(max);
            return minValue.compareTo(maxValue);
        } catch (ParseException e) {
            return 1;
        }
    }

    protected abstract Comparable getValue(String value) throws ParseException;

    protected abstract boolean checkType(String value);

    private static final String INVALID_RANGE_ERROR_CODE = "invalid.range";
    private static final String MAX_SIZE_FIELD = "maxSize";
    private static final String MIN_SIZE_FIELD = "minSize";
}

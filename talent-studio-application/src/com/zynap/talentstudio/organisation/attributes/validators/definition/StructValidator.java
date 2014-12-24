package com.zynap.talentstudio.organisation.attributes.validators.definition;

import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.InvalidDynamicAttributeException;
import com.zynap.talentstudio.organisation.attributes.validators.IDaSpecification;

import org.springframework.util.StringUtils;

/**
* Class or Interface description.
*
* @author jsuiras
* @since 30-Jun-2005 09:50:38
* @version 0.1
*/
public class StructValidator implements IDaSpecification {

    public boolean validate(DynamicAttribute attribute) throws InvalidDynamicAttributeException {
        if(attribute.getRefersToType() == null || !StringUtils.hasText(attribute.getRefersToType().getTypeId())) {
            throw new InvalidDynamicAttributeException(ERROR_CODE, LOOKUP_TYPE_FIELD, null);
        }
        return true;
    }

    private static final String ERROR_CODE = "invalid.selection.value";
    private static final String LOOKUP_TYPE_FIELD = "refersTo";
}

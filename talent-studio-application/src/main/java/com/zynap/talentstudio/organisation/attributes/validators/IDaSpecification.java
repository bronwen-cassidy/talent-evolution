package com.zynap.talentstudio.organisation.attributes.validators;

import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.InvalidDynamicAttributeException;

/**
* Class or Interface description.
*
* @author jsuiras
* @since 29-Jun-2005 16:03:37
* @version 0.1
*/
public interface IDaSpecification {

    public abstract boolean validate(DynamicAttribute attribute) throws InvalidDynamicAttributeException;
}

/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation.attributes.validators.definition;

import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.InvalidDynamicAttributeException;
import com.zynap.talentstudio.organisation.attributes.validators.IDaSpecification;

import org.springframework.util.StringUtils;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class MandatoryValidator implements IDaSpecification {

    // todo is this method needed here ??
    public boolean validate(DynamicAttribute attribute) throws InvalidDynamicAttributeException {
        return true;
    }

    public String validate(DynamicAttribute definition, String value) {
        return StringUtils.hasText(value) ? null : "mandatory.has.no.value";
    }
}

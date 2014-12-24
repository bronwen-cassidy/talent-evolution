/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.attributes.validators;

import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class StructAttributeValueValidator implements IAttributeValueSpecification {

	// todo determine what validation if any this needs, mandatory and no value has already been checked
    public ErrorMessageHandler validate(AttributeWrapperBean attributeWrapperBean, Long nodeId) {
        return null;
    }

    public void setDynamicAttributeService(IDynamicAttributeService attributeService) {}
}


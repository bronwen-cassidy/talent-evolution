/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.attributes;

import com.zynap.talentstudio.web.organisation.attributes.validators.IAttributeValueSpecification;
import com.zynap.talentstudio.web.organisation.attributes.validators.TextAttributeValueValidator;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;

import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class AttributeValueValidationFactory {

    public IAttributeValueSpecification getAttributeValidator(String dynamicAttributeType) {
        IAttributeValueSpecification attributeSpecification = attributeValueMappings.get(dynamicAttributeType);
        if(attributeSpecification == null) {
            attributeSpecification = new TextAttributeValueValidator();
        }
        attributeSpecification.setDynamicAttributeService(attributeService);
        return attributeSpecification;
    }

    public void setAttributeValueMappings(Map attributeValueMappings) {
        this.attributeValueMappings = attributeValueMappings;
    }

    public void setAttributeService(IDynamicAttributeService attributeService) {
        this.attributeService = attributeService;
    }

    private Map<String, IAttributeValueSpecification> attributeValueMappings;
    private IDynamicAttributeService attributeService;
}

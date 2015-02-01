/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.attributes.validators;

import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class LinkAttributeValueValidator implements IAttributeValueSpecification {

    public ErrorMessageHandler validate(AttributeWrapperBean attributeWrapperBean, Long nodeId) {
        try {
            new URL(attributeWrapperBean.getValue());
        } catch (MalformedURLException e) {
            return new ErrorMessageHandler(ERROR_CODE);
        }
        return null;
    }

    public void setDynamicAttributeService(IDynamicAttributeService attributeService) {}

    private static final String ERROR_CODE = "invalid.link.value";
}


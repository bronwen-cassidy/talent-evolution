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
public interface IAttributeValueSpecification {

    ErrorMessageHandler validate(AttributeWrapperBean attributeWrapperBean, Long nodeId);

    void setDynamicAttributeService(IDynamicAttributeService attributeService);
}

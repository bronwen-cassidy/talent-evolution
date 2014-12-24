/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.integration.dto.filters;

import com.zynap.exception.TalentStudioException;

import java.beans.PropertyDescriptor;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface IPropertyFilter {


    PropertyDescriptor[] filter(PropertyDescriptor[] propertyDescriptors, String artefactName) throws TalentStudioException;

    String DEFAULT_INTEGRATION_SCHEMA_PATH = "schema/integration.xsd";
}

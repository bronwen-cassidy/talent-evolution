/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.organisationunit;

import com.zynap.talentstudio.organisation.IOrganisationUnitService;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.web.organisation.EditNodeAttributesController;
import com.zynap.web.controller.ZynapDefaultFormController;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public abstract class BaseOrganisationController extends EditNodeAttributesController {

    public void setOrganisationManager(IOrganisationUnitService organisationManager) {
        organisationUnitService = organisationManager;
    }

    protected IOrganisationUnitService organisationUnitService;
}

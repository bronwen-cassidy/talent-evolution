/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.organisationunit;

import com.zynap.talentstudio.organisation.IOrganisationUnitService;
import com.zynap.talentstudio.web.organisation.EditNodeAttributesController;

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

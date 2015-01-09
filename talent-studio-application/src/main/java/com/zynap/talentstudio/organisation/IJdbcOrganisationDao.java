/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation;

import com.zynap.exception.TalentStudioException;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface IJdbcOrganisationDao {


    /**
     * Disable (make inactive) the selected org unit, its children org units and the positions belonging to the
     * parent org units and the children.
     *
     * @param organisationUnit
     * @param principalId
     * @throws TalentStudioException
     */
    void disable(OrganisationUnit organisationUnit, Long principalId) throws TalentStudioException;

}

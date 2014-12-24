/*
 * Copyright (c) 2004 Zynap Ltd. All rights reserved.
 */
package com.zynap.talentstudio.organisation;

import com.zynap.talentstudio.common.lookups.LookupValue;


/**
 * Interface representing an association between any two DomainObjects.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface IAssociation {

    /**
     * Set the target of this association the target represents any class implementing a domainObject.
     *
     * DomainObjects currently defined in the system are:
     * <ul>
     *  <li>{@link com.zynap.talentstudio.organisation.positions.Position}</li>
     *  <li>{@link com.zynap.talentstudio.organisation.subjects.Subject}</li>
     * </ul>
     *
     * @param lookupValue
     */
    void setQualifier(LookupValue lookupValue);

    /**
     * Get the target this association represents
     *
     * @return IDomainObject the target of this association
     */
    LookupValue getQualifier();
}

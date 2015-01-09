/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.security;

import com.zynap.talentstudio.security.permits.IPermit;

/**
 * Interface that marks an object as being secure - ie:
 * associated with a {@link com.zynap.talentstudio.security.permits.IPermit}.
 *
 * @author amark
 */
public interface ISecureResource {

    /**
     * Set the permit.
     * @param permit The IPermit
     */
    public void setPermit(IPermit permit);

    /**
     * Get the permit.
     * @return The IPermit
     */
    public IPermit getPermit();
}

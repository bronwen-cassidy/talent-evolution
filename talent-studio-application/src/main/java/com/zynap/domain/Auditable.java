/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.domain;

import com.zynap.talentstudio.organisation.Node;

import java.io.Serializable;

/**
 * Marker class for which any auditable object must implement if it wishes to have it's state
 * serialized to the db
 *
 * @author bcassidy
 * @version 0.1
 * @since 09-Mar-2009 11:24:12
 */
public interface Auditable<T extends Serializable> {

    T createAuditable();
}

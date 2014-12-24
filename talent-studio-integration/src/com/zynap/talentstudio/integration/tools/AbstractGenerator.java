/* 
 * Copyright (C)  Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.integration.tools;

import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.common.lookups.ILookupManager;

/**
 * Class or Interface description.
 *
 * @author syeoh
 * @version 0.1
 * @since 13-Jul-2007 10:58:37
 */
public abstract class AbstractGenerator {

    public void setDynamicAttributeService(IDynamicAttributeService dynamicAttributeService) {
        this.dynamicAttributeService = dynamicAttributeService;
    }

    public void setLookupManager(ILookupManager lookupManager) {
        this.lookupManager = lookupManager;
    }

    protected IDynamicAttributeService dynamicAttributeService;
    protected ILookupManager lookupManager;
}

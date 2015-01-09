/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation;

import com.zynap.domain.SearchAdaptor;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class OrgUnitSearchQuery extends SearchAdaptor {

    public String getLabel() {
        return getStringQueryParamValue(LABEL);
    }

    public void setLabel(String label) {
        setStringQueryParam(LABEL, label);
    }
}

/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.domain.orgbuilder;

import com.zynap.domain.SearchAdaptor;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class PositionSearchQuery extends SearchAdaptor {

    public String getTitle() {
        return getStringQueryParamValue(TITLE);
    }

    public void setTitle(String title) {
        setStringQueryParam(TITLE, title);
    }

}
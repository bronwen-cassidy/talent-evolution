/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.domain.admin;

import com.zynap.domain.SearchAdaptor;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class UserSearchQuery extends SearchAdaptor {

    public String getPrefName() {
        return getStringQueryParamValue(PREF_NAME);
    }

    public void setPrefName(String name) {
        setStringQueryParam(PREF_NAME, name);
    }    

    public String getUsername() {
        return getStringQueryParamValue(USER_NAME);
    }

    public void setUsername(String name) {
        setStringQueryParam(USER_NAME, name);
    }

    public String getFirstName() {
        return getStringQueryParamValue(FIRST_NAME);
    }

    public void setFirstName(String name) {
        setStringQueryParam(FIRST_NAME, name);
    }

    public String getSecondName() {
        return getStringQueryParamValue(SECOND_NAME);
    }

    public void setSecondName(String name) {
        setStringQueryParam(SECOND_NAME, name);
    }
}

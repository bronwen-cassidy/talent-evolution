/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.security.admin;

import com.zynap.domain.SearchAdaptor;
import com.zynap.domain.admin.UserSearchQuery;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class UserSearchQueryWrapper implements Serializable {

    public UserSearchQueryWrapper(SearchAdaptor searchAdaptor) {
        this.searchAdaptor = (UserSearchQuery) searchAdaptor;
    }

    public String getUserName() {
        return searchAdaptor.getUsername();
    }

    public void setUserName(String name) {
        searchAdaptor.setUsername(name);
    }

    public String getFirstName() {
        return searchAdaptor.getFirstName();
    }

    public void setFirstName(String name) {
        this.searchAdaptor.setFirstName(name);
    }

    public void setSecondName(String name) {
        this.searchAdaptor.setSecondName(name);
    }

    public String getSecondName() {
        return this.searchAdaptor.getSecondName();
    }

    public String getPrefName() {
        return this.searchAdaptor.getPrefName();
    }

    public void setPrefName(String name) {
        searchAdaptor.setPrefName(name);
    }

    public SearchAdaptor getSearchAdaptor() {
        return searchAdaptor;
    }

    public void setActive(String active) {
        searchAdaptor.setActive(active);
    }

    public String getActive() {
        return this.searchAdaptor.getActive();
    }

    public Long getGroupId() {
        return this.searchAdaptor.getLongQueryParamValue("group.id");
    }

    public void setGroupId(Long value) {
        this.searchAdaptor.setLongQueryParam("group.id", value);
    }

    private UserSearchQuery searchAdaptor;
}

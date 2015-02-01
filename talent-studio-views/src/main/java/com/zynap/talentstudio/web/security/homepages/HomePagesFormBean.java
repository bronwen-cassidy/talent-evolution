/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.security.homepages;

import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.security.homepages.HomePage;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class containing a list of homePageWrappers
 *
 * @author bcassidy
 * @version 0.1
 * @since 26-Nov-2007 11:25:01
 */
public class HomePagesFormBean implements Serializable {

    private List<String> internalUrls;

    public HomePagesFormBean() {
    }

    public List<HomePageWrapperBean> getHomePages() {
        return homePages;
    }

    public void setHomePages(List<HomePageWrapperBean> homePages) {
        this.homePages = homePages;
    }

    public void add(HomePageWrapperBean wrapperBean) {
        homePages.add(wrapperBean);
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    public Group getCreatedHomePages() {
        group.getHomePages().clear();
        for (HomePageWrapperBean homePageWrapperBean : homePages) {
            HomePage homePage = homePageWrapperBean.getModifiedHomePage();
            if (StringUtils.hasText(homePage.getUrl())) {
                final boolean isInternal = checkInternalUrlMatch(homePage.getUrl());
                homePage.setInternalUrl(isInternal);
            }
            group.addHomePage(homePage);
        }
        return group;
    }

    private boolean checkInternalUrlMatch(String url) {
        for (String internalUrl : internalUrls) {
            if(url.contains(internalUrl)) return true;
        }
        return false;
    }

    public String getGroupLabel() {
        return group.getLabel();
    }

    public void setGroupLabel(String groupLabel) {
        group.setLabel(groupLabel);
    }

    public boolean isEditing() {
        return editing;
    }

    public void setEditing(boolean editing) {
        this.editing = editing;
    }

    public void clearState() {
        group.setId(null);
        for(HomePageWrapperBean homePageWrapperBean : homePages) {
            HomePage homePage = homePageWrapperBean.getModifiedHomePage();
            homePage.setId(null);
        }
    }

    private List<HomePageWrapperBean> homePages = new ArrayList<HomePageWrapperBean>();
    private Group group;
    private boolean editing = false;

    public void setInternalUrls(List<String> internalUrls) {
        this.internalUrls = internalUrls;
    }
}

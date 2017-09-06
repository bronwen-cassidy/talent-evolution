/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.common.groups;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.security.homepages.HomePage;

import java.util.HashSet;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 19-Nov-2007 12:02:11
 */
public class Group extends ZynapDomainObject implements Comparable<Group> {

    /**
     * Constructor.
     */
    public Group() {
    }

    public Group(Long id, String groupLabel, String type) {
        super(id, groupLabel);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<HomePage> getHomePages() {
        return homePages;
    }

    public void setHomePages(Set<HomePage> homePages) {
        this.homePages = homePages;
    }

    public void addHomePage(HomePage homePage) {
        homePage.setGroup(this);
        homePages.add(homePage);
    }

    public boolean isHomePageType() {
        return TYPE_HOMEPAGE.equals(type);
    }

    public Group createAuditable() {
        return new Group(id, label, type);
    }

    public int compareTo(Group other) {
        return getLabel().compareTo(other.getLabel());
    }
	
	private String type;
    private Set<HomePage> homePages = new HashSet<HomePage>();

    public static final String TYPE_QUESTIONNAIRE = "QUESTIONNAIRE";
    public static final String TYPE_HOMEPAGE = "HOMEPAGE";
}

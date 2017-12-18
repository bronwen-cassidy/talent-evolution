/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.dashboard;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.security.roles.Role;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 17-May-2010 17:25:16
 */
public class Dashboard extends ZynapDomainObject {

    public Dashboard() {
    }

    public Dashboard(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<DashboardItem> getDashboardItems() {
        return dashboardItems;
    }

    public void setDashboardItems(List<DashboardItem> dashboardItems) {
        this.dashboardItems = dashboardItems;
    }

    public Long getPopulationId() {
        return populationId;
    }

    public void setPopulationId(Long populationId) {
        this.populationId = populationId;
    }

    public Population getPopulation() {
        return population;
    }

    public void setPopulation(Population population) {
        this.population = population;
    }

    public void addDashboardItem(DashboardItem dashboardItem) {
        dashboardItem.setDashboard(this);
        dashboardItem.setPosition(new Integer(dashboardItems.size()));
        this.dashboardItems.add(dashboardItem);
    }

    public void addGroup(Group group) {
        this.groups.add(group);
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public boolean isPerson() {
        return PERSON_TYPE.equals(type);
    }

    public boolean isPersonal() {
        return PERSONAL_TYPE.equals(type);
    }

	public void removeDashboardItem(DashboardItem item) {
		for (Iterator<DashboardItem> iterator = dashboardItems.iterator(); iterator.hasNext(); ) {
			DashboardItem next = iterator.next();
			if(item.getId().equals(next.getId())) iterator.remove();
		}
	}

	private String type;
    private Set<Group> groups = new LinkedHashSet<Group>();
    private Set<Role> roles = new LinkedHashSet<Role>();
    private List<DashboardItem> dashboardItems = new ArrayList<DashboardItem>();
    public static final String PERSONAL_TYPE = "PERSONAL";
    public static final String PERSON_TYPE = "PERSON";
    private Long populationId;
    private Population population;
}

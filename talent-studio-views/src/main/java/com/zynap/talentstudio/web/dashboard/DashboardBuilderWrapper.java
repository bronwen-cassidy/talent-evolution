/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.dashboard;

import com.zynap.talentstudio.analysis.populations.PopulationDto;
import com.zynap.talentstudio.common.SelectionNode;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.dashboard.Dashboard;
import com.zynap.talentstudio.security.roles.Role;
import com.zynap.talentstudio.web.utils.SelectionNodeHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Class representing a delegate for the dashboard business object
 *
 * @author bcassidy
 * @version 0.1
 * @since 09-Feb-2009 13:01:17
 */
public class DashboardBuilderWrapper implements Serializable {

    public DashboardBuilderWrapper(Dashboard dashboard) {
        this.dashboard = dashboard;
    }

    public Dashboard getModifiedDashboard() {

        cleanItems();
        sortDashboardItems();
        assignDashboardItems();
        assignGroups();
        assignRoles();
        return dashboard;
    }

    private void cleanItems() {
        for (Iterator<DashboardItemWrapper> iter = dashboardItems.iterator(); iter.hasNext();) {
            DashboardItemWrapper dashboardItem = iter.next();
            if (dashboardItem == null || !dashboardItem.isValid()) {                
                iter.remove();
            }
        }
    }

    private void assignRoles() {
        dashboard.getRoles().clear();
        for (SelectionNode role : roles) {
            if (role.isSelected()) {
                dashboard.addRole((Role) role.getValue());
            }
        }
    }

    private void assignGroups() {
        dashboard.getGroups().clear();
        for (SelectionNode group : groups) {
            if (group.isSelected()) {
                dashboard.addGroup((Group) group.getValue());
            }
        }
    }

    private void assignDashboardItems() {
        dashboard.getDashboardItems().clear();
        for (Iterator<DashboardItemWrapper> iter = dashboardItems.iterator(); iter.hasNext();) {
            DashboardItemWrapper dashboardItem = iter.next();
            if (dashboardItem.isValid()) {
                dashboard.addDashboardItem(dashboardItem.getModifiedItem());
            } else {
                iter.remove();
            }
        }
    }

    private void sortDashboardItems() {
        Collections.sort(dashboardItems, new Comparator<DashboardItemWrapper>() {
            public int compare(DashboardItemWrapper o1, DashboardItemWrapper o2) {
                return o1.getPosition().compareTo(o2.getPosition());
            }
        });
    }

    public void setDashboardItems(List<DashboardItemWrapper> dashboardItems) {
        this.dashboardItems = dashboardItems;
    }

    public List<DashboardItemWrapper> getDashboardItems() {
        return dashboardItems;
    }

    public String getLabel() {
        return dashboard.getLabel();
    }

    public void setLabel(String label) {
        dashboard.setLabel(label);
    }

    public String getNumItems() {
        String itemPositionString = "";
        int i = 0;
        for (DashboardItemWrapper dashboardItem : dashboardItems) {
            itemPositionString += dashboardItem.getPosition();
            if (i++ < (dashboardItems.size() - 1)) itemPositionString += ",";
        }
        return itemPositionString;
    }

    public Long getId() {
        return dashboard.getId();
    }

    public void setPopulations(Collection<PopulationDto> populations) {
        this.populations = populations;
    }

    public Collection<PopulationDto> getPopulations() {
        return populations;
    }

    public void setRoles(List<Role> activeAccessRoles) {
        final Set<Role> roleSet = dashboard.getRoles();
        this.roles = (List<SelectionNode>) SelectionNodeHelper.createDomainObjectSelections(new ArrayList(activeAccessRoles), roleSet);
    }

    public List<SelectionNode> getGroups() {
        return groups;
    }

    public List<SelectionNode> getRoles() {
        return roles;
    }

    public Long getPopulationId() {
        return dashboard.getPopulationId();
    }

    public void setPopulationId(Long id) {
        dashboard.setPopulationId(id);
    }

    public void setGroups(List<Group> groups) {
        final Set<Group> groupSet = dashboard.getGroups();
        this.groupSize = groupSet.size();
        this.groups = (List<SelectionNode>) SelectionNodeHelper.createDomainObjectSelections(new ArrayList(groups), groupSet);
    }

    public void setGroupIds(Long[] groupIds) {
        this.groupSize = groupIds.length;
        SelectionNodeHelper.disableSelections(groups);
        SelectionNodeHelper.enableDomainObjectSelections(groups, groupIds);
    }

    public Long[] getGroupIds() {
        return new Long[0];
    }

    public void setRoleIds(Long[] roleIds) {
        this.roleSize = roleIds.length;
        SelectionNodeHelper.disableSelections(roles);
        SelectionNodeHelper.enableDomainObjectSelections(roles, roleIds);
    }

    public Long[] getRoleIds() {
        return new Long[0];
    }

    public int getRoleSize() {
        return roleSize;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public boolean isPersonType() {
        return dashboard.isPerson();
    }

    public boolean isPersonalType() {
        return dashboard.isPersonal();
    }

    private Dashboard dashboard;
    private List<DashboardItemWrapper> dashboardItems = new ArrayList<DashboardItemWrapper>();
    private Collection<PopulationDto> populations;
    private List<SelectionNode> groups = new ArrayList<SelectionNode>();
    private List<SelectionNode> roles = new ArrayList<SelectionNode>();
    private int roleSize = 0;
    private int groupSize = 0;
}
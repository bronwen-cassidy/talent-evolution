/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.display;

import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.common.SelectionNode;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.display.DisplayConfig;
import com.zynap.talentstudio.display.DisplayConfigItem;
import com.zynap.talentstudio.display.DisplayItemReport;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.security.roles.Role;
import com.zynap.talentstudio.web.analysis.AnalysisAttributeWrapperBean;
import com.zynap.talentstudio.web.analysis.picker.AnalysisAttributeCollection;
import com.zynap.talentstudio.web.utils.SelectionNodeHelper;
import com.zynap.talentstudio.web.utils.controller.RoleUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class DisplayConfigWrapper implements Serializable {

    public DisplayConfigWrapper(List<DisplayConfig> displayConfigs, List<Role> allRoles, List<Group> allGroups) {
        this.displayConfigs = displayConfigs;
        this.allRoles = allRoles;
        this.allGroups = allGroups;
    }

    public String getActiveTab() {
        return activeTab;
    }

    public void setActiveTab(String activeTab) {
        this.activeTab = activeTab;
    }

    public DisplayConfig getDisplayConfig() {
        return displayConfig;
    }

    public void setDisplayConfig(DisplayConfig displayConfig) {
        this.displayConfig = displayConfig;
    }

    public DisplayConfigItem getModifedReportConfigItem() {

        displayConfigItem.getRoles().clear();
        for (Iterator iterator = accessRoles.iterator(); iterator.hasNext();) {
            Role role = (Role) iterator.next();
            if (role.isAssigned()) {
                displayConfigItem.addRole(role);
            }
        }

        displayConfigItem.getGroups().clear();
        for (SelectionNode selectionNode : groups) {
            if(selectionNode.isSelected()) {
                displayConfigItem.addGroup((Group)selectionNode.getValue());
            }
        }

        if (isReportConfigItem()) {
            displayConfigItem.getReportItems().clear();
            for (int i = 0; i < reports.size(); i++) {
                DisplayConfigReportWrapper displayConfigReportWrapper = reports.get(i);
                DisplayItemReport itemReport = displayConfigReportWrapper.getModifiedItemReport();
                displayConfigItem.addDisplayItemReport(itemReport);
            }
        }
        return displayConfigItem;
    }

    public Collection<DisplayConfig> getDisplayConfigs() {
        return this.displayConfigs;
    }

    public void setDisplayConfigs(List<DisplayConfig> displayConfigs) {
        this.displayConfigs = displayConfigs;
    }

    public List<DisplayConfigItem> getDisplayConfigItems() {
        return this.displayConfig != null ? this.displayConfig.getDisplayConfigItems() : null;
    }

    public boolean isHasDisplayConfigItems() {
        return getDisplayConfigItems() != null;
    }

    public void setSelectedDisplayConfig(Long displayConfigId) {
        for (int i = 0; i < displayConfigs.size(); i++) {
            DisplayConfig config = displayConfigs.get(i);
            if (displayConfigId.equals(config.getId())) {
                this.displayConfig = config;
                break;
            }
        }
        this.displayConfigItem = null;
    }

    public boolean isHasDisplayConfigItem() {
        return displayConfigItem != null;
    }

    public void setEditing(boolean editing) {
        this.editing = editing;
    }

    public boolean isEditing() {
        return editing;
    }

    public boolean isReportConfigItem() {
        return displayConfigItem != null && displayConfigItem.getReportItems() != null && !displayConfigItem.getReportItems().isEmpty();
    }

    public DisplayConfigItem getDisplayConfigItem() {
        return displayConfigItem;
    }

    public void setAttributeCollection(AnalysisAttributeCollection analysisAttributeCollection) {
        for (int i = 0; i < reports.size(); i++) {
            DisplayConfigReportWrapper displayConfigReportWrapper = reports.get(i);
            displayConfigReportWrapper.setAttributeCollection(analysisAttributeCollection);
        }
    }

    public void addColumn(Column column, int index) {
        if (index == -1) return;
        DisplayConfigReportWrapper report = reports.get(index);
        report.addColumn(column);
        checkColumnAttributes();
    }

    public void removeColumn(int index, int reportIndex) {
        if (reportIndex == -1) return;
        DisplayConfigReportWrapper report = reports.get(reportIndex);
        report.removeColumn(index);
        checkColumnAttributes();
    }


    public void reset(DisplayConfig displayConfig) {
        this.displayConfig = displayConfig;
        setDisplayConfigItemId(displayConfigItem.getId());
    }

    public void setDisplayConfigItemId(Long displayItemId) {
        this.displayConfigItem = displayConfig.getDisplayConfigItem(displayItemId);
        // do the group stuff
        this.groups = SelectionNodeHelper.createDomainObjectSelections(new ArrayList(allGroups), displayConfigItem.getGroups());

        Set configItemRoles = displayConfigItem.getRoles();
        RoleUtils.clearRoles(configItemRoles);
        RoleUtils.clearRoles(allRoles);
        this.accessRoles = RoleUtils.initRoles(allRoles, configItemRoles);
        RoleUtils.sortRoles(accessRoles);

        wrapReports(displayConfigItem.getReportItems());
    }

    public void setDisplayConfigItem(DisplayConfigItem displayConfigItem) {
        this.displayConfigItem = displayConfigItem;
        wrapReports(displayConfigItem.getReportItems());
    }

    public List getReports() {
        return reports;
    }

    public Collection<SelectionNode> getGroups() {
        return groups;
    }

    public void wrapReports(List configReports) {
        this.reports = new ArrayList<DisplayConfigReportWrapper>();
        for (int i = 0; i < configReports.size(); i++) {
            DisplayItemReport report = displayConfigItem.getReportItems().get(i);
            this.reports.add(new DisplayConfigReportWrapper(report));
        }
    }

    public String getReportType() {
        return isReportConfigItem() ? ((DisplayConfigReportWrapper) getReports().get(0)).getReportType() : null;
    }

    /**
     * Get view type.
     *
     * @return The view type or null
     */
    public String getViewType() {

        String viewType = null;

        if (displayConfig != null) {
            if (displayConfig.isAdd()) {
                viewType = displayConfig.getType();
            } else if (displayConfigItem != null) {
                viewType = getConfigItemContentType();
            }
        }

        return viewType;
    }

    public void checkColumnAttributes() {

        for (int i = 0; i < reports.size(); i++) {
            DisplayConfigReportWrapper displayConfigReportWrapper = reports.get(i);
            displayConfigReportWrapper.checkColumnAttributes();
        }
    }

    public void removeInvalidAttributes() {
        for (int i = 0; i < reports.size(); i++) {
            DisplayConfigReportWrapper displayConfigReportWrapper = reports.get(i);
            displayConfigReportWrapper.removeInvalidItems();
        }
    }

    /**
     * Spring binding method.
     *
     * @param roleIds setter used by spring binding
     */
    public void setAccessRoleIds(Long[] roleIds) {
        // make all roles as unassigned
        RoleUtils.clearRoles(accessRoles);
        RoleUtils.updateAssigned(roleIds, accessRoles);
    }

    /**
     * Spring binding method.
     *
     * @return new Long[0]
     */
    public Long[] getAccessRoleIds() {
        return new Long[0];
    }

    public Collection getAccessRoles() {
        return accessRoles;
    }

    public Collection getDisplayConfigItemRoles() {
        if (displayConfigItem == null) return null;
        List<Role> roles = new ArrayList<Role>(displayConfigItem.getRoles());
        RoleUtils.sortRoles(roles);
        return roles;
    }

    public Collection getDisplayConfigItemGroups() {
        if (displayConfigItem == null) return null;
        List<Group> assignedGroups = new ArrayList<Group>(displayConfigItem.getGroups());
        Collections.sort(assignedGroups);
        return assignedGroups;
    }

    public void setGroupIds(Long[] groupIds) {
        SelectionNodeHelper.disableSelections(groups);
        SelectionNodeHelper.enableDomainObjectSelections(groups, groupIds);
    }

    public Long[] getGroupIds() {
        return new Long[0];
    }

    public List<AnalysisAttributeWrapperBean> getAttributeWrapperBeans() {
        List<AnalysisAttributeWrapperBean> result = new ArrayList<AnalysisAttributeWrapperBean>();
        for (DisplayConfigReportWrapper displayConfigReportWrapper : reports) {
            result.addAll(displayConfigReportWrapper.getColumns());
        }
        return result;
    }

    public boolean isPersonExecSummary() {
        boolean isSubjectType = Node.SUBJECT_UNIT_TYPE_.equals(getReportType());
        boolean isExecSummary = DisplayConfigItem.EXEC_VIEW_TYPE.equals(getConfigItemContentType());
        return isSubjectType && isExecSummary;
    }

    private String getConfigItemContentType() {
        return displayConfigItem != null ? displayConfigItem.getContentType() : null;
    }

    private DisplayConfig displayConfig;
    private boolean editing;

    private String activeTab = "displayconfig";

    private List<DisplayConfig> displayConfigs;
    private List<DisplayConfigReportWrapper> reports;
    private DisplayConfigItem displayConfigItem;
    private List accessRoles;
    private List allRoles;
    private Collection<Group> allGroups;
    private Collection<SelectionNode> groups;
}

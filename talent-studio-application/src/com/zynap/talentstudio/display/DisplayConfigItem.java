/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.display;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.arenas.Arena;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.security.roles.Role;

import org.apache.commons.lang.builder.ToStringBuilder;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class DisplayConfigItem extends ZynapDomainObject implements Comparable {

    public DisplayConfigItem() {
    }

    public DisplayConfigItem(String label) {
        this.label = label;
    }

    public DisplayConfigItem(String label, String contentType, boolean active) {
        this.label = label;
        this.contentType = contentType;
        this.active = active;
    }

    public DisplayConfig getDisplayConfig() {
        return displayConfig;
    }

    public boolean isHideable() {
        return hideable;
    }

    public void setHideable(boolean hideable) {
        this.hideable = hideable;
    }

    public void setDisplayConfig(DisplayConfig displayConfig) {
        this.displayConfig = displayConfig;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public List<DisplayItemReport> getReportItems() {
        return reportItems;
    }

    public void setReportItems(List<DisplayItemReport> reportItems) {
        this.reportItems = reportItems;
    }

    public Set<Arena> getArenas() {
        return arenas;
    }

    public void setArenas(Set<Arena> arenas) {
        this.arenas = arenas;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public boolean isRolesModifiable() {
        return rolesModifiable;
    }

    public void setRolesModifiable(boolean rolesModifiable) {
        this.rolesModifiable = rolesModifiable;
    }

    public List<Column> getReportColumns() {
        List<Column> reportColumns = new ArrayList<Column>();
        if(reportItems != null) {
            for (DisplayItemReport itemReport : reportItems) {
                Report report = itemReport.getReport();
                if(report != null) {
                    reportColumns.addAll(report.getColumns());
                }
            }
        }
        return reportColumns;
    }

    public void addDisplayItemReport(DisplayItemReport report) {
        if(reportItems == null) reportItems = new ArrayList<DisplayItemReport>();
        reportItems.add(report);
    }

    public boolean isHasItemReports() {
        return reportItems != null && !reportItems.isEmpty();
    }

    public boolean isAssociation() {
        return ASSOCIATION_DISPLAY_VIEW_TYPE.equals(getContentType());
    }

    public boolean isPortfolio() {
        return PORTFOLIO_DISPLAY_VIEW_TYPE.equals(getContentType());
    }

    public boolean isDashboard() {
        return DASHBOARD_DISPLAY_VIEW_TYPE.equals(getContentType());
    }

    public boolean isUser() {
        return USER_DISPLAY_TYPE.equals(getContentType());
    }

    public boolean isObjectives() {
        return OBJECTIVES_DISPLAY_VIEW_TYPE.equals(getContentType());
    }

    public boolean isPersonReports() {
        return REPORT_DISPLAY_TYPE.equals(getContentType());
    }

    public boolean isProgressReports() { 
        return PROGRESS_REPORT_DISPLAY_TYPE.equals(getContentType());
    }

    public String getKey() {
        return StringUtils.deleteAny(getLabel(), " ,'!?\"\\/-_@*+&").toLowerCase().trim();
    }

    public String getFirstReportLabel() {
        return getFirstReportItem() != null ? getFirstReportItem().getReport().getLabel() : null;
    }

    public int compareTo(Object o) {
        return getSortOrder().compareTo(((DisplayConfigItem)o).getSortOrder());
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("label", getLabel())
                .append("contentType", getContentType())
                .append("hideable", isHideable())
                .toString();
    }

    private DisplayItemReport getFirstReportItem() {
        DisplayItemReport itemReport = null;
        if(reportItems != null && reportItems.size() > 0) {
            itemReport = getReportItems().get(0) ;
        }
        return itemReport;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public void addGroup(Group group) {
        groups.add(group);    
    }

    public void setPopulation(Population population) {
        this.population = population;
    }

    public Population getPopulation() {
        return population;
    }

    private DisplayConfig displayConfig;
    private List<DisplayItemReport> reportItems;
    private Set<Arena> arenas;
    private String contentType;
    private boolean hideable;
    private boolean rolesModifiable;
    private Integer sortOrder;
    private Set<Role> roles;

    public static final String ASSOCIATION_DISPLAY_VIEW_TYPE = "ASSOCIATION";
    public static final String PORTFOLIO_DISPLAY_VIEW_TYPE = "PORTFOLIO";
    public static final String DASHBOARD_DISPLAY_VIEW_TYPE = "DASHBOARD";
    public static final String OBJECTIVES_DISPLAY_VIEW_TYPE = "OBJECTIVES";
    public static final String USER_DISPLAY_TYPE = "USER";
    public static final String REPORT_DISPLAY_TYPE = "REPORTS";
    public static final String PROGRESS_REPORT_DISPLAY_TYPE = "PROGRESS_REPORT";

    public static final String ATTRIBUTE_VIEW_TYPE = "ATTRIBUTE";
    public static final String EXEC_VIEW_TYPE = "EXEC";
    public static final String ADD_VIEW_TYPE = "ADD";
    private Set<Group> groups;
    private Population population;
}

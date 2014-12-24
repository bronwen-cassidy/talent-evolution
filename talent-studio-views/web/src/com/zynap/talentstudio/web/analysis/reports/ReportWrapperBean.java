/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.populations.PopulationDto;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.TabularReport;
import com.zynap.talentstudio.arenas.MenuSection;
import com.zynap.talentstudio.common.AccessType;
import com.zynap.talentstudio.common.SelectionNode;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.util.collections.DomainObjectCollectionHelper;
import com.zynap.talentstudio.web.analysis.GroupableBean;
import com.zynap.talentstudio.web.analysis.picker.AnalysisAttributeCollection;
import com.zynap.talentstudio.web.analysis.reports.functions.FunctionWrapperBean;
import com.zynap.talentstudio.web.analysis.reports.functions.OperandWrapperBean;
import com.zynap.talentstudio.web.arena.IMenuItemContainer;
import com.zynap.talentstudio.web.arena.MenuItemHelper;
import com.zynap.talentstudio.web.arena.MenuItemWrapper;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class ReportWrapperBean extends GroupableBean implements IMenuItemContainer {

    public ReportWrapperBean(Report report) {
        this.report = report;
    }

    public ReportWrapperBean(Report report, Collection<MenuSection> menuSections, Collection<MenuSection> homePageMenuSections, String url) {

        this.url = url;
        this.menuItemWrappers = MenuItemHelper.buildMenuItemWrappers(report.getMenuItems(), menuSections, homePageMenuSections);

        this.report = report;
        this.reportType = report.getPopulationType();
        this.originalAccess = report.getAccessType();
        this.originalPopulation = report.getDefaultPopulation();
        this.populations = new ArrayList<PopulationDto>();
    }

    public void setLabel(String name) {
        this.report.setLabel(name);
    }

    public String getLabel() {
        return report.getLabel();
    }

    public void setType(String type) {
        this.report.setPopulationType(type);
    }

    public String getType() {
        return report.getPopulationType();
    }

    public void setAccess(String access) {
        this.report.setAccessType(access);
    }

    public String getAccess() {
        return report.getAccessType();
    }

    public void setDescription(String description) {
        this.report.setDescription(description);
    }

    public String getDescription() {
        return report.getDescription();
    }

    /**
     * All reports have a population.
     *
     * @param populationId
     */
    public void setPopulationId(Long populationId) {
        PopulationDto selected = null;
        if (populationId != null) {
            selected = (PopulationDto) DomainObjectCollectionHelper.findById(populations, populationId);
        }
        report.setDefaultPopulation(new Population(populationId));
        if (selected != null) report.setPopulationType(selected.getType());
    }

    public Long getPopulationId() {
        return report.getDefaultPopulation() != null ? report.getDefaultPopulation().getId() : null;
    }

    public Report getModifiedReport() {

        // ensures that columns have correct associated dynamic attribute
        checkColumnAttributes();

        // removes all columns that reference a deleted extended attribute
        removeInvalidItems();

        List<Column> newColumns = assignColumnIndexes();

        // set new columns (tabular or crosstab)
        report.assignNewColumns(newColumns);

        // build menu items - for each selected menu section build a new menu item
        assignNewMenuItems(report);

        assignNewGroups(report);

        return report;
    }

    public void removeInvalidItems() {
        List<ColumnWrapperBean> invalidItems = new ArrayList<ColumnWrapperBean>();
        for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
            ColumnWrapperBean columnWrapperBean = (ColumnWrapperBean) iterator.next();
            if (!columnWrapperBean.isFormula() && columnWrapperBean.isInvalid()) {
                invalidItems.add(columnWrapperBean);
            }
        }
        columns.removeAll(invalidItems);
    }

    public List<ColumnWrapperBean> getColumns() {
        return this.columns;
    }

    public void addColumn(Column column) {
        column.setReport(report);
        final ColumnWrapperBean o = new ColumnWrapperBean(column);
        o.setColumnPosition(columns.size());
        this.columns.add(o);
        if(column.isFormula()) {
            o.setFunctionWrapperBean(getSelectedFunction());
        }
    }

    public void removeColumn(int index) {
        columns.remove(index);
    }

    public Collection<PopulationDto> getPopulations() {
        return populations;
    }

    public void setPopulations(Collection<PopulationDto> populations) {
        this.populations = populations;
    }

    public String getOperator() {
        return report.getOperator();
    }

    public void setOperator(String value) {
        report.setOperator(value);
    }

    public void setAttributeCollection(AnalysisAttributeCollection analysisAttributeCollection) {
        this.attributeCollection = analysisAttributeCollection;
    }

    public AnalysisAttributeCollection getAttributeCollection() {
        return attributeCollection;
    }

    public Population getDefaultPopulation() {
        return report.getDefaultPopulation();
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public boolean isPrivate() {
        return AccessType.PRIVATE_ACCESS.toString().equals(report.getAccessType());
    }

    public boolean isCrossTabReport() {
        return report.isCrossTabReport();
    }

    /**
     * Loop through ColumnWrapperBean objects
     * and make sure they all have the correct AttributeWrapperBean.
     */
    public void checkColumnAttributes() {
        for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
            ColumnWrapperBean columnWrapperBean = (ColumnWrapperBean) iterator.next();
            checkColumnAttribute(columnWrapperBean);
        }
    }

    public void resetSelectedFunction() {

        if (selectedFunction != null) {
            selectedFunction.reset();
            checkFunctionAttributes(selectedFunction);
        }
    }

    /**
     * Checks the column for state validity by finding the correct attribute selected and setting it on the columnWrapper.
     *
     * @param column
     */
    protected void checkColumnAttribute(ColumnWrapperBean column) {
        if (attributeCollection != null) {
            if (column.isFormula()) {
                checkFunctionAttributes(column.getFunctionWrapperBean());
            } else {
                attributeCollection.setAttributeDefinition(column);
            }
        }
    }

    /**
     * Checks the function attributes for the pickers only.
     * The method attempts to find the attribute associated with the picked item on the picker.
     *
     * @param functionWrapperBean the delegate containing the function operands
     */
    protected void checkFunctionAttributes(FunctionWrapperBean functionWrapperBean) {
        final List<OperandWrapperBean> expressionWrappers = functionWrapperBean.getOperands();
        for (OperandWrapperBean wrapper : expressionWrappers) {
            AttributeWrapperBean attributeWrapper = attributeCollection.findAttributeDefinition(wrapper.getAttribute());
            wrapper.setAttributeDefinition(attributeWrapper);
        }
    }

    protected void assignNewMenuItems(Report report) {
        Set newMenuItems = getAssignedMenuItems();
        report.assignNewMenuItems(newMenuItems);
    }

    protected void assignNewGroups(Report report) {
        Set<Group> newGroups = new LinkedHashSet<Group>();
        for (SelectionNode selectionNode : groups) {
            if(selectionNode.isSelected()) {
                newGroups.add((Group) selectionNode.getValue());
            }
        }
        report.assignNewGroups(newGroups);
    }


    protected List<Column> assignColumnIndexes() {

        List<Column> newColumns = new ArrayList<Column>();
        int index = 0;
        for (ColumnWrapperBean columnWrapperBean : columns) {
            final Column newColumn = columnWrapperBean.getModifiedColumn();
            newColumn.setReport(report);
            newColumn.setPosition(new Integer(index++));
            newColumns.add(newColumn);
        }
        return newColumns;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public void setScopeEditable(boolean scopeEditable) {
        this.scopeEditable = scopeEditable;
    }

    public boolean isScopeEditable() {
        return scopeEditable;
    }

    public String getReportType() {
        return reportType;
    }

    public void setGroupedColumns(List columnIndexes) {
        resetGrouping();
        List<ColumnWrapperBean> allColumns = getColumns();
        for (int i = 0; i < columnIndexes.size(); i++) {
            int columnIndex = ((Integer) columnIndexes.get(i)).intValue();
            ColumnWrapperBean columnWrapperBean = allColumns.get(columnIndex);
            columnWrapperBean.setGrouped(true);
        }
    }

    private void resetGrouping() {
        List<ColumnWrapperBean> allColumns = getColumns();
        for (int i = 0; i < allColumns.size(); i++) {
            ColumnWrapperBean columnWrapperBean = allColumns.get(i);
            columnWrapperBean.setGrouped(false);
        }
    }

    public boolean hasAccessChanged() {
        return (!this.originalAccess.equals(report.getAccessType()));
    }

    public Population getOriginalPopulation() {
        return originalPopulation;
    }

    public void updateAccess() {
        this.originalAccess = report.getAccessType();
    }

    /**
     * Method for spring binding.
     *
     * @param positions
     */
    public void setActiveMenuItems(String[] positions) {
        MenuItemHelper.setSelected(menuItemWrappers, positions);
    }

    /**
     * Method for spring binding.
     *
     * @return String[]
     */
    public String[] getActiveMenuItems() {
        return new String[0];
    }

    /**
     * Method for spring binding.
     *
     * @param positions
     */
    public void setHomePageMenuItems(String[] positions) {
        MenuItemHelper.setHomePage(menuItemWrappers, positions);
    }

    /**
     * Method for spring binding.
     *
     * @return String[]
     */
    public String[] getHomePageMenuItems() {
        return new String[0];
    }

    /**
     * Get collection of menu item wrappers available for selection.
     *
     * @return Collection of {@link com.zynap.talentstudio.web.arena.MenuItemWrapper} objects
     */
    public List getMenuItemWrappers() {
        return menuItemWrappers;
    }

    /**
     * Check if has assigned menu items.
     *
     * @return true or false
     */
    public boolean hasAssignedMenuItems() {
        return MenuItemHelper.hasSelectedItems(menuItemWrappers);
    }

    /**
     * Get assigned menu items.
     *
     * @return Set
     */
    public Set getAssignedMenuItems() {
        return MenuItemHelper.getAssignedMenuItems(menuItemWrappers, report.getLabel(), url);
    }

    public String getUrl() {
        return url;
    }

    public FunctionWrapperBean getSelectedFunction() {
        return selectedFunction;
    }

    public void setSelectedFunction(FunctionWrapperBean selectedFunction) {
        this.selectedFunction = selectedFunction;
    }

    public Integer getFormulaIndex() {
        return formulaIndex;
    }

    public void setFormulaIndex(Integer formulaIndex) {
        this.formulaIndex = formulaIndex;
    }

    public void updateColumnFormula() {
        int index = this.getCurrentColumnIndex().intValue();
        columns.get(index).setFunctionWrapperBean(getSelectedFunction());
    }

    public void setCurrentColumnIndex(Integer currentColumnIndex) {
        this.currentColumnIndex = currentColumnIndex;
    }

    public Integer getCurrentColumnIndex() {
        return currentColumnIndex;
    }

    /**
     * Method for spring data display.
     *
     * @return ColumnWrapperBean (can be null)
     */
    public ColumnWrapperBean getSelectedColumnWrapper() {
        return selectedColumnWrapper;
    }

    public void setSelectedColumnWrapper(ColumnWrapperBean selectedColumnWrapper) {
        this.selectedColumnWrapper = selectedColumnWrapper;
    }

    /**
     * Find a column.
     *
     * @param selectedColumnIndex
     * @return The ColumnWrapperBean, or null
     */
    public ColumnWrapperBean getColumnWrapper(int selectedColumnIndex) {
        if (selectedColumnIndex < columns.size()) {
            final ColumnWrapperBean column = columns.get(selectedColumnIndex);
            if (column != null) {
                return column;
            }
        }

        return null;
    }

    public void setPersonal(boolean personal) {
        report.setPersonal(personal);
    }

    public boolean isPersonal() {
        return report.isPersonal();
    }

    public void setLastLineItem(boolean lastLineItem) {
        report.setLastLineItem(lastLineItem);        
    }

    public boolean isLastLineItem() {
        return report.isLastLineItem();
    }

    public boolean isChartReport() {
        return report.isChartReport();
    }

    protected Report report;
    protected List<ColumnWrapperBean> columns = new ArrayList<ColumnWrapperBean>();

    private Collection<PopulationDto> populations;
    private int target;

    protected AnalysisAttributeCollection attributeCollection;

    private boolean scopeEditable = true;
    private String reportType;
    private String originalAccess;
    private Population originalPopulation;
    private FunctionWrapperBean selectedFunction;

    /**
     * The list of MenuItemWrappers.
     */
    private List<MenuItemWrapper> menuItemWrappers = new ArrayList<MenuItemWrapper>();

    /**
     * The url for the menu items.
     */
    private String url;
    private Integer formulaIndex;
    private Integer currentColumnIndex;
    private ColumnWrapperBean selectedColumnWrapper;
}

package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.BasicAnalysisAttribute;
import com.zynap.talentstudio.analysis.metrics.Metric;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.ColumnDisplayImage;
import com.zynap.talentstudio.calculations.Calculation;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.web.analysis.AnalysisAttributeWrapperBean;
import com.zynap.talentstudio.web.analysis.reports.functions.FunctionWrapperBean;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * User: amark
 * Date: 30-Jul-2005
 * Time: 14:25:24
 */
public final class ColumnWrapperBean extends AnalysisAttributeWrapperBean {

    public ColumnWrapperBean(Column column) {

        this.column = column;
        if (column != null) {
            if (column.isFormula()) {
                functionWrapperBean = new FunctionWrapperBean(column.getCalculation());
            }
            this.columnDisplayImages = new ArrayList<>(column.getColumnDisplayImages());
        }
    }

    public Column getModifiedColumn() {

        if (attributeDefinition != null) {
            column.setColumnType(attributeDefinition.getType());
            column.setDynamicLineItem(attributeDefinition.isDynamic());
        }
        if (columnDisplayImages != null) column.assignNewColumnDisplayImages(getAssignedColumnDisplayImages());
        if (functionWrapperBean != null) {
            Calculation calculation = functionWrapperBean.getModifiedCalculation();
            column.setCalculation(calculation);
            column.setAttributeName(calculation.getConcatenatedAttributeName());
        }
        return column;
    }

    public void setAttributeDefinition(AttributeWrapperBean newAttributeWrapperBean) {

        // if changing dynamic attribute reset wrapper state - null checks required as new columns have no dynamic attribute assigned
        if (attributeDefinition != null && newAttributeWrapperBean != null) {
            final Long currentId = attributeDefinition.getAttributeDefinition().getId();
            final Long newId = newAttributeWrapperBean.getAttributeDefinition().getId();

            if (currentId != null && !currentId.equals(newId)) {
                reset();
            }
        }

        // save attribute wrapper
        super.setAttributeDefinition(newAttributeWrapperBean);
    }

    public String getLabel() {
        return column.getLabel();
    }

    public void setLabel(String label) {
        column.setLabel(label);
    }

    public boolean isColorDisplayable() {
        return column.isColorDisplayable();
    }

    public boolean isUseColours() {
        return column.isUseColours();
    }

    /**
     * Check if column can be used for ordering.
     * <br/> Returns false if the column is grouped,
     * or it is an associatedArtefact attribute,
     * or it is a dynamic attribute question and the dynamic attribute it refers to supportsMultipleAnswers.
     * or it is a dynamic attribute question that is a dynamic lineItem
     *
     * @return true or false
     */
    public boolean isOrderable() {

        if (isGrouped()) return false;
        if (isDynamicLineItem()) return false;
        if (isFormula()) return false;
        if (isMultiSelect()) return false;

        final AnalysisParameter analysisParameter = column.getAnalysisParameter();
        if (analysisParameter == null) return false;
        if (analysisParameter.isDerivedAttribute()) return false;
        if (analysisParameter.isDocument()) return false;
        if (analysisParameter.isAssociatedArtefactAttribute()) return false;
        if (analysisParameter.isDynamicAttribute()) {
            if( attributeDefinition != null && attributeDefinition.supportsMultipleAnswers()) {
                return false;
            }
        }
        return true;
    }

    private boolean isDynamicLineItem() {
        return column.isDynamicLineItem();
    }

    public void setColorDisplayable(boolean colorDisplayable) {
        column.setColorDisplayable(colorDisplayable);
    }

    public String getColumnSource() {
        return column.getColumnSource();
    }

    public void setColumnSource(String columnSource) {
        column.setColumnSource(columnSource);
    }

    public String getColumnType() {
        return column.getColumnType();
    }

    public void setColumnType(String columnType) {
        column.setColumnType(columnType);
    }

    public String getDisplayColour() {
        return column.getDisplayColour();
    }

    public void setDisplayColour(String value) {
        column.setDisplayColour(value);
    }

    public BasicAnalysisAttribute getAnalysisAttribute() {
        return column;
    }

    public Metric getMetric() {
        return column.getMetric();
    }

    public void setMetricId(Long metricId) {
        if (metricId != null) {
            column.setMetric(new Metric(metricId));
        } else {
            column.setMetric(IPopulationEngine.COUNT_METRIC);
        }
    }

    public Long getMetricId() {
        return (getMetric() != null ? column.getMetric().getId() : null);
    }

    public Integer getColumnPosition() {
        return column.getPosition();
    }

    public void setColumnPosition(Integer position) {
        column.setPosition(position);
    }

    public boolean isMandatory() {
        return column.isMandatory();
    }

    /**
     * Does the column support colours.
     *
     * @return true or false
     */
    public boolean isSupportColours() {

        boolean supportsColours = false;

        if (attributeDefinition != null) {
            final DynamicAttribute dynamicAttribute = attributeDefinition.getAttributeDefinition();
            if (dynamicAttribute != null) {
                supportsColours = supportsColours(dynamicAttribute);
            }
        }

        return supportsColours;
    }

    /**
     * Get list of ColumnDisplayImage objects.
     * <br/> Never returns a null list.
     *
     * @return Collection of {@link ColumnDisplayImage} objects
     */
    public List getColumnDisplayImages() {        
        if (columnDisplayImages == null) {
            columnDisplayImages = new ArrayList<ColumnDisplayImage>();
        }

        return columnDisplayImages;
    }

    /**
     * Sets the colours.
     *
     * @param values The list of selected colours.
     */
    public void setSelectedColours(String[] values) {
        
        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            ColumnDisplayImage columnDisplayImage = getColumnDisplayImage(i);
            if (columnDisplayImage != null) {
                columnDisplayImage.setDisplayImage(value);
            }
        }
    }

    public void initColumnDisplayImages(DynamicAttribute newDynamicAttribute) {

        if (attributeDefinition != null && newDynamicAttribute != null) {
            buildColumnDisplayImages(newDynamicAttribute);

            // overwrite dynamic attribute on attribute wrapper
            attributeDefinition.setAttributeDefinition(newDynamicAttribute);
        }
    }

    public boolean isFormula() {
        return column.isFormula() && column.isHasExpressions();
    }

    public boolean isMultiSelect() {
        return DynamicAttribute.DA_TYPE_MULTISELECT.equals(column.getColumnType());
    }

    public FunctionWrapperBean getFunctionWrapperBean() {
        return functionWrapperBean;
    }

    public void setFunctionWrapperBean(FunctionWrapperBean functionWrapperBean) {
        this.functionWrapperBean = functionWrapperBean;
    }

    public boolean isSystemAttribute() {
        final AnalysisParameter attribute = column.getAnalysisParameter();
        return attribute != null && attribute.isSystemAttribute(column.getReport().getReportType());
    }

    /**
     * Groups are only supported if the column attribute is an organisation unit attribute or a selection type attribute.
     * return attribute != null && !attribute.isAssociatedArtefactAttribute()
     * && attributeDefinition != null && !attributeDefinition.isDynamic()
     * && (attributeDefinition.isSelectionType() || attributeDefinition.isOrganisationUnit() || attributeDefinition.isMappingType());
     *
     * @return true if the attribute is not an associated artefact attribute and represents a selection type or the organisation unit or a mapping.
     */
    public boolean isSupportsGroups() {
        boolean supportsGroups = false;

        if (column != null) {
            final AnalysisParameter attribute = column.getAnalysisParameter();
            if (attribute != null) {
                if (attribute.isAssociatedArtefactAttribute()) {
                    // can only be one level nested for the organisation unit for people
                    if (attribute.getName().startsWith(IPopulationEngine.PERSONS_ORGUNIT_ATTRIBUTE_NAME)) supportsGroups = true;
                    if (attribute.getName().startsWith(IPopulationEngine.PERSONS_ORGUNIT_ATTRIBUTE)) supportsGroups = true;
                }
                if(attribute.isOrganisationUnitLabelAttribute()) supportsGroups = true;

                if (attributeDefinition != null && (attributeDefinition.isSelectionType() || attributeDefinition.isMappingType())) {
                    supportsGroups = true;
                }
            }
        }

        return supportsGroups;
    }

    /**
     * Defines a column that is to be grouped.
     *
     * @param grouped
     */
    public void setGrouped(boolean grouped) {
        column.setGrouped(grouped);
    }

    public boolean isGrouped() {
        return column.isGrouped();
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public boolean isMetricSelected() {
        return metricSelected;
    }

    public void setMetricSelected(boolean metricSelected) {
        this.metricSelected = metricSelected;
    }

    private ColumnDisplayImage getColumnDisplayImage(int i) {
        final List displayImages = getColumnDisplayImages();
        try {
            return (ColumnDisplayImage) displayImages.get(i);
        } catch (IndexOutOfBoundsException e) {
        }

        return null;
    }

    /**
     * Reset column state when attribute changes.
     */
    private void reset() {
        setGrouped(false);
        column.setColorDisplayable(false);
        column.getColumnDisplayImages().clear();
        columnDisplayImages = null;
        column.setDynamicLineItem(false);
    }

    /**
     * Get column display images that have been assigned colours.
     *
     * @return Collection of {@link ColumnDisplayImage} objects or an empty collection.
     */
    private Collection<ColumnDisplayImage> getAssignedColumnDisplayImages() {

        final Collection<ColumnDisplayImage> assignedColumnDisplayImages = new HashSet<ColumnDisplayImage>();

        if (columnDisplayImages != null) {
            for (Iterator iterator = columnDisplayImages.iterator(); iterator.hasNext();) {
                ColumnDisplayImage columnDisplayImage = (ColumnDisplayImage) iterator.next();
                if (StringUtils.hasText(columnDisplayImage.getDisplayImage())) {
                    assignedColumnDisplayImages.add(columnDisplayImage);
                }
            }
        }

        return assignedColumnDisplayImages;
    }

    private void buildColumnDisplayImages(DynamicAttribute dynamicAttribute) {

        if (dynamicAttribute != null && supportsColours(dynamicAttribute)) {

            if (columnDisplayImages == null) {
                columnDisplayImages = new ArrayList<ColumnDisplayImage>();
            }

            final Collection lookupValues = dynamicAttribute.getRefersToType().getLookupValues();
            for (Iterator iterator = lookupValues.iterator(); iterator.hasNext();) {
                LookupValue lookupValue = (LookupValue) iterator.next();

                // check for matching ColumnDisplayImage in column - if not found, create a new empty one
                ColumnDisplayImage columnDisplayImage = column.getColumnDisplayImage(lookupValue);
                if (columnDisplayImage == null) {
                    columnDisplayImage = new ColumnDisplayImage(lookupValue, null);
                }

                // check for value in existing columnDisplayImages collection - if not found, add it
                final ColumnDisplayImage matchingColumnDisplayImage = (ColumnDisplayImage) CollectionUtils.find(columnDisplayImages, new ColumnDisplayImagePredicate(lookupValue));
                if (matchingColumnDisplayImage == null) {
                    columnDisplayImages.add(columnDisplayImage);
                }
            }
        }
    }

    private boolean supportsColours(DynamicAttribute attribute) {
        return (attribute.isSelectionType() || attribute.isMappingType()) && !attribute.isDynamic() && !AnalysisAttributeHelper.isQualifierAttribute(column.getAttributeName());
    }

    public void setIsOrgunitBranch(boolean orgUnitBranch) {
        this.orgunitBranch = orgUnitBranch;
    }

    public boolean isOrgunitBranch() {
        return orgunitBranch;
    }

    private class ColumnDisplayImagePredicate implements Predicate {

        private LookupValue lookupValue;

        public ColumnDisplayImagePredicate(LookupValue lookupValue) {
            this.lookupValue = lookupValue;
        }

        public boolean evaluate(Object object) {
            ColumnDisplayImage columnDisplayImage = (ColumnDisplayImage) object;
            final LookupValue displayImageLookupValue = columnDisplayImage.getLookupValue();
            return displayImageLookupValue != null && displayImageLookupValue.equals(lookupValue);
        }
    }

    private FunctionWrapperBean functionWrapperBean;
    private final Column column;
    private List<ColumnDisplayImage> columnDisplayImages = null;
    private boolean metricSelected = false;
    private Integer sortOrder = new Integer(0);
    private boolean orgunitBranch;
}

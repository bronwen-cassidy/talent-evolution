/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports;

import com.zynap.talentstudio.analysis.BasicAnalysisAttribute;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.metrics.Metric;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.calculations.Calculation;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.*;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class Column extends BasicAnalysisAttribute implements Cloneable {

    public Column() {
    }

    public Column(String label) {
        this.label = label;
    }

    public Column(Long id, String label) {
        super(id);
        this.label = label;
    }

    /**
     * Constructor.
     *
     * @param label         the label (displayable value) of the column
     * @param attributeName the value that references a node attribute (a core attribute, dynamicAttribute or derived attribute)
     * @param columnType    the type of value the column represents, a number, text, struct DynamicAttribute type
     */
    public Column(String label, String attributeName, String columnType) {
        this.label = label;
        this.attributeName = attributeName;
        this.columnType = columnType;
    }

    public Column(String label, Report report) {
        this.label = label;
        this.report = report;
    }

    /**
     * Constructor.
     *
     * @param label         the label (displayable value) of the column
     * @param attributeName the value that references a node attribute (a core attribute, dynamicAttribute or derived attribute)
     * @param index         the index of the column in relation to all the other columns
     * @param columnType    the type of value the column represents, a number, text, struct DynamicAttribute type
     */
    public Column(String label, String attributeName, Integer index, String columnType) {
        this.label = label;
        this.attributeName = attributeName;
        this.position = index;
        this.columnType = columnType;
    }

    /**
     * Constructor.
     *
     * @param label         the label (displayable value) of the column
     * @param attributeName the value that references a node attribute (a core attribute, dynamicAttribute or derived attribute)
     * @param position      the index of the column in relation to all the other columns
     * @param columnType    the type of value the column represents, a number, text, struct DynamicAttribute type
     * @param columnSource  the source direction of the column, it is either from a position population or from a person population, hence it
     *                      is created via the {@link com.zynap.talentstudio.common.Direction#POSITION_LAB } or {@link com.zynap.talentstudio.common.Direction#PERSON_LAB}
     *                      if it is any report other than CrossTab report. CrossTab reports have a columnSource value of
     *                      {@link com.zynap.talentstudio.common.Direction#VERTICAL } or {@link com.zynap.talentstudio.common.Direction#HORIZONTAL }
     */
    public Column(String label, String attributeName, Integer position, String columnType, String columnSource) {
        this(label, attributeName, position, columnType);
        this.columnSource = columnSource;
    }

    public Column(Long id, String label, String attributeName, Integer position, String columnType, String columnSource) {
        this(label, attributeName, position, columnType, columnSource);
        setId(id);
    }

    public Column(Long dynamicAttributeId, Long questionnaireWorkflowId) {
        this.attributeName = String.valueOf(dynamicAttributeId);
        this.questionnaireWorkflowId = questionnaireWorkflowId;
    }

    public Column(String label, Long dynamicAttributeId, Long questionnaireWorkflowId) {
        this(dynamicAttributeId, questionnaireWorkflowId);
        this.label = label;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public Report getReport() {
        return report;
    }

    public String getColumnSource() {
        return columnSource;
    }

    public void setColumnSource(String columnSource) {
        this.columnSource = columnSource;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getWeighting() {
        return weighting;
    }

    public void setWeighting(Integer weighting) {
        this.weighting = weighting;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public Metric getMetric() {
        return metric;
    }

    public void setMetric(Metric metric) {
        this.metric = metric;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("label", getLabel())
                .append("columnSource", getColumnSource())
                .append("attributeName", getAttributeName())
                .append("position", getPosition())
                .append("columnType", getColumnType())
                .toString();
    }

    public boolean isSystemAttribute() {
        return getAnalysisParameter().isSystemAttribute(report.getReportType());
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    /**
     * @return Set of {@link ColumnDisplayImage } objects
     */
    public Collection<ColumnDisplayImage> getColumnDisplayImages() {
        return columnDisplayImages;
    }

    public void setColumnDisplayImages(Collection<ColumnDisplayImage> columnDisplayImages) {
        this.columnDisplayImages = columnDisplayImages;
        if (this.columnDisplayImages != null && !this.columnDisplayImages.isEmpty()) {
            Collections.sort((List<ColumnDisplayImage>) this.columnDisplayImages);
        }
    }

    public void assignNewColumnDisplayImages(Collection<ColumnDisplayImage> newDisplayImages) {
        columnDisplayImages.clear();

        if (newDisplayImages != null) {
            for (ColumnDisplayImage newDisplayImage : newDisplayImages) {
                addColumnDisplayImage(newDisplayImage);
            }
        }
    }

    public void addColumnDisplayImage(ColumnDisplayImage columnDisplayImage) {
        columnDisplayImage.setColumn(this);
        columnDisplayImages.add(columnDisplayImage);
    }

    public void assignNewColumnAttributes(Collection<ChartColumnAttribute> newAttributes) {
        chartColumnAttributes.clear();

        if (newAttributes != null) {
            for (ChartColumnAttribute attribute : newAttributes) {
                addColumnAttribute(attribute);
            }
        }
    }

    public void addColumnAttribute(ChartColumnAttribute attr) {
        attr.setColumn(this);
        chartColumnAttributes.add(attr);
    }

    public boolean isColorDisplayable() {
        return colorDisplayable;
    }

    public boolean isUseColours() {
        return isColorDisplayable() && columnDisplayImages != null && !columnDisplayImages.isEmpty();
    }

    public boolean isGrouped() {
        return grouped;
    }

    public void setGrouped(boolean grouped) {
        this.grouped = grouped;
    }

    public void setColorDisplayable(boolean colorDisplayable) {
        this.colorDisplayable = colorDisplayable;
    }

    public ColumnDisplayImage getColumnDisplayImage(LookupValue lookupValue) {
        for (Iterator iterator = columnDisplayImages.iterator(); iterator.hasNext();) {
            ColumnDisplayImage columnDisplayImage = (ColumnDisplayImage) iterator.next();
            if (columnDisplayImage.matches(lookupValue)) {
                return columnDisplayImage;
            }
        }

        return null;
    }

    public String getColumnDisplayImageValue(LookupValue lookupValue) {
        ColumnDisplayImage columnDisplayImage = getColumnDisplayImage(lookupValue);
        return columnDisplayImage != null ? columnDisplayImage.getDisplayImage() : null;
    }

    public Set<ChartColumnAttribute> getChartColumnAttributes() {
        return chartColumnAttributes;
    }

    public void setChartColumnAttributes(Set<ChartColumnAttribute> chartColumnAttributes) {
        this.chartColumnAttributes = chartColumnAttributes;
    }

    /**
     * Shallow clone the report and the columnDisplayImages are the same instances any modification to these will
     * reflect through.
     *
     * @return a clone
     * @throws CloneNotSupportedException
     */
    public final Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return new Column(id, label, attributeName, position, columnType, columnSource);
        }
    }

    public boolean isFormula() {
        return formula;
    }

    public void setFormula(boolean formula) {
        this.formula = formula;
    }

    public String getDisplayColour() {
        return displayColour;
    }

    public void setDisplayColour(String displayColour) {
        this.displayColour = displayColour;
    }

    /**
     * Answers the question does this columns attribute reference a dynamicAttribute which is a dynamicLineItem.
     *
     * @return true if the column references a dynamicAttribute which is a dynamicLineItem
     */
    public boolean isDynamicLineItem() {
        return dynamicLineItem;
    }

    public void setDynamicLineItem(boolean dynamicLineItem) {
        this.dynamicLineItem = dynamicLineItem;
    }

    public void setCalculation(Calculation calculation) {
        this.calculation = calculation;
    }

    public Calculation getCalculation() {
        return calculation;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCollectionPrefix() {
        if(isFormula()) return null;
        return AnalysisAttributeHelper.getCollectionPrefix(attributeName);
    }

    public boolean isHasExpressions() {
        return calculation != null && !calculation.getExpressions().isEmpty();
    }

    public boolean isSelectionType() {
        return columnType != null && (columnType.equals(DynamicAttribute.DA_TYPE_SELECT) || columnType.equals(DynamicAttribute.DA_TYPE_STRUCT));
    }

    public boolean isEnumMappingType() {
        return columnType != null && (columnType.equals(DynamicAttribute.DA_TYPE_ENUM_MAPPING));
    }

    public boolean isProgressRowHeader() {
        return ReportConstants.WORKFLOW_NAME.equals(attributeName);
    }

    public void removeChartColumnAttribute(ChartColumnAttribute chartColumnAttribute) {
        chartColumnAttributes.remove(chartColumnAttribute);
    }

	public DynamicAttribute getDynamicAttribute() {
		return dynamicAttribute;
	}

	public void setDynamicAttribute(DynamicAttribute dynamicAttribute) {
		this.dynamicAttribute = dynamicAttribute;
	}

	public static final String X_AXIS_SOURCE = "X-AXIS";
	public static final String Y_AXIS_SOURCE = "Y-AXIS";

	private String columnSource = "NA";
    public static final String NO_VALUE = "_NULL_";

    /* this is used in chart reports to determine the colour of the bar or pie when generating the chart */    
    private String displayColour;
    private Report report;
    private Integer position;
    private Integer weighting;
    private String columnType;
    /* only used in chart reports the value we are looking for is the label of some lookup value, we are interested in counting these rather than real references*/
    private String value;
    private Metric metric;
    private boolean mandatory;
    private boolean grouped;
    private boolean colorDisplayable;
    private boolean formula;
    private boolean dynamicLineItem;

    /* the display image values associated with a column */
    private Collection<ColumnDisplayImage> columnDisplayImages = new ArrayList<ColumnDisplayImage>();
    private Set<ChartColumnAttribute> chartColumnAttributes = new LinkedHashSet<ChartColumnAttribute>();
    private Calculation calculation;

	private DynamicAttribute dynamicAttribute;
}

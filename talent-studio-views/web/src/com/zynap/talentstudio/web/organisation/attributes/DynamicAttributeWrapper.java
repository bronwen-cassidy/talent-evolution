package com.zynap.talentstudio.web.organisation.attributes;

import com.zynap.talentstudio.common.lookups.LookupType;
import com.zynap.talentstudio.calculations.Calculation;
import com.zynap.talentstudio.calculations.DateCalculation;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.DynamicAttributeDTO;
import com.zynap.talentstudio.calculations.Expression;
import com.zynap.talentstudio.util.FormatterFactory;
import com.zynap.talentstudio.util.IFormatter;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author jsuiras
 * @version 0.1
 * @since 07-Feb-2005 15:38:37
 */
public class DynamicAttributeWrapper implements Serializable {

    public DynamicAttributeWrapper(DynamicAttribute attributeDefinition) {
        this.attributeDefinition = attributeDefinition;
        this.calculated = attributeDefinition.isCalculated();
    }

    public boolean isSelectionType() {
        return attributeDefinition != null && (attributeDefinition.isSelectionType() || attributeDefinition.isMultiSelectionType());
    }

    public void setType(String type) {
        attributeDefinition.setType(type);
        reset();
    }

    public String getType() {
        return attributeDefinition.getType();
    }

    public DynamicAttribute getModifiedAttributeDefinition() {

        if (calculated) {

            attributeDefinition.setCalculated(calculated);
            // loop the expressionWrappers
            Calculation calculation = attributeDefinition.getCalculation();
            if (calculation == null) calculation = new DateCalculation(attributeDefinition.getType());
            calculation.getExpressions().clear();            
            for (int i = 0; i < numExpressions; i++) {
                
                ExpressionWrapper expressionWrapper = expressions.get(i);
                Expression expression = expressionWrapper.getModifiedExpression();
                expression.setIndex(i);
                // the last expression should never have an operator
                if(i == (numExpressions - 1)) expression.setOperator(null);
                calculation.addExpression(expression);
            }
            if(numExpressions < expressions.size()) {
                for(int i = numExpressions; i < expressions.size(); i++) {
                    expressions.remove(i);
                }
            }
            attributeDefinition.setCalculation(calculation);

        } else {

            if (attributeDefinition.typeMatches(DynamicAttribute.DA_TYPE_TIMESTAMP)) {
                attributeDefinition.setMinSize(getTimeValue(minHour, minMinute));
                attributeDefinition.setMaxSize(getTimeValue(maxHour, maxMinute));
            } else if (attributeDefinition.typeMatches(DynamicAttribute.DA_TYPE_DATETIMESTAMP)) {
                attributeDefinition.setMinSize(getDateTimeValue(minDate, minHour, minMinute));
                attributeDefinition.setMaxSize(getDateTimeValue(maxDate, maxHour, maxMinute));
            } else if (attributeDefinition.typeMatches(DynamicAttribute.DA_TYPE_DATE)) {
                attributeDefinition.setMinSize(minDate);
                attributeDefinition.setMaxSize(maxDate);
            }
        }

        return attributeDefinition;
    }

    private String getDateTimeValue(String date, String hours, String minutes) {
        String timeValue = getTimeValue(hours, minutes);
        if (!StringUtils.hasText(timeValue) && !StringUtils.hasText(date)) {
            return null;
        }
        return date + " " + timeValue;
    }

    private String getTimeValue(String hours, String minutes) {

        if (!StringUtils.hasText(hours) && !StringUtils.hasText(minutes)) {
            return "";
        }
        return hours + IFormatter.TIME_DELIMITER + minutes;
    }

    public Integer getDecimalPlaces() {
        return attributeDefinition.getDecimalPlaces();
    }

    public void setDecimalPlaces(Integer value) {
        attributeDefinition.setDecimalPlaces(value);
    }

    public String getMinSize() {
        return attributeDefinition.getMinSize();
    }

    public String getMaxSize() {
        return attributeDefinition.getMaxSize();
    }

    public void setMinSize(String minsize) {
        attributeDefinition.setMinSize(minsize);
    }

    public void setMaxSize(String maxsize) {
        attributeDefinition.setMaxSize(maxsize);
    }

    public String[] getMinTime() {
        if (StringUtils.hasText(attributeDefinition.getMinSize())) {
            return StringUtils.delimitedListToStringArray(attributeDefinition.getMinSize(), IFormatter.TIME_DELIMITER);
        }
        return new String[2];
    }

    public void setMaxHour(String value) {
        this.maxHour = value;
    }

    public void setMinHour(String value) {
        this.minHour = value;
    }

    public void setMinMinute(String value) {
        this.minMinute = value;
    }

    public void setMaxMinute(String value) {
        this.maxMinute = value;
    }

    public String[] getMaxTime() {
        if (StringUtils.hasText(attributeDefinition.getMaxSize())) {
            return StringUtils.delimitedListToStringArray(attributeDefinition.getMaxSize(), IFormatter.TIME_DELIMITER);
        }
        return new String[2];
    }

    public boolean isMandatory() {
        return attributeDefinition.isMandatory();
    }

    public void setMandatory(boolean mandatory) {
        attributeDefinition.setMandatory(mandatory);
    }

    public void setLabel(String label) {
        attributeDefinition.setLabel(label);
    }

    public String getLabel() {
        return attributeDefinition.getLabel();
    }

    public void setDescription(String description) {
        attributeDefinition.setDescription(description);
    }

    public String getDescription() {
        return attributeDefinition.getDescription();
    }

    public void setActive(boolean value) {
        attributeDefinition.setActive(value);
    }

    public boolean isActive() {
        return attributeDefinition.isActive();
    }

    public void setSearchable(boolean value) {
        attributeDefinition.setSearchable(value);
    }

    public boolean isSearchable() {
        return attributeDefinition.isSearchable();
    }

    public void setRefersTo(String value) {
        attributeDefinition.setRefersToType(new LookupType(value));
    }

    public String getRefersTo() {
        final LookupType refersToType = attributeDefinition.getRefersToType();
        return refersToType != null ? refersToType.getTypeId() : null;
    }

    public String getMinDate() {
        return FormatterFactory.getDateFormatter().formatDateAsString(attributeDefinition.getMinSize());
    }

    public void setMinDate(String value) {
        this.minDate = value;
    }

    public String getMaxDate() {
        return FormatterFactory.getDateFormatter().formatDateAsString(attributeDefinition.getMaxSize());
    }

    public void setMaxDate(String value) {
        this.maxDate = value;
    }

    public String getMinDateTimeDisplay() {
        String minSize = attributeDefinition.getMinSize();
        return StringUtils.hasText(minSize) ? FormatterFactory.getDateFormatter().formatDateTimeAsString(minSize) : null;
    }

    public String getMaxDateTimeDisplay() {
        String maxSize = attributeDefinition.getMaxSize();
        return StringUtils.hasText(maxSize) ? FormatterFactory.getDateFormatter().formatDateTimeAsString(maxSize) : null;
    }

    public String[] getMinDateTime() {
        String minSize = attributeDefinition.getMinSize();
        return getValues(minSize);
    }

    private String[] getValues(String size) {
        String[] values = new String[3];
        if (StringUtils.hasText(size)) {
            String[] dateAndTime = StringUtils.delimitedListToStringArray(size, " ");
            values[0] = dateAndTime[0];

            String[] timeValues = StringUtils.delimitedListToStringArray(dateAndTime[1], ":");
            if (timeValues.length == 2) {
                values[1] = timeValues[0];
                values[2] = timeValues[1];
            }
        }
        return values;
    }

    public String getMaxDateTimeDisplayValue() {
        String[] values = getValues(attributeDefinition.getMaxSize());
        if (values.length > 1) {
            return FormatterFactory.getDateFormatter().formatDateAsString(values[0]);
        }
        return "";
    }

    public String getMinDateTimeDisplayValue() {
        String[] values = getValues(attributeDefinition.getMinSize());
        if (values.length > 1) {
            return FormatterFactory.getDateFormatter().formatDateAsString(values[0]);
        }
        return "";
    }

    public String[] getMaxDateTime() {
        String maxSize = attributeDefinition.getMaxSize();
        return getValues(maxSize);
    }

    public String getArtefactType() {
        return attributeDefinition.getArtefactType();
    }

    public void setArtefactType(String artefactType) {
        attributeDefinition.setArtefactType(artefactType);
    }

    public Long getId() {
        return attributeDefinition.getId();
    }

    public String getRefersToTypeLabel() {
        if(attributeDefinition != null && attributeDefinition.getRefersToType() != null) return attributeDefinition.getRefersToType().getLabel();
        return null;
    }


    public boolean isCalculated() {
        return calculated;
    }

    public void setCalculated(boolean calculated) {
        this.calculated = calculated;
    }

    public String getFieldName() {

        if (attributeDefinition.typeMatches(DynamicAttribute.DA_TYPE_DATE)) {
            return ".date";
        } else if (attributeDefinition.typeMatches(DynamicAttribute.DA_TYPE_DATETIMESTAMP)) {
            return ".dateTime";
        } else if (attributeDefinition.typeMatches(DynamicAttribute.DA_TYPE_TIMESTAMP)) {
            return ".time";
        } else if (attributeDefinition.typeMatches(DynamicAttribute.DA_TYPE_IMAGE)) {
            return ".fileName";
        } else {
            return ".value";
        }
    }

    private void reset() {

        maxMinute = null;
        maxHour = null;
        maxDate = null;
        minMinute = null;
        minHour = null;
        minDate = null;

        // clear min size and max size
        // - important so that changing type of dynamic attribute clears values on definition
        if (attributeDefinition != null) {
            attributeDefinition.setMaxSize(null);
            attributeDefinition.setMinSize(null);
            attributeDefinition.setDecimalPlaces(null);
            attributeDefinition.setCalculation(null);
        }
    }

    public void setAttributes(Collection<DynamicAttributeDTO> attributes) {
        this.attributes = attributes;
    }

    public Collection<DynamicAttributeDTO> getAttributes() {
        return attributes;
    }


    public int getNumExpressions() {
        return numExpressions;
    }

    public void setNumExpressions(int numExpressions) {
        this.numExpressions = numExpressions;
    }

    public void setExpressions(List<ExpressionWrapper> expressions) {
        this.expressions = expressions;
        this.numExpressions = expressions.size();
    }

    public List<ExpressionWrapper> getExpressions() {
        return expressions;
    }

    private DynamicAttribute attributeDefinition;
    private String maxHour;
    private String maxMinute;
    private String minHour;
    private String minMinute;
    private String maxDate;
    private String minDate;
    private boolean calculated;
    private Collection<DynamicAttributeDTO> attributes;
    private List<ExpressionWrapper> expressions = new ArrayList<ExpressionWrapper>();
    private int numExpressions;
}

/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.metrics;

import com.zynap.talentstudio.analysis.BasicAnalysisAttribute;
import com.zynap.talentstudio.analysis.metrics.Metric;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.web.analysis.AnalysisAttributeWrapperBean;
import com.zynap.talentstudio.web.analysis.picker.AnalysisAttributeCollection;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class MetricWrapper extends AnalysisAttributeWrapperBean {

    public MetricWrapper(Metric metric) {
        this.metric = metric;
        this.scopeChangeable = true;
    }

    public MetricWrapper(Metric metric, AnalysisAttributeCollection attributeCollection) {
        this(metric);

        this.attributeCollection = attributeCollection;
        checkAttribute();
    }

    public Metric getMetric() {
        return this.metric;
    }

    public void setMetric(Metric metric) {
        this.metric = metric;
    }

    public Metric getModifiedMetric() {
        return metric;
    }

    public BasicAnalysisAttribute getAnalysisAttribute() {
        return metric;
    }

    public boolean isDerivedAttribute() {
        return attributeDefinition != null && attributeDefinition.isDerivedAttribute();
    }

    /**
     * Method used to determine if metric artefact type has been set.
     * <br/> Only used in JSPs.
     *
     * @return true if metric has artefact type
     */
    public boolean isTypeSet() {
        return StringUtils.hasText(getType());
    }

    public boolean isOperatorSet() {
        return StringUtils.hasText(getOperator());
    }

    public boolean isCountOperator() {
        return isOperatorSet() && IPopulationEngine.COUNT.equals(getOperator());
    }

    public boolean isPickerEnabled() {
        return isTypeSet() && isOperatorSet();
    }

    /**
     * Method used to determine if type and metric attribute have been set.
     * <br/> Only used in JSPs.
     *
     * @return true if metric has attribute name
     */
    public boolean isAttributeSet() {
        return isTypeSet() && super.isAttributeSet();
    }

    /**
     * Method to clear state when type changes.
     */
    public void reset() {
        setAttribute(null);
    }

    public void setLabel(String name) {
        this.metric.setLabel(name);
    }

    public String getLabel() {
        return metric.getLabel();
    }

    public void setType(String type) {
        this.metric.setArtefactType(type);
    }

    public String getType() {
        return metric.getArtefactType();
    }

    public void setAccess(String access) {
        this.metric.setAccessType(access);
    }

    public String getAccess() {
        return metric.getAccessType();
    }

    public void setDescription(String description) {
        this.metric.setDescription(description);
    }

    public String getDescription() {
        return metric.getDescription();
    }

    public String getOperator() {
        return metric.getOperator();
    }

    public void setOperator(String value) {
        metric.setOperator(value);
    }

    public void setValue(String value) {
        metric.setValue(value);
    }

    public String getValue() {
        return metric.getValue();
    }

    public String getDisplayValue() {
        if(attributeDefinition == null) return getRefValue();

        DynamicAttribute definition = attributeDefinition.getAttributeDefinition();
        if(definition.isBlogComment() || definition.isTextAttribute() || definition.isNumericType()) {
            return getRefValue();
        }
        
        return attributeDefinition.getDisplayValue();
    }

//    public void setAttributeDefinition(AttributeWrapperBean attributeDefinition) {
//        if(attributeDefinition != null) attributeDefinition.setValue(getValue());
//        super.setAttributeDefinition(attributeDefinition);
//    }

    public Collection getActiveLookupValues() {
        if(attributeDefinition == null) return new ArrayList();
        return attributeDefinition.getActiveLookupValues();
    }

    public String getRefValue() {
        return metric.getValue();
    }

    public AnalysisAttributeCollection getAttributeCollection() {
        return attributeCollection;
    }

    public void setAttributeCollection(AnalysisAttributeCollection attributeCollection) {
        this.attributeCollection = attributeCollection;
    }

    /**
     * Check that the metric has the correct AttributeWrapperBean set (the AttributeWrapperBean contains the dynamic attribute definition.)
     */
    public void checkAttribute() {

        if (this.hasAttributeName()) {
            boolean definitionNotSet = (attributeDefinition == null);
            if (this.hasIncorrectAttributeDefinition()) {
                attributeCollection.setAttributeDefinition(this);
            }
            if(definitionNotSet && attributeDefinition != null) {
                attributeDefinition.setValue(getValue());
            }
        }
    }

    public boolean isScopeChangeable() {
        return scopeChangeable;
    }

    public void setScopeChangeable(boolean scopeChangeable) {
        this.scopeChangeable = scopeChangeable;
    }

    private Metric metric;

    private AnalysisAttributeCollection attributeCollection;

    private boolean scopeChangeable = true;
}

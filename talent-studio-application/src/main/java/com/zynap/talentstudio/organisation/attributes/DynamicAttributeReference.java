/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.organisation.attributes;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.talentstudio.common.lookups.LookupValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 18-Aug-2006 12:08:15
 */
public class DynamicAttributeReference extends ZynapDomainObject {

    public DynamicAttribute getParentDa() {
        return parentDa;
    }

    public void setParentDa(DynamicAttribute parentDa) {
        this.parentDa = parentDa;
    }

    public DynamicAttribute getReferenceDa() {
        return referenceDa;
    }

    public void setReferenceDa(DynamicAttribute referenceDa) {
        this.referenceDa = referenceDa;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString() {
        return new StringBuffer("Parent dynamicAttribute: ")
                .append(parentDa != null ? parentDa.getLabel() : "null")
                .append(" referenced DynamicAttribute: ")
                .append(referenceDa != null ? referenceDa.getLabel() : "null")
                .toString();
    }

    public Integer getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(Integer upperBound) {
        this.upperBound = upperBound;
    }

    public Integer getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(Integer lowerBound) {
        this.lowerBound = lowerBound;
    }

    public DynamicAttributeReference getParent() {
        return parent;
    }

    public void setParent(DynamicAttributeReference parent) {
        this.parent = parent;
    }

    public LookupValue getLookupValue() {
        return lookupValue;
    }

    public void setLookupValue(LookupValue lookupValue) {
        this.lookupValue = lookupValue;
    }

    public void addChild(DynamicAttributeReference child) {
        child.setParent(this);
        this.children.add(child);
    }

    public List getChildren() {
        return children;
    }

    public void setChildren(List<DynamicAttributeReference> children) {
        this.children = children;
    }

    private DynamicAttribute parentDa;
    private DynamicAttribute referenceDa;
    private DynamicAttributeReference parent;
    private LookupValue lookupValue;

    private List<DynamicAttributeReference> children = new ArrayList<DynamicAttributeReference>();

    private String type;
    private Integer upperBound;
    private Integer lowerBound;

    private static final long serialVersionUID = 5176885739598408700L;
}

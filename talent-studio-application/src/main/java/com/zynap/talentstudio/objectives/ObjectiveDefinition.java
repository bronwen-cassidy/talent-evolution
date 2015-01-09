/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.objectives;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;

import java.util.*;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 12-Mar-2007 15:22:08
 */
public class ObjectiveDefinition extends ZynapDomainObject implements ObjectiveConstants {

    public ObjectiveDefinition() {

    }

    public ObjectiveDefinition(Long id) {
        super(id);
    }

    public List<DynamicAttribute> getDynamicAttributes() {
        return dynamicAttributes;
    }

    public void setDynamicAttributes(List<DynamicAttribute> dynamicAttributes) {
        this.dynamicAttributes = dynamicAttributes;
    }

    public Set<ObjectiveSet> getObjectiveSets() {
        return objectiveSets;
    }

    public void setObjectiveSets(Set<ObjectiveSet> objectiveSets) {
        this.objectiveSets = objectiveSets;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void addObjectiveSet(ObjectiveSet objectiveSet) {
        objectiveSet.setObjectiveDefinition(this);
        objectiveSets.add(objectiveSet);
    }

    public void addDynamicAttribute(DynamicAttribute dynamicAttribute) {
        dynamicAttributes.add(dynamicAttribute);
    }

    public List<DynamicAttribute> getSortedAttributes() {
        Collections.sort(dynamicAttributes);
        return dynamicAttributes;
    }

    public void initLazy() {
        for (Iterator<DynamicAttribute> iterator = dynamicAttributes.iterator(); iterator.hasNext();) {
            DynamicAttribute dynamicAttribute = iterator.next();
            if (dynamicAttribute.isSelectionType()) {
                dynamicAttribute.getRefersToType().getActiveLookupValues();
            }
        }
    }

    /**
     * The group of attrbiutes that define this objective definition.
     */
    private List<DynamicAttribute> dynamicAttributes = new ArrayList<DynamicAttribute>();

    /**
     * All the sets of objectives for all people, business units, or corporate that belong to this class.
     */
    private Set<ObjectiveSet> objectiveSets = new HashSet<ObjectiveSet>();

    private String description;
    private String status = STATUS_OPEN;
    private Date createdDate;
}

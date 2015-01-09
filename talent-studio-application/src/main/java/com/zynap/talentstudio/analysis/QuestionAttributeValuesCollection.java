/* Copyright: Copyright (c) 2004
 * Company:
 */
package com.zynap.talentstudio.analysis;

import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Class that wraps a Map containing AttributeValues.
 *
 * @author Angus Mark
 */
public final class QuestionAttributeValuesCollection implements Serializable {

    public QuestionAttributeValuesCollection() {
        questionAttributeValues = new HashMap<String, AttributeValue>();
    }

    /**
     * Add value.
     * <br/> Also records maxDynamicPosition.
     *
     * @param key
     * @param nodeExtendedAttribute
     * @param domainObjectLabel (If the attribute value actually points to a node or user, this will be the label (used for display purposes.)
     */
    public void addValue(String key, NodeExtendedAttribute nodeExtendedAttribute, String domainObjectLabel) {

        // record max dynamic position if maxposition is null or dynamic position is not null and is greater than maxposition
        final Integer dynamicPosition = nodeExtendedAttribute.getDynamicPosition();
        if (maxDynamicPosition == null || (dynamicPosition != null && dynamicPosition.intValue() > maxDynamicPosition.intValue())) {
            maxDynamicPosition = dynamicPosition;
        }

        AttributeValue attributeValue = questionAttributeValues.get(key);
        if (attributeValue == null) {
            attributeValue = AttributeValue.create(nodeExtendedAttribute);
            attributeValue.setLabel(domainObjectLabel);
            questionAttributeValues.put(key, attributeValue);
        } else {
            attributeValue.addValue(nodeExtendedAttribute, true);
        }
    }

    /**
     * Get value.
     *
     * @param key - the attributeName in the case of a dynamicAttribute this is the id, in the case of a person's name 'coreDetail.firstName' 
     *              and finally in the case of a questionnaire 'dynamicAttributeId_questionnaireWorkflowId_roleId'
     * @return AttributeValue or null.
     */
    public AttributeValue getValue(String key) {
        return questionAttributeValues.get(key);
    }

    /**
     * Get max dynamic position.
     * <br/> In practice this is the max number of rows for the Node.
     *
     * @return maxDynamicPosition (can be null)
     */
    public Integer getMaxDynamicPosition() {
        return maxDynamicPosition;
    }

    public List<AttributeValue> getAttributeValues() {
        return new ArrayList<AttributeValue>(questionAttributeValues.values());
    }

    public void addWorkflowValue(Long workflowId, boolean participant) {
        workflowParticipation.put(workflowId, new Boolean(participant));    
    }

    public boolean isParticipant(Long workflowId) {
        return workflowParticipation.get(workflowId) != null && workflowParticipation.get(workflowId).booleanValue();
    }

    public boolean containsWorflow(Long workflowId) {
        return workflowParticipation.containsKey(workflowId);
    }

    private final Map<String, AttributeValue> questionAttributeValues;
    private final Map<Long, Boolean> workflowParticipation = new HashMap<Long, Boolean>();
    private Integer maxDynamicPosition;
}

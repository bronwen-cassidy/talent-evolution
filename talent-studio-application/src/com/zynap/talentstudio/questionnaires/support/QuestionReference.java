/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.questionnaires.support;

import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.DynamicAttributeReference;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 17-Aug-2006 12:04:03
 */
public class QuestionReference implements Serializable {

    private static final long serialVersionUID = -2041521009502610998L;

    public QuestionReference() {
    }

    public QuestionReference(String targetQuestionId) {
        this.referenceId = targetQuestionId;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public Question getReferenceQuestion() {
        return referenceQuestion;
    }

    public void setReferenceQuestion(Question referenceQuestion) {
        DynamicAttribute dynamicAttribute = referenceQuestion.getDynamicAttribute();
        dynamicAttributeReference.setReferenceDa(dynamicAttribute);
        dynamicAttribute.addReference(dynamicAttributeReference);
        this.referenceQuestion = referenceQuestion;
    }

    public Question getParentQuestion() {
        return parentQuestion;
    }

    public void setOperandParent(Question question) {
        dynamicAttributeReference = new DynamicAttributeReference();
        dynamicAttributeReference.setLabel(value);
        dynamicAttributeReference.setType(DynamicAttribute.DA_TYPE_SUM);
        DynamicAttribute dynamicAttribute = question.getDynamicAttribute();
        dynamicAttribute.addChild(dynamicAttributeReference);
        dynamicAttributeReference.setParentDa(dynamicAttribute);
        this.parentQuestion = question;
    }

    public void setMappingQuestionParent(Question question) {
        DynamicAttributeReference dynamicAttributeRef = getDynamicAttributeReference();
        DynamicAttribute dynamicAttribute = question.getDynamicAttribute();
        dynamicAttribute.addChild(dynamicAttributeRef);
        dynamicAttributeRef.setParentDa(dynamicAttribute);
        for (int i = 0; i < children.size(); i++) {
            QuestionReference questionReference = children.get(i);
            questionReference.setMappingQuestionParent(question);
        }
        this.parentQuestion = question;
    }

    public void addChild(QuestionReference child) {
        child.setParent(this);
        children.add(child);
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

    public String getValue() {
        return value;
    }

    public LookupValue getLookupValue() {
        return lookupValue;
    }

    public void setValue(String value) {
        this.value = value;
        if (StringUtils.hasText(value)) {
            if (lookupValue == null) lookupValue = new LookupValue();
            lookupValue.setLabel(value);
            lookupValue.setValueId(value.toUpperCase());
            lookupValue.setSortOrder(SORT_ORDER++);
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        if (StringUtils.hasText(description)) {
            if (lookupValue == null) lookupValue = new LookupValue();
            lookupValue.setDescription(description);
        }
    }

    public List<QuestionReference> getChildren() {
        List<QuestionReference> allChildren = new ArrayList<QuestionReference>();
        for (int i = 0; i < children.size(); i++) {
            QuestionReference questionReference = children.get(i);
            allChildren.add(questionReference);
            allChildren.addAll(questionReference.getChildren());
        }
        return allChildren;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final QuestionReference that = (QuestionReference) o;

        if (lowerBound != null ? !lowerBound.equals(that.lowerBound) : that.lowerBound != null) return false;
        if (!referenceId.equals(that.referenceId)) return false;
        if (upperBound != null ? !upperBound.equals(that.upperBound) : that.upperBound != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = referenceId.hashCode();
        result = 29 * result + (value != null ? value.hashCode() : 0);
        result = 29 * result + (upperBound != null ? upperBound.hashCode() : 0);
        result = 29 * result + (lowerBound != null ? lowerBound.hashCode() : 0);
        return result;
    }

    public String toString() {
        return new StringBuffer("referenceId = ")
                .append(referenceId)
                .append(", lowerBound = ")
                .append(lowerBound)
                .append(", upperBound = ")
                .append(upperBound)
                .append(", value = ")
                .append(value)
                .append(", parent Mapping = ")
                .append(parent)
                .append(", parent question = ")
                .append(parentQuestion)
                .toString();
    }

    private void setParent(QuestionReference parent) {
        DynamicAttributeReference dynamicAttributeMappingReference = getDynamicAttributeReference();
        DynamicAttributeReference parentMappingReference = parent.getDynamicAttributeReference();
        parentMappingReference.addChild(dynamicAttributeMappingReference);
        this.parent = parent;
    }

    private DynamicAttributeReference getDynamicAttributeReference() {
        if (dynamicAttributeReference == null) {
            dynamicAttributeReference = new DynamicAttributeReference();
            dynamicAttributeReference.setLabel(value);
            dynamicAttributeReference.setType(DynamicAttribute.DA_TYPE_ENUM_MAPPING);
            dynamicAttributeReference.setUpperBound(upperBound);
            dynamicAttributeReference.setLowerBound(lowerBound);
            dynamicAttributeReference.setLookupValue(lookupValue);
        }
        return dynamicAttributeReference;
    }

    private String referenceId;
    private String value;
    private String description;
    private Integer upperBound;
    private Integer lowerBound;

    private Question referenceQuestion;
    private Question parentQuestion;

    private QuestionReference parent;
    private List<QuestionReference> children = new ArrayList<QuestionReference>();

    private DynamicAttributeReference dynamicAttributeReference;
    private LookupValue lookupValue;

    private int SORT_ORDER = 0;
}

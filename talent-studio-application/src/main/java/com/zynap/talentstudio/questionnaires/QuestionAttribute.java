/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.questionnaires;

import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;

import java.util.Collection;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 25-Jan-2007 17:18:31
 */
public class QuestionAttribute extends AbstractQuestion {

    private static final long serialVersionUID = 7640989070619047941L;
    private boolean cannotDisable = true;


    /**
     * Required default constructor
     */
    public QuestionAttribute() {
    }

    /**
     * Constructor used to create the question element
     *
     * @param dynamicAttribute the dynamicAttribute
     * @param length the field length may be null;
     * @param target the target as yet undefined (relates to objectives)
     * @param textId a textual id for the question
     * @param title  the displayable title for the element (used for mouse-overs)
     * @param type the question type (it supports more than dynamicAttributes do)
     * @param managerWrite is this a manager write only question.
     */
    public QuestionAttribute(DynamicAttribute dynamicAttribute, int length, String target, String textId, String title, String type, boolean managerWrite) {
        this.dynamicAttribute = dynamicAttribute;
        if (length > 0) {
            this.length = length;
        }
        this.target = target;
        this.textId = textId;
        this.title = title;
        this.questionType = type;
        this.managerWrite = managerWrite;
    }

    public QuestionAttribute(DynamicAttribute attribute, int length, String target, String textId, String title, String type, boolean managerWrite, boolean hidden, Integer sortOrder) {
        this(attribute, length, target, textId, title, type, managerWrite);
        this.hidden = hidden;
        this.sortOrder = sortOrder;
    }

    public QuestionAttribute(DynamicAttribute attribute, int length, String target, String textId, String title, String type, boolean managerWrite, boolean cannotDisable, boolean hidden, Integer sortOrder, String cellStyle, String questionStyle) {
        this(attribute, length, target, textId, title, type, managerWrite, hidden, sortOrder);
        this.cellStyle = cellStyle;
        this.cannotDisable = cannotDisable;
        this.questionStyle = questionStyle;
    }

    /**
     * Constructor for the narrative question types
     *
     * @param narrative the description for this element
     */
    public QuestionAttribute(String narrative) {
        this.narrative = narrative;
    }

    public DynamicAttribute getDynamicAttribute() {
        return dynamicAttribute;
    }

    public void setDynamicAttribute(DynamicAttribute dynamicAttribute) {
        this.dynamicAttribute = dynamicAttribute;
    }

    public LineItem getLineItem() {
        return lineItem;
    }

    public void setLineItem(LineItem lineItem) {
        this.lineItem = lineItem;
    }

    public QuestionGroup getQuestionGroup() {
        return questionGroup;
    }

    public void setQuestionGroup(QuestionGroup questionGroup) {
        this.questionGroup = questionGroup;
    }

    public String getNarrative() {
        return narrative;
    }

    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getTextId() {
        return textId;
    }

    public void setTextId(String textId) {
        this.textId = textId;
    }

    public boolean isMultiQuestion() {
        return false;
    }

    public boolean isMultiSelectType() {
        return DynamicAttribute.DA_TYPE_MULTISELECT.equals(questionType);
    }

    public boolean isNarrativeType() {
        return dynamicAttribute == null || narrative != null;
    }

    public boolean isCheckboxType() {
        return TYPE_CHECKBOX.equals(questionType);
    }

    public void refresh() {
        if(dynamicAttribute != null && dynamicAttribute.getRefersToType() != null) {
            dynamicAttribute.getRefersToType().getLookupValues().size();            
        }
    }

    /**
     * ITreeElement implementation method
     *
     * @return null
     */
    public Collection getChildren() {
        return null;
    }

    /**
     * ITreeElement implementation method
     * @return the dynamicAttributes id as a string
     */
    public String getElementId() {
        return dynamicAttribute != null ? String.valueOf(dynamicAttribute.getId()) : null;
    }

    /**
     * ITreeElement implementation method
     * @return the label of the dynamicAttribute
     */
    public String getLabel() {
        return dynamicAttribute != null ? dynamicAttribute.getLabel() : null;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("label: ").append(getLabel());
        result.append(" title: ").append(title);
        result.append(" textId: ").append(textId);
        result.append(" questionType: ").append(questionType);
        result.append(" isNarrative: ").append(isNarrativeType());
        result.append(" narrative: ").append(narrative);
        return result.toString();
    }

    public boolean isSum() {
        return !isNarrativeType() && DynamicAttribute.DA_TYPE_SUM.equals(dynamicAttribute.getType());
    }

    public boolean isEnumMapping() {
        return !isNarrativeType() && DynamicAttribute.DA_TYPE_ENUM_MAPPING.equals(dynamicAttribute.getType());
    }

    public boolean isLabelDisplayable() {
        return labelDisplayable;
    }

    /**
     * @return true if this question can only be modified by a manager.
     */
    public boolean isManagerWrite() {
        return managerWrite;
    }

    public void setManagerWrite(boolean managerWrite) {
        this.managerWrite = managerWrite;
    }

    public boolean isCannotDisable() {
        return cannotDisable;
    }

    public void setCannotDisable(boolean cannotDisable) {
        this.cannotDisable = cannotDisable;
    }

    public void setLabelDisplayable(boolean labelDisplayable) {
        this.labelDisplayable = labelDisplayable;
    }

    public boolean isDynamic() {
        return dynamicAttribute != null && dynamicAttribute.isDynamic();
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public String getCellStyle() {
        return cellStyle;
    }

    public void setCellStyle(String cellStyle) {
        this.cellStyle = cellStyle;
    }

    public String getQuestionStyle() {
        return questionStyle;
    }

    public void setQuestionStyle(String questionStyle) {
        this.questionStyle = questionStyle;
    }

    private DynamicAttribute dynamicAttribute;
    private LineItem lineItem;
    private QuestionGroup questionGroup;

    private String narrative;
    private String title;
    private String target;
    private Integer length;
    private Integer sortOrder;
    private String questionType;
    private String textId;
    private boolean labelDisplayable;
    private String cellStyle;
    private String questionStyle;

    /* determines if the question is editable only by managers */
    private boolean managerWrite;
    /* determines if this question is hidden from view default is false */
    private boolean hidden;
    public static final String TYPE_CHECKBOX = "CHECKBOX";
    public static final String TYPE_RADIO = "RADIO";

}

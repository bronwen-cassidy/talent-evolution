/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires;

import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.questionnaires.QuestionAttribute;
import com.zynap.talentstudio.questionnaires.QuestionGroup;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;
import com.zynap.talentstudio.web.organisation.attributes.FormAttribute;

import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class QuestionAttributeWrapperBean extends AttributeWrapperBean {

    public QuestionAttributeWrapperBean(QuestionAttribute question) {

        super(question.getDynamicAttribute());
        this.question = question;
    }

    public QuestionAttributeWrapperBean(QuestionAttribute question, AttributeValue attributeValue) {
        super(attributeValue);
        this.question = question;
    }

    public QuestionAttribute getQuestion() {
        return question;
    }

    public boolean isPartOfMultiQuestion() {
        return question.getLineItem() != null;
    }

    public Long getLineItemId() {
        return isPartOfMultiQuestion() ? question.getLineItem().getId() : null;
    }

    public QuestionGroup getQuestionnaireGroup() {
        final QuestionGroup questionGroup = question.getQuestionGroup();
        return questionGroup != null ? questionGroup : question.getLineItem().getMultiQuestion().getGroup();
    }
    
    public String getRowStyle() {
        return isPartOfMultiQuestion() ? question.getLineItem().getRowStyle() : "";
    }
    public String getHeaderStyle() {
        return isPartOfMultiQuestion() ? question.getLineItem().getHeaderStyle() : "";
    }
    public String getFooterStyle() {
        return isPartOfMultiQuestion() ? question.getLineItem().getFooterStyle() : "";
    }
    public String getType() {
        return question.getQuestionType();
    }

    public String getDescription() {
        return attributeDefinition.getDescription();
    }

    public void setDescription(String newDescription) {
        attributeDefinition.setDescription(newDescription);
    }

    public String getLabel() {
        return question.getLabel();
    }

    public int getTextLength() {return StringUtils.hasText(question.getLabel()) ? 1 : 0; }

    /**
     * Sets a new Label on the dynamic attribute used when editing the questionnaire definition to modify the question label
     *
     * @param newLabel the newLabel passed through by spring bind mechanisms
     */
    public void setLabel(String newLabel) {
        attributeDefinition.setLabel(newLabel);
    }

    public AttributeValue getModifiedAttributeValue() {

        AttributeValue newValue = super.getModifiedAttributeValue();
        if (isPartOfMultiQuestion()) {
            List<NodeExtendedAttribute> nodeAttrs = newValue.getNodeExtendedAttributes();
            for (NodeExtendedAttribute attr : nodeAttrs) {
                attr.setDisabled(newValue.isDisabled());
            }
        }
        return newValue;
    }

    public boolean isHidden() {
        return question.isHidden();
    }

    /**
     * @return the label of the node when an artefact is the answer to a question.
     */
    public String getNodeLabel() {
        return nodeLabel;
    }

    /**
     * The label of the node that has been selected as an answer to the question.
     * The value is the id the nodeLabel is the displayValue
     *
     * @param nodeLabel - the label of the node may be the name or title depending on the node
     */
    public void setNodeLabel(String nodeLabel) {
        this.nodeLabel = nodeLabel;
    }

    public int getLength() {
        final Integer length = question.getLength();
        return length != null ? length : 0;
    }

    public String getQuestionStyle() {
        return question.getQuestionStyle() != null ? question.getQuestionStyle() : "";
    }

    public String getCellStyle() {
        return question.getCellStyle();
    }

    public boolean isDisplayable() {
        return question.isLabelDisplayable();
    }

    public boolean isHasTitle() {
        final String title = getTitle();
        return StringUtils.hasText(title);
    }

    public int getRowCount() {
        return attributeDefinition.getRefersToType().getLookupValues().size();
    }

    public String getTitle() {
        return getQuestion().getTitle();
    }

    public boolean isHasBlank() {
        return attributeDefinition.getRefersToType() != null && attributeDefinition.getRefersToType().hasBlankValue();
    }

    public String getLineItemLabel() {

        String lineItemLabel = null;

        if (isPartOfMultiQuestion()) {
            lineItemLabel = question.getLineItem().getLabel();
        }

        if (!StringUtils.hasText(lineItemLabel)) {
            lineItemLabel = "";
        }

        return lineItemLabel;
    }

    public void setLineItemLabel(String newLabel) {
        if (isPartOfMultiQuestion()) {
            question.getLineItem().setLabel(newLabel);
        }
    }

    public boolean isCanDisable() {
        return isPartOfMultiQuestion() && question.getLineItem().isCanDisable();
    }
    public boolean isQuestionCannotDisable() {
        return question.isCannotDisable();
    }

    public void setDisabled(boolean value) {
        attributeValue.setDisabled(value);
    }

    public boolean isDisabled() {
        return attributeValue.isDisabled();
    }

    public boolean isRowDisabled() {
        if (isDynamic()) {
            final QuestionAttributeWrapperBean[] attributeWrapperBeans = lineItemWrapper.getGrid()[dynamicPosition];
            for (QuestionAttributeWrapperBean attributeWrapperBean : attributeWrapperBeans) {
                if (attributeWrapperBean.isDisabled()) {
                    return true;
                }
            }
        } else if (isPartOfMultiQuestion()) {
            List<FormAttribute> questionWrappers = lineItemWrapper.getQuestionWrappers();
            for (FormAttribute questionWrapper : questionWrappers) {
                if(((QuestionAttributeWrapperBean) questionWrapper).isDisabled()) return true;
            }
        }
        return false;
    }

    public Long getDaId() {
        return attributeDefinition.getId();
    }

    public Long getAttributeValueId() {
        return attributeValue != null ? attributeValue.getId() : null;
    }

    public void setAttributeValueId(Long attributeValueId) {
        // this is a method for spring binding only
    }

    public Object clone() throws CloneNotSupportedException {
        return new QuestionAttributeWrapperBean(question);
    }

    public Integer getDynamicPosition() {
        return dynamicPosition;
    }

    public void setDynamicPosition(Integer dynamicPosition) {
        this.dynamicPosition = dynamicPosition;
    }

    public void setDynamicPosition(int dynamicPosition) {
        setDynamicPosition(new Integer(dynamicPosition));
    }

    public boolean isManagerWrite() {
        return question.isManagerWrite();
    }

    public void setLineItemWrapper(LineItemWrapper lineItemWrapper) {
        this.lineItemWrapper = lineItemWrapper;
    }

    public LineItemWrapper getLineItemWrapper() {
        return lineItemWrapper;
    }

    public boolean isCheckbox() {
        return question.isCheckboxType();
    }

    public boolean isMultiSelect() {
        return question.isMultiSelectType();
    }

    private final QuestionAttribute question;
    private Integer dynamicPosition;
    private LineItemWrapper lineItemWrapper;
}

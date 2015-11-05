/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.questionnaires.support;

import com.zynap.common.util.StringUtil;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.questionnaires.QuestionAttribute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class QuestionLineItem implements Serializable {

    private static final long serialVersionUID = 1417495845738810288L;

    public String getLabel() {
        return label;
    }

    public String getDynamic() {
        return dynamic;
    }

    public void setManagerCanDisable(String value) {
        this.canDisable = YES_ATTR.equalsIgnoreCase(value);
    }

    public boolean isCanDisable() {
        return canDisable;
    }

    public void setDynamic(String dynamic) {
        this.dynamic = (YES_ATTR.equals(dynamic) ? StringUtil.TRUE : StringUtil.FALSE);
    }

    public String getRowStyle() {
        return rowStyle;
    }

    public void setRowStyle(String rowStyle) {
        this.rowStyle = rowStyle;
    }

    public String getHeaderStyle() {
        return headerStyle;
    }

    public void setHeaderStyle(String headerStyle) {
        this.headerStyle = headerStyle;
    }

    public String getFooterStyle() {
        return footerStyle;
    }

    public void setFooterStyle(String footerStyle) {
        this.footerStyle = footerStyle;
    }

    public Collection<BaseQuestion> getChildren() {
        return getQuestions();
    }

    public String getElementId() {
        return null;
    }

    public void setLabel(String label) {
        this.label = label != null ? label.trim() : null;
    }

    public void addQuestion(Question question) throws UnsupportedQuestionTypeException {
        if (this.questions == null) questions = new ArrayList<BaseQuestion>();
        final boolean dynamicLineItem = isDynamicLineItem();
        if (dynamicLineItem) {
            verifyQuestionType(question, disallowedDynamicLineItemTypes);
        } else {
            verifyQuestionType(question, disallowedTypes);
        }

        question.setLineItem(this);
        question.setDynamic(dynamicLineItem);
        question.setSortOrder(questions.size());
        this.questions.add(question);
    }

    private void verifyQuestionType(Question question, List<String> disallowedTypes) throws UnsupportedQuestionTypeException {
        if (disallowedTypes.contains(question.getType())) {
            throw new UnsupportedQuestionTypeException("Question of type " + question.getType() + " is not allowed within a " + (isDynamicLineItem() ? "dynamic" : "") + " line-item element",
                    UNSUPPORTED_QUESTION_TYPE_ERROR,
                    question.getType());
        }
    }

    public boolean isDynamicLineItem() {
        return StringUtil.convertToBoolean(dynamic);
    }

    public List<BaseQuestion> getQuestions() {
        return questions;
    }

    private String label;
    private List<BaseQuestion> questions;
    private String dynamic = StringUtil.FALSE;
    private boolean canDisable;
    private String rowStyle;
    private String headerStyle;
    private String footerStyle;

    static final List<String> disallowedTypes = new ArrayList<String>();
    static final List<String> disallowedDynamicLineItemTypes = new ArrayList<String>();

    static {

        disallowedTypes.add(DynamicAttribute.DA_TYPE_SUM);
        disallowedTypes.add(DynamicAttribute.DA_TYPE_ENUM_MAPPING);

        disallowedDynamicLineItemTypes.addAll(disallowedTypes);
        disallowedDynamicLineItemTypes.add(DynamicAttribute.DA_TYPE_MULTISELECT);
        disallowedDynamicLineItemTypes.add(DynamicAttribute.DA_TYPE_LAST_UPDATED);
        disallowedDynamicLineItemTypes.add(DynamicAttribute.DA_TYPE_LAST_UPDATED_BY);
        disallowedDynamicLineItemTypes.add(DynamicAttribute.DA_TYPE_IMAGE);
        disallowedDynamicLineItemTypes.add(DynamicAttribute.DA_TYPE_OU);
        disallowedDynamicLineItemTypes.add(DynamicAttribute.DA_TYPE_POSITION);
        disallowedDynamicLineItemTypes.add(DynamicAttribute.DA_TYPE_SUBJECT);
        disallowedDynamicLineItemTypes.add(DynamicAttribute.DA_TYPE_BLOG_COMMENT);
        disallowedDynamicLineItemTypes.add(QuestionAttribute.TYPE_CHECKBOX);
        disallowedDynamicLineItemTypes.add(QuestionAttribute.TYPE_RADIO);
    }

    private static final String UNSUPPORTED_QUESTION_TYPE_ERROR = "error.lineitem.unsupported.question.type";
    private static final String YES_ATTR = "Y";
}

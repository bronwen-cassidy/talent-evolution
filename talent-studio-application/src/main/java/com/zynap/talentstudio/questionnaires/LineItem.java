/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.questionnaires;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.talentstudio.questionnaires.support.ITreeElement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 25-Jan-2007 17:18:31
 */
public class LineItem extends ZynapDomainObject implements ITreeElement {

    /**
     * Required default constructor
     */
    public LineItem() {
    }

    public LineItem(String label, boolean dynamic) {
        this.label = label;
        this.dynamic = dynamic;
    }

    public LineItem(Long id, String label, boolean dynamic) {
        this.id = id;
        this.label = label;
        this.dynamic = dynamic;
    }

    public LineItem(String label, boolean isDynamic, boolean canDisable) {
        this(label, isDynamic);
        this.canDisable = canDisable;
    }

    public LineItem(String label, boolean isDynamic, boolean canDisable, String rowStyle, String headerStyle, String footerStyle) {
        this(label, isDynamic, canDisable);
        this.rowStyle = rowStyle;
        this.headerStyle = headerStyle;
        this.footerStyle = footerStyle;
    }

    public MultiQuestionItem getMultiQuestion() {
        return multiQuestion;
    }

    public void setMultiQuestion(MultiQuestionItem multiQuestion) {
        this.multiQuestion = multiQuestion;
    }

    public List<QuestionAttribute> getQuestions() {
        return questions != null ? questions: new ArrayList<QuestionAttribute>();
    }

    public void setQuestions(List<QuestionAttribute> questions) {
        this.questions = questions;
    }

    public boolean isDynamic() {
        return dynamic;
    }

    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    public boolean isCanDisable() {
        return canDisable;
    }

    public void setCanDisable(boolean canDisable) {
        this.canDisable = canDisable;
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

    /**
     * ITreeElement implementation method.
     *
     * @return the contained questions
     */
    public Collection<QuestionAttribute> getChildren() {
        return questions;
    }

    /**
     * ITreeElement implementation method.
     * 
     * @return null
     */
    public String getElementId() {
        return null;
    }

    public void addQuestion(QuestionAttribute questionAttribute) {
        if(questions == null) questions = new ArrayList<QuestionAttribute>();
        questionAttribute.setLineItem(this);
        questions.add(questionAttribute);
    }


    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("label: ").append(label);
        result.append(" dynamic: ").append(dynamic);
        return result.toString();
    }

    private MultiQuestionItem multiQuestion;
    private List<QuestionAttribute> questions;
    private boolean dynamic;
    private boolean canDisable;
    private String rowStyle;
    private String headerStyle;
    private String footerStyle;


    private static final long serialVersionUID = -7416518455128391766L;
}

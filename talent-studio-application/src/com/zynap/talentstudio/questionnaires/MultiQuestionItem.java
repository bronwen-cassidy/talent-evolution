/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.questionnaires;

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
public class MultiQuestionItem extends AbstractQuestion {

    private static final long serialVersionUID = 8039600388283389986L;
    
    /**
     * Required default constructor
     */
    public MultiQuestionItem() {
    }

    public MultiQuestionItem(String label) {
        this.label = label;
    }

    public MultiQuestionItem(Long id, String label) {
        this.id = id;
        this.label = label;
    }

    public QuestionGroup getGroup() {
        return group;
    }

    public void setGroup(QuestionGroup group) {
        this.group = group;
    }

    public List<LineItem> getLineItems() {
        return lineItems != null ? lineItems : new ArrayList<LineItem>();
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public boolean isMultiQuestion() {
        return true;
    }

    public Collection getChildren() {
        return lineItems;
    }

    public String getElementId() {
        return null;
    }


    public boolean isNarrativeType() {
        return false;
    }


    public void refresh() {
        List<QuestionAttribute> questions = getQuestions();
        for (QuestionAttribute questionAttribute : questions) {
            questionAttribute.refresh();
        }
    }

    public void addLineItem(LineItem lineItem) {
        if(lineItems == null) lineItems = new ArrayList<LineItem>();
        lineItem.setMultiQuestion(this);
        lineItems.add(lineItem);
    }


    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("label: ").append(label);
        return result.toString();
    }

    /**
     * Non hibernate method to get all questions contained within the line items
     *
     * @return list of all the questions
     */
    public List<QuestionAttribute> getQuestions() {
        List<QuestionAttribute> result = new ArrayList<QuestionAttribute>();
        for (Object lineItem1 : lineItems) {
            LineItem lineItem = (LineItem) lineItem1;
            result.addAll(lineItem.getQuestions());
        }
        return result;
    }

    private QuestionGroup group;
    private List<LineItem> lineItems;
}

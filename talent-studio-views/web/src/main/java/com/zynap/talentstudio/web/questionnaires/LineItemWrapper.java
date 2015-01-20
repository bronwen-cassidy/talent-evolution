package com.zynap.talentstudio.web.questionnaires;

import com.zynap.talentstudio.questionnaires.MultiQuestionItem;
import com.zynap.talentstudio.questionnaires.LineItem;
import com.zynap.talentstudio.web.organisation.attributes.FormAttribute;

import java.util.ArrayList;
import java.util.List;

/**
 * User: amark
 * Date: 11-Sep-2006
 * Time: 11:30:08
 * <p/>
 * Wrapper that holds QuestionAttributeWrapperBeans that belong to a line item.
 */
public class LineItemWrapper implements FormAttribute {

    public LineItemWrapper(MultiQuestionItem multiQuestion) {
        this.multiQuestion = multiQuestion;
    }

    public final void addLineItemQuestion(QuestionAttributeWrapperBean questionAttributeWrapperBean) {
        questionAttributeWrapperBean.setLineItemWrapper(this);
        questionWrappers.add(questionAttributeWrapperBean);
    }

    public void initialiseState() {
        
        final int numberOfWrappers = getNumberOfQuestionWrappers();
        final int numberOfLineItems = getNumberOfLineItems();
        final int numberOfQuestions = numberOfWrappers / numberOfLineItems;

        grid = new QuestionAttributeWrapperBean[numberOfLineItems][numberOfQuestions];
        int row = 0;
        int col = 0;
        for (int i = 0; i < numberOfWrappers; i++) {
            final QuestionAttributeWrapperBean attributeWrapperBean = getQuestionAttributeWrapperBean(i);
            attributeWrapperBean.setLineItemWrapper(this);
            grid[row][col++] = attributeWrapperBean;

            // reset cols and increment row
            if (col == numberOfQuestions) {
                row++;
                col = 0;
            }
        }
    }

    public boolean isDynamicOrManagerDisable() {
        return isDynamic() || isCanManagerDisable();
    }

    public boolean isHidden() {
        return false;
    }

    /**
     * Build the grid for display purposes.
     * <br/> For a multi question with two line items and two questions the grid will be of size 4,4.
     * <br/> Row[0] will contain the QuestionAttributeWrapperBeans for each of the questions for the first line item.
     * <br/> Row[1] will contain the QuestionAttributeWrapperBeans for each of the questions for the second line item.
     *
     * @return QuestionAttributeWrapperBean[][]
     */
    public QuestionAttributeWrapperBean[][] getGrid() {
        return grid;
    }

    public final List<FormAttribute> getQuestionWrappers() {
        return questionWrappers;
    }

    public final String getValue() {
        return null;
    }

    public final String getLabel() {
        return multiQuestion.getLabel();
    }

    public void setLabel(String newLabel) {
        multiQuestion.setLabel(newLabel);
    }

    public String getLineItemLabel() {
        final List<LineItem> lineItems = multiQuestion.getLineItems();
        return lineItems != null && lineItems.size() > 0 ? lineItems.get(0).getLabel() : "";
    }

    public final String getId() {
        return null;
    }

    public final boolean isEditable() {
        return true;
    }

    public final boolean isLineItem() {
        return true;
    }

    public final int getNumberOfLineItems() {
        return multiQuestion.getLineItems().size();
    }

    public final int getNumberOfQuestionWrappers() {
        return questionWrappers.size();
    }

    public boolean isDynamic() {
        return false;
    }

    protected final QuestionAttributeWrapperBean getQuestionAttributeWrapperBean(final int pos) {
        return (QuestionAttributeWrapperBean) questionWrappers.get(pos);
    }


    public boolean isMultiQuestion() {
        return true;
    }

    public boolean isCanManagerDisable() {
        for (int i = 0; i < questionWrappers.size(); i++) {
            QuestionAttributeWrapperBean formAttribute = (QuestionAttributeWrapperBean) questionWrappers.get(i);
            if(formAttribute.isCanDisable()) return true;
        }
        return false;
    }

    protected final List<FormAttribute> questionWrappers = new ArrayList<FormAttribute>();
    protected final MultiQuestionItem multiQuestion;
    protected QuestionAttributeWrapperBean[][] grid;
}

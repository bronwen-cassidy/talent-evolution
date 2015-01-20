package com.zynap.talentstudio.web.questionnaires;

import com.zynap.talentstudio.questionnaires.MultiQuestionItem;

import java.util.List;

/**
 * User: amark
 * Date: 08-Sep-2006
 * Time: 11:07:25
 *
 * Wrapper that holds DynamicQuestionAttributeWrapperBeans that belong to a dynamic line item.
 */
public final class DynamicLineItemWrapper extends LineItemWrapper {

    public DynamicLineItemWrapper(MultiQuestionItem multiQuestion) {
        super(multiQuestion);
    }

    public QuestionAttributeWrapperBean[][] getGrid() {
        return grid;
    }

    public boolean isDynamic() {
        return true;
    }


    public void initialiseState() {
        final QuestionAttributeWrapperBean questionWrapper = getQuestionAttributeWrapperBean(0);
        final int maxNumberOfRows = ((DynamicQuestionAttributeWrapperBean) questionWrapper).getQuestionWrappers().size();
        final int numberOfWrappers = getNumberOfQuestionWrappers();

        grid = new QuestionAttributeWrapperBean[maxNumberOfRows][numberOfWrappers];
        for (int i = 0; i < numberOfWrappers; i++) {
            for (int j = 0; j < maxNumberOfRows; j++) {
                final QuestionAttributeWrapperBean attributeWrapperBean = getQuestionAttributeWrapperBean(i, j);
                attributeWrapperBean.setLineItemWrapper(this);
                grid[j][i] = attributeWrapperBean;
            }
        }
    }

    private QuestionAttributeWrapperBean getQuestionAttributeWrapperBean(final int pos, final int nestedPos) {

        final QuestionAttributeWrapperBean questionAttributeWrapperBean = getQuestionAttributeWrapperBean(pos);
        final List wrappers = ((DynamicQuestionAttributeWrapperBean) questionAttributeWrapperBean).getQuestionWrappers();
        return (QuestionAttributeWrapperBean) wrappers.get(nestedPos);
    }
}

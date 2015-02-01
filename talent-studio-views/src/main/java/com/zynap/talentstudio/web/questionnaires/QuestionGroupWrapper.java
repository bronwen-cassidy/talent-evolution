/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires;

import com.zynap.talentstudio.questionnaires.QuestionGroup;

import java.io.Serializable;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class QuestionGroupWrapper implements Serializable {

    public QuestionGroupWrapper(QuestionGroup questionnaireGroup) {
        this.questionnaireGroup = questionnaireGroup;
    }

    public void setWrappedDynamicAttributes(List attributeWrappers) {
        this.wrappedDynamicAttributes = attributeWrappers;
    }

    public QuestionGroup getQuestionnaireGroup() {
        return questionnaireGroup;
    }

    public List getWrappedDynamicAttributes() {
        return wrappedDynamicAttributes;
    }

    public String getLabel() {
        return questionnaireGroup.getLabel();
    }

    public void setLabel(String newLabel) {
        questionnaireGroup.setLabel(newLabel);
    }

    public boolean isDisplayable() {
        return questionnaireGroup.isDisplayable();
    }

    private QuestionGroup questionnaireGroup;
    private List wrappedDynamicAttributes;
}

/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.questionnaires;

import java.util.Iterator;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 01-Feb-2007 10:41:56
 */
public class QuestionnaireUtils {

    /**
     * private constructor 
     */
    private QuestionnaireUtils() {

    }

    public static void initLazyCollections(QuestionnaireDefinition questionnaireDefinition) {
        questionnaireDefinition.getDynamicAttributes().size();
        final QuestionnaireDefinitionModel questionnaireDefinitionModel = questionnaireDefinition.getQuestionnaireDefinitionModel();
        final List questions = questionnaireDefinitionModel.getQuestions();
        for (Iterator iterator1 = questions.iterator(); iterator1.hasNext();) {
            AbstractQuestion abstractQuestion = (AbstractQuestion) iterator1.next();
            abstractQuestion.refresh();
        }
    }
}

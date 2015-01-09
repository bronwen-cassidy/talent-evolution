/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.questionnaires.support;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 06-Jun-2006 11:56:24
 * @version 0.1
 */

import junit.framework.TestCase;

import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;

public class TestQuestion extends TestCase {

    public void testClone() throws Exception {

        final Question question = new Question();
        question.setDescription("description");
        question.setDisplayableLabel(true);
        question.setLabel("label");
        question.setLength(22);
        question.setTarget("target");
        question.setTextId("1");
        question.setTitle("question title");
        question.setType("STRUCT");
        final QuestionLineItem questionLineItem = new QuestionLineItem();
        questionLineItem.setLabel("line item label");
        question.setLineItem(questionLineItem);
        question.setMandatory("T");
        final MultiQuestion multiQuestion = new MultiQuestion();
        final QuestionLineItem lineItem = new QuestionLineItem();
        lineItem.setLabel("line item label");
        multiQuestion.setName("multi question name");
        multiQuestion.addQuestion(question);
        multiQuestion.addLineItem(lineItem);
        final QuestionnaireGroup questionnaireGroup = new QuestionnaireGroup();
        questionnaireGroup.setDescription("group description");
        questionnaireGroup.setDisplayable(true);
        questionnaireGroup.setLabel("group label");
        questionnaireGroup.addMultiQuestion(multiQuestion);

        Question cloned = (Question) question.clone();

        final DynamicAttribute clonedDynamicAttribute = cloned.getDynamicAttribute();
        assertNotNull(clonedDynamicAttribute);
        assertNotNull(cloned.getMultiQuestion());

        assertEquals(question.getLabel(), cloned.getLabel());
        assertEquals(question.getLineItem(), cloned.getLineItem());
        assertEquals(question.getMultiQuestion(), cloned.getMultiQuestion());
        assertEquals(question.getQuestionnaireGroup(), cloned.getQuestionnaireGroup());

        // not the same object
        final DynamicAttribute dynamicAttribute = question.getDynamicAttribute();
        assertNotSame(dynamicAttribute, clonedDynamicAttribute);

        assertEquals(dynamicAttribute.getLabel(), clonedDynamicAttribute.getLabel());
        assertEquals(dynamicAttribute.getType(), clonedDynamicAttribute.getType());
        assertEquals(dynamicAttribute.getArtefactType(), clonedDynamicAttribute.getArtefactType());
        assertEquals(dynamicAttribute.getRefersToType(), clonedDynamicAttribute.getRefersToType());
        assertEquals(dynamicAttribute.getDescription(), clonedDynamicAttribute.getDescription());
        assertEquals(dynamicAttribute.getExternalRefLabel(), clonedDynamicAttribute.getExternalRefLabel());
        assertEquals(dynamicAttribute.getQuestionnaireDefinitionId(), clonedDynamicAttribute.getQuestionnaireDefinitionId());
        assertFalse(dynamicAttribute.getUniqueNumber().equals(clonedDynamicAttribute.getUniqueNumber()));

    }
}

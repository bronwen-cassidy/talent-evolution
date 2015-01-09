package com.zynap.talentstudio.questionnaires.support;

/**
 * User: amark
 * Date: 21-Nov-2006
 * Time: 16:50:17
 */

import com.zynap.common.util.StringUtil;
import com.zynap.talentstudio.ZynapTestCase;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;

import java.util.ArrayList;
import java.util.List;

public class TestQuestionLineItem extends ZynapTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        questionLineItem = new QuestionLineItem();
    }

    public void testGetId() throws Exception {
        assertNull(questionLineItem.getElementId());
    }

    public void testSetDynamic() throws Exception {

        questionLineItem.setDynamic(null);
        assertFalse(questionLineItem.isDynamicLineItem());

        questionLineItem.setDynamic(StringUtil.FALSE);
        assertFalse(questionLineItem.isDynamicLineItem());

        questionLineItem.setDynamic(StringUtil.TRUE);
        assertFalse(questionLineItem.isDynamicLineItem());

        questionLineItem.setDynamic("Y");
        assertTrue(questionLineItem.isDynamicLineItem());
    }

    public void testSetLabel() throws Exception {

        final String str = "test";
        questionLineItem.setLabel(" " + str + " ");
        assertEquals(str, questionLineItem.getLabel());
    }

    public void testAddQuestion() throws Exception {
        final Question question = new Question();
        questionLineItem.addQuestion(question);

        final List questions = questionLineItem.getQuestions();
        assertEquals(1, questions.size());
        assertEquals(question, questions.get(0));
    }

    public void testAddQuestionInvalidTypes() throws Exception {

        final Question question = new Question();

        testType(question, DynamicAttribute.DA_TYPE_SUM);
        testType(question, DynamicAttribute.DA_TYPE_ENUM_MAPPING);
    }

    public void testAddDynamicLineItemQuestionInvalidTypes() throws Exception {

        questionLineItem.setDynamic("Y");
        assertTrue(questionLineItem.isDynamicLineItem());

        final Question question = new Question();
        testType(question, DynamicAttribute.DA_TYPE_SUM);
        testType(question, DynamicAttribute.DA_TYPE_ENUM_MAPPING);
        testType(question, DynamicAttribute.DA_TYPE_MULTISELECT);
        testType(question, DynamicAttribute.DA_TYPE_LAST_UPDATED);
        testType(question, DynamicAttribute.DA_TYPE_LAST_UPDATED_BY);
        testType(question, DynamicAttribute.DA_TYPE_IMAGE);
        testType(question, DynamicAttribute.DA_TYPE_OU);
        testType(question, DynamicAttribute.DA_TYPE_POSITION);
        testType(question, DynamicAttribute.DA_TYPE_SUBJECT);
    }

    public void testGetLabel() throws Exception {
        assertNull(questionLineItem.getLabel());
    }

    public void testGetChildren() throws Exception {
        final Question question = new Question();
        questionLineItem.addQuestion(question);

        final List<BaseQuestion> questions = new ArrayList<BaseQuestion>(questionLineItem.getChildren());
        assertEquals(1, questions.size());
        assertEquals(question, questions.get(0));
    }

    private void testType(final Question question, final String type) {
        question.setType(type);
        try {
            questionLineItem.addQuestion(question);
            fail("Added invalid type:" + type);
        } catch (UnsupportedQuestionTypeException expected) {
        }
    }

    private QuestionLineItem questionLineItem;
}
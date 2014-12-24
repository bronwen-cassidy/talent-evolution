package com.zynap.talentstudio.web.workflow.performance;

/**
 * User: amark
 * Date: 07-Jul-2006
 * Time: 13:57:45
 */

import com.zynap.talentstudio.ZynapTestCase;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.domain.admin.User;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class TestPerformanceReviewQuestion extends ZynapTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        performanceReviewQuestion = new PerformanceReviewQuestion(QUESTION_LABEL);

        final Collection answers = performanceReviewQuestion.getAnswers();
        assertNotNull(answers);
        assertTrue(answers.isEmpty());
        assertFalse(performanceReviewQuestion.isHasAnswers());
    }

    public void testAddAnswer() throws Exception {

        final LookupValue role1 = new LookupValue("Z", "z role", "desc", true, false, 2, null);
        final LookupValue role2 = new LookupValue("D", "z role", "desc", true, false, 1, null);
        final LookupValue role3 = new LookupValue("A", "z role", "desc", true, false, 0, null);

        final PerformanceReviewAnswer performanceReviewAnswer1 = new PerformanceReviewAnswer(new User(), role1, null);
        final PerformanceReviewAnswer performanceReviewAnswer2 = new PerformanceReviewAnswer(new User(), role2, null);
        final PerformanceReviewAnswer performanceReviewAnswer3 = new PerformanceReviewAnswer(new User(), role3, null);

        performanceReviewQuestion.addAnswer(performanceReviewAnswer1);
        performanceReviewQuestion.addAnswer(performanceReviewAnswer2);
        performanceReviewQuestion.addAnswer(performanceReviewAnswer3);
        assertTrue(performanceReviewQuestion.isHasAnswers());

        final List answers = new ArrayList(performanceReviewQuestion.getAnswers());
        assertEquals(3, answers.size());

        // check they are ordered correctly
        assertSame(performanceReviewAnswer3, answers.get(0));
        assertSame(performanceReviewAnswer2, answers.get(1));
        assertSame(performanceReviewAnswer1, answers.get(2));
    }

    public void testGetQuestionLabel() throws Exception {
        assertEquals(QUESTION_LABEL, performanceReviewQuestion.getQuestionLabel());
    }

    private PerformanceReviewQuestion performanceReviewQuestion;

    private static final String QUESTION_LABEL = "label1";
}
package com.zynap.talentstudio.web.perfomance;

/**
 * User: amark
 * Date: 17-Aug-2006
 * Time: 17:12:49
 */

import com.zynap.domain.admin.LoginInfo;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.CoreDetail;
import com.zynap.talentstudio.ZynapTestCase;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.performance.PerformanceEvaluator;
import com.zynap.talentstudio.performance.PerformanceReview;

public class TestPerformanceEvaluatorWrapper extends ZynapTestCase {

    public void setUp() throws Exception {

        super.setUp();

        final CoreDetail evaluatorCoreDetail = new CoreDetail("user", "user");

        final Subject evaluator = new Subject(evaluatorCoreDetail);
        evaluator.setId(new Long(23233));

        final User user = new User(new LoginInfo(), evaluatorCoreDetail);
        user.setId(new Long(-8989));
        user.getSubjects().add(evaluator);
        evaluator.setUser(user);

        final Subject evaluatee = new Subject(new CoreDetail("firstname", "lastname"));

        final PerformanceReview performanceReview = new PerformanceReview(new Long(-1), "label");
        final LookupValue role = new LookupValue("PEER", "Peer", "desc", "APPRAISAL_ROLES");
        performanceEvaluator = new PerformanceEvaluator(role, performanceReview, evaluatee, user);
        performanceEvaluatorWrapper = new PerformanceEvaluatorWrapper(performanceEvaluator);
    }

    public void testGetPerformanceReviewLabel() throws Exception {
        assertEquals(performanceEvaluator.getPerformanceReview().getLabel(), performanceEvaluatorWrapper.getPerformanceReviewLabel());
    }

    public void testIsHasUser() throws Exception {
        assertTrue(performanceEvaluatorWrapper.isHasUser());
    }

    public void testGetSubjectName() throws Exception {
        assertEquals(performanceEvaluator.getSubject().getLabel(), performanceEvaluatorWrapper.getSubjectName());
    }

    public void testSetPerformerId() throws Exception {

        final Long newId = new Long(-12122);

        performanceEvaluatorWrapper.setPerformerId(newId);
        assertEquals(newId, performanceEvaluatorWrapper.getPerformerId());
    }

    public void testGetPerformanceEvaluator() throws Exception {
        assertEquals(performanceEvaluator, performanceEvaluatorWrapper.getPerformanceEvaluator());
    }

    public void testGetPerformerLabel() throws Exception {
        assertEquals(performanceEvaluator.getUser().getLabel(), performanceEvaluatorWrapper.getPerformerLabel());
    }

    public void testSetPerformerLabel() throws Exception {

        final String newLabel = "newLabel";

        performanceEvaluatorWrapper.setPerformerLabel(newLabel);
        assertEquals(newLabel, performanceEvaluatorWrapper.getPerformerLabel());
    }

    public void testGetLabel() throws Exception {
        assertEquals(performanceEvaluator.getLabel(), performanceEvaluatorWrapper.getLabel());
    }

    public void testGetPerformerId() throws Exception {
        assertEquals(performanceEvaluator.getUser().getId(), performanceEvaluatorWrapper.getPerformerId());
    }

    private PerformanceEvaluator performanceEvaluator;
    PerformanceEvaluatorWrapper performanceEvaluatorWrapper;
}
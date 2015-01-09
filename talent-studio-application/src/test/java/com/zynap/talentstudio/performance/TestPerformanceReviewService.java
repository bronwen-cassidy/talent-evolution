package com.zynap.talentstudio.performance;

/**
 * User: amark
 * Date: 24-Apr-2006
 * Time: 17:02:34
 */

import com.zynap.domain.admin.User;
import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.ZynapDatabaseTestCase;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.questionnaires.IQueWorkflowService;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.talentstudio.workflow.IWorkflowAdapter;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Collection;

public class TestPerformanceReviewService extends ZynapDatabaseTestCase {

    protected String getDataSetFileName() {
        return "test-data.xml";
    }

    protected void setUp() throws Exception {
        super.setUp();

        userService = (IUserService) getBean("userService");
        performanceReviewService = (IPerformanceReviewService) getBean("performanceReviewService");
        workFlowAdapter = (IWorkflowAdapter) getBean("workFlowAdapter");
    }

    public void testFindAll() throws Exception {

        PerformanceReview perfReview = createPerformanceReview(null, false);

        assertNotNull(perfReview.getId());

        final List all = performanceReviewService.findAll();
        assertTrue(all.contains(perfReview));
    }

    public void testCreate() throws Exception {

        final Date expiryDate = new Date();
        final PerformanceReview perfReview = createPerformanceReview(expiryDate, false);

        final Set queWorkflows = perfReview.getQueWorkflows();
        assertEquals(2, queWorkflows.size());

        for (Iterator iterator = queWorkflows.iterator(); iterator.hasNext();) {
            final QuestionnaireWorkflow workflow = (QuestionnaireWorkflow) iterator.next();
            assertNotNull(workflow.getId());
            assertEquals(expiryDate, workflow.getExpiryDate());
            assertEquals(POPULATION_ID, workflow.getPopulation().getId());
            assertEquals(perfReview.getLabel(), workflow.getLabel());

            if (workflow.isQuestionnaireManager()) {
                assertEquals(MANAGER_DEFINITION_ID, workflow.getQuestionnaireDefinition().getId());
            } else {
                assertEquals(GENERAL_DEFINITION_ID, workflow.getQuestionnaireDefinition().getId());
            }
        }
    }

    public void testStartAndDelete() throws Exception {
        final Date expiryDate = new Date();
        final PerformanceReview perfReview = createPerformanceReview(expiryDate, false);

        final User user = getAdminUser(userService);
        performanceReviewService.startReview(perfReview, user, false);
        // get notifications there will be none as no commit has occurred
        final List notifications = workFlowAdapter.getNotifications(user.getId(), true);
        System.out.println("notifications = " + notifications);
        performanceReviewService.deleteReview(perfReview.getId());
    }

    public void testStartAndDeleteUserManaged() throws Exception {
        final Date expiryDate = new Date();
        final PerformanceReview perfReview = createPerformanceReview(expiryDate, true);
        final User user = getAdminUser(userService);
        performanceReviewService.startReview(perfReview, user, true);
        PerformanceReview real = (PerformanceReview) performanceReviewService.findById(perfReview.getId());
        assertTrue(real != null);
        assertEquals(true, real.isUserManaged());
        performanceReviewService.deleteReview(perfReview.getId());
    }

    public void testGetSavedRoles() throws Exception {
        PerformanceReview perfReview = createPerformanceReview(null, false);

        // number of assigned roles (there are none at the moment
        final List roles = performanceReviewService.getAssignedPerformanceEvaluators(perfReview.getId(), SUBJECT_ID);
        assertEquals(0, roles.size());

        ISubjectService subjectService = (ISubjectService) getBean("subjectService");
        final Subject subject = subjectService.findById(SUBJECT_ID);

        // id must be null as have not been saved yet
        // check performance review and role and subject are set
        for (Iterator iterator = roles.iterator(); iterator.hasNext();) {
            PerformanceEvaluator performanceEvaluator = (PerformanceEvaluator) iterator.next();
            assertNull(performanceEvaluator.getId());

            assertEquals(perfReview, performanceEvaluator.getPerformanceReview());
            assertEquals(subject, performanceEvaluator.getSubject());
        }
    }

    public void testGetAssignedPerformanceEvaluators() throws Exception {
        final List roles = performanceReviewService.getRoles();
        assertFalse(roles.isEmpty());
        for (Iterator iterator = roles.iterator(); iterator.hasNext();) {
            final LookupValue role = (LookupValue) iterator.next();
            assertTrue(role.isActive());
        }

        final LookupValue selfEvaluatorRole = (LookupValue) roles.get(0);
        assertEquals(PerformanceEvaluator.SELF_EVALUATOR, selfEvaluatorRole.getValueId());
    }

    public void testSaveRoles() throws Exception {
        final User user = getAdminUser(userService);
        PerformanceReview perfReview = createPerformanceReview(null, false);
        final Long perfReviewId = perfReview.getId();

        // set user for even numbered roles
        final List<PerformanceEvaluator> roles = performanceReviewService.getAssignedPerformanceEvaluators(perfReviewId, SUBJECT_ID);
        for (int i = 0; i < roles.size(); i++) {
            PerformanceEvaluator performanceEvaluator = roles.get(i);
            if (i % 2 == 0) performanceEvaluator.setUser(user);
        }

        performanceReviewService.saveRoles(roles, SUBJECT_ID, perfReviewId);

        final List savedRoles = performanceReviewService.getAssignedPerformanceEvaluators(perfReviewId, SUBJECT_ID);
        assertEquals(0, savedRoles.size());

        // check order and that user was set on correct roles
        for (int i = 0; i < savedRoles.size(); i++) {
            PerformanceEvaluator savedEvaluator = (PerformanceEvaluator) savedRoles.get(i);
            if (i % 2 == 0) assertEquals(user, savedEvaluator.getUser());
            final PerformanceEvaluator actualPerformanceEvaluator = roles.get(i);
            assertEquals(actualPerformanceEvaluator.getRole(), savedEvaluator.getRole());
            assertEquals(actualPerformanceEvaluator.getLabel(), savedEvaluator.getLabel());
        }
    }

    public void testFindByID() throws Exception {
        PerformanceReview perfReview = createPerformanceReview(null, false);

        final PerformanceReview found = (PerformanceReview) performanceReviewService.findById(perfReview.getId());
        assertEquals(perfReview, found);
    }

    public void testFindWithInvalidId() throws Exception {

        try {
            performanceReviewService.findById(new Long(-1));
            fail("Should have thrown exception when finding with invalid id");
        } catch (DomainObjectNotFoundException expected) {
        }
    }

    public void testDelete() throws Exception {
        PerformanceReview perfReview = createPerformanceReview(null, false);
        final Long id = perfReview.getId();

        performanceReviewService.deleteReview(id);

        try {
            performanceReviewService.findById(id);
            fail("Should have thrown exception when finding with invalid id");
        } catch (DomainObjectNotFoundException expected) {
        }
    }

    public void testCloseReview() throws Exception {
        PerformanceReview performanceReview = (PerformanceReview) performanceReviewService.findById(new Long(-12));
        performanceReviewService.closeReview(performanceReview);
        commitAndStartNewTx();
        performanceReview = (PerformanceReview) performanceReviewService.findById(new Long(-12));
        assertEquals(QuestionnaireWorkflow.STATUS_COMPLETED, performanceReview.getStatus());
    }

    public void testHandleExpiredWorkflowsAndAppraisals() throws Exception {
        IQueWorkflowService workflowService = (IQueWorkflowService) getBean("queWorkflowService");
        try {
            workflowService.handleExpiredWorkflowsAndAppraisals();
        } catch (TalentStudioException e) {
            fail("no exception expected");
        }
    }

    public void testGetManagers() throws Exception {
        PerformanceReview performanceReview = (PerformanceReview) performanceReviewService.findById(new Long(-12));
        Collection<User> results = performanceReviewService.getManagers(performanceReview);
        assertEquals(1, results.size());
    }

    private PerformanceReview createPerformanceReview(Date expiryDate, boolean userManaged) throws TalentStudioException {

        final User user = getAdminUser(userService);

        PerformanceReview perfReview = new PerformanceReview(null, "test 1");
        perfReview.setUserManaged(userManaged);
        performanceReviewService.createReview(perfReview, user, MANAGER_DEFINITION_ID, GENERAL_DEFINITION_ID, POPULATION_ID, expiryDate);
        assertNotNull(perfReview.getId());
        assertEquals(QuestionnaireWorkflow.STATUS_NEW, perfReview.getStatus());
        return perfReview;
    }

    private IPerformanceReviewService performanceReviewService;
    private IUserService userService;

    /**
     * Id of subject in data file.
     */
    private static final Long SUBJECT_ID = new Long(-10);

    /**
     * ID OF ALL PEOPLE POPULATION.
     */
    private static final Long POPULATION_ID = new Long(-2);

    /**
     * Ids of definitions in data file.
     */
    private static final Long MANAGER_DEFINITION_ID = new Long(-11);
    private static final Long GENERAL_DEFINITION_ID = new Long(-12);
    private IWorkflowAdapter workFlowAdapter;
}
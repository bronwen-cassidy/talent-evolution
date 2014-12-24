/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.performance;

import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.common.IFinder;
import com.zynap.talentstudio.common.IModifiable;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.questionnaires.AbstractQuestionnaireService;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.workflow.Notification;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 27-Mar-2008 11:38:42
 */
public class PerformanceReviewService extends AbstractQuestionnaireService implements IPerformanceReviewService {

    public void createReview(PerformanceReview performanceReview, User user, Long managerQuestionnaireDefinitionId, Long evaluatorQuestionnaireDefinitionId, Long populationId, Date expiryDate) throws TalentStudioException {

        QuestionnaireDefinition managerDefinition = questionnaireDao.findDefinition(managerQuestionnaireDefinitionId);
        QuestionnaireDefinition evaluatorDefinition = questionnaireDao.findDefinition(evaluatorQuestionnaireDefinitionId);

        final String managerLabel = performanceReview.getLabel();
        final String evaluationLabel = performanceReview.getLabel();

        final Population newPopulation = new Population(populationId);

        QuestionnaireWorkflow managerWorkflow = new QuestionnaireWorkflow(managerLabel, QuestionnaireWorkflow.TYPE_MANAGER_APPRAISAL, managerDefinition, user.getId());
        managerWorkflow.setExpiryDate(expiryDate);
        managerWorkflow.setPopulation(newPopulation);

        QuestionnaireWorkflow evaluatorWorkflow = new QuestionnaireWorkflow(evaluationLabel, QuestionnaireWorkflow.TYPE_EVALUATOR_APPRAISAL, evaluatorDefinition, user.getId());
        evaluatorWorkflow.setExpiryDate(expiryDate);
        evaluatorWorkflow.setPopulation(newPopulation);

        performanceReview.addQuestionnaireWorkflow(managerWorkflow);
        performanceReview.addQuestionnaireWorkflow(evaluatorWorkflow);

        performanceReview.setStatus(QuestionnaireWorkflow.STATUS_NEW);
        performanceReviewDao.create(performanceReview);


        // pass in false as reviews go to managers so evaluatees may or may not be able to log in
        final Long managerPopId = managerWorkflow.getPopulation().getId();
        Collection results = determineParticipants(user.getId(), false, managerPopId, populationEngine, analysisService);
        questionnaireDao.addParticipants(managerWorkflow, results);
    }

    /**
     * @param performanceReview the performance review that has been started.
     * @param user              the logged in user who created and started the review process.
     * @param userManagedReview
     * @throws TalentStudioException
     */
    public void startReview(PerformanceReview performanceReview, User user, boolean userManagedReview) throws TalentStudioException {
        if (userManagedReview) {
            workflowAdapter.startUserManagedReview(performanceReview, user);
        } else {
            workflowAdapter.start(performanceReview, user);
        }
    }

    public List<PerformanceEvaluator> getAssignedPerformanceEvaluators(Long performanceReviewId, Long subjectId) throws TalentStudioException {
        return performanceReviewDao.getRoles(performanceReviewId, subjectId);
    }

    public PerformanceManager findPerformanceManager(Long performanceId, Long subjectId) throws TalentStudioException {
        return performanceReviewDao.findPerformanceManager(performanceId, subjectId);
    }

    public void saveRoles(List<PerformanceEvaluator> performanceEvaluators, PerformanceManager performanceManager) throws TalentStudioException {

        saveRoles(performanceEvaluators, performanceManager.getSubjectId(), performanceManager.getPerformanceId());

        Long managerId = performanceManager.getManagerId();
        if (managerId == null && performanceManager.getId() != null) {
            // needs to be deleted
            performanceReviewDao.deletePerformanceManager(performanceManager);
        } else {
            // save or update
            if (managerId != null) {
                performanceReviewDao.saveOrUpdatePerformanceManager(performanceManager);
            }
        }
    }

    public List<PerformanceReview> findAllPerformanceReviews() {
        return performanceReviewDao.getAllPerformanceReviews();
    }

    public QuestionnaireDefinition findPerformanceDefinition(Long appraisalId) {
        return performanceReviewDao.findPerformanceDefinition(appraisalId);
    }

    public List<Notification> getAppraisalReviewNotifications(Long performanceReviewId) {
      return performanceReviewDao.getAppraisalReviewNotifications(performanceReviewId);
    }

    public List<LookupValue> getRoles() {
        return lookupManagerDao.findActiveLookupValues(ROLE_LOOKUP_TYPE_ID);
    }

    public void saveRoles(List<PerformanceEvaluator> performanceEvaluators, Long evaluateeSubjectId, Long reviewId) throws TalentStudioException {

        performanceReviewDao.deleteEvaluators(evaluateeSubjectId, reviewId);

        for (Iterator<PerformanceEvaluator> iterator = performanceEvaluators.iterator(); iterator.hasNext();) {
            PerformanceEvaluator performanceEvaluator = iterator.next();
            performanceEvaluator.setId(null);
            if (performanceEvaluator.getUser() != null) {
                performanceReviewDao.saveOrUpdateEvaluator(performanceEvaluator);
            }
        }
    }

    /**
     * Delete performance review.
     *
     * @param performanceReview
     * @throws TalentStudioException
     */
    public void deleteReview(PerformanceReview performanceReview) throws TalentStudioException {
        removeAppraisalProcesses(performanceReview);
        performanceReviewDao.delete(performanceReview);
    }

    public Collection<User> getManagers(PerformanceReview performanceReview) {
        return performanceReviewDao.getManagers(performanceReview.getManagerWorkflow());
    }

    public void deleteReview(Long id) throws TalentStudioException {
        final PerformanceReview performanceReview = (PerformanceReview) findById(id);
        performanceReviewDao.delete(performanceReview);
    }

    public void closeReview(PerformanceReview performanceReview) throws TalentStudioException {

        // removes the notifications
        // removeAppraisalProcesses(performanceReview);

        // complete the appraisal process
        completeAppraisalProcess(performanceReview);
    }

    private void completeAppraisalProcess(PerformanceReview performanceReview) {
        workflowAdapter.completeAppraisalProcess(performanceReview.getId());
    }

    /**
     * Remove performance review process and processes associated with performance review children workflows.
     *
     * @param performanceReview
     * @throws com.zynap.exception.TalentStudioException
     *
     */
    private void removeAppraisalProcesses(PerformanceReview performanceReview) throws TalentStudioException {
        //remove the performance review master process and all it's processes
        questionnaireDao.removeNotifications(performanceReview.getId());
    }

    public void setPerformanceReviewDao(IPerformanceReviewDao performanceReviewDao) {
        this.performanceReviewDao = performanceReviewDao;
    }

    protected IFinder getFinderDao() {
        return performanceReviewDao;
    }

    protected IModifiable getModifierDao() {
        return performanceReviewDao;
    }

    private static final String ROLE_LOOKUP_TYPE_ID = "APPRAISAL_ROLES";

    private IPerformanceReviewDao performanceReviewDao;

}

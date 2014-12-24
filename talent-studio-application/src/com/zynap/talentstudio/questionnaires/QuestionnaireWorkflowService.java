/* 
 * Copyright (C)  Zynap Ltd. 2006
 * All rights reserved.
 */

package com.zynap.talentstudio.questionnaires;

import com.zynap.domain.IDomainObject;
import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.IFinder;
import com.zynap.talentstudio.common.IModifiable;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.performance.IPerformanceReviewService;
import com.zynap.talentstudio.performance.PerformanceReview;
import com.zynap.talentstudio.questionnaires.support.RepublishResults;

import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author syeoh
 * @version 0.1
 * @since 08-Mar-2007 16:48:34
 */

public class QuestionnaireWorkflowService extends AbstractQuestionnaireService implements IQueWorkflowService {

    public List findAll() throws TalentStudioException {
        return (List) questionnaireDao.findAllWorkflows();
    }

    public void create(IDomainObject domObject) throws TalentStudioException {

        QuestionnaireWorkflow questionnaireWorkflow = (QuestionnaireWorkflow) domObject;
        questionnaireWorkflow.setStatus(QuestionnaireWorkflow.STATUS_NEW);
        questionnaireDao.create(questionnaireWorkflow);

        // pass in value of questionnaireWorkflow.isNotificationBased() so that infoforms do not require users to be active and questionnaires do
        final Long userId = questionnaireWorkflow.getUserId();
        final boolean activeUsersOnly = questionnaireWorkflow.isNotificationBased();
        final Long populationId = questionnaireWorkflow.getPopulation().getId();
        Collection results = determineParticipants(userId, activeUsersOnly, populationId, populationEngine, analysisService);
        questionnaireDao.addParticipants(questionnaireWorkflow, results);
    }

    public void startWorkflow(QuestionnaireWorkflow questionnaireWorkflow) throws TalentStudioException {

        final Long user = questionnaireWorkflow.getUserId();

        if (questionnaireWorkflow.isNotificationBased()) {
            workflowAdapter.start(questionnaireWorkflow, user);
        }

        questionnaireWorkflow.setStatus(QuestionnaireWorkflow.STATUS_PUBLISHED);
        update(questionnaireWorkflow);
    }

    public void closeWorkflow(QuestionnaireWorkflow questionnaireWorkflow) throws TalentStudioException {
        // removes the people involved in the process
        removeParticipants(questionnaireWorkflow);
        // closes the workflow given and the completes the questionnaires associated to it 
        closeWorkflowProcess(questionnaireWorkflow);
    }

    /**
     * Needed to refresh the hibernate records, unfortunately the reload is via a stored procedure and is therefore not visible to hibernate
     * @param workflowId
     * @throws TalentStudioException
     */
    public void reload(Long workflowId) throws TalentStudioException {
        QuestionnaireWorkflow workflow = findWorkflowById(workflowId);
        questionnaireDao.reload(workflow);
    }

    /**
     * Marks questionnaires associated with workflow as completed and sets date completed to current date.
     * <br/> Only applies to info forms.
     *
     * @param questionnaireWorkflow questionnaireWorflow to be closed
     */
    protected void closeQuestionnaires(QuestionnaireWorkflow questionnaireWorkflow) {
        if (!questionnaireWorkflow.isNotificationBased()) {
            questionnaireDao.closeQuestionnaires(questionnaireWorkflow);
        }
    }

    /**
     * Deletes the workflow, the associated questionnaires and answers
     * @param questionnaireWorkflow - the workflow to be deleted
     * @throws TalentStudioException - any exceptions
     */
    public void delete(QuestionnaireWorkflow questionnaireWorkflow) throws TalentStudioException {
        closeWorkflowProcess(questionnaireWorkflow);
        questionnaireDao.delete(questionnaireWorkflow);
    }

    public void setNotificationActionable(Long notificationId, boolean actionable, String nextAction) throws TalentStudioException {
        workflowAdapter.setNotificationActionable(notificationId, actionable, nextAction);
    }

    protected IFinder getFinderDao() {
        return questionnaireDao;
    }

    protected IModifiable getModifierDao() {
        return questionnaireDao;
    }

    public List findSearchableInfoforms() throws TalentStudioException {
        return questionnaireDao.findSearchableWorkflows();
    }

    public void handleExpiredWorkflowsAndAppraisals() throws TalentStudioException {

        final Collection<QuestionnaireWorkflow> expiredWorkflows = questionnaireDao.findExpiredWorkflows();
        for (QuestionnaireWorkflow questionnaireWorkflow : expiredWorkflows) {
            final PerformanceReview performanceReview = questionnaireWorkflow.getPerformanceReview();
            if (performanceReview != null) {
                performanceReviewService.closeReview(performanceReview);
            } else {
                closeWorkflow(questionnaireWorkflow);
            }
        }
    }

    public RepublishResults republishWorkflow(Long queId, Long userId) throws TalentStudioException {

        QuestionnaireWorkflow queWorkflow = (QuestionnaireWorkflow) this.findById(queId);


        final Date republishedDate = new Date();
        queWorkflow.setLastRepublishedDate(republishedDate);


        final Long populationId = queWorkflow.getPopulation().getId();
        Collection expectedParticipants = determineParticipants(userId, false, populationId, populationEngine, analysisService);
        Collection currentParticipants = questionnaireDao.getSubjectParticipants(queWorkflow);

        Collection<Subject> toAdd = (Collection<Subject>) CollectionUtils.subtract(expectedParticipants, currentParticipants);
        Collection<Subject> toRemove = (Collection<Subject>) CollectionUtils.subtract(currentParticipants, expectedParticipants);


        String[] toAddNames = getArrayOfFullNames(toAdd);
        String[] toRemoveNames = getArrayOfFullNames(toRemove);

        questionnaireDao.addParticipants(queWorkflow, toAdd);
        questionnaireDao.removeParticipants(queWorkflow, toRemove);

        // set the results
        RepublishResults results = new RepublishResults();
        results.setQueId(queId);
        results.setUsersAdded(toAddNames);
        results.setUsersRemoved(toRemoveNames);
        results.setCompletedDate(republishedDate);
        
        return results;
    }

    private String[] getArrayOfFullNames(Collection<Subject> subjects) {
        String[] fullNames = new String[subjects.size()];
        int counter = 0;
        for (Subject subject : subjects) {
            StringBuilder fullName = new StringBuilder();
            fullName.append(subject.getFirstName());
            fullName.append(" ");
            fullName.append(subject.getSecondName());
            fullNames[counter] = fullName.toString();
            counter++;
        }
        return fullNames;
    }

    public IDomainObject findById(Serializable questionnaireWorkflowId) throws TalentStudioException {
        final QuestionnaireWorkflow questionnaireWorkflow = (QuestionnaireWorkflow) questionnaireDao.findByID(questionnaireWorkflowId);
        questionnaireWorkflow.getQuestionnaireDefinition().getDynamicAttributes();
        return questionnaireWorkflow;
    }

    public QuestionnaireWorkflow findWorkflowById(Long queWorkflowId) throws TalentStudioException {
        return (QuestionnaireWorkflow) questionnaireDao.findByID(QuestionnaireWorkflow.class, queWorkflowId);
    }

    public Collection<QuestionnaireWorkflowDTO> findAllQuestionnaireWorkflowDTOs() {
        return questionnaireDao.findAllQuestionnaireWorkflowDTOs();
    }

    public Collection<QuestionnaireWorkflowDTO> findAllWorkflowDTOs() {
        return questionnaireDao.findAllWorkflowDTOs();
    }

    public void setPerformanceReviewService(IPerformanceReviewService performanceReviewService) {
        this.performanceReviewService = performanceReviewService;
    }

    private IPerformanceReviewService performanceReviewService;    
}

/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.performance;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.IFinder;
import com.zynap.talentstudio.common.IModifiable;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;
import com.zynap.talentstudio.workflow.Notification;
import com.zynap.domain.admin.User;

import java.util.Collection;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface IPerformanceReviewDao extends IFinder, IModifiable {

    List<PerformanceEvaluator> getRoles(Long performanceId, Long subjectId);

    void saveOrUpdateEvaluator(PerformanceEvaluator performanceEvaluator) throws TalentStudioException;

    void deleteEvaluator(PerformanceEvaluator performanceEvaluator) throws TalentStudioException;

    void deleteEvaluators(Long evaluateeSubjectId, Long reviewId);

    /**
     * Get's the managers due to recieve a start the performance review process
     *
     * @param questionnaireWorkflow takes questionnaire workflow for which you wish to find the managers for.
     * @return List of users {@link com.zynap.domain.admin.User} objects
     */
    public Collection<User> getManagers(QuestionnaireWorkflow questionnaireWorkflow);

    PerformanceManager findPerformanceManager(Long performanceId, Long subjectId) throws TalentStudioException;

    void deletePerformanceManager(PerformanceManager performanceManager) throws TalentStudioException;

    void saveOrUpdatePerformanceManager(PerformanceManager performanceManager)throws TalentStudioException;

    List<Notification> getAppraisalReviewNotifications(Long performanceReviewId);

    List<PerformanceReview> getAllPerformanceReviews();

    QuestionnaireDefinition findPerformanceDefinition(Long appraisalId);


}

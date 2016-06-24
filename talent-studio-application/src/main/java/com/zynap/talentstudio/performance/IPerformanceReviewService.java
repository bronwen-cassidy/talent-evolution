/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.performance;

import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.common.IZynapService;
import com.zynap.talentstudio.questionnaires.IQuestionnaireCommon;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;
import com.zynap.talentstudio.workflow.Notification;

import java.util.Date;
import java.util.List;
import java.util.Collection;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 27-Mar-2008 11:38:56
 */
public interface IPerformanceReviewService extends IZynapService, IQuestionnaireCommon {

    void createReview(PerformanceReview performanceReview, User user, Long managerQuestionnaireDefinitionId, Long evaluatorQuestionnaireDefinitionId, Long populationId, Date expiryDate, Long hrUserId) throws TalentStudioException;

    void startReview(PerformanceReview performanceReview, User user, boolean userManagedReview) throws TalentStudioException;

    void deleteReview(PerformanceReview performanceReview) throws TalentStudioException;
    
    void deleteReview(Long id) throws TalentStudioException;

    void closeReview(PerformanceReview performanceReview) throws TalentStudioException;

    List<PerformanceEvaluator> getAssignedPerformanceEvaluators(Long performanceReviewId, Long subjectId) throws TalentStudioException;

    void saveRoles(List<PerformanceEvaluator> performanceEvaluators, Long evaluateeSubjectId, Long reviewId) throws TalentStudioException;

    Collection<User> getManagers(PerformanceReview performanceReview);

    /**
     * Get roles - gets the active lookup values for the appraisal role lookup type.
     *
     * @return List of {@link com.zynap.talentstudio.common.lookups.LookupValue}s.
     */
    List<LookupValue> getRoles();

    PerformanceManager findPerformanceManager(Long performanceId, Long subjectId) throws TalentStudioException;

    void saveRoles(List<PerformanceEvaluator> performanceEvaluators, PerformanceManager performanceManager) throws TalentStudioException;

    List<Notification> getAppraisalReviewNotifications(Long performanceReviewId);

    List<PerformanceReview> findAllPerformanceReviews();

    QuestionnaireDefinition findPerformanceDefinition(Long appraisalId);


}

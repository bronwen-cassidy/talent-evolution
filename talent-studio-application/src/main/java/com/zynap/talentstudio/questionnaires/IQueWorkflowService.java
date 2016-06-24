package com.zynap.talentstudio.questionnaires;

import com.zynap.exception.TalentStudioException;

import com.zynap.talentstudio.common.IZynapService;
import com.zynap.talentstudio.questionnaires.support.RepublishResults;

import java.util.List;
import java.util.Collection;

/**
 * Class or Interface description.
 *
 * @author syeoh
 * @version 0.1
 * @since 27-Apr-2007 11:43:35
 */

public interface IQueWorkflowService extends IZynapService, IQuestionnaireCommon {

    List findSearchableInfoforms() throws TalentStudioException;

    RepublishResults republishWorkflow(Long queId, Long userId) throws TalentStudioException;

    Collection<QuestionnaireWorkflowDTO> findAllQuestionnaireWorkflowDTOs();
    Collection<QuestionnaireWorkflowDTO> findAllWorkflowDTOs();

    void startWorkflow(QuestionnaireWorkflow questionnaireWorkflow) throws TalentStudioException;

    /**
     * Close workflow so that no more answers can be provided.
     * <br/> Removes workflow processes if any and sets status to {@link QuestionnaireWorkflow#STATUS_COMPLETED}.
     *
     * @param workflow
     * @throws TalentStudioException
     */
    void closeWorkflow(QuestionnaireWorkflow workflow) throws TalentStudioException;

    QuestionnaireWorkflow findWorkflowById(Long queWorkflowId) throws TalentStudioException;

    void delete(QuestionnaireWorkflow questionnaireWorkflow) throws TalentStudioException;

    void handleExpiredWorkflowsAndAppraisals() throws TalentStudioException;

    void setNotificationActionable(Long notificationId, boolean actionable, String nextAction) throws TalentStudioException;

    void reload(Long workflowId) throws TalentStudioException;

    void setNotificationActionable(Long notificationId, Long nextUserId, boolean actioable, String approveWorkflow) throws TalentStudioException;
}

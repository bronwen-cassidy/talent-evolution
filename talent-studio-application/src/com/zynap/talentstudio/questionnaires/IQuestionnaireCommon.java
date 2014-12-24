/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.questionnaires;

import com.zynap.domain.admin.User;

import java.util.Collection;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 27-Mar-2008 12:34:54
 */
public interface IQuestionnaireCommon {

    /**
     * Gets the participants for the given workflow.
     *
     * @param questionnaireWorkflow the workflow for whom people are assigned
     * @return Collection of {@link com.zynap.domain.admin.User} objects
     */
    Collection<User> getParticipants(QuestionnaireWorkflow questionnaireWorkflow);

    boolean isParticipant(Long subjectId, Long questionnaireWorkflowId);
}

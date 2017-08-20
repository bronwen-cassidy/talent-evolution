/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.questionnaires;

import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.IZynapService;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface IQuestionnaireService extends IZynapService {

    /**
     * Finds the questionnaire node for a given user with the given workflow.
     *
     * @param questionnaireWorkflowId
     * @param userId
     * @param subjectId
     * @param roleId
     * @return Questionnaire or null if the questionnaire does not exist.
     * @throws TalentStudioException
     */
    Questionnaire findQuestionnaireByWorkflow(Long questionnaireWorkflowId, Long userId, Long subjectId, Long roleId) throws TalentStudioException;

    /**
     * This method loads and locks down the questionnaire for editing.
     *
     * @param questionnaireId          - the id of the questionnaire may not be null
     * @param loggedInUserId           - the user that is currently logged in
     * @return Questionnaire           - the locked for editing questionnaire
     * @throws TalentStudioException   - any pessimistic locking exception specifically.
     */
    Questionnaire loadQuestionnaire(Long questionnaireId, Long loggedInUserId) throws TalentStudioException;


    /**
     * Update the questionnnaire (used to save answers.)
     *
     * @param questionnaire
     * @throws TalentStudioException
     */
    void createOrUpdateQuestionnaire(Questionnaire questionnaire) throws TalentStudioException;

    /**
     * Get the questionnaires for the subject portfolio.
     *
     * @param subjectId
     * @return Collection of {@link Questionnaire} objects (never returns null)
     * @throws TalentStudioException
     */
    Collection<QuestionnaireDTO> getPortfolioQuestionnaires(Long subjectId) throws TalentStudioException;

    /**
     * Get the completed questionnaires for my portfolio section.
     *
     * @param subjectId
     * @param userId
     * @return Collection of {@link Questionnaire} objects (never returns null)
     * @throws TalentStudioException
     */
    Collection<QuestionnaireDTO> getPersonalPortfolioQuestionnaires(Long subjectId, Long userId) throws TalentStudioException;

    /**
     * Find questionnaires for workflow and subject.
     *
     * @param workflowId
     * @param subjectId
     * @return Collection of {@link Questionnaire} objects (never returns null)
     * @throws TalentStudioException
     */
    Collection<Questionnaire> findQuestionnaires(Long workflowId, Long subjectId) throws TalentStudioException;

    void unlockQuestionnaires(Long loggedInUserId);

    Questionnaire findOrCreateQuestionnaire(Long workflowId, Long userId, Long subjectId) throws TalentStudioException;

    Long saveUpdateDeleteQuestionAttribute(Long queId, Long daId, Long attributeId, Integer dynamicPosition, String value, String currency, User modifiedBy) throws TalentStudioException;

    Long saveUpdateDeleteQuestionAttribute(Long queId, Long daId, Long attributeId, String value, User user) throws TalentStudioException;

    void insertQuestionnaireAttribute(Long queId, Long daId, Long attributeId, Integer dynamicPosition, String[] attributeIdValues, User user) throws TalentStudioException;

    void deleteQuestionnaireAttributes(Long queId, Long daId) throws TalentStudioException;

    Long saveDeleteQuestionnaireCheckBox(Long queId, Long daId, Long attributeId, String value, String action, Integer dynamicPosition, User modifedBy)throws TalentStudioException;

    void saveUpdateQuestionnaireAttributeDisabled(boolean disabled, Long queId, Long lineItemId, Integer dynamicPosition, User user)throws TalentStudioException;

    List<NodeExtendedAttribute> findAttributes(Long dynamicAttributeId, Long questionnaireId);

    void saveUpdateLastModifiedQuestionnaireAction(Long queId, Long queDefId, Date lastModified, User user)throws TalentStudioException;

    void deleteQuestionnaireAttribute(Long attributeId) throws TalentStudioException;

    void deleteLineItemAnswers(Long questionnaireId, Integer dynamicPosition, Long lineItemId);

    List<Questionnaire> findQuestionnairesByWorkflow(Long questionnaireWorkflowId);

    void unlockQuestionnaire(Long questionnaireId, Long userId);

    List<Questionnaire> findCompletedQuestionnaires(Long workflowId, Long subjectId) throws TalentStudioException;

    Integer countNumAppraisals(Long userId) throws TalentStudioException;

    Integer countNumQuestionnaires(Long userId) throws TalentStudioException;


    String EXPIRY_DATE_PATTERN = "dd/MM/yyyy";
}

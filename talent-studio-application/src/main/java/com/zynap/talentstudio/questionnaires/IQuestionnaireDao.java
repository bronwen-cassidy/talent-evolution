/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.questionnaires;

import com.zynap.domain.IDomainObject;
import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.IFinder;
import com.zynap.talentstudio.common.IModifiable;
import com.zynap.talentstudio.common.Specification;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;

import java.util.Collection;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface IQuestionnaireDao extends IFinder, IModifiable {

    void createDefinition(QuestionnaireDefinition questionnaireDefinition) throws TalentStudioException;

    void deleteDefinition(QuestionnaireDefinition questionnaireDefinition) throws TalentStudioException;

    QuestionnaireDefinition findDefinition(Long questionnaireDefinitionId);

    void createQuestionnaireModel(QuestionnaireDefinitionModel questionnaireDefinitionModel) throws TalentStudioException;

    void createQuestionnaireWorkflow(QuestionnaireWorkflow questionnaireWorkflow) throws TalentStudioException;

    Collection<QuestionnaireDefinition> findAllDefinitions() throws Exception;

    Collection<Questionnaire> findAllQuestionnaires() throws Exception;

    void updateDefinition(QuestionnaireDefinition questionnaireDefinition) throws TalentStudioException;

    Questionnaire findQuestionnaireByWorkflow(Long questionnaireWorkflowId, Long userId, Long subjectId, Long roleId) throws TalentStudioException;

    List<QuestionnaireDefinition> findPublishedQuestionnaireDefinitions(String[] questionTypes) throws TalentStudioException;

    void createOrUpdate(Questionnaire questionnaire) throws TalentStudioException;

    void createQuestionnaire(Questionnaire questionnaire) throws TalentStudioException;

    Questionnaire loadQuestionnaireForUpdate(Long questionnaireId, Long loggedInUserId) throws TalentStudioException;

    Collection<QuestionnaireDTO> getPortfolioQuestionnaires(Long subjectId) throws TalentStudioException;

    Collection<QuestionnaireDTO> getPersonalPortfolioQuestionnaires(Long subjectId, Long userId) throws TalentStudioException;

    /**
     * Get questionnaires answered in the specified workflow and for the specified subject.
     *
     * @param workflowId workflowid of questionnaires to be found and returned
     * @param subjectId  subjectid of questionnaires to be found and returned
     * @return collection of questionnaires fitting the supplied criteria
     * @throws TalentStudioException
     */
    Collection<Questionnaire> findQuestionnaires(Long workflowId, Long subjectId) throws TalentStudioException;

    /**
     * add a participant for the given workflow.
     *
     * @param questionnaireWorkflow the workflow who will get an added participant
     * @param subject               the participant to be added
     */
    void addParticipant(QuestionnaireWorkflow questionnaireWorkflow, Subject subject);

    void removeParticipant(QuestionnaireWorkflow questionnaireWorkflow, Subject subject);

    void addParticipants(QuestionnaireWorkflow questionnaireWorkflow, Collection<Subject> subjects);

    void removeParticipants(QuestionnaireWorkflow questionnaireWorkflow, Collection<Subject> subjects);

    boolean isParticipant(Long subjectId, Long questionnaireWorkflowId);


    /**
     * Finds participants for the given workflow
     *
     * @param questionnaireWorkflow
     * @return List of participants.
     */
    Collection<User> getParticipants(QuestionnaireWorkflow questionnaireWorkflow);

    Collection getSubjectParticipants(QuestionnaireWorkflow questionnaireWorkflow);

    /**
     * Remove participants from workflow.
     *
     * @param questionnaireWorkflow
     */
    void removeAllParticipants(QuestionnaireWorkflow questionnaireWorkflow);

    /**
     * Find incomplete workflows that have expired.
     *
     * @return Collection of {@link QuestionnaireWorkflow} objects.
     */
    Collection<QuestionnaireWorkflow> findExpiredWorkflows();

    Collection findWorkflowsForPopulation(Long populationId);

    Collection findAllWorkflows();

    /**
     * Finds the workflows that are manager write, not completed and is an info form
     *
     * @return questionnaires that are searchable and editable
     */
    List<QuestionnaireDTO> findSearchableWorkflows();

    void updateQuestionnaireModel(QuestionnaireDefinitionModel questionnaireDefinitionModel) throws TalentStudioException;

    List<DefinitionDTO> listDefinitions();

    Collection<QuestionnaireWorkflowDTO> findAllQuestionnaireWorkflowDTOs();

    Collection<QuestionnaireWorkflowDTO> findAllWorkflowDTOs();

    void unlockQuestionnaires(Long loggedInUserId);

    void unlockQuestionnaire(Long questionnaireId, Long userId);

    Questionnaire findOrCreateQuestionnaire(Long workflowId, Long userId, Long subjectId) throws TalentStudioException;

    void removeNotifications(Long performanceId);

    void createAttribute(NodeExtendedAttribute newAttribute);

    NodeExtendedAttribute findAttribute(Long attributeId);

    void deleteQuestionnaireAttributes(Long queId, Long daId);

    void deleteAttribute(Long attributeId);

    List<NodeExtendedAttribute> findNodeAttributes(Long daId, Integer dynamicPosition, Long questionnaireId);

    List<QuestionAttribute> findLineItemQuestions(Long lineItemId);

    void createOrUpdateAttribute(NodeExtendedAttribute newAttribute);

    List<NodeExtendedAttribute> findAttributes(Long dynamicAttributeId, Long questionnaireId);

    List<DynamicAttribute> findAttributesLastMofidied(Long queDefId);

    void createData(QuestionnaireXmlData data);

    QuestionnaireXmlData findQuestionnaireXmlData(Long questionnaireDefinitionId);

    void closeQuestionnaires(QuestionnaireWorkflow questionnaireWorkflow);

    void deleteLineItemAnswers(Long questionnaireId, Integer dynamicPosition, Long lineItemId);

    List<Questionnaire> findQuestionnairesByWorkflow(Long questionnaireWorkflowId);

    List<Questionnaire> findCompletedQuestionnaires(Long workflowId, Long subjectId) throws TalentStudioException;

    void reload(QuestionnaireWorkflow questionnaireWorkflow);

    Long getLineItemId(Long dynamicAttributeId);

    Integer countNumAppraisals(Long userId) throws TalentStudioException;

    Integer countNumQuestionnaires(final Long userId) throws TalentStudioException;

	List<QuestionnaireWorkflowDTO> query(Specification spec);
}

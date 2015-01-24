/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.questionnaires;

import com.zynap.domain.IDomainObject;
import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.DefaultService;
import com.zynap.talentstudio.common.IFinder;
import com.zynap.talentstudio.common.IModifiable;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeDao;
import com.zynap.talentstudio.util.FormatterFactory;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @author taulant bajraktari
 * @version $Revision: $
 *          $Id: $
 */


public class QuestionnaireService extends DefaultService implements IQuestionnaireService {

    public Collection<Questionnaire> findQuestionnaires(Long workflowId, Long subjectId) throws TalentStudioException {
        return questionnaireDao.findQuestionnaires(workflowId, subjectId);
    }

    public List<Questionnaire> findCompletedQuestionnaires(Long workflowId, Long subjectId) throws TalentStudioException {
        return questionnaireDao.findCompletedQuestionnaires(workflowId, subjectId);
    }

    /**
     * This method loads and locks down the questionnaire for editing.
     *
     * @param questionnaireId - the id of the questionnaire may not be null
     * @param loggedInUserId  - the user that is currently logged in
     * @return Questionnaire           - the locked for editing questionnaire
     * @throws TalentStudioException - any pessimistic locking exception specifically.
     */
    public Questionnaire loadQuestionnaire(Long questionnaireId, Long loggedInUserId) throws TalentStudioException {
        return questionnaireDao.loadQuestionnaireForUpdate(questionnaireId, loggedInUserId);
    }

    public void createOrUpdateQuestionnaire(Questionnaire questionnaire) throws TalentStudioException {
        questionnaireDao.createOrUpdate(questionnaire);
    }

    public Collection<QuestionnaireDTO> getPortfolioQuestionnaires(Long subjectId) throws TalentStudioException {
        return questionnaireDao.getPortfolioQuestionnaires(subjectId);
    }

    public List<Questionnaire> findQuestionnairesByWorkflow(Long questionnaireWorkflowId) {
        return questionnaireDao.findQuestionnairesByWorkflow(questionnaireWorkflowId);
    }

    public void unlockQuestionnaire(Long questionnaireId, Long userId) {
        questionnaireDao.unlockQuestionnaire(questionnaireId, userId);
    }

    public Long saveUpdateDeleteQuestionAttribute(Long queId, Long daId, Long attributeId, Integer dynamicPosition, String value, User modifiedBy) throws TalentStudioException {

        boolean create = attributeId == null && StringUtils.hasText(value);
        boolean delete = (attributeId != null && !StringUtils.hasText(value));
        boolean update = attributeId != null && StringUtils.hasText(value);

        final Questionnaire questionnaire = new Questionnaire();
        questionnaire.setId(queId);

        final DynamicAttribute attribute = (DynamicAttribute) attributeDao.findByID(daId);
        // get the question and the line item id only if dynamic
        Long lineItemId = null;
        if(attribute.isDynamic()) {
            lineItemId = questionnaireDao.getLineItemId(daId);    
        }

        Integer attrDynamicPosition = attribute.isDynamic() ? dynamicPosition : null;
        Date modifiedDate = new Date();

        if (create) {
            NodeExtendedAttribute newAttribute = new NodeExtendedAttribute(value, questionnaire, attribute);
            newAttribute.setDynamicPosition(attrDynamicPosition);
            newAttribute.setAddedBy(modifiedBy);
            newAttribute.setDateAdded(modifiedDate);
            newAttribute.setLineItemId(lineItemId);
            questionnaireDao.createAttribute(newAttribute);

            return newAttribute.getId();
        } else if (delete) {
            NodeExtendedAttribute toDelete = questionnaireDao.findAttribute(attributeId);
            questionnaireDao.delete(toDelete);
            return null;
        } else if (update) {
            //update
            final NodeExtendedAttribute updatedAttribute = questionnaireDao.findAttribute(attributeId);
            updatedAttribute.setValue(value);
            updatedAttribute.setDynamicPosition(attrDynamicPosition);
            updatedAttribute.setAddedBy(modifiedBy);
            updatedAttribute.setDateAdded(modifiedDate);
            questionnaireDao.update(updatedAttribute);
        }
        return attributeId;
    }

    public Long saveUpdateDeleteQuestionAttribute(Long queId, Long daId, Long attributeId, String value, User user) throws TalentStudioException {

        boolean create = attributeId == null && StringUtils.hasText(value);
        boolean delete = (attributeId != null && !StringUtils.hasText(value));

        final Questionnaire questionnaire = new Questionnaire();
        questionnaire.setId(queId);
        final DynamicAttribute attribute = new DynamicAttribute(daId);

        if (create) {
            NodeExtendedAttribute newAttribute = new NodeExtendedAttribute(value, questionnaire, attribute);
            newAttribute.setAddedBy(user);
            newAttribute.setDateAdded(new Date());
            questionnaireDao.createAttribute(newAttribute);
            return newAttribute.getId();
        } else if (delete) {
            NodeExtendedAttribute toDelete = questionnaireDao.findAttribute(attributeId);
            questionnaireDao.delete(toDelete);
            return null;
        } else {
            NodeExtendedAttribute newAttribute = questionnaireDao.findAttribute(attributeId);
            newAttribute.setValue(value);
            newAttribute.setAddedBy(user);
            newAttribute.setDateAdded(new Date());
            questionnaireDao.update(newAttribute);
            return newAttribute.getId();
        }
    }

    public void insertQuestionnaireAttribute(Long queId, Long daId, Long attributeId, Integer dynamicPosition, String[] attributeIdValues, User user) throws TalentStudioException {

        final Questionnaire questionnaire = new Questionnaire();
        questionnaire.setId(queId);
        final DynamicAttribute attribute = (DynamicAttribute) attributeDao.findByID(daId);
        Integer attrDynamicPosition = attribute.isDynamic() ? dynamicPosition : null;

        for (int i = 0; i < attributeIdValues.length; i++) {
            String value = attributeIdValues[i];
            if (StringUtils.hasText(value)) {
                NodeExtendedAttribute newAttribute = new NodeExtendedAttribute(value, questionnaire, attribute);
                newAttribute.setDynamicPosition(attrDynamicPosition);
                newAttribute.setAddedBy(user);
                newAttribute.setDateAdded(new Date());
                questionnaireDao.createAttribute(newAttribute);
            }
        }
    }

    public void deleteQuestionnaireAttributes(Long queId, Long daId) throws TalentStudioException {
        //delete all attribute ids where id
        questionnaireDao.deleteQuestionnaireAttributes(queId, daId);
    }

    public Long saveDeleteQuestionnaireCheckBox(Long queId, Long daId, Long attributeId, String value, String action, Integer dynamicPosition, User modifedBy) throws TalentStudioException {

        final Questionnaire questionnaire = new Questionnaire();
        questionnaire.setId(queId);
        final DynamicAttribute attribute = (DynamicAttribute) attributeDao.findByID(daId);
        Integer attrDynamicPosition = attribute.isDynamic() ? dynamicPosition : null;

        // questionnaire.addOrUpdateAttributeValue();
        if (CHECKBOX_ACTION_DELETE.equals(action)) {
            NodeExtendedAttribute toDelete = questionnaireDao.findAttribute(attributeId);
            questionnaireDao.delete(toDelete);
            return null;
        } else {
            NodeExtendedAttribute newAttribute = new NodeExtendedAttribute(value, questionnaire, attribute);
            newAttribute.setDynamicPosition(attrDynamicPosition);
            newAttribute.setAddedBy(modifedBy);
            newAttribute.setDateAdded(new Date());
            questionnaireDao.createAttribute(newAttribute);
            return newAttribute.getId();
        }
    }

    public void saveUpdateQuestionnaireAttributeDisabled(boolean disabled, Long queId, Long lineItemId, Integer dynamicPosition, User user) throws TalentStudioException {
        try {
            List<QuestionAttribute> listItemQuestions = questionnaireDao.findLineItemQuestions(lineItemId);

            for (QuestionAttribute qAttr : listItemQuestions) {
                List<NodeExtendedAttribute> dynamicAttributesList;
                boolean dynamic = qAttr.isDynamic();
                if (dynamic) {
                    dynamicAttributesList = questionnaireDao.findNodeAttributes(qAttr.getDynamicAttribute().getId(), dynamicPosition, queId);
                } else {
                    dynamicAttributesList = questionnaireDao.findAttributes(qAttr.getDynamicAttribute().getId(), queId);
                }
                
                for (NodeExtendedAttribute nodeExtendedAtt : dynamicAttributesList) {
                    nodeExtendedAtt.setDisabled(disabled);
                    if(dynamic) nodeExtendedAtt.setDynamicPosition(dynamicPosition);
                    nodeExtendedAtt.setAddedBy(user);
                    nodeExtendedAtt.setDateAdded(new Date());
                    questionnaireDao.update(nodeExtendedAtt);
                }
            }

        } catch (Throwable e) {
            e.printStackTrace();
            logger.error(e);
        }
    }

    public void deleteQuestionnaireAttribute(Long attributeId) throws TalentStudioException {
        try {
            questionnaireDao.deleteAttribute(attributeId);
        } catch (Throwable e) {
            //exception expected - void
            //trying to get an object that already may be deleted
        }

    }

    public void deleteLineItemAnswers(Long questionnaireId, Integer dynamicPosition, Long lineItemId) {
        questionnaireDao.deleteLineItemAnswers(questionnaireId, dynamicPosition, lineItemId);
    }

    public List<NodeExtendedAttribute> findAttributes(Long dynamicAttributeId, Long questionnaireId) {
        return questionnaireDao.findAttributes(dynamicAttributeId, questionnaireId);
    }

    public void saveUpdateLastModifiedQuestionnaireAction(Long queId, Long queDefId, Date lastModified, User user) throws TalentStudioException {

        //find dynamic attributes
        List<DynamicAttribute> dymanicAttributes = questionnaireDao.findAttributesLastMofidied(queDefId);

        for (DynamicAttribute dynamicAttribute : dymanicAttributes) {

            List<NodeExtendedAttribute> nodeExtendedAttrs = questionnaireDao.findAttributes(dynamicAttribute.getId(), queId);
            //what if node extended attrs is empty
            String value = dynamicAttribute.isLastUpdatedByType() ? user.getId().toString() : FormatterFactory.getDateFormatter().getStoredDateTimeFormat(lastModified);
            if (nodeExtendedAttrs.isEmpty()) {
                NodeExtendedAttribute attr = new NodeExtendedAttribute(value, new Node(queId), dynamicAttribute);
                questionnaireDao.createAttribute(attr);
            }
            // the extended node
            for (NodeExtendedAttribute nodeExAtt : nodeExtendedAttrs) {
                nodeExAtt.setValue(value);
                questionnaireDao.update(nodeExAtt);
            }
        }
    }


    public Collection<QuestionnaireDTO> getPersonalPortfolioQuestionnaires(Long subjectId, Long userId) throws TalentStudioException {
        return questionnaireDao.getPersonalPortfolioQuestionnaires(subjectId, userId);
    }

    public void unlockQuestionnaires(Long loggedInUserId) {
        questionnaireDao.unlockQuestionnaires(loggedInUserId);
    }

    public Questionnaire findOrCreateQuestionnaire(Long workflowId, Long userId, Long subjectId) throws TalentStudioException {
        return questionnaireDao.findOrCreateQuestionnaire(workflowId, userId, subjectId);
    }

    public Questionnaire findQuestionnaireByWorkflow(Long questionnaireWorkflowId, Long userId, Long subjectId, Long roleId) throws TalentStudioException {
        return questionnaireDao.findQuestionnaireByWorkflow(questionnaireWorkflowId, userId, subjectId, roleId);
    }

    public IDomainObject findById(Serializable id) throws TalentStudioException {
        return (IDomainObject) questionnaireDao.findByID(Questionnaire.class, id);
    }

    public List<Questionnaire> findAll() throws TalentStudioException {
        List<Questionnaire> questionnaires = new ArrayList<Questionnaire>();

        try {
            questionnaires = (List<Questionnaire>) questionnaireDao.findAllQuestionnaires();
        } catch (Exception e) {
            logger.error(e);
        }

        return questionnaires;
    }

    public void create(IDomainObject domainObject) throws TalentStudioException {
        questionnaireDao.createQuestionnaire((Questionnaire) domainObject);
    }

    public void update(IDomainObject domainObject) throws TalentStudioException {
        questionnaireDao.createOrUpdate((Questionnaire) domainObject);
    }

    public Integer countNumAppraisals(Long userId) throws TalentStudioException {
        return questionnaireDao.countNumAppraisals(userId);
    }

    public Integer countNumQuestionnaires(Long userId) throws TalentStudioException {
        return questionnaireDao.countNumQuestionnaires(userId);
    }

    protected IFinder getFinderDao() {
        return questionnaireDao;
    }

    public IModifiable getModifierDao() {
        return questionnaireDao;
    }

    public void setQuestionnaireDao(IQuestionnaireDao questionnaireDao) {
        this.questionnaireDao = questionnaireDao;
    }

    public void setAttributeDao(IDynamicAttributeDao attributeDao) {
        this.attributeDao = attributeDao;
    }

    private IQuestionnaireDao questionnaireDao;
    private IDynamicAttributeDao attributeDao;
    private static final String CHECKBOX_ACTION_DELETE = "delete";
}

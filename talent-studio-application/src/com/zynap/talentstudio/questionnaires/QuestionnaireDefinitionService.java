/*
 * Copyright (C)  Zynap Ltd. 2006
 * All rights reserved.
 */

package com.zynap.talentstudio.questionnaires;

import com.zynap.domain.IDomainObject;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.IFinder;
import com.zynap.talentstudio.common.IModifiable;
import com.zynap.talentstudio.common.lookups.LookupType;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.security.UserSessionFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author syeoh
 * @version 0.1
 * @since 12-Mar-2007 17:20:44
 */

public class QuestionnaireDefinitionService extends AbstractQuestionnaireService implements IQueDefinitionService {

    public QuestionnaireDefinition findDefinition(Long queDefinitionId) {
        return questionnaireDao.findDefinition(queDefinitionId);
    }

    public List<DefinitionDTO> listDefinitions() {
        return questionnaireDao.listDefinitions();
    }

    public List<QuestionnaireDefinition> findAll() throws TalentStudioException {
        try {
            return (List<QuestionnaireDefinition>) questionnaireDao.findAllDefinitions();
        } catch (Exception e) {
            logger.error("An exception occurred whilst trying to find all questionnairedefinitions.");
            logger.error("Error details : " + e.toString());
            throw new TalentStudioException("finding all questionnaire definitions failed: " + e.getMessage(), e);
        }
    }

    public List<QuestionnaireDefinition> findReportableDefinitions(String[] questionTypes) throws TalentStudioException {
        return questionnaireDao.findPublishedQuestionnaireDefinitions(questionTypes);
    }

    public void createXml(QuestionnaireXmlData data) {
        questionnaireDao.createData(data);
    }

    public QuestionnaireXmlData findQuestionnaireDefinitionXml(Long questionnaireDefinitionId) {
        return questionnaireDao.findQuestionnaireXmlData(questionnaireDefinitionId);
    }

    public void create(IDomainObject domainObject) throws TalentStudioException {
        QuestionnaireDefinition questionnaireDefinition = (QuestionnaireDefinition) domainObject;
        try {
            createLookupTypes(questionnaireDefinition);
            questionnaireDao.createDefinition(questionnaireDefinition);
            QuestionnaireDefinitionModel questionnaireDefinitionModel = questionnaireDefinition.getQuestionnaireDefinitionModel();
            questionnaireDefinitionModel.setQueDefinitionId(questionnaireDefinition.getId());
            questionnaireDao.createQuestionnaireModel(questionnaireDefinitionModel);
        } catch (Exception e) {
            logger.error("An exception occurred whilst trying to create a new questionnairedefinition with id : " + questionnaireDefinition.getId() + ".");
            logger.error("Error details : " + e.toString());
            throw new TalentStudioException("creation of the questionnaire definition failed: " + e.getMessage(), e);
        }
    }

    /**
     * Create lookup types associated with dynamic attributes in questionnaire definition.
     *
     * @param questionnaireDefinition - the definition the lookup types are defined for
     * @throws com.zynap.exception.TalentStudioException
     *          - if creation of the types errors
     */
    public void createLookupTypes(QuestionnaireDefinition questionnaireDefinition) throws TalentStudioException {
        final Long userId = UserSessionFactory.getUserSession().getId();
        final Collection<DynamicAttribute> attributes = questionnaireDefinition.getDynamicAttributes();
        for (DynamicAttribute dynamicAttribute : attributes) {
            final LookupType lookupType = dynamicAttribute.getRefersToType();
            if (lookupType != null && lookupType.getTypeId() == null) {
                lookupManagerDao.createLookupType(userId, lookupType);
            }
        }
    }

    public void update(IDomainObject domainObject) throws TalentStudioException {
        QuestionnaireDefinition questionnaireDefinition = (QuestionnaireDefinition) domainObject;
        questionnaireDao.updateDefinition(questionnaireDefinition);
        questionnaireDao.updateQuestionnaireModel(questionnaireDefinition.getQuestionnaireDefinitionModel());
    }

    public void delete(IDomainObject domainObject) throws TalentStudioException {

        QuestionnaireDefinition questionnaireDefinition = (QuestionnaireDefinition) domainObject;
        questionnaireDao.deleteDefinition(questionnaireDefinition);
    }

    /**
     * Delete all lookup types associated with dynamic attributes in questionnaire definition.
     *
     * @param questionnaireDefinition - the definition to delete the lookup types from
     * @throws com.zynap.exception.TalentStudioException
     *          - if deletion fails
     */
    public void deleteLookupTypes(QuestionnaireDefinition questionnaireDefinition) throws TalentStudioException {
        final Collection<DynamicAttribute> attributes = questionnaireDefinition.getDynamicAttributes();
        final Collection<LookupType> deleteables = new HashSet<LookupType>();
        for (Iterator<DynamicAttribute> iterator = attributes.iterator(); iterator.hasNext();) {
            final DynamicAttribute dynamicAttribute = iterator.next();
            final LookupType lookupType = dynamicAttribute.getRefersToType();
            if (lookupType != null && lookupType.getTypeId() != null) {
                deleteables.add(lookupType);
            }
        }

        lookupManagerDao.deleteLookupTypes(deleteables);
    }

    protected IFinder getFinderDao() {
        return questionnaireDao;
    }

    protected IModifiable getModifierDao() {
        return questionnaireDao;
    }
}

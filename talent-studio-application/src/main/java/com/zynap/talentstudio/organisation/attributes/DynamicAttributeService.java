/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation.attributes;

import com.zynap.domain.IDomainObject;
import com.zynap.domain.admin.User;
import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.DefaultService;
import com.zynap.talentstudio.common.IFinder;
import com.zynap.talentstudio.common.IModifiable;
import com.zynap.talentstudio.help.HelpTextItem;
import com.zynap.talentstudio.help.IHelpTextDao;
import com.zynap.talentstudio.organisation.Node;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
@Transactional
public class DynamicAttributeService extends DefaultService implements IDynamicAttributeService {

    public Collection<DynamicAttribute> getActiveAttributes(String nodeType, boolean searchableOnly, String[] attributeTypes) {
        return getActiveAttributes(nodeType, searchableOnly, attributeTypes, true);
    }


    public Collection<DynamicAttribute> getActiveAttributes(String nodeType, boolean searchableOnly, String[] attributeTypes, boolean includeCalcFields) {
        return dynamicAttributeDao.getActiveAttributes(nodeType, searchableOnly, attributeTypes, includeCalcFields);
    }

    /**
     * Get active attributes for the given node types.
     * <br/> If attributeTypes is not provided will return all.
     * <br/> If searchableOnly is true will only return active searchable dynamic attributes.
     *
     * @param nodeTypes      nodeTypes the attributes are relevant for
     * @param searchableOnly true or false
     * @param attributeTypes can be null
     * @return Collection of {@link com.zynap.talentstudio.organisation.attributes.DynamicAttributeDTO } objects
     */
    public List<DynamicAttributeDTO> listActiveAttributes(String[] nodeTypes, boolean searchableOnly, String[] attributeTypes) {
        return dynamicAttributeDao.listActiveAttributes(nodeTypes, searchableOnly, attributeTypes);
    }

    public List<DynamicAttributeDTO> listAllAttributes(String nodeType) {
        return dynamicAttributeDao.listAllAttributes(nodeType);
    }

    public Collection<DynamicAttribute> getAllAttributes(String nodeType) {
        return dynamicAttributeDao.getAllAttributes(nodeType);
    }

    public Collection<DynamicAttribute> getAllAttributes(Long[] attributeIds) {
        return dynamicAttributeDao.getAllAttributes(attributeIds);
    }

    @Override
    public Map<String, String> getAllSubjectAttributes(Long subjectId) {
        Map<String, String> result = new HashMap<>();
        if (subjectId != null) {
            List<NodeExtendedAttribute> answers = dynamicAttributeDao.findAllSubjectAnswers(subjectId);
            for (NodeExtendedAttribute attr : answers) {
                AttributeValue attributeValue = AttributeValue.create(attr);
                String externalRefLabel = org.springframework.util.StringUtils.replace(attr.getDynamicAttribute().getExternalRefLabel(), ".", "_");
                result.put(externalRefLabel, attributeValue.getDisplayValue());
            }
        }
        return result;
    }

    public Collection<DynamicAttribute> getSearchableAttributes(String nodeType) {
        return dynamicAttributeDao.getSearchableAttributes(nodeType);
    }

    public Collection<DynamicAttributeDTO> getSearchableAttributeDtos(String nodeType) {
        return dynamicAttributeDao.getSearchableAttributeDtos(nodeType);
    }

    public Collection getTypedAttributes(String nodeType, String attributeType) {
        return dynamicAttributeDao.getTypedAttributes(nodeType, attributeType);
    }

    public Collection<DynamicAttribute> getAllActiveAttributes(String nodeType, boolean includeCalculatedAttributes) {
        return dynamicAttributeDao.getAllActiveAttributes(nodeType, includeCalculatedAttributes);
    }

    public boolean usedByNode(Long id) {
        return dynamicAttributeDao.usedByNode(id);
    }

    public void setHelpTextDao(IHelpTextDao helpTextDao) {
        this.helpTextDao = helpTextDao;
    }

    public void setDynamicAttributeDao(IDynamicAttributeDao dynamicAttributeDao) {
        this.dynamicAttributeDao = dynamicAttributeDao;
    }

    public void create(IDomainObject domainObject) throws TalentStudioException {

        final DynamicAttribute dynamicAttribute = (DynamicAttribute) domainObject;

        //The external ref label is set when the dynamicAttribute is created and does not change.
        dynamicAttribute.setExternalRefLabel(StringUtils.deleteWhitespace(dynamicAttribute.getLabel().trim().toLowerCase()));
        dynamicAttributeDao.create(dynamicAttribute);
    }

    public void update(IDomainObject domainObject) throws TalentStudioException {

        final DynamicAttribute dynamicAttribute = (DynamicAttribute) domainObject;
        final Long id = dynamicAttribute.getId();

        // save help text
        final HelpTextItem helpTextItem = dynamicAttribute.getHelpTextItem();
        if (helpTextItem != null) {
            helpTextItem.setId(id);
            helpTextDao.saveOrUpdate(helpTextItem);
        } else {
            helpTextDao.delete(id);
        }

        super.update(dynamicAttribute);
    }

    public void delete(Long attributeId) throws TalentStudioException {
        final DynamicAttribute dynamicAttribute = dynamicAttributeDao.findById(attributeId);
        dynamicAttributeDao.delete(dynamicAttribute);
    }

    public AttributeValueFile findAttributeValueFile(Long id) {
        return dynamicAttributeDao.findAttributeValueFile(id);
    }

    public String getDomainObjectLabel(AttributeValue attributeValue) {
        IDomainObject domainObject = getDomainObject(attributeValue);
        return getLabel(domainObject);
    }

    public String getDomainObjectLabel(String id, DynamicAttribute dynamicAttribute) {
        final IDomainObject domainObject = findDomainObject(id, dynamicAttribute);
        return getLabel(domainObject);
    }

    public IDomainObject getDomainObject(AttributeValue attributeValue) {

        IDomainObject domainObject = null;

        if (attributeValue != null) {
            final String value = attributeValue.getValue();
            final DynamicAttribute dynamicAttribute = attributeValue.getDynamicAttribute();
            domainObject = findDomainObject(value, dynamicAttribute);
        }

        return domainObject;
    }

    public String getNodeLabel(String id) {
        final IDomainObject domainObject = findDomainObject(id, Node.class);
        return getLabel(domainObject);
    }

    public HelpTextItem findHelpTextItem(Long id) throws TalentStudioException {
        try {
            return (HelpTextItem) helpTextDao.findById(id);
        } catch (DomainObjectNotFoundException e) {
            return null;
        }
    }

    public DynamicAttribute findAttributeByRefLabel(String refLabel, String artefactType) {
        return dynamicAttributeDao.findAttributeByRefLabel(refLabel, artefactType);
    }

    public boolean checkUniqueness(Long daId, String value, Long nodeId) {
        return dynamicAttributeDao.checkUniqueness(daId, value, nodeId);
    }

    protected IFinder getFinderDao() {
        return this.dynamicAttributeDao;
    }

    protected IModifiable getModifierDao() {
        return this.dynamicAttributeDao;
    }

    /**
     * Get the label.
     *
     * @param domainObject the domain object to extract the label from
     * @return domain object label or empty string if domain object is null.
     */
    private String getLabel(final IDomainObject domainObject) {
        return domainObject != null ? domainObject.getLabel() : "";
    }


    /**
     * Find domain object based on id and dynamic attribute type.
     *
     * @param id - id of the entity
     * @param dynamicAttribute the attribute ownded by the node
     * @return IDomainObject with matching id or null (never throws Exceptions)
     */
    private IDomainObject findDomainObject(final String id, final DynamicAttribute dynamicAttribute) {
        final Class entityClass = dynamicAttribute.isNodeType() ? Node.class : dynamicAttribute.isLastUpdatedByType() ? User.class : null;
        return findDomainObject(id, entityClass);
    }

    /**
     * Find domain object.
     *
     * @param id the id of the entity
     * @param entityClass - the entity class type
     * @return IDomainObject with matching id or null (never throws Exceptions)
     */
    private IDomainObject findDomainObject(final String id, final Class entityClass) {

        IDomainObject domainObject = null;
        if (StringUtils.isNotBlank(id) && entityClass != null) {
            try {
                domainObject = getFinderDao().findById(entityClass, new Long(id));
            } catch (TalentStudioException e) {
                logger.debug("exception type: " + e.getClass().getName() + " -> info: " + e.getMessage(), e);
            }
        }

        return domainObject;
    }

    private IHelpTextDao helpTextDao;
    private IDynamicAttributeDao dynamicAttributeDao;
}

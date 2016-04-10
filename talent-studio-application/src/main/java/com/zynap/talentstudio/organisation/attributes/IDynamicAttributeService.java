/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation.attributes;

import com.zynap.domain.IDomainObject;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.IZynapService;
import com.zynap.talentstudio.help.HelpTextItem;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface IDynamicAttributeService extends IZynapService {

    /**
     * Get active attributes for the given node type.
     * <br/> If attributeTypes is not provided will return all.
     * <br/> If searchableOnly is true will only return active searchable dynamic attributes.
     *
     * @param nodeType
     * @param searchableOnly true or false
     * @param attributeTypes can be null
     * @return Collection of DynamicAttribute objects
     */
    Collection getActiveAttributes(String nodeType, boolean searchableOnly, String[] attributeTypes);

    Collection getActiveAttributes(String nodeType, boolean searchableOnly, String[] attributeTypes, boolean includeCalcFields);

    /**
     * Get active attributes for the given node types.
     * <br/> If attributeTypes is not provided will return all.
     * <br/> If searchableOnly is true will only return active searchable dynamic attributes.
     *
     * @param nodeTypes nodeTypes the attributes are relevant for
     * @param searchableOnly true or false
     * @param attributeTypes can be null
     * @return Collection of DynamicAttribute objects
     */
    Collection<DynamicAttributeDTO> listActiveAttributes(String[] nodeTypes, boolean searchableOnly, String[] attributeTypes);

    List<DynamicAttributeDTO> listAllAttributes(String nodeType);

    /**
     * Finds the attributes for the given node type (active or not.)
     *
     * @param nodeType one of {@link com.zynap.talentstudio.organisation.Node#POSITION_UNIT_TYPE_ }
     * or {@link com.zynap.talentstudio.organisation.Node#SUBJECT_UNIT_TYPE_}
     * @return Collection of DynamicAttribute objects
     */
    Collection<DynamicAttribute> getAllAttributes(String nodeType);

    /**
     * Finds the searchable active attributes for the given node type.
     *
     * @param nodeType one of {@link com.zynap.talentstudio.organisation.Node#POSITION_UNIT_TYPE_ }
     * or {@link com.zynap.talentstudio.organisation.Node#SUBJECT_UNIT_TYPE_}
     * @return Collection of DynamicAttribute objects
     */
    Collection<DynamicAttribute> getSearchableAttributes(String nodeType);

    /**
     * Finds the active attributes of a given attribute type for the given node type.
     *
     * @param nodeType one of {@link com.zynap.talentstudio.organisation.Node#POSITION_UNIT_TYPE_ }
     * or {@link com.zynap.talentstudio.organisation.Node#SUBJECT_UNIT_TYPE_}
     * @param attributeType type of attribute required, may be any listed as constants in the DynamicAttribute class
     * such as {@link DynamicAttribute#DA_TYPE_NUMBER}
     * @return Collection of DynamicAttribute objects
     */
    Collection getTypedAttributes(String nodeType, String attributeType);

    /**
     * Find active attributes for the given node type.
     *
     * @param nodeType one of {@link com.zynap.talentstudio.organisation.Node#POSITION_UNIT_TYPE_ }
     * or {@link com.zynap.talentstudio.organisation.Node#SUBJECT_UNIT_TYPE_}
     * @param includeCalculatedAttributes
     * @return Collection of DynamicAttribute objects
     */
    Collection<DynamicAttribute> getAllActiveAttributes(String nodeType, boolean includeCalculatedAttributes);

    /**
     * Check if attribute has associated values.
     *
     * @param id The attribute id
     * @return true or false
     */
    boolean usedByNode(Long id);

    /**
     * Find attribute value file by id.
     *
     * @param id
     * @return AttributeValueFile
     */
    AttributeValueFile findAttributeValueFile(Long id);

    /**
     * Delete the dynamic attribute.
     *
     * @param attributeId The attribute id
     * @throws TalentStudioException
     */
    void delete(Long attributeId) throws TalentStudioException;

    /**
     * Get object based on the value of the attribute value (which will be the id) and the dynamic attribute type.
     *
     * @param attributeValue
     * @return The IDomainObject or null
     */
    IDomainObject getDomainObject(AttributeValue attributeValue);

    /**
     * Get label for object based on the value of the attribute value and the dynamic attribute type.
     * <br/> Used to find labels for node type and updated by questions.
     *
     * @param attributeValue
     * @return The label or empty string
     */
    String getDomainObjectLabel(AttributeValue attributeValue);

    /**
     * Get label for object based on the value of the attribute value and the dynamic attribute type.
     * <br/> Used to find labels for node type and updated by questions.
     *
     * @param id
     * @param dynamicAttribute
     * @return The label or empty string
     */
    String getDomainObjectLabel(String id, DynamicAttribute dynamicAttribute);

    /**
     * Get node label.
     *
     * @param id
     * @return The label or empty string
     */
    String getNodeLabel(String id);

    /**
     * Get help text.
     *
     * @param id
     * @return HelpTextItem or null (does not throw {@link com.zynap.exception.DomainObjectNotFoundException})
     * @throws TalentStudioException
     */
    HelpTextItem findHelpTextItem(Long id) throws TalentStudioException;

    Collection<DynamicAttributeDTO> getSearchableAttributeDtos(String nodeType);
    DynamicAttribute findAttributeByRefLabel(String refLabel, String artefactType);

    boolean checkUniqueness(Long daId, String value, Long nodeId);

    Collection<DynamicAttribute> getAllAttributes(Long[] attributeIds);

    Map<String, String> getAllSubjectAttributes(Long subjectId);

    DynamicAttribute PP_SUB_DERIVED_ATT_DEFINITION = new DynamicAttribute(" Subordinate", "targetDerivedAttributes");
    DynamicAttribute PP_SUP_DERIVED_ATT_DEFINITION = new DynamicAttribute(" Superior", "sourceDerivedAttributes");
    DynamicAttribute SP_SUB_DERIVED_ATT_DEFINITION = new DynamicAttribute(" Person", "targetDerivedAttributes");
    DynamicAttribute SP_SUP_DERIVED_ATT_DEFINITION = new DynamicAttribute(" Position", "sourceDerivedAttributes");
    DynamicAttribute INVALID_ATT = new DynamicAttribute();
}

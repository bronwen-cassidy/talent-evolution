/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation.attributes;

import com.zynap.talentstudio.common.IFinder;
import com.zynap.talentstudio.common.IModifiable;

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
public interface IDynamicAttributeDao extends IFinder, IModifiable {

    Collection<DynamicAttribute> getActiveAttributes(String nodeType, boolean searchableOnly, String[] attributeTypes, boolean includeCalculated);

    List<DynamicAttributeDTO> listActiveAttributes(String[] nodeTypes, boolean searchableOnly, String[] attributeTypes);

    List<DynamicAttributeDTO> listAllAttributes(String nodeType);

    Collection getAllAttributes(String nodeType);

    Collection<DynamicAttribute> getSearchableAttributes(String nodeType);

    Collection<DynamicAttributeDTO> getSearchableAttributeDtos(String nodeType);

    Collection getTypedAttributes(String nodeType, String attributeType);

    Collection<DynamicAttribute> getAllActiveAttributes(String nodeType, boolean includeCalculatedAttributes);

    boolean usedByNode(Long id);

    AttributeValueFile findAttributeValueFile(Long id);

    DynamicAttribute findAttributeByRefLabel(String refLabel, String artefactType);

    boolean checkUniqueness(Long daId, String value, Long nodeId);

    Collection<DynamicAttribute> getAllAttributes(Long[] attributeIds);

    Map<String, String> findAllSubjectAnswers(Long subjectId);
}

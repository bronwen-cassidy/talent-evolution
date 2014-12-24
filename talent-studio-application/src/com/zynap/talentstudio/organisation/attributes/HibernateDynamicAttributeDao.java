/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation.attributes;

import com.zynap.domain.IDomainObject;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.HibernateCrudAdaptor;
import com.zynap.util.ArrayUtils;

import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class HibernateDynamicAttributeDao extends HibernateCrudAdaptor implements IDynamicAttributeDao {

    public Class getDomainObjectClass() {
        return DynamicAttribute.class;
    }

    public Collection<DynamicAttribute> getActiveAttributes(String nodeType, boolean searchableOnly, String[] attributeTypes, boolean includeCalculated) {

        // default parameters
        Object[] parameters = new Object[]{nodeType};

        // basic query
        StringBuffer query = new StringBuffer("select attribute from DynamicAttribute attribute where attribute.artefactType=? and attribute.active='T'");

        // append searchable condition to query
        if (searchableOnly) {
            query.append(" and attribute.searchable='T'");
        }

        if (!includeCalculated) {
            query.append(" and attribute.calculated = 'F'");
        }

        // append attribute type condition to query
        if (attributeTypes != null && attributeTypes.length > 0) {
            query.append(" and attribute.type in (");
            query.append(ArrayUtils.arrayToString(attributeTypes, ",", "'"));
            query.append(")");
        }

        // order by
        query.append(" order by upper(attribute.label)");

        return getHibernateTemplate().find(query.toString(), parameters);
    }


    public List<DynamicAttributeDTO> listActiveAttributes(String[] nodeTypes, boolean searchableOnly, String[] attributeTypes) {
        // basic query   type, String label, Long id, String artefactType, String description, String modifiedLabel
        StringBuffer query = new StringBuffer("select new ");
        query.append(DynamicAttributeDTO.class.getName()).append("( attribute.type,");
        query.append(" attribute.label, attribute.id, attribute.artefactType, attribute.description, attribute.externalRefLabel, attribute.refersToType )");
        query.append(" from ").append(DynamicAttribute.class.getName()).append(" attribute");
        query.append(" left join fetch attribute.refersToType ");
        query.append(" where attribute.active='T'");
        if (nodeTypes != null && nodeTypes.length > 0) {
            query.append(" and attribute.artefactType in ( ");
            query.append(ArrayUtils.arrayToString(nodeTypes, ",", "'"));
            query.append(")");
        }

        // append searchable condition to query
        if (searchableOnly) {
            query.append(" and attribute.searchable='T'");
        }

        // append attribute type condition to query
        if (attributeTypes != null && attributeTypes.length > 0) {
            query.append(" and attribute.type in (");
            query.append(ArrayUtils.arrayToString(attributeTypes, ",", "'"));
            query.append(")");
        }

        // order by
        query.append(" order by upper(attribute.label)");

        return getHibernateTemplate().find(query.toString());
    }

    public List<DynamicAttributeDTO> listAllAttributes(String nodeType) {
        StringBuffer query = new StringBuffer();
        query.append("select new ").append(DynamicAttributeDTO.class.getName()).append(" (");
        query.append(" attribute.type, attribute.label, attribute.id, attribute.artefactType, attribute.description, attribute.externalRefLabel,");
        query.append(" attribute.refersToType, attribute.calculated, attribute.active, attribute.mandatory, attribute.searchable");
        query.append(" )");
        query.append(" from DynamicAttribute attribute left join fetch attribute.refersToType where attribute.artefactType=? order by upper(attribute.label)");
        return getHibernateTemplate().find(query.toString(), nodeType);
    }

    public Collection getAllAttributes(String nodeType) {
        return getHibernateTemplate().find("from DynamicAttribute attribute where attribute.artefactType=? order by upper(attribute.label)", nodeType);
    }

    public Collection<DynamicAttribute> getAllAttributes(Long[] attributeIds) {
        return getHibernateTemplate().find("from DynamicAttribute attribute where attribute.id in (" + StringUtils.arrayToCommaDelimitedString(attributeIds) + ") order by upper(attribute.label)");
    }

    public Collection<DynamicAttribute> getSearchableAttributes(String nodeType) {
        String queryString = "select attribute from DynamicAttribute attribute " +
                "where attribute.active='T' " +
                "and attribute.searchable='T' and attribute.artefactType= :artefactType order by upper(attribute.label)";
        return getHibernateTemplate().findByNamedParam(queryString, "artefactType", nodeType);
    }

    // from DynamicAttribute attribute left join fetch attribute.refersToType
    //String type, String label, Long id, String artefactType, String description, String modifiedLabel, LookupType refersTo, boolean mandatory
    public Collection<DynamicAttributeDTO> getSearchableAttributeDtos(String nodeType) {
        StringBuffer query = new StringBuffer("select new ");
        query.append(DynamicAttributeDTO.class.getName())
                .append("(attribute.type, attribute.label, attribute.id, attribute.artefactType, attribute.description, attribute.externalRefLabel, attribute.refersToType, ")
                .append("attribute.mandatory) ")
                .append("from DynamicAttribute attribute left join fetch attribute.refersToType where attribute.active='T' and attribute.searchable='T' ")
                .append("and attribute.artefactType= :artefactType order by upper(attribute.label)");
        return getHibernateTemplate().findByNamedParam(query.toString(), "artefactType", nodeType);
    }

    public Collection getTypedAttributes(String nodeType, String attributeType) {
        String queryString = "select attribute from DynamicAttribute attribute " +
                "where attribute.active='T' " +
                "and attribute.type= :attributeType and attribute.artefactType= :artefactType order by upper(attribute.label)";
        return getHibernateTemplate().findByNamedParam(queryString, new String[]{"attributeType", "artefactType"}, new Object[]{attributeType, nodeType});
    }

    public Collection<DynamicAttribute> getAllActiveAttributes(String attributeType, boolean includeCalculatedAttributes) {
        if (includeCalculatedAttributes) {
            return getHibernateTemplate().find("from DynamicAttribute attribute where attribute.artefactType=? and attribute.active = 'T' order by upper(attribute.label)", attributeType);
        } else {
            return getHibernateTemplate().find("from DynamicAttribute attribute where attribute.artefactType=? and attribute.active = 'T' and attribute.calculated='F' order by upper(attribute.label)", attributeType);
        }
    }

    public List<DynamicAttribute> findQuestionnaireAttributes(Long questionnaireDefinitionId) {
        return getHibernateTemplate().find("from DynamicAttribute attribute where attribute.questionnaireDefinitionId=?", questionnaireDefinitionId);       
    }

    public boolean usedByNode(Long id) {
        String queryString = "select attribute from DynamicAttribute attribute,  " +
                "where attribute.id = :id " +
                "and exists ( select av.id from NodeExtendedAttribute av where attribute.id = av.dynamicAttribute.id )";
        Collection l = getHibernateTemplate().findByNamedParam(queryString, "id", id);
        return (l != null && l.size() > 0);
    }

    public AttributeValueFile findAttributeValueFile(Long id) {
        final NodeExtendedAttributeFile nodeExtendedAttributeFile = (NodeExtendedAttributeFile) getHibernateTemplate().load(NodeExtendedAttributeFile.class, id);
        return (AttributeValueFile) AttributeValue.create(nodeExtendedAttributeFile);
    }

    public DynamicAttribute findAttributeByRefLabel(String refLabel, String artefactType) {
        final List reuslts = getHibernateTemplate().find("from DynamicAttribute attribute where attribute.externalRefLabel='" + refLabel + "' and attribute.artefactType='" + artefactType + "'");
        if (reuslts.isEmpty()) return null;
        return (DynamicAttribute) reuslts.get(0);
    }

    public boolean checkUniqueness(Long daId, String value, Long nodeId) {
        StringBuffer sql = new StringBuffer("select count(*) from NodeExtendedAttribute attribute where attribute.dynamicAttribute.id=:attrId");
        sql.append(" and attribute.value='").append(value).append("'");
        // we are editing someone therefore count excluding that person
        Object[] paramValues = new Object[]{daId};
        String[] paramNames = new String[]{"attrId"};
        if (nodeId != null) {
            sql.append(" and attribute.node.id <> :nodeId");
            paramValues = new Object[]{daId, nodeId};
            paramNames = new String[]{"attrId", "nodeId"};

        }
        final List countVal = getHibernateTemplate().findByNamedParam(sql.toString(), paramNames, paramValues);
        return countVal.isEmpty() || ((Integer) countVal.get(0)).intValue() < 1;
    }

    public void delete(IDomainObject da) throws TalentStudioException {
        getHibernateTemplate().delete(" from NodeExtendedAttribute av where av.dynamicAttribute.id = " + da.getId());
        super.delete(da);
    }
}

package com.zynap.talentstudio.organisation;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.talentstudio.objectives.Objective;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.AttributeValuesCollection;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.organisation.portfolio.PortfolioItem;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.questionnaires.Questionnaire;
import com.zynap.talentstudio.security.areas.Area;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author Hibernate CodeGenerator
 */
public class Node extends ZynapDomainObject {

    public Node() {
        this.active = true;
    }

    public Node(Long id) {
        this();
        this.id = id;
    }

    public String getNodeType() {
        if (nodeType == null) {
            if (this instanceof OrganisationUnit)
                this.nodeType = ORG_UNIT_TYPE_;
            else if (this instanceof Position)
                this.nodeType = POSITION_UNIT_TYPE_;
            else if (this instanceof Subject)
                this.nodeType = SUBJECT_UNIT_TYPE_;
            else if (this instanceof Area)
                this.nodeType = AREA_UNIT_TYPE_;
            else if (this instanceof Questionnaire)
                this.nodeType = QUESTIONNAIRE_TYPE;
            else if (this instanceof Objective)
                this.nodeType = OBJECTIVE_TYPE;
        }
        return this.nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * Add new extended attributes.
     * <br/> Also clears dynamic attribute values.
     *
     * @param nodeExtendedAttributes Collection of {@link NodeExtendedAttribute}s.
     */
    public void addNodeExtendedAttributes(Collection nodeExtendedAttributes) {

        resetDynamicAttributeValues();

        if (nodeExtendedAttributes != null) {
            for (Iterator iterator = nodeExtendedAttributes.iterator(); iterator.hasNext();) {
                NodeExtendedAttribute nodeExtendedAttribute = (NodeExtendedAttribute) iterator.next();
                addNodeExtendedAttribute(nodeExtendedAttribute);
            }
        }
    }

    /**
     * Remove node extended attributes.
     * <br/> Also clears dynamic attribute values.
     *
     * @param nodeExtendedAttributes Collection of {@link NodeExtendedAttribute}s.
     */
    public void removeNodeExtendedAttributes(Collection nodeExtendedAttributes) {

        resetDynamicAttributeValues();

        if (nodeExtendedAttributes != null) {
            for (Iterator iterator = nodeExtendedAttributes.iterator(); iterator.hasNext();) {
                NodeExtendedAttribute nodeExtendedAttribute = (NodeExtendedAttribute) iterator.next();
                removeNodeExtendedAttribute(nodeExtendedAttribute);
            }
        }
    }

    /**
     * Remove node extended attribute.
     * <br/> Also clear dynamic attribute values collection.
     *
     * @param nodeExtendedAttribute The {@link NodeExtendedAttribute}
     */
    public void removeNodeExtendedAttribute(NodeExtendedAttribute nodeExtendedAttribute) {

        resetDynamicAttributeValues();

        if (nodeExtendedAttribute != null) {
            getExtendedAttributes().remove(nodeExtendedAttribute);
        }
    }

    /**
     * Add to extended attributes and set node.
     * <br/> Also clear dynamic attribute values collection.
     *
     * @param nodeExtendedAttribute The {@link NodeExtendedAttribute}
     */
    public void addNodeExtendedAttribute(NodeExtendedAttribute nodeExtendedAttribute) {

        resetDynamicAttributeValues();

        if (nodeExtendedAttribute != null) {
            nodeExtendedAttribute.setNode(this);
            this.getExtendedAttributes().add(nodeExtendedAttribute);
        }
    }

    /**
     * Get dynamic attribute values.
     *
     * @return AttributeValuesCollection
     */
    public AttributeValuesCollection getDynamicAttributeValues() {

        if (dynamicAttributeValues == null) {
            dynamicAttributeValues = new AttributeValuesCollection(this);
        }

        return dynamicAttributeValues;
    }

    /**
     * Remove dynamic attribute values - actually remove extended attributes.
     * <br/> Also clears existing dynamic attribute values.
     *
     * @param attributeValues Collection of {@link AttributeValue}s.
     */
    public void removeAttributeValues(Collection attributeValues) {

        resetDynamicAttributeValues();

        if (attributeValues != null) {
            for (Iterator iterator = attributeValues.iterator(); iterator.hasNext();) {
                AttributeValue attributeValue = (AttributeValue) iterator.next();
                removeAttributeValue(attributeValue);
            }
        }
    }

    /**
     * Add new dynamic attribute values - actually adds new extended attributes.
     * <br/> Also clears existing dynamic attribute values, but not existing extended attributes.
     *
     * @param attributeValues Collection of {@link AttributeValue}s.
     */
    public void addAttributeValues(Collection attributeValues) {

        resetDynamicAttributeValues();

        if (attributeValues != null) {
            for (Iterator iterator = attributeValues.iterator(); iterator.hasNext();) {
                AttributeValue attributeValue = (AttributeValue) iterator.next();
                addAttributeValue(attributeValue);
            }
        }
    }

    public void addOrUpdateAttributeValue(AttributeValue newAttributeValue) {

        resetDynamicAttributeValues();

        final boolean hasValue = newAttributeValue.hasValue();
        final AttributeValuesCollection currentAttributeValues = getDynamicAttributeValues();

        // iterate existing node attribute values and set new value if found.
        // If does not have value, remove it
        boolean found = false;
        final DynamicAttribute dynamicAttribute = newAttributeValue.getDynamicAttribute();
        for (Iterator currentAttributeValuesIterator = currentAttributeValues.getValues().iterator(); currentAttributeValuesIterator.hasNext();) {
            AttributeValue currentAttributeValue = (AttributeValue) currentAttributeValuesIterator.next();
            if (dynamicAttribute.equals(currentAttributeValue.getDynamicAttribute())) {
                if (hasValue) {
                    updateAttributeValue(newAttributeValue);
                } else {
                    removeAttributeValue(currentAttributeValue);
                }

                found = true;
                break;
            }
        }

        // if has value and not found is new so add it
        if (hasValue && !found) {
            addAttributeValue(newAttributeValue);
        }
    }

    /**
     * Clear old extended attributes for attributeValue and adds the new ones.
     *
     * @param newAttributeValue
     */
    public void updateAttributeValue(AttributeValue newAttributeValue) {

        resetDynamicAttributeValues();

        final DynamicAttribute dynamicAttribute = newAttributeValue.getDynamicAttribute();
        final AttributeValue currentAttributeValue = getDynamicAttributeValues().get(dynamicAttribute);

        if (dynamicAttribute.supportsMultipleAnswers()) {
            removeAttributeValue(currentAttributeValue);
            addAttributeValue(newAttributeValue);
        } else {
            final List newNodeExtendedAttributes = newAttributeValue.getNodeExtendedAttributes();
            final List currentNodeExtendedAttributes = currentAttributeValue.getNodeExtendedAttributes();
            final NodeExtendedAttribute newNodeExtendedAttribute = (NodeExtendedAttribute) newNodeExtendedAttributes.get(0);
            final NodeExtendedAttribute currentNodeExtendedAttribute = (NodeExtendedAttribute) currentNodeExtendedAttributes.get(0);
            currentNodeExtendedAttribute.copyValues(newNodeExtendedAttribute);
        }
    }

    /**
     * Add a dynamic attribute value - actually adds new extended attributes.
     * <br/> Also clears existing dynamic attribute values.
     *
     * @param attributeValue The AttributeValue
     */
    public void addAttributeValue(AttributeValue attributeValue) {

        resetDynamicAttributeValues();

        if (attributeValue != null) {
            final List nodeExtendedAttributes = attributeValue.getNodeExtendedAttributes();
            for (Object nodeExtendedAttribute1 : nodeExtendedAttributes) {
                NodeExtendedAttribute nodeExtendedAttribute = (NodeExtendedAttribute) nodeExtendedAttribute1;
                addNodeExtendedAttribute(nodeExtendedAttribute);
            }
        }
    }

    /**
     * Remove dynamic attribute value.
     * <br/> Also clears existing dynamic attribute values.
     *
     * @param attributeValue
     */
    public void removeAttributeValue(AttributeValue attributeValue) {

        resetDynamicAttributeValues();

        if (attributeValue != null) {
            getExtendedAttributes().removeAll(attributeValue.getNodeExtendedAttributes());
        }

    }

    public Set<PortfolioItem> getPortfolioItems() {
        if(portfolioItems == null) portfolioItems = new HashSet<PortfolioItem>();
        return portfolioItems;
    }

    public void setPortfolioItems(Set<PortfolioItem> portfolioItems) {
        this.portfolioItems = portfolioItems;
    }

    public void addPortfolioItem(PortfolioItem item) {
        item.setNode(this);
        getPortfolioItems().add(item);
    }

    public boolean isHasAccess() {
        return hasAccess;
    }

    public void setHasAccess(boolean hasAccess) {
        this.hasAccess = hasAccess;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("nodeType", getNodeType())
                .append("comments", getComments())
                .append("isActive", isActive())
                .toString();
    }

    public Map getSourceDerivedAttributes() {
        return sourceDerivedAttributes;
    }

    public void setSourceDerivedAttributes(Map sourceDerivedAttributes) {
        this.sourceDerivedAttributes = sourceDerivedAttributes;
    }

    public Map getTargetDerivedAttributes() {
        return targetDerivedAttributes;
    }

    public void setTargetDerivedAttributes(Map targetDerivedAttributes) {
        this.targetDerivedAttributes = targetDerivedAttributes;
    }

    public Set getAreaLinkedElements() {
        return areaLinkedElements;
    }

    public void setAreaLinkedElements(Set areaLinkedElements) {
        this.areaLinkedElements = areaLinkedElements;
    }

    public NodeAudit getNodeAudit() {
        return (getNodeAudits() == null || getNodeAudits().isEmpty() ? null : getNodeAudits().iterator().next());
    }

    public void setNodeAudit(NodeAudit nodeAudit) {
        this.nodeAudits = new HashSet<NodeAudit>();
        nodeAudits.add(nodeAudit);
    }

    public Collection<NodeAudit> getNodeAudits() {
        return nodeAudits;
    }

    public void setNodeAudits(Collection<NodeAudit> nodeAudits) {
        this.nodeAudits = nodeAudits;
    }

    /**
     *
     * @return Set of {@link com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute } objects
     */
    public Set<NodeExtendedAttribute> getExtendedAttributes() {
        return extendedAttributes;
    }

    public void setExtendedAttributes(Set<NodeExtendedAttribute> extendedAttributes) {
        this.extendedAttributes = extendedAttributes;
    }

    private void resetDynamicAttributeValues() {
        if (dynamicAttributeValues != null) dynamicAttributeValues = null;
    }

    public void removePortfolioItem(PortfolioItem item) {
        item.setNode(null);
        getPortfolioItems().remove(item);
    }


    public void initLazy() {
        getExtendedAttributes().size();
    }

    /**
     * persistent field.
     */
    private Collection<NodeAudit> nodeAudits;

    /**
     * persistent field.
     */
    private Set areaLinkedElements;

    /**
     * nullable persistent field
     */
    private String nodeType;

    /**
     * nullable persistent field
     */
    private String comments;

    /**
     * persistent field
     */
    private Set<NodeExtendedAttribute> extendedAttributes = new HashSet<NodeExtendedAttribute>();

    /**
     * persistent field
     */
    private Set<PortfolioItem> portfolioItems;

    /**
     * persistent field.
     */
    private Map targetDerivedAttributes;

    /**
     * persistent field.
     */
    private Map sourceDerivedAttributes;

    /**
     * non persistent field that indicates if user has access to the Node.
     *
     * @see com.zynap.talentstudio.security.ISecurityManager#checkAccess(com.zynap.talentstudio.security.permits.IPermit, com.zynap.domain.UserPrincipal, Node)
     */
    private boolean hasAccess;

    /**
     * non persistent field.
     */
    private transient AttributeValuesCollection dynamicAttributeValues;

    public static final String ORG_UNIT_TYPE_ = "O";
    public static final String AREA_UNIT_TYPE_ = "A";
    public static final String POSITION_UNIT_TYPE_ = "P";
    public static final String SUBJECT_UNIT_TYPE_ = "S";
    public static final String QUESTIONNAIRE_TYPE = "Q";
    public static final String OBJECTIVE_TYPE = "OBJ";
}

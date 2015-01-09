package com.zynap.talentstudio.organisation.positions;

import com.zynap.talentstudio.organisation.ArtefactAssociation;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.organisation.subjects.SubjectAssociation;
import com.zynap.talentstudio.organisation.subjects.SubjectPrimaryAssociation;
import com.zynap.talentstudio.organisation.subjects.SubjectSecondaryAssociation;
import com.zynap.domain.Auditable;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;


/**
 * @author Hibernate CodeGenerator
 */
public class Position extends Node implements Serializable, Auditable {

    /**
     * default constructor
     */
    public Position() {
    }


    public Position(Long id) {
        super(id);
    }

    public Position(OrganisationUnit organisationUnit) {
        this(null, null, organisationUnit);
    }

    public Position(Long id, OrganisationUnit organisationUnit) {
        this(id, null, organisationUnit);
    }

    public Position(Long id, String title) {
        this(id, title, null);
    }

    public Position(String title, OrganisationUnit organisationUnit) {
        this(null, title, organisationUnit);
    }

    public Position(Long id, String title, OrganisationUnit organisationUnit) {
        super(id);
        this.title = title;
        this.organisationUnit = organisationUnit;
    }

    /**
     * full constructor
     */
    public Position(Long nodeId, String title, OrganisationUnit organizationUnit, Position parent,
                    Set<SubjectAssociation> subjectAssociations, Set<Position> children,
                    Set<PositionAssociation> positionAssociationsBySourceId,
                    Set<PositionAssociation> positionAssociationsByTargetId) {

        setId(nodeId);
        this.title = title;
        this.organisationUnit = organizationUnit;
        this.parent = parent;
        this.subjectAssociations = subjectAssociations;
        this.children = children;
        this.targetAssociations = positionAssociationsBySourceId;
        this.sourceAssociations = positionAssociationsByTargetId;
    }


    public void addSubjectAssociation(SubjectAssociation subjectAssociation) {
        subjectAssociation.setTarget(this);
        getSubjectAssociations().add(subjectAssociation);
    }

    public void removeSubjectAssociation(SubjectAssociation subjectAssociation) {
        getSubjectAssociations().remove(subjectAssociation);
    }

    public void addSourceAssociation(PositionAssociation positionAssociation) {
        positionAssociation.setSource(this);
        positionAssociation.setSourceId(getId());
        getSourceAssociations().add(positionAssociation);
    }

    /**
     * Is this the default position - returns true if the position has an id but has no parent.
     *
     * @return True if this is the default position
     */
    public boolean isDefault() {
        return id != null && getParent() == null;
    }

    /**
     * @hibernate.property column="TITLE"
     * length="200"
     * not-null="true"
     */
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.label = title;
    }

    /**
     * @hibernate.many-to-one not-null="true"
     * @hibernate.column name="ORG_UNIT_ID"
     */
    public OrganisationUnit getOrganisationUnit() {
        return this.organisationUnit;
    }

    public void setOrganisationUnit(OrganisationUnit organisationUnit) {
        this.organisationUnit = organisationUnit;
    }

    /**
     * @hibernate.many-to-one not-null="true"
     * @hibernate.column name="PARENT_ID"
     */
    public Position getParent() {
        return this.parent;
    }

    public void setParent(Position parent) {
        this.parent = parent;
    }

    /**
     * @hibernate.set lazy="true"
     * inverse="true"
     * cascade="none"
     * @hibernate.collection-key column="POSITION_ID"
     * @hibernate.collection-one-to-many class="com.zynap.domain.SubjectAssociation"
     */
    public Set<SubjectAssociation> getSubjectAssociations() {
        if (subjectAssociations == null) subjectAssociations = new HashSet<SubjectAssociation>();
        return this.subjectAssociations;
    }

    public void setSubjectAssociations(Set<SubjectAssociation> subjectAssociations) {
        this.subjectAssociations = subjectAssociations;
    }

    /**
     * @hibernate.set lazy="true"
     * inverse="true"
     * cascade="none"
     * @hibernate.collection-key column="PARENT_ID"
     * @hibernate.collection-one-to-many class="com.zynap.domain.Position"
     */
    public Set<Position> getChildren() {
        if (children == null) children = new HashSet<Position>();
        return this.children;
    }

    public void setChildren(Set<Position> children) {
        this.children = children;
    }

    /**
     * @hibernate.set lazy="true"
     * inverse="true"
     * cascade="none"
     * @hibernate.collection-key column="SOURCE_ID"
     * @hibernate.collection-one-to-many class="com.zynap.domain.PositionAssociation"
     */
    public Collection<PositionAssociation> getTargetAssociations() {
        if (targetAssociations == null) targetAssociations = new HashSet<PositionAssociation>();
        return this.targetAssociations;
    }

    public void setTargetAssociations(Set<PositionAssociation> targetAssociations) {
        this.targetAssociations = targetAssociations;
    }

    /**
     * @hibernate.set lazy="true"
     * inverse="true"
     * cascade="none"
     * @hibernate.collection-key column="TARGET_ID"
     * @hibernate.collection-one-to-many class="com.zynap.domain.PositionAssociation"
     */
    public Set<PositionAssociation> getSourceAssociations() {
        if (this.sourceAssociations == null) this.sourceAssociations = new HashSet<PositionAssociation>();
        return this.sourceAssociations;
    }

    /**
     * Clear the list of current subject associations and add the ones in the collection passed in to the underlying collection.
     * Only works for positions other than the default.
     *
     * @param newSourceAssociations Collection of {@link PositionAssociation}s
     */
    public void assignNewSourceAssociations(Collection<ArtefactAssociation> newSourceAssociations) {

        if (!isDefault()) {
            getSourceAssociations().clear();
            if (newSourceAssociations != null) {
                for (Iterator iterator = newSourceAssociations.iterator(); iterator.hasNext();) {
                    PositionAssociation positionAssociation = (PositionAssociation) iterator.next();
                    addSourceAssociation(positionAssociation);
                }
            }
        }
    }

    public void assignNewSubjectAssociations(Set<ArtefactAssociation> newSubjectAssociations) {
        getSubjectAssociations().clear();
        if (newSubjectAssociations != null) {
            for (Iterator iterator = newSubjectAssociations.iterator(); iterator.hasNext();) {
                SubjectAssociation subjectAssociation = (SubjectAssociation) iterator.next();
                addSubjectAssociation(subjectAssociation);
            }
        }
    }

    public void setSourceAssociations(Set<PositionAssociation> sourceAssociations) {
        this.sourceAssociations = sourceAssociations;
    }

    public String getLabel() {
        return getTitle();
    }

    public Collection<SubjectPrimaryAssociation> getSubjectPrimaryAssociations() {
        return subjectPrimaryAssociations;
    }

    public String getCurrentHolderInfo() {
        return currentHolderInfo;
    }

    public void setCurrentHolderInfo(String currentHolderInfo) {
        this.currentHolderInfo = currentHolderInfo;
    }

    public String getOrganisationUnitLabel() {
        return organisationUnit != null ? organisationUnit.getLabel() : null;
    }

    public void setSubjectPrimaryAssociations(Collection<SubjectPrimaryAssociation> subjectPrimaryAssociations) {
        this.subjectPrimaryAssociations = subjectPrimaryAssociations;
    }

    public Collection<SubjectSecondaryAssociation> getSubjectSecondaryAssociations() {
        return subjectSecondaryAssociations;
    }

    public void setSubjectSecondaryAssociations(Collection<SubjectSecondaryAssociation> subjectSecondaryAssociations) {
        this.subjectSecondaryAssociations = subjectSecondaryAssociations;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("title", getTitle())
                .append("active", isActive())
                .append("currentHolderInfo", getCurrentHolderInfo())
                .toString();
    }

    public void removeChild(Position position) {
        children.remove(position);
        // remove from source associations also
        removeSourceAssociation(position);
    }

    private void removeSourceAssociation(Position position) {
        for (Iterator<PositionAssociation> iterator = targetAssociations.iterator(); iterator.hasNext();) {
            PositionAssociation positionAssociation = iterator.next();
            if (positionAssociation.getSourceId().equals(position.getId())) {
                iterator.remove();
            }
        }
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<Subject> getCurrentHolders() {
        List<Subject> subjects = new ArrayList<Subject>();
        for (SubjectPrimaryAssociation subjectPrimaryAssociation : subjectPrimaryAssociations) {
            subjects.add(subjectPrimaryAssociation.getSubject());
        }
        return subjects;
    }

    public Position createAuditable() {
        OrganisationUnit ou = new OrganisationUnit(organisationUnit.getId(), organisationUnit.getLabel());
        Position p = new Position(id, title, ou);

        // subject associations, source associations, target associations
        Set<SubjectAssociation> subjectAssocs = new HashSet<SubjectAssociation>();
        for (SubjectAssociation subjectAssociation : getSubjectAssociations()) {
            subjectAssocs.add(subjectAssociation.createAuditable());
        }
        p.setSubjectAssociations(subjectAssocs);

        Set<PositionAssociation> sourceAssocs = new HashSet<PositionAssociation>();
        for(PositionAssociation assoc : getSourceAssociations()) {
            sourceAssocs.add(assoc.createAuditable());    
        }
        p.setSourceAssociations(sourceAssocs);

        Set<PositionAssociation> targetAssocs = new HashSet<PositionAssociation>();
        for(PositionAssociation assoc : getTargetAssociations()) {
            targetAssocs.add(assoc.createAuditable());
        }
        p.setTargetAssociations(targetAssocs);        

        Set<NodeExtendedAttribute> auditAttrs = new HashSet<NodeExtendedAttribute>();
        for (NodeExtendedAttribute attr : getExtendedAttributes()) {
            auditAttrs.add(attr.createAuditable());
        }
        p.setExtendedAttributes(auditAttrs);

        return p;
    }

    /**
     * persistent field.
     */
    private String title;

    private String currentHolderInfo;

    /**
     * persistent field.
     */
    private OrganisationUnit organisationUnit;

    /**
     * read only persistent field.
     */
    private Position parent;

    private Long parentId;

    /**
     * persistent field.
     */
    private Set<SubjectAssociation> subjectAssociations;

    /**
     * persistent field.
     */
    private Set<Position> children;

    /**
     * persistent field.
     */
    private Collection<PositionAssociation> targetAssociations;

    /**
     * persistent field.
     */
    private Set<PositionAssociation> sourceAssociations;

    /**
     * persistent field.
     */
    private Collection<SubjectPrimaryAssociation> subjectPrimaryAssociations = new HashSet<SubjectPrimaryAssociation>();

    /**
     * persistent field.
     */
    private Collection<SubjectSecondaryAssociation> subjectSecondaryAssociations = new HashSet<SubjectSecondaryAssociation>();
}

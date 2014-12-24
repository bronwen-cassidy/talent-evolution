package com.zynap.talentstudio.organisation;

import com.zynap.talentstudio.objectives.ObjectiveSet;
import com.zynap.talentstudio.objectives.ObjectiveUtils;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.domain.Auditable;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * Represents a single OrganisationUnit.
 *
 * @author bcassidy
 */
public class OrganisationUnit extends Node implements Auditable {

    /**
     * Default constructor.
     */
    public OrganisationUnit() {
    }

    /**
     * Minimal constructor.
     *
     * @param id
     */
    public OrganisationUnit(Long id) {
        this(id, null);
    }

    /**
     * Full constructor.
     *
     * @param id
     * @param label
     */
    public OrganisationUnit(Long id, String label) {
        setId(id);
        setLabel(label);
    }

    public OrganisationUnit getParent() {
        return this.parent;
    }

    public void setParent(OrganisationUnit parent) {
        this.parent = parent;
    }

    public Set<Position> getPositions() {
        if (positions == null) return new LinkedHashSet<Position>();
        return this.positions;
    }

    public void setPositions(Set<Position> positions) {
        this.positions = positions;
    }

    public Set<OrganisationUnit> getChildren() {
        if (this.children == null) children = new LinkedHashSet<OrganisationUnit>();
        return this.children;
    }

    public void setChildren(Set<OrganisationUnit> children) {
        this.children = children;
    }

    public void addChild(OrganisationUnit organisationUnit) {
        organisationUnit.setParent(this);
        if (children == null) children = new HashSet<OrganisationUnit>();
        children.add(organisationUnit);
    }

    public Collection<ObjectiveSet> getObjectiveSets() {
        return objectiveSets;
    }

    public void setObjectiveSets(Collection<ObjectiveSet> objectiveSets) {
        this.objectiveSets = objectiveSets;
    }

    public ObjectiveSet getCurrentObjectiveSet() {
        return ObjectiveUtils.findCurrentObjectiveSet(objectiveSets);
    }

    /**
     * Is this the default org unit - returns true if the org unit has an id but has no parent.
     *
     * @return True if this is the default org unit
     */
    public boolean isDefault() {
        return id != null && getParent() == null;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("label", getLabel())
                .append("active", isActive())
                .toString();
    }

    public Collection getApprovedObjectives() {

        ObjectiveSet objectiveSet = getCurrentObjectiveSet();
        if (objectiveSet != null && objectiveSet.isApproved()) {
            return objectiveSet.getObjectives();
        }
        return new ArrayList();
    }

    public void removePosition(Position position) {
        positions.remove(position);
    }

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    public void removeChild(OrganisationUnit organisationUnit) {
        children.remove(organisationUnit);
    }

    public OrganisationUnit createAuditable() {
        return new OrganisationUnit(id, label);
    }

    public static final Long ROOT_ORG_UNIT_ID = (long) 0;

    public void setCompanyRoot(boolean companyRoot) {
        this.companyRoot = companyRoot;
    }

    public boolean isCompanyRoot() {
        return companyRoot;
    }

    private Long rootId;
    private OrganisationUnit parent;
    private Set<Position> positions;
    private Set<OrganisationUnit> children = new LinkedHashSet<OrganisationUnit>();
    private Collection<ObjectiveSet> objectiveSets = new LinkedHashSet<ObjectiveSet>();
    private boolean companyRoot;
}

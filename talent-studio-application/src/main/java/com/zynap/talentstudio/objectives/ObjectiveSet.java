package com.zynap.talentstudio.objectives;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.subjects.Subject;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.*;


/**
 * @author Hibernate CodeGenerator
 */
public class ObjectiveSet extends ZynapDomainObject {

    /**
     * default constructor
     */
    public ObjectiveSet() {
    }

    /**
     * @param subject    the subject which this objective set belongs to.
     * @param objectives the list of objectives for this set
     */
    public ObjectiveSet(Subject subject, List<Objective> objectives) {
        this.subject = subject;
        this.objectives = objectives;
    }

    /**
     * Clear the list of current objectives and add the ones in the collection passed in to the underlying collection.
     *
     * @param newObjectives Collection of {@link Objective}s
     */
    public void assignNewObjectives(Collection<Objective> newObjectives) {
        objectives.clear();
        if (newObjectives != null) {
            for (Objective newObjective : newObjectives) {
                addObjective(newObjective);
            }
        }
    }

    public void addObjective(Objective newObjective) {
        newObjective.setObjectiveSet(this);
        this.objectives.add(newObjective);
    }

    public void initLazy() {
        objectives.size();
    }

    public List<Objective> getInvalidObjectives(List<OrganisationUnit> organisationUnits) {
        List<Objective> invalidObjectives = new ArrayList<Objective>();
        for (Objective objective : getObjectives()) {
            OrganisationUnit orgunit = objective.getParent().getObjectiveSet().getOrganisationUnit();
            if(!organisationUnits.contains(orgunit)) {
                // we have an invalid objective
                invalidObjectives.add(objective);
            }
        }
        return invalidObjectives;
    }

    /**
     * @return The type of the Objective set, this may be any of CORPORATE, BUSINESS_UNIT, OR USER.
     */
    public String getType() {
        return type;
    }

    /**
     * If the type is USER the subject is guarenteed to be not null, if BUSINESS_UNIT the organisationUnit will be not null.
     * otherwise both are null if the type is corporate.
     *
     * @param type The type of the Objective set, this may be any of CORPORATE, BUSINESS_UNIT, OR USER.
     */
    public void setType(String type) {
        this.type = type;
    }

    public Subject getSubject() {
        return this.subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    /**
     * This is non-null only if the objective set belongs to an organisation unit
     *
     * @return The organisation unit that owns these objectives or null if these are personal objectives
     */
    public OrganisationUnit getOrganisationUnit() {
        return organisationUnit;
    }

    public void setOrganisationUnit(OrganisationUnit organisationUnit) {
        this.organisationUnit = organisationUnit;
    }

    public ObjectiveSet getCorporateObjectiveSet() {
        return corporateObjectiveSet;
    }

    public void setCorporateObjectiveSet(ObjectiveSet corporateObjectiveSet) {
        this.corporateObjectiveSet = corporateObjectiveSet;
    }

    public List<Objective> getObjectives() {
        return this.objectives;
    }

    public void setObjectives(List<Objective> objectives) {
        this.objectives = objectives;
    }

    public Objective findObjective(Long objectiveId) {
        for (Objective objective : objectives) {
            Long currentId = objective.getId();
            if (currentId != null && currentId.equals(objectiveId)) {
                return objective;
            }
        }
        return null;
    }

    public ObjectiveDefinition getObjectiveDefinition() {
        return objectiveDefinition;
    }

    public void setObjectiveDefinition(ObjectiveDefinition objectiveDefinition) {
        this.objectiveDefinition = objectiveDefinition;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("label", getLabel())
                .toString();
    }

    public boolean isCorporate() {
        return ObjectiveConstants.CORPORATE_TYPE.equals(type);
    }

    public boolean isPublished() {
        return ObjectiveConstants.STATUS_PUBLISHED.equals(status);
    }

    public boolean isArchived() {
        return isExpired();
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void expire() {
        for (Objective objective : objectives) {
            objective.setStatus(ObjectiveConstants.STATUS_ARCHIVED);
        }
    }

    public boolean isHasObjectives() {
        return objectives != null && !objectives.isEmpty();
    }

    public boolean isExpired() {
        return ObjectiveConstants.STATUS_ARCHIVED.equals(status);
    }

    public boolean isOpenOrPending() {
        return ObjectiveConstants.STATUS_OPEN.equals(status) || ObjectiveConstants.STATUS_PENDING.equals(status);
    }

    public boolean isOpen() {
        return ObjectiveConstants.STATUS_OPEN.equals(status);
    }

    public boolean isPending() {
        return ObjectiveConstants.STATUS_PENDING.equals(status);
    }

    public boolean isManagerPending() {
        return isPending() && ObjectiveConstants.ACTION_GROUP_MANAGER.equals(actionGroup);
    }

    public Long getSubjectId() {
        return subject != null ? subject.getId() : null;
    }

    public boolean isIndividualPending() {
        return isPending() && ObjectiveConstants.ACTION_GROUP_INDIVIDUAL.equals(actionGroup);
    }

    public boolean isComplete() {
        return ObjectiveConstants.STATUS_COMPLETE.equals(status);
    }

    public String getActionGroup() {
        return actionGroup;
    }

    public void setActionGroup(String actionGroup) {
        this.actionGroup = actionGroup;
    }

    public String getActionRequired() {
        return actionRequired;
    }

    public void setActionRequired(String awaitingAction) {
        this.actionRequired = awaitingAction;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public boolean isApprovedOrComplete() {
        return isApproved() || isComplete();
    }


    public boolean isAppearInTodo() {
        return appearInTodo;
    }

    public void setAppearInTodo(boolean appearInTodo) {
        this.appearInTodo = appearInTodo;
    }

    private Subject subject;
    private OrganisationUnit organisationUnit;
    private ObjectiveDefinition objectiveDefinition;
    private ObjectiveSet corporateObjectiveSet;

    private List<Objective> objectives = new ArrayList<Objective>();

    private String status = ObjectiveConstants.STATUS_OPEN;
    private Date publishedDate;
    private Date expiryDate;
    private Date lastModifiedDate;
    private String actionRequired = ObjectiveConstants.ACTION_REQUIRED_NOACTION;
    private String actionGroup = ObjectiveConstants.ACTION_GROUP_UNASSIGNED;
    private String type;
    private boolean approved;
    /**
     * Determines whether this objective should appear in the to_do list
     */
    private boolean appearInTodo;
}

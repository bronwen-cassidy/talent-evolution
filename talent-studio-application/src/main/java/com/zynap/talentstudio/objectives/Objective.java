package com.zynap.talentstudio.objectives;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.organisation.Node;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.*;


/**
 * @author Hibernate CodeGenerator
 */
public final class Objective extends Node {

    /**
     * default constructor
     */
    public Objective() {
        super();
    }

    public Objective(String label, ObjectiveSet objectiveSet, Date dateCreated, Long createdById, String status) {
        super();
        this.label = label;
        this.objectiveSet = objectiveSet;
        this.dateCreated = dateCreated;
        this.createdById = createdById;
        this.status = status;
    }

    public Objective(Long objectiveId) {
        super(objectiveId);
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateCreated() {
        return this.dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateUpdated() {
        return this.dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public ObjectiveSet getObjectiveSet() {
        return this.objectiveSet;
    }

    public void setObjectiveSet(ObjectiveSet objectiveSet) {
        this.objectiveSet = objectiveSet;
    }

    public Date getDateApproved() {
        return dateApproved;
    }

    public void setDateApproved(Date dateApproved) {
        this.dateApproved = dateApproved;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedById() {
        return updatedById;
    }

    public void setUpdatedById(Long updatedById) {
        this.updatedById = updatedById;
    }

    public User getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(User updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Objective getParent() {
        return parent;
    }

    public void setParent(Objective parent) {
        this.parent = parent;
    }

    public Set<ObjectiveAssessment> getAssessments() {
        return assessments;
    }

    public void setAssessments(Set<ObjectiveAssessment> assessments) {
        this.assessments = assessments;
    }

    /**
     * Gets the assessments completed by other users
     *
     * @param userId the user who completed the assessment
     * @return list of assessments completed by the given user
     */
    public List<ObjectiveAssessment> getUserAssessments(final Long userId) {
        //noinspection unchecked
        return new ArrayList<ObjectiveAssessment>(CollectionUtils.select(assessments, new Predicate() {
            public boolean evaluate(Object object) {
                ObjectiveAssessment assessment = (ObjectiveAssessment) object;
                User user = assessment.getAssessor();
                return user != null && user.getId().equals(userId);
            }
        }));
    }

    /**
     * Gets the manager/self assessment only.
     *
     * @return null if the set is empty otherwise just the first one
     */
    public ObjectiveAssessment getManagerAssessment() {
        for (ObjectiveAssessment assessment : assessments) {
            if (assessment.getAssessor() == null) {
                return assessment;
            }
        }
        return null;
    }

    public void addAssessment(ObjectiveAssessment assessment) {
        assessment.setObjective(this);
        assessments.add(assessment);
    }

    public boolean isApproved() {
        return ObjectiveConstants.STATUS_APPROVED.equals(status);
    }

    public boolean isApprovedOrComplete() {
        return ObjectiveConstants.STATUS_APPROVED.equals(status) || ObjectiveConstants.STATUS_COMPLETE.equals(status);
    }

    public boolean isComplete() {
        return ObjectiveConstants.STATUS_COMPLETE.equals(status);
    }

    /**
     * The users assigned to assess this objective.
     *
     * @return users
     */
    public Set<User> getAssessors() {
        return assessors;
    }

    public boolean isAppearInTodo() {
        return appearInTodo;
    }

    public void setAssessors(Set<User> assessors) {
        this.assessors = assessors;
    }

    public void setAppearInTodo(boolean appearInTodo) {
        this.appearInTodo = appearInTodo;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("label", getLabel())
                .append("status", getStatus())
                .append("description", getDescription())
                .append("dateCreated", getDateCreated())
                .append("dateUpdated", getDateUpdated())
                .toString();
    }

    public boolean hasApprovableAssessments() {

        for (ObjectiveAssessment assessment : assessments) {
            if (!assessment.isApproved() && isApproved()) return true;
        }
        return assessments.isEmpty() && isApproved();
    }

    public void addAssessor(User user) {
        assessors.add(user);
    }

    public void removeAssessment(ObjectiveAssessment objectiveAssessment) {
        assessments.remove(objectiveAssessment);
    }

    public ObjectiveAssessment findUserAssessment(Long id) {
        for (ObjectiveAssessment assessment : assessments) {
            if (assessment.getAssessor() != null && assessment.getAssessor().getId().equals(id)) {
                return assessment;
            }
        }
        return null;
    }

    /**
     * Determines whether this objective should appear in the to_do list
     */
    private boolean appearInTodo;
    private String description;
    private Date dateCreated;
    private Date dateUpdated;
    private Date dateApproved;
    private ObjectiveSet objectiveSet;

    /**
     * The objective this objective is based on
     */
    private Objective parent;

    private Set<ObjectiveAssessment> assessments = new HashSet<ObjectiveAssessment>();

    private Set<User> assessors = new HashSet<User>();
    private Long updatedById;
    private User updatedBy;
    private Long createdById;
    private User createdBy;

    /**
     * One of OPEN (not yet been approved by a manager) or APPROVED (manager has approved
     * and is no longer open to modification)
     */
    private String status = ObjectiveConstants.STATUS_OPEN;

}

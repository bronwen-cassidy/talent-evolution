/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.objectives;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.domain.admin.User;

import com.zynap.talentstudio.common.lookups.LookupValue;

import java.util.Date;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 28-Mar-2007 12:26:48
 */
public class ObjectiveAssessment extends ZynapDomainObject {


    /**
     * Constructor required by hibernate
     */
    public ObjectiveAssessment() {
    }

    /**
     * Real constructor. Cannot create an objective assessment without the objective.
     * @param objective The objective this class is an assessment for.
     */
    public ObjectiveAssessment(Objective objective) {
        this.objective = objective;
    }

    public Objective getObjective() {
        return objective;
    }

    public void setObjective(Objective objective) {
        this.objective = objective;
    }

    public LookupValue getSelfRating() {
        return selfRating;
    }

    public void setSelfRating(LookupValue selfRating) {
        this.selfRating = selfRating;
    }

    public LookupValue getManagerRating() {
        return managerRating;
    }

    public void setManagerRating(LookupValue managerRating) {
        this.managerRating = managerRating;
    }

    public String getManagerComment() {
        return managerComment;
    }

    public void setManagerComment(String managerComment) {
        this.managerComment = managerComment;
    }

    public String getSelfComment() {
        return selfComment;
    }

    public void setSelfComment(String selfComment) {
        this.selfComment = selfComment;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public Date getDateApproved() {
        return dateApproved;
    }

    public void setDateApproved(Date dateApproved) {
        this.dateApproved = dateApproved;
    }

    public String getLabel() {
        return objective.getLabel();
    }

    public User getAssessor() {
        return assessor;
    }

    public void setAssessor(User assessor) {
        this.assessor = assessor;
    }

    private Objective objective;
    private LookupValue selfRating;
    private LookupValue managerRating;
    private Date dateApproved;
    private User assessor;

    private String managerComment;
    private String selfComment;
    private boolean approved;
}

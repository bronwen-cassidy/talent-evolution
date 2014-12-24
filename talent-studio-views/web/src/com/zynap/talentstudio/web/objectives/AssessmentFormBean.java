/*
 * $Header: ${}
 * $Revision: ${}
 * $Date: 28-Mar-2007
 *
 * Copyright (c) 1999-2006 Bronwen Cassidy.  All rights reserved.
 */
package com.zynap.talentstudio.web.objectives;

import com.zynap.talentstudio.common.lookups.LookupType;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.objectives.ObjectiveAssessment;
import com.zynap.talentstudio.objectives.Objective;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.domain.admin.User;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 28-Mar-2007 19:09:49
 */
public class AssessmentFormBean implements Serializable {

    public AssessmentFormBean(ObjectiveAssessment objectiveAssessment) {
        if (objectiveAssessment == null) {
            this.objectiveAssessment = new ObjectiveAssessment();
        } else {
            this.objectiveAssessment = objectiveAssessment;
            this.user = objectiveAssessment.getAssessor();
            this.objective = objectiveAssessment.getObjective();
        }
    }

    public AssessmentFormBean(ObjectiveAssessment objectiveAssessment, LookupType rating) {
        this(objectiveAssessment);
        this.rating = rating;
    }

    public AssessmentFormBean(ObjectiveAssessment assessment, LookupType rating, User user, Subject subject) {
        this(assessment, rating);
        this.user = user;
        this.subject = subject;
    }

    public ObjectiveAssessment getModifiedAssessment() {
        objectiveAssessment.setAssessor(user);        
        return objectiveAssessment;
    }

    public String getManagerRatingValue() {
        LookupValue value = getManagerRatingLookupValue();
        return value != null ? value.getLabel() : null;
    }

    public String getSelfRatingValue() {
        LookupValue value = getSelfRatingLookupValue();
        return value != null ? value.getLabel() : null;
    }

    public void setManagerRating(Long lookupId) {
        objectiveAssessment.setManagerRating(findLookup(lookupId));
    }

    public Long getManagerRating() {
        LookupValue value = getManagerRatingLookupValue();
        return value != null ? value.getId() : null;
    }

    public void setSelfRating(Long lookupValueId) {
        objectiveAssessment.setSelfRating(findLookup(lookupValueId));
    }

    public Long getSelfRating() {
        LookupValue value = getSelfRatingLookupValue();
        return value != null ? value.getId() : null;
    }

    public LookupType getRating() {
        return rating;
    }

    public void setRating(LookupType rating) {
        this.rating = rating;
    }

    public String getManagerComment() {
        return objectiveAssessment != null ? objectiveAssessment.getManagerComment() : null;
    }

    public void setManagerComment(String value) {
        objectiveAssessment.setManagerComment(value);
    }

    public void setSelfComment(String value) {
        objectiveAssessment.setSelfComment(value);
    }

    public String getSelfComment() {
        return objectiveAssessment != null ? objectiveAssessment.getSelfComment() : null;
    }

    public User getUser() {
        return user;
    }

    private LookupValue findLookup(Long lookupValueId) {
        Collection lookupValues = rating.getLookupValues();
        for (Iterator iterator = lookupValues.iterator(); iterator.hasNext();) {
            LookupValue lookupValue = (LookupValue) iterator.next();
            if (lookupValue.getId().equals(lookupValueId)) return lookupValue;
        }
        return null;
    }

    public boolean isApproved() {
        return objectiveAssessment != null && objectiveAssessment.isApproved();
    }

    /**
     * Once an assessment has been approved you cannot un-approve it.
     *
     * @param approved true to approve the objectives.
     */
    public void setApproved(boolean approved) {
        if (!objectiveAssessment.isApproved()) objectiveAssessment.setApproved(approved);
    }

    public Collection getRatingValues() {
        return rating.getActiveLookupValues();
    }

    public Long getId() {
        return objectiveAssessment.getId();
    }

    public String getComment() {
        return objectiveAssessment.getSelfComment();
    }

    public LookupValue getRatingValue() {
        return objectiveAssessment.getSelfRating();
    }

    private LookupValue getSelfRatingLookupValue() {
        return objectiveAssessment != null ? objectiveAssessment.getSelfRating() : null;
    }

    private LookupValue getManagerRatingLookupValue() {
        return objectiveAssessment != null ? objectiveAssessment.getManagerRating() : null;
    }

    public boolean isManagerAssessment() {
        return objectiveAssessment.getAssessor() == null;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setObjective(Objective objective) {
        this.objective = objective;
    }

    public Objective getObjective() {
        return objective;
    }

    private ObjectiveAssessment objectiveAssessment;
    private LookupType rating;
    private User user;
    private Subject subject;
    private Objective objective;
}

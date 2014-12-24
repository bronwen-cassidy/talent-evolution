/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.objectives;

import com.zynap.talentstudio.objectives.Objective;
import com.zynap.talentstudio.objectives.ObjectiveAssessment;
import com.zynap.talentstudio.organisation.subjects.Subject;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 09-Aug-2007 14:43:36
 */
public class AssessorFormBean implements Serializable, Comparable<AssessorFormBean> {

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Objective getObjective() {
        return objective;
    }

    public void setObjective(Objective objective) {
        this.objective = objective;
    }

    public int compareTo(AssessorFormBean o) {
        return subject.getSecondName().compareTo(o.subject.getSecondName());
    }

    public Objective getModifiedObjective() {
        return objective;
    }

    public void setComment(String value) {
        assessment.setSelfComment(value);
    }

    public String getComment() {
        return assessment.getSelfComment();
    }

    public ObjectiveAssessment getAssessment() {
        return assessment;
    }

    public void setAssessment(ObjectiveAssessment assessment) {
        this.assessment = assessment;
    }

    private Subject subject;
    private Objective objective;
    private ObjectiveAssessment assessment;
}

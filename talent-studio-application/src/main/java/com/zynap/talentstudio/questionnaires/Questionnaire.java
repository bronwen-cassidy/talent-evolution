package com.zynap.talentstudio.questionnaires;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.subjects.Subject;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;


/**
 * @author Hibernate CodeGenerator
 */
public class Questionnaire extends Node {

    /**
     * default constructor
     */
    public Questionnaire() {
        super();
    }

    public Questionnaire(Long id, String label) {
        super(id);
        setLabel(label);
    }

    /**
     * minimal constructor
     */
    public Questionnaire(Long nodeId, QuestionnaireWorkflow questionnaireWorkflow, User user) {
        super(nodeId);
        this.questionnaireWorkflow = questionnaireWorkflow;
        this.questionnaireWorkflowId = questionnaireWorkflow.getId();
        this.user = user;
    }

    public Long getQuestionnaireWorkflowId() {
        return questionnaireWorkflowId;
    }

    public void setQuestionnaireWorkflowId(Long questionnaireWorkflowId) {
        this.questionnaireWorkflowId = questionnaireWorkflowId;
    }

    public void setQuestionnaireWorkflow(QuestionnaireWorkflow questionnaireWorkflow) {
        this.questionnaireWorkflow = questionnaireWorkflow;
    }

    public QuestionnaireWorkflow getQuestionnaireWorkflow() {
        return this.questionnaireWorkflow;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public String getLabel() {
        return questionnaireWorkflow != null ? questionnaireWorkflow.getLabel() : "";
    }

    public LookupValue getRole() {
        return role;
    }

    public void setRole(LookupValue role) {
        this.role = role;
    }

    public boolean isManager() {
        return questionnaireWorkflow != null && questionnaireWorkflow.isQuestionnaireManager();
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("user", getUser().getId())
                .toString();
    }

    public Long getLockedBy() {
        return lockedBy;
    }

    public void setLockedBy(Long lockedBy) {
        this.lockedBy = lockedBy;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    /**
     * Non-persistent field.
     */
    private QuestionnaireWorkflow questionnaireWorkflow;

    private Subject subject;
    /**
     * persistent field
     */
    private User user;

    /**
     * persistent field
     */
    private Long subjectId;

    /**
     * persistent field
     */
    private boolean completed;

    private boolean locked;

    /**
     * persistent field
     */
    private Long notificationId;

    /**
     * persistent field
     */
    private Long questionnaireWorkflowId;

    /**
     * persistent field
     */
    private Date completedDate;

    private Long lockedBy;

    /**
     * persistent field
     */
    private LookupValue role;
    public static final String STATUS_OPEN = "OPEN";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_ARCHIVED = "ARCHIVED";
}

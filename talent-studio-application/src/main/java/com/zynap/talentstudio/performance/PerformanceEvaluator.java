package com.zynap.talentstudio.performance;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.subjects.Subject;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * @author Hibernate CodeGenerator
 */
public class PerformanceEvaluator extends ZynapDomainObject {

    /**
     * default constructor.
     */
    public PerformanceEvaluator() {
    }


    public PerformanceEvaluator(LookupValue role, PerformanceReview performanceReview, Subject subject, User user) {
        
        this.role = role;
        this.performanceReview = performanceReview;
        this.performanceId = performanceReview != null ? performanceReview.getId() : null;
        this.subject = subject;
        this.subjectId = subject != null ? subject.getId() : null;
        this.user = user;
        this.userId = user != null ? user.getId() : null;
    }

    public LookupValue getRole() {
        return role;
    }

    public void setRole(LookupValue role) {
        this.role = role;
    }

    public String getLabel() {
        return role.getLabel();
    }

    public PerformanceReview getPerformanceReview() {
        return this.performanceReview;
    }

    public void setPerformanceReview(PerformanceReview performanceReview) {
        this.performanceReview = performanceReview;
    }

    public Subject getSubject() {
        return this.subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isSelfEvaluator() {
        return SELF_EVALUATOR.equals(role.getValueId());
    }

    public Long getPerformanceId() {
        return performanceId;
    }

    public void setPerformanceId(Long performanceId) {
        this.performanceId = performanceId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("role", getRole())
                .toString();
    }

    public static final String SELF_EVALUATOR = "SELFEVALUATOR";

    private LookupValue role;
    private PerformanceReview performanceReview;
    private Subject subject;
    private User user;
    private Long performanceId;
    private Long subjectId;
    private Long userId;
}

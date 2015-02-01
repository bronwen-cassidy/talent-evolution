package com.zynap.talentstudio.web.perfomance;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.performance.PerformanceEvaluator;
import com.zynap.talentstudio.common.lookups.LookupValue;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 18-Nov-2005
 * Time: 11:18:52
 * Wrapper used by Spring for setting appraisal roles.
 */
public final class PerformanceEvaluatorWrapper {

    public PerformanceEvaluatorWrapper(PerformanceEvaluator performanceEvaluator) {
        this.performanceEvaluator = performanceEvaluator;

        final User user = performanceEvaluator.getUser();
        if (user != null) {
            this.performerLabel = user.getLabel();
            this.performerId = user.getId();
        }
    }

    public Long getPerformerId() {
        return performerId;
    }

    public void setPerformerId(Long performerId) {
        this.performerId = performerId;
    }

    public String getLabel() {
        return performanceEvaluator.getLabel();
    }

    public PerformanceEvaluator getPerformanceEvaluator() {
        return performanceEvaluator;
    }

    public String getPerformerLabel() {
        return performerLabel;
    }

    public void setPerformerLabel(String performerLabel) {
        this.performerLabel = performerLabel;
    }

    public boolean isHasUser() {
        return performanceEvaluator.getUser() != null;
    }

    public String getPerformanceReviewLabel() {
        return performanceEvaluator.getPerformanceReview().getLabel();
    }

    public String getSubjectName() {
        return performanceEvaluator.getSubject().getLabel();
    }

    public Long getPerformanceRoleId() {
        LookupValue role = performanceEvaluator.getRole();
        return role != null ? role.getId() : null;
    }

    public void setPerformanceRoleId(Long id) {
        LookupValue role = performanceEvaluator.getRole();
        if(role == null) {
            role = new LookupValue(id);
        }
        role.setId(id);
        performanceEvaluator.setRole(role);
    }

    private final PerformanceEvaluator performanceEvaluator;
    private String performerLabel;
    private Long performerId;
}

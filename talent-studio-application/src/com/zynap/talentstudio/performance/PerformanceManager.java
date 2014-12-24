package com.zynap.talentstudio.performance;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.subjects.Subject;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * @author Hibernate CodeGenerator
 */
public class PerformanceManager extends ZynapDomainObject {

    /**
     * default constructor.
     */
    public PerformanceManager() {
    }

    public PerformanceManager(Long subjectId, Long performanceId, Long managerId) {
        this.subjectId = subjectId;
        this.performanceId = performanceId;
        this.managerId = managerId;
    }


    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public Long getPerformanceId() {
        return performanceId;
    }

    public void setPerformanceId(Long performanceId) {
        this.performanceId = performanceId;
    }


    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .toString();
    }

    private Long subjectId;
    private Long managerId;
    private Long performanceId;


}
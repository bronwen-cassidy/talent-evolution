/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports;

import com.zynap.talentstudio.performance.PerformanceReview;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 09-Apr-2010 20:59:12
 */
public class AppraisalSummaryReport extends Report {

    public AppraisalSummaryReport() {
    }

    public AppraisalSummaryReport(Long id) {
        super(id);
    }

    public Long getAppraisalId() {
        return appraisalId;
    }

    public void setAppraisalId(Long appraisalId) {
        this.appraisalId = appraisalId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PerformanceReview getPerformanceReview() {
        return performanceReview;
    }

    /**
     * Hibernate setter field only as this is a read only object of this class
     *
     * @param performanceReview - the performance review the rport is based on (used for appraisal reports)
     */
    public void setPerformanceReview(PerformanceReview performanceReview) {
        this.performanceReview = performanceReview;
    }

    public boolean isAppraisalReport() {
        return true;
    }

    /* published, archived or new. Default is new */
    public static final String STATUS_NEW = "NEW";
    private String status = STATUS_NEW;
    /* used for the per per-person appraisal summary reports default null */
    private Long appraisalId;
    /* read only field not for insert or update */
    private PerformanceReview performanceReview;
    public static final String STATUS_PUBLISHED = "PUBLISHED";
    public static final String STATUS_ARCHIVED = "ARCHIVED";
}

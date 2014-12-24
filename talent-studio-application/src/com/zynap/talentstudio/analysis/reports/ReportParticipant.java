/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 10-Feb-2011 17:01:35
 */
public class ReportParticipant implements Serializable {

    public ReportParticipant() {
    }

    public ReportParticipant(Long reportId, Long subjectId) {
        this.reportId = reportId;
        this.subjectId = subjectId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReportParticipant that = (ReportParticipant) o;

        if (reportId != null ? !reportId.equals(that.reportId) : that.reportId != null) return false;
        if (subjectId != null ? !subjectId.equals(that.subjectId) : that.subjectId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = subjectId != null ? subjectId.hashCode() : 0;
        result = 31 * result + (reportId != null ? reportId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ReportParticipant{" +
                "subjectId=" + subjectId +
                ", reportId=" + reportId +
                '}';
    }

    private Long subjectId;
    private Long reportId;
}

/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.dashboard;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 17-May-2010 17:33:36
 */
public class DashboardParticipant implements Serializable {

    public DashboardParticipant() {
    }


    public DashboardParticipant(Long dashboardId, Long subjectId) {
        this.dashboardId = dashboardId;
        this.subjectId = subjectId;
    }

    public Long getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(Long dashboardId) {
        this.dashboardId = dashboardId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DashboardParticipant that = (DashboardParticipant) o;

        if (!dashboardId.equals(that.dashboardId)) return false;
        if (!subjectId.equals(that.subjectId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = dashboardId.hashCode();
        result = 31 * result + subjectId.hashCode();
        return result;
    }

    private Long dashboardId;
    private Long subjectId;
}

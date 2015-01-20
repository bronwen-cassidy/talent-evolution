/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.dashboard;

import com.zynap.talentstudio.display.DisplayConfigItem;
import com.zynap.talentstudio.web.organisation.DisplayContentWrapper;
import com.zynap.talentstudio.web.organisation.SubjectDashboardWrapper;
import com.zynap.talentstudio.web.organisation.subjects.SubjectWrapperBean;

import java.io.Serializable;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 09-Jun-2010 15:21:39
 */
public class MyDashboardWrapper implements Serializable {

    public void setDashboards(Set<SubjectDashboardWrapper> subjectDashboardItems) {
        this.dashboards = subjectDashboardItems;
    }

    public Set<SubjectDashboardWrapper> getDashboards() {
        return dashboards;
    }

    public void setNodeWrapper(SubjectWrapperBean nodeWrapper) {
        this.nodeWrapper = nodeWrapper;
    }

    public SubjectWrapperBean getNodeWrapper() {
        return nodeWrapper;
    }

    public void setContentView(DisplayContentWrapper contentView) {
        this.contentView = contentView;
    }

    public DisplayContentWrapper getContentView() {
        return contentView;
    }

    public String getActiveTab() {
        DisplayConfigItem portfolioView = contentView.getPortfolioView();
        return portfolioView != null ? portfolioView.getKey() : "";
    }

    private Set<SubjectDashboardWrapper> dashboards;
    private SubjectWrapperBean nodeWrapper;
    private DisplayContentWrapper contentView;
}

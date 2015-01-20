/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.views;

import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.organisation.subjects.Subject;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 22-Feb-2006 09:40:15
 */
public class RunProgressReportWrapper extends RunReportWrapperBean {

    public RunProgressReportWrapper(Report reportDefinition, Subject node) {
        super(reportDefinition);
        this.node = node;
    }

    public Subject getNode() {
        return node;
    }

    private Subject node;
}
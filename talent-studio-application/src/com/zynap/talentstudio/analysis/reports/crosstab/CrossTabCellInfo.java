/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports.crosstab;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.talentstudio.analysis.reports.Report;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 16-Apr-2008 09:46:43
 */
public class CrossTabCellInfo extends ZynapDomainObject {

    public CrossTabCellInfo() {
    }

    public CrossTabCellInfo(Long horizontalValueId, Long verticalValueId) {
        this.horizontalValueId = horizontalValueId;
        this.verticalValueId = verticalValueId;
    }

    public CrossTabCellInfo(Long horizontalValueId, Long verticalValueId, String label) {
        this(horizontalValueId, verticalValueId);
        setLabel(label);
    }

    public Long getVerticalValueId() {
        return verticalValueId;
    }

    public void setVerticalValueId(Long verticalValueId) {
        this.verticalValueId = verticalValueId;
    }

    public Long getHorizontalValueId() {
        return horizontalValueId;
    }

    public void setHorizontalValueId(Long horizontalValueId) {
        this.horizontalValueId = horizontalValueId;
    }

    public Report getCrosstabReport() {
        return crosstabReport;
    }

    public void setCrosstabReport(Report crosstabReport) {
        this.crosstabReport = crosstabReport;
    }

    public String toString() {
        return "Crosstab Cell Info: " + getLabel() + " for crosstab report: " + getCrosstabReport();
    }

    private Long verticalValueId;
    private Long horizontalValueId;
    private Report crosstabReport;
}

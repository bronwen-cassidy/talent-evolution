/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports;

import com.zynap.talentstudio.analysis.reports.crosstab.CrossTabCellInfo;
import com.zynap.talentstudio.common.Direction;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 09-Apr-2010 20:54:58
 */
public class CrossTabReport extends Report {

    public CrossTabReport() {
        super();
    }

    public CrossTabReport(Long id) {
        super(id);
    }

    public CrossTabReport(String label, String description, String access) {
        super(label, description, access);
    }

    public void assignNewCellInfos(List<CrossTabCellInfo> cellInfos) {
        getCrossTabCellInfos().clear();
        if (cellInfos != null) {
            for (Iterator<CrossTabCellInfo> iterator = cellInfos.iterator(); iterator.hasNext();) {
                CrossTabCellInfo cellInfo = iterator.next();
                addCrosstabCellInfo(cellInfo);
            }
        }
    }

    /**
     * is the equivalent to the left column on a cross tab report.
     * <br/>
     * The vertical column is identifiable by it's columnSource equal to {@link com.zynap.talentstudio.common.Direction#VERTICAL } toString value
     *
     * @return Column
     */
    public Column getVerticalColumn() {
        return (Column) CollectionUtils.find(getColumns(), new ReportDirectionPredicate(Direction.VERTICAL.toString()));
    }

    /**
     * Gets the horizontal column for the cross-tab if this is not cross-tab report returns null.
     *
     * @return Column identified by the toString() value of {@link Direction#HORIZONTAL }
     */
    public Column getHorizontalColumn() {
        return (Column) CollectionUtils.find(getColumns(), new ReportDirectionPredicate(Direction.HORIZONTAL.toString()));
    }

    public void setVerticalColumn(Column column) {
        column.setColumnSource(Direction.VERTICAL.toString());
        addColumn(column);
    }

    public void setHorizontalColumn(Column column) {
        column.setColumnSource(Direction.HORIZONTAL.toString());
        addColumn(column);
    }

    public void addCrosstabCellInfo(CrossTabCellInfo crossTabCellInfo) {
        crossTabCellInfo.setCrosstabReport(this);
        crossTabCellInfos.add(crossTabCellInfo);
    }

    public boolean isCrossTabReport() {
        return true;
    }

    public List<CrossTabCellInfo> getCrossTabCellInfos() {
        return crossTabCellInfos;
    }

    public void setCrossTabCellInfos(List<CrossTabCellInfo> crossTabCellInfos) {
        this.crossTabCellInfos = crossTabCellInfos;
    }

    public Integer getDisplayLimit() {
        return displayLimit;
    }

    public void setDisplayLimit(Integer displayLimit) {
        this.displayLimit = displayLimit;
    }

    private Integer displayLimit;
    private List<CrossTabCellInfo> crossTabCellInfos = new ArrayList<CrossTabCellInfo>();

}

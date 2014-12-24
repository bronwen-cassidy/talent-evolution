/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.domain.UserSession;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.metrics.IMetricService;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.CrossTabReport;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.crosstab.CrossTabCellInfo;
import com.zynap.talentstudio.organisation.IOrganisationUnitService;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.web.analysis.reports.cewolf.ChartConstants;
import com.zynap.talentstudio.web.analysis.reports.cewolf.processors.ChartHelper;
import com.zynap.talentstudio.web.analysis.reports.support.CrossTabReportRunner;
import com.zynap.talentstudio.web.analysis.reports.views.RunCrossTabReportWrapper;
import com.zynap.talentstudio.web.analysis.reports.views.RunReportWrapperBean;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class RunCrossTabController extends RunReportController {

    protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

        RunCrossTabReportWrapper wrapper = (RunCrossTabReportWrapper) command;
        Map<String, Object> refData = new HashMap<String, Object>();

        // load populations and reports that can be used as run options
        UserSession userSession = ZynapWebUtils.getUserSession(request);
        refData.put(REPORT_KEY, getRunnableReports(wrapper, userSession));
        refData.put(ControllerConstants.POPULATIONS, getRunnablePopulations(wrapper, userSession));

        // add chart types
        refData.put(CHART_TYPES, barChartTypes);

        final ChartHelper chartHelper = new ChartHelper();
        switch (page) {
            case PIE_CHART_IDX:

                // must get the column index id or the row index id to get the correct information.
                String rowLabel = request.getParameter(CROSS_TAB_ROW_ID);
                int rowIndex = RequestUtils.getIntParameter(request, ROW_INDEX, -1);
                String columnLabel = request.getParameter(CROSS_TAB_COLUMN_ID);
                int columnIndex = RequestUtils.getIntParameter(request, COLUMN_INDEX, -1);

                // get the passed through label
                String label = StringUtils.hasText(rowLabel) ? rowLabel : columnLabel;
                List rows = (List) wrapper.getDiscreetRows();
                refData.put(ChartConstants.PRODUCER_PARAM, chartHelper.createCrossTabPieChartProducer(rows, rowIndex, columnIndex, label, wrapper.getReport()));
                refData.put(ChartConstants.LABEL_PROCESSOR, chartHelper.createPieChartLabelProcessor(wrapper));
                wrapper.setActiveTab(CHART_TAB);
                break;
            
            case BAR_CHART_OPTIONS_SELECTED:

                wrapper.setActiveTab(CHART_TAB);
                String axisOrientation = request.getParameter("axis");
                if (StringUtils.hasText(axisOrientation)) {
                    wrapper.setAxisOrientation(axisOrientation);
                }
                if (wrapper.getChartType().equals(ChartHelper.OVERLAID_CHART_TYPE)) {
                    refData.put(ChartConstants.PRODUCER_PARAM, chartHelper.createCrossTabOverlaidChartProducer(wrapper));
                } else {
                    refData.put(ChartConstants.PRODUCER_PARAM, chartHelper.createCrossTabBarChartProducer(wrapper));
                    refData.put(ChartConstants.LABEL_PROCESSOR, chartHelper.createBarChartLabelProcessor(wrapper));
                    refData.put(ChartConstants.PERCENT_ITEM_PROCESSOR, chartHelper.createPercentLabelProcessor(wrapper));
                }
                break;
        }
        return refData;
    }

    @Override
    public Class getReportClass() {
        return CrossTabReport.class;
    }

    protected Collection getRunnableReports(RunReportWrapperBean reportWrapperBean, UserSession userSession) {
        return getReportService().findAllCrossTabReports(userSession.getId());
    }

    protected void runReport(RunReportWrapperBean runReportWrapper, Long userId, HttpServletRequest request) throws TalentStudioException {
        RunCrossTabReportWrapper crossTabWrapper = (RunCrossTabReportWrapper) runReportWrapper;
        CrossTabReport report = (CrossTabReport) crossTabWrapper.getReport();
        // check potential number of results
        final Column horizontalAttribute = report.getHorizontalColumn();
        final Column verticalAttribute = report.getVerticalColumn();
        if (!CrossTabReportRunner.checkNumberOfResults(horizontalAttribute, verticalAttribute, maxCellsNumber, dynamicAttributeService, organisationUnitService)) {
            crossTabWrapper.setOverflow(new Integer(maxCellsNumber));
        } else {
            reportRunner.run(runReportWrapper, userId);
        }
    }

    protected RunReportWrapperBean createFormBackingObject(Report report, Long userId) throws TalentStudioException {
        final Report displayReport = report.getDisplayReport();

        if (displayReport != null) {
            formatDisplayReport(displayReport, report);
        }
        RunCrossTabReportWrapper wrapper = new RunCrossTabReportWrapper(report);
        wrapper.setMetrics(metricService.findAvailableMetrics(userId, getReportType(report)));
        final List<CrossTabCellInfo> infoList = ((CrossTabReport) report).getCrossTabCellInfos();
        // lazy load
        infoList.size();
        wrapper.setCrosstabCellInfos(infoList);
        return wrapper;
    }

    protected RunReportWrapperBean recoverFormBackingObject(HttpServletRequest request) {
        return (RunCrossTabReportWrapper) HistoryHelper.recoverCommand(request, RunCrossTabReportWrapper.class);
    }

    public void setOrganisationUnitService(IOrganisationUnitService organisationUnitService) {
        this.organisationUnitService = organisationUnitService;
    }

    public void setDynamicAttributeService(IDynamicAttributeService dynamicAttributeService) {
        this.dynamicAttributeService = dynamicAttributeService;
    }

    public void setMetricService(IMetricService metricService) {
        this.metricService = metricService;
    }

    private void formatDisplayReport(Report displayReport, Report report) throws TalentStudioException {
        final List<Column> columns = displayReport.getColumns();

        // get grouping columns
        Column verticalGroupingColumn = getGroupingColumn(((CrossTabReport) report).getVerticalColumn());
        Column horizontalGroupingColumn = getGroupingColumn(((CrossTabReport) report).getHorizontalColumn());

        final AnalysisParameter verticalColumnAttribute = verticalGroupingColumn.getAnalysisParameter();
        final AnalysisParameter horizontalColumnAttribute = horizontalGroupingColumn.getAnalysisParameter();

        // remove duplicate and grouping columns
        for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
            Column column = (Column) iterator.next();
            final AnalysisParameter columnAttribute = column.getAnalysisParameter();
            if(column.isFormula()) continue;
            if (column.isGrouped() || (columnAttribute.equals(verticalColumnAttribute) || columnAttribute.equals(horizontalColumnAttribute))) {
                iterator.remove();
            }
        }

        // add grouping columns for horizontal and then vertical
        columns.add(0, verticalGroupingColumn);
        columns.add(1, horizontalGroupingColumn);

        // set positions of columns
        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            column.setPosition(new Integer(i));
        }

        // compile new report definition
        getReportService().compileReportDesign(displayReport);
    }

    private Column getGroupingColumn(Column originalColumn) {

        Column newColumn;
        newColumn = (Column) originalColumn.clone();
        newColumn.setGrouped(true);

        // always use org unit label rather than id for grouping
        String attributeName = newColumn.getAttributeName();
        String newAttributeName = attributeName.replaceFirst(AnalysisAttributeHelper.ORG_UNIT_ID_ATTR, AnalysisAttributeHelper.ORG_UNIT_LABEL_ATTR);
        newColumn.setAttributeName(newAttributeName);

        return newColumn;
    }

    private String getReportType(Report report) {
        if (report.getDefaultPopulation() != null) {
            return report.getDefaultPopulation().getType();
        }
        return report.getPopulationType();
    }

    public void setMaxCellsNumber(int maxCellsNumber) {
        this.maxCellsNumber = maxCellsNumber;
    }

    private IDynamicAttributeService dynamicAttributeService;
    private IOrganisationUnitService organisationUnitService;
    private IMetricService metricService;

    private int maxCellsNumber;

    public static final String CROSS_TAB_ROW_ID = "ct_row_id";
    public static final String CROSS_TAB_COLUMN_ID = "ct_column_id";
    public static final String COLUMN_INDEX = "colIndex";
    public static final String ROW_INDEX = "rowIndex";
}

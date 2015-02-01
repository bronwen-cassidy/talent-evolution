/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.domain.UserSession;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.metrics.IMetricService;
import com.zynap.talentstudio.analysis.metrics.Metric;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.common.Direction;
import com.zynap.talentstudio.web.analysis.reports.functions.FunctionWrapperBean;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public abstract class BaseMetricReportController extends BaseReportsWizardController {

    protected final void initMetrics(MetricReportWrapperBean metricReportWrapperBean, Long userId) throws TalentStudioException {
        List<Metric> metrics = metricService.findAll(userId, metricReportWrapperBean.getType(), metricReportWrapperBean.getAccess());
        metricReportWrapperBean.setMetrics(metrics);
        metricReportWrapperBean.checkColumnAttributes();
    }

    protected final Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        Map<String, Object> refData = new HashMap<String, Object>();
        MetricReportWrapperBean bean = (MetricReportWrapperBean) command;

        final UserSession userSession = ZynapWebUtils.getUserSession(request);
        final Long userId = userSession.getId();
        if (page != CORE_DETAILS_IDX) {
            if(bean.getType() != null) initMetrics(bean, userId);
        }

        int titlePageNumber = page;
        switch (page) {
            case CORE_DETAILS_IDX:
                setSuitablePopulations(bean, userId);
                checkReportOwner(bean, userId);
                break;
            case ADD_DELETE_COLUMNS_IDX:
                setColumnAttributes(bean);
                bean.setDrillDownReports(getReportService().findCompatibleReports(bean.getReport(), userId));
                addColumn(request, bean, getDefaultMetricColumn());
                break;
            case REMOVE_METRIC_COLUMNS_IDX:
                removeColumn(request, bean);
                break;
            case ADD_FORMULA_IDX:
            case EDIT_FORMULA_IDX:
            case ADD_OPERAND_TO_FORMULA:
            case REMOVE_OPERAND_FROM_FORMULA:
                addDataForFormula(refData);
                bean.checkFunctionAttributes(bean.getSelectedFunction());
                break;
        }

        // use correct page title
        if (titlePageNumber > ADD_DELETE_COLUMNS_IDX) {
            titlePageNumber = ADD_DELETE_COLUMNS_IDX;
        }

        String pageTitle = "metreports.wizard.page." + titlePageNumber;
        refData.put(ControllerConstants.TITLE, pageTitle);
        return refData;
    }

    protected final void validateColumns(ReportValidator validator, ReportWrapperBean bean, Errors errors) {
        validator.validateMetricColumns(bean, errors);
    }

    public final void setMetricService(IMetricService metricService) {
        this.metricService = metricService;
    }

    protected final void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

        // IMPORTANT - sets target and clears menu sections if required
        super.onBindAndValidateInternal(request, command, errors, page);

        // only handle validating the grouping information if we have finished as this is the beset time to pick up all the checkboxes accurately!!
        final MetricReportWrapperBean metricReportWrapperBean = (MetricReportWrapperBean) command;

        if (!isFinishRequest(request)) {
            switch (getTargetPage(request, page)) {
                case ADD_FORMULA_IDX:
                    final FunctionWrapperBean selectedFunction = new FunctionWrapperBean(null);
                    metricReportWrapperBean.setSelectedFunction(selectedFunction);
                    metricReportWrapperBean.setCurrentColumnIndex(null);
                    break;
                case EDIT_FORMULA_IDX:
                    int selectedColumnIndex = RequestUtils.getIntParameter(request, SELECTED_COLUMN_INDEX, -1);
                    assignFormulaInformation(selectedColumnIndex, metricReportWrapperBean);
                    break;
                case ADD_OPERAND_TO_FORMULA:
                    FunctionWrapperBean functionWrapperBean = metricReportWrapperBean.getSelectedFunction();
                    functionWrapperBean.addEmptyExpression();
                    break;
                case REMOVE_OPERAND_FROM_FORMULA:
                    selectedColumnIndex = RequestUtils.getIntParameter(request, SELECTED_COLUMN_INDEX, -1);
                    functionWrapperBean = metricReportWrapperBean.getSelectedFunction();
                    functionWrapperBean.getOperands().remove(selectedColumnIndex);
                    break;
                case SAVE_FORMULA_IDX:
                    ((ReportValidator) getValidator()).validateFormula(metricReportWrapperBean, errors);
                    if (!errors.hasErrors()) saveFormula(metricReportWrapperBean);
                    break;
                case CANCEL_FORMULA_IDX:
                    metricReportWrapperBean.resetSelectedFunction();
                    break;
            }
        }

    }

    /**
     * Returns a column for the default count metric.
     *
     * @return Column
     */
    private Column getDefaultMetricColumn() {
        return new Column(IPopulationEngine.COUNT, null, new Integer(0), null, Direction.NA_DIRECTION.toString());
    }

    private IMetricService metricService;
    private static final int REMOVE_METRIC_COLUMNS_IDX = 2;
    private static final int ADD_FORMULA_IDX = 3;
    private static final int EDIT_FORMULA_IDX = 4;
    private static final int ADD_OPERAND_TO_FORMULA = 5;
    private static final int REMOVE_OPERAND_FROM_FORMULA = 6;
    private static final int SAVE_FORMULA_IDX = 7;
    private static final int CANCEL_FORMULA_IDX = 8;
}

package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.domain.UserSession;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.web.analysis.reports.functions.FunctionWrapperBean;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.controller.ControllerUtils;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * User: amark
 * Date: 30-Sep-2005
 * Time: 09:42:47
 */
public abstract class BaseTabularReportWizardController extends BaseReportsWizardController {

    /**
     * Handles the completion of the processing of a report when both adding and editing, calls specific internal finishes depending on action.
     *
     * @param request
     * @param response
     * @param command
     * @param errors
     * @return ModelAndView
     * @throws Exception
     */
    protected final ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        ReportWrapperBean bean = (ReportWrapperBean) command;
        Report report = bean.getModifiedReport();
        if (report.getColumns().isEmpty()) {
            errors.reject(COLUMNS_REQUIRED_ERROR_KEY, "There needs to be at least one column for the report");
            return showPage(request, errors, ADD_DELETE_COLUMNS_IDX);
        }

        return processFinishInternal(report, request);
    }

    protected final Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

        final Map<String, Object> refData = new HashMap<String, Object>();

        TabularReportWrapperBean tabularReportWrapperBean = (TabularReportWrapperBean) command;
        final ColumnWrapperBean selectedColumnWrapper = tabularReportWrapperBean.getSelectedColumnWrapper();
        int selectedColumnIndex = RequestUtils.getIntParameter(request, SELECTED_COLUMN_INDEX, -1);

        final UserSession userSession = ZynapWebUtils.getUserSession(request);
        final Long userId = userSession.getId();

        int titlePageNumber = page;
        switch (page) {
            case CORE_DETAILS_IDX:
                setSuitablePopulations(tabularReportWrapperBean, userId);
                checkReportOwner(tabularReportWrapperBean, userId);
                break;
            case ADD_DELETE_COLUMNS_IDX:
                setColumnAttributes(tabularReportWrapperBean);
                addColumn(request, tabularReportWrapperBean, new Column());
                tabularReportWrapperBean.checkColumnAttributes();
                break;
            case ASSIGN_COLOURS_IDX:
                assignColourInformation(selectedColumnIndex, tabularReportWrapperBean, refData);
                break;
            case SAVE_COLOURS_IDX:
                saveColourInformation(selectedColumnWrapper, request);
                break;
            case REMOVE_COLUMNS_IDX:
                removeColumn(request, tabularReportWrapperBean);
                tabularReportWrapperBean.checkColumnAttributes();
                break;
            case ADD_FORMULA_IDX:
            case EDIT_FORMULA_IDX:
            case ADD_OPERAND_TO_FORMULA:
            case REMOVE_OPERAND_FROM_FORMULA:
                addDataForFormula(refData);
                tabularReportWrapperBean.checkFunctionAttributes(tabularReportWrapperBean.getSelectedFunction());
                break;
        }

        // use correct page title
        if (titlePageNumber > ADD_DELETE_COLUMNS_IDX) {
            titlePageNumber = ADD_DELETE_COLUMNS_IDX;
        }

        String pageTitle = "tabreports.wizard.page." + titlePageNumber;
        refData.put(ControllerConstants.TITLE, pageTitle);
        return refData;
    }

    protected final void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

        // IMPORTANT - sets target and clears menu sections if required
        super.onBindAndValidateInternal(request, command, errors, page);

        // only handle validating the grouping information if we have finished as this is the beset time to pick up all the checkboxes accurately!!
        final TabularReportWrapperBean tabularReportWrapperBean = (TabularReportWrapperBean) command;

        if (isFinishRequest(request) || page == ADD_DELETE_COLUMNS_IDX) {
            // find all the grouping attribute indexes
            handleGroupingIndexes(request, tabularReportWrapperBean);
        }
        if (!isFinishRequest(request)) {
            switch (getTargetPage(request, page)) {
                case ADD_FORMULA_IDX:
                    final FunctionWrapperBean selectedFunction = new FunctionWrapperBean(null);
                    tabularReportWrapperBean.setSelectedFunction(selectedFunction);
                    tabularReportWrapperBean.setCurrentColumnIndex(null);
                    break;
                case EDIT_FORMULA_IDX:
                    int selectedColumnIndex = RequestUtils.getIntParameter(request, SELECTED_COLUMN_INDEX, -1);
                    assignFormulaInformation(selectedColumnIndex, tabularReportWrapperBean);
                    break;
                case ADD_OPERAND_TO_FORMULA:
                    FunctionWrapperBean functionWrapperBean = tabularReportWrapperBean.getSelectedFunction();
                    functionWrapperBean.addEmptyExpression();
                    tabularReportWrapperBean.checkFunctionAttributes(functionWrapperBean);
                    break;
                case REMOVE_OPERAND_FROM_FORMULA:
                    selectedColumnIndex = RequestUtils.getIntParameter(request, SELECTED_COLUMN_INDEX, -1);
                    functionWrapperBean = tabularReportWrapperBean.getSelectedFunction();
                    functionWrapperBean.getOperands().remove(selectedColumnIndex);
                    tabularReportWrapperBean.checkFunctionAttributes(functionWrapperBean);
                    break;
                case SAVE_FORMULA_IDX:
                    ((ReportValidator) getValidator()).validateFormula(tabularReportWrapperBean, errors);
                    tabularReportWrapperBean.checkFunctionAttributes(tabularReportWrapperBean.getSelectedFunction());
                    if (!errors.hasErrors())
                        saveFormula(tabularReportWrapperBean);
                    break;
                case CANCEL_FORMULA_IDX:
                    tabularReportWrapperBean.resetSelectedFunction();
                    break;
            }
        }
    }

    private void handleGroupingIndexes(HttpServletRequest request, TabularReportWrapperBean tabularReportWrapperBean) {
        List<Integer> indexValues = new ArrayList<Integer>();
        Map parameterMap = request.getParameterMap();
        for (Iterator iterator = parameterMap.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String name = (String) entry.getKey();
            if (name.endsWith(GROUPED_PROPERTY)) {
                indexValues.add(ControllerUtils.extractIndex(name));
            }
        }
        tabularReportWrapperBean.setGroupedColumns(indexValues);
    }

    protected void validateColumns(ReportValidator validator, ReportWrapperBean bean, Errors errors) {
        validator.validateTabularColumns(bean, errors);
    }

    protected abstract ModelAndView processFinishInternal(Report report, HttpServletRequest request) throws TalentStudioException;

    private void saveColourInformation(ColumnWrapperBean selectedColumnWrapper, HttpServletRequest request) {
        if (selectedColumnWrapper != null) {
            final boolean useSelectedColours = RequestUtils.getBooleanParameter(request, USE_SELECTED_COLOURS_PARAM, false);
            selectedColumnWrapper.setColorDisplayable(useSelectedColours);

            final String[] colours = request.getParameterValues(SELECTED_COLOURS);
            if (colours != null) selectedColumnWrapper.setSelectedColours(colours);
        }
    }

    private void assignColourInformation(int selectedColumnIndex, TabularReportWrapperBean tabularReportWrapperBean, Map<String, Object> refData) throws TalentStudioException {
        if (selectedColumnIndex != -1) {

            // if there is no current one get it and get the dynamic attribute and
            // use it to set everything - required 'cos lookup values are lazy-loaded
            final ColumnWrapperBean columnWrapperBean = tabularReportWrapperBean.getColumnWrapper(selectedColumnIndex);
            if (columnWrapperBean != null) {
                final Long dynamicAttributeId = columnWrapperBean.getDynamicAttributeId();
                if (dynamicAttributeId != null) {
                    final DynamicAttribute dynamicAttribute = getDynamicAttributeService().findById(dynamicAttributeId);
                    columnWrapperBean.initColumnDisplayImages(dynamicAttribute);
                    tabularReportWrapperBean.setSelectedColumnWrapper(columnWrapperBean);
                }

                // bind request details back into the page so that they can be picked up and used by the javascript
                refData.put(SHOW_POPUP, Boolean.TRUE);
                refData.put(SELECTED_COLUMN_INDEX, new Integer(selectedColumnIndex));
            }
        }
    }

    private static final int ADD_FORMULA_IDX = 5;
    private static final int EDIT_FORMULA_IDX = 6;
    private static final int ADD_OPERAND_TO_FORMULA = 7;
    private static final int REMOVE_OPERAND_FROM_FORMULA = 8;
    private static final int SAVE_FORMULA_IDX = 9;
    private static final int CANCEL_FORMULA_IDX = 10;

    private static final String GROUPED_PROPERTY = "grouped";

    private static final String SELECTED_COLOURS = "selectedColours";
    private static final String USE_SELECTED_COLOURS_PARAM = "useSelectedColours";
    protected static final String COLUMNS_REQUIRED_ERROR_KEY = "error.columns.required";


}

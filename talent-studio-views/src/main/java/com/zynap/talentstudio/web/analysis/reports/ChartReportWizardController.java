package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.domain.UserSession;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.reports.ChartReport;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.arenas.MenuSection;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.web.analysis.reports.support.ReportServiceHelper;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.beans.ColourEditor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * User: bcassidy
 * Date: 30-Sep-2010
 * Time: 09:42:47
 */
public class ChartReportWizardController extends BaseReportsWizardController {

    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        super.initBinder(request, binder);
        binder.registerCustomEditor(String.class, "reportColumns.displayColour", new ColourEditor());
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        UserSession userSession = ZynapWebUtils.getUserSession(request);
        final Collection<MenuSection> menuSections = getReportService().getMenuSections();
        final Collection<MenuSection> homeMenuSections = getReportService().getHomePageReportMenuSections();
        String reportIdParameter = request.getParameter(ParameterConstants.REPORT_ID);
        ChartReport report;
        if (reportIdParameter != null) {
            report = (ChartReport) reportService.findById(new Long(reportIdParameter));
        } else {
            report = new ChartReport();
            report.setUserId(userSession.getId());
        }
        ChartReportWrapperBean chartReportWrapperBean = new ChartReportWrapperBean(report, menuSections, homeMenuSections, getRunReportURL());
        if (reportIdParameter == null) {
            chartReportWrapperBean.setReports(reportService.findAllReports(false, userSession.getId(), Node.SUBJECT_UNIT_TYPE_, Report.TABULAR_REPORT));
        }
        chartReportWrapperBean.setDrillDownReports(reportService.findAllReports(true, userSession.getId(), Node.SUBJECT_UNIT_TYPE_, Report.TABULAR_REPORT));
        chartReportWrapperBean.setGroups(wrapGroups(getUserGroups(), report));
        return chartReportWrapperBean;
    }

    /**
     * Only supports publishing to people as takes all of it's information from workflows which are not supported for jobs.
     *
     * @param bean   - the form bean to support the views
     * @param userId - the logged in user to determine allowed populations
     */
    @Override
    protected void setSuitablePopulations(ReportWrapperBean bean, Long userId) throws TalentStudioException {
        if (bean.getColumns().isEmpty()) {
            bean.setPopulations(getPopulations(userId, new Population(null, Node.SUBJECT_UNIT_TYPE_, "", bean.getAccess(), "", null, null), bean.getAccess()));
        } else {
            bean.setPopulations(getPopulations(userId, bean.getDefaultPopulation(), bean.getAccess()));
        }
    }

    protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

        final Map<String, Object> refData = new HashMap<String, Object>();

        ChartReportWrapperBean bean = (ChartReportWrapperBean) command;

        final UserSession userSession = ZynapWebUtils.getUserSession(request);
        final Long userId = userSession.getId();

        switch (page) {
            case CORE_DETAILS_IDX:
                setSuitablePopulations(bean, userId);
                checkReportOwner(bean, userId);
                break;
            case ADD_DELETE_COLUMNS_IDX:
                int addColButton = RequestUtils.getIntParameter(request, "selectedColumnIndex", -1);
                int deleteColButton = RequestUtils.getIntParameter(request, "deletedColumnIndex", -1);
                if (addColButton > -1) {
                    bean.addColumn(new Column());
                }
                if (deleteColButton > -1) {
                    bean.removeColumn(deleteColButton);
                    // reassign any answers
                    Long[] attributeIds = bean.collectSelectedAttributes();
                    Collection<DynamicAttribute> attrs = dynamicAttributeService.getAllAttributes(attributeIds);
                    Set<String> answers = new HashSet<String>();
                    assignAnswers(bean, attrs, answers);
                }
                bean.checkColumnAttributes();
                break;
        }

        String pageTitle = "chartreports.wizard.page." + page;
        refData.put(ControllerConstants.TITLE, pageTitle);
        return refData;
    }

    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        // IMPORTANT - sets target and clears menu sections if required
        super.onBindAndValidateInternal(request, command, errors, page);
        // interested in the target page as we need to prepare the columns when adding this will be taken from the reportId
        int targetPage = getTargetPage(request, page);
        ChartReportWrapperBean bean = (ChartReportWrapperBean) command;
        if(isFinishRequest(request)) {
            ((ReportValidator)getValidator()).validateChartColumns(bean, errors);
        }

        switch (targetPage) {
            case ADD_DELETE_COLUMNS_IDX:
                int addColButton = RequestUtils.getIntParameter(request, "selectedColumnIndex", -1);
                int deleteColButton = RequestUtils.getIntParameter(request, "deletedColumnIndex", -1);

                if (addColButton == -1 && deleteColButton == -1) {
                    Long reportId = bean.getReportId();
                    if (reportId != null) {
                        addAndFilterColumns(bean, reportId);
                    } else if (bean.getColumns().isEmpty()) {
                        for (int i = 0; i < 3; i++) {
                            bean.addColumn(new Column());
                        }
                    }
                }
                setColumnAttributes(bean);
                break;
            case ASSIGN_COLOURS_IDX:
                ReportValidator validator = (ReportValidator) getValidator();
                validator.validateChartAttributes(bean, errors);
                if (!errors.hasErrors()) {
                    // prepare the report columns we need the dynamicAttributes from the previous selections and then to gather the short descriptions from the lookup_values
                    Long[] attributeIds = bean.collectSelectedAttributes();
                    Collection<DynamicAttribute> attrs = dynamicAttributeService.getAllAttributes(attributeIds);
                    Set<String> answers = new HashSet<String>();
                    assignAnswers(bean, attrs, answers);
                }
                break;
        }
    }

    @SuppressWarnings({"unchecked"})
    private void assignAnswers(ChartReportWrapperBean bean, Collection<DynamicAttribute> attrs, Set<String> answers) {
        for (DynamicAttribute attr : attrs) {
            answers.addAll(CollectionUtils.collect(attr.getRefersToType().getLookupValues(), new Transformer() {
                public Object transform(Object input) {
                    return ((LookupValue) input).getLabel();
                }
            }));
        }

        bean.setAnswers(answers);
    }

    private void addAndFilterColumns(ChartReportWrapperBean bean, Long reportId) throws TalentStudioException {
        Report report = (Report) reportService.findById(reportId);
        // clear any previous report columns
        bean.getReportColumns().clear();
        bean.getColumns().clear();
        for (Column column : report.getColumns()) {
            if (shouldAdd(column)) {
                bean.addColumn(new Column(column.getLabel(), column.getDynamicAttributeId(), column.getQuestionnaireWorkflowId()));
            }
        }
    }

    private boolean shouldAdd(Column column) {
        if ((column.isSelectionType() || column.isEnumMappingType()) && AnalysisAttributeHelper.getLevel(column.getAttributeName()) < 1 && column.getQuestionnaireWorkflowId() != null) {
            return true;
        }
        return false;
    }

    protected final void validateColumns(ReportValidator validator, ReportWrapperBean bean, Errors errors) {

    }

    protected void addStaticFields(RedirectView view, Object command) {
        ChartReportWrapperBean bean = (ChartReportWrapperBean) command;
        if (bean.getReport().getId() != null) {
            view.addStaticAttribute(ParameterConstants.REPORT_ID, bean.getReport().getId());
        }
    }

    /**
     * Handles the completion of the processing of a report when both adding and editing
     *
     * @param request
     * @param response
     * @param command
     * @param errors
     * @return ModelAndView
     * @throws Exception
     */
    protected final ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        ChartReportWrapperBean bean = (ChartReportWrapperBean) command;
        Report report = bean.getModifiedReport();
        if (editing) {
            return ReportServiceHelper.updateReport(getSuccessView(), getReportService(), report, ZynapWebUtils.getUserSession(request));
        }
        return ReportServiceHelper.createReport(getSuccessView(), getReportService(), report, ZynapWebUtils.getUserSession(request));
    }

    public void setEditing(boolean editing) {
        this.editing = editing;
    }

    private boolean editing = false;

}
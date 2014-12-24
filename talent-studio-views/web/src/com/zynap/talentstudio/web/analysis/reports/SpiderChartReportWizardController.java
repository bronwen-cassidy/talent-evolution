package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.domain.UserSession;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.reports.ChartReport;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.arenas.MenuSection;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.web.analysis.reports.support.ReportServiceHelper;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.web.beans.ColourEditor;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * User: bcassidy
 * Date: 30-Sep-2010
 * Time: 09:42:47
 */
public class SpiderChartReportWizardController extends BaseReportsWizardController {

    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        super.initBinder(request, binder);
        binder.registerCustomEditor(String.class, "columns.displayColour", new ColourEditor());
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
            report.setChartType(ChartReport.SPIDER_CHART);            
        }
        SpiderChartWrapperBean chartReportWrapperBean = new SpiderChartWrapperBean(report, menuSections, homeMenuSections, getRunReportURL());
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

    protected final Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

        final Map<String, Object> refData = new HashMap<String, Object>();

        SpiderChartWrapperBean bean = (SpiderChartWrapperBean) command;

        final UserSession userSession = ZynapWebUtils.getUserSession(request);
        final Long userId = userSession.getId();

        switch (page) {
            case CORE_DETAILS_IDX:
                setSuitablePopulations(bean, userId);
                checkReportOwner(bean, userId);
                break;
            case ADD_DELETE_COLUMNS_IDX:
                break;
            case ADD_DELETE_ATTRIBUTES_IDX:
                bean.checkColumnAttributes();
                break;
        }

        String pageTitle = "spiderreports.wizard.page." + page;
        refData.put(ControllerConstants.TITLE, pageTitle);
        return refData;
    }

    protected final void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        // IMPORTANT - sets target and clears menu sections if required
        super.onBindAndValidateInternal(request, command, errors, page);
        // interested in the target page as we need to prepare the columns when adding this will be taken from the reportId
        int targetPage = getTargetPage(request, page);
        SpiderChartWrapperBean bean = (SpiderChartWrapperBean) command;
        ReportValidator validator = (ReportValidator) getValidator();

        switch (targetPage) {
            case ADD_DELETE_COLUMNS_IDX:
                if(page == CORE_DETAILS_IDX) {
                    validator.validateCoreValues(command, errors);    
                }
                int addColButton = RequestUtils.getIntParameter(request, "selectedColumnIndex", -1);
                int deleteColButton = RequestUtils.getIntParameter(request, "deletedColumnIndex", -1);

                if (addColButton != -1) bean.addColumn(new Column());
                if (deleteColButton != -1) bean.removeColumn(deleteColButton);

                break;
            case ADD_DELETE_ATTRIBUTES_IDX:
                if(page == ADD_DELETE_COLUMNS_IDX) {
                    validator.validateSpiderChartColumns(bean, errors);
                }
                int addAttrColButton = RequestUtils.getIntParameter(request, "selectedColumnIndex", -1);
                int deleteAttrColButton = RequestUtils.getIntParameter(request, "deletedColumnIndex", -1);
                if(addAttrColButton != -1) bean.addColumnAttribute();
                if(deleteAttrColButton != -1) bean.removeColumnAttribute(deleteAttrColButton);
                setColumnAttributes(bean);                
                break;
        }

        if(isFinishRequest(request)) {
            validator.validateCoreValues(command, errors);
            validator.validateSpiderChartColumns(bean, errors);
            validator.validateSpiderChartAttributes(bean, errors);
        }
    }

    protected void validatePage(Object command, Errors errors, int page, boolean finish) {
    }

    protected final void validateColumns(ReportValidator validator, ReportWrapperBean bean, Errors errors) {
    }

    protected void addStaticFields(RedirectView view, Object command) {
        SpiderChartWrapperBean bean = (SpiderChartWrapperBean) command;
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

        SpiderChartWrapperBean bean = (SpiderChartWrapperBean) command;
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
    private static final int ADD_DELETE_ATTRIBUTES_IDX = 2;
}
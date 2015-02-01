/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.common.util.StringUtil;
import com.zynap.domain.IDomainObject;
import com.zynap.domain.UserSession;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.IAnalysisService;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.populations.PopulationDto;
import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.ReportConstants;
import com.zynap.talentstudio.arenas.IArenaManager;
import com.zynap.talentstudio.web.analysis.AnalysisAttributeWrapperBean;
import com.zynap.talentstudio.web.analysis.picker.PopulationCriteriaBuilder;
import com.zynap.talentstudio.web.analysis.populations.PopulationUtils;
import com.zynap.talentstudio.web.analysis.reports.support.ReportRunner;
import com.zynap.talentstudio.web.analysis.reports.views.RunReportWrapperBean;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.DefaultWizardFormController;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collection;
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
public abstract class RunReportController extends DefaultWizardFormController implements ReportConstants {

    protected RunReportController() {
        setSynchronizeOnSession(true);
    }

    protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

        RunReportWrapperBean runReportWrapperBean = (RunReportWrapperBean) command;

        Map<String, Object> refData = new HashMap<String, Object>();
        // display legend
        Long colId = RequestUtils.getLongParameter(request, BaseReportsWizardController.SELECTED_COLUMN_INDEX, null);
        if (page == DISPLAY_LEGEND_IDX && colId != null) {
            runReportWrapperBean.setSelectedColumn(colId);

            // set fields for javascript to find
            refData.put(BaseReportsWizardController.SHOW_POPUP, Boolean.TRUE);
            refData.put(BaseReportsWizardController.SELECTED_COLUMN_INDEX, colId);
            refData.put(BaseReportsWizardController.COL_ID_PREFIX, request.getParameter(BaseReportsWizardController.COL_ID_PREFIX));
        }

        // load populations and reports that can be used as run options
        UserSession userSession = ZynapWebUtils.getUserSession(request);
        refData.put(REPORT_KEY, getRunnableReports(runReportWrapperBean, userSession));
        final Collection<PopulationDto> runnablePopulations = getRunnablePopulations(runReportWrapperBean, userSession);
        refData.put(ControllerConstants.POPULATIONS, runnablePopulations);
        refData.put("popCount", new Integer(runnablePopulations.size()));
        return refData;
    }

    protected final ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        return null;
    }

    protected void setSelectedPopulation(RunReportWrapperBean wrapperBean) throws TalentStudioException {

        Long[] populationIds = wrapperBean.getPopulationIds();
        final Long defaultPopulationId = wrapperBean.getPopulationId();
        Population merged = PopulationUtils.buildPopulation(populationIds, defaultPopulationId, analysisService);
        if (merged != null) wrapperBean.setPopulation(merged);
    }

    /**
     * Get the populations that can be used with the report being run.
     * The populations returned are dependant on a number of things
     * <ul>
     * <li>If building reports or anywhere in the analysis arena you get all populations private to you and public</li>
     * <li>
     * If running reports in any other arena than the analysis arena you get those populations assigned to your group,
     * public populations with no group, and all of your private populations.
     * </li>
     * </ul>
     *
     * @param wrapperBean
     * @param userSession
     * @throws TalentStudioException
     */
    protected final Collection<PopulationDto> getRunnablePopulations(RunReportWrapperBean wrapperBean, UserSession userSession) throws TalentStudioException {

        final String id = userSession.getCurrentArenaId();
        final String nodeType = wrapperBean.getReport().getPopulationType();
        final Long userId = userSession.getId();

        if (IArenaManager.ANALYSIS_MODULE.equals(id)) {
            return analysisService.findAll(nodeType, userId, null);
        } else {
            final Collection<PopulationDto> results = analysisService.findAllByGroup(nodeType, userId, userSession.getUserGroup());
            // this caters for the fact a default population on a report is not part of the group returned above
            final Population population = wrapperBean.getPopulation();
            Long populationId = population.getId();
            if (populationId != null) {
                PopulationDto extra = new PopulationDto(populationId, population.getLabel(), population.getType(), population.getScope(), population.getDescription());
                if (!results.contains(extra)) {
                    results.add(extra);
                }
            }

            return results;
        }

    }

    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        RunReportWrapperBean wrapper = (RunReportWrapperBean) command;
        if (!wrapper.isDrillDown()) setSelectedPopulation(wrapper);
        if (getTargetPage(request, page) == RUN_REPORT_IDX) {
            try {
                Long userId = ZynapWebUtils.getUserId(request);
                String mockUser = request.getParameter(ParameterConstants.MOCK_USER_ID_PARAM);
                if (mockUser != null) {
                    userId = Long.valueOf(mockUser);
                    wrapper.setLockDown(true);
                } else if (wrapper.isLockDown()) {
                    userId = IDomainObject.ROOT_USER_ID;
                }
                runReport(wrapper, userId, request);
            } catch (Throwable e) {
                String errorMgs = "error.invalid.population.list";
                if (wrapper.getPopulationIds().length < 2) {
                    // ok not a problem with multiple populations
                    errorMgs = "error.running.report";
                }
                errors.rejectValue("populationIds", errorMgs);
                wrapper.setActiveTab(RunReportWrapperBean.RUN_TAB);
                logger.error(e.getMessage(), e);
            }
        }
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        // indicates if report should be run again - applies if we came back using history or if a drill-down report has been selected
        boolean runReport = this.autoRun;

        RunReportWrapperBean wrapperBean = recoverFormBackingObject(request);
        if (wrapperBean != null) {
            runReport = true;
        } else {
            Long reportId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.REPORT_ID);
            final Report report = getReportService().findById(reportId, getReportClass());

            final Long userId = ZynapWebUtils.getUserId(request);
            wrapperBean = createFormBackingObject(report, userId);

            final String displayArenaId = request.getParameter(ControllerConstants.DISPLAY_CONFIG_KEY);
            if (displayArenaId != null) wrapperBean.setDisplayConfigArena(displayArenaId);
        }

        // set the attribute labels
        final List<AnalysisAttributeWrapperBean> attributeWrapperBeanList = wrapperBean.getSettableColumns();
        builder.setAttributeLabels(wrapperBean.getReport().getPopulationType(), attributeWrapperBeanList, null);

        // check for drill-down report
        String originalPopulationId = request.getParameter(ORIGINAL_POPULATION_ID);
        if (originalPopulationId != null) {

            runReport = true;

            Long[] populationIds = StringUtil.convertToLongArray(StringUtils.commaDelimitedListToStringArray(originalPopulationId));

            Population originalPopulation = PopulationUtils.buildPopulation(populationIds, populationIds[0], analysisService);

            //final Population originalPopulation = (Population) analysisService.findById(new Long(originalPopulationId));
            // from the request parameters build a population that will give us the people involved in the slice
            // look for a request determining whether we are coming from a chart report, we need a different type of population for this!
            String chartType = RequestUtils.getStringParameter(request, CHART_REPORT_PARAM, null);
            if (chartType != null) {
                originalPopulation = buildChartDrillDown(request, wrapperBean, originalPopulation);
            } else {
                originalPopulation = buildDrillDownPopulation(request, wrapperBean, originalPopulation);
            }
            // set population
            wrapperBean.setOriginalPopulation(originalPopulation);
            wrapperBean.setPopulationIds(populationIds);
        }

        if (runReport) {
            // ensures that when coming back from a CSV export that the user sees the run tab not the results tab
            if (!RunReportWrapperBean.RUN_TAB.equals(wrapperBean.getActiveTab())) wrapperBean.setActiveTab(RunReportWrapperBean.RESULTS_TAB);
            Long userId = ZynapWebUtils.getUserId(request);
            String mockUser = request.getParameter(ParameterConstants.MOCK_USER_ID_PARAM);
            if (mockUser != null) {
                userId = Long.valueOf(mockUser);
                wrapperBean.setLockDown(true);
            } else if (wrapperBean.isLockDown()) {
                // catering for history back
                userId = IDomainObject.ROOT_USER_ID;
            }
            runReport(wrapperBean, userId, request);
        }

        return wrapperBean;
    }

    protected Population buildChartDrillDown(HttpServletRequest request, RunReportWrapperBean wrapperBean, Population originalPopulation) throws Exception {

        String populationPerson = request.getParameter(ParameterConstants.POPULATION_PERSON_ID);
        String chartDataId = request.getParameter(ReportConstants.CHART_DATA_ID);

        String label = request.getParameter(HORIZONTAL_ATTR_LABEL);
        String key = request.getParameter(ReportConstants.CHART_ATTR_VALUE);

        Population population = PopulationUtils.buildChartDrillDown(request, populationEngine, originalPopulation);
        if (populationPerson != null) {
            wrapperBean.setPopulationPersonId(Long.valueOf(populationPerson));
        }

        wrapperBean.setChartId(chartDataId);
        wrapperBean.setHorizontalCriteriaLabel(label);
        wrapperBean.setHorizontalCriteriaValue(key);
        return population;
    }

    protected Population buildDrillDownPopulation(HttpServletRequest request, RunReportWrapperBean wrapperBean, Population originalPopulation) throws Exception {

        final String horizontalLabel = RequestUtils.getRequiredStringParameter(request, HORIZONTAL_ATTR_LABEL);

        // horizontal attribute is required
        final String horizontalAttribute = RequestUtils.getRequiredStringParameter(request, HORIZONTAL_ATTR);

        // value will be blank if drilling down from N/A option
        final String horizontalValue = request.getParameter(HORIZONTAL_ATTR_VALUE);

        // we need to bracket the original population so the db sees this as a group of queries, to be determined before the extras.
        if (StringUtils.hasText(horizontalAttribute)) {
            originalPopulation.wrapCriteria();
        }

        // once we know the original population has been ( ) we can add our 'and' clauses
        PopulationUtils.addCriteria(originalPopulation, horizontalAttribute, horizontalValue, horizontalLabel);

        wrapperBean.setHorizontalCriteria(horizontalAttribute);
        wrapperBean.setHorizontalCriteriaLabel(horizontalLabel);
        wrapperBean.setHorizontalCriteriaValue(horizontalValue);
        // vertical is only supplied if drilling down from cross tab report
        final String verticalLabel = request.getParameter(VERTICAL_ATTR_LABEL);
        if (StringUtils.hasText(verticalLabel)) {

            // must have attribute but value will be blank if drilling down from N/A option
            final String verticalAttribute = RequestUtils.getRequiredStringParameter(request, VERTICAL_ATTR);
            final String verticalValue = request.getParameter(VERTICAL_ATTR_VALUE);
            PopulationUtils.addCriteria(originalPopulation, verticalAttribute, verticalValue, verticalLabel);

            wrapperBean.setVerticalCriteriaLabel(verticalLabel);
            wrapperBean.setVerticalCriteriaValue(verticalValue);
            wrapperBean.setVerticalCriteria(verticalAttribute);
        }

        return originalPopulation;
    }

    public abstract Class getReportClass();

    protected abstract void runReport(RunReportWrapperBean wrapperBean, Long userId, HttpServletRequest request) throws TalentStudioException;

    /**
     * Recover form backing object from request.
     *
     * @param request
     * @return subclass of RunReportWrapperBean
     */
    protected abstract RunReportWrapperBean recoverFormBackingObject(HttpServletRequest request);

    /**
     * Create form backing object.
     *
     * @param report
     * @param userId
     * @return subclass of RunReportWrapperBean
     */
    protected abstract RunReportWrapperBean createFormBackingObject(Report report, Long userId) throws TalentStudioException;

    /**
     * Get the other reports that can be run instead of the current one (will be reports of the same type
     * - eg: if the report originally run was a public crosstab using a position-based population then the other reports will be public, crosstab and position-based as well.
     *
     * @param reportWrapperBean
     * @param userSession
     */
    protected abstract Collection getRunnableReports(RunReportWrapperBean reportWrapperBean, UserSession userSession);

    public IReportService getReportService() {
        return reportService;
    }

    public void setReportService(IReportService reportService) {
        this.reportService = reportService;
    }

    public void setAnalysisService(IAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    public void setAutoRun(boolean autoRun) {
        this.autoRun = autoRun;
    }

    public void setReportRunner(ReportRunner reportRunner) {
        this.reportRunner = reportRunner;
    }

    public void setBarChartTypes(List barChartTypes) {
        this.barChartTypes = barChartTypes;
    }

    public void setBuilder(PopulationCriteriaBuilder builder) {
        this.builder = builder;
    }

    public void setPopulationEngine(IPopulationEngine populationEngine) {
        this.populationEngine = populationEngine;
    }

    private IReportService reportService;
    protected IAnalysisService analysisService;
    protected ReportRunner reportRunner;
    private PopulationCriteriaBuilder builder;
    protected IPopulationEngine populationEngine;

    /**
     * Config parameter that indicates if report should be run automatically.
     * <br/> defaults to false.
     */
    private boolean autoRun = false;

    protected List barChartTypes;

    protected static final int RUN_REPORT_IDX = 0;
    protected static final int DISPLAY_LEGEND_IDX = 1;

    protected static final int PIE_CHART_IDX = 2;
    protected static final int BAR_CHART_OPTIONS_IDX = 3;
    protected static final int BAR_CHART_OPTIONS_SELECTED = 4;

    protected static final String REPORT_KEY = "reports";
    protected static final String CHART_TAB = "charts";

    public static final String CHART_TYPES = "chartTypes";
}

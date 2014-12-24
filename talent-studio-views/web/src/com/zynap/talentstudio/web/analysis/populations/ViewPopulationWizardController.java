/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.populations;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.TabularReport;
import com.zynap.talentstudio.common.AccessType;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.web.analysis.picker.AnalysisAttributeCollection;
import com.zynap.talentstudio.web.analysis.reports.support.ReportRunner;
import com.zynap.talentstudio.web.analysis.reports.views.RunReportWrapperBean;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.dao.DataAccessException;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;

import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class ViewPopulationWizardController extends BasePopulationWizardController {

    public ViewPopulationWizardController() {
        super();

    }

    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

        if (getTargetPage(request, page) == PREVIEW_PAGE) {
            PopulationWrapperBean wrapper = (PopulationWrapperBean) command;
            final Long userId = ZynapWebUtils.getUserId(request);

            try {
                runPreviewReport(wrapper, userId);
            } catch (DataAccessException e) {
                errors.reject("error.invalid.population", "The population is not valid and cannot be run.");
            }
        }
    }

    protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        PopulationWrapperBean wrapper = (PopulationWrapperBean) command;
        return referenceDataForViewCriteria(wrapper);
    }

    public Object formBackingObject(HttpServletRequest request) throws Exception {

        PopulationWrapperBean populationWrapperBean = (PopulationWrapperBean) super.recoverFormBackingObject(request);
        if (populationWrapperBean == null) {
            Long populationId = getPopulationId(request);
            Population population = (Population) analysisService.findById(populationId);
            final AnalysisAttributeCollection analysisAttributeCollection = builder.buildCollection();
            population.getGroups().size();
            populationWrapperBean = new PopulationWrapperBean(population, analysisAttributeCollection);
            setNodeLabelInCriteria(populationWrapperBean);
            builder.setAttributeLabels(population.getType(), populationWrapperBean.getPopulationCriterias(), null);
        }

        return populationWrapperBean;
    }

    public void setPopulationEngine(IPopulationEngine populationEngine) {
        this.populationEngine = populationEngine;
    }

    public IPopulationEngine getPopulationEngine() {
        return populationEngine;
    }

    /**
     * Run a preview report to show the artefacts that match the population criteria.
     * <br/> Only applies to simple populations of type position or subject, not to association populations or
     *
     * @param wrapper The PopulationWrapperBean
     * @param userId  The user id
     * @throws TalentStudioException
     */
    private void runPreviewReport(PopulationWrapperBean wrapper, final Long userId) throws TalentStudioException {

        RunReportWrapperBean runReportWrapperBean = wrapper.getResultsetPreview();

        if (runReportWrapperBean == null) {
            final Population population = wrapper.getPopulation();
            Report report = getPreviewReport(population);
            reportService.compileReportDesign(report);
            runReportWrapperBean = new RunReportWrapperBean(report);
            runReportWrapperBean.setOrderBy(report.getColumns().get(0).getAttributeName());
        }

        reportRunner.run(runReportWrapperBean, userId);
        wrapper.setResultsetPreview(runReportWrapperBean);

    }

    /**
     * Build report for preview purposes.
     * <br/> Returns a report with only the label selected as a column.
     *
     * @param population
     * @return Report
     */
    private Report getPreviewReport(Population population) {

        final String populationType = population.getType();

        Report report = new TabularReport("Preview " + population.getLabel(), "some description", AccessType.PUBLIC_ACCESS.toString());
        report.setDefaultPopulation(population);
        report.setReportType(Report.TABULAR_REPORT);

        report.setPopulationType(populationType);

        final String attributeName;
        if (IPopulationEngine.P_POS_TYPE_.equals(populationType)) {
            attributeName = AnalysisAttributeHelper.POSITION_TITLE_ATTR;
        } else {
            attributeName = AnalysisAttributeHelper.PERSON_FULL_NAME_ATTR;
        }

        report.addColumn(new Column("Label", attributeName, new Integer(0), DynamicAttribute.DA_TYPE_TEXTFIELD));

        return report;
    }

    public void setReportService(IReportService reportService) {
        this.reportService = reportService;
    }

    public void setReportRunner(ReportRunner reportRunner) {
        this.reportRunner = reportRunner;
    }

    private IReportService reportService;

    private IPopulationEngine populationEngine;
    private ReportRunner reportRunner;

    private static final int PREVIEW_PAGE = 1;
}

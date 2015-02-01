/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.talentstudio.analysis.reports.AppraisalSummaryReport;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.talentstudio.web.analysis.picker.QueDefinitionTreeBuilder;
import com.zynap.talentstudio.web.analysis.picker.AnalysisAttributeBranch;
import com.zynap.talentstudio.performance.PerformanceReview;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.ArrayList;


/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class EditAppraisalWizardController extends AddAppraisalReportWizardController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        Long reportId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.REPORT_ID);
        AppraisalSummaryReport report = (AppraisalSummaryReport) reportService.findById(reportId);
        final QuestionnaireDefinition definition = performanceReviewService.findPerformanceDefinition(report.getAppraisalId());

        final AppraisalReportWrapperBean bean = new AppraisalReportWrapperBean(report);

        final PerformanceReview review = report.getPerformanceReview();
        final Long appraisalId = review.getId();
        bean.setSelectedReview(review);

        QueDefinitionTreeBuilder treeBuilder = new QueDefinitionTreeBuilder();
        treeBuilder.setAttributeTypes(Arrays.asList(DynamicAttribute.DA_TYPE_NUMBER, DynamicAttribute.DA_TYPE_SUM));
        AnalysisAttributeBranch root = new AnalysisAttributeBranch(appraisalId.toString(), review.getLabel(), "");
        treeBuilder.buildQuestionnaireBranch(root, definition, null);
        List<AnalysisAttributeBranch> branches = new ArrayList<AnalysisAttributeBranch>();
        branches.add(root);
        bean.setTree(branches);
        return bean;
    }

    protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        Map<String, Object> refdata = new HashMap<String, Object>();
        refdata.put("edit", Boolean.TRUE);
        return refdata;
    }

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        AppraisalReportWrapperBean bean = (AppraisalReportWrapperBean) command;
        final Report modifiedReport = bean.getModifiedReport();
        reportService.update(modifiedReport);
        return new ModelAndView(new ZynapRedirectView(getSuccessView(), ParameterConstants.REPORT_ID, modifiedReport.getId()));
    }

    protected ModelAndView processCancelInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {
        RedirectView view = new ZynapRedirectView(getCancelView(), ParameterConstants.REPORT_ID, ((AppraisalReportWrapperBean) command).getReportId());
        return new ModelAndView(view);
    }
}
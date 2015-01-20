/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.talentstudio.analysis.reports.AppraisalSummaryReport;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.performance.IPerformanceReviewService;
import com.zynap.talentstudio.performance.PerformanceReview;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.web.analysis.picker.AnalysisAttributeBranch;
import com.zynap.talentstudio.web.analysis.picker.QueDefinitionTreeBuilder;
import com.zynap.talentstudio.web.common.DefaultWizardFormController;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Arrays;
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
public class AddAppraisalReportWizardController extends DefaultWizardFormController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        final AppraisalReportWrapperBean reportWrapperBean = new AppraisalReportWrapperBean(new AppraisalSummaryReport());
        reportWrapperBean.addColumn(new Column());
        return reportWrapperBean;
    }

    protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        Map<String, Object> refData = new HashMap<String, Object>();
        switch (page) {
            case SELECT_APPRAISAL_IDX:
                final List<PerformanceReview> reviews = performanceReviewService.findAll();
                AppraisalReportWrapperBean bean = (AppraisalReportWrapperBean) command;
                filterReviews(reviews);
                bean.setAppraisals(reviews);
                break;
        }
        return refData;
    }

    protected void filterReviews(List<PerformanceReview> reviews) {
        CollectionUtils.filter(reviews, new Predicate() {
            public boolean evaluate(Object object) {
                PerformanceReview review = (PerformanceReview) object;
                final QuestionnaireWorkflow workflow = review.getEvaluatorWorkflow();
                final QuestionnaireDefinition definition = workflow.getQuestionnaireDefinition();
                return !filter(definition.getDynamicAttributes()).isEmpty();
            }
        });
    }


    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        AppraisalReportWrapperBean bean = (AppraisalReportWrapperBean) command;

        validate(request, command, errors, page);
        // do not process if there are errros
        if(errors.hasErrors()) return;
        
        final int targetPage = getTargetPage(request, page);
        switch (targetPage) {
            case SHOW_COLUMN_PAGE:
                // get the questionnaireDefinition for the questions
                final Long appraisalId = bean.getAppraisalId();
                final QuestionnaireDefinition definition = performanceReviewService.findPerformanceDefinition(appraisalId);
                
                QueDefinitionTreeBuilder treeBuilder = new QueDefinitionTreeBuilder();
                treeBuilder.setAttributeTypes(Arrays.asList(DynamicAttribute.DA_TYPE_NUMBER, DynamicAttribute.DA_TYPE_SUM));
                AnalysisAttributeBranch root = new AnalysisAttributeBranch(appraisalId.toString(), bean.getSelectedReview().getLabel(), "");
                treeBuilder.buildQuestionnaireBranch(root, definition, null);
                List<AnalysisAttributeBranch> branches = new ArrayList<AnalysisAttributeBranch>();
                branches.add(root);
                bean.setTree(branches);
                break;
            case ADD_COLUMN_IDX:
                bean.addColumn(new Column());
                break;
            case DELETE_COLUMN_IDX:
                removeColumn(request, bean);
                break;
        }
    }

    private void validate(HttpServletRequest request, Object command, Errors errors, int page) {        
        final AppraisalReportValidator validator = (AppraisalReportValidator) getValidator();
        // cannot move off the first page if no appraisal has been selected
        if (page == SELECT_APPRAISAL_IDX) {
            validator.validate(command, errors);
        }
        if(isFinishRequest(request)) {
            validator.validate((AppraisalReportWrapperBean) command, errors, true);
        }
    }

    protected List<DynamicAttribute> filter(List<DynamicAttribute> dynamicAttributes) {
        List<DynamicAttribute> filtered = new ArrayList<DynamicAttribute>();
        for (DynamicAttribute dynamicAttribute : dynamicAttributes) {
            if(dynamicAttribute.isNumericType() || dynamicAttribute.isSumType()) {
                filtered.add(dynamicAttribute);
            }
        }
        return filtered;
    }

    protected ModelAndView processFinishInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {
        AppraisalReportWrapperBean bean = (AppraisalReportWrapperBean) command;
        final Report modifiedReport = bean.getModifiedReport();
        modifiedReport.setUserId(ZynapWebUtils.getUserId(request));
        reportService.create(modifiedReport);
        return new ModelAndView(new ZynapRedirectView(getSuccessView(), ParameterConstants.REPORT_ID, modifiedReport.getId()));
    }

    protected final void removeColumn(HttpServletRequest request, AppraisalReportWrapperBean bean) {
        int index = RequestUtils.getIntParameter(request, SELECTED_COLUMN_INDEX, -1);
        if (index != -1) {
            bean.removeColumn(index);
        }
    }

    public void setPerformanceReviewService(IPerformanceReviewService performanceReviewService) {
        this.performanceReviewService = performanceReviewService;
    }

    public void setReportService(IReportService reportService) {
        this.reportService = reportService;
    }

    public static final int SELECT_APPRAISAL_IDX = 0;
    public static final int SHOW_COLUMN_PAGE = 1;
    public static final int ADD_COLUMN_IDX = 2;
    public static final int DELETE_COLUMN_IDX = 3;

    public static String APPRAISAL_REF_KEY = "appraisals";
    public static final String SELECTED_COLUMN_INDEX = "selectedColumnIndex";

    IPerformanceReviewService performanceReviewService;
    protected IReportService reportService;
}
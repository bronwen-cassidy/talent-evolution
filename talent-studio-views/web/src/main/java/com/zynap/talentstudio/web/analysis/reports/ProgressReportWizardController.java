/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.talentstudio.analysis.IAnalysisService;
import com.zynap.talentstudio.analysis.populations.PopulationDto;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.ProgressReport;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.common.AccessType;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.questionnaires.DefinitionDTO;
import com.zynap.talentstudio.questionnaires.IQueDefinitionService;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.web.analysis.picker.AnalysisAttributeBranch;
import com.zynap.talentstudio.web.analysis.picker.QueDefinitionTreeBuilder;
import com.zynap.talentstudio.web.common.DefaultWizardFormController;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class ProgressReportWizardController extends DefaultWizardFormController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        Long reportId = RequestUtils.getLongParameter(request, "id", null);

        ProgressReportWrapperBean wrapper;
        if (reportId != null) {

            ProgressReport report = (ProgressReport) reportService.findById(reportId);
            report.getMenuItems().size();
            wrapper = new ProgressReportWrapperBean(report);
            setInitialPage(SELECTED_DEFINITION_IDX);
            buildQuestionTree(wrapper, report.getQuestionnaireDefinition());
        } else {
            wrapper = new ProgressReportWrapperBean(new ProgressReport());
        }

        return wrapper;
    }

    protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        Map<String, Object> refData = new HashMap<String, Object>();
        ProgressReportWrapperBean bean = (ProgressReportWrapperBean) command;
        refData.put("add", new Boolean(bean.isAdding()));

        switch (page) {
            case ADD_START_PAGE_IDX:
                List<DefinitionDTO> questionnaireDefinitions = questionnaireDefinitionService.listDefinitions();
                refData.put(QUESTIONNAIRE_DEFINITIONS, questionnaireDefinitions);
                break;
            // all other pages will need the population information 
            default:
                Collection<PopulationDto> populations = populationService.findAll("S", ZynapWebUtils.getUserId(request), "");
                refData.put(POPULATIONS, populations);
                break;
        }

        return refData;
    }

    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        boolean finished = isFinishRequest(request);
        if (finished) return;

        ProgressReportWrapperBean bean = (ProgressReportWrapperBean) command;
        if(page > 0) {
            bean.setLastLineItem(RequestUtils.getBooleanParameter(request, "lastLineItem", false));
        }

        int targetPage = getTargetPage(request, page);

        switch (targetPage) {
            case SELECTED_DEFINITION_IDX:
                Long queDefinitionId = bean.getQuestionnaireDefinitionId();
                if (queDefinitionId == null) {
                    break;
                }
                // if adding we always need to clear
                if (!bean.getQuestionnaireWorkflows().isEmpty()) {
                    //check the definition ids
                    QuestionnaireWorkflow workflow = bean.getQuestionnaireWorkflows().iterator().next();
                    if (!queDefinitionId.equals(workflow.getQuestionnaireDefinition().getId())) {
                        bean.clearCollections();
                    }
                }
                QuestionnaireDefinition definition = questionnaireDefinitionService.findDefinition(queDefinitionId);
                buildQuestionTree(bean, definition);
                break;
            case ADD_ATTRIBUTE_IDX:
                bean.addColumn(new Column());
                break;
            case REMOVE_ATTRIBUTE_IDX:
                removeColumn(request, bean);
                break;
            case ADD_WORKFLOW_IDX:
                bean.addWorkflow();
                break;
            case REMOVE_WORKFLOW_IDX:
                removeWorkflow(request, bean);
                break;
        }
    }

    private void buildQuestionTree(ProgressReportWrapperBean bean, QuestionnaireDefinition definition) {
        Set<QuestionnaireWorkflow> questionnaireWorkflows = definition.getQuestionnaireWorkflows();
        bean.setQuestionnaireWorkflows(questionnaireWorkflows);
        bean.setQuestionnaireDefinition(definition);

        QueDefinitionTreeBuilder treeBuilder = new QueDefinitionTreeBuilder();
        AnalysisAttributeBranch root = new AnalysisAttributeBranch(definition.getId().toString(), definition.getLabel(), "");
        treeBuilder.buildQuestionnaireBranch(root, definition, null);
        List<AnalysisAttributeBranch> branches = new ArrayList<AnalysisAttributeBranch>();
        branches.add(root);
        bean.setTree(branches);
    }

    @Override
    protected void validatePage(Object command, Errors errors, int page, boolean finish) {
        ProgressReportValidator validator = (ProgressReportValidator) getValidator();
        switch (page) {
            case ADD_START_PAGE_IDX:
                validator.validateDefinition(errors);
                break;
        }
        if (finish && page > 4) {
            validator.validate(command, errors);
        }
    }

    protected final void removeColumn(HttpServletRequest request, ProgressReportWrapperBean bean) {
        int index = RequestUtils.getIntParameter(request, SELECTED_COLUMN_INDEX, -1);
        if (index != -1) {
            bean.removeColumn(index);
        }
    }

    protected final void removeWorkflow(HttpServletRequest request, ProgressReportWrapperBean bean) {
        int index = RequestUtils.getIntParameter(request, SELECTED_WORKFLOW_INDEX, -1);
        if (index != -1) {
            bean.removeWorkflow(index);
        }
    }

    protected ModelAndView processFinishInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {
        ProgressReportWrapperBean bean = (ProgressReportWrapperBean) command;
        Report modifiedReport = bean.getModifiedReport();
        modifiedReport.setUserId(ZynapWebUtils.getUserId(request));
        modifiedReport.setAccessType(AccessType.PUBLIC_ACCESS.toString());
        modifiedReport.setPopulationType(Node.SUBJECT_UNIT_TYPE_);
        modifiedReport.setPersonal(true);

        boolean adding = modifiedReport.getId() == null;
        if (adding) {
            reportService.create(modifiedReport);
        } else {
            reportService.update(modifiedReport);
        }
        return new ModelAndView(new ZynapRedirectView(getSuccessView(), ParameterConstants.REPORT_ID, modifiedReport.getId()));
    }

    @Override
    protected ModelAndView processCancelInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {
        ProgressReportWrapperBean bean = (ProgressReportWrapperBean) command;
        RedirectView view = new ZynapRedirectView(getCancelView(), ParameterConstants.REPORT_ID, bean.getReport().getId());
        return new ModelAndView(view);
    }

    public void setReportService(IReportService reportService) {
        this.reportService = reportService;
    }

    public void setQuestionnaireDefinitionService(IQueDefinitionService questionnaireDefinitionService) {
        this.questionnaireDefinitionService = questionnaireDefinitionService;
    }

    public void setPopulationService(IAnalysisService populationService) {
        this.populationService = populationService;
    }

    public static final String SELECTED_COLUMN_INDEX = "selectedColumnIndex";
    public static final String SELECTED_WORKFLOW_INDEX = "selectedWfIndex";

    private IReportService reportService;
    private IAnalysisService populationService;
    private IQueDefinitionService questionnaireDefinitionService;

    private static final int ADD_START_PAGE_IDX = 0;
    private static final int SELECTED_DEFINITION_IDX = 1;
    private static final int ADD_ATTRIBUTE_IDX = 2;
    private static final int REMOVE_ATTRIBUTE_IDX = 3;
    private static final int ADD_WORKFLOW_IDX = 4;
    private static final int REMOVE_WORKFLOW_IDX = 5;
    private static final String QUESTIONNAIRE_DEFINITIONS = "questionnaireDefinitions";
    private static final String POPULATIONS = "populations";
}
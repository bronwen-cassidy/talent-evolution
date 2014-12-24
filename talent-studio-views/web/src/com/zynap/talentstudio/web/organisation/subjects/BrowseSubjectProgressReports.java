/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.subjects;

import com.zynap.domain.IDomainObject;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.QuestionAttributeValuesCollection;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.web.analysis.reports.data.FilledReport;
import com.zynap.talentstudio.web.analysis.reports.managers.ProgressReportFiller;
import com.zynap.talentstudio.web.common.DefaultWizardFormController;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;

import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used as a controller to provide the information required to display a subjects
 * portfolio items and questionnaires/appraisals and nothing else.
 *
 * @author bcassidy
 * @version 0.1
 * @since 10-Feb-2011 10:16:13
 */
public class BrowseSubjectProgressReports extends DefaultWizardFormController {

    /**
     * Creates the form backing object for an ajax get request the name of the command in this instance is 'artefact'
     *
     * @param request the request carrying the subject id param
     * @return the subjectWrapperBean a delegate for the Subject object
     * @throws Exception
     */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        Long subjectId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.NODE_ID_PARAM);

        Subject subject = subjectService.findById(subjectId);
        SubjectWrapperBean artefact = new SubjectWrapperBean(subject);

        List<Report> reports = reportService.findProgressReports(subjectId);
        List<Long> nodeIds = new ArrayList<Long>();
        nodeIds.add(subjectId);

        List<SubjectProgressReportWrapper> progressReports = new ArrayList<SubjectProgressReportWrapper>();

        for (Report report : reports) {

            SubjectProgressReportWrapper wrapper = new SubjectProgressReportWrapper();
            List<AnalysisParameter> questionnaireAttributes = report.getQuestionnaireAttributes();
            Map<Long, QuestionAttributeValuesCollection> answers = new HashMap<Long, QuestionAttributeValuesCollection>();

            if (!questionnaireAttributes.isEmpty()) {
                if (personal) {
                    answers = populationEngine.findPersonalQuestionnaireAnswers(questionnaireAttributes, subject, IDomainObject.ROOT_USER_ID);
                } else {
                    answers = populationEngine.findQuestionnaireAnswers(questionnaireAttributes, nodeIds, IDomainObject.ROOT_USER_ID);
                }
            }
            
            FilledReport filledReport = progressReportFiller.fillReport(report, subject, answers);
            wrapper.setReport(report);
            wrapper.setFilledReport(filledReport);
            progressReports.add(wrapper);
        }

        artefact.setProgressReports(progressReports);

        return artefact;
    }

    public Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        String activeTab = RequestUtils.getStringParameter(request, "activeTab", "progress");
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("progressActiveTab", activeTab);
        return model;
    }

    public void setSubjectService(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    public void setReportService(IReportService reportService) {
        this.reportService = reportService;
    }

    public void setPopulationEngine(IPopulationEngine populationEngine) {
        this.populationEngine = populationEngine;
    }

    public void setPersonal(boolean personal) {
        this.personal = personal;
    }

    public void setProgressReportFiller(ProgressReportFiller progressReportFiller) {
        this.progressReportFiller = progressReportFiller;
    }

    protected ISubjectService subjectService;
    private IReportService reportService;
    private IPopulationEngine populationEngine;
    private ProgressReportFiller progressReportFiller;
    private boolean personal;
}
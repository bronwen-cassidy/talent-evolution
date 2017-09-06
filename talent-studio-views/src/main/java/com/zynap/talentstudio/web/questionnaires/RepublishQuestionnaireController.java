package com.zynap.talentstudio.web.questionnaires;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.ProgressReport;
import com.zynap.talentstudio.analysis.reports.ReportWorkflow;
import com.zynap.talentstudio.questionnaires.IQueWorkflowService;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Controller
@RequestMapping("/admin/questionnaires")
public class RepublishQuestionnaireController {


	private IQueWorkflowService questionnaireWorkflowService;
	private IReportService reportService;
	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	public RepublishQuestionnaireController(IQueWorkflowService queWorkflowService, IReportService reportService) {
		this.questionnaireWorkflowService = queWorkflowService;
		this.reportService = reportService;
	}

	@RequestMapping(value = "/republishQuestionnaire.htm", method = RequestMethod.GET)
	public String republishQuestionnaire(HttpServletRequest request, @RequestParam("qId") Long queWorkflowId) {
		try {
			final QuestionnaireWorkflow workflow = questionnaireWorkflowService.findWorkflowById(queWorkflowId);
			QuestionnaireWorkflow newWorkflow = republishNewWorkflow(request, workflow);
			updateProgressReports(newWorkflow);

		} catch (TalentStudioException e) {
			logger.error(e.getMessage(), e);
		}
		return "redirect:/admin/listqueworkflows.htm";
	}

	@RequestMapping(value = "/republishDefQuestionnaire.htm", method = RequestMethod.GET)
	public String republishDefQuestionnaire(HttpServletRequest request, @RequestParam("qId") Long queWorkflowId) {
		QuestionnaireWorkflow workflow = null;
		try {
			workflow = questionnaireWorkflowService.findWorkflowById(queWorkflowId);
			if(workflow.isInfoForm()) {
				QuestionnaireWorkflow newWorkflow = republishNewWorkflow(request, workflow);
				updateProgressReports(newWorkflow);
			}

		} catch (TalentStudioException e) {
			logger.error(e.getMessage(), e);
		}
		return "redirect:/admin/viewquestionnairedefinition.htm?qd_id=" + workflow.getQuestionnaireDefinition().getId() + "&activeTab=questionnaires";
	}

	private void updateProgressReports(QuestionnaireWorkflow newWorkflow) {

		try {
			List<ProgressReport> progressReports = reportService.findProgressReportDefinitions(newWorkflow.getQuestionnaireDefinition().getId());
			for (ProgressReport progressReport : progressReports) {
				ReportWorkflow reportWorkflow = new ReportWorkflow();//.setWorkflow(new QuestionnaireWorkflow(questionnaireWorkflowId));
				reportWorkflow.setWorkflow(new QuestionnaireWorkflow(newWorkflow.getId()));
				reportWorkflow.setLabel(newWorkflow.getLabel());
				progressReport.addReportWorkflow(reportWorkflow);
				reportService.update(progressReport);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private QuestionnaireWorkflow republishNewWorkflow(HttpServletRequest request, QuestionnaireWorkflow workflow) throws TalentStudioException {
		QuestionnaireWorkflow newWorkflow = workflow.copy(ZynapWebUtils.getUserId(request));
		final DateFormat dateInstance = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, request.getLocale());
		String today = dateInstance.format(new Date());
		String parentlabel = StringUtils.hasText(workflow.getParentLabel()) ? workflow.getParentLabel() : workflow.getLabel();

		newWorkflow.setLabel(parentlabel + " - " + today);
		newWorkflow.setParentLabel(parentlabel);
		try {
			questionnaireWorkflowService.create(newWorkflow);
		} catch (Throwable e) {
			logger.debug(e.getMessage(), e);
		}
		questionnaireWorkflowService.startWorkflow(newWorkflow);
		return newWorkflow;
	}
}

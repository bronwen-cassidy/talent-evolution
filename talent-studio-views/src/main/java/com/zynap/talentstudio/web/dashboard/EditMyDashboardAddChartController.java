package com.zynap.talentstudio.web.dashboard;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.reports.ChartReport;
import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.dashboard.Dashboard;
import com.zynap.talentstudio.dashboard.IDashboardService;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.questionnaires.IQueWorkflowService;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflowDTO;
import com.zynap.talentstudio.web.analysis.reports.SeriesChartDashboardItemWrapperBean;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Controller
@RequestMapping ("talentarena/dashboards")
public class EditMyDashboardAddChartController {

	@Autowired
	public EditMyDashboardAddChartController(IQueWorkflowService queWorkflowService, IDashboardService dashboardService,
	                                         IReportService reportService) {
		this.questionnaireWorkflowService = queWorkflowService;
		this.dashboardService = dashboardService;
		this.reportService = reportService;
	}

	@RequestMapping(value = "editmydashboard.htm", method = RequestMethod.GET)
	public String showEditMyDashbaordView(HttpServletRequest request, @RequestParam("uid") Long userId, Model model) {
		List<QuestionnaireWorkflowDTO> workflows = questionnaireWorkflowService.findRepublishableWorkflows(ZynapWebUtils.getUserSession(request).getSubjectId());
		
		model.addAttribute("command", new SeriesChartDashboardItemWrapperBean(userId));
		model.addAttribute("workflows", workflows);
		return "editmydashboard";
	}
	
	@RequestMapping(value = "loadattributes.htm", method = RequestMethod.GET)
	public String loadAttributes(@RequestParam("wfId") Long workflowId, Model model) throws TalentStudioException {
		QuestionnaireWorkflow workflow = questionnaireWorkflowService.findWorkflowById(workflowId);
		
		List<DynamicAttribute> attributes = workflow.getQuestionnaireDefinition().getDynamicAttributes();
		List<DynamicAttribute> filteredAttributes = new ArrayList<>();
		for (DynamicAttribute attribute : attributes) {
			if(!(attribute.isBlogComment() || attribute.isCalculated() || attribute.isDynamic() || attribute.isFunctionType())) {
				filteredAttributes.add(attribute);	
			}
		}
		model.addAttribute("attributes", filteredAttributes);
		return "mydashboardattributes";
	}
	
	@RequestMapping(value = "savemydashboard", method = RequestMethod.POST)
	public String saveDashboard(HttpServletRequest request, @ModelAttribute("command") SeriesChartDashboardItemWrapperBean command) throws TalentStudioException {
		final Report chartReport = command.getModifiedReport();
		reportService.create(chartReport);
		
		final Dashboard modifiedDashboard = command.getModifiedDashboard(new ChartReport(chartReport.getId()));
		
		modifiedDashboard.setPopulation(new Population(Population.DUMMY_POPULATION_ID));
		// 1 save the report
		dashboardService.createOrUpdate(modifiedDashboard, ZynapWebUtils.getUserSession(request).getSubjectId());
		
		return "redirect: tohomepage";
	}


	@ModelAttribute("command")
	public SeriesChartDashboardItemWrapperBean getCommand(HttpServletRequest request){
		return new SeriesChartDashboardItemWrapperBean(ZynapWebUtils.getUserId(request));
	}

	private final IReportService reportService;
	private IQueWorkflowService questionnaireWorkflowService;
	private IDashboardService dashboardService;

}

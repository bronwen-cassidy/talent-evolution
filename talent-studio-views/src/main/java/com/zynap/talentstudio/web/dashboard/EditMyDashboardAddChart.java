package com.zynap.talentstudio.web.dashboard;

import com.zynap.talentstudio.dashboard.IDashboardService;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.questionnaires.IQueWorkflowService;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflowDTO;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

/**
 *
 */
@Controller
@RequestMapping ("talentarena/dashboards")
public class EditMyDashboardAddChart {

	@Autowired
	public EditMyDashboardAddChart(IQueWorkflowService queWorkflowService, IDashboardService dashboardService, ISubjectService subjectService) {
		this.questionnaireWorkflowService = queWorkflowService;
		this.dashboardService = dashboardService;
		this.subjectService = subjectService;
	}

	@RequestMapping(value = "editmydashboard.htm", method = RequestMethod.GET)
	public String showEditMyDashbaordView(HttpServletRequest request, Model model) {
		List<QuestionnaireWorkflowDTO> workflows = questionnaireWorkflowService.findRepublishableWorkflows(ZynapWebUtils.getUser(request).getSubjectId());
		return "editmydashboard";
	}
	
	
	private IQueWorkflowService questionnaireWorkflowService;
	private IDashboardService dashboardService;
	private ISubjectService subjectService;

}

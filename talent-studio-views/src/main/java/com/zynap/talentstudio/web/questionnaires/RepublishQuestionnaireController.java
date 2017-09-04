package com.zynap.talentstudio.web.questionnaires;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.questionnaires.IQueWorkflowService;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.util.FormatterFactory;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 */
@Controller
@RequestMapping("/admin/questionnaires")
public class RepublishQuestionnaireController {
	
	
	private IQueWorkflowService questionnaireWorkflowService;

	@Autowired
	public RepublishQuestionnaireController(IQueWorkflowService queWorkflowService) {
		this.questionnaireWorkflowService = queWorkflowService;
	}

	@RequestMapping(value="/republishQuestionnaire.htm", method = RequestMethod.GET)
	public String republishQuestionnaire(HttpServletRequest request, @RequestParam("qId") Long queWorkflowId) {
		try {
			final QuestionnaireWorkflow workflow = questionnaireWorkflowService.findWorkflowById(queWorkflowId);
			republishNewWorkflow(request, workflow);
			
		} catch (TalentStudioException e) {
			e.printStackTrace();
		}
		return "redirect:/admin/listqueworkflows.htm";
	}
	
	@RequestMapping(value="/republishDefQuestionnaire.htm", method = RequestMethod.GET)
	public String republishDefQuestionnaire(HttpServletRequest request, @RequestParam("qId") Long queWorkflowId) {
		QuestionnaireWorkflow workflow = new QuestionnaireWorkflow();
		try {
			workflow = questionnaireWorkflowService.findWorkflowById(queWorkflowId);
			republishNewWorkflow(request, workflow);
			
		} catch (TalentStudioException e) {
			e.printStackTrace();
		}
		return "redirect:/admin/viewquestionnairedefinition.htm?qd_id=" + workflow.getQuestionnaireDefinition().getId() + "&activeTab=questionnaires";
	}

	public void republishNewWorkflow(HttpServletRequest request, QuestionnaireWorkflow workflow) throws TalentStudioException {
		QuestionnaireWorkflow newWorkflow = workflow.copy(ZynapWebUtils.getUserId(request));
		final DateFormat dateInstance = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, request.getLocale());
		final String today = dateInstance.format(new Date());
		String parentlabel = StringUtils.hasText(workflow.getParentLabel()) ? workflow.getParentLabel() : workflow.getLabel();
		
		newWorkflow.setLabel(parentlabel + " - " + today);
		newWorkflow.setParentLabel(parentlabel);

		questionnaireWorkflowService.create(newWorkflow);
		questionnaireWorkflowService.startWorkflow(newWorkflow);
	}
}

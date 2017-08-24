package com.zynap.talentstudio.web.questionnaires;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.questionnaires.IQueWorkflowService;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.util.FormatterFactory;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

import java.util.Date;

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
			QuestionnaireWorkflow newWorkflow = workflow.copy(ZynapWebUtils.getUserId(request));
			newWorkflow.setLabel(workflow.getLabel() + " - " + 
					FormatterFactory.getDateFormatter(ZynapWebUtils.getUserLocale(request)).formatDateAsString(new Date()));
			
			questionnaireWorkflowService.create(newWorkflow);
			questionnaireWorkflowService.startWorkflow(newWorkflow);
			
		} catch (TalentStudioException e) {
			e.printStackTrace();
		}
		return "redirect:/admin/listqueworkflows.htm";
	}
}

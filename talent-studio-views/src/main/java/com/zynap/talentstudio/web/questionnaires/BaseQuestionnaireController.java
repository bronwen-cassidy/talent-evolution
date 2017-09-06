/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires;

import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.questionnaires.IQuestionnaireService;
import com.zynap.talentstudio.questionnaires.IQueWorkflowService;
import com.zynap.talentstudio.questionnaires.IQueDefinitionService;
import com.zynap.talentstudio.web.controller.ZynapDefaultFormController;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;

import javax.servlet.http.HttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public abstract class BaseQuestionnaireController extends ZynapDefaultFormController {

    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        super.initBinder(request, binder);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }

    public void setQuestionnaireService(IQuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    public void setQuestionnaireDefinitionService(IQueDefinitionService questionnaireDefinitionService) {
        this.questionnaireDefinitionService = questionnaireDefinitionService;
    }

    public IQueWorkflowService getQuestionnaireWorkflowService() {
        return questionnaireWorkflowService;
    }

    public void setQuestionnaireWorkflowService(IQueWorkflowService questionnaireWorkflowService) {
        this.questionnaireWorkflowService = questionnaireWorkflowService;
    }

    public void setQuestionnaireHelper(QuestionnaireHelper questionnaireHelper) {
        this.questionnaireHelper = questionnaireHelper;
    }

	public void setReportService(IReportService reportService) {
		this.reportService = reportService;
	}

	protected IQuestionnaireService questionnaireService;
    protected IQueDefinitionService questionnaireDefinitionService;
    protected IQueWorkflowService questionnaireWorkflowService;
    protected QuestionnaireHelper questionnaireHelper;
    protected IReportService reportService;


}

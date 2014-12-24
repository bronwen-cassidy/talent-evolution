/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires;

import com.zynap.talentstudio.questionnaires.IQueWorkflowService;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflowDTO;
import com.zynap.web.controller.ZynapDefaultFormController;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 24-Jan-2008 11:56:56
 */
public class ListQuestionnaireWorkflowsController extends ZynapDefaultFormController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        List<QuestionnaireWorkflowDTO> allQueWorkflows = new ArrayList<QuestionnaireWorkflowDTO>(questionnaireWorkflowService.findAllQuestionnaireWorkflowDTOs());
        return new QuestionnaireWorkflowListWrapper(allQueWorkflows);
    }

    public void setQuestionnaireWorkflowService(IQueWorkflowService questionnaireWorkflowService) {
        this.questionnaireWorkflowService = questionnaireWorkflowService;
    }

    private IQueWorkflowService questionnaireWorkflowService;
}

/* 
 * Copyright (C)  Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.integration.dto.processors;

import com.zynap.domain.IDomainObject;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.questionnaires.Questionnaire;
import com.zynap.talentstudio.questionnaires.IQuestionnaireService;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.questionnaires.IQueWorkflowService;
import com.zynap.exception.TalentStudioException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class or Interface description.
 *
 * @author syeoh
 * @version 0.1
 * @since 15-Mar-2007 11:17:50
 */
public class QuestionnaireProcessor implements IPostProcessor {

    public void process(IDomainObject domainObject, User user) {

        Questionnaire questionnaire = (Questionnaire) domainObject;
        try {
            // set the user of the questionnaire, this should be the default webservice user
            questionnaire.setUser(user);

            // set the questionnaire workflow
            QuestionnaireWorkflow queWorkflow = (QuestionnaireWorkflow) questionnaireWorkflowService.findById(questionnaire.getQuestionnaireWorkflowId());
            questionnaire.setQuestionnaireWorkflow(queWorkflow);

        } catch (TalentStudioException e) {
            logger.error("An error occured  during post processing new questionnaire with id : " + questionnaire.getId() + "\n Error:" + e.toString());
        }
    }

    public void setQuestionnaireWorkflowService(IQueWorkflowService questionnaireWorkflowService) {
        this.questionnaireWorkflowService = questionnaireWorkflowService;
    }

    private IQueWorkflowService questionnaireWorkflowService;
    private static final Log logger = LogFactory.getLog(QuestionnaireProcessor.class);
}

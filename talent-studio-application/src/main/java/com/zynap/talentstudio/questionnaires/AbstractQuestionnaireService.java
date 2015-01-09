/* 
 * Copyright (C)  Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.questionnaires;

import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.IAnalysisService;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.audit.IAuditDao;
import com.zynap.talentstudio.common.DefaultService;
import com.zynap.talentstudio.common.lookups.ILookupManagerDao;
import com.zynap.talentstudio.workflow.IWorkflowAdapter;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author syeoh
 * @version 0.1
 * @since 08-Mar-2007 16:40:57
 */

public abstract class AbstractQuestionnaireService extends DefaultService implements IQuestionnaireCommon {

    public Collection<User> getParticipants(QuestionnaireWorkflow questionnaireWorkflow) {
        return questionnaireDao.getParticipants(questionnaireWorkflow);
    }

    public boolean isParticipant(Long subjectId, Long questionnaireWorkflowId) {
        return questionnaireDao.isParticipant(subjectId, questionnaireWorkflowId);
    }

    public void setQuestionnaireDao(IQuestionnaireDao questionnaireDao) {
        this.questionnaireDao = questionnaireDao;
    }

    public void setLookupManagerDao(ILookupManagerDao lookupManagerDao) {
        this.lookupManagerDao = lookupManagerDao;
    }

    public void setWorkflowAdapter(IWorkflowAdapter workflowAdapter) {
        this.workflowAdapter = workflowAdapter;
    }

    public void setPopulationEngine(IPopulationEngine populationEngine) {
        this.populationEngine = populationEngine;
    }

    public void setAnalysisService(IAnalysisService analysisService) {
        this.analysisService = analysisService;
    }


    /**
     * Remove questionnaire workflow process.
     * This method just removes the notifications for this workflow from the database.
     * It completes the workflow, set a date_closed field and marks the questionnaires as completed
     *
     * @param questionnaireWorkflow
     * @throws com.zynap.exception.TalentStudioException
     *
     */
    public void closeWorkflowProcess(QuestionnaireWorkflow questionnaireWorkflow) throws TalentStudioException {

        Long questionnaireWorkflowId = questionnaireWorkflow.getId();
        workflowAdapter.closeProcess(questionnaireWorkflowId);
    }

    /**
     * Remove participants.
     *
     * @param questionnaireWorkflow
     */
    protected void removeParticipants(QuestionnaireWorkflow questionnaireWorkflow) {
        if (!QuestionnaireWorkflow.TYPE_INFO_FORM.equals(questionnaireWorkflow.getWorkflowType())) {
            questionnaireDao.removeAllParticipants(questionnaireWorkflow);
        }
    }

    /**
     * Build map of attributes to be added to workflow process.
     *
     * @param id
     * @param label
     * @param username
     * @param processName
     * @param expiryDate  optional
     * @return Map
     */
    protected Map<String, Serializable> buildWorkflowAttributeMap(final String id, final String label, final String username, final String processName, final Date expiryDate) {

        final Map<String, Serializable> attributes = new HashMap<String, Serializable>();
        attributes.put(IWorkflowAdapter.WORK_FLOW_ID_ATTRIBUTE_NAME, id);
        attributes.put(IWorkflowAdapter.WORK_FLOW_NAME_ATTRIBUTE_NAME, label);
        attributes.put(IWorkflowAdapter.LAUNCHER_ATTRIBUTE_NAME, username);
        attributes.put(IWorkflowAdapter.DETAIL_PROCESS_TYPE_ATTRIBUTE_NAME, processName);

        if (expiryDate != null) {
            attributes.put(IWorkflowAdapter.EXPIRE_DATE_ATTRIBUTE_NAME, expiryDate);
        }

        return attributes;
    }

    public void setAuditDao(IAuditDao auditDao) {
        this.auditDao = auditDao;
    }

    protected IQuestionnaireDao questionnaireDao;
    protected ILookupManagerDao lookupManagerDao;
    protected IWorkflowAdapter workflowAdapter;
    protected IPopulationEngine populationEngine;
    protected IAnalysisService analysisService;
    protected IAuditDao auditDao;
}

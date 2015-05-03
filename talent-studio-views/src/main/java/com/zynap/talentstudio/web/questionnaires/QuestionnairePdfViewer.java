/* 
 * Copyright (c) TalentScope Ltd. 2008
 * All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.analysis.populations.HibernatePopulationEngine;
import com.zynap.talentstudio.display.IDisplayConfigService;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.organisation.positions.IPositionService;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.display.support.ArtefactViewQuestionnaireHelper;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ResponseUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.workflow.WorkflowConstants;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class or Interface description.
 *
 * @author taulant.bajraktari
 * @since 08-Sep-2008 12:42:15
 */
public class QuestionnairePdfViewer implements Controller {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        final Long questionnaireId = RequestUtils.getLongParameter(request, ParameterConstants.QUESTIONNAIRE_ID);
        final Long workflowId = RequestUtils.getLongParameter(request, WorkflowConstants.WORKFLOW_ID_PARAM_PREFIX);
        final Long subjectId = RequestUtils.getLongParameter(request, ParameterConstants.NODE_ID_PARAM);
        final User user = ZynapWebUtils.getUser(request);

        QuestionnaireWrapper questionnaireWrapper = questionnaireHelper.getQuestionnaireWrapper(questionnaireId, workflowId, user, subjectId, false);
        questionnaireWrapper.setMyPortfolio(mySummary);

        
        ArtefactViewQuestionnaireHelper displayHelper = getDisplayHelper(user);
        
        byte[] pdf = questionnairePdfMakerHelper.createPdfDocument(user, questionnaireWrapper, messageSource, request, subjectService,
                displayConfigService, displayHelper, mySummary);
        
        String pdfLabel = questionnaireWrapper.getQuestionnaireLabel()  + " - " + questionnairePdfMakerHelper.getSubject().getLabel();
        final String fileName = pdfLabel + ".pdf";

        if(pdf.length < 1) {
            return null;
        }
        
        ResponseUtils.writeToResponse(response, request, fileName, pdf, true);
        
        return null;
    }

    public ArtefactViewQuestionnaireHelper getDisplayHelper(User user) {
        ArtefactViewQuestionnaireHelper artefactViewQuestionnaireHelper = new ArtefactViewQuestionnaireHelper(populationEngine);
        artefactViewQuestionnaireHelper.setPositionService(positionService);
        artefactViewQuestionnaireHelper.setSubjectService(subjectService);
        artefactViewQuestionnaireHelper.setUserId(user.getId());
        artefactViewQuestionnaireHelper.setDynamicAttributeService(dynamicAttributeService);
        return artefactViewQuestionnaireHelper;
    }

    public void setQuestionnaireHelper(QuestionnaireHelper questionnaireHelper) {
        this.questionnaireHelper = questionnaireHelper;
    }

    public void setQuestionnairePdfMakerHelper(QuestionnairePdfMakerHelper questionnairePdfMakerHelper) {
        this.questionnairePdfMakerHelper = questionnairePdfMakerHelper;
    }

    public void setMessageSource(org.springframework.context.support.ReloadableResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setSubjectService(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    public void setDisplayConfigService(com.zynap.talentstudio.display.IDisplayConfigService displayConfigService) {
        this.displayConfigService = displayConfigService;
    }

    public void setDynamicAttributeService(com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService dynamicAttributeService) {
        this.dynamicAttributeService = dynamicAttributeService;
    }

    public void setPositionService(com.zynap.talentstudio.organisation.positions.IPositionService positionService) {
        this.positionService = positionService;
    }

    public void setPopulationEngine(com.zynap.talentstudio.analysis.populations.HibernatePopulationEngine populationEngine) {
        this.populationEngine = populationEngine;
    }

    public void setMySummary(boolean mySummary) {
        this.mySummary = mySummary;
    }

    protected QuestionnaireHelper questionnaireHelper;
    protected QuestionnairePdfMakerHelper questionnairePdfMakerHelper;
    protected ReloadableResourceBundleMessageSource messageSource;
    protected ISubjectService subjectService;
    protected IDisplayConfigService displayConfigService;
    protected IDynamicAttributeService dynamicAttributeService;
    protected IPositionService positionService;
    protected HibernatePopulationEngine populationEngine;
    protected boolean mySummary;
}

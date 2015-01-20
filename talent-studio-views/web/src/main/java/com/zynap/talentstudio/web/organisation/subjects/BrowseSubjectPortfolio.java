/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.subjects;

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.organisation.portfolio.PortfolioItem;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.questionnaires.IQuestionnaireService;
import com.zynap.talentstudio.questionnaires.QuestionnaireDTO;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.web.common.DefaultWizardFormController;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.portfolio.MyPortfolioHelper;
import com.zynap.talentstudio.web.portfolio.PortfolioItemHelper;
import com.zynap.talentstudio.web.utils.RequestUtils;

import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used as a controller to provide the information required to display a subjects
 * portfolio items and questionnaires/appraisals and nothing else.
 *
 * @author bcassidy
 * @version 0.1
 * @since 14-Jun-2010 10:16:13
 */
public class BrowseSubjectPortfolio extends DefaultWizardFormController {

    /**
     * Creates the form backing object for an ajax get request the name of the command in this instance is 'artefact'
     *
     * @param request the request carrying the subject id param
     * @return the subjectWrapperBean a delegate for the Subject object
     * @throws Exception
     */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        final UserSession userSession = UserSessionFactory.getUserSession();
        Long subjectId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.NODE_ID_PARAM);

        Subject subject = subjectService.findById(subjectId);
        SubjectWrapperBean artefact = new SubjectWrapperBean(subject);
        final Collection<QuestionnaireDTO> questionnaires = questionnaireService.getPortfolioQuestionnaires(subjectId);

        assignQuestionnaires(artefact, questionnaires);
        Collection<PortfolioItem> sortedItems = PortfolioItemHelper.filterPortfolioItems(userSession, userSession.getId(), subject.getPortfolioItems());

        artefact.setPortfolioItems(sortedItems);
        return artefact;
    }

    public Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        String activeTab = RequestUtils.getStringParameter(request, "activeTab", "portfolio");
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("portfolioActiveTab", activeTab);
        return model;
    }

    protected void assignQuestionnaires(SubjectWrapperBean subjectWrapper, Collection<QuestionnaireDTO> questionnaires) {
        MyPortfolioHelper.assignQuestionnaires(subjectWrapper, questionnaires);
    }

    public void setSubjectService(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    public void setQuestionnaireService(IQuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    protected ISubjectService subjectService;
    protected IQuestionnaireService questionnaireService;
}

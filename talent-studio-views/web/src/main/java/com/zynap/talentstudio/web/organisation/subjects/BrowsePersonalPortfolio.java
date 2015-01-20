/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.subjects;

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.organisation.portfolio.PortfolioItem;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.questionnaires.QuestionnaireDTO;
import com.zynap.talentstudio.web.portfolio.PortfolioItemHelper;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import javax.servlet.http.HttpServletRequest;

import java.util.Collection;

/**
 * Controller that returns the portfolio items and questionnaires/appraisals information for the current logged in user.
 * This is a person's own 'stuff' and has been filtered with it's security attributes hence returning only those items the
 * logged in user is allowed to see. 
 *
 * @author bcassidy
 * @version 0.1
 * @since 14-Jun-2010 10:16:13
 */
public class BrowsePersonalPortfolio extends BrowseSubjectPortfolio {

    /**
     * Creates the form backing object for an ajax get request the name of the command in this instance is 'artefact'
     *
     * @param request the request carrying the subject id param
     * @return the subjectWrapperBean a delegate for the Subject object
     * @throws Exception
     */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        final UserSession userSession = ZynapWebUtils.getUserSession(request);

        final Long userId = userSession.getId();
        try {
            Subject subject = subjectService.findByUserId(userId);
            SubjectWrapperBean artefact = new SubjectWrapperBean(subject);
            final Collection<QuestionnaireDTO> questionnaires = questionnaireService.getPersonalPortfolioQuestionnaires(subject.getId(), userSession.getId());
            assignQuestionnaires(artefact, questionnaires);
            Collection<PortfolioItem> sortedItems = PortfolioItemHelper.filterPersonalPortfolioItems(subject);
            artefact.setPortfolioItems(sortedItems);
            return artefact;
        } catch (Exception e) {
            return new SubjectWrapperBean(new Subject());
        }
    }
}
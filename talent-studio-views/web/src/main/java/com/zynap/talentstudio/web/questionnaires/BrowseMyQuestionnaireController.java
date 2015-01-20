/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires;

import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.workflow.WorkflowConstants;
import com.zynap.talentstudio.questionnaires.QuestionnaireDTO;
import com.zynap.domain.admin.User;

import javax.servlet.http.HttpServletRequest;

import java.util.Collection;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 01-Dec-2008 11:55:40
 */
public class BrowseMyQuestionnaireController extends BrowseQuestionnaireController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        final Long questionnaireId = RequestUtils.getLongParameter(request, ParameterConstants.QUESTIONNAIRE_ID);
        final Long workflowId = RequestUtils.getLongParameter(request, WorkflowConstants.WORKFLOW_ID_PARAM_PREFIX);
        final User user = ZynapWebUtils.getUser(request);
        final Long subjectId = ZynapWebUtils.getUserSession(request).getSubjectId();

        QuestionnaireWrapper questionnaireWrapper = questionnaireHelper.getQuestionnaireWrapper(questionnaireId, workflowId, user, subjectId, false);
        questionnaireWrapper.setMyPortfolio(true);

        final Collection<QuestionnaireDTO> questionnaires = questionnaireService.getPersonalPortfolioQuestionnaires(subjectId, user.getId());
        List<QuestionnaireDTO> results = QuestionnaireHelper.sortResults(questionnaires);
        QuestionnaireDTO current = QuestionnaireHelper.findCurrent(questionnaireWrapper.getWorkflowId(), results);
        final BrowseQuestionnaireWrapper wrapper = new BrowseQuestionnaireWrapper(results, questionnaireWrapper, current, true);
        wrapper.setSubjectId(subjectId);
        wrapper.setMyPortfolio(true);        
        return wrapper;
    }
}

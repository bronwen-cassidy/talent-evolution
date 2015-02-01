/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires;

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.questionnaires.IQuestionnaireService;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * Class to make sure that any locked questionnaires are unlocked whenever
 * a person navigates to a url other than the edit*questionnaire urls
 *
 * @author bcassidy
 * @version 0.1
 * @since 07-Feb-2008 10:05:07
 */
public class QuestionnaireInterceptor extends HandlerInterceptorAdapter {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        UserSession userSession = ZynapWebUtils.getUserSession(request);
        // get the logged in user check the url requested is not in the list of targetUrls, if not unlockUsersQuestionnaires
        boolean match = ZynapWebUtils.urlMatches(targetUrls, request);
        if(!match) {
            final Long userId = ZynapWebUtils.getUserId(request);
            questionnaireService.unlockQuestionnaires(userId);
        }
        return true;
    }

    public void setTargetUrls(List targetUrls) {
        this.targetUrls = targetUrls;
    }

    public void setQuestionnaireService(IQuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    private List targetUrls;
    private IQuestionnaireService questionnaireService;
}

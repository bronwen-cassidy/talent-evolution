/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.objectives;

import com.zynap.talentstudio.objectives.IObjectiveService;
import com.zynap.talentstudio.objectives.Objective;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.organisation.subjects.Subject;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 09-Aug-2007 14:08:48
 */
public class ListAssessorsAssessmentsController implements Controller {

    /**
     * Process the request and return a ModelAndView object which the DispatcherServlet
     * will render. A <code>null</code> return value is not an error: It indicates that
     * this object completed request processing itself, thus there is no ModelAndView
     * to render.
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @return a ModelAndView to render, or <code>null</code> if handled directly
     * @throws Exception in case of errors
     */
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<Objective> objectives = objectiveService.findAssessorsObjectives(ZynapWebUtils.getUserId(request));

        List<AssessorFormBean> assessedObjectives = new ArrayList<AssessorFormBean>();

        for(Objective objective : objectives) {
            Subject subject = objective.getObjectiveSet().getSubject();
            AssessorFormBean assessorFormBean = new AssessorFormBean();
            assessorFormBean.setSubject(subject);
            assessorFormBean.setObjective(objective);
            assessedObjectives.add(assessorFormBean);
        }

        Collections.sort(assessedObjectives);

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("assessments", assessedObjectives);
        return new ModelAndView(pageView, "command", model);
    }

    public void setObjectiveService(IObjectiveService objectiveService) {
        this.objectiveService = objectiveService;
    }

    public void setPageView(String pageView) {
        this.pageView = pageView;
    }

    private IObjectiveService objectiveService;
    private String pageView;
}

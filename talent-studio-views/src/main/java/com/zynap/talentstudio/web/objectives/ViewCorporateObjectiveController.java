/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.objectives;

import com.zynap.talentstudio.objectives.IObjectiveService;
import com.zynap.talentstudio.objectives.ObjectiveSet;
import com.zynap.talentstudio.web.utils.RequestUtils;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 16-Mar-2007 09:47:00
 */
public class ViewCorporateObjectiveController implements Controller {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long id = RequestUtils.getRequiredLongParameter(request, "id");
        ObjectiveSet definition = (ObjectiveSet) objectiveService.findById(id);
        
        return new ModelAndView(pageView, "command", definition);
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

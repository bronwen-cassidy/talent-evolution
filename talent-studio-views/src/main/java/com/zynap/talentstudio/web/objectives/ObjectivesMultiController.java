/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.objectives;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.objectives.IObjectiveService;
import com.zynap.talentstudio.objectives.ObjectiveSet;
import com.zynap.talentstudio.objectives.ObjectiveConstants;
import com.zynap.talentstudio.objectives.Objective;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.controller.ZynapMultiActionController;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.talentstudio.web.history.History;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.common.ParameterConstants;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 22-Mar-2007 10:53:30
 */
public class ObjectivesMultiController extends ZynapMultiActionController {

    public ModelAndView deleteCorporateObjectiveSet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        deleteObjectiveSet(request);
        return new ModelAndView(new ZynapRedirectView(CORPORATE_LIST_VIEW));
    }

    public ModelAndView deleteOrganisationObjectiveSet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        deleteObjectiveSet(request);
        return new ModelAndView(new ZynapRedirectView(ORGANISATION_OBJECTIVES_LIST_VIEW));
    }

    public ModelAndView publishCorporateObjectiveSet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long id = RequestUtils.getRequiredLongParameter(request, "id");
        objectiveService.publishObjectives(id);
        return new ModelAndView(new ZynapRedirectView(CORPORATE_LIST_VIEW));
    }

    public ModelAndView approveOrganisationObjectiveSet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long id = RequestUtils.getRequiredLongParameter(request, "id");
        ObjectiveSet objectiveSet = (ObjectiveSet) objectiveService.findById(id);
        objectiveService.approve(objectiveSet, ZynapWebUtils.getUser(request));
        return new ModelAndView(new ZynapRedirectView(ORGANISATION_OBJECTIVES_LIST_VIEW));
    }

    public ModelAndView archiveCorporateObjectiveSet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long id = RequestUtils.getRequiredLongParameter(request, "id");
        objectiveService.expire(id);
        return new ModelAndView(new ZynapRedirectView(CORPORATE_LIST_VIEW));
    }

    public ModelAndView viewOrganisationObjectives(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // find the organisation unit and the objective set
        Long id = RequestUtils.getRequiredLongParameter(request, "id");
        ObjectiveSet objectiveSet = (ObjectiveSet) objectiveService.findById(id);
        ObjectiveSetFormBean bean = new ObjectiveSetFormBean();
        bean.setOrganisationUnit(objectiveSet.getOrganisationUnit());
        bean.setObjectiveSet(objectiveSet);
        
        return new ModelAndView(ORGANISATION_OBJECTIVES_VIEW, "command", bean);
    }

    public ModelAndView archiveSubjectObjectiveSet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String previousUrl = HistoryHelper.getBackURL(request);
        Long id = RequestUtils.getRequiredLongParameter(request, "id");
        objectiveService.expire(id);
        ZynapRedirectView view = new ZynapRedirectView(previousUrl);        
        return new ModelAndView(view);
    }

    public ModelAndView reopenSubjectObjectiveSet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        String previousUrl = HistoryHelper.getBackURL(request);
        Long id = RequestUtils.getRequiredLongParameter(request, "id");
        ObjectiveSet set = (ObjectiveSet) objectiveService.findById(id);
        set.setApproved(false);
        set.setStatus(ObjectiveConstants.STATUS_OPEN);
        set.setActionRequired(ObjectiveConstants.ACTION_REQUIRED_NOACTION);
        set.setActionGroup(ObjectiveConstants.ACTION_GROUP_UNASSIGNED);
        List<Objective> objectiveList = set.getObjectives();
        for (Objective objective : objectiveList) {
            objective.setStatus(ObjectiveConstants.STATUS_OPEN);
        }
        objectiveService.update(set);
        ZynapRedirectView view = new ZynapRedirectView(previousUrl);
        return new ModelAndView(view);
    }

    private void deleteObjectiveSet(HttpServletRequest request) throws TalentStudioException {
        Long id = RequestUtils.getRequiredLongParameter(request, "id");
        Objective objective = objectiveService.findById(id);
        objectiveService.delete(objective);
    }

    public void setObjectiveService(IObjectiveService objectiveService) {
        this.objectiveService = objectiveService;
    }

    private IObjectiveService objectiveService;
    private static final String CORPORATE_LIST_VIEW = "listcorporateobjectives.htm";
    private static final String ORGANISATION_OBJECTIVES_VIEW = "vieworganisationobjectives";
    private static final String ORGANISATION_OBJECTIVES_LIST_VIEW = "listorgunitobjectives.htm";
}

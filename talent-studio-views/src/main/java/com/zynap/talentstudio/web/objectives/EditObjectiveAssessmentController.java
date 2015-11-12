/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.objectives;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.common.lookups.LookupType;
import com.zynap.talentstudio.objectives.IObjectiveService;
import com.zynap.talentstudio.objectives.Objective;
import com.zynap.talentstudio.objectives.ObjectiveAssessment;
import com.zynap.talentstudio.objectives.ObjectiveConstants;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.talentstudio.web.controller.ZynapDefaultFormController;
import com.zynap.exception.TalentStudioException;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 09-Aug-2007 16:42:08
 */
public class EditObjectiveAssessmentController extends ZynapDefaultFormController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        User user = (User) userService.findById(ZynapWebUtils.getUserId(request));
        Long objectiveId = RequestUtils.getRequiredLongParameter(request, ObjectiveConstants.OBJECTIVE_ID);
        Objective objective = objectiveService.findObjective(objectiveId);
        ObjectiveAssessment assessment = objective.findUserAssessment(user.getId());
        LookupType rating = lookupManager.findLookupType(ObjectiveConstants.RATING_LOOKUP_TYPE);
        Subject subject = objective.getObjectiveSet().getSubject();
        AssessmentFormBean bean = new AssessmentFormBean(assessment, rating, user, subject);
        bean.setObjective(objective);
        return bean;
    }

    protected ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {
        AssessmentFormBean assessmentFormBean = (AssessmentFormBean) command;
        ObjectiveAssessment objectiveAssessment = assessmentFormBean.getModifiedAssessment();
        Objective objective = assessmentFormBean.getObjective();
        objectiveAssessment.setObjective(objective);
        try {
            objectiveService.createOrUpdateAssessment(objectiveAssessment);
        } catch (TalentStudioException e) {
            logger.error(e.getMessage(), e);
            errors.reject("unknown.error", e.getMessage());
            return showForm(request, response, errors);
        }
        return new ModelAndView(new ZynapRedirectView(getSuccessView()));
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    public void setObjectiveService(IObjectiveService objectiveService) {
        this.objectiveService = objectiveService;
    }

    public void setLookupManager(ILookupManager lookupManager) {
        this.lookupManager = lookupManager;
    }

    private IUserService userService;
    private IObjectiveService objectiveService;
    private ILookupManager lookupManager;
}

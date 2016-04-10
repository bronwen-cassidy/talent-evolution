/*
 * $Header: ${}
 * $Revision: ${}
 * $Date: 02-Apr-2007
 *
 * Copyright (c) 1999-2006 Bronwen Cassidy.  All rights reserved.
 */
package com.zynap.talentstudio.web.objectives;

import com.zynap.domain.UserSession;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.navigation.ZynapNavigator;
import com.zynap.talentstudio.organisation.IOrganisationUnitService;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.web.common.DefaultWizardFormController;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.objectives.IObjectiveService;
import com.zynap.talentstudio.objectives.ObjectiveSet;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collection;
import java.util.List;

/**
 * Lists business unit / organisation unit objectives.
 *
 * @author bcassidy
 * @version 0.1
 * @since 07-Mar-2007 15:27:59
 */
public class ListOrgUnitObjectivesController extends DefaultWizardFormController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        UserSession session = ZynapWebUtils.getUserSession(request);
        ZynapNavigator navigator = session.getNavigator();
        Long organisationUnitId = navigator.getOrganisationUnitId();
        if (organisationUnitId == null) organisationUnitId = RequestUtils.getLongParameter(request, ParameterConstants.ORG_UNIT_ID_PARAM);

        ObjectiveSetFormBean formBean = new ObjectiveSetFormBean();
        Subject subject = getTargetSubject(request);
        // get the corporate objectives
        ObjectiveSet corporateSet = objectiveService.getPublishedCorporateObjectiveSet();
        formBean.setPublishedCorporateObjectiveSet(corporateSet);

        // set the preferred selected organisationUnit
        OrganisationUnit organisationUnit = null;
        if (organisationUnitId == null) {
            if (subject != null) {
                List<OrganisationUnit> primaryOrganisationUnits = subject.getPrimaryOrganisationUnits();
                organisationUnit = primaryOrganisationUnits.isEmpty() ? null : primaryOrganisationUnits.iterator().next();
            }
        } else {
            organisationUnit = organisationUnitService.findById(organisationUnitId);
        }

        if(organisationUnit == null) {
            organisationUnit = organisationUnitService.findById(OrganisationUnit.ROOT_ORG_UNIT_ID);
        }

        // todo set the parent oragnisation units objectives
        formBean.setOrganisationUnit(organisationUnit);
        Collection<ObjectiveSet> objectiveSets = organisationUnit.getObjectiveSets();
        formBean.setObjectiveSets(objectiveSets);
        ObjectiveSet set = organisationUnit.getCurrentObjectiveSet();
        formBean.setHasCurrentObjectives(set != null && !set.getObjectives().isEmpty());


        return formBean;
    }

    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

        ObjectiveSetFormBean formBean = (ObjectiveSetFormBean) command;
        Long orgUnitId = RequestUtils.getLongParameter(request, "navigator.organisationUnitId");
        if (orgUnitId != null && orgUnitId.equals(formBean.getOrganisationUnitId())) return;
        if (orgUnitId == null) return;
        // otherwise the organisation unit has been changed and we need to update the wrapper
        OrganisationUnit organisationUnit = organisationUnitService.findById(orgUnitId);

        // todo get the parent organisation units objectives
        Collection<ObjectiveSet> sets = organisationUnit.getObjectiveSets();
        formBean.setObjectiveSets(sets);
        formBean.setOrganisationUnit(organisationUnit);
        ObjectiveSet set = organisationUnit.getCurrentObjectiveSet();
        formBean.setHasCurrentObjectives(set != null && !set.getObjectives().isEmpty());
    }

    /**
     * Get the subject the objective will be added to.
     *
     * @param request the servlet http request
     * @return Subject of the user logged in
     */
    private Subject getTargetSubject(HttpServletRequest request) {
        try {
            return subjectService.findByUserId(ZynapWebUtils.getUserId(request));
        } catch (TalentStudioException e) {
            return null;
        }
    }

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        return showPage(request, errors, ORG_UNIT_SELECTED_IDX);
    }

    public void setSubjectService(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    public void setObjectiveService(IObjectiveService objectiveService) {
        this.objectiveService = objectiveService;
    }

    public void setOrganisationUnitService(IOrganisationUnitService organisationUnitService) {
        this.organisationUnitService = organisationUnitService;
    }

    private static final int ORG_UNIT_SELECTED_IDX = 1;

    private ISubjectService subjectService;
    private IObjectiveService objectiveService;
    private IOrganisationUnitService organisationUnitService;
}

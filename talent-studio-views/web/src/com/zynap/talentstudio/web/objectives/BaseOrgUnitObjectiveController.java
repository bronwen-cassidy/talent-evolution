/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.objectives;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.objectives.IObjectiveService;
import com.zynap.talentstudio.objectives.Objective;
import com.zynap.talentstudio.objectives.ObjectiveConstants;
import com.zynap.talentstudio.objectives.ObjectiveSet;
import com.zynap.talentstudio.objectives.ObjectiveDefinition;
import com.zynap.talentstudio.organisation.IOrganisationUnitService;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.web.NodeInfo;
import com.zynap.talentstudio.web.common.DefaultWizardFormController;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Handles adding or editing the organisation unit objectives.
 *
 * @author bcassidy
 * @version 0.1
 * @since 16-Mar-2007 16:52:29
 */
public class BaseOrgUnitObjectiveController extends DefaultWizardFormController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        setCancelView(HistoryHelper.getBackURL(request));
        setSuccessView(HistoryHelper.getBackURL(request));

        // get the targeted organisation unit
        Long orgUnitId = RequestUtils.getRequiredLongParameter(request, OU_ID_PARAM);
        ObjectiveSetFormBean formBean = new ObjectiveSetFormBean();
        OrganisationUnit organisationUnit = organisationUnitService.findByID(orgUnitId);

        ObjectiveSet publishedCorporateSet = assignCorporateObjectiveSet(formBean, organisationUnit);
        ObjectiveSet currentOrganisationObjectiveSet = assignOrganisationSet(formBean, organisationUnit, publishedCorporateSet);
        List<ObjectiveWrapperBean> wrappedObjectives = wrapObjectives(currentOrganisationObjectiveSet);

        formBean.setObjectives(wrappedObjectives);
        return formBean;
    }

    private List<ObjectiveWrapperBean> wrapObjectives(ObjectiveSet currentOrganisationObjectiveSet) {
        List objectives = currentOrganisationObjectiveSet.getObjectives();
        List<ObjectiveWrapperBean> wrappedObjectives = new ArrayList<ObjectiveWrapperBean>();

        for (Iterator iterator = objectives.iterator(); iterator.hasNext();) {
            Objective objective = (Objective) iterator.next();
            ObjectiveWrapperBean wrapper = new ObjectiveWrapperBean(objective);
            wrappedObjectives.add(wrapper);
        }

        if (wrappedObjectives.isEmpty()) wrappedObjectives.add(new ObjectiveWrapperBean(new Objective()));
        return wrappedObjectives;
    }

    private ObjectiveSet assignOrganisationSet(ObjectiveSetFormBean formBean, OrganisationUnit organisationUnit, ObjectiveSet corporateSet) {

        ObjectiveSet currentOUObjectiveSet = organisationUnit.getCurrentObjectiveSet();
        if (currentOUObjectiveSet == null) {
            // we are doing an add create a new one
            currentOUObjectiveSet = new ObjectiveSet();
            // add the definition to the currently published one
            ObjectiveDefinition objectiveDefinition = objectiveService.getPublishedDefinition();
            objectiveDefinition.addObjectiveSet(currentOUObjectiveSet);
            if (corporateSet != null) {
                currentOUObjectiveSet.setCorporateObjectiveSet(corporateSet);
            }
        }

        formBean.setObjectiveSet(currentOUObjectiveSet);
        return currentOUObjectiveSet;
    }

    private ObjectiveSet assignCorporateObjectiveSet(ObjectiveSetFormBean formBean, OrganisationUnit organisationUnit) {
        formBean.setNodeInfo(new NodeInfo(organisationUnit));
        formBean.setOrganisationUnit(organisationUnit);

        ObjectiveSet corporateSet = objectiveService.getPublishedCorporateObjectiveSet();
        corporateSet.getObjectives().size();
        corporateSet.initLazy();
        formBean.setPublishedCorporateObjectiveSet(corporateSet);
        return corporateSet;
    }

    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

        ObjectiveValidator validator = (ObjectiveValidator) getValidator();
        ObjectiveSetFormBean bean = (ObjectiveSetFormBean) command;
        bean.setApproveObjectives(RequestUtils.getBooleanParameter(request, "approveObjectives", false));

        if (isFinishRequest(request)) {
            validator.validateOrgUnitObjectives(bean, errors);
        } else {
            switch (getTargetPage(request, page)) {

                case ADD_OBJECTIVE_IDX:

                    ObjectiveWrapperBean wrapperBean = new ObjectiveWrapperBean();
                    wrapperBean.setObjective(new Objective());
                    bean.addObjective(wrapperBean);
                    break;

                case DELETE_OBJECTIVE_IDX:

                    int deleteIndex = RequestUtils.getIntParameter(request, "deleteIndex", -1);
                    if (deleteIndex > -1) {
                        bean.deleteObjective(deleteIndex);
                    }
                    break;
            }
        }
    }

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        ObjectiveSetFormBean formBean = (ObjectiveSetFormBean) command;
        ObjectiveSet modifiedObjectiveSet = formBean.getModifiedObjectiveSet();
        OrganisationUnit organisationUnit = formBean.getOrganisationUnit();
        modifiedObjectiveSet.setOrganisationUnit(organisationUnit);
        modifiedObjectiveSet.setType(ObjectiveConstants.ORG_UNIT_TYPE);
        if (formBean.isApproveObjectives()) modifiedObjectiveSet.setStatus(ObjectiveConstants.STATUS_APPROVED);

        try {
            objectiveService.createOrUpdate(modifiedObjectiveSet);
        } catch (TalentStudioException e) {
            logger.error(e.getMessage(), e);
        }
        return buildview(organisationUnit);
    }

    private ModelAndView buildview(OrganisationUnit organisationUnit) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(ParameterConstants.NAVIGATOR_OU_LABEL, organisationUnit.getLabel());
        parameters.put(ParameterConstants.NAVIGATOR_OU_ID, organisationUnit.getId());
        return new ModelAndView(new ZynapRedirectView(getSuccessView(), parameters));
    }


    protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        ObjectiveSetFormBean formBean = (ObjectiveSetFormBean) command;
        return buildview(formBean.getOrganisationUnit());
    }

    public void setOrganisationUnitService(IOrganisationUnitService organisationUnitService) {
        this.organisationUnitService = organisationUnitService;
    }

    public final void setObjectiveService(IObjectiveService objectiveService) {
        this.objectiveService = objectiveService;
    }

    private IOrganisationUnitService organisationUnitService;
    private IObjectiveService objectiveService;

    private static final int ADD_OBJECTIVE_IDX = 2;
    private static final int DELETE_OBJECTIVE_IDX = 3;
    private static final String OU_ID_PARAM = "ou_id";
}

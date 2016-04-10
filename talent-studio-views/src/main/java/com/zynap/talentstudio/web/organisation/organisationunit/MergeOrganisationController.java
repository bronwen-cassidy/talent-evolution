/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.organisationunit;


import com.zynap.domain.admin.User;
import com.zynap.talentstudio.organisation.IOrganisationUnitService;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.organisation.MergeOrgUnitWrapperBean;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.talentstudio.web.controller.ZynapDefaultFormController;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class MergeOrganisationController extends ZynapDefaultFormController {

    public MergeOrganisationController() {
    }

    /**
     * Retrieves the organisation unit for the given id in the request
     *
     * @param request
     * @return Object     an instance of an {@link OrganisationUnit}
     * @throws Exception
     */
    public Object formBackingObject(HttpServletRequest request) throws Exception {

        setSuccessView(ZynapWebUtils.addContextPath(request, HistoryHelper.getCurrentURL(request).getCompleteURL()));
        setCancelView(ZynapWebUtils.addContextPath(request, HistoryHelper.getCurrentURL(request).getCompleteURL()));

        Map params = WebUtils.getParametersStartingWith(request, MERGE_OU_PARAM);
        if (params == null || params.size() != 2) {
            return new MergeOrgUnitWrapperBean(new OrganisationUnit(), null);
        }

        String[] idValues = (String[]) params.values().toArray(new String[params.size()]);
        final OrganisationUnit baseOrganisationUnit = organisationUnitService.findById(new Long(idValues[0]));
        final OrganisationUnit secondOrganisationUnit = organisationUnitService.findById(new Long(idValues[1]));
        baseOrganisationUnit.getObjectiveSets().size();
        secondOrganisationUnit.getObjectiveSets().size();                
        return new MergeOrgUnitWrapperBean(baseOrganisationUnit, secondOrganisationUnit);
    }

    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {

        MergeOrgUnitWrapperBean wrapper = (MergeOrgUnitWrapperBean) command;
        Map refData = new HashMap();
        // validation must be done here as we need to handle invalid parameters having come through to the form backing object
        validate(refData, wrapper);

        return refData;
    }

    private void validate(Map refData, MergeOrgUnitWrapperBean wrapper) {

        String errorMessage = null;

        List mergedUnits = wrapper.getMergedFrom();
        if (mergedUnits == null || mergedUnits.size() != 2) {
            errorMessage = "no.organisations.selected";
        } else {
            OrganisationUnit unit1 = (OrganisationUnit) mergedUnits.get(0);
            OrganisationUnit unit2 = (OrganisationUnit) mergedUnits.get(1);
            if (unit1.isDefault() || unit2.isDefault()) {
                errorMessage = "cannot.merge.default.organisation";
            } else {
                final OrganisationUnit parent1 = unit1.getParent();
                final OrganisationUnit parent2 = unit2.getParent();
                if (parent1 != null && parent2 != null && !parent1.getId().equals(parent2.getId())) {
                    errorMessage = "parent.organisation.units.must.be.same";
                }
            }
        }
        if (errorMessage != null) refData.put(ControllerConstants.ERROR, errorMessage);
    }

    public ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {
        MergeOrgUnitWrapperBean organisationUnitWrapper = (MergeOrgUnitWrapperBean) command;
        try {
            OrganisationUnit mergedOrganisationUnit = organisationUnitWrapper.getMergedOrganisationUnit(new User(ZynapWebUtils.getUserId(request)));
            OrganisationUnit defunctOrganisationUnit = organisationUnitWrapper.getDefunctOrganisationUnit();
            organisationUnitService.updateMerge(mergedOrganisationUnit, defunctOrganisationUnit);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            errors.reject(ControllerConstants.ERROR, e.getMessage());
            return showForm(request, errors, getFormView(), getModel(errors));
        }
        return new ModelAndView(new ZynapRedirectView(getSuccessView()));
    }

    /**
     * Determine if the given request represents a form submission.
     * If this form contains the parameters containig the ids of the organisation units to updateMerge, then
     * we return false, otherwise we return default true which is if this is a post;
     *
     * @param request current HTTP request
     * @return true if the request represents a form submission
     */
    protected boolean isFormSubmission(HttpServletRequest request) {
        return !StringUtils.hasText(request.getParameter("_mergedList")) && super.isFormSubmission(request);
    }

    public void setOrganisationManager(IOrganisationUnitService organisationManager) {
        organisationUnitService = organisationManager;
    }

    private IOrganisationUnitService organisationUnitService;

    public static final String MERGE_OU_PARAM = "mc_id";
}

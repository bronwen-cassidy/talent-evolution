/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.organisationunit;

import com.zynap.talentstudio.organisation.IOrganisationUnitService;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.talentstudio.web.controller.ZynapDefaultFormController;

import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class DisableOrganisationController extends ZynapDefaultFormController {

    public DisableOrganisationController() {
    }

    /**
     * Retrieves the organisation unit for the given id in the request
     *
     * @param request
     * @return Object     an instance of an {@link com.zynap.talentstudio.organisation.OrganisationUnit}
     * @throws Exception
     */
    public Object formBackingObject(HttpServletRequest request) throws Exception {
        return getOrganisationManager().findById(RequestUtils.getRequiredLongParameter(request, ParameterConstants.ORG_UNIT_ID_PARAM));
    }

    public ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {

        OrganisationUnit organisationUnit = (OrganisationUnit) command;
        organisationManager.deleteOrgUnitCascade(organisationUnit);
        return new ModelAndView(getSuccessView());
    }

    protected ModelAndView onCancelInternal(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {

        OrganisationUnit organisationUnit = (OrganisationUnit) command;        
        return new ModelAndView(new ZynapRedirectView(getCancelView(), ParameterConstants.ORG_UNIT_ID_PARAM, organisationUnit.getId()));
    }

    public IOrganisationUnitService getOrganisationManager() {
        return organisationManager;
    }

    public void setOrganisationManager(IOrganisationUnitService organisationManager) {
        this.organisationManager = organisationManager;
    }

    private IOrganisationUnitService organisationManager;
}

/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.organisationunit;


import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.organisation.OrganisationUnitWrapperBean;
import com.zynap.talentstudio.web.organisation.attributes.DynamicAttributesHelper;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.talentstudio.web.utils.tree.TreeBuilderHelper;
import com.zynap.talentstudio.organisation.OrganisationUnit;

import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

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
public class EditOrganisationController extends BaseOrganisationController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        Long orgUnitId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.ORG_UNIT_ID_PARAM);
        final OrganisationUnit organisationUnit = organisationUnitService.findByID(orgUnitId);
        final OrganisationUnitWrapperBean wrapperBean = new OrganisationUnitWrapperBean(organisationUnit);
        wrapperBean.setWrappedDynamicAttributes(DynamicAttributesHelper.getEditableAttributeWrapperBeans(organisationUnit, dynamicAttributeService));
        return wrapperBean;
    }

    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        OrganisationUnitWrapperBean organisationUnit = (OrganisationUnitWrapperBean) command;
        Long orgUnitId = organisationUnit.getId();

        Map refData = new HashMap();
        Long principalId = ZynapWebUtils.getUserId(request);
        List orgUnitTree = TreeBuilderHelper.buildOrgUnitTree(organisationUnitService.findValidParents(orgUnitId, principalId));
        refData.put(ControllerConstants.ORG_UNIT_TREE, orgUnitTree);

        return refData;
    }

    protected ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {


        OrganisationUnitWrapperBean organisationUnit = (OrganisationUnitWrapperBean) command;

        boolean imageAttributeDeleted = clearImageExtendedAttribute(request, organisationUnit);
        if (imageAttributeDeleted) return showForm(request, response, errors);

        final OrganisationUnit modified = organisationUnit.getModifiedOrganisationUnit(UserSessionFactory.getUserSession().getUser());
        organisationUnitService.update(modified);

        return new ModelAndView(new ZynapRedirectView(getSuccessView(), ParameterConstants.ORG_UNIT_ID_PARAM, organisationUnit.getId()));
    }

    /**
     * Handles the situation where a simple form has been cancelled.
     *
     * @param request
     * @param response
     * @param command
     * @return ModelAndView the cancel view
     */
    protected ModelAndView onCancelInternal(HttpServletRequest request, HttpServletResponse response, Object command) {
        OrganisationUnitWrapperBean organisationUnit = (OrganisationUnitWrapperBean) command;
        return new ModelAndView(new ZynapRedirectView(getCancelView(), ParameterConstants.ORG_UNIT_ID_PARAM, organisationUnit.getId()));
    }
}

/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.organisationunit;


import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.organisation.OrganisationUnitWrapperBean;
import com.zynap.talentstudio.web.organisation.attributes.DynamicAttributesHelper;
import com.zynap.talentstudio.web.organisation.attributes.FormAttribute;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.controller.ViewConfig;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.talentstudio.web.utils.tree.TreeBuilderHelper;

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
public class AddOrganisationController extends BaseOrganisationController {

    public void setCancelViewConfig(ViewConfig cancelViewConfig) {
        this.cancelViewConfig = cancelViewConfig;
    }

    public ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {

        OrganisationUnitWrapperBean organisationUnit = (OrganisationUnitWrapperBean) command;

        boolean imageAttributeDeleted = clearImageExtendedAttribute(request, organisationUnit);
        if (imageAttributeDeleted) return showForm(request, response, errors);

        final OrganisationUnit unit = organisationUnit.getModifiedOrganisationUnit(UserSessionFactory.getUserSession().getUser());
        if(ZynapWebUtils.isMultiTenant(request)) {
            if(OrganisationUnit.ROOT_ORG_UNIT_ID.equals(unit.getParent().getId())) {
                unit.setRootId(unit.getId());
                unit.setCompanyRoot(true);
            } else {
                // load the parent properly
                OrganisationUnit parent = organisationUnitService.findByID(unit.getParent().getId());
                Long rootId = parent.getRootId();
                if(parent.isCompanyRoot()) rootId = parent.getId();
                unit.setRootId(rootId);
            }
        }
        organisationUnitService.create(unit);
        ZynapRedirectView view = new ZynapRedirectView(getSuccessView(), ParameterConstants.ORG_UNIT_ID_PARAM, organisationUnit.getId());
        return new ModelAndView(view);
    }

    protected Map referenceData(HttpServletRequest request) throws Exception {

        Map<String, Object> refData = new HashMap<String, Object>();

        List orgUnitTree;
        if (ZynapWebUtils.isMultiTenant(request)) {
            // secure tree includes your own orgunit
            List<OrganisationUnit> units = organisationUnitService.findValidParents(ZynapWebUtils.getUserId(request));
            OrganisationUnit orgUnit = organisationUnitService.findOrgUnitBySubjectUserId(ZynapWebUtils.getUserId(request).toString());
            if(orgUnit != null) units.add(0, orgUnit);
            orgUnitTree = TreeBuilderHelper.buildOrgUnitTree(units);
        } else {
            orgUnitTree = TreeBuilderHelper.buildOrgUnitTree(organisationUnitService.findOrgUnitTree(OrganisationUnit.ROOT_ORG_UNIT_ID));
        }
        refData.put(ControllerConstants.ORG_UNIT_TREE, orgUnitTree);

        return refData;
    }

    protected ModelAndView onCancelInternal(HttpServletRequest request, HttpServletResponse response, Object command) {
        return new ModelAndView(cancelViewConfig.getRedirectView());
    }

    public Object formBackingObject(HttpServletRequest request) throws Exception {
        final OrganisationUnit organisationUnit = new OrganisationUnit();
        final OrganisationUnitWrapperBean wrapperBean = new OrganisationUnitWrapperBean(organisationUnit);

        final List<FormAttribute> formAttributes = DynamicAttributesHelper.getEditableAttributeWrapperBeans(organisationUnit, dynamicAttributeService);
        wrapperBean.setWrappedDynamicAttributes(formAttributes);
        return wrapperBean;
    }

    private ViewConfig cancelViewConfig;
}

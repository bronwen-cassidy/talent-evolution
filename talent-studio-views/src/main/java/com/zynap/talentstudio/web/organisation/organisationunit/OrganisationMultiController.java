/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.organisationunit;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.organisation.IOrganisationUnitService;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.organisation.positions.IPositionService;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.organisation.OrganisationUnitWrapperBean;
import com.zynap.talentstudio.web.organisation.attributes.DynamicAttributesHelper;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.controller.ZynapMultiActionController;
import com.zynap.talentstudio.web.utils.tree.Branch;
import com.zynap.talentstudio.web.utils.tree.TreeBuilderHelper;
import com.zynap.talentstudio.web.tag.TreeTag;

import org.apache.commons.collections.Transformer;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Supports functionality to view and list org units.
 *
 * @author bcassidy
 * @version $Revision: $
 */
public class OrganisationMultiController extends ZynapMultiActionController {

    /**
     * Loads full org unit structure for display.
     *
     * @param request
     * @param response
     * @return ModelAndView
     * @throws Exception
     */
    public ModelAndView listOrganisationHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {

        final Long principalId = ZynapWebUtils.getUserId(request);
        final String orgUnitId = RequestUtils.getStringParameter(request, TreeTag.CELL_REQUEST_PARAM,null);
        List<OrganisationUnit> units;
        if (ZynapWebUtils.isMultiTenant(request)) {
            units = getOrganisationManager().findValidParents(ZynapWebUtils.getUserId(request));
            OrganisationUnit orgUnit = getOrganisationManager().findOrgUnitByUser(ZynapWebUtils.getUserId(request).toString());
            if(orgUnit != null && !units.contains(orgUnit)) units.add(0, orgUnit);

            org.apache.commons.collections.CollectionUtils.transform(units, new Transformer() {
                public Object transform(Object o) {
                    OrganisationUnit u = (OrganisationUnit) o;
                    u.setHasAccess(true);
                    return u;
                }
            });

        } else {
            units = getOrganisationManager().findSecureOrgUnitTree(principalId);
        }
        List<Branch> tree = TreeBuilderHelper.buildOrgUnitTree(units);

        final Map<String, Object> model = new HashMap<String, Object>();
        model.put(ORGANISATION_UNIT, tree.get(0));

        if (orgUnitId != null) {
            model.put(TreeTag.CELL_REQUEST_PARAM, orgUnitId);
        }

        return new ModelAndView(LIST_OUS_VIEW, ControllerConstants.MODEL_NAME, model);
    }

    /**
     * Get org unit based on id in request - only works for active org units.
     *
     * @param request
     * @param response
     * @return ModelAndView
     * @throws ServletRequestBindingException
     * @throws TalentStudioException
     */
    public ModelAndView viewOrganisationHandler(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException, TalentStudioException {

        final Long orgUnitId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.ORG_UNIT_ID_PARAM);
        final OrganisationUnit orgUnit = getOrganisationManager().findByID(orgUnitId);
        OrganisationUnitWrapperBean wrapper = new OrganisationUnitWrapperBean(orgUnit);
        wrapper.setWrappedDynamicAttributes(DynamicAttributesHelper.getDisplayableAttributeWrapperBeans(orgUnit, dynamicAttributeService));
        final Map<String, Object> model = new HashMap<String, Object>();
        model.put(ORGANISATION_UNIT, wrapper);

        // this parameter indicates that the navigation (edit / delete) buttons are to be included in the view
        model.put("includeButtons", checkArena(request));

        return new ModelAndView(ORGBUILDER_VIEW, ControllerConstants.MODEL_NAME, model);
    }

    /**
     * Check if  current arena is in List of includeButtonArenas.
     * @param request
     * @return True or false
     */
    private Boolean checkArena(HttpServletRequest request) {
        return ZynapWebUtils.checkForArena(includeButtonArenas, request);
    }

    public void setIncludeButtonArenas(List includeButtonArenas) {
        this.includeButtonArenas = includeButtonArenas;
    }

    public IOrganisationUnitService getOrganisationManager() {
        return organisationManager;
    }

    public void setOrganisationManager(IOrganisationUnitService organisationManager) {
        this.organisationManager = organisationManager;
    }

    public IPositionService getPositionService() {
        return positionService;
    }

    public void setPositionService(IPositionService positionService) {
        this.positionService = positionService;
    }

    public void setDynamicAttributeService(IDynamicAttributeService dynamicAttributeService) {
        this.dynamicAttributeService = dynamicAttributeService;
    }

    private IOrganisationUnitService organisationManager;
    private IDynamicAttributeService dynamicAttributeService;

    private IPositionService positionService;

    /**
     * List of arenas for which the edit / delete buttons will be included in the view org unit display.
     */
    private List includeButtonArenas;

    static final String ORGANISATION_UNIT = "organisation";
    private static final String LIST_OUS_VIEW = "listorganisations";
    private static final String ORGBUILDER_VIEW = "vieworganisation";
}

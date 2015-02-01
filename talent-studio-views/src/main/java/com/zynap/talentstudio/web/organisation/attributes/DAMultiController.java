/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.attributes;

import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.controller.ZynapMultiActionController;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Multi controller that handles listing and display of dynamic attributes.
 *
 * @author bcassidy
 */
public class DAMultiController extends ZynapMultiActionController {

    /**
     * Get all attributes associated with subjects - includes inactive ones.
     *
     * @param request The HttpRequest
     * @param response The HttpResponse
     * @return ModelAndView
     * @throws Exception
     */
    public ModelAndView listSubjectAttributeHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map model = listAttributeHandler(Node.SUBJECT_UNIT_TYPE_);
        return new ModelAndView(LIST_SUBJECT_DA_VIEW, ControllerConstants.MODEL_NAME, model);
    }

    public ModelAndView listOrganisationAttributeHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map model = listAttributeHandler(Node.ORG_UNIT_TYPE_);
        return new ModelAndView(LIST_SUBJECT_DA_VIEW, ControllerConstants.MODEL_NAME, model);
    }

    /**
     * Get all attributes associated with positions - includes inactive ones.
     *
     * @param request The HttpRequest
     * @param response The HttpResponse
     * @return ModelAndView
     * @throws Exception
     */
    public ModelAndView listPositionAttributeHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map model = listAttributeHandler(Node.POSITION_UNIT_TYPE_);
        return new ModelAndView(LIST_POSITION_DA_VIEW, ControllerConstants.MODEL_NAME, model);
    }

    /**
     * Delete the specified attribute.
     *
     * @param request The HttpRequest
     * @param response The HttpResponse
     * @return ModelAndView
     * @throws Exception
     */
    public ModelAndView deleteAttributeHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String artefactType = RequestUtils.getRequiredStringParameter(request, ParameterConstants.ARTEFACT_TYPE);
        Long attributeId = getAttributeId(request);

        if (ZynapWebUtils.isConfirmed(request)) {

            getDynamicAttributeService().delete(attributeId);
            final String url = artefactType.equals(Node.SUBJECT_UNIT_TYPE_) ? LIST_SUBJECT_DA_REDIRECT : artefactType.equals(Node.POSITION_UNIT_TYPE_) ? LIST_POSITION_DA_REDIRECT : LIST_ORGUNIT_DA_REDIRECT;
            return new ModelAndView(url);
        }

        Map<String, Object> model = new HashMap<String, Object>();
        model.put(ControllerConstants.DYNAMIC_ATTRIBUTE, getDynamicAttributeService().findById(attributeId));
        model.put(USED_BY_NODE, new Boolean(getDynamicAttributeService().usedByNode(attributeId)));
        return new ModelAndView(CONFIRM_DELETE_DA, ControllerConstants.MODEL_NAME, model);
    }

    /**
     * View the details of the specified attribute.
     *
     * @param request The HttpRequest
     * @param response The HttpResponse
     * @return ModelAndView
     */
    public ModelAndView viewAttributeHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Long attributeId = getAttributeId(request);

        DynamicAttribute dynamicAttribute = getDynamicAttributeService().findById(attributeId);

        Map<String, Object> model = new HashMap<String, Object>();
        model.put(ControllerConstants.DYNAMIC_ATTRIBUTE, new DynamicAttributeWrapper(dynamicAttribute));
        List<ExpressionWrapper> expressionWrappers = EditDynamicAttributeController.wrapExpressions(dynamicAttribute);
        if (!expressionWrappers.isEmpty()) {
            model.put(EXPRESSIONS_KEY, expressionWrappers);
        }
        return new ModelAndView(DISPLAY_DA_VIEW, ControllerConstants.MODEL_NAME, model);
    }

    private Map listAttributeHandler(String artefactType) {

        Map<String, Object> model = new HashMap<String, Object>();
        final List attributes = dynamicAttributeService.listAllAttributes(artefactType);
        model.put(ControllerConstants.DYNAMIC_ATTRIBUTES, attributes);
        model.put(ControllerConstants.ARTEFACT_TYPE, artefactType);
        return model;
    }

    private Long getAttributeId(HttpServletRequest request) throws Exception {
        return RequestUtils.getRequiredLongParameter(request, ParameterConstants.ATTR_ID);
    }

    public IDynamicAttributeService getDynamicAttributeService() {
        return dynamicAttributeService;
    }

    public void setDynamicAttributeService(IDynamicAttributeService dynamicAttributeService) {
        this.dynamicAttributeService = dynamicAttributeService;
    }

    public ILookupManager getLookupManager() {
        return lookupManager;
    }

    public void setLookupManager(ILookupManager lookupManager) {
        this.lookupManager = lookupManager;
    }

    private IDynamicAttributeService dynamicAttributeService;
    private ILookupManager lookupManager;

    static final String LIST_POSITION_DA_REDIRECT = "listpositionDARedirect";
    static final String LIST_SUBJECT_DA_REDIRECT = "listsubjectDARedirect";
    static final String LIST_ORGUNIT_DA_REDIRECT = "listorganisationDARedirect";

    static final String LIST_POSITION_DA_VIEW = "listpositionDA";
    static final String LIST_SUBJECT_DA_VIEW = "listsubjectDA";
    static final String LIST_ORGUNIT_DA_VIEW = "listorganisationDA";
    static final String DISPLAY_DA_VIEW = "viewDA";
    static final String CONFIRM_DELETE_DA = "confirmdeleteDA";

    static final String USED_BY_NODE = "usedByNode";
    private static final String EXPRESSIONS_KEY = "expressions";
}

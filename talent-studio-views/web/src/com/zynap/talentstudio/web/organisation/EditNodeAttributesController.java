/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation;

import com.zynap.common.util.UploadedFile;
import com.zynap.talentstudio.display.DisplayConfigItem;
import com.zynap.talentstudio.display.IDisplayConfigService;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.organisation.positions.IPositionService;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.common.validation.NodeValidator;
import com.zynap.talentstudio.web.organisation.attributes.DynamicAttributesHelper;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.beans.UploadedFilePropertyEditor;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.web.controller.ZynapDefaultFormController;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public abstract class EditNodeAttributesController extends ZynapDefaultFormController {

    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        super.initBinder(request, binder);
        binder.registerCustomEditor(UploadedFile.class, new UploadedFilePropertyEditor());
        binder.registerCustomEditor(Date.class, AnalysisAttributeHelper.DOB_ATTR, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }

    protected boolean clearImageExtendedAttribute(HttpServletRequest request, NodeWrapperBean nodeWrapperBean) {
        final boolean delete = DynamicAttributesHelper.isClearAttributeValueRequest(request, nodeWrapperBean);
        if (delete) {
            DynamicAttributesHelper.clearAttributeValue(nodeWrapperBean);
        }

        return delete;
    }

    protected void applyAttributes(HttpServletRequest request, NodeWrapperBean nodeWrapperBean, Node node) throws Exception {
        Long displayConfigItemId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.DISPLAY_ITEM_ID_PARAM);
        DisplayConfigItem configItem = displayConfigService.findConfigItemById(displayConfigItemId);
        DynamicAttributesHelper.assignDisplayConfigAttributes(nodeWrapperBean, configItem, node, getDynamicAttributeService());
    }

    /**
     * Handles the situation where the form was cancelled.
     *
     * @param request
     * @param response
     * @param command
     * @return ModelAndView the cancel view
     * @throws Exception
     */
    protected ModelAndView onCancelInternal(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {

        NodeWrapperBean node = (NodeWrapperBean) command;

        RedirectView view = new ZynapRedirectView(getCancelView());
        view.addStaticAttribute(ParameterConstants.NODE_ID_PARAM, node.getId());
        return new ModelAndView(view);
    }

    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors) throws Exception {
        NodeWrapperBean nodeWrapperBean = (NodeWrapperBean) command;
        if (DynamicAttributesHelper.isClearAttributeValueRequest(request, nodeWrapperBean)) {
            DynamicAttributesHelper.clearAttributeValue(nodeWrapperBean);
        }
        NodeValidator validator = (NodeValidator) getValidator();
        validator.validateDynamicAttributes(nodeWrapperBean, errors);
    }

    public IDynamicAttributeService getDynamicAttributeService() {
        return dynamicAttributeService;
    }

    public void setDynamicAttributeService(IDynamicAttributeService dynamicAttributeService) {
        this.dynamicAttributeService = dynamicAttributeService;
    }

    public IPositionService getPositionService() {
        return positionService;
    }

    public void setPositionService(IPositionService positionService) {
        this.positionService = positionService;
    }

    public void setDisplayConfigService(IDisplayConfigService displayConfigService) {
        this.displayConfigService = displayConfigService;
    }

    protected IPositionService positionService;
    protected IDynamicAttributeService dynamicAttributeService;
    protected IDisplayConfigService displayConfigService;
}


/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.security.area;

import com.zynap.talentstudio.security.ISecurityManager;
import com.zynap.talentstudio.security.areas.Area;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.controller.ZynapMultiActionController;
import com.zynap.talentstudio.analysis.IAnalysisService;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller that handles lookup and deletion of Areas.
 *
 * @author amark
 */
public class AreaMultiController extends ZynapMultiActionController {

    public static final Long getAreaId(HttpServletRequest request) throws ServletRequestBindingException {
        return RequestUtils.getRequiredLongParameter(request, ParameterConstants.AREA_ID);
    }

    public void setSecurityManager(ISecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    public void setAnalysisService(IAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    public ModelAndView listAreaHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Collection areas = securityManager.getAreas();

        Map model = new HashMap();
        model.put(AREAS, areas);
        return new ModelAndView("listarea", ControllerConstants.MODEL_NAME, model);
    }

    public ModelAndView viewAreaHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {

        final Long areaId = getAreaId(request);
        Area area = securityManager.findArea(areaId);

        Map model = new HashMap();
        model.put(AREA, area);
        if(area.getSubjectPopulationId() != null) {
            model.put("subjectPopulation", analysisService.findById(area.getSubjectPopulationId()));
        }
        if(area.getPositionPopulationId() != null) {
            model.put("positionPopulation", analysisService.findById(area.getPositionPopulationId()));
        }
        return new ModelAndView("viewarea", ControllerConstants.MODEL_NAME, model);
    }

    public ModelAndView deleteAreaHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {

        final Long areaId = getAreaId(request);

        if (ZynapWebUtils.isConfirmed(request)) {
            try {
                
                securityManager.deleteArea(areaId);
                return new ModelAndView("listareaRedirect");

            } catch (DataIntegrityViolationException e) {
                final ModelAndView modelAndView = viewAreaHandler(request, response);
                modelAndView.getModel().put(ControllerConstants.ERROR, new Boolean(true));
                return modelAndView;
            }
        }

        final Map model = new HashMap();
        model.put(AREA, securityManager.findArea(areaId));
        return new ModelAndView(CONFIRM_DELETE_VIEW, ControllerConstants.MODEL_NAME, model);
    }

    private ISecurityManager securityManager;
    private IAnalysisService analysisService;

    private static final String CONFIRM_DELETE_VIEW = "confirmdeletearea";

    public static final String AREA = "area";
    public static final String AREAS = "areas";
}

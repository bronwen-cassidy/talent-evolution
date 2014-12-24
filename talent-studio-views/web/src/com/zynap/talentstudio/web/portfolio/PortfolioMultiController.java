/*
 * Copyright (c) 2004 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.portfolio;

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.organisation.portfolio.IPortfolioService;
import com.zynap.talentstudio.organisation.portfolio.PortfolioItem;
import com.zynap.talentstudio.organisation.positions.IPositionService;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.controller.ZynapMultiActionController;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller that views portfolio items and deletes portfolio items.
 *
 * @author Andreas Andersson
 * @since 05/04/2004
 */
public class PortfolioMultiController extends ZynapMultiActionController {

    /**
     * Method to handle viewing a subject portfolio item.
     *
     * @param request
     * @return ModelAndView
     * @throws ServletException
     */
    public ModelAndView viewSubjectPortfolioItemHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return viewPortfolioItem(request, response, "editsubjectportfolioitem.htm", "editsubjectdeleteportfolioitem.htm", false);
    }

    /**
     * Method to handle viewing a position portfolio item.
     *
     * @param request
     * @return ModelAndView
     * @throws ServletException
     */
    public ModelAndView viewPositionPortfolioItemHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return viewPortfolioItem(request, response, "editpositionportfolioitem.htm", "editpositiondeleteportfolioitem.htm", false);
    }

    /**
     * Method to handle viewing an item in my (subject) portfolio.
     *
     * @param request
     * @return ModelAndView
     * @throws ServletException
     */
    public ModelAndView viewMyPortfolioItemHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return viewPortfolioItem(request, response, "editmyportfolioitem.htm", "deletemyportfolioitem.htm", true);
    }

    /**
     * Method to handle the deletion of an item from a position portfolio.
     *
     * @param request
     * @return ModelAndView
     * @throws ServletException
     */
    public ModelAndView deletePositionPortfolioItemHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return deletePortfolioItem(request, response, "viewpositionportfolioitem.htm", "editpositiondeleteportfolioitem.htm");
    }

    /**
     * Method to handle the deletion of an item from a subject portfolio.
     *
     * @param request
     * @return ModelAndView
     * @throws ServletException
     */
    public ModelAndView deleteSubjectPortfolioItemHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return deletePortfolioItem(request, response, "viewsubjectportfolioitem.htm", "editsubjectdeleteportfolioitem.htm");
    }

    /**
     * Method to handle the deletion of an item from a subjectuser's personal portfolio.
     *
     * @param request
     * @return ModelAndView
     * @throws ServletException
     */
    public ModelAndView deleteMyPortfolioItemHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return deletePortfolioItem(request, response, "viewmyportfolioitem.htm", "deletemyportfolioitem.htm");
    }

    public ISubjectService getSubjectService() {
        return _subjectService;
    }

    public void setSubjectService(ISubjectService subjectService) {
        _subjectService = subjectService;
    }

    public IPortfolioService getPortfolioService() {
        return portfolioService;
    }

    public void setPortfolioService(IPortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    public IPositionService getPositionService() {
        return _positionService;
    }

    public void setPositionService(IPositionService positionService) {
        _positionService = positionService;
    }

    /**
     * Get portfolio item id from request (parameter).
     *
     * @param request
     * @param response
     * @return The portfolio item id
     * @throws ServletRequestBindingException
     */
    private Long getPortfolioItemId(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException {
        return RequestUtils.getRequiredLongParameter(request, ParameterConstants.ITEM_ID);
    }

    /**
     * Common handler for deletion of portfolio item.
     *
     * @param request
     * @return ModelAndView
     * @throws Exception
     */
    private ModelAndView deletePortfolioItem(HttpServletRequest request, HttpServletResponse response, String cancelView, String confirmView) throws Exception {
        Long portfolioItemId = getPortfolioItemId(request, response);

        if (ZynapWebUtils.isConfirmed(request)) {
            portfolioService.delete(portfolioItemId);
            final String successView = HistoryHelper.getSuccessViewURL(request);
            return new ModelAndView(new ZynapRedirectView(successView));
        } else {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put(ControllerConstants.PORTFOLIO_ITEM, portfolioService.findById(portfolioItemId));
            model.put(ControllerConstants.CANCEL_VIEW, cancelView);
            model.put(ControllerConstants.CONFIRM_VIEW, confirmView);

            return new ModelAndView(CONFIRM_DELETE_ITEM_VIEW, ControllerConstants.MODEL_NAME, model);
        }
    }

    /**
     * Method to handle viewing of a portfolio item.
     * <br> Checks access to artefact that the item belongs to so that view can decide whether or not to display edit and delete links.
     *
     * @param request
     * @param editView
     * @param deleteView
     * @param personalPortfolio
     * @return ModelAndView
     * @throws ServletException
     */
    private ModelAndView viewPortfolioItem(HttpServletRequest request, HttpServletResponse response, String editView, String deleteView, boolean personalPortfolio) throws Exception {

        Map<String, Object> model = new HashMap<String, Object>();

        Long portfolioItemId = getPortfolioItemId(request, response);
        final UserSession session = ZynapWebUtils.getUserSession(request);
        PortfolioItem portfolioItem;
        if(session.isAdministrator()) {
            portfolioItem = (PortfolioItem) portfolioService.findById(portfolioItemId);
        } else {
            portfolioItem = (PortfolioItem) portfolioService.findAndCheckArtefactAccess(portfolioItemId);
        }

        PortfolioItemWrapper portItemWrapper = new PortfolioItemWrapper(portfolioItem, portfolioService.findItemFile(portfolioItemId));
        portItemWrapper.setMyPortfolio(personalPortfolio);
        portItemWrapper.setUserSession(session);
        model.put(ControllerConstants.PORTFOLIO_ITEM, portItemWrapper);

        model.put(ControllerConstants.EDIT_VIEW, editView);
        model.put(ControllerConstants.DELETE_VIEW, deleteView);

        return new ModelAndView(PORTFOLIO_ITEM_VIEW, ControllerConstants.MODEL_NAME, model);
    }

    private ISubjectService _subjectService;
    private IPortfolioService portfolioService;
    private IPositionService _positionService;

    private static final String PORTFOLIO_ITEM_VIEW = "viewportfolioitem";
    private static final String CONFIRM_DELETE_ITEM_VIEW = "confirmdeleteportfolioitem";


    /**
     * Name of constant that holds value indicating if user has access to portfolio item.
     */
    public static final String HAS_ACCESS = "hasAccess";
}

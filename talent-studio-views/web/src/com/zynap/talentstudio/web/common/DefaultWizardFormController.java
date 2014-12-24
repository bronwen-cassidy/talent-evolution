/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.common;

import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.controller.ControllerUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.talentstudio.web.common.exceptions.InvalidSubmitException;

import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractWizardFormController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public abstract class DefaultWizardFormController extends AbstractWizardFormController {

    public String getSuccessView() {
        return successView;
    }

    public void setSuccessView(String successView) {
        this.successView = successView;
    }

    public String getCancelView() {
        return cancelView;
    }

    public void setCancelView(String cancelView) {
        this.cancelView = cancelView;
    }

    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {

        //Tell the security interceptor which is the current Controller to be able to check the command
        request.getSession().setAttribute(ParameterConstants.CONTROLLER_NAME, this.getClass().getName());

        // register common custom binders
        ControllerUtils.registerCommonEditors(binder);
    }

    protected final boolean isBack(HttpServletRequest request) {
        return StringUtils.hasText(request.getParameter(BACK_REQUEST_PARAM));
    }


    /**
     * Return the current page number. Used by {@link #processFormSubmission}.
     * <p>The default implementation checks the page session attribute.
     * Subclasses can override this for customized page determination.
     * Any missing page attribute perhaps can be defaulted to 0 and the
     * page session attribute added.
     *
     * @param request current HTTP request
     * @return the current page number
     * @see #getPageSessionAttributeName()
     */
    protected int getCurrentPage(HttpServletRequest request) {
        try {
            return super.getCurrentPage(request);
        } catch (java.lang.IllegalStateException e) {
            logger.error(" CAUGHT AN IllegalStateException redirecting this state to InvalidSubmitException to be handled without the stack error", e);
            throw new InvalidSubmitException(request.getSession(), null, request.getRequestURI(), true, getClass().getName());
        }
    }

    /**
     * Process cancel request.
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param command  form object with the current wizard state
     * @param errors   Errors instance containing errors
     * @return the cancellation view
     * @throws Exception in case of invalid state or arguments
     */
    protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        return processCancelInternal(request, response, command, errors);
    }

    protected ModelAndView processCancelInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {
        RedirectView view = new ZynapRedirectView(getCancelView());
        return new ModelAndView(view);
    }

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        return processFinishInternal(request, response, command, errors);
    }

    /**
     * Default behaviour is to return the configured successview
     * @param request
     * @param response
     * @param command
     * @param errors
     * @return
     */
    protected ModelAndView processFinishInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {
        return new ModelAndView(new ZynapRedirectView(getSuccessView()));
    }

    /**
     * Set the page session attribute.
     *
     * @param request    the HttpServletRequest
     * @param pageNumber The page number
     */
    protected void setPageSessionAttribute(HttpServletRequest request, int pageNumber) {
        request.getSession().setAttribute(getPageSessionAttributeName(), new Integer(pageNumber));
    }

    protected void setInitialPage(int i) {
        this.initialPage = new Integer(i);
    }

    protected int getInitialPage(HttpServletRequest request) {
        return (initialPage != null) ? initialPage.intValue() : 0;
    }

    protected void onBind(HttpServletRequest request, Object command, BindException errors) throws Exception {
        if (!isBack(request) && !isCancelRequest(request)) {
            onBindInternal(request, command, errors);
        }
    }

    /**
     * Default behaviour is do nothing
     *
     * @param request
     * @param command
     * @param errors
     */
    protected void onBindInternal(HttpServletRequest request, Object command, Errors errors) {
    }

    /**
     * Overrides the super implementation to check if the request is a back request.
     * If the request is not a back request forwards processing to implementations of {@link #onBindAndValidateInternal(javax.servlet.http.HttpServletRequest,Object,org.springframework.validation.Errors,int)}
     *
     * @param request the request
     * @param command the form backing object
     * @param errors  the errors
     * @param page    the current page
     * @throws Exception
     */
    protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {
        if (!isBack(request) && !isCancelRequest(request)) {
            onBindAndValidateInternal(request, command, errors, page);
        }
    }

    /**
     * Called when the request is not a back request, in order to handle validation when NOT going back.
     *
     * @param request
     * @param command
     * @param errors
     * @param page
     * @throws Exception
     */
    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        // by default does nothing
    }

    /**
     * Checks to see if the request is a display tag sort request.
     *
     * @param request
     * @return true is this is a display tag sort request false otherwise
     */
    protected boolean isDisplayTagSort(HttpServletRequest request) {
        return !ZynapWebUtils.getParametersStartingWith(request, ControllerConstants.DISPLAY_TAG_PREFIX).isEmpty();
    }

    private static final String BACK_REQUEST_PARAM = "_back";
    private String successView;
    private String cancelView;
    private Integer initialPage;
}

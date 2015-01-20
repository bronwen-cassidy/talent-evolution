/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.security.domain;

import com.zynap.talentstudio.security.SecurityDomain;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
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
public class EditDomainController extends BaseDomainController {

    /**
     * Constructor.
     */
    public EditDomainController() {
    }

    /**
     * Form backing object consists of a SecurityDomainWrapperBean containing the SecurityDomain being edited.
     *
     * @param request
     * @return SecurityDomainWrapperBean
     * @throws Exception
     */
    public Object formBackingObject(HttpServletRequest request) throws Exception {
        final Long domainId = DomainMultiController.getDomainId(request);
        SecurityDomain domain = getSecurityManager().findDomain(domainId);
        SecurityDomainWrapperBean securityDomainWrapperBean = new SecurityDomainWrapperBean(domain);
        setRoles(securityDomainWrapperBean);
        setUsers(securityDomainWrapperBean);
        setAreas(securityDomainWrapperBean);
        return securityDomainWrapperBean;
    }

    /**
     * Attempts to update SecurityDomain.
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param command  form object with the current wizard state
     * @param errors   Errors instance containing errors
     * @return the cancellation view
     * @throws Exception
     */
    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        SecurityDomainWrapperBean securityDomainWrapperBean = getWrapper(command);
        SecurityDomain updatedDomain = securityDomainWrapperBean.getModifiedSecurityDomain();

        try {
            getSecurityManager().updateDomain(updatedDomain);
        } catch (DataIntegrityViolationException e) {
            logger.error("Failed to update domain because of: ", e);
            errors.rejectValue("label", "error.duplicate.label", e.getMessage());
            return showPage(request, errors, CORE_VIEW_IDX);
        }

        RedirectView view = new ZynapRedirectView(getSuccessView());
        view.addStaticAttribute(ParameterConstants.DOMAIN_ID, updatedDomain.getId());
        return new ModelAndView(view);
    }

    /**
     * Process cancel request.
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param command  form object with the current wizard state
     * @param errors   Errors instance containing errors
     * @return the cancellation view
     * @throws Exception
     */
    protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        SecurityDomainWrapperBean securityDomainWrapperBean = getWrapper(command);

        ModelAndView modelAndView = super.processCancel(request, response, command, errors);
        RedirectView view = (RedirectView) modelAndView.getView();
        view.addStaticAttribute(ParameterConstants.DOMAIN_ID, securityDomainWrapperBean.getId());
        return modelAndView;
    }
}

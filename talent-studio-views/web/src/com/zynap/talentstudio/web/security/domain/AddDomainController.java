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
public class AddDomainController extends BaseDomainController {

    /**
     * Constructor.
     */
    public AddDomainController() {
    }

    /**
     * Form backing object consists of a new SecurityDomainWrapperBean.
     *
     * @param request
     * @return SecurityDomainWrapperBean
     * @throws Exception
     */
    public Object formBackingObject(HttpServletRequest request) throws Exception {
        final SecurityDomainWrapperBean securityDomainWrapperBean = new SecurityDomainWrapperBean(new SecurityDomain());
        setRoles(securityDomainWrapperBean);
        setUsers(securityDomainWrapperBean);
        setAreas(securityDomainWrapperBean);        
        return securityDomainWrapperBean;
    }

    /**
     * Attempts to save new SecurityDomain.
     *
     * @param request current HTTP request
     * @param response current HTTP response
     * @param command form object with the current wizard state
     * @param errors Errors instance containing errors
     * @return the success view
     * @throws Exception
     */
    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        SecurityDomainWrapperBean securityDomainWrapperBean = getWrapper(command);
        SecurityDomain newSecurityDomain = securityDomainWrapperBean.getModifiedSecurityDomain();

        try {
            getSecurityManager().createDomain(newSecurityDomain);
        } catch (DataIntegrityViolationException e) {

            securityDomainWrapperBean.resetIds();

            logger.error("Failed to create domain because of: ", e);
            errors.rejectValue("label", "error.duplicate.label", e.getMessage());
            return showPage(request, errors, CORE_VIEW_IDX);
        }

        RedirectView view = new ZynapRedirectView(getSuccessView());
        view.addStaticAttribute(ParameterConstants.DOMAIN_ID, newSecurityDomain.getId());
        return new ModelAndView(view);
    }

}

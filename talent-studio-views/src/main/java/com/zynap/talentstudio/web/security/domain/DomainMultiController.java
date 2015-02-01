/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.security.domain;

import com.zynap.talentstudio.arenas.IArenaManager;
import com.zynap.talentstudio.security.ISecurityManager;
import com.zynap.talentstudio.security.SecurityDomain;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.security.UserHelper;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.controller.ZynapMultiActionController;
import com.zynap.domain.admin.User;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller that handles lookup and deletion of Security Domains.
 *
 * @author amark
 */
public class DomainMultiController extends ZynapMultiActionController {

    public static final Long getDomainId(HttpServletRequest request) throws ServletRequestBindingException {
        return RequestUtils.getRequiredLongParameter(request, ParameterConstants.DOMAIN_ID);
    }

    public ISecurityManager getSecurityManager() {
        return securityManager;
    }

    public void setSecurityManager(ISecurityManager securityManager) {
        this.securityManager = securityManager;
    }


    public ModelAndView listDomainHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Collection domains = getSecurityManager().getAllDomains();

        Map<String, Object> model = new HashMap<String, Object>();
        model.put(SECURITY_DOMAINS, domains);
        return new ModelAndView("listsecuritydomain", ControllerConstants.MODEL_NAME, model);
    }

    public ModelAndView viewDomainHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {

        final Long domainId = getDomainId(request);
        SecurityDomain domain = getSecurityManager().findDomain(domainId);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(SECURITY_DOMAIN, domain);

        // filter out root users from list of users
        model.put(ControllerConstants.USERS, UserHelper.removeRootUsers(domain.getUsers()));
        return new ModelAndView("viewsecuritydomain", ControllerConstants.MODEL_NAME, model);
    }

    public ModelAndView deleteDomainHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {

        final Long domainId = getDomainId(request);

        if (ZynapWebUtils.isConfirmed(request)) {
            getSecurityManager().deleteDomain(domainId);
            return new ModelAndView("listsecuritydomainRedirect");
        }

        final Map<String, Object> model = new HashMap<String, Object>();
        model.put(SECURITY_DOMAIN, securityManager.findDomain(domainId));
        return new ModelAndView(CONFIRM_DELETE_VIEW, ControllerConstants.MODEL_NAME, model);
    }

    private ISecurityManager securityManager;

    private static final String CONFIRM_DELETE_VIEW = "confirmdeletesecuritydomain";

    public static final String SECURITY_DOMAIN = "securityDomain";
    public static final String SECURITY_DOMAINS = "securityDomains";
}

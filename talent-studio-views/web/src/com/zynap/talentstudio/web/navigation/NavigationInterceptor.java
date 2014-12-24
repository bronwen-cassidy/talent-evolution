package com.zynap.talentstudio.web.navigation;

import com.zynap.domain.UserSession;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.arenas.MenuSection;
import com.zynap.talentstudio.navigation.ZynapNavigator;
import com.zynap.talentstudio.organisation.IOrganisationUnitService;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.tree.TreeBuilderHelper;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * User: amark
 * Date: 23-Dec-2004
 * Time: 14:10:53
 */
public class NavigationInterceptor extends HandlerInterceptorAdapter {

    /**
     * Save requested url so that it can be found by the <code>SaveUrlTag</code> if present in the jsp.
     *
     * @param request  The HttpServletRequest
     * @param response The HttpServletResponse
     * @param handler  The Handler
     * @return true always
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        UserSession userSession = ZynapWebUtils.getUserSession(request);
        if (userSession != null) {
            updateMenus(userSession, request);
            if (userSession.getUserPrincipal() != null) {
                // required for the menus enabling us to check the number of unopened items for the given user
                request.setAttribute("loggedInUserId", userSession.getId());
                updateNavigator(userSession, request);
            }
        }

        // add the navigation state to the request
        HttpSession session = request.getSession(false);
        if (session != null) {
            request.setAttribute(NAV_VISIBLE_STATE_ATTR, session.getAttribute(NAV_VISIBLE_STATE_ATTR));
        } else {
            request.setAttribute(NAV_VISIBLE_STATE_ATTR, Boolean.TRUE);
        }

        return true;
    }

    private void updateMenus(UserSession userSession, HttpServletRequest request) {
        String menuId = request.getParameter(ParameterConstants.MENU_PARAM);
        if (StringUtils.hasText(menuId)) {
            userSession.setCurrentMenuSectionId(menuId);
        } else {
            // check if it is in the session
            menuId = userSession.getCurrentMenuSectionId();
        }

        // Ok, we know we have a user that should have menus dipslayed since an arenas was returned
        Collection menuSections = userSession.getMenuSections();
        ZynapWebUtils.setMenuSections(menuSections, request);

        if (menuId != null) request.setAttribute(ParameterConstants.MENU_PARAM, menuId);
    }

    private void updateNavigator(UserSession userSession, HttpServletRequest request) throws TalentStudioException {
        if (!isOrgUnitPickerNeed(request)) return;
        String orgUnitLabel = RequestUtils.getStringParameter(request, ParameterConstants.NAVIGATOR_OU_LABEL, "");
        ZynapNavigator navigator;
        if (orgUnitLabel.length() > 0) {
            Long orgUnitId = RequestUtils.getLongParameter(request, ParameterConstants.NAVIGATOR_OU_ID, -1000);
            navigator = new ZynapNavigator(orgUnitId, orgUnitLabel);
        } else {
            navigator = userSession.getNavigator();

            Collection menus = userSession.getMenuSections();
            if (navigator == null) navigator = new ZynapNavigator();
            if (menus != null) {
                navigator.setHasOU(false);
                for (Iterator iterator = menus.iterator(); iterator.hasNext(); ) {
                    MenuSection section = (MenuSection) iterator.next();
                    navigator.setHasOU(section.isOrgUnitHierarchy());
                    if (navigator.isHasOU()) break;
                }
            }
            if (navigator.getOrganisationUnitId() != null) {
                OrganisationUnit ou;
                try {
                    if (ZynapWebUtils.isMultiTenant(request)) {
                        ou = getOrganisationManager().findOrgUnitByUser(userSession.getId().toString());
                    } else {
                        ou = getOrganisationManager().findByID(navigator.getOrganisationUnitId());
                    }
                    navigator.setOrganisationUnitId(ou.getId());
                    navigator.setOrganisationUnitLabel(ou.getLabel());
                } catch (TalentStudioException e) {
                    navigator.setOrganisationUnitId(null);
                }
            }
            if (navigator.getOrganisationUnitId() == null) {
                try {
                    OrganisationUnit ou = getUserService().getUserDefaultOrganisationUnit(userSession.getUserName());
                    navigator.setOrganisationUnitId(ou.getId());
                    navigator.setOrganisationUnitLabel(ou.getLabel());
                } catch (TalentStudioException e) {
                    e.printStackTrace();
                }
            }
        }
        boolean submitParameter = (RequestUtils.getStringParameter(request, ParameterConstants.NAVIGATOR_NOT_SUBMIT, "").length() > 0);
        if (submitParameter) {
            navigator.setNotSubmit(RequestUtils.getBooleanParameter(request, ParameterConstants.NAVIGATOR_NOT_SUBMIT, false));
        }

        userSession.setNavigator(navigator);
        request.setAttribute(ParameterConstants.NAVIGATOR_COMMAND, navigator);

        buildOUTree(userSession, request);
    }

    private void buildOUTree(UserSession userSession, HttpServletRequest request) throws TalentStudioException {
        Long userId = userSession.getId();
        List results;
        if (userSession.isMultiTenant() && userId != null) {

            List<OrganisationUnit> units = getOrganisationManager().findValidParents(userId);
            OrganisationUnit orgUnit = getOrganisationManager().findOrgUnitByUser(userId.toString());
            if (orgUnit != null && !units.contains(orgUnit)) units.add(0, orgUnit);

            results = TreeBuilderHelper.buildOrgUnitTree(units);
        } else {
            results = TreeBuilderHelper.buildOrgUnitTree(getOrganisationManager().findOrgUnitTree(OrganisationUnit.ROOT_ORG_UNIT_ID));
        }
        request.setAttribute(ControllerConstants.ORG_UNIT_TREE, results);
    }

    private boolean isOrgUnitPickerNeed(HttpServletRequest request) {
        return ZynapWebUtils.urlMatches(urlsWithPicker, request);
    }


    public IOrganisationUnitService getOrganisationManager() {
        return organisationManager;
    }

    public void setOrganisationManager(IOrganisationUnitService organisationManager) {
        this.organisationManager = organisationManager;
    }

    public IUserService getUserService() {
        return userService;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    public void setUrlsWithPicker(List urlsWithPicker) {
        this.urlsWithPicker = urlsWithPicker;
    }

    private IOrganisationUnitService organisationManager;
    private IUserService userService;
    private List urlsWithPicker;


    public static final String NAV_VISIBLE_STATE_ATTR = "navigationVisible";
}

/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.arena;

import com.zynap.domain.UserSession;
import com.zynap.domain.UserPrincipal;
import com.zynap.talentstudio.arenas.Arena;
import com.zynap.talentstudio.arenas.IArenaManager;
import com.zynap.talentstudio.display.DisplayConfig;
import com.zynap.talentstudio.display.IDisplayConfigService;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.talentstudio.security.roles.IRoleManager;
import com.zynap.talentstudio.security.roles.Role;
import com.zynap.web.controller.ZynapDefaultFormController;
import com.zynap.exception.TalentStudioException;

import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.dao.DataIntegrityViolationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class EditArenaController extends ZynapDefaultFormController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        final List all = displayConfigService.findByType(DisplayConfig.VIEW_TYPE);

        final String arenaId = RequestUtils.getRequiredStringParameter(request, ParameterConstants.ARENA_ID_PARAM);
        final Arena arena = arenaManager.getArena(arenaId);

        return new ArenaWrapperBean(arena, all);
    }

    /**
     * Sets the active flag to false if required.
     *
     * @param request
     * @param command
     * @throws Exception
     */
    protected void onBindInternal(HttpServletRequest request, Object command) throws Exception {
        ArenaWrapperBean wrapperBean = (ArenaWrapperBean) command;
        if (wrapperBean.isHideable()) wrapperBean.setActive(RequestUtils.getBooleanParameter(request, ParameterConstants.ACTIVE, false));
    }

    /**
     * Process the submit - commits the updates to the db and reloads the menus.
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param command Formbacking object
     * @param errors BindException
     * @return The ModelAndView
     */
    protected ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {
        ArenaWrapperBean wrapperBean = (ArenaWrapperBean) command;
        final Arena arena = wrapperBean.getModifiedArena();
        try {
            arenaManager.updateArena(arena);
        } catch (DataIntegrityViolationException e) {
            errors.rejectValue("label", "error.duplicate.label");
            return showForm(request, response, errors);
        }

        final UserSession userSession = ZynapWebUtils.getUserSession(request);
        userSession.reloadMenus();
        RedirectView view = new ZynapRedirectView(getSuccessView());
        return new ModelAndView(view);
    }

    public void setArenaManager(IArenaManager arenaManager) {
        this.arenaManager = arenaManager;
    }

    public void setDisplayConfigService(IDisplayConfigService displayConfigService) {
        this.displayConfigService = displayConfigService;
    }

    private IArenaManager arenaManager;
    private IDisplayConfigService displayConfigService;

    static final String DISPLAY_CONFIGS = "displayConfigs";
}

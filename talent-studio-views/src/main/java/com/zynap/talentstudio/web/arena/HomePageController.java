/*
 * Copyright (c) 2004 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.arena;

import com.zynap.domain.UserSession;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.arenas.Arena;
import com.zynap.talentstudio.arenas.IArenaManager;
import com.zynap.talentstudio.security.homepages.HomePage;

import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: aandersson
 * Date: 05-Feb-2004
 * Time: 15:02:32
 */
public class HomePageController implements Controller {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // default time out 20 mins
        int sessionTimeout = 20;

        UserSession userSession = ZynapWebUtils.getUserSession(request);
        final String currentArenaId = userSession.getCurrentArenaId();

        // get current details
        String arenaHomePage = "";
        try {
            Arena arena = arenaManager.getArena(currentArenaId);
            if (arena != null) {
                sessionTimeout = arena.getSessionTimeout();
                String arenaPath = arena.getUrl();
                arenaHomePage = arenaPath.substring(0, arenaPath.lastIndexOf('/'));
            }
        } catch (TalentStudioException e) {
            logger.error(e.getMessage(), e);
        }

        // set session timeout on HttpSession
        HttpSession session = request.getSession();

        // convert to secs
        session.setMaxInactiveInterval(sessionTimeout * 60);

        HomePage value = ZynapWebUtils.getHomePage(request);
        Map<String, Object> model = new HashMap<String, Object>();

        if (value != null) {

            model.put("homePage", value);
            model.put("arenaContext", arenaHomePage);
        }

        // redirect to view - consists of arena id + _home eg: adminmodule_home
        return new ModelAndView(currentArenaId.toLowerCase() + "_home", model);
    }

    public void setArenaManager(IArenaManager arenaManager) {
        this.arenaManager = arenaManager;
    }

    private IArenaManager arenaManager;
    protected final Log logger = LogFactory.getLog(getClass());
}

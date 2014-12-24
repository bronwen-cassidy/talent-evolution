/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.arena;

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.arenas.Arena;
import com.zynap.talentstudio.arenas.IArenaMenuHandler;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.common.ParameterConstants;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * Interceptor that checks that arenas are active and prevents access if they are not.
 * <br/> Complements SecurityInterceptor.
 *
 * @author amark
 */
public class ArenaInterceptor extends HandlerInterceptorAdapter {

    /**
     * Check if arena is active.
     * <br/> Also sets current arena id on UserSession if arena is active.
     *
     * @param request
     * @param response
     * @param handler
     * @return true by default; only returns false if the arena is inactive.
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        UserSession userSession = ZynapWebUtils.getUserSession(request);

        // check if this URL is allowed
        boolean allowed = ZynapWebUtils.urlMatches(excludedURLs, request);
        if (!allowed) {

            // find matching arena
            final Collection arenas = arenaMenuHandler.getActiveArenas();
            final String requestURI = ZynapWebUtils.getRequestURIWithoutContextPath(request);
            final Arena arena = (Arena) CollectionUtils.find(arenas, new ArenaURLPredicate(requestURI));

            if (arena != null) {
                // if found set current arena id
                userSession.setCurrentArenaId(arena.getArenaId());
                request.setAttribute(ParameterConstants.ARENAS_MENU_PARAM, userSession.getArenas());
                request.setAttribute(ParameterConstants.CURRENT_ARENA_ID_PARAM, userSession.getCurrentArenaId());
                request.setAttribute(ParameterConstants.USER_PRINCIPAL_PARAM, userSession.getUserPrincipal());
                request.setAttribute(ParameterConstants.ARENA, arena);
                
            } else {
                // otherwise redirect to access denied page and return false to indicate that request should end here
                // todo determine whether this should be set to null?
                // userSession.setCurrentArenaId(null);
                response.sendRedirect(ZynapWebUtils.addContextPath(request, accessDeniedURL));

                return false;
            }
        }

        return true;
    }

    class ArenaURLPredicate implements Predicate {

        private String url;

        public ArenaURLPredicate(String url) {
            this.url = url;
        }

        public boolean evaluate(Object object) {
            Arena arena = (Arena) object;
            String arenaURL = arena.getBaseURL();

            return StringUtils.hasText(arenaURL) && url.startsWith(arenaURL);
        }
    }

    public void setArenaMenuHandler(IArenaMenuHandler arenaMenuHandler) {
        this.arenaMenuHandler = arenaMenuHandler;
    }

    public void setAccessDeniedURL(String accessDeniedURL) {
        this.accessDeniedURL = accessDeniedURL;
    }

    public void setExcludedURLs(Collection excludedURLs) {
        this.excludedURLs = excludedURLs;
    }

    private IArenaMenuHandler arenaMenuHandler;

    /**
     * The URL to redirect to if access is denied.
     */
    private String accessDeniedURL;

    /**
     * The Collection of URLs that are ignored by this interceptor.
     */
    private Collection excludedURLs;
}

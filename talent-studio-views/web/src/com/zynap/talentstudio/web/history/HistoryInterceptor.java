package com.zynap.talentstudio.web.history;

import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collection;


/**
 * User: amark
 * Date: 23-Dec-2004
 * Time: 14:10:53
 */
public class HistoryInterceptor extends HandlerInterceptorAdapter {

    /**
     * Save request and session data required for history.
     *
     * @param request  The HttpServletRequest
     * @param response The HttpServletResponse
     * @param handler  The Handler
     * @return true always
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        boolean ignore = ZynapWebUtils.urlMatches(excludedURLs, request);
        if (!ignore) {
            HistoryHelper.saveHistory(request);
        }

        return true;
    }

    /**
     * Tidy up any "used" history and remove any unused history.
     *
     * @param request      The HttpServletRequest
     * @param response     The HttpServletResponse
     * @param handler      The Handler
     * @param modelAndView The current model and view
     * @throws Exception
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        boolean ignore = ZynapWebUtils.urlMatches(excludedURLs, request);
        if (!ignore) {
            HistoryHelper.clearHistory(request);
            HistoryHelper.copyToken(request);
            ZynapWebUtils.savePreviousURI(request);
        }
    }


    public Collection getExcludedURLs() {
        return excludedURLs;
    }

    public void setExcludedURLs(Collection excludedURLs) {
        this.excludedURLs = excludedURLs;
    }

    private Collection excludedURLs;
}
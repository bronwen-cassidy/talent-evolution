package com.zynap.talentstudio.web.security;

import com.zynap.domain.UserPrincipal;
import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.talentstudio.security.ISecurityManager;
import com.zynap.talentstudio.security.permits.IPermit;
import com.zynap.talentstudio.security.roles.IRoleManager;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.web.SessionConstants;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Interceptor that checks if user can access the specified resource based on their role.
 * <p/>
 * User: amark
 * Date: 25-Jan-2005
 * Time: 17:30:56
 */
public class SecurityInterceptor extends HandlerInterceptorAdapter {

    /**
     * The logger.
     */
    private static final Log logger = LogFactory.getLog(SecurityInterceptor.class);

    /**
     * Check that the user has the correct security to access the resource.
     * <br> By default returns false disallowing access and redirects the user to a URL - which URL to use is configured
     * by setting the <code>accessDeniedURL</code> property of this class in the Spring configuration file (eg: talentstudio-servlet.xml)
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  chosen handler to execute, for type and/or instance evaluation
     * @return true if the user is allowed access.
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // check if url needs security check
        boolean allowAccess = ZynapWebUtils.urlMatches(excludedURLs, request);

        // if access not allowed check permits
        if (!allowAccess) {

            final UserPrincipal user = ZynapWebUtils.getUserSession(request).getUserPrincipal();
            final Long userId = user.getUserId();

            final String url = request.getServletPath();
            try {
                final List<IPermit> permits = findPermits(url);
                logger.debug(permits.size() + " permits found for URL: " + url + " - user id is: " + userId);

                if (!permits.isEmpty()) {

                    for (IPermit permit : permits) {
                        allowAccess = permit.isAccess() ? user.hasAccessPermit(permit) : checkDomainObjectPermit(user, permit, permits, request);
                        // if we have found a match we can quit this loop and continue.
                        if (allowAccess) break;
                    }

                }
            } catch (DomainObjectNotFoundException e) {

                // redirect to view for object not found
                response.sendRedirect(ZynapWebUtils.addContextPath(request, objectNotFoundURL));

                // return false to stop further processing
                return false;

            } catch (Throwable e) {
                logger.error("Failed to check security permissions because of: ", e);
            }

            // if still have no access log and redirect to no access page
            if (!allowAccess) {

                logger.error("Access denied to URL: " + url + "?" + request.getQueryString() + " for user: " + userId);
                response.sendRedirect(ZynapWebUtils.addContextPath(request, accessDeniedURL));
            }
        }

        return allowAccess;
    }

    private boolean checkDomainObjectPermit(UserPrincipal user, IPermit permit, Collection permits, HttpServletRequest request) throws DomainObjectNotFoundException {

        String contentParam = permit.getContentParam();
        if (StringUtils.hasText(contentParam)) {
            final String contentType = ZynapWebUtils.getValue(request, contentParam);
            if (StringUtils.hasText(contentType)) {
                permit = (IPermit) CollectionUtils.find(permits, new ContentTypePredicate(contentType));
            }
        }

        final String docScope = request.getParameter(DOC_TYPE_PARAM);
        if (StringUtils.hasText(docScope)) {
            if (PUBLIC_SCOPE.equals(docScope)) {
                // go no further
                return true;
            }
        }

        String idParam = permit.getIdParam();
        if (StringUtils.hasText(idParam)) {
            Long[] nodeIds = getNodeIds(request, idParam);
            boolean result = false;
            for (int i = 0; i < nodeIds.length; i++) {
                result = securityManager.checkAccess(permit, user, nodeIds[i]);
                // exit the loop if user does not have access to every single one of the nodes
                if (!result) break;
            }

            return result;
        } else {
            logger.warn("DomainObjectPermit " + permit.getId() + " has no idParam associated");
        }

        return false;
    }

    private Long[] getNodeIds(HttpServletRequest request, String idParam) {

        if (request.getParameterValues(idParam) != null && request.getParameterValues(idParam).length > 1) {
            List<Long> longIds = new ArrayList<Long>();
            for (int i = 0; i < request.getParameterValues(idParam).length; i++) {
                String requestParam = request.getParameterValues(idParam)[i];
                Long nodeId = getValueAsLong(requestParam);
                if (nodeId != null && !longIds.contains(nodeId)) longIds.add(nodeId);
            }
            return longIds.toArray(new Long[longIds.size()]);
        }

        Long nodeId = getValueAsLong(request.getParameter(idParam));
        if (nodeId == null) {
            nodeId = getValueAsLong(request.getAttribute(idParam));
        }
        if (nodeId == null) {
            nodeId = getValueAsLong(request.getSession().getAttribute(idParam));
        }
        if (nodeId == null) {
            int index = idParam.indexOf('.');
            if (index > 0) {
                String commandName = idParam.substring(0, index);
                Object command = getCommand(request, commandName);
                if (command != null) {
                    try {
                        String nodeSubString = idParam.substring(index + 1);
                        nodeId = getValueAsLong(BeanUtils.getNestedProperty(command, nodeSubString.substring(nodeSubString.indexOf(".") + 1)));
                    } catch (Exception e) {
                        // e.printStackTrace();
                        // todo exception thrown from here is java.lang.NoSuchMethodException: Unknown property 'node'
                    }
                }
            }
        }
        return new Long[]{nodeId};
    }

    private Object getCommand(HttpServletRequest request, String commandName) {

        Object command = null;

        if (HistoryHelper.isHistoryBackRequest(request)) {
            command = HistoryHelper.getLastCommand(request);
        }

        if (command == null) {
            String name = (String) request.getSession().getAttribute(SessionConstants.CONTROLLER_NAME);
            command = request.getSession().getAttribute(name + ".FORM." + commandName);
        }

        return command;
    }

    private Long getValueAsLong(Object object) {
        try {
            return Long.valueOf(object.toString());
        } catch (Throwable e) {
            return null;
        }
    }


    /**
     * Find permits.
     * <br> The list will be empty if no permits are found that match the URL.
     *
     * @param url The URL
     * @return List
     */
    private List<IPermit> findPermits(String url) {

        if (activePermits == null) {
            activePermits = roleManager.getActivePermits();
        }
        
        
        List<IPermit> permits = new ArrayList<IPermit>();
        for (IPermit permit : activePermits) {
            Matcher matcher = Pattern.compile(permit.getUrl(), Pattern.CASE_INSENSITIVE).matcher(url);
            if (matcher.matches()) permits.add(permit);
        }

        return permits;
    }


    public void setRoleManager(IRoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public void setSecurityManager(ISecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    public void setExcludedURLs(Collection<String> excludedURLs) {
        this.excludedURLs = excludedURLs;
    }

    public void setAccessDeniedURL(String accessDeniedURL) {
        this.accessDeniedURL = accessDeniedURL;
    }

    public void setObjectNotFoundURL(String objectNotFoundURL) {
        this.objectNotFoundURL = objectNotFoundURL;
    }

    /**
     * Predicate that matches IPermits based on content.
     */
    private static class ContentTypePredicate implements Predicate {

        public ContentTypePredicate(String contentType) {
            this.contentType = contentType;
        }

        public boolean evaluate(Object object) {
            IPermit permit = (IPermit) object;
            return permit.getContent().equals(contentType);
        }

        private final String contentType;
    }

    /**
     * The URL to redirect to if a node is not found
     */
    private String objectNotFoundURL;

    /**
     * The URL to redirect to if access is denied.
     */
    private String accessDeniedURL;

    /**
     * The Collection of URLs this interceptor ignores.
     */
    private Collection<String> excludedURLs;

    /**
     * The Collection of active {@link IPermit} for the application - cached for performance reasons.
     */
    private Collection<IPermit> activePermits;

    /**
     * Used to find the list of active {@link com.zynap.talentstudio.security.permits.IPermit}s.
     */
    private IRoleManager roleManager;

    /**
     * Used to check domain permits.
     */
    private ISecurityManager securityManager;
    private static final String DOC_TYPE_PARAM = "doc_type";
    private static final String PUBLIC_SCOPE = "PUBLIC";

    private static final Pattern tester = Pattern.compile(".*/.*/viewmy*.*.htm");
}


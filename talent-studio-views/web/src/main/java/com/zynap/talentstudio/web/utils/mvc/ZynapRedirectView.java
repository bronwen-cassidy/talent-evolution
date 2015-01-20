package com.zynap.talentstudio.web.utils.mvc;

import com.zynap.talentstudio.web.history.HistoryHelper;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Iterator;

/**
 * User: amark
 * Date: 13-Jan-2005
 * Time: 09:28:52
 */
public class ZynapRedirectView extends RedirectView {

    /**
     * Constructor for use as a bean.
     */
    public ZynapRedirectView() {
    }

    /**
     * Create a new RedirectView with the given URL.
     *
     * @param url the URL to redirect to
     */
    public ZynapRedirectView(String url) {
        super(url);
    }

    /**
     * Create a new RedirectView with the given URL.
     * <p>The given URL will be considered as relative to the web server,
     * not as relative to the current ServletContext.
     *
     * @param url the URL to redirect to
     * @param idParamName The name of the id parameter
     * @param idParam The id parameter value
     */
    public ZynapRedirectView(String url, String idParamName, Object idParam) {
        super(url);
        addStaticAttribute(idParamName, idParam);
    }

    /**
     * Create a new RedirectView with the given URL.
     * <p>The given URL will be considered as relative to the web server,
     * not as relative to the current ServletContext.
     *
     * @param url the URL to redirect to
     * @param additionalParameters A Map of additional parameters to add to the request
     */
    public ZynapRedirectView(String url, Map additionalParameters) {
        super(url);

        if (additionalParameters != null) {
            for (Iterator iterator = additionalParameters.entrySet().iterator(); iterator.hasNext();) {
                Map.Entry entry = (Map.Entry) iterator.next();
                final String name = entry.getKey().toString();
                addStaticAttribute(name, entry.getValue());
            }
        }
    }

    /**
     * Send a redirect back to the HTTP client.
     *
     * @param request          current HTTP request (allows for reacting to request method)
     * @param response         current HTTP response (for sending response headers)
     * @param targetUrl        the target URL to redirect to
     * @param http10Compatible whether to stay compatible with HTTP 1.0 clients
     * @throws java.io.IOException if thrown by response methods
     */
    protected void sendRedirect(HttpServletRequest request, HttpServletResponse response, String targetUrl, boolean http10Compatible) throws IOException {

        targetUrl = HistoryHelper.addTokenToURL(request, targetUrl, false);
        super.sendRedirect(request, response, targetUrl, http10Compatible);
    }
}

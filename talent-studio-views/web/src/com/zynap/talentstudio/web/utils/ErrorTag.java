package com.zynap.talentstudio.web.utils;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Iterator;

/**
 * User: amark
 * Date: 15-Dec-2004
 * Time: 10:08:39
 * Tag library that displays an exception.
 * <br> Handles exceptions handled by <code>org.springframework.web.servlet.handler.SimpleMappingExceptionResolver</code>
 * and exceptions produced by the Servlet engine as well (eg: JSP compilation errors)
 */
public class ErrorTag extends TagSupport {

    /**
     * Logger.
     */
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * Looks up the exception and prints it out.
     *
     * @return SKIP_BODY
     * @throws JspException
     */
    public int doStartTag() throws JspException {

        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        // check for Spring throwable, then for servlet engine throwable        
        Throwable throwable = ZynapWebUtils.getSpringException(request);
        if (throwable == null) throwable = ZynapWebUtils.getException(request);

        // get stack trace
        String stackTrace = (throwable != null) ? ExceptionUtils.getStackTrace(throwable) : "";

        // check if we want to display errors to user
        final boolean displayErrors = Boolean.valueOf(pageContext.getServletContext().getInitParameter("displayErrors")).booleanValue();
        if (displayErrors) {
            try {
                JspWriter out = pageContext.getOut();
                out.print(stackTrace);
            } catch (IOException e) {
                throw new JspException(e);
            }
        }

        logError(request, throwable, stackTrace);

        return SKIP_BODY;
    }

    private void logError(HttpServletRequest request, Throwable exception, String stackTrace) {

        Long userId = null;
        try {
            userId = ZynapWebUtils.getUserId(request);
        } catch (Exception e) {
            //
        }

        StringBuffer sb = new StringBuffer();
        sb.append("\nLogged In:\n");

        sb.append(userId != null ? userId : "null");

        sb.append("\nSession ID:\n");
        final HttpSession session = request.getSession(false);
        sb.append(session != null ? session.getId() : "null");

        sb.append("\nRequest\n");
        sb.append(ZynapWebUtils.getCurrentURI(request)).append("?").append(request.getQueryString());

        sb.append("\nRequest Headers\n");
        for (Enumeration e = request.getHeaderNames(); e.hasMoreElements();) {
            String name = (String) e.nextElement();
            sb.append(name).append(" = ").append(request.getHeader(name)).append("\n");
        }

        sb.append("\nRequest Parameters\n");
        Map requestParams = request.getParameterMap();
        if (requestParams != null) {
            for(Iterator iter = requestParams.entrySet().iterator(); iter.hasNext();) {
                Map.Entry entry = (Map.Entry) iter.next();
                sb.append(entry.getKey()).append(" -> ").append(entry.getValue());
            }
        }

        sb.append("\nException\n");
        if (exception != null) {
            sb.append(exception.getMessage());
            sb.append("\n");
            sb.append(stackTrace);
        }

        logger.error(sb);
    }
}

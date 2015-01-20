package com.zynap.talentstudio.web.common.html.link;

import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import java.util.HashMap;
import java.util.Map;

/**
 * User: amark
 * Date: 21-Jul-2005
 * Time: 09:12:21
 */
public class LinkTag extends BodyTagSupport {

    private String url;

    private String var;

    private Map<String, Object> parameters = new HashMap<String, Object>();

    public final String getUrl() {
        return url;
    }

    public final void setUrl(String url) throws JspException {
        this.url = ExpressionEvaluationUtils.evaluateString("url", url, pageContext);
        //this.url = new UrlPathHelper().getOriginatingRequestUri((HttpServletRequest) pageContext.getRequest());
    }

    public final String getVar() {
        return var;
    }

    public final void setVar(String var) {
        this.var = var;
    }

    public final Map<String, Object> getParameters() {
        return parameters;
    }

    public void release() {
        super.release();

        reset();
    }

    public final void addParameter(String key, String value) {
        parameters.put(key, value);
    }

    public int doEndTag() {

        String link = buildLink();
        pageContext.setAttribute(var, link, PageContext.REQUEST_SCOPE);

        reset();

        return EVAL_PAGE;
    }

    /**
     * Build link.
     *
     * @return un-encoded URL as string (no additional parameters).
     */
    protected String buildLink() {
        return buildUrl(false);
    }

    /**
     * Build the URL - removes any query string and then adds the specified parameters.
     *
     * @param uri is this a uRI or a URL
     * @return The url
     */
    protected String buildUrl(boolean uri) {
        return ZynapWebUtils.buildURL(ZynapWebUtils.removeQueryString(url), parameters, uri);
    }

    /**
     * Clear state.
     */
    private void reset() {
        var = null;
        url = null;
        parameters.clear();
    }
}

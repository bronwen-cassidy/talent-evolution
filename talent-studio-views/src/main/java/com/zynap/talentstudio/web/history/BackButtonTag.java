package com.zynap.talentstudio.web.history;

import com.zynap.util.resource.PropertiesManager;

import com.zynap.web.tag.ZynapTagSupport;

import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.util.StringUtils;
import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Taglibrary that builds form with back button based on the URL stored in the session and keyed on the historyToken.
 * <br> This is stored in the session by the <code>SaveURLTag</code>.
 * <p/>
 * User: amark
 * Date: 23-Dec-2004
 * Time: 09:26:24
 */
public class BackButtonTag extends ZynapTagSupport {

    private static final PropertiesManager PROPERTIES_MANAGER = PropertiesManager.getInstance(BackButtonTag.class);

    private static final String DEFAULT_BUTTON_TYPE = "button";

    private String label;

    private String cssClass;

    private String defaultUrl;

    /**
     * The method attribute to be used in the form.
     * <br> Defaults to {@link ZynapWebUtils#POST_METHOD}.
     */
    private String method = ZynapWebUtils.POST_METHOD;

    private String buttonType = DEFAULT_BUTTON_TYPE;

    /**
     * Build form for back button.
     *
     * @return EVAL_PAGE
     * @throws JspException
     */
    protected int doInternalStartTag() throws Exception {

        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

        StringBuffer stringBuffer = new StringBuffer();

        if (StringUtils.hasText(defaultUrl)) {

            startForm(request, response, defaultUrl, stringBuffer);

            // add hidden field with history token if request has one.
            String token = HistoryHelper.getCurrentToken(request);
            if (StringUtils.hasText(token)) {
                String tokenField = getProperty("history.token.field");
                stringBuffer.append(replaceTokens(tokenField, new Object[]{token}));
            }

            addButton(stringBuffer);
            closeForm(stringBuffer);

        } else {

            // check to see if there are any objects in the command history first
            // if none found try the token-based history
            SavedURL lastURL = HistoryHelper.getLastURL(request);
            if (lastURL == null) {
                lastURL = HistoryHelper.getCurrentURL(request);
            }

            if (lastURL != null) {
                String url = lastURL.getURL();
                Map<String, Object> parameterMap = lastURL.getParameters();
                // TS-2271 make sure the history token is present
                String parameter = request.getParameter(HistoryHelper.TOKEN_KEY);
                if (parameter != null && !parameterMap.containsKey(HistoryHelper.TOKEN_KEY)) {
                    parameterMap.put(HistoryHelper.TOKEN_KEY, new String[]{parameter});
                }

                startForm(request, response, url, stringBuffer);

                Set entries = parameterMap.entrySet();
                appendParameters(stringBuffer, entries);
                addButton(stringBuffer);
                closeForm(stringBuffer);
            }

        }

        out.write(stringBuffer.toString());

        return EVAL_PAGE;
    }

    private void appendParameters(StringBuffer stringBuffer, Set entries) {
        String hiddenField = getProperty("form.hidden.field");
        
        for (Iterator iterator = entries.iterator(); iterator.hasNext();) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String key = (String) entry.getKey();
            Object value = entry.getValue();

            if (value != null) {
                if (value instanceof String[]) {
                    String[] values = (String[]) value;
                    for (int i = 0; i < values.length; i++) {
                        stringBuffer.append(replaceTokens(hiddenField, new Object[]{key, values[i]}));
                    }
                } else {
                    stringBuffer.append(replaceTokens(hiddenField, new Object[]{key, value.toString()}));
                }
            }
        }
    }

    private void startForm(HttpServletRequest request, HttpServletResponse response, String url, StringBuffer stringBuffer) {

        if (url.startsWith(ZynapWebUtils.PATH_SEPARATOR)) {
            url = ZynapWebUtils.addContextPath(request, url);
        }

        String start = getProperty("form.start");
        Object[] tokens = new Object[]{method, response.encodeURL(url)};
        stringBuffer.append(replaceTokens(start, tokens));
    }

    private void closeForm(StringBuffer stringBuffer) {
        stringBuffer.append(getProperty("form.end"));
    }

    private void addButton(StringBuffer stringBuffer) {
        String button = getProperty("form.button");
        stringBuffer.append(replaceTokens(button, new Object[]{cssClass, buttonType, label}));
    }

    /**
     * Reset all attributes to default values.
     */
    public void release() {
        label = null;
        cssClass = null;
        defaultUrl = null;
        buttonType = DEFAULT_BUTTON_TYPE;
        method = ZynapWebUtils.POST_METHOD;
    }

    public void setButtonType(String buttonType) throws JspException {
        this.buttonType = ExpressionEvaluationUtils.evaluateString("buttonType", buttonType, pageContext);
    }

    public void setDefaultUrl(String defaultUrl) throws JspException {
        this.defaultUrl = ExpressionEvaluationUtils.evaluateString("defaultUrl", defaultUrl, pageContext);
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public void setLabel(String label) throws JspException {
        this.label = ExpressionEvaluationUtils.evaluateString("label", label, pageContext);
    }

    public void setMethod(String method) {
        this.method = method;
    }

    private String getProperty(String propertyName) {
        return PROPERTIES_MANAGER.getString(propertyName);
    }
}

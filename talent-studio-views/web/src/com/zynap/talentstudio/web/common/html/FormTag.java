package com.zynap.talentstudio.web.common.html;

import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.util.format.MessageTemplateFormatter;
import com.zynap.util.resource.PropertiesManager;
import org.springframework.util.StringUtils;
import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;


/**
 * Tag library that generates HTML form tags with hidden fields if required.
 * Has 6 attributes - "method" (expects either GET or POST case-insensitive) "action" (the URL),
 * "onSubmit" (for javascript), "encType", "name" and "target".
 * The only required attribute is "method".
 *
 * @author Angus Mark
 */
public class FormTag extends TagSupport {

    /**
     * Constant for default encType.
     */
    private static final String DEFAULT_ENC_TYPE = "application/x-www-form-urlencoded";

    /**
     * PropertiesManager for looking up HTML content from properties file.
     */
    protected static final PropertiesManager propertiesManager = PropertiesManager.getInstance(FormTag.class);

    /**
     * The method attribute.
     */
    private String method;

    /**
     * The htmlId attribute.
     */
    private String htmlId;

    /**
     * The name of the form.
     */
    protected String name;

    /**
     * The action attribute.
     */
    private String action;

    /**
     * The onSubmit attribute.
     */
    private String onSubmit;

    /**
     * The encryption type of the form - defaults to "application/x-www-form-urlencoded".
     */
    private String encType = DEFAULT_ENC_TYPE;

    /**
     * The target for the form.
     */
    private String target;

    /**
     * indicates if you want to include or exclude history token.
     */
    private boolean excludeHistory;

    /**
     * Setter for excludeHistory attribute.
     *
     * @param excludeHistory The new value
     */
    public void setExcludeHistory(String excludeHistory) {
        this.excludeHistory = Boolean.valueOf(excludeHistory).booleanValue();
    }

    /**
     * Setter for target attribute.
     *
     * @param target The new value
     */
    public void setTarget(String target) {
        this.target = target;
    }

    /**
     * Setter for encType attribute.
     *
     * @param encType The new value
     */
    public void setEncType(String encType) {
        this.encType = encType;
    }

    /**
     * Setter for name attribute.
     *
     * @param name The new value
     */
    public void setName(String name) throws JspException {
        this.name = ExpressionEvaluationUtils.evaluateString("name", name, pageContext);
    }

    /**
     * Setter for htmlId attribute.
     *
     * @param htmlId The new value
     */
    public void setHtmlId(String htmlId) throws JspException {
        this.htmlId = ExpressionEvaluationUtils.evaluateString("id", htmlId, pageContext);
    }

    /**
     * Setter for method attribute.
     *
     * @param method The new value
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * Setter for action attribute.
     *
     * @param action The new value
     * @throws JspException
     */
    public void setAction(String action) throws JspException {
        this.action = ExpressionEvaluationUtils.evaluateString("action", action, pageContext);
    }

    /**
     * Setter for onSubmit attribute.
     *
     * @param onSubmit The new value
     */
    public void setOnSubmit(String onSubmit) {
        this.onSubmit = onSubmit;
    }

    /**
     * Writes out start of HTML form using the parameters supplied.
     *
     * @return EVAL_BODY_INCLUDE
     * @throws JspException If the action is not null but does not starts with a "/".
     */
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        try {
            startForm(request, out);
        } catch (Throwable e) {
            throw new JspException(e);
        }

        return EVAL_BODY_INCLUDE;
    }

    protected final void addAttribute(StringBuffer attrs, String attrName, String value) {
        if (StringUtils.hasText(value)) {
            attrs.append(attrName).append("=\"").append(value).append("\" ");
        }
    }

    protected final void startForm(HttpServletRequest request, JspWriter out) throws IOException {
        StringBuffer attributes = new StringBuffer();

        addAttribute(attributes, "method", method);
        addAttribute(attributes, "encType", encType);
        addAttribute(attributes, "target", target);
        addAttribute(attributes, "name", name);

        // add id - if none provided use name
        addAttribute(attributes, "id", StringUtils.hasText(htmlId) ? htmlId : name);

        String actionParameter = null;
        if (StringUtils.hasText(action)) {

            // if action starts with / append context path; otherwise use action directly
            if (action.startsWith(ZynapWebUtils.PATH_SEPARATOR)) {
                actionParameter = ZynapWebUtils.addContextPath(request, action);
            } else {
                actionParameter = action;
            }
        }

        addAttribute(attributes, "action", actionParameter);
        addAttribute(attributes, "onSubmit", onSubmit);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("attributes", attributes.toString());

        MessageTemplateFormatter formatter = new MessageTemplateFormatter(propertiesManager.getString("form.start"));
        out.println(formatter.format(parameters));

        // add hidden field with history token if request has one and history is not to be excluded
        String token = HistoryHelper.getCurrentToken(request);
        if (!excludeHistory && StringUtils.hasText(token)) {
            formatter = new MessageTemplateFormatter(propertiesManager.getString("history.token.field"));
            parameters.clear();
            parameters.put(HistoryHelper.TOKEN_KEY, token);
            out.println(formatter.format(parameters));
        }
    }

    /**
     * Writes out end of HTML form.
     *
     * @return EVAL_PAGE
     * @throws JspException
     */
    public final int doEndTag() throws JspException {
        JspWriter out = pageContext.getOut();

        try {
            out.println(propertiesManager.getString("form.end"));
        } catch (Throwable e) {
            throw new JspException(e);
        }

        return EVAL_PAGE;
    }

    /**
     * Reset all attributes to default values.
     */
    public void release() {
        this.method = null;
        this.action = null;
        this.onSubmit = null;
        this.name = null;
        this.htmlId = null;
        this.encType = DEFAULT_ENC_TYPE;
        this.target = null;
        this.excludeHistory = false;
    }
}

package com.zynap.talentstudio.web.common.html;

import com.zynap.util.resource.PropertiesManager;

import com.zynap.web.tag.ZynapTagSupport;

import org.springframework.util.StringUtils;

import javax.servlet.jsp.JspException;

/**
 * User: amark
 * Date: 02-Jun-2006
 * Time: 11:09:08
 */
public class ButtonTag extends ZynapTagSupport {

    private static final PropertiesManager PROPERTIES_MANAGER = PropertiesManager.getInstance(ButtonTag.class);

    private String type;
    private String value;
    private String name;
    private String cssClass;
    private String htmlId;
    private String onclick;
    private String disableonclick = Boolean.TRUE.toString();

    protected int doInternalStartTag() throws Exception {

        boolean mustDisable = Boolean.valueOf(disableonclick).booleanValue();

        final String key = mustDisable ? DISABLEABLE_BUTTON_FORMAT : BUTTON_FORMAT;
        final String format = PROPERTIES_MANAGER.getString(key);

        // use name if no id supplied
        final String tagId = StringUtils.hasText(htmlId) ? htmlId : name;

        if (onclick == null) onclick = "";

        Object[] tokens = new Object[]{cssClass, type, tagId, name, value, onclick};
        final String output = replaceTokens(format, tokens);
        out.write(output);

        return EVAL_PAGE;
    }

    public void release() {
        super.release();

        type = null;
        value = null;
        name = null;
        cssClass = null;
        htmlId = null;
        onclick = null;
        disableonclick = Boolean.TRUE.toString();
    }

    public void setType(String type) throws JspException {
        this.type = evaluateExpression("type", type);
    }

    public void setValue(String value) throws JspException {
        this.value = evaluateExpression("value", value);
    }

    public void setName(String name) throws JspException {
        this.name = evaluateExpression("name", name);
    }

    public void setCssClass(String cssClass) throws JspException {
        this.cssClass = evaluateExpression("cssClass", cssClass);
    }

    public void setHtmlId(String htmlId) throws JspException {
        this.htmlId = evaluateExpression("htmlId", htmlId);
    }

    public void setOnclick(String onclick) throws JspException {
        this.onclick = evaluateExpression("onclick", onclick);
    }

    public void setDisableonclick(String disableonclick) throws JspException {
        this.disableonclick = evaluateExpression("disableonclick", disableonclick);
    }

    private static final String DISABLEABLE_BUTTON_FORMAT = "disableable.button";
    private static final String BUTTON_FORMAT = "button";
}

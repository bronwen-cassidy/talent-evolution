package com.zynap.talentstudio.web.common.html.link;

import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * User: amark
 * Date: 21-Jul-2005
 * Time: 09:25:41
 *
 * Tag library that
 */
public class LinkParameterTag extends BodyTagSupport {

    private String name;

    private String value;

    public void setName(String name) throws JspException {
        this.name = ExpressionEvaluationUtils.evaluateString("name", name, pageContext);
    }

    public void setValue(String value) throws JspException {
        this.value = ExpressionEvaluationUtils.evaluateString("value", value, pageContext);
    }

    public void release() {
        super.release();

        name = null;
        value = null;
    }

    /**
     * Add name and value to list of parameters on parent {@link LinkTag}.
     * @return EVAL_PAGE always
     * @throws JspException
     */
    public int doEndTag() throws JspException {

        final LinkTag parentLink = (LinkTag) findAncestorWithClass(this, LinkTag.class);
        if (parentLink == null) {
            throw new JspTagException(getClass().getName() + " has no parent of type " + LinkTag.class);
        }

        parentLink.addParameter(name, value);

        return EVAL_PAGE;
    }
}

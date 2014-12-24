/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.common.html;

import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 05-Feb-2010 11:29:59
 */
public class MessageParameterTag extends BodyTagSupport {


    public void setValue(String value) throws JspException {
        if (value != null) {
            this.value = ExpressionEvaluationUtils.evaluateString("value", value, pageContext);
        }
    }

    public void release() {
        super.release();
        value = null;
    }

    /**
     * Add name and value to list of parameters on parent {@link com.zynap.talentstudio.web.common.html.ZynapMessageTag}.
     * @return EVAL_PAGE always
     * @throws JspException
     */
    public int doEndTag() throws JspException {

        final ZynapMessageTag parentLink = (ZynapMessageTag) findAncestorWithClass(this, ZynapMessageTag.class);
        if (parentLink == null) {
            throw new JspTagException(getClass().getName() + " has no parent of type " + ZynapMessageTag.class);
        }

        parentLink.addParameter(value);

        return EVAL_PAGE;
    }

    private String value;

}

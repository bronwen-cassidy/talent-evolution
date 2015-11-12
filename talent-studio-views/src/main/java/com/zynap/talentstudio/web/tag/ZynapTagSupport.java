/*
 * Copyright (c) 2004 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import java.text.MessageFormat;

/**
 * Created by IntelliJ IDEA.
 * User: aandersson
 * Date: 11-Feb-2004
 * Time: 14:30:21
 */
public abstract class ZynapTagSupport extends TagSupport {

    /**
     * Logger for this class and subclasses.
     */
    protected final Log logger = LogFactory.getLog(getClass());

    protected JspWriter out;

    public final int doStartTag() throws JspException {

        out = pageContext.getOut();
        try {
            return doInternalStartTag();
        } catch (Exception e) {
            final String message = "Error in Tag " + this.getClass().getName();
            throw new JspException(message, e);
        }
    }

    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    /**
     * Utility method that replaces tokens (place holders) within the html properties
     * for labels and urls.
     *
     * @param html
     * @param tokens
     * @return String The string that has been created by replacing the tokens
     */
    public final static String replaceTokens(String html, Object... tokens) {
        return MessageFormat.format(html, tokens);
    }

    public final void setId(String id) {
        try {
            this.id = ExpressionEvaluationUtils.evaluateString("id", id, pageContext);
        } catch (JspException e) {
            logger.error("Failed to get id from pageContext", e);
        }
    }

    public final String getId() {
        return super.getId();
    }

    protected String evaluateExpression(String attributeName, String type) throws JspException {
        return ExpressionEvaluationUtils.evaluateString(attributeName, type, pageContext);
    }

    protected abstract int doInternalStartTag() throws Exception;
}

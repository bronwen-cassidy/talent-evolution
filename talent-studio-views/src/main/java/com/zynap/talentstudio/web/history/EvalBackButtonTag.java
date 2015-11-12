/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.history;

import com.zynap.talentstudio.web.tag.ZynapTagSupport;

import org.springframework.util.StringUtils;
import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 05-Apr-2006 08:06:11
 */
public class EvalBackButtonTag extends ZynapTagSupport {

    protected int doInternalStartTag() throws Exception {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        if (StringUtils.hasText(defaultUrl) || HistoryHelper.getCurrentURL(request) != null || HistoryHelper.getLastURL(request) != null) {
            return EVAL_BODY_INCLUDE;
        } else {
            return SKIP_BODY;
        }
    }

    public String getDefaultUrl() {
        return defaultUrl;
    }

    public void setDefaultUrl(String defaultUrl) throws JspException {
        this.defaultUrl = ExpressionEvaluationUtils.evaluateString("defaultUrl", defaultUrl, pageContext);
    }

    private String defaultUrl;
}

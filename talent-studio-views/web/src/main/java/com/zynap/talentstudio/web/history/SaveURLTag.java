package com.zynap.talentstudio.web.history;

import com.zynap.talentstudio.web.common.html.link.LinkTag;

import org.springframework.web.util.ExpressionEvaluationUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * User: amark
 * Date: 12-Jan-2005
 * Time: 09:07:21
 *
 * Tag library that saves url in history so that it can be used by back buttons.
 */
public class SaveURLTag extends LinkTag {

    /**
     * Save current url and its parameters into the session with a random token as the key and then bind the token into
     * the request as an attribute so that back buttons can be built using this data later.
     *
     * @return EVAL_PAGE
     * @throws JspException
     */
    public int doEndTag() {

        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        HttpSession session = request.getSession();
        synchronized (session) {
            if (StringUtils.hasText(getUrl())) {
                HistoryHelper.save(request, getUrl(), getParameters());
            } else {
                HistoryHelper.save(request, getParameters());
            }
        }

        return EVAL_PAGE;
    }
}

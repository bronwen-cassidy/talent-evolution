/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.security.tags;

import com.zynap.domain.UserSession;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.web.tag.ZynapTagSupport;

import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

/**
 * Class or Interface description.
 *
 * @author taulant bajraktari
 * @version 0.1
 * @since 29-Jan-2009 11:38:51
 */
public class SecureButtonTag extends ZynapTagSupport {

    protected int doInternalStartTag() throws Exception {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        UserSession userSession = ZynapWebUtils.getUserSession(request);

        if (isRootOrCreator(userSession)) {
            return EVAL_BODY_INCLUDE;
        } else {
            return SKIP_BODY;
        }
    }

    private boolean isRootOrCreator(UserSession userSession) {
        if (userSession == null) return false;
        final User userInSession = userSession.getUser();
        if (userSession.isRoot()) return true;
        //check if it is the report creator
        if (userInSession.getId().equals(userId)) {
            return true;
        }
        return false;
    }

    public void release() {
        super.release();
        userId = null;
    }

    public void setUserId(Object userId) throws JspException {
        this.userId = (Long) ExpressionEvaluationUtils.evaluate("userId", userId.toString(), pageContext);
    }

    private Long userId;
}
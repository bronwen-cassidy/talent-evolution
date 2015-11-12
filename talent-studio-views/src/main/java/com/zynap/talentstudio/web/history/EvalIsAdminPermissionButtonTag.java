/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.history;

import com.zynap.talentstudio.web.tag.ZynapTagSupport;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.security.roles.Role;
import com.zynap.domain.UserSession;

import javax.servlet.http.HttpServletRequest;

import java.util.Collection;

/**
 * Class or Interface description.
 *
 * @author taulant bajraktari
 * @version 0.1
 * @since 29-Jan-2009 11:38:51
 */
public class EvalIsAdminPermissionButtonTag extends ZynapTagSupport {

    protected int doInternalStartTag() throws Exception {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        UserSession userSession = ZynapWebUtils.getUserSession(request);

        if (isAdminRole(userSession)) {
            return EVAL_BODY_INCLUDE;
        } else {
            return SKIP_BODY;
        }
    }

    private boolean isAdminRole(UserSession userSession) {
        if (userSession == null) return false;
        final Collection<Role> collection = userSession.getUserRoles();
        for (Role role : collection) {
            if (role.isAdminRole()) {
                return true;
            }
        }
        return false;
    }
}

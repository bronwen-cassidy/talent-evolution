/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.session;

import com.zynap.talentstudio.web.navigation.NavigationInterceptor;

import javax.servlet.http.HttpSession;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 05-Dec-2007 09:32:08
 */
public class NavSessionBean implements Serializable {

    public void setNavHidden(HttpSession session) {
        session.setAttribute(NavigationInterceptor.NAV_VISIBLE_STATE_ATTR, Boolean.FALSE);
    }

    public void setNavVisible(HttpSession session) {
        session.setAttribute(NavigationInterceptor.NAV_VISIBLE_STATE_ATTR, Boolean.TRUE);
    }
}

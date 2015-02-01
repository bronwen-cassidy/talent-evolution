/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.web;

/**
 * Interface that holds constants for names of session attributes
 * that are shared between presentation tier components (Controllers, Tag libraries, etc.)
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface SessionConstants {

    String USER_SESSION = "userSession";
    String CONTROLLER_NAME = "controllerName";
    String ACCESS_DENIED_URL = "/common/accessDeniedView.htm";
    String ERROR_VIEW = "/common/defaultErrorView.htm";
    String ERROR_NAME = "errorMessage";
    String USER_SESSION_ID = "user_session_id";
    String USER_ID = "userId";
    String PERMITS_DONE = "permitsDone";
    String SESSION_BUSY = "BUSY";
}

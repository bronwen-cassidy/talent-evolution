package com.zynap.talentstudio.web.security;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.talentstudio.web.SessionConstants;

import javax.servlet.http.HttpSession;

/**
* Created by bronwen.
* Date: 31/07/12
* Time: 12:57
*/
public class PermitHandler implements Runnable {

    public PermitHandler(Long uid, HttpSession session, IUserService userService) {
        this.uid = uid;
        this.session = session;
        this.userService = userService;
    }

    public void run() {
        try {
            userService.assignUserPermits(uid);

            if(session != null) {
                session.setAttribute(SessionConstants.PERMITS_DONE, Boolean.TRUE);
            }
        } catch (TalentStudioException e) {
            e.printStackTrace();
        }
    }

    private Long uid;
    private HttpSession session;
    private IUserService userService;
}

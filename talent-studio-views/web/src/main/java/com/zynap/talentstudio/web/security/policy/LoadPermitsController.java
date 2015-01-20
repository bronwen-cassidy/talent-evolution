package com.zynap.talentstudio.web.security.policy;

import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.exceptions.TalentStudioDataAccessException;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.web.SessionConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Controller that handles the user confirming that they accept the site usage policy.
 * <br> Redirects the user to the URL they originally requested.
 * <p/>
 * Date: 13-Oct-2004
 * Time: 10:11:54
 */
public class LoadPermitsController implements Controller {


    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        String username = ServletRequestUtils.getStringParameter(request, "uid", "");
        if(!StringUtils.hasLength(username)) return null;

        try {
            User user;
            try {
                user = userService.findByUserName(username);
            } catch (TalentStudioException e) {
                // user not found nothing to do
                return null;
            }
            if (user != null && user.getId() != null) {
                try {
                    userService.assignUserPermits(user.getId());
                    final HttpSession session = request.getSession(false);
                    if (session != null) {
                        session.setAttribute(SessionConstants.PERMITS_DONE, Boolean.TRUE);
                    }
                } catch (TalentStudioDataAccessException e) {
                    logger.error("user id was: " + user.getId() + " session was: " + request.getSession(false) + " error was: " + e.getMessage());
                }
            }
        } catch (TalentStudioException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    private IUserService userService;
    protected final Log logger = LogFactory.getLog(getClass());
}
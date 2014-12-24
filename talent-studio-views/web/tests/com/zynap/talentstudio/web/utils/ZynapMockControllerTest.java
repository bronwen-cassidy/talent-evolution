package com.zynap.talentstudio.web.utils;

import com.zynap.domain.UserPrincipal;
import com.zynap.domain.UserSession;
import com.zynap.domain.admin.LoginInfo;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.CoreDetail;
import com.zynap.talentstudio.arenas.IArenaManager;
import com.zynap.talentstudio.audit.SessionLog;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.security.permits.IPermit;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.web.SessionConstants;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestScope;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.SessionScope;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: amark
 * Date: 08-Feb-2005
 * Time: 10:40:54
 */
public abstract class ZynapMockControllerTest extends AbstractHibernateTestCase {

    protected List<String> getConfigLocations() {

        final List<String> configLocations = super.getConfigLocations();

        String validationConfig = "classpath:config/spring/applicationContext-validation.xml";
        String webappConfig = "classpath:config/spring/talentstudio-servlet.xml";
        configLocations.add(validationConfig);
        configLocations.add(webappConfig);
        configLocations.add("classpath:config/spring/testAppContext.xml");
        return configLocations;
    }

    protected void setUp() throws Exception {
        super.setUp();
        applicationContext.getBeanFactory().registerScope("request", new RequestScope());
        applicationContext.getBeanFactory().registerScope("session", new SessionScope());
        mockRequest = new MockHttpServletRequest();
        ServletRequestAttributes attributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(attributes);
        mockRequest.setContextPath(BASE_URL);
        mockRequest.setSession(new MockHttpSession());
        mockResponse = new MockHttpServletResponse();
        mockServletContext = new MockServletContext();
        mockPageContext = new ZynapMockPageContext(mockServletContext, mockRequest, mockResponse);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mockRequest = null;
        mockResponse = null;
        mockPageContext = null;
    }

    protected void setUserSession(UserSession userSession, HttpServletRequest request) {
        request.getSession().setAttribute(SessionConstants.USER_SESSION, userSession);
        UserSessionFactory.setUserSession(userSession);
    }

    protected void setUserSession(Long userId, HttpServletRequest request) {
        User user = new User(userId);
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername("agsfdhjfjkg");
        loginInfo.setPassword("xxdfgw45");
        user.setLoginInfo(loginInfo);
        user.setCoreDetail(new CoreDetail("first", "second"));
        UserSession userSession = new UserSession(new UserPrincipal(user, new ArrayList<IPermit>()), getArenaMenuHandler());
        setUserSession(userSession, request);
    }

    protected void setUserSession(User user, HttpServletRequest request) {
        UserSession userSession = new UserSession(new UserPrincipal(user, new ArrayList<IPermit>(), new SessionLog()), getArenaMenuHandler());
        userSession.setCurrentArenaId(IArenaManager.MYZYNAP_MODULE);
        setUserSession(userSession, request);
    }

    protected void setUserSession(User user, HttpServletRequest request, List<IPermit> accesPermits) {
        UserSession userSession = new UserSession(new UserPrincipal(user, accesPermits, new SessionLog()), getArenaMenuHandler());
        userSession.setCurrentArenaId(IArenaManager.MYZYNAP_MODULE);
        setUserSession(userSession, request);
    }

    protected Map getModel(final ModelAndView modelAndView) {
        return (Map) modelAndView.getModel().get(ControllerConstants.MODEL_NAME);
    }

    protected ZynapRedirectView getRedirectView(final ModelAndView modelAndView) {
        return (ZynapRedirectView) modelAndView.getView();
    }

    protected Errors getErrors(final Object command) {
        return new BindException(command, ControllerConstants.COMMAND_NAME);
    }

    protected void setTargetPage(int targetPage) {

        if (!WebUtils.getParametersStartingWith(mockRequest, ControllerConstants.TARGET_PARAM_PREFIX).isEmpty()) {
            mockRequest = new MockHttpServletRequest();
        }

        mockRequest.addParameter(ControllerConstants.TARGET_PARAM_PREFIX + targetPage, Integer.toString(targetPage));
    }

    protected MockHttpServletRequest mockRequest;
    protected MockHttpServletResponse mockResponse;
    protected MockPageContext mockPageContext;
    protected MockServletContext mockServletContext;

    protected static final String BASE_URL = "/talentstudio";

}

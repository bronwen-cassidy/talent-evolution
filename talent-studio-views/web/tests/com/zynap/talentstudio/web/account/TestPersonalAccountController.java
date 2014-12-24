package com.zynap.talentstudio.web.account;

import com.zynap.domain.UserPrincipal;
import com.zynap.domain.UserSession;
import com.zynap.domain.admin.LoginInfo;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.CoreDetail;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.security.admin.UserWrapperBean;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * User: amark
 * Date: 07-Feb-2005
 * Time: 14:49:43
 */
public class TestPersonalAccountController extends BasePersonalControllerTest {

    protected void setUp() throws Exception {
        super.setUp();

        _personalAccountController = (PersonalAccountController) applicationContext.getBean("personalAccountController");
    }

    public void testWithUser() throws Exception {

        UserPrincipal principal = getAdminUserPrincipal();
        UserSession userSession = new UserSession(principal, getArenaMenuHandler());
        ZynapWebUtils.setUserSession(mockRequest, userSession);

        ModelAndView modelAndView = _personalAccountController.handleRequest(mockRequest, mockResponse);
        assertEquals(PersonalAccountController.MY_ACCOUNT_VIEW, modelAndView.getViewName());
        Map o = getModel(modelAndView);

        UserWrapperBean userWrapperBean = (UserWrapperBean) o.get(ControllerConstants.ARTEFACT);
        assertNotNull(userWrapperBean);
        assertEquals(ADMINISTRATOR_USER_ID, userWrapperBean.getId());
    }

    public void testWithSubjectUser() throws Exception {
        CoreDetail coreDetail = new CoreDetail("dr", "fred", "flintstone");
        coreDetail.setPrefGivenName("fred");
        Subject newSubject = new Subject(coreDetail);

        final String username = "11eddie23";
        final String password = "11eddie12";
        final LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(username);
        loginInfo.setPassword(password);

        User user = new User(loginInfo, coreDetail);
        newSubject.setUser(user);
        subjectService.create(newSubject);

        UserPrincipal userPrincipal = getUserPrincipal(newSubject);
        UserSession userSession = new UserSession(userPrincipal, getArenaMenuHandler());
        ZynapWebUtils.setUserSession(mockRequest, userSession);

        ModelAndView modelAndView = _personalAccountController.handleRequest(mockRequest, mockResponse);
        assertEquals(PersonalAccountController.MY_ACCOUNT_VIEW, modelAndView.getViewName());
        Map o = getModel(modelAndView);
        UserWrapperBean accountWrapperBean = (UserWrapperBean) o.get(ControllerConstants.ARTEFACT);

        assertNotNull(accountWrapperBean);
        assertEquals(username, accountWrapperBean.getLoginInfo().getUsername());
    }

    private PersonalAccountController _personalAccountController;
}

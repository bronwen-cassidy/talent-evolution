package com.zynap.talentstudio.web.account;


import com.zynap.domain.UserPrincipal;
import com.zynap.domain.UserSession;
import com.zynap.domain.admin.LoginInfo;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.CoreDetail;

import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.security.roles.HibernateRoleManagerDao;
import com.zynap.talentstudio.security.roles.Role;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.security.admin.UserWrapperBean;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * User: amark
 * Date: 08-Feb-2005
 * Time: 10:01:05
 */
public class TestEditPersonalAccountController extends BasePersonalControllerTest
{
    protected void setUp() throws Exception {
        super.setUp();

        editPersonalAccountController = (EditPersonalAccountController) applicationContext.getBean("editPersonalAccountController");
        hibernateRoleManagerDao = (HibernateRoleManagerDao) applicationContext.getBean("roleManDao");
    }

	public void testEditUser() throws Exception {

        UserPrincipal principal = getAdminUserPrincipal();
        UserSession userSession = new UserSession(principal, getArenaMenuHandler());
        ZynapWebUtils.setUserSession(mockRequest, userSession);

        UserWrapperBean accountWrapperBean = (UserWrapperBean) editPersonalAccountController.formBackingObject(mockRequest);
        accountWrapperBean.getCoreDetail().setPrefGivenName("new given name");
        assertEquals(EditPersonalAccountController.EDIT_MY_ACCOUNT_VIEW, editPersonalAccountController.getFormView());

        ModelAndView modelAndView = editPersonalAccountController.onSubmit(mockRequest, mockResponse,
                accountWrapperBean, new BindException(accountWrapperBean, editPersonalAccountController.getCommandName()));

        assertEquals(editPersonalAccountController.getSuccessView(), getRedirectView(modelAndView).getUrl());

        UserWrapperBean editedUser = (UserWrapperBean) editPersonalAccountController.formBackingObject(mockRequest);
        assertEquals(accountWrapperBean.getCoreDetail().getPrefGivenName(), editedUser.getCoreDetail().getPrefGivenName());
    }

    public void testEditSubject() throws Exception {

        CoreDetail coreDetail = new CoreDetail("Mr", "joe", "bloggs");
        Subject subject = new Subject(coreDetail);
        subject.setActive(true);

        final String username = "lanagh";
        final String password = "januagh";
        final LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(username);
        loginInfo.setPassword(password);

        User user = new User(loginInfo, coreDetail);
        Role role = (Role) hibernateRoleManagerDao.getActiveAccessRoles().get(0);
        user.addRole(role);
        subject.setUser(user);
        subjectService.create(subject);

        UserPrincipal principal = getUserPrincipal(subject);
        UserSession userSession = new UserSession(principal, getArenaMenuHandler());
        ZynapWebUtils.setUserSession(mockRequest, userSession);

        // change user name and title
        UserWrapperBean accountWrapperBean = (UserWrapperBean) editPersonalAccountController.formBackingObject(mockRequest);
        accountWrapperBean.setTitle("Mr");
        final String newUserName = "username";
        accountWrapperBean.getLoginInfo().setUsername(newUserName);

        editPersonalAccountController.onSubmit(mockRequest, mockResponse, accountWrapperBean, new BindException(accountWrapperBean, editPersonalAccountController.getCommandName()));

        // check that title and user name have been changed
        UserWrapperBean editedUser = (UserWrapperBean) editPersonalAccountController.formBackingObject(mockRequest);
        assertEquals(accountWrapperBean.getLoginInfo().getUsername(), editedUser.getLoginInfo().getUsername());
        assertEquals(accountWrapperBean.getTitle(), editedUser.getTitle());
    }

    public void testReferenceData() throws Exception {
        Map map = editPersonalAccountController.referenceData(mockRequest);
        List titles = (List) map.get(ControllerConstants.TITLES);
        assertNotNull(titles);
    }

    private EditPersonalAccountController editPersonalAccountController;
    private HibernateRoleManagerDao hibernateRoleManagerDao;
}

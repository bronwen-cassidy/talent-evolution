package com.zynap.talentstudio.web.account;

import com.zynap.domain.admin.LoginInfo;
import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.CoreDetail;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;


/**
 * Base class for personal account and personal details controllers.
 *
 * @author amark
 */
public abstract class BasePersonalControllerTest extends ZynapMockControllerTest {
    protected ISubjectService subjectService;

    protected void setUp() throws Exception {
        super.setUp();

        subjectService = (ISubjectService) applicationContext.getBean("subjectService");
    }

    protected Subject addNewSubject() throws TalentStudioException {
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
        return newSubject;
    }
}

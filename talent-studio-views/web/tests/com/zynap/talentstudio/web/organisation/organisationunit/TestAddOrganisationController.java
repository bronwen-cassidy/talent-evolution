package com.zynap.talentstudio.web.organisation.organisationunit;

import com.zynap.domain.UserPrincipal;
import com.zynap.domain.UserSession;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.audit.SessionLog;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.security.permits.IPermit;
import com.zynap.talentstudio.web.organisation.OrganisationUnitWrapperBean;
import com.zynap.talentstudio.web.utils.ZynapDbUnitMockControllerTestCase;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

/**
 * Created by bronwen.
 * Date: 29/07/12
 * Time: 11:07
 */
public class TestAddOrganisationController extends ZynapMockControllerTest {

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testOnSubmitInternalMultiTenent() throws Exception {
        User user = new User(new Long(12), "aaadddbbbdd", "test", "person");
        UserSession userSession = new UserSession(new UserPrincipal(user, new ArrayList<IPermit>(), new SessionLog(new Long(4), "ssadaddjsnfmjhrrru"), new OrganisationUnit(new Long(4))), getArenaMenuHandler());
        userSession.setMultiTenant(true);
        setUserSession(userSession, mockRequest);
        AddOrganisationController add = (AddOrganisationController) applicationContext.getBean("addOrganisationController");
        OrganisationUnitWrapperBean command = (OrganisationUnitWrapperBean) add.formBackingObject(mockRequest);
        command.setParentId(new Long(0));
        command.setParentLabel("Default OrgUnit");
        command.setLabel("IKEA");

        ModelAndView modelAndView = add.onSubmitInternal(mockRequest, mockResponse, command, getErrors(command));
        assertNotNull(modelAndView);

        assertEquals("vieworganisation.htm", ((ZynapRedirectView) modelAndView.getView()).getUrl());

        OrganisationUnit modifiedOrganisationUnit = command.getModifiedOrganisationUnit(user);
        assertEquals(modifiedOrganisationUnit.getId(), modifiedOrganisationUnit.getRootId());

    }
}

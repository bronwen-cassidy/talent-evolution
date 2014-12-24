package com.zynap.talentstudio.web.utils;

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.audit.SessionLog;
import com.zynap.talentstudio.arenas.MockArenaMenuHandlerImpl;
import com.zynap.web.mocks.MockJspWriter;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;

import javax.servlet.jsp.tagext.TagSupport;

/**
 * User: amark
 * Date: 06-Apr-2005
 * Time: 08:47:06
 */
public abstract class ZynapMockTagLibTest extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        mockRequest = new MockHttpServletRequest();
        mockRequest.setSession(new MockHttpSession());
        mockResponse = new MockHttpServletResponse();
        mockServletContext = new MockServletContext();
        mockPageContext = new ZynapMockPageContext(mockServletContext, mockRequest, mockResponse);

        TagSupport tagSupport = getTabLibrary();
        tagSupport.setPageContext(mockPageContext);
        ZynapWebUtils.setUserSession(mockRequest, getUserSession());
    }

    protected abstract TagSupport getTabLibrary() throws Exception;

    protected UserSession getUserSession() {
        return new UserSession(getAdminUserPrincipal(), new MockArenaMenuHandlerImpl());
    }

    protected void debugResponse() {
        try {
            MockJspWriter out = (MockJspWriter) mockPageContext.getOut();
            System.out.println(out.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String getOutput() {
        return ((MockJspWriter) mockPageContext.getOut()).getValue();
    }

    protected MockHttpServletRequest mockRequest;
    protected MockHttpServletResponse mockResponse;
    protected MockPageContext mockPageContext;
    protected MockServletContext mockServletContext;
}

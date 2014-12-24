package com.zynap.talentstudio.web.utils;

import com.zynap.web.mocks.MockJspWriter;
import org.springframework.mock.web.MockPageContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;
import java.io.StringWriter;

/**
 * User: amark
 * Date: 18-Jul-2005
 * Time: 12:51:45
 */
public class ZynapMockPageContext extends MockPageContext {

    /**
     * Create new MockPageContext with a MockServletConfig.
     *
     * @param servletContext the ServletContext that the servlet runs in
     * @param request the current HttpServletRequest
     * @param response the current HttpServletResponse
     * (only necessary when writing to the response)
     */
    public ZynapMockPageContext(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response) {
        super(servletContext, request, response);
        writer = new MockJspWriter(0, true, new StringWriter());
    }

    public JspWriter getOut() {
        return writer;
    }

    private MockJspWriter writer;
}

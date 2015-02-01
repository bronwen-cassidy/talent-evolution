package com.zynap.talentstudio.web;

import org.springframework.web.context.ContextLoaderServlet;

import javax.servlet.ServletException;
import javax.servlet.ServletContext;

/**
 * Class or Interface description.
 *
 * @author jsuiras
 * @version 0.1
 * @since 30-Jan-2006 10:35:56
 */
public class TalentStudioContextLoaderServlet extends ContextLoaderServlet {

    /**
     * Initialize the root web application context.
     */
    public void init() throws ServletException {
        super.init();
        ServletContext servletContext = this.getServletContext();
        System.setProperty(
                "jasper.reports.compile.class.path",
                servletContext.getRealPath("/WEB-INF/lib/jasperreports-1.3.3.jar") +
                        System.getProperty("path.separator") +
                        servletContext.getRealPath("/WEB-INF/classes/") +
                        System.getProperty("path.separator") +
                        servletContext.getRealPath("/WEB-INF/lib/talent-studio-app.jar"));

        System.setProperty("java.awt.headless", Boolean.TRUE.toString());
    }
}
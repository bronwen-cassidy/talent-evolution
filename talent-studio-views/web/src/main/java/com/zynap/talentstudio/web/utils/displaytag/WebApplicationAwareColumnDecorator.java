/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.utils.displaytag;

import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.properties.MediaTypeEnum;
import org.displaytag.exception.DecoratorException;

import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.servlet.jsp.PageContext;
import javax.servlet.ServletContext;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 30-Oct-2008 15:07:09
 */
public abstract class WebApplicationAwareColumnDecorator implements DisplaytagColumnDecorator {

    public Object decorate(Object columnValue, PageContext pageContext, MediaTypeEnum media) throws DecoratorException {
        final ServletContext servletContext = pageContext.getServletContext();
        final XmlWebApplicationContext context = (XmlWebApplicationContext) servletContext.getAttribute(SPRING_SERVLET_CONTEXT);
        return decorateInternal(context, columnValue, pageContext, media);
    }

    public abstract Object decorateInternal(XmlWebApplicationContext context, Object columnValue, PageContext pageContext, MediaTypeEnum media) throws DecoratorException;

    static final String SPRING_SERVLET_CONTEXT = "org.springframework.web.servlet.FrameworkServlet.CONTEXT.talentstudio";
}

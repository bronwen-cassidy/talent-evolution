/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.common.html;

import org.springframework.web.servlet.tags.MessageTag;

import javax.servlet.jsp.JspException;

import java.util.List;
import java.util.ArrayList;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 05-Feb-2010 11:23:28
 */
public class ZynapMessageTag extends MessageTag {

    public int doEndTag() throws JspException {
        parameters.clear();
        return super.doEndTag();
    }

    protected Object[] resolveArguments(Object arguments) throws JspException {
        return parameters.toArray(new Object[parameters.size()]);
    }

    public void addParameter(Object value) {
        parameters.add(value);
    }

    public void release() {
        super.release();
        parameters.clear();
    }

    private List<Object> parameters = new ArrayList<Object>();
}

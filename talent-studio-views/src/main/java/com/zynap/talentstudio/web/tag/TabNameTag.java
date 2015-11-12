/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TabNameTag extends BodyTagSupport {

    public String getSpecificURL() {
        return specificURL;
    }

    public void setSpecificURL(String specificURL) {
        this.specificURL = specificURL;
    }

    public void setName(String propertyName) {
        name = propertyName;
    }

    public void setValue(String propertyValue) {
        value = propertyValue;
    }

    public void setOnClick(String propertyValue) {
        this.onClick = propertyValue;
    }

    /**
     * Passes attribute information up to the parent TabTag.
     * <p/>
     * <p/>
     * When we hit the end of the tag, we simply let our parent (which must be a TabTag) know what the user wants
     * to change a property value, and we pass the name/value pair that the user gave us, up to the parent
     * </p>
     *
     * @return <code>TagSupport.EVAL_PAGE</code>
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    public int doEndTag() throws JspException {
        TabTag tab = (TabTag) findAncestorWithClass(this, TabTag.class);
        if (tab == null) {
            throw new JspTagException(getClass().getName() + " has no parent of type " + TabTag.class);
        }

        tab.setProperty(name, value, specificURL, onClick);
        return TagSupport.EVAL_PAGE;
    }

    /**
     * property name.
     */
    private String name;

    /**
     * property value.
     */
    private String value;

    /**
     * Each tab can have its own unique URL.
     */
    private String specificURL;

    /**
     * Each tab can have an onclick function
     */
    private String onClick;
}

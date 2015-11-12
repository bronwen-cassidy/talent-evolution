package com.zynap.talentstudio.web.tag;

import com.zynap.talentstudio.web.tag.properties.TagGeneralProperties;

import javax.servlet.jsp.JspException;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: aandersson
 * Date: 20-Apr-2004
 * Time: 17:48:50
 * To change this template use File | Settings | File Templates.
 */
public final class ActionEntryTag extends ZynapTagSupport {

    protected int doInternalStartTag() throws Exception {

        properties = new TagGeneralProperties();
        printActionEntryStart();

        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag() throws JspException {
        try {
            printActionEntryEnd();
        } catch (IOException e) {
            throw new JspException("Error in InfoboxTag, " + e.getMessage());
        }

        return EVAL_PAGE;
    }

    private void printActionEntryStart() throws IOException {
        StringBuffer output = new StringBuffer();
        output.append(properties.getActionEntryStartHtml());
        out.print(output.toString());
    }

    private void printActionEntryEnd() throws IOException {
        StringBuffer output = new StringBuffer();
        output.append(properties.getActionEntryEndHtml());
        out.print(output.toString());
    }

    private TagGeneralProperties properties;
}
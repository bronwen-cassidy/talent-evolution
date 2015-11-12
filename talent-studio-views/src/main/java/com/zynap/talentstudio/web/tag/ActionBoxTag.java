package com.zynap.talentstudio.web.tag;

import com.zynap.talentstudio.web.tag.properties.TagGeneralProperties;

import javax.servlet.jsp.JspException;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: aandersson
 * Date: 20-Apr-2004
 * Time: 17:17:30
 * To change this template use File | Settings | File Templates.
 */
public final class ActionBoxTag extends ZynapTagSupport {

    protected int doInternalStartTag() throws Exception {

        properties = new TagGeneralProperties();
        printActionBoxStart();

        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag() throws JspException {
        try {
            printActionBoxEnd();
        } catch (IOException e) {
            throw new JspException("Error in InfoboxTag, " + e.getMessage());
        }

        return EVAL_PAGE;
    }

    private void printActionBoxStart() throws IOException {
        StringBuffer output = new StringBuffer();
        output.append(replaceTokens(properties.getActionBoxStartHtml(), new Object[]{id}));
        out.print(output.toString());
    }

    private void printActionBoxEnd() throws IOException {
        StringBuffer output = new StringBuffer();
        output.append(properties.getActionBoxEndHtml());
        out.print(output.toString());
    }

    private TagGeneralProperties properties;
}

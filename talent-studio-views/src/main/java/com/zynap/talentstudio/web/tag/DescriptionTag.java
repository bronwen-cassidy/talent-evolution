/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

/**
 * Tag library used to input html break staements into content.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class DescriptionTag extends BodyTagSupport {

    public void doInitBody() {
        // do nothing
    }

    public int doStartTag() {
        return EVAL_BODY_BUFFERED;
    }

    public void setBodyContent(BodyContent bodyContent) {
        this.bodyContent = bodyContent;
    }

    public int doAfterBody() throws JspException {
        try {
            String content = readBodyContent();
            // pass through the html escape
            // content = HtmlUtils.htmlEscape(content);
            content = replaceForHtmlBr(content);
            writeBodyContent(content);
        } catch (IOException ex) {
            throw new JspException("Could not write escaped body", ex);
        }
        return (SKIP_BODY);
    }

    private String replaceForHtmlBr(String input) {
        if (input == null) return null;
        StringBuffer buffer = new StringBuffer(input.length());
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == 13) {
                buffer.append("<br/>");
                char next = input.charAt(i + 1);
                // check the next is 10
                if (next == 10) {
                    i = i + 1;
                }
            } else {
                buffer.append(c);
            }
        }
        return buffer.toString();
    }

    /**
     * Read the unescaped body content from the page.
     *
     * @return the original content
     * @throws IOException if reading failed
     */
    protected String readBodyContent() throws IOException {
        return this.bodyContent.getString();
    }

    /**
     * Write the escaped body content to the page.
     * <p>Can be overridden in subclasses, e.g. for testing purposes.</p>
     *
     * @param content the content to write
     * @throws IOException if writing failed
     */
    protected void writeBodyContent(String content) throws IOException {
        this.bodyContent.getEnclosingWriter().print(content);
    }

    public String getHtmlEscape() {
        return htmlEscape;
    }

    public void setHtmlEscape(String htmlEscape) {
        this.htmlEscape = htmlEscape;
    }

    private String htmlEscape;
}

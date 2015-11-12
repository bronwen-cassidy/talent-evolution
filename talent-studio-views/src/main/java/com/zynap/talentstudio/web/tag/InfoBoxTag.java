package com.zynap.talentstudio.web.tag;

import com.zynap.talentstudio.web.tag.properties.TagGeneralProperties;
import com.zynap.talentstudio.web.utils.HtmlUtils;

import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.jsp.JspException;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: aandersson
 * Date: 20-Apr-2004
 * Time: 16:30:49
 * To change this template use File | Settings | File Templates.
 */
public final class InfoBoxTag extends ZynapTagSupport {

    public void setTitle(String title) throws JspException {
        this.title = ExpressionEvaluationUtils.evaluateString("title", title, pageContext);
    }

    public void setHtmlEscape(boolean htmlEscape) {
        this.htmlEscape = htmlEscape;
    }

    protected int doInternalStartTag() throws Exception {

        properties = new TagGeneralProperties();

        // ensure id is never null
        if (this.id == null) {
            this.id = "";
        }

        // escape html characters in title
        final String label = htmlEscape ? HtmlUtils.htmlEscape(title) : title;
        printInfoBoxStart(label);

        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag() throws JspException {
        try {
            printInfoBoxEnd();
        } catch (IOException e) {
            throw new JspException("Error in InfoboxTag, " + e.getMessage());
        }

        return EVAL_PAGE;
    }

    public void release() {
        super.release();
        
        this.id = null;
        this.title = null;
        this.htmlEscape = false;
    }

    private void printInfoBoxStart(String title) throws IOException {
        StringBuffer output = new StringBuffer();
        output.append(replaceTokens(properties.getInfoBoxStartHtml(), new Object[]{title, id}));
        out.print(output.toString());
    }

    private void printInfoBoxEnd() throws IOException {
        StringBuffer output = new StringBuffer();
        output.append(properties.getInfoBoxEndHtml());
        out.print(output.toString());
    }

    private TagGeneralProperties properties;
    private String title;

    /**
     * Indicates whether or not to html escape the title.
     * <br/> Defaults to false.
     */
    private boolean htmlEscape = false;
}

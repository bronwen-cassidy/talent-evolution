/*
 * Copyright (c) 2004 Zynap Limited. All rights reserved.
 */
package com.zynap.web.tag;

import com.zynap.domain.UserSession;

import com.zynap.web.PageTabs;
import com.zynap.web.SessionConstants;
import com.zynap.web.TabHelper;
import com.zynap.web.tag.properties.TabProperties;
import com.zynap.web.utils.HtmlUtils;

import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.util.StringUtils;
import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Iterator;

/**
 * Tag which prints out the framework for tabs on a html page.
 *
 * @author Andreas Andersson
 * @since 08/03/2004
 */
public final class TabTag extends BodyTagSupport {

    public void setStyleSheetClass(String styleSheetClass) {
        this.styleSheetClass = styleSheetClass;
    }

    public String getStyleSheetClass() {
        return styleSheetClass;
    }

    public String getUrl() {
        return url;
    }

    public String getIncludeBody() {
        return includeBody;
    }

    public void setIncludeBody(String includeBody) {
        this.includeBody = includeBody;
    }

    public String getTabParamName() {
        return tabParamName;
    }

    public void setTabParamName(String tabParamName) {
        this.tabParamName = tabParamName;
    }

    public void setId(String id) {
        String resolvedId;
        try {
            resolvedId = ExpressionEvaluationUtils.evaluateString("id", id, pageContext);
        } catch (JspException e) {
            resolvedId = "default_id";
        }

        this.id = resolvedId;
    }

    public void setUrl(String url) {

        try {
            this.url = ExpressionEvaluationUtils.evaluateString("url", url, pageContext);
        } catch (JspException e) {
            this.url = url;
        }
        if (this.url != null && this.url.equals("javascript"))
            this.javascript = true;
    }

    public String getDefaultTab() {
        return defaultTab;
    }

    public void setDefaultTab(String defaultTab) {
        String resolvedId = defaultTab;
        try {
            resolvedId = ExpressionEvaluationUtils.evaluateString("defaultTab", defaultTab, pageContext);
        } catch (JspException e) {
            e.printStackTrace();
        }
        this.defaultTab = resolvedId;
    }

    /**
     * Start tag.
     *
     * @return EVAL_BODY_BUFFERED request the body be evaluated
     */
    public int doStartTag() {
        properties = new TabProperties();
        return EVAL_BODY_BUFFERED;
    }

    /**
     * Initialize any helper objects that need initializing here.
     *
     * @throws JspException
     */
    public void doInitBody() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        UserSession userSession = ZynapWebUtils.getUserSession(request);
        String resolvedTab = ExpressionEvaluationUtils.evaluateString("defaultTab", this.defaultTab, pageContext);
        pageTabs = TabHelper.createTabs(request, resolvedTab, url, this.tabParamName, javascript);
        
        //Set an alternative key for the arenas id
        String arenaId = pageContext.getRequest().getParameter(ParameterConstants.ARENA_ID);
        if (StringUtils.hasText(arenaId)) {
            pageTabs.addAlternativeKey(SessionConstants.USER_SESSION, userSession.getCurrentArenaId());
        } else {
            pageTabs.addAlternativeKey(SessionConstants.USER_SESSION, arenaId);
        }
    }

    /**
     * doAfterBody.
     *
     * We are not an iterator tag therefore we do not want to be called again and again,
     * so returning SKIP_BODY after we have replaced the specially marked tokens in the html
     * we have been given by the body content.
     *
     * @return SKIP_BODY so we are not called again and again
     * @throws JspException
     */
    public int doAfterBody() throws JspException {
        // get the evaluated body content object
        BodyContent body = getBodyContent();

        // get the evaluated page content
        String content = body.getString();

        // wrap the content with our tab html information
        StringBuffer output = new StringBuffer();

        StringBuffer otherAttributes = new StringBuffer();

        final String localId = getId();
        if (localId != null) {
            otherAttributes.append("id=\"").append(localId).append("\" ");
        }

        output.append(replaceTokens(properties.getTabStart(), new Object[]{getStyleSheetClass(), otherAttributes.toString()}));

        for (Iterator iterator = pageTabs.getTabIds().iterator(); iterator.hasNext();) {
            String tabId = (String) iterator.next();

            // html escape label
            final String label = HtmlUtils.htmlEscape(pageTabs.getLabel(tabId));

            if (isJavascript()) {

                String jsUrl = getJavaScriptUrl(getTabParamName(), tabId);
                if (pageTabs.isActive(tabId)) {
                    output.append(replaceTokens(properties.getJavaScriptActiveTab(), new Object[]{getStyleSheetClass(), jsUrl, label, tabId}));
                } else {
                    output.append(replaceTokens(properties.getInactiveTab(), new Object[]{getStyleSheetClass(), jsUrl, label, tabId}));
                }
            } else {

                final String tabUrl = pageTabs.getUrl(tabId, javascript);

                if (pageTabs.isActive(tabId)) {
                    output.append(replaceTokens(properties.getActiveTab(), new Object[]{getStyleSheetClass(), tabUrl, label, tabId}));
                } else {
                    if (pageTabs.isOnClick(tabId)) {
                        final String onClickFunction = pageTabs.getOnClickFunction(tabId);
                        output.append(replaceTokens(properties.getInactiveOnClickTab(), new Object[]{getStyleSheetClass(), tabUrl, label, tabId, onClickFunction}));
                    } else {
                        output.append(replaceTokens(properties.getInactiveTab(), new Object[]{getStyleSheetClass(), tabUrl, label, tabId}));
                    }
                }
            }
        }
        output.append(replaceTokens(properties.getTabEnd(), new Object[]{getStyleSheetClass()}));

        if (isJavascript()) {
            output.append(replaceTokens(properties.getProperty("javascript.hiddenField"), new Object[]{getTabParamName(), defaultTab}));
        }

        if (Boolean.TRUE.toString().equals(includeBody)) {
            output.append(replaceTokens(properties.getContentStart(), new Object[]{getStyleSheetClass()}));
        }
        output.append(content);

        try {
            body.getEnclosingWriter().write(output.toString());
        } catch (IOException e) {
            throw new JspTagException("Unable to write out the information to the page: " + e.getMessage());
        }

        return SKIP_BODY;
    }

    private String getJavaScriptUrl(String hiddenFieldName, String tabId) {
        return MessageFormat.format(properties.getProperty("javascript.url"), hiddenFieldName, tabId);
    }

    /**
     * Utility method that replaces tokens (place holders) within the html properties
     * for labels and urls.
     *
     * @param activeTab
     * @param tokens
     * @return String  the resolved html with the replaced tokens
     */
    private String replaceTokens(String activeTab, Object[] tokens) {
        return MessageFormat.format(activeTab, tokens);
    }

    /**
     * will write out the closing table or content end we are then done
     * and the rest of the page can be evaluated.
     *
     * @return 6 EVAL_PAGE
     * @throws JspException
     */
    public int doEndTag() throws JspException {
        if (Boolean.TRUE.toString().equals(includeBody)) {
            String endTabTable = replaceTokens(properties.getContentEnd(), new Object[]{getStyleSheetClass()});
            write(endTabTable);
        }
        return EVAL_PAGE;
    }

    public void write(String string) throws JspTagException {
        try {
            JspWriter out = pageContext.getOut();
            out.write(string);
        } catch (IOException e) {
            throw new JspTagException("Writer Exception: " + e.getMessage());
        }
    }

    public void setProperty(String name, String value, String specificURL, String onClickFunction) throws JspTagException {
        if (pageTabs == null) {
            pageTabs = new PageTabs("", tabParamName);
        }
        try {
            String resolvedName = ExpressionEvaluationUtils.evaluateString("name", name, pageContext);
            String resolvedValue = ExpressionEvaluationUtils.evaluateString("value", value, pageContext);
            //Check to see if we need to add a url or use one defined in the config
            String resolvedUrl = ExpressionEvaluationUtils.evaluateString("specificURL", specificURL, pageContext);
            if (resolvedUrl == null) {
                pageTabs.addTab(resolvedName, resolvedValue, onClickFunction);
            } else {
                pageTabs.addTab(resolvedName, resolvedValue, resolvedUrl, onClickFunction);
            }
        } catch (JspException e) {
            throw new JspTagException("Unable to evaluate name property");
        }
    }

    public boolean isJavascript() {
        return javascript;
    }

    private String styleSheetClass = "tab";
    private String defaultTab;
    private String url;
    private PageTabs pageTabs;
    private TabProperties properties;
    private String includeBody = "true";

    private String tabParamName = ParameterConstants.TAB;
    private boolean javascript;
}


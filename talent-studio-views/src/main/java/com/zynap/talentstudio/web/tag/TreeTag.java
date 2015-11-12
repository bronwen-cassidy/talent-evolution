/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.tag;

import com.zynap.talentstudio.web.tag.properties.TabProperties;
import com.zynap.talentstudio.web.tag.support.tree.orgunit.OrganisationUnitTreeVisitor;

import com.zynap.talentstudio.web.utils.tree.ITreeContainer;

import org.springframework.util.StringUtils;
import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.jsp.JspException;

/**
 * Tree tag will write out a tree structure in a number of ways, exploded, or linkable.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TreeTag extends ZynapTagSupport {

    protected int doInternalStartTag() throws Exception {
        TabProperties properties = new TabProperties();
        OrganisationUnitTreeVisitor visitor = new OrganisationUnitTreeVisitor();
        // check for org unit parameter
        final String orgUnitParam = pageContext.getRequest().getParameter(CELL_REQUEST_PARAM);
        final String selectedOrganisationUnitId = StringUtils.hasText(orgUnitParam) ? orgUnitParam : null;

        open = pageContext.getRequest().getParameter(CELL_OPEN_PARAM);

        StringBuffer buffer = new StringBuffer();
        buffer.append(visitor.getHtmlStart(properties)).append("\n");

        buffer.append(generateTree(visitor, properties, selectedOrganisationUnitId, container));
        buffer.append(visitor.getHtmlEnd(properties));
        out.print(buffer.toString());

        return EVAL_BODY_INCLUDE;
    }

    protected String generateTree(OrganisationUnitTreeVisitor visitor, TabProperties properties, String selectedOrganisationUnitId, ITreeContainer target) {
        visitor.setSelectedOrganisationUnitId(selectedOrganisationUnitId);
        visitor.setOpen(open);
        visitor.setProperties(properties);
        visitor.setUrl(url);
        visitor.setViewLink(viewLink);
        visitor.setViewParamName(viewParamName);
        target.accept(visitor);
        return visitor.getResult();
    }

    public Object getComposite() {
        return container;
    }

    public void setComposite(Object composite) throws JspException {
        if (ExpressionEvaluationUtils.isExpressionLanguage(composite.toString())) {
            this.container = (ITreeContainer) ExpressionEvaluationUtils.evaluate(composite.toString(), composite.toString(), Object.class, pageContext);
        } else {
            this.container = (ITreeContainer) composite;
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) throws JspException {
        this.url = ExpressionEvaluationUtils.evaluateString("url", url, pageContext);
    }

    public String getViewLink() {
        return viewLink;
    }

    public void setViewLink(String viewLink) throws JspException {
        this.viewLink = ExpressionEvaluationUtils.evaluateString("viewLink", viewLink, pageContext);
    }

    public String getViewParamName() {
        return viewParamName;
    }

    public void setViewParamName(String viewParamName) {
        this.viewParamName = viewParamName;
    }

    private String url;
    private String viewLink;
    private String viewParamName;

    private String open = "F";
    private ITreeContainer container;

    public static final String CELL_REQUEST_PARAM = "cell";
    public static final String CELL_OPEN_PARAM = "open";
}

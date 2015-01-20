/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.web.tag;

import com.zynap.util.resource.PropertiesManager;

import com.zynap.talentstudio.web.utils.tree.TreeDisplayVisitor;

import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.jsp.JspException;

import java.text.MessageFormat;

import java.util.List;

/**
 * Tree tag will write out a trees structure in a number of ways, exploded, or linkable.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public final class TreeServerTag extends ZynapTagSupport {

    /**
     * Clear up resources and reset variables to default state.
     */
    public void release() {
        super.release();

        this.branchSelectable = false;
        this.selectedBranchId = null;
        this.serverMode = false;
    }

    protected int doInternalStartTag() throws Exception {
        
        StringBuffer html = new StringBuffer();
        String styleClass = serverMode ? SERVER_POPUP_STYLE_CLASS : CLIENT_POPUP_STYLE_CLASS;
        if (id != null) {
            String startId = propertiesManager.getString("tree.selector.start.id");
            html.append(MessageFormat.format(startId, id, styleClass));
        } else {
            html.append(MessageFormat.format(propertiesManager.getString("tree.selector.start"), styleClass));
        }

        TreeDisplayVisitor displayVisitor = new TreeDisplayVisitor(propertiesManager, branchSelectable, leafIcon, branchIcon, emptyBranchIcon, serverMode, getId());
        displayVisitor.display(trees, html);
        html.append(propertiesManager.getString("tree.selector.end"));

        if (serverMode) {
            html.append(propertiesManager.getString("tree.branch.form"));
            if (selectedBranchId != null && id != null) {
                html.append("<script type=\"text/javascript\"> { ");
                String scriptOpen = propertiesManager.getString("tree.open.branch");
                html.append(MessageFormat.format(scriptOpen, id, selectedBranchId));
                html.append("} </script>  ");
            }
        }

        out.print(html);

        return EVAL_BODY_INCLUDE;
    }

    public void setTrees(Object tree) throws JspException {
        if (ExpressionEvaluationUtils.isExpressionLanguage(tree.toString())) {
            this.trees = (List) ExpressionEvaluationUtils.evaluate(tree.toString(), tree.toString(), Object.class, pageContext);
        } else {
            this.trees = (List) tree;
        }
    }

    public void setBranchSelectable(boolean branchSelectable) throws JspException {
        this.branchSelectable = ExpressionEvaluationUtils.evaluateBoolean("branchSelectable", String.valueOf(branchSelectable), pageContext);
    }

    public void setEmptyBranchIcon(String emptyBranchIcon) throws JspException {
        this.emptyBranchIcon = ExpressionEvaluationUtils.evaluateString("emptyBranchIcon", emptyBranchIcon, pageContext);
    }

    public void setBranchIcon(String branchIcon) throws JspException {
        this.branchIcon = ExpressionEvaluationUtils.evaluateString("branchIcon", branchIcon, pageContext);
    }

    public void setLeafIcon(String leafIcon) throws JspException {
        this.leafIcon = ExpressionEvaluationUtils.evaluateString("leafIcon", leafIcon, pageContext);
    }

    public void setServerMode(boolean serverMode) {
        this.serverMode = serverMode;
    }

    public void setSelectedBranchId(String selectedBranchId) throws JspException {
        this.selectedBranchId = ExpressionEvaluationUtils.evaluateString("selectedBranchId", selectedBranchId, pageContext);
    }

    private List trees;
    private boolean branchSelectable;

    private String emptyBranchIcon;
    private String branchIcon;
    private String leafIcon;

    private boolean serverMode;
    private String selectedBranchId;

    private static final PropertiesManager propertiesManager = PropertiesManager.getInstance(TreeServerTag.class);

    public static final String SERVER_POPUP_STYLE_CLASS = "popupScrollArea";
    public static final String CLIENT_POPUP_STYLE_CLASS = "popupTreeArea popupScrollArea clientScrollArea";
}

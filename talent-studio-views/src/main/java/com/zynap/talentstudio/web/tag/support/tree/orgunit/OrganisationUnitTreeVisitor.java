/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.tag.support.tree.orgunit;

import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.tree.ITreeContainer;
import com.zynap.talentstudio.web.utils.tree.IVisitor;
import com.zynap.talentstudio.web.tag.TreeTag;
import com.zynap.talentstudio.web.tag.properties.TabProperties;
import com.zynap.talentstudio.web.tag.support.tree.ChildVisitor;
import com.zynap.talentstudio.web.utils.HtmlUtils;

import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class OrganisationUnitTreeVisitor implements IVisitor {

    public OrganisationUnitTreeVisitor() {
        barStack = new Stack<String>();
        result = new StringBuffer();
    }

    public void setProperties(TabProperties properties) {
        this.properties = properties;
    }

    public void setSelectedOrganisationUnitId(String selectedOrganisationUnitId) {
        this.selectedOrganisationUnitId = selectedOrganisationUnitId;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setViewLink(String viewLink) {
        this.viewLink = viewLink;
    }

    public void setViewParamName(String viewParamName) {
        this.viewParamName = viewParamName;
    }

    /**
     * get the output.
     * <br> Also resets the StringBuffer used by the Visitor.
     *
     * @return The output
     */
    public String getResult() {
        final String output = result.toString();
        this.result = new StringBuffer();

        return output;
    }

    public String getHtmlStart(TabProperties properties) {
        return properties.getTreeStart();
    }

    public String getHtmlEnd(TabProperties properties) {
        return properties.getTreeEnd();
    }

    /**
     * @param container
     * @return boolean
     */
    public boolean visitEnter(ITreeContainer container) {
        if (selectedOrganisationUnitId != null) {

            // if id matches check if entry is being expanded / collapsed - if open equals "F" this means it is currently closed
            // and is being expanded so return true so that the children are displayed as well
            final boolean idMatches = container.getId().equals(selectedOrganisationUnitId);
            if (idMatches) return ("F".equals(open));

            // otherwise visit children
            ChildVisitor tempVisitor = new ChildVisitor(selectedOrganisationUnitId);
            container.accept(tempVisitor);
            if (tempVisitor.isChild()) {
                return true;
            }
        }
        return false;
    }

    public void visit(ITreeContainer container) {
        String currentUrl = buildUrl(container);
        if (isLastSibling(container)) {
            // add the verticalbar
            barStack.push(properties.getSelectorTreeInvisibleBar());
        } else {
            barStack.push(properties.getSelectorTreeImageBar());
        }

        // todo goes to end result.append(formatTreeRow(properties, currentUrl));
        StringBuffer spaces = appendSpaces(barStack.size() - 1);
        StringBuffer image = buildTreeRowImage(container);
        result.append(MessageFormat.format(properties.getTreeRow(), spaces.toString(), currentUrl, image.toString(), container.getId().toString()));
        // create the view link
        String extractedViewLink = extractViewLink(container);
        String id = container.getId().toString();
        result.append(getCellView(container, extractedViewLink, id));

        result.append(properties.getTreeTdEnd());
        result.append(properties.getTreeRowEnd());
    }

    protected StringBuffer appendSpaces(int level) {
        StringBuffer temp = new StringBuffer();
        for (int i = getStartLevel(); i < level; i++) {
            temp.append(barStack.get(i - getStartLevel()));
        }
        return temp;
    }

    protected int getStartLevel() {
        return 0;
    }

    public void visitLeave(ITreeContainer container) {
        if (!barStack.isEmpty()) {
            barStack.pop();
        }
    }

    protected String getCellView(ITreeContainer container, String viewLink, String id) {
        final String label = HtmlUtils.htmlEscape(container.getLabel());

        if (container.isHasAccess()) {
            return MessageFormat.format(properties.getTreeCheckedViewLink(), viewLink, label, id);
        } else {
            return label;
        }
    }

    /**
     * Builds the row of the tree. This needs to determine if we are the last child in the list, if we are we need to apply
     * a tree_leaf_end image.
     *
     * @param container
     */
    private StringBuffer buildTreeRowImage(ITreeContainer container) {
        StringBuffer buffer = new StringBuffer();
        if (hasNoChildren(container)) {
            String leaf = getTreeLeafValue();
            if (isLastSibling(container)) {
                leaf = getLastSiblingValue();
            }
            buffer.append(leaf);
        } else {
            if (visitEnter(container)) {
                buffer.append(properties.getTreeOpenImage());
            } else {
                buffer.append(properties.getTreeClosedImage());
            }
        }
        return buffer;
    }

    protected String getLastSiblingValue() {
        return properties.getTreeLastSibling();
    }

    protected String getTreeLeafValue() {
        return properties.getTreeLeafEnd();
    }

    private boolean hasNoChildren(ITreeContainer container) {
        return container.getChildren().isEmpty();
    }

    private String buildUrl(ITreeContainer container) {

        String status = visitEnter(container) ? "T" : "F";
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(TreeTag.CELL_OPEN_PARAM, status);
        parameters.put(TreeTag.CELL_REQUEST_PARAM, container.getId());

        return ZynapWebUtils.buildURL(url, parameters);
    }

    /**
     * Build view link.
     *
     * @param container
     * @return The view link with the correct parameters, or null if the view link was not specified.
     */
    private String extractViewLink(ITreeContainer container) {

        if (StringUtils.hasText(viewLink) && StringUtils.hasText(viewParamName)) {
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put(viewParamName, container.getId());

            return ZynapWebUtils.buildURL(viewLink, parameters);
        }

        return "";
    }

    private boolean isLastSibling(ITreeContainer container) {
        if (container.getParent() == null) return true;
        List children = container.getParent().getChildren();
        if (children.indexOf(container) == (children.size() - 1)) return true;
        return false;
    }

    private TabProperties properties;
    private String selectedOrganisationUnitId;
    private String open;
    private String url;
    private String viewLink;
    private String viewParamName;
    private StringBuffer result;
    private Stack<String> barStack;
}

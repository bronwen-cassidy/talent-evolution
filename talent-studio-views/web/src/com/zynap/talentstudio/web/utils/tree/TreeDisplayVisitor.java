package com.zynap.talentstudio.web.utils.tree;

import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.util.resource.PropertiesManager;
import com.zynap.web.utils.HtmlUtils;

import java.text.MessageFormat;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 11-Aug-2005
 * Time: 13:37:50
 * To change this template use File | Settings | File Templates.
 */
public class TreeDisplayVisitor {

    public TreeDisplayVisitor(PropertiesManager propertiesManager, boolean branchSelectable, String leafImage, String branchImage, String branchEmptyImage, boolean serverMode, String popupId) {
        this.propertiesManager = propertiesManager;
        this.branchSelectable = branchSelectable;
        blank = propertiesManager.getString("tree.blank");
        vertical = propertiesManager.getString("tree.vertical");
        this.leafImage = leafImage;
        this.branchImage = branchImage;
        this.branchEmptyImage = branchEmptyImage;
        this.serverMode = serverMode;
        this.popupId = popupId;
    }

    public void display(ITreeContainer container, StringBuffer buffer) {
        display((Branch) container, buffer, new StringBuffer());

    }

    private void display(Leaf leaf, StringBuffer buffer, StringBuffer indent, boolean isLastSibling) {

        String output;

        if (leaf.isSeparator()) {
            final Object[] params = new Object[]{indent};
            output = MessageFormat.format(propertiesManager.getString("leaf.separator"), params);
        } else {
            final String image = isLastSibling ? LAST_BRANCH_GIF : BRANCH_GIF;
            String leafHtml;
            Object[] params;
            final String label = leaf.getLabel();
            final String htmlEncodedLabel = HtmlUtils.htmlEscape(label);
            final String javascriptEncodedLabel = HtmlUtils.htmlEscape(ZynapWebUtils.javaScriptEscape(label));
            if (serverMode) {
                leafHtml = propertiesManager.getString("leaf.servermode");
                params = new Object[]{indent, leafImage, javascriptEncodedLabel, leaf.getId(), htmlEncodedLabel, image, popupId};
            } else {
                leafHtml = propertiesManager.getString("leaf");
                params = new Object[]{indent, leafImage, javascriptEncodedLabel, leaf.getId(), htmlEncodedLabel, image};
            }

            output = MessageFormat.format(leafHtml, params);
        }


        buffer.append(output);
    }

    public void display(Branch branch, StringBuffer buffer, StringBuffer indent) {
        display(branch, buffer, indent, true);
    }

    public void display(Branch branch, StringBuffer buffer, StringBuffer indent, boolean isLastSibling) {
        final boolean hasChildren = branch.isHasChildren();

        final String folderImage = hasChildren ? branchImage : branchEmptyImage;
        final String image = hasChildren ? (!isLastSibling ? CLOSED_BRANCH_GIF : LAST_CLOSED_BRANCH_GIF) : (!isLastSibling ? BRANCH_GIF : LAST_BRANCH_GIF);

        displayBranchStart(branch, buffer, indent.toString(), folderImage, image);
        displayBranchEnd(buffer);
        if (hasChildren) {

            StringBuffer localIndent = buildIndent(indent, isLastSibling);
            buffer.append(propertiesManager.getString("tree.level.start"));
            for (int i = 0; i < branch.getLeaves().size(); i++) {
                Leaf leaf = (Leaf) branch.getLeaves().get(i);
                display(leaf, buffer, localIndent, (i == branch.getLeaves().size() - 1) && !(branch.getChildren() != null && branch.getChildren().size() > 0));
            }
            for (int i = 0; i < branch.getChildren().size(); i++) {
                Branch branch1 = (Branch) branch.getChildren().get(i);
                display(branch1, buffer, localIndent, (i == branch.getChildren().size() - 1));
            }
            buffer.append(propertiesManager.getString("tree.level.end"));
        }
    }

    private void displayBranchEnd(StringBuffer buffer) {
        buffer.append(propertiesManager.getString("branch.end"));
    }

    private void displayBranchStart(Branch branch, StringBuffer buffer, String indent, String folderImage, String branchImage) {

        String start = propertiesManager.getString("branch.start");
        buffer.append(MessageFormat.format(start, indent, folderImage, branchImage));
        final String label = branch.getLabel();
        final String htmlEncodedLabel = HtmlUtils.htmlEscape(label);
        final String javascriptEncodedLabel = HtmlUtils.htmlEscape(ZynapWebUtils.javaScriptEscape(label));
        if (branchSelectable) {
            if (serverMode) {
                String branchLink = propertiesManager.getString("branch.form.link");
                buffer.append(MessageFormat.format(branchLink, branch.getId(), htmlEncodedLabel));
            } else {
                String branchLink = propertiesManager.getString("branch.link");
                buffer.append(MessageFormat.format(branchLink, branch.getId(), javascriptEncodedLabel, htmlEncodedLabel));
            }
        } else {
            buffer.append(MessageFormat.format(propertiesManager.getString("branch.label"), htmlEncodedLabel));
        }
    }

    StringBuffer buildIndent(StringBuffer level, boolean isLastSibling) {
        StringBuffer buffer = new StringBuffer().append(level);
        buffer.append(isLastSibling ? blank : vertical);
        return buffer;
    }

    public void display(List trees, StringBuffer html) {
        if (trees != null) {
            for (int i = 0; i < trees.size(); i++) {
                Branch branch = (Branch) trees.get(i);
                display(branch, html, new StringBuffer(), i == trees.size() - 1);
            }
        }
    }


    public String getPopupId() {
        return popupId;
    }

    public void setPopupId(String popupId) {
        this.popupId = popupId;
    }

    private PropertiesManager propertiesManager;
    private String blank;
    private String vertical;
    private boolean branchSelectable;
    private String popupId;

    private String leafImage;
    private String branchImage;
    private String branchEmptyImage;

    private boolean serverMode;

    private static final String LAST_BRANCH_GIF = "LastBranch.gif";
    private static final String BRANCH_GIF = "Branch.gif";
    private static final String CLOSED_BRANCH_GIF = "ClosedBranch.gif";
    private static final String LAST_CLOSED_BRANCH_GIF = "LastClosedBranch.gif";
}

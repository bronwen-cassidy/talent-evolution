package com.zynap.talentstudio.web.organisation;

import com.zynap.domain.IDomainObject;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.web.common.AbstractBrowseWrapper;
import com.zynap.talentstudio.web.dashboard.DashboardViewable;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 04-May-2005
 * Time: 09:36:27
 */
public class BrowseNodeWrapper extends AbstractBrowseWrapper implements DashboardViewable {

    public BrowseNodeWrapper(NodeSearchQueryWrapper queryWrapper, List results, NodeWrapperBean nodeWrapperBean) {
        nodeWrapper = nodeWrapperBean;
        currentNodes = results;
        filter = queryWrapper;
        activeSearchTab = _SEARCH_DETAILS_TAB_;
    }

    public Node getNode() {
        return nodeWrapper != null ? nodeWrapper.getNode() : null;
    }

    public NodeWrapperBean getNodeWrapper() {
        return nodeWrapper;
    }

    public void setNodeWrapper(NodeWrapperBean nodeWrapper) {
        this.nodeWrapper = nodeWrapper;
    }

    public NodeSearchQueryWrapper getFilter() {
        return filter;
    }

    public void setFilter(NodeSearchQueryWrapper filter) {
        this.filter = filter;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public String getActiveSearchTab() {
        return activeSearchTab;
    }

    public void setActiveSearchTab(String activeSearchTab) {
        this.activeSearchTab = activeSearchTab;
    }

    public Map.Entry getPageNumberParameter() {
        return pageNumberParameter;
    }

    public void setPageNumberParameter(Map.Entry pageNumberParameter) {
        this.pageNumberParameter = pageNumberParameter;
    }

    public void resetPageNumber() {
        setPageNumberParameter(null);
    }

    public boolean isNewNode() {
        return newNode;
    }

    public void setNewNode(boolean newNode) {
        this.newNode = newNode;
    }

    public Long getFirst() {
        Long firstId = getId(0);
        if (firstId == null) return null;
        return (nodeWrapper == null || !firstId.equals(nodeWrapper.getId())) ? firstId : null;
    }

    public Long getLast() {
        Long lastId = getId(currentNodes.size() - 1);
        if (lastId == null) return null;
        return (nodeWrapper == null || !lastId.equals(nodeWrapper.getId())) ? lastId : null;
    }

    public Long getId(int pos) {
        if (currentNodes != null && !currentNodes.isEmpty())
            try {
                return ((IDomainObject) currentNodes.get(pos)).getId();
            } catch (IndexOutOfBoundsException e) {
                return null;
            }
        return null;
    }

    public int getCurrentItemIndex() {
        if (nodeWrapper != null && (currentNodes != null && !currentNodes.isEmpty())) {
            return getIndex();
        }
        return -1;
    }

    public int getIndex() {
        return currentNodes.indexOf(nodeWrapper.getNode());
    }

    public Long getCurrentIndex() {
        if (nodeWrapper != null && currentNodes != null) {
            int index = getIndex();
            return new Long(index + 1);
        }
        return null;
    }

    public boolean isSearching() {
        return searching;
    }

    public void setSearching(boolean searching) {
        this.searching = searching;
    }

    public Collection getWrappedDynamicAttributes() {
        return filter.getWrappedDynamicAttributes();
    }

    public void setWrappedDynamicAttributes(Collection attributeValues) {
        filter.setWrappedDynamicAttributes(attributeValues);
    }

    /**
     * Remove specified node from list of current nodes.
     *
     * @param nodeId The id of the Node to remove
     */
    public void removeNode(final Long nodeId) {

        final boolean[] nodeFound = new boolean[] {false};

        // remove from current nodes - filter where node id param equals current node's id
        CollectionUtils.filter(currentNodes, new Predicate() {
            public boolean evaluate(Object object) {
                Node currentNode = (Node) object;
                if(currentNode.getId().equals(nodeId)) {
                    nodeFound[0] = true;
                }
                return !nodeId.equals(currentNode.getId());
            }
        });

        // if we have not found the node with the given id it is because it is probably the deletion of an associated node and therefore we do not need to find next
        if(!nodeFound[0]) return;

        // get next node - try next first; if there's isn't a next try the previous
        Long nextNodeId = getNext();
        if (nextNodeId == null) {
            nextNodeId = getPrevious();
        }

        // set the node id regardless of whether or not it is null
        setNodeId(nextNodeId);

        // reset default tabs
        if (nextNodeId == null) {
            if (searching) {
                setActiveSearchTab(_SEARCH_DETAILS_TAB_);
            } /*else {
                setActiveTab(_DETAILS_TAB_);
            }*/
        }
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setContentView(DisplayContentWrapper displayContentWrapper) {
        this.displayContent = displayContentWrapper;
        setActiveTab(displayContent.getActiveDisplay());
    }

    public DisplayContentWrapper getContentView() {
        return displayContent;
    }

    /**
     * Method is needed for when the node is edited and the visible element of the label is changed.
     * <br/>
     * The node that has been edited is refetched from the database so all information is up to date.
     * The problem arises in the collection of nodes as these are stale. The problem of added artefacts is not solved.
     *
     * @param nodeWrapperBean wrapper containing nodes to be updated
     */
    public void updateCurrentNodes(NodeWrapperBean nodeWrapperBean) {
        if (currentNodes != null && !currentNodes.isEmpty()) {
            Node updatedNode = nodeWrapperBean.getNode();
            int addIndex = currentNodes.indexOf(updatedNode);
            if (addIndex >= 0) {
                currentNodes.remove(updatedNode);
                currentNodes.add(addIndex, updatedNode);
            }
        }
    }

    public void setObjectivesModifiable(boolean objectivesModifiable) {
        this.objectivesModifiable = objectivesModifiable;
    }

    public boolean isObjectivesModifiable() {
        return objectivesModifiable;
    }

    public void setErrorKey(String errorKey) {
        this.errorKey = errorKey;
    }

    public String getErrorKey() {
        return errorKey;
    }

    public void setHasCustomColumn(boolean hasCustomColumn) {
        this.hasCustomColumn = hasCustomColumn;
    }

    public boolean isHasCustomColumn() {
        return hasCustomColumn;
    }

    public void setCustomColumnLabel(String attributeLabel) {
        this.attributeLabel = attributeLabel;
    }

    public String getAttributeLabel() {
        return attributeLabel;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * The managers userId used in my team views
     *
     * @return the managers (logged in) user id
     */
    public Long getUserId() {
        return userId;
    }

    public boolean isHasDashboardView() {
        return displayContent != null && displayContent.isHasDashboardView();
    }

    private boolean searching;
    private Long nodeId;
    private NodeSearchQueryWrapper filter;
    private NodeWrapperBean nodeWrapper;
    private String activeSearchTab;

    /**
     * Field used to store page number parameter name and value (used in searches for paging.)
     */
    private Map.Entry pageNumberParameter;

    /**
     * Field that indicates that node has just been added.
     */
    private boolean newNode;

    public static final int FIRST_NODE = 0;
    public static final int PREVIOUS_NODE = 1;
    public static final int NEXT_NODE = 2;
    public static final int LAST_NODE = 3;

    public static final String _SEARCH_DETAILS_TAB_ = "search";
    public static final String _SEARCH_RESULTS_TAB = "results";
    public static final String _SEARCH_INFO_TAB = "browse";

    private String nodeType;
    private DisplayContentWrapper displayContent;
    private boolean objectivesModifiable = false;
    private String errorKey;
    private boolean hasCustomColumn = false;
    private String attributeLabel;
    private Long userId;
}

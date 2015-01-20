/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.common;

import com.zynap.talentstudio.web.organisation.BrowseNodeWrapper;

import java.io.Serializable;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public abstract class AbstractBrowseWrapper implements Serializable {

    public String getActiveTab() {
        return activeTab;
    }

    public void setActiveTab(String activeTab) {
        this.activeTab = activeTab;
    }

    /**
     *
     * @return Long the number of items in the collection.
     */
    public Long getNodesNumber() {
        return (currentNodes != null && currentNodes.size() > 0) ? new Long(currentNodes.size()) : null;
    }

    /**
     *
     * @return the list of items currently being browsed.
     */
    public List getCurrentNodes() {
        return currentNodes;
    }

    /**
     * Sets the collection of items one will be vrowsing.
     *
     * @param currentNodes
     */
    public void setCurrentNodes(List currentNodes) {
        this.currentNodes = currentNodes;
    }

    public int getNodeTarget() {
        return nodeTarget;
    }

    public void setNodeTarget(int nodeTarget) {
        this.nodeTarget = nodeTarget;
    }

    /**
     * Reflects the concept of browsing.
     *
     * @return Long the previous item's id in the collection of browsed items
     */
    public Long getPrevious() {
        final int currentItemIndex = getCurrentItemIndex();
        if (currentItemIndex > 0) {
            return getId(currentItemIndex - 1);
        }
        return null;
    }

    /**
     * Reflects the concept of browsing.
     *
     * @return Long the next item's id in the collection of browsed items
     */
    public Long getNext() {

        final int currentItemIndex = getCurrentItemIndex();
        if (currentItemIndex >= 0 && currentItemIndex < currentNodes.size() - 1) {
            return getId(currentItemIndex + 1);
        }

        return null;
    }

    /**
     * Reflects the concept of browsing.
     *
     * @return Long the first item's id in the collection of browsed items
     */
    public abstract Long getFirst();

    /**
     * Reflects the concept of browsing.
     *
     * @return Long the last item's id in the collection of browsed items
     */
    public abstract Long getLast();

    /**
     * Get the items id.
     *
     * @param pos the position in the collection
     * @return Long the items id at that position.
     */
    public abstract Long getId(int pos);

    /**
     * Get the index of the current item.
     *
     * @return int the index in the collection of items where the current item sits.
     */
    public abstract int getCurrentItemIndex();

    public abstract void setNodeId(Long itemId);

    /**
     * Determine which is the next item in the collection and set this as the node.
     */
    public void applyNextItemId() {
        switch (getNodeTarget()) {
            case BrowseNodeWrapper.FIRST_NODE:
                setNodeId(getFirst());
                break;
            case BrowseNodeWrapper.PREVIOUS_NODE:
                setNodeId(getPrevious());
                break;
            case BrowseNodeWrapper.NEXT_NODE:
                setNodeId(getNext());
                break;
            case BrowseNodeWrapper.LAST_NODE:
                setNodeId(getLast());
                break;
        }
    }

    /**
     * The active tab.
     */
    protected String activeTab;
    protected List currentNodes;
    /**
     * Indicates in which direction to look for the next item in the list - set by spring:bind to values such as BrowseNodeWrapper.FIRST_NODE, PREVIOUS_NODE, etc.
     */
    private int nodeTarget;
}

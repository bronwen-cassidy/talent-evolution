/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.display;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.talentstudio.util.collections.DomainObjectCollectionHelper;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class DisplayConfig extends ZynapDomainObject implements Comparable {

    public DisplayConfig() {
    }

    public DisplayConfig(String label) {
        this.label = label;
    }

    public DisplayConfig(Long id, String label) {
        super(id, true, label);
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public List<DisplayConfigItem> getDisplayConfigItems() {
        return displayConfigItems;
    }

    public void setDisplayConfigItems(List<DisplayConfigItem> displayConfigItems) {
        this.displayConfigItems = displayConfigItems;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DisplayConfigItem getDisplayConfigItem(Long id) {
        return (DisplayConfigItem) DomainObjectCollectionHelper.findById(displayConfigItems, id);
    }

    public int compareTo(Object o) {
        return getLabel().compareTo(((DisplayConfig) o).getLabel());
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("nodeType", getNodeType())
                .append("index", getIndex())
                .append("type", getType())
                .toString();
    }

    public boolean isExecutiveSummary() {
        return EXECUTIVE_SUMMARY_TYPE.equals(getType());
    }

    public boolean isPersonalExecutiveSummary() {
        return MY_EXEC_SUMMARY_TYPE.equals(getType());
    }

    public boolean isView() {
        return VIEW_TYPE.equals(getType());
    }

    public DisplayConfigItem getFirstDisplayConfigItem() {
        return displayConfigItems.get(0);
    }

    public boolean isAdd() {
        return ADD_TYPE.equals(getType());
    }

    public boolean isMyDetails() {
        return MY_DETAILS_TYPE.equals(getType());
    }

    public boolean isPositionType() {
        return POSITION_NODE_TYPE.equals(getNodeType());
    }

    public boolean isSubjectType() {
        return SUBJECT_NODE_TYPE.equals(getNodeType());
    }

    public List<DisplayConfigItem> getActiveDisplayConfigItems() {
        List<DisplayConfigItem> displayableItems = new ArrayList<DisplayConfigItem>();
        for (int i = 0; i < displayConfigItems.size(); i++) {
            DisplayConfigItem displayConfigItem = displayConfigItems.get(i);
            if (displayConfigItem.isActive()) {
                displayableItems.add(displayConfigItem);
            }
        }
        return displayableItems;
    }

    /**
     * Whether we are displaying for a position or subject
     */
    private String nodeType;

    private List<DisplayConfigItem> displayConfigItems;
    private Integer index;
    private String type;

    // the type of display configuration for the executive summery
    public static final String EXECUTIVE_SUMMARY_TYPE = "EXEC";

    public static final String MY_EXEC_SUMMARY_TYPE = "MY_EXEC";

    // the display configuration for the artefact views
    public static final String VIEW_TYPE = "VIEW";

    // the configuration type for add
    public static final String ADD_TYPE = "ADD";

    public static final String MY_DETAILS_TYPE = "MY_DETAILS";
    //NODE TYPES FOR POSITION AND SUBJECT
    private static final String POSITION_NODE_TYPE = "P";
    private static final String SUBJECT_NODE_TYPE = "S";
}

/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.tag.properties;

import com.zynap.common.util.FileUtils;

import java.io.Serializable;
import java.util.Properties;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public final class TabProperties implements Serializable {

    private static final String DEFAULT_FILENAME = "tab.properties";

    private static final String PROPERTY_STRING_TAB_START = "tab.tabstart";
    private static final String PROPERTY_STRING_TAB_END = "tab.tabend";
    private static final String PROPERTY_STRING_CONTENT_START = "tab.contentstart";
    private static final String PROPERTY_STRING_CONTENT_END = "tab.contentend";
    private static final String PROPERTY_STRING_INACTIVE_TAB = "tab.inactivetab";
    private static final String PROPERTY_STRING_INACTIVE_TAB_ONCLICK = "tab.inactivetab.onclick";
    private static final String PROPERTY_STRING_ACTIVE_TAB = "tab.activetab";

    private static final String PROPERTY_TREE_START = "tree.cellHtmlStart";
    private static final String PROPERTY_TREE_ROW = "tree.cellRowHtml";
    private static final String PROPERTY_TREE_END = "tree.cellHtmlEnd";
    private static final String PROPERTY_TREE_LAST_SIBLING = "tree.lastLeafHtml";
    private static final String PROPERTY_TREE_IMAGE_OPEN = "tree.imageOpen";
    private static final String PROPERTY_TREE_IMAGE_CLOSED = "tree.imageClosed";
    private static final String PROPERTY_TREE_TD_END = "tree.tdend";
    private static final String PROPERTY_TREE_LEAF = "tree.leafHtml";
    private static final String PROPERTY_TREE_CHECKED_VIEW_LINK = "tree.checkedViewLink";
    private static final String PROPERTY_TREE_ROW_END = "tree.rowEnd";
    private static final String PROPERTU_TREE_IMAGE_BAR = "tree.image.bar";
    private static final String PROPERTY_TREE_INVISIBLE_BAR = "tree.invisible.bar";

    private Properties properties = null;

    public TabProperties() {
        properties = FileUtils.loadPropertiesFile(DEFAULT_FILENAME);
    }

    public String getSelectorTreeInvisibleBar() {
        return getProperty(PROPERTY_TREE_INVISIBLE_BAR);
    }

    public String getSelectorTreeImageBar() {
        return getProperty(PROPERTU_TREE_IMAGE_BAR);
    }

    public String getTabStart() {
        return getProperty(PROPERTY_STRING_TAB_START);
    }

    public String getTabEnd() {
        return getProperty(PROPERTY_STRING_TAB_END);
    }

    public String getContentStart() {
        return getProperty(PROPERTY_STRING_CONTENT_START);
    }

    public String getContentEnd() {
        return getProperty(PROPERTY_STRING_CONTENT_END);
    }

    public String getInactiveTab() {
        return getProperty(PROPERTY_STRING_INACTIVE_TAB);
    }

    public String getInactiveOnClickTab() {
        return getProperty(PROPERTY_STRING_INACTIVE_TAB_ONCLICK);
    }

    public String getActiveTab() {
        return getProperty(PROPERTY_STRING_ACTIVE_TAB);
    }

    public String getJavaScriptActiveTab() {
        return getProperty("javascript." + PROPERTY_STRING_ACTIVE_TAB);
    }

    public String getTreeLastSibling() {
        return getProperty(PROPERTY_TREE_LAST_SIBLING);
    }

    public String getTreeStart() {
        return getProperty(PROPERTY_TREE_START);
    }

    public String getTreeCheckedViewLink() {
        return getProperty(PROPERTY_TREE_CHECKED_VIEW_LINK);
    }

    public String getTreeRow() {
        return getProperty(PROPERTY_TREE_ROW);
    }

    public String getTreeTdEnd() {
        return getProperty(PROPERTY_TREE_TD_END);
    }

    public String getTreeLeafEnd() {
        return getProperty(PROPERTY_TREE_LEAF);
    }

    public String getTreeRowEnd() {
        return getProperty(PROPERTY_TREE_ROW_END);
    }

    public String getTreeEnd() {
        return getProperty(PROPERTY_TREE_END);
    }

    public String getTreeOpenImage() {
        return getProperty(PROPERTY_TREE_IMAGE_OPEN);
    }

    public String getTreeClosedImage() {
        return getProperty(PROPERTY_TREE_IMAGE_CLOSED);
    }

    /**
     * Get property.
     *
     * @param key String
     * @return String
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
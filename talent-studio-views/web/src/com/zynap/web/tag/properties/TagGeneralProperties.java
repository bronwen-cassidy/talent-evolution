/*
 * Copyright (c) 2004 Zynap Limited. All rights reserved.
 */
package com.zynap.web.tag.properties;

import com.zynap.common.util.FileUtils;

import java.io.Serializable;
import java.util.Properties;

/**
 * Class or Interface description.
 *
 * @author Andreas Andersson
 * @version $Revision: $
 *          $Id: $
 */
public final class TagGeneralProperties implements Serializable {

    private static final String DEFAULT_FILENAME = "zynaptag.properties";

    private static final String PROPERTY_STRING_INFOBOX_START = "infobox.start.html";
    private static final String PROPERTY_STRING_INFOBOX_END = "infobox.end.html";
    private static final String PROPERTY_STRING_ACTIONBOX_START = "actionbox.start.html";
    private static final String PROPERTY_STRING_ACTIONBOX_END = "actionbox.end.html";
    private static final String PROPERTY_STRING_ACTIONENTRY_START = "actionentry.start.html";
    private static final String PROPERTY_STRING_ACTIONENTRY_END = "actionentry.end.html";

    private static final String PROPERTY_PERMISSION_TABLE_TOP = "permission.tabletop";
    private static final String PROPERTY_PERMISSION_TABLE_TOP_CLOSE = "permission.tabletop.close";
    private static final String PROPERTY_PERMISSION_TR_START = "permission.tr.start";
    private static final String PROPERTY_PERMISSION_NO_ENTRY = "permission.no.entry";
    private static final String PROPERTY_PERMISSION_ACTIVE = "permission.active";
    private static final String PROPERTY_PERMISSION_INACTIVE = "permission.inactive";
    private static final String PROPERTY_PERMISSION_ACTIVE_CHECKED = "permission.active.checked";
    private static final String PROPERTY_PERMISSION_NON_ACTIVE = "permission.active.unchecked";
    private static final String PROPERTY_PERMISSION_TABLE_HEAD = "permission.table.header";

    private static final String PROPERTY_POPUP_DIV_START = "popup.div.start.html";
    private static final String PROPERTY_POPUP_DIV_END = "popup.div.end.html";

    private Properties properties = null;

    public TagGeneralProperties() {
        properties = FileUtils.loadPropertiesFile(DEFAULT_FILENAME);
    }

    public String getPopupDivStart() {
        return getProperty(PROPERTY_POPUP_DIV_START);
    }

    public String getPopupDivEnd() {
        return getProperty(PROPERTY_POPUP_DIV_END);
    }

    public String getPermissionUnchecked() {
        return getProperty(PROPERTY_PERMISSION_NON_ACTIVE);
    }

    public String getPermissionChecked() {
        return getProperty(PROPERTY_PERMISSION_ACTIVE_CHECKED);
    }

    public String getPermissionInactive() {
        return getProperty(PROPERTY_PERMISSION_INACTIVE);
    }

    public String getPermissionActive() {
        return getProperty(PROPERTY_PERMISSION_ACTIVE);
    }

    public String getPermissionNoEntry() {
        return getProperty(PROPERTY_PERMISSION_NO_ENTRY);
    }

    public String getPermissionTrStart() {
        return getProperty(PROPERTY_PERMISSION_TR_START);
    }

    public String getPermissionTableTopClose() {
        return getProperty(PROPERTY_PERMISSION_TABLE_TOP_CLOSE);
    }

    public String getPermissionTableTop() {
        return getProperty(PROPERTY_PERMISSION_TABLE_TOP);
    }

    public String getPermissionTableHeader() {
        return getProperty(PROPERTY_PERMISSION_TABLE_HEAD);
    }

    public String getInfoBoxStartHtml() {
        return getProperty(PROPERTY_STRING_INFOBOX_START);
    }

    public String getInfoBoxEndHtml() {
        return getProperty(PROPERTY_STRING_INFOBOX_END);
    }

    public String getActionBoxStartHtml() {
        return getProperty(PROPERTY_STRING_ACTIONBOX_START);
    }

    public String getActionBoxEndHtml() {
        return getProperty(PROPERTY_STRING_ACTIONBOX_END);
    }

    public String getActionEntryStartHtml() {
        return getProperty(PROPERTY_STRING_ACTIONENTRY_START);
    }

    public String getActionEntryEndHtml() {
        return getProperty(PROPERTY_STRING_ACTIONENTRY_END);
    }

    public String getIframeSrc() {
        return getProperty("popup.iframe.html");
    }

    /**
     * Method getProperty
     *
     * @param key String
     * @return String
     */
    private String getProperty(String key) {
        return properties.getProperty(key);
    }
}

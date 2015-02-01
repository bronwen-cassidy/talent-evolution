/*
 * Copyright (c) 2004 Zynap Limited. All rights reserved.
 */
package com.zynap.web;

import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.web.util.JavaScriptUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Helper class to create and add tabs from a form component
 * The tags are hold as an ArrayList and an instance of this object
 * is located in the request scope the TabTag can display the defined tags
 * <p/>
 * A page tab URL_SUBTYPE is usually set to that defined in the TabTag url.  It may be possible to override this base URl
 * with a specific URL_SUBTYPE that is defined when the JSP is constructed.
 * <p/>
 * Had to use a hashmap to hold pagetabs to distinguish the active tab when nested tags are
 * used, tab retrieval will now be based on id rather than the index of an arraylist
 *
 * @author Andreas Andersson
 * @since 08/03/2004
 */
public class PageTabs extends Object implements Serializable {

    /**
     * Constructor
     */
    public PageTabs() {
    }

    public PageTabs(String url, String parameterTabName) {
        baseUrl = url;
        this.tabParamName = parameterTabName;
    }

    public void addTab(String id, String label, String onClickFunction) {
        Tab tab = new Tab(id, label);
        tab.setOnClickFunction(onClickFunction);
        tabList.put(id, tab);
    }

    public void addTab(String id, String label, String url, String onClickFunction) {
        Tab tab = new Tab(id, label, url);
        tab.setOnClickFunction(onClickFunction);
        tabList.put(id, tab);
    }


    public void setDefaultUrl(String url) {
        baseUrl = url;
    }

    public void setActiveTab(String id) {
        activeId = id;
    }

    public int getTabCount() {
        return tabList.size();
    }

    public Set getTabIds() {
        return tabList.keySet();
    }

    public Collection getTabs() {
        return tabList.values();
    }

    public String getActive() {
        return activeId;
    }

    public boolean isActive(String tabId) {

        if (alternativeActiveKeys.containsValue(tabId)) {
            return true;
        }

        if (activeId == null || activeId.trim().length() < 1) {
            // Get the tab
            Tab temp = tabList.get(tabId);
            return temp != null && temp.id.equals(activeId);
        }
        return activeId.equals(tabId);
    }

    public boolean isOnClick(String tabId) {
        Tab temp = tabList.get(tabId);
        return temp != null && temp.onClickFunction != null;
    }

    public String getUrl(String tabName, boolean javascript) {
        //Get the tab using the index
        Tab tab = tabList.get(tabName);
        //If tab has a specific url use that instead
        String url = tab.url;
        if (url != null) {
            return getTabURL(url, tab, javascript);
        }
        return getTabURL(baseUrl, tab, javascript);
    }

    public String getLabel(String n) {
        return tabList.get(n).label;
    }

    private String getTabURL(String url, Tab tab, boolean javascript) {

        Map<String, String> parameters = new HashMap<String, String>();
        if (!javascript)
        {
            parameters.put(tabParamName, tab.id);
            return ZynapWebUtils.buildURL(url, parameters);
        }
        return url;
    }

    public void addAlternativeKey(String key, String value) {
        alternativeActiveKeys.put(key, value);
    }

    public String getOnClickFunction(String tabId) {
        Tab tab = tabList.get(tabId);
        return tab.onClickFunction != null ? JavaScriptUtils.javaScriptEscape(tab.onClickFunction) : "";
    }

    public class Tab {

        Tab(String id, String label) {
            this.id = id;
            this.label = label;
        }

        /**
         * This constructor is used when a specific URL_SUBTYPE is specified by the JSP parameter
         *
         * @param id
         * @param label
         * @param url
         */
        Tab(String id, String label, String url) {
            this.id = id;
            this.label = label;
            this.url = url;
        }

        public void setOnClickFunction(String onClickFunction) {
            this.onClickFunction = onClickFunction;
        }

        public String id = null;
        public String label = null;
        public String url = null;
        public String onClickFunction = null;
    }

    private Map<String, Tab> tabList = new LinkedHashMap<String, Tab>();
    //This Hashmap can hold a set of alternative keys ie, the keys for other tabs
    private HashMap<String, String> alternativeActiveKeys = new HashMap<String, String>();
    private String tabParamName;

    private String baseUrl = "";
    private String activeId = null;
}

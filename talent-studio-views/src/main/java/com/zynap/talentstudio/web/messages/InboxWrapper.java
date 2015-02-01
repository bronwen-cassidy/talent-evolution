/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.messages;

import com.zynap.talentstudio.messages.MessageItem;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 13-Sep-2007 13:59:35
 */
public class InboxWrapper implements Serializable {

    public void setInboxItems(List<MessageItem> inboxItems) {
        this.inboxItems = inboxItems;
    }

    public List<MessageItem> getInboxItems() {
        return inboxItems;
    }

    public void setSortValues(String key, String value) {
        this.sortKey=key;
        this.sortValue = value;
    }

    public String getSortKey() {
        return sortKey;
    }

    public String getSortValue() {
        return sortValue;
    }

    public void setDisplayTagParams(Map displayTagParams) {
        this.displayTagParams = displayTagParams;
    }

    public Map getDisplayTagParams() {
        return displayTagParams;
    }

    private List<MessageItem> inboxItems;
    private String sortKey;
    private String sortValue;
    private Map displayTagParams;
}

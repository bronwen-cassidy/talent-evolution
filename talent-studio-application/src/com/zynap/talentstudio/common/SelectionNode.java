/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.common;

import com.zynap.talentstudio.organisation.portfolio.search.IField;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class SelectionNode implements Serializable, IField {

    public SelectionNode() {
    }

    public SelectionNode(Object value) {
        this.value = value;
    }

    public SelectionNode(Object value, Object name) {
        this.value = value;
        this.name = name;
    }

    public SelectionNode(Object value, Object messageKey, Object name) {
        this.value = value;
        this.messageKey = messageKey;
        this.name = name;
    }

    public SelectionNode(Object value, Object name, boolean selected) {
        this.value = value;
        this.selected = selected;
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public Object getName() {
        return this.name;
    }

    public String getLabel() {
        return name != null ? name.toString() : "";
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Object getMessageKey() {
        return messageKey;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SelectionNode)) return false;

        final SelectionNode selectionNode = (SelectionNode) o;

        if (messageKey != null ? !messageKey.equals(selectionNode.messageKey) : selectionNode.messageKey != null) return false;
        if (name != null ? !name.equals(selectionNode.name) : selectionNode.name != null) return false;
        if (value != null ? !value.equals(selectionNode.value) : selectionNode.value != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (value != null ? value.hashCode() : 0);
        result = 29 * result + (messageKey != null ? messageKey.hashCode() : 0);
        result = 29 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SelectionNode[");
        stringBuffer.append("\r\n value=").append(value);
        stringBuffer.append("\r\n selected=").append(selected);
        stringBuffer.append("\r\n messageKey=").append(messageKey);
        if (name != null) stringBuffer.append("\r\n name=").append(name);
        stringBuffer.append("]");

        return stringBuffer.toString();
    }

    private Object value;
    private boolean selected;
    private Object messageKey;
    private Object name;
}

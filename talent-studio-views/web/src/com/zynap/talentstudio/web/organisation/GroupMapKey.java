/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.organisation;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 22-Nov-2007 12:08:04
 */
public class GroupMapKey implements Serializable {

    public GroupMapKey(String groupName, String ref) {
        this.groupName = groupName;
        this.ref = ref;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getRef() {
        return ref;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupMapKey that = (GroupMapKey) o;

        if (!groupName.equals(that.groupName)) return false;

        return true;
    }

    public int hashCode() {
        return groupName.hashCode();
    }

    public void setNumValues(int numValues) {
        this.numValues = numValues;
    }

    public int getNumValues() {
        return numValues;
    }

    private String groupName;
    private boolean hidden;
    private String ref;
    private int numValues = 0;
}

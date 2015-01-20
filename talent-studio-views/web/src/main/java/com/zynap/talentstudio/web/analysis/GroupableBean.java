/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis;

import com.zynap.talentstudio.web.utils.SelectionNodeHelper;
import com.zynap.talentstudio.common.SelectionNode;

import java.io.Serializable;
import java.util.Collection;
import java.util.ArrayList;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 06-Aug-2009 22:19:00
 */
public class GroupableBean implements Serializable {

    public void setGroupIds(Long[] groupIds) {
        SelectionNodeHelper.enableDomainObjectSelections(groups, groupIds);
    }

    public Long[] getGroupIds() {
        return new Long[0];
    }

    public void setGroups(Collection<SelectionNode> groups) {
        this.groups = groups;
    }

    public Collection<SelectionNode> getGroups() {
        return groups;
    }

    public int getGroupsSize() {
        return groups.size();
    }

    public boolean hasAssignedGroups() {
        return SelectionNodeHelper.hasSelectedItems(groups);
    }

    protected Collection<SelectionNode> groups = new ArrayList<SelectionNode>();
}

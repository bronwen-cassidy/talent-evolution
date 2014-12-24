package com.zynap.talentstudio.web.arena;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * User: amark
 * Date: 30-Nov-2005
 * Time: 09:16:46
 * Interface that wrappers that can be published to an arena must implement.
 */
public interface IMenuItemContainer extends Serializable {

    void setActiveMenuItems(String[] positions);

    String[] getActiveMenuItems();

    void setHomePageMenuItems(String[] positions);

    String[] getHomePageMenuItems();

    List getMenuItemWrappers();

    boolean hasAssignedMenuItems();

    Set getAssignedMenuItems();
}

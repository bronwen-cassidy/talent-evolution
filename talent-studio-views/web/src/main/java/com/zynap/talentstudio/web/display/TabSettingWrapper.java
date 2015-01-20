/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.display;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author taulant bajraktari
 * @version 0.1
 * @since 26-May-2009 13:38:46
 */
public class TabSettingWrapper implements Serializable {


    public void setTabItems(TabItemWrapper[] tabItems) {

        this.tabItems = tabItems;
    }

    public TabItemWrapper[] getTabItems() {
        return tabItems;
    }
  
    private TabItemWrapper[] tabItems;
}

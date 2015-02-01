/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.display;

import com.zynap.talentstudio.arenas.Arena;
import com.zynap.talentstudio.arenas.IArenaManager;
import com.zynap.talentstudio.display.DisplayConfig;
import com.zynap.talentstudio.display.IDisplayConfigService;
import com.zynap.web.controller.ZynapDefaultFormController;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class TabListConfigsController extends ZynapDefaultFormController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        TabSettingWrapper wrapper = new TabSettingWrapper();
        List<DisplayConfig> viewConfigs = displayConfigService.findAll();
        filterViewDisplay(viewConfigs);
        //1 find what is the selected tab for the section
        //2 if nothing select, go to the default one
        //get displayconfigitems


        ArrayList<Arena> arenas = (ArrayList<Arena>) arenaManager.getArenas();
        removeAdmin(arenas);

        TabItemWrapper[] tabItems = new TabItemWrapper[arenas.size()];

        for (int i = 0; i < arenas.size(); i++) {
            Arena arena = arenas.get(i);

            final TabItemWrapper itemWrapper = new TabItemWrapper(arena);
            List<DisplayConfig> list = new LinkedList(displayConfigService.findByType(DisplayConfig.VIEW_TYPE));

            if (itemWrapper.isHomeArena()) {
                //add an extra view
                final List<DisplayConfig> mydetails = displayConfigService.findByType(DisplayConfig.MY_DETAILS_TYPE);
                list.add(mydetails.get(0));
            }

            if (list.size() > 3) {
                throw new Exception("Expected the following View's, Position, Subject and Subject Home only");
            }


            itemWrapper.setDisplayConfig(orderSequence(list));
            tabItems[i] = itemWrapper;
        }
        wrapper.setTabItems(tabItems);
        return wrapper;
    }


    /**
     * i need the order to be specific for the items
     * the expected income is three
     * add them to order, correctly to match with the jsp view
     * i.e. note jsp view for category displayconfig view selections
     *
     * 1)position detail view
     * 2)subject default view
     * 3)Subject default view  
     *
     * @param list
     * @return
     */
    private List orderSequence(List<DisplayConfig> list) {
        LinkedList configs = new LinkedList();

        for (DisplayConfig displayConfig : list) {
            if (displayConfig.isPositionType()) {
                configs.addFirst(displayConfig);
            } else if (displayConfig.isMyDetails()) {
                configs.addLast(displayConfig);
            } else {
                configs.add(displayConfig);
            }
        }
        return configs;
    }

    /**
     * remove anything that is not view or my details
     * @param viewConfigs
     */
    protected void filterViewDisplay(List<DisplayConfig> viewConfigs) {
        Iterator<DisplayConfig> viewConfigIter = viewConfigs.iterator();

        while (viewConfigIter.hasNext()) {
            DisplayConfig viewConfig = viewConfigIter.next();
            if (!(viewConfig.isView() || viewConfig.isMyDetails())) {
                viewConfigIter.remove();
            }
        }
    }

    /**
     * remove admin arena
     * @param arenas
     */
    protected void removeAdmin(Collection<Arena> arenas) {
        for (Arena arena : arenas) {
            if (arena.getArenaId().equals(IArenaManager.ADMIN_MODULE)) {
                arenas.remove(arena);
                return;
            }
        }
    }


    public void setArenaManager(IArenaManager arenaManager) {
        this.arenaManager = arenaManager;
    }

    public void setDisplayConfigService(IDisplayConfigService displayConfigService) {
        this.displayConfigService = displayConfigService;
    }

    protected IArenaManager arenaManager;
    protected IDisplayConfigService displayConfigService;

}
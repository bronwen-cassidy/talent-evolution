/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.display;

import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.arenas.Arena;
import com.zynap.talentstudio.arenas.ArenaDisplayConfigItem;
import com.zynap.talentstudio.display.DisplayConfigItem;
import com.zynap.talentstudio.display.DisplayConfig;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Collection;
import java.util.LinkedList;


public class EditTabConfigsController extends TabListConfigsController {

    protected ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {
        TabSettingWrapper tabSettingWrapper = (TabSettingWrapper) command;
        TabItemWrapper[] items = tabSettingWrapper.getTabItems();

        for (TabItemWrapper tabItemWrapper : items) {
            Arena arena = tabItemWrapper.getArena();
            Long[] tabIds = tabItemWrapper.getSelectTabIds();

            Collection<DisplayConfig> displayConfigs = tabItemWrapper.getDisplayConfigs();
            List<ArenaDisplayConfigItem> listArenaDisplayItems = new LinkedList<ArenaDisplayConfigItem>();
            /*
                from selected tab ids, ensure that the config items are found and stored
             */
            for (Long tabId : tabIds) {
                if (tabId != null) {
                    for (DisplayConfig displayConfig : displayConfigs) {


                        DisplayConfigItem configItem = displayConfig.getDisplayConfigItem(tabId);
                        if (configItem != null) {
                            ArenaDisplayConfigItem arenaItem = new ArenaDisplayConfigItem(null, arena, configItem);
                            listArenaDisplayItems.add(arenaItem);
                        }
                    }
                }
            }
            
            arena.assignNewArenaDisplayConfigItems(listArenaDisplayItems);
            arenaManager.updateArenaConfigItems(arena);
        }

        return new ModelAndView(new RedirectView(getSuccessView()));

    }

    protected ModelAndView onCancelInternal(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {
        return new ModelAndView(new RedirectView(getSuccessView()));
    }
}
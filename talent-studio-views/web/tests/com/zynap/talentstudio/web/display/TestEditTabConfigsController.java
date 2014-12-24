/**
 * Class or Interface description.
 *
 * @author taulant bajraktari
 * @since 20-May-2009 14:54:48
 * @version 0.1
 */
package com.zynap.talentstudio.web.display;

import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.display.DisplayConfig;
import com.zynap.talentstudio.display.DisplayConfigItem;
import com.zynap.talentstudio.arenas.ArenaDisplayConfigItem;
import com.zynap.talentstudio.arenas.Arena;
import com.zynap.talentstudio.arenas.IArenaManager;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;

import java.util.List;
import java.util.LinkedList;

/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
public class TestEditTabConfigsController extends ZynapMockControllerTest {

    protected void setUp() throws Exception {
        super.setUp();
        edittabListConfigsController = (EditTabConfigsController) getBean("editTabSettingsController");
        arenaManager = (IArenaManager) getBean("arenaManager");

    }

    protected void tearDown() throws Exception {
        super.tearDown();
        edittabListConfigsController = null;
    }


    public void testOnSubmit() throws Exception {
        TabSettingWrapper wrapper = (TabSettingWrapper) edittabListConfigsController.formBackingObject(mockRequest);
        TabItemWrapper[] tabItems = wrapper.getTabItems();

        for (TabItemWrapper tabItem : tabItems) {
            Long[] tabIds = new Long[]{new Long(-2), new Long(-13), new Long(-6)};
            tabItem.setSelectTabIds(tabIds);
        }
        DataBinder binder = new DataBinder(wrapper, ControllerConstants.COMMAND_NAME);
        final BindException exception = new BindException(binder.getBindingResult());
        try {
            edittabListConfigsController.onSubmit(mockRequest, mockResponse, wrapper, exception);
        } catch (Exception e) {
            fail(e.getMessage());
        }
       
    }

    public void testAddNew() throws Exception {

        // get an arena
        final Arena arena = arenaManager.getArena(IArenaManager.ADMIN_MODULE);

        ArenaDisplayConfigItem arenaDisplayConfigItem = new ArenaDisplayConfigItem();
        arenaDisplayConfigItem.setArena(arena);
        final DisplayConfigItem displayConfigItem = new DisplayConfigItem();
        displayConfigItem.setId(new Long(SUCCESSORS_CONFIG_ITEM_ID));
        arenaDisplayConfigItem.setDisplayConfigItem(displayConfigItem);

        List newConfigItems = new LinkedList();
        newConfigItems.add(arenaDisplayConfigItem);

        arena.assignNewArenaDisplayConfigItems(newConfigItems);

        arenaManager.updateArena(arena);
        assertEquals(arena.getArenaDisplayConfigItems().size(), 1);

    }

    private EditTabConfigsController edittabListConfigsController;
    private IArenaManager arenaManager;
    private final int SUCCESSORS_CONFIG_ITEM_ID = -12;
}
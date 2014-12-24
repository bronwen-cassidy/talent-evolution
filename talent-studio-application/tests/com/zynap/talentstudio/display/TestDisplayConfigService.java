/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.display;

import com.zynap.domain.admin.LoginInfo;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.CoreDetail;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.security.roles.Role;
import com.zynap.talentstudio.security.users.IUserService;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TestDisplayConfigService extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        displayConfigService = (IDisplayConfigService) applicationContext.getBean("displayConfigService");
    }

    public void testUpdate() throws Exception {
        DisplayConfig displayConfig = displayConfigService.findById(new Long(-1));
        List items = displayConfig.getDisplayConfigItems();
        for (int i = 0; i < items.size(); i++) {
            DisplayConfigItem displayConfigItem = (DisplayConfigItem) items.get(i);
            displayConfigItem.setLabel("test_" + i);
        }
        displayConfigService.update(displayConfig);

        DisplayConfig actual = displayConfigService.findById(new Long(-1));
        List actualList = actual.getDisplayConfigItems();
        for (int i = 0; i < actualList.size(); i++) {
            DisplayConfigItem displayConfigItem = (DisplayConfigItem) actualList.get(i);
            assertNotSame("test_" + i, displayConfigItem.getLabel());
        }

    }

    public void testFindById() throws Exception {
        final Long id = new Long(-2);

        DisplayConfig displayConfig = displayConfigService.findById(id);
        assertNotNull(displayConfig);
        assertEquals(id, displayConfig.getId());
        assertEquals(Node.SUBJECT_UNIT_TYPE_, displayConfig.getNodeType());
    }

    public void testFindAll() throws Exception {
        List configs = displayConfigService.findAll();
        assertTrue(configs.size() > 1);
    }

    public void testFindByNodeType() throws Exception {
        final String nodeType = Node.SUBJECT_UNIT_TYPE_;
        final List list = displayConfigService.find(nodeType);
        assertNotNull(list);
        assertFalse(list.isEmpty());
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            DisplayConfig displayConfig = (DisplayConfig) iterator.next();
            assertEquals(nodeType, displayConfig.getNodeType());
        }
    }

    public void testFindByType() throws Exception {
        final String type = DisplayConfig.VIEW_TYPE;

        final List list = displayConfigService.findByType(type);
        assertNotNull(list);
        assertFalse(list.isEmpty());
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            DisplayConfig displayConfig = (DisplayConfig) iterator.next();
            assertEquals(type, displayConfig.getType());
        }
    }

    public void testFindByNodeTypeAndConfigType() throws Exception {
        final String nodeType = Node.SUBJECT_UNIT_TYPE_;
        final String type = DisplayConfig.EXECUTIVE_SUMMARY_TYPE;

        final DisplayConfig displayConfig = displayConfigService.find(nodeType, type);
        assertNotNull(displayConfig);
        assertEquals(nodeType, displayConfig.getNodeType());
        assertEquals(type, displayConfig.getType());
    }

    public void testFindConfigItemById() throws Exception {
        // the item representing the persons details
        DisplayConfigItem configItem = displayConfigService.findConfigItemById(new Long(-20));
        assertNotNull(configItem);
        assertEquals(DisplayConfigItem.ATTRIBUTE_VIEW_TYPE, configItem.getContentType());
    }

    public void testFindUserDisplayItems() throws Exception {
        final List displayItems = displayConfigService.findUserDisplayItems(Node.SUBJECT_UNIT_TYPE_, DisplayConfig.VIEW_TYPE, new Long(2507));
        assertNotNull(displayItems);
    }

    public void testFindUserDisplayItems_OnlyDetails() throws Exception {
        // create a user with just the home role
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername("xxxxxxxx");
        loginInfo.setPassword("xxxxxxxx");
        User user = new User(loginInfo, new CoreDetail("test", "test"));

        DisplayConfig displayConfig = displayConfigService.find(Node.SUBJECT_UNIT_TYPE_, DisplayConfig.VIEW_TYPE);

        Role homeRole = null;
        // modify the roles on the items so each except details has only the admin role
        List displayConfigItems = displayConfig.getDisplayConfigItems();
        for (int i = 0; i < displayConfigItems.size(); i++) {
            DisplayConfigItem displayConfigItem = (DisplayConfigItem) displayConfigItems.get(i);
            if (!displayConfigItem.getId().equals(new Long(-9))) {
                Set roles = displayConfigItem.getRoles();
                for (Iterator iterator = roles.iterator(); iterator.hasNext();) {
                    Role role = (Role) iterator.next();
                    if(role.isHomeRole()) user.addRole(role);
                    if(!role.isAdminRole()) {
                        iterator.remove();
                    }
                }
            }
        }
        displayConfigService.update(displayConfig);

        IUserService userService = (IUserService) getBean("userService");
        userService.create(user);

        // now go and find the display config items should only be the details tab
        List userDisplayConfigItems = displayConfigService.findUserDisplayItems(Node.SUBJECT_UNIT_TYPE_, DisplayConfig.VIEW_TYPE, user.getId());

        assertEquals(1, userDisplayConfigItems.size());
    }


    private IDisplayConfigService displayConfigService;
}
/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.display;

import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.organisation.Node;

import java.util.Collection;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TestHibernateDisplayConfigDao extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        hibernateDisplayConfigDao = (HibernateDisplayConfigDao) applicationContext.getBean("displayConfigDao");
    }

    public void testGetDomainObjectClass() throws Exception {
        assertEquals(DisplayConfig.class, hibernateDisplayConfigDao.getDomainObjectClass());
    }

    public void testFindAll() throws Exception {
        Collection allConfig = hibernateDisplayConfigDao.findAll();
        assertTrue(allConfig.size() > 0);
    }

    public void testFindAllPositionTypeOnly() throws Exception {
        DisplayConfig positionConfig = hibernateDisplayConfigDao.find(Node.POSITION_UNIT_TYPE_, DisplayConfig.EXECUTIVE_SUMMARY_TYPE);
        assertNotNull(positionConfig);
    }

    public void testFindConfigItemById() throws Exception {
        // the item representing the persons details
        DisplayConfigItem configItem = hibernateDisplayConfigDao.findConfigItemById(new Long(-20));
        assertNotNull(configItem);
        assertEquals(DisplayConfigItem.ATTRIBUTE_VIEW_TYPE, configItem.getContentType());
    }

    public void testFind() throws Exception {
        List displayConfigItems = hibernateDisplayConfigDao.find(Node.SUBJECT_UNIT_TYPE_, DisplayConfig.VIEW_TYPE, ADMINISTRATOR_USER_ID);
        assertEquals(10, displayConfigItems.size());
        // assert the sort order
        int sortOrder = -1;
        for (int i = 0; i < displayConfigItems.size(); i++) {
            DisplayConfigItem displayConfigItem = (DisplayConfigItem) displayConfigItems.get(i);
            int itemOrder = displayConfigItem.getSortOrder().intValue();
            assertTrue(itemOrder >= sortOrder);
            sortOrder = itemOrder;
        }
    }

    public void testFindDisplayConfigReport() throws Exception {
        try {
            hibernateDisplayConfigDao.findDisplayConfigReport("S", "EXEC");
        } catch (Exception e) {
            fail("No exception expected but got " + e.getMessage());
        }
    }

    private HibernateDisplayConfigDao hibernateDisplayConfigDao;
}
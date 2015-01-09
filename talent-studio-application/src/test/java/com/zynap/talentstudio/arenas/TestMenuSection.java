package com.zynap.talentstudio.arenas;

/**
 * User: amark
 * Date: 10-Mar-2005
 * Time: 11:37:10
 */

import junit.framework.TestCase;

public class TestMenuSection extends TestCase {

    public void testShallowCopy() throws Exception {
        final MenuSectionPK pk = new MenuSectionPK("id","arenaId");
        final MenuSection menuSection = new MenuSection(pk, "label", 0);
        menuSection.setArena(new Arena("-19", "arena1"));
        menuSection.setUrl("test.htm");
        final MenuSection copiedMenuSection = menuSection.shallowCopy();

        assertNotNull(copiedMenuSection);
        assertFalse(menuSection == copiedMenuSection);
        assertTrue(menuSection.equals(copiedMenuSection));

        assertNotNull(copiedMenuSection.getMenuItems());
        assertEquals(menuSection.getLabel(), copiedMenuSection.getLabel());
        assertEquals(menuSection.getPrimaryKey(), copiedMenuSection.getPrimaryKey());
        assertEquals(menuSection.getSortOrder(), copiedMenuSection.getSortOrder());
        assertEquals(menuSection.getUrl(), copiedMenuSection.getUrl());
    }
}
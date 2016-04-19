/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.common.util;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 08-Nov-2006 16:25:17
 * @version 0.1
 */

import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class MapUtilTest {

    @Test
    public void testGetKeysEmpty() throws Exception {
        Map testMap = new HashMap();
        Object[] keys = MapUtil.getKeys(testMap, "test");
        assertNull(keys);
    }

    @Test
    public void testGetKeysNullTarget() throws Exception {
        Object[] keys = MapUtil.getKeys(null, null);
        assertNull(keys);
    }

    @Test
    public void testGetKeysNullValueNoMatches() throws Exception {
        Map<String, String> testMap = new HashMap<String, String>();
        testMap.put("a", "aaa");
        Object[] keys = MapUtil.getKeys(testMap, null);
        assertEquals(0, keys.length);
    }

    @Test
    public void testGetKeysNullValueMatch() throws Exception {
        Map<String, String> testMap = new HashMap<String, String>();
        testMap.put("a", "aaa");
        testMap.put("b", null);
        Object[] keys = MapUtil.getKeys(testMap, null);
        assertEquals(1, keys.length);
        assertEquals("b", keys[0]);
    }

    @Test
    public void testGetKeysNullKey() throws Exception {
        Map<String, String> testMap = new HashMap<String, String>();
        testMap.put("a", "aaa");
        testMap.put(null, null);
        Object[] keys = MapUtil.getKeys(testMap, null);
        assertEquals(1, keys.length);
        assertEquals(null, keys[0]);
    }

    @Test
    public void testGetKeysManyKeys() throws Exception {
        // linked to guarentee order so we can test the indexes and check no duplicates
        Map<String, String> testMap = new LinkedHashMap<>();
        testMap.put("a", "aaa");
        testMap.put("b", "aaa");
        testMap.put("c", "aaa");
        testMap.put("d", "aaa");
        Object[] keys = MapUtil.getKeys(testMap, "aaa");
        assertEquals(4, keys.length);
        assertEquals("a", keys[0]);
        assertEquals("b", keys[1]);
        assertEquals("c", keys[2]);
        assertEquals("d", keys[3]);
    }
}
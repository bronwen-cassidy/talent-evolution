/* 
 * Copyright (C)  Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.integration.conversion;

import junit.framework.TestCase;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import java.util.ArrayList;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author syeoh
 * @version 0.1
 * @since 16-Jul-2007 17:33:49
 */
public class TestSortUtil extends TestCase {

    public void testSortByTreeLevel() {

        String[] unSortedIds = {"22", "1", "14", "2", "3", "12", "13", "4", "5", "6", "7", "11", "15", "8", "9", "10"};
        String[] unSortedParentIds = {null,"-1", "-1", "1", "1", "14 ", "14", "2", "2", "3", "3", "12", "12", "4", "4", "5"};

        String[] levelOneIds = {"1", "14", "22"};
        String[] levelTwoIds = {"2", "3", "12", "13"};
        String[] levelThreeIds = {"4", "5", "6", "7", "11", "15"};
        String[] levelFourIds = {"8", "9", "10"};

        List<ISortNode> unsorted = createList(unSortedIds, unSortedParentIds);
        List<ISortNode> results = SortUtil.sortByTreeLevel(unsorted);

        int indexCount = 0;
        List<ISortNode> levelOne = results.subList(indexCount, levelOneIds.length);

        indexCount += levelOneIds.length;
        List<ISortNode> levelTwo = results.subList(indexCount, (indexCount+levelTwoIds.length));

        indexCount += levelTwoIds.length;
        List<ISortNode> levelThree = results.subList(indexCount, (indexCount+levelThreeIds.length));

        indexCount += levelThreeIds.length;
        List<ISortNode> levelFour = results.subList(indexCount, (indexCount+levelFourIds.length));

        assertTrue(exists(levelOne, "1"));
        assertTrue(exists(levelOne, "14"));
        assertTrue(exists(levelOne, "22"));

        assertTrue(exists(levelTwo, "2"));
        assertTrue(exists(levelTwo, "3"));
        assertTrue(exists(levelTwo, "12"));
        assertTrue(exists(levelTwo, "13"));

        assertTrue(exists(levelThree, "4"));
        assertTrue(exists(levelThree, "5"));
        assertTrue(exists(levelThree, "6"));
        assertTrue(exists(levelThree, "7"));
        assertTrue(exists(levelThree, "11"));
        assertTrue(exists(levelThree, "15"));

        assertTrue(exists(levelFour, "8"));
        assertTrue(exists(levelFour, "9"));
        assertTrue(exists(levelFour, "10"));
    }

    private static boolean exists(List<ISortNode> unsorted, final String id) {
        ISortNode node = (ISortNode) CollectionUtils.find(unsorted, new Predicate() {
            public boolean evaluate(Object object) {
                return id.equals(((ISortNode) object).getId());
            }
        });
        return node != null;
    }

    private List<ISortNode> createList(String[] ids, String[] parentIds) {
        assertEquals(ids.length, parentIds.length);
        List<ISortNode> list = new ArrayList<ISortNode>();
        for (int i = 0; i < ids.length; i++) {
            ISortNode node = new Node(ids[i], parentIds[i]);
            list.add(node);
        }
        return list;
    }

    private class Node implements ISortNode {

        public Node(String id, String parentId) {
            this.id = id;
            this.parentId = parentId;
        }

        public String getId() {
            return id;
        }

        public String getParentId() {
            return parentId;
        }

        private String id;
        private String parentId;

    }
}

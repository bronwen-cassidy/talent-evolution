package com.zynap.talentstudio.common;

/**
 * User: amark
 * Date: 01-Aug-2005
 * Time: 09:47:50
 */

import junit.framework.TestCase;

public class TestDirection extends TestCase {

    public void testHasVerticalDirection() throws Exception {
        assertTrue(Direction.hasVerticalDirection(Direction.VERTICAL.toString()));
    }

    public void testHasNADirection() throws Exception {
        assertTrue(Direction.hasNADirection(Direction.NA_DIRECTION.toString()));
    }

    public void testHasSubordinateDirection() throws Exception {
        assertTrue(Direction.hasSubordinateDirection(Direction.SUBORDINATE_POS.toString()));
    }

    public void testHasPositionDirection() throws Exception {
        assertTrue(Direction.hasPositionDirection(Direction.POSITION_LAB.toString()));
    }

    public void testHasPersonDirection() throws Exception {
        assertTrue(Direction.hasPersonDirection(Direction.PERSON_LAB.toString()));
    }

    public void testHasSuperiorDirection() throws Exception {
        assertTrue(Direction.hasSuperiorDirection(Direction.SUPERIOR_POS.toString()));
    }

    public void testHasHorizontalDirection() throws Exception {
        assertTrue(Direction.hasHorizontalDirection(Direction.HORIZONTAL.toString()));
    }
}
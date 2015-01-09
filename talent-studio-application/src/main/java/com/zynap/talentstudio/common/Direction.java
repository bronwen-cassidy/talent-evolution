/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.common;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public final class Direction implements Serializable {

    private Direction(String direction) {
        this.direction = direction;
    }

    public String toString() {
        return this.direction;
    }

    public boolean equals(Object command) {
        if (this == command) return true;
        if (!(command instanceof Direction)) return false;

        final Direction direction1 = (Direction) command;

        if (direction != null ? !direction.equals(direction1.direction) : direction1.direction != null) return false;

        return true;
    }

    public int hashCode() {
        return (direction != null ? direction.hashCode() : 0);
    }

    private String direction;

    public static final Direction NA_DIRECTION = new Direction("NA");

    public static final Direction VERTICAL = new Direction("Vertical");
    public static final Direction HORIZONTAL = new Direction("Horizontal");

    public static final Direction SUBORDINATE_POS = new Direction("Subordinate Position");
    public static final Direction SUPERIOR_POS = new Direction("Superior Position");
    public static final Direction POSITION_LAB = new Direction("Position");
    public static final Direction PERSON_LAB = new Direction("Person");

    public static boolean hasNADirection(String columnSource) {
        return equals(NA_DIRECTION, columnSource);
    }

    public static boolean hasVerticalDirection(String columnSource) {
        return equals(VERTICAL, columnSource);
    }

    public static boolean hasHorizontalDirection(String columnSource) {
        return equals(HORIZONTAL, columnSource);
    }

    public static boolean hasSubordinateDirection(String columnSource) {
        return equals(SUBORDINATE_POS, columnSource);
    }

    public static boolean hasSuperiorDirection(String columnSource) {
        return equals(SUPERIOR_POS, columnSource);
    }

    public static boolean hasPersonDirection(String columnSource) {
        return equals(PERSON_LAB, columnSource);
    }

    public static boolean hasPositionDirection(String columnSource) {
        return equals(POSITION_LAB, columnSource);
    }

    private static boolean equals(Direction direction, String columnSource) {
        return direction.toString().equals(columnSource);
    }
}

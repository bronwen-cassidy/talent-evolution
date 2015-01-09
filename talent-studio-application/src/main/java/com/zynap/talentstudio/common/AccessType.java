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
public class AccessType implements Serializable {

    private AccessType(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }    

    public static final AccessType PUBLIC_ACCESS = new AccessType("Public");
    public static final AccessType PROTECTED_ACCESS = new AccessType("Protected");
    public static final AccessType PRIVATE_ACCESS = new AccessType("Private");
    public static final String PUBLIC = "Public";
    public static final String PRIVATE = "Private";

    private String name;
}

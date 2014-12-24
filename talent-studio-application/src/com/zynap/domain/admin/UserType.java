/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.domain.admin;

import java.io.Serializable;


/**
 * Class or Interface description.
 * 
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class UserType implements Serializable {

    private UserType(String name) {
        _name = name;
    }

    public String toString() {
        return _name;
    }

    public static UserType create(String value) {
        if(USER.toString().equalsIgnoreCase(value)) {
            return USER;
        }
        return SUBJECT;
    }

    public static final UserType USER = new UserType("SYSTEM");
    public static final UserType SUBJECT = new UserType("SYSTEMSUBJECT");

    private String _name;
}

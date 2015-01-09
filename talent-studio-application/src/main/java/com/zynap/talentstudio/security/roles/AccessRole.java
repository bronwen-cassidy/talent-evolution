/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.security.roles;

import java.util.Set;


/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class AccessRole extends Role {

    /**
     * Basic constructor.
     */
    public AccessRole() {
    }

    /**
     * full constructor.
     */
    public AccessRole(Long id, String label, Boolean isPositionDefault, Boolean isOunitDefault, String description, boolean isActive, boolean isSystem, Boolean isSubDefault, Set permits) {
        super(id, ACCESS_TYPE, label, isPositionDefault, isOunitDefault, description, isActive, isSystem, isSubDefault, permits);
    }

    /**
     * minimal constructor.
     */
    public AccessRole(Long id, String label, String description, boolean isActive, boolean isSystem, Set permits) {
        super(id, ACCESS_TYPE, label, description, isActive, isSystem, permits);
    }

    public String getRoleType() {
        return Role.ACCESS_TYPE;
    }
}

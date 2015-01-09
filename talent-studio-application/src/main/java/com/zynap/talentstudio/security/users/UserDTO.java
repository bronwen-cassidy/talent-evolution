/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.security.users;

import com.zynap.domain.ZynapDomainObject;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 02-Aug-2007 17:01:16
 */
public class UserDTO extends ZynapDomainObject implements Comparable<UserDTO> {

    public UserDTO(String firstName, String secondName, Long id, boolean active) {
        
        this.firstName = firstName;
        this.secondName = secondName;
        this.id = id;
        this.active = active;
    }

    public UserDTO(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return firstName + " " + secondName;
    }

    public String getDisplayLabel() {
        return secondName + " " + firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public int compareTo(UserDTO o) {
        return getDisplayLabel().compareTo(o.getDisplayLabel());
    }

    private String firstName;
    private String secondName;
}

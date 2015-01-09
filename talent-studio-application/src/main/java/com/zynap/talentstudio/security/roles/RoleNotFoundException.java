package com.zynap.talentstudio.security.roles;

import com.zynap.exception.DomainObjectNotFoundException;

/**
 * User: amark
 * Date: 19-Jan-2005
 * Time: 16:25:14
 * Exception that indicates that a role cannot be found.
 */
public class RoleNotFoundException extends DomainObjectNotFoundException {

    /**
     * Constructor.
     *
     * @param label The role label
     */
    public RoleNotFoundException(String label) {
        super(Role.class, "label", label);
    }

    /**
     * Constructor.
     *
     * @param id The id
     * @param t The Throwable
     */
    public RoleNotFoundException(Long id, Throwable t) {
        super(Role.class, id, t);
    }
}

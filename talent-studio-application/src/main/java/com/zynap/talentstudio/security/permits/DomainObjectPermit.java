package com.zynap.talentstudio.security.permits;


/**
 * Object that represents a domain object permit.
 * A domain access permit is a permit that applies to a secure resource
 * in the system such as an individual position and indicates what can be done to it.
 * 
 * User: amark
 * Date: 20-Jan-2005
 * Time: 14:17:42
 */
public class DomainObjectPermit extends Permit {

    /**
     * default constructor.
     */
    public DomainObjectPermit() {
        super();
    }

    /**
     * full constructor.
     */
    public DomainObjectPermit(Long id, String action, String content, String description, boolean active, String url, String idParam, String contentParam, String clazz, String method) {
        super(id, DOMAIN_TYPE, action, content, description, active, url, idParam, contentParam, clazz, method);
    }

    /**
     * minimal constructor.
     */
    public DomainObjectPermit(Long id, String action, boolean active) {
        this(id, action, null, null, active, null, null, null, null, null);
    }

    /**
     * Is this an access permit or a domain permit.
     *
     * @return false.
     */
    public boolean isAccess() {
        return false;
    }
}

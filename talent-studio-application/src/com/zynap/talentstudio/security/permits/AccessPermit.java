package com.zynap.talentstudio.security.permits;

/**
 * Object that represents a security access permit.
 * <p/>
 * User: amark
 * Date: 20-Jan-2005
 * Time: 14:17:42
 */
public class AccessPermit extends Permit {

    /**
     * default constructor.
     */
    public AccessPermit() {
        super();
    }

    /**
     * full constructor.
     */
    public AccessPermit(Long id, String action, String content, String description, boolean active, String url, String idParam, String contentParam, String clazz, String method) {
        super(id, ACCESS_TYPE, action, content, description, active, url, idParam, contentParam, clazz, method);
    }

    /**
     * minimal constructor.
     */
    public AccessPermit(Long id, String action, boolean active) {
        this(id, action, null, null, active, null, null, null, null, null);
    }

    /**
     * Is this an access permit or a domain permit.
     *
     * @return true
     */
    public boolean isAccess() {
        return true;
    }
}

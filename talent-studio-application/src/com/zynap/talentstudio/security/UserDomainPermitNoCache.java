package com.zynap.talentstudio.security;

import java.io.Serializable;


/**
 * @hibernate.class table="ZYNAP_USER_DOMAIN_PERMITS"
 * dynamic-update="true"
 * dynamic-insert="true"
 */
public class UserDomainPermitNoCache extends UserDomainPermitCommon implements Serializable {

    /**
     * full constructor
     */
    public UserDomainPermitNoCache(Long userId, Long nodeId, Long permitId) {
        super(userId, nodeId, permitId);
    }

    /**
     * default constructor
     */
    public UserDomainPermitNoCache() {
        super();
    }
}

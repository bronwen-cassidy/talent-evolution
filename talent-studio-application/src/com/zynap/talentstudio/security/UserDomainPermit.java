package com.zynap.talentstudio.security;

import java.io.Serializable;


/**
 *        @hibernate.class
 *         table="USER_NODE_DOMAIN_PERMITS"
 *         dynamic-update="true"
 *         dynamic-insert="true"
 *
*/
public class UserDomainPermit extends UserDomainPermitCommon implements Serializable {

    /** identifier field */
        //private Long id;

        /** full constructor */
        public UserDomainPermit(Long userId, Long nodeId, Long permitId) {
            super(userId,nodeId,permitId);
        }

        /** default constructor */
        public UserDomainPermit() {
            super();
        }
    

}

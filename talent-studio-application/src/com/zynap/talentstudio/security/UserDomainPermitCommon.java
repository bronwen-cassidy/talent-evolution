package com.zynap.talentstudio.security;

import org.apache.commons.lang.builder.ToStringBuilder;

public class UserDomainPermitCommon {

    /**
     * @hibernate.property column="USER_ID"
     * length="22"
     */
    public UserDomainPermitCommon(Long userId, Long nodeId, Long permitId) {
        this.userId = userId;
        this.nodeId = nodeId;
        this.permitId = permitId;
    }

    /**
     * default constructor
     */
    public UserDomainPermitCommon() {
    }


    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @hibernate.property column="NODE_ID"
     * length="22"
     */
    public Long getNodeId() {
        return this.nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    /**
     * @hibernate.property column="PERMIT_ID"
     * length="22"
     */
    public Long getPermitId() {
        return this.permitId;
    }

    public void setPermitId(Long permitId) {
        this.permitId = permitId;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("userId", getUserId())
                .append("nodeId", getNodeId())
                .append("permitId", getPermitId())
                .toString();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDomainPermitCommon)) return false;
        final UserDomainPermitCommon userDomainPermit = (UserDomainPermitCommon) o;

        if (nodeId != null ? !nodeId.equals(userDomainPermit.nodeId) : userDomainPermit.nodeId != null) return false;
        if (permitId != null ? !permitId.equals(userDomainPermit.permitId) : userDomainPermit.permitId != null) return false;
        if (userId != null ? !userId.equals(userDomainPermit.userId) : userDomainPermit.userId != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (userId != null ? userId.hashCode() : 0);
        result = 29 * result + (nodeId != null ? nodeId.hashCode() : 0);
        result = 29 * result + (permitId != null ? permitId.hashCode() : 0);
        return result;
    }

    public Long getId() {
        return new Long(hashCode());
    }

    /**
     * identifier field
     */
    protected Long userId;
    /**
     * identifier field
     */
    protected Long nodeId;
    /**
     * identifier field
     */
    protected Long permitId;
}

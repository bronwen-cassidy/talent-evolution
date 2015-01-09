/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.audit;

import com.zynap.domain.admin.User;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */

/**
 * @author Hibernate CodeGenerator
 */
public class SessionLog implements Serializable {

    /**
     * full constructor
     */
    public SessionLog(String sessionId, String remoteHost, String status, Date loggedInDts, Date loggedOutDts, String loggedOutReason, User user) {
        this.sessionId = sessionId;
        this.remoteHost = remoteHost;
        this.status = status;
        this.loggedInDate = loggedInDts;
        this.loggedOutDate = loggedOutDts;
        this.loggedOutReason = loggedOutReason;
        this.user = user;
    }

    /**
     * default constructor
     */
    public SessionLog() {
    }

    /**
     * minimal constructor
     */
    public SessionLog(String sessionId, String remoteHost, String status, Date loggedInDts, User user) {
        this.sessionId = sessionId;
        this.remoteHost = remoteHost;
        this.status = status;
        this.loggedInDate = loggedInDts;
        this.user = user;
    }

    public SessionLog(Long id, String sessionId) {
        this.id = id;
        this.sessionId = sessionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getRemoteHost() {
        return this.remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLoggedInDate() {
        return this.loggedInDate;
    }

    public void setLoggedInDate(Date loggedInDate) {
        this.loggedInDate = loggedInDate;
    }

    public Date getLoggedOutDate() {
        return this.loggedOutDate;
    }

    public void setLoggedOutDate(Date loggedOutDate) {
        this.loggedOutDate = loggedOutDate;
    }

    public String getLoggedOutReason() {
        return this.loggedOutReason;
    }

    public void setLoggedOutReason(String loggedOutReason) {
        this.loggedOutReason = loggedOutReason;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("sessionId", getSessionId())
                .append("remoteHost", getRemoteHost())
                .append("status", getStatus())
                .append("loggedInDate", getLoggedInDate())
                .append("loggedOutDate", getLoggedOutDate())
                .append("loggedOutReason", getLoggedOutReason())
                .toString();
    }

    public boolean equals(Object other) {
        if ((this == other)) return true;
        if (!(other instanceof SessionLog)) return false;
        SessionLog castOther = (SessionLog) other;
        return new EqualsBuilder()
                .append(getSessionId(), castOther.getSessionId())
                .append(getRemoteHost(), castOther.getRemoteHost())
                .append(getStatus(), castOther.getStatus())
                .append(getLoggedInDate(), castOther.getLoggedInDate())
                .append(getLoggedOutDate(), castOther.getLoggedOutDate())
                .append(getLoggedOutReason(), castOther.getLoggedOutReason())
                .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
                .append(getSessionId())
                .append(getRemoteHost())
                .append(getStatus())
                .append(getLoggedInDate())
                .append(getLoggedOutDate())
                .append(getLoggedOutReason())
                .toHashCode();
    }

    /**
     * identifier field
     */
    private String sessionId;

    /**
     * persistent field
     */
    private String remoteHost;

    /**
     * persistent field
     */
    private String status;

    /**
     * persistent field
     */
    private Date loggedInDate;

    /**
     * nullable persistent field
     */
    private Date loggedOutDate;

    /**
     * nullable persistent field
     */
    private String loggedOutReason;

    /**
     * persistent field
     */
    private User user;
    private Long id;
}

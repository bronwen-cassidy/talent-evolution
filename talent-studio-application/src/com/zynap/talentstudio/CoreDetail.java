/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio;

import com.zynap.domain.Auditable;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class CoreDetail implements Serializable {

    /**
     * default constructor
     */
    public CoreDetail() {
    }

    /**
     * min constructor
     */
    public CoreDetail(String firstName, String secondName) {
        this.firstName = firstName;
        this.secondName = secondName;
    }

    /**
     * convenience constructor
     */
    public CoreDetail(String title, String firstName, String secondName) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.title = title;
    }

    /**
     * full constructor
     */
    public CoreDetail(String firstName, String secondName, String prefGivenName, String title, String contactEmail, String contactTelephone) {
        this.firstName = firstName;
        this.contactEmail = contactEmail;
        this.title = title;
        this.prefGivenName = prefGivenName;
        this.contactTelephone = contactTelephone;
        this.secondName = secondName;
    }

    /**
     * Get first name and second name as one.
     *
     * @return String
     */
    public String getName() {
        return firstName + " " + secondName;
    }

    public String getLastNameFirstName() {
        return secondName + " " + firstName;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getContactEmail() {
        return this.contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrefGivenName() {
        return this.prefGivenName;
    }

    public void setPrefGivenName(String prefGivenName) {
        this.prefGivenName = prefGivenName;
    }

    public String getContactTelephone() {
        return this.contactTelephone;
    }

    public void setContactTelephone(String contactTelephone) {
        this.contactTelephone = contactTelephone;
    }

    public String getSecondName() {
        return this.secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("firstName", getFirstName())
                .append("contactEmail", getContactEmail())
                .append("title", getTitle())
                .append("prefGivenName", getPrefGivenName())
                .append("contactTelephone", getContactTelephone())
                .append("secondName", getSecondName())
                .toString();
    }

    /**
     * identifier field
     */
    private Long id;

    /**
     * persistent field
     */
    private String firstName;

    /**
     * nullable persistent field
     */
    private String contactEmail;

    /**
     * nullable persistent field
     */
    private String title;

    /**
     * nullable persistent field
     */
    private String prefGivenName;

    /**
     * nullable persistent field
     */
    private String contactTelephone;

    /**
     * persistent field
     */
    private String secondName;
}

/*
 * $Header: ${}
 * $Revision: ${}
 * $Date: 12-Nov-2007
 *
 * Copyright (c) 1999-2006 Bronwen Cassidy.  All rights reserved.
 */
package com.zynap.talentstudio.organisation.subjects;

import com.zynap.domain.admin.User;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 12-Nov-2007 11:35:53
 */
public class SubjectDTO extends Subject {

    public SubjectDTO(Long id, String firstName, String secondName, String currentJobInfo, String username) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.username = username;
        setCurrentJobInfo(currentJobInfo);
    }

    public SubjectDTO(Long id, String firstName, String secondName, User user) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        if(user != null) {
            username = user.getUserName();
        }
    }

    public SubjectDTO(Long id, String firstName, String secondName, String username, String dynamicAttributeLabel, String dynamicAttributeValue) {
        this.dynamicAttributeLabel = dynamicAttributeLabel;
        this.dynamicAttributeValue = dynamicAttributeValue;
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getUsername() {
        return username;
    }

    public String getLabel() {
        return firstName + " " + secondName;
    }

    public boolean isCanLogIn() {
        return username != null;
    }

    public String getDynamicAttributeLabel() {
        return dynamicAttributeLabel;
    }

    public String getDynamicAttributeValue() {
        return dynamicAttributeValue;
    }

    public String toString() {
        return new StringBuilder(getSecondName())
                .append(" ")
                .append(getFirstName())
                .toString();
    }

    private String firstName;
    private String secondName;
    private String username;
    private String dynamicAttributeLabel;
    private String dynamicAttributeValue;
}

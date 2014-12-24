/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation.portfolio;

import com.zynap.domain.IBasicDomainObject;
import com.zynap.util.ArrayUtils;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Collections;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class ContentType implements IBasicDomainObject  {

    /**
     * identifier field
     */
    private String id;

    /**
     * persistent field
     */
    private String subTypes;

    /**
     * persistent field
     */
    private String label;

    /**
     * persistent field
     */
    private String type;

    /**
     * persistent field
     */
    private boolean active;

    /**
     * persistent field
     */
    private boolean available;

    /**
     * default constructor
     */
    public ContentType() {
    }

    public ContentType(String id) {
        this.id = id;
    }

    /**
     * full constructor
     */
    public ContentType(String id, String subTypes, String label, String type, boolean active, boolean available) {
        this.id = id;
        this.subTypes = subTypes;
        this.label = label;
        this.type = type;
        this.active = active;
        this.available = available;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubTypes() {
        return this.subTypes;
    }

    public void setSubTypes(String subTypes) {
        this.subTypes = subTypes;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isAvailable() {
        return available;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("subTypes", getSubTypes())
                .append("label", getLabel())
                .append("type", getType())
                .append("isActive", isActive())
                .append("isAvailable", isAvailable())
                .toString();
    }

    /**
     * Get sorted content types.
     *
     * @return sorted array of content sub types.
     */
    public String[] getContentSubTypes() {

        final List<String> list = ArrayUtils.stringToList(getSubTypes(), ",", String.class);
        Collections.sort(list);

        return (String[]) list.toArray();
    }
}

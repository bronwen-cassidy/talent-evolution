/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.security.permits;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.talentstudio.security.ISecureResource;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Object that represents a security permit.
 *
 * @author bcassidy
 */
public abstract class Permit extends ZynapDomainObject implements IPermit {

    public static final String DOMAIN_TYPE = "DP";
    public static final String ACCESS_TYPE = "AP";

    /**
     * default constructor.
     */
    public Permit() {
    }

    /**
     * full constructor.
     */
    public Permit(Long id, String type, String action, String content, String description, boolean active, String url, String idParam, String contentParam, String clazz, String method) {
        this.id = id;
        this.type = type;
        this.action = action;
        this.content = content;
        this.description = description;
        this.contentParam = contentParam;
        this.active = active;
        this.url = url;
        this.idParam = idParam;
        this.clazz = clazz;
        this.method = method;
    }

    /**
     * minimal constructor.
     */
    public Permit(Long id, String type, String action, boolean active) {
        this(id, type, action, null, null, active, null, null, null, null, null);
    }

    public String getObjectName() {
        return getClazz();
    }

    public void setObjectName(String objectName) {
        setClazz(objectName);
    }

    public String getLabel() {
        return getDescription();
    }

    public void setLabel(String label) {
        setDescription(label);
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIdParam() {
        return this.idParam;
    }

    public void setIdParam(String idParam) {
        this.idParam = idParam;
    }

    public String getContentParam() {
        return contentParam;
    }

    public void setContentParam(String contentParam) {
        this.contentParam = contentParam;
    }

    public String getClazz() {
        return this.clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void addSecureResource(ISecureResource secureResource) {
        if (secureResources==null) secureResources = new ArrayList<ISecureResource>();
        this.secureResources.add(secureResource);
    }

    public Collection<ISecureResource> getSecureResources() {
        return secureResources;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("type", getType())
                .append("action", getAction())
                .append("content", getContent())
                .append("description", getDescription())
                .append("active", isActive())
                .append("url", getUrl())
                .append("idParam", getIdParam())
                .append("clazz", getClazz())
                .append("method", getMethod())
                .toString();
    }

    /**
     * persistent field.
     */
    private String type;

    /**
     * persistent field.
     */
    private String action;

    /**
     * nullable persistent field.
     */
    private String content;

    /**
     * nullable persistent field.
     */
    private String description;

    /**
     * nullable persistent field.
     */
    private String url;

    /**
     * nullable persistent field.
     */
    private String idParam;

    /**
     * nullable persistent field.
     */
    private String contentParam;

    /**
     * nullable persistent field.
     */
    private String clazz;

    /**
     * nullable persistent field.
     */
    private String method;

    /**
     * Non-persistent field.
     * <br> Indicates whether or not the permit is active for the specified role.
     */
    private boolean selected;

    /**
     * Non-persistent field.
     * <br> Holds the {@link ISecureResource} that the permit relates to.
     */
    private Collection<ISecureResource> secureResources;
}

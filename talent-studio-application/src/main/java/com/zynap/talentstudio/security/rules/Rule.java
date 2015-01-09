/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.security.rules;

import com.zynap.domain.ZynapDomainObject;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class Rule extends ZynapDomainObject {

    public Rule() {
    }

    /**
     * full constructor
     */
    public Rule(Long id, String label, String description, String type, String value, Integer maxValue, Integer minValue, boolean isActive) {
        super(id, isActive, label);
        this.description = description;
        this.value = value;
        this.type = type;
        this.maxValue = maxValue;
        this.minValue = minValue;
    }

    public Rule(Long id, String label, String description, String type, String value, boolean isActive) {
        super(id, isActive, label);
        this.description = description;
        this.value = value;
        this.type = type;
    }

    public Integer getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Integer maxValue) {
        this.maxValue = maxValue;
    }

    public Integer getMinValue() {
        return minValue;
    }

    public void setMinValue(Integer minValue) {
        this.minValue = minValue;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClazz() {
        return this.clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public boolean isBoolean() {
        return BOOLEAN_TYPE.equals(this.type);
    }

    public boolean isNumber() {
        return NUMBER_TYPE.equals(this.type);
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("label", getLabel())
                .append("description", getDescription())
                .append("clazz", getClazz())
                .append("type", getType())
                .append("isActive", isActive())
                .append("value", getValue())
                .append("value", getMaxValue())
                .append("value", getMinValue())
                .toString();
    }

    /**
     * nullable persistent field
     */
    private String description;

    /**
     * persistent field
     */
    private String clazz;

    /**
     * persistent field
     */
    private String type;

    /**
     * The value that validates this rule
     */
    private String value;

    /**
     * The max setting for the value that validates this rule
     */
    private Integer maxValue;

    /**
     * The min setting for the value that validates this rule
     */
    private Integer minValue;


    private Config config;

    public static final String NUMBER_TYPE = "NUMBER";
    public static final String BOOLEAN_TYPE = "BOOLEAN";

    public static final Long PASSWORD_EXPIRED_RULE = new Long(-10);
    public static final Long PASSWORD_MIN_LENGTH = new Long(-11);
    public static final Long PASSWORD_MAX_LENGTH = new Long(-12);
    public static final Long PASSWORD_ALPHA_ONLY = new Long(-13);
    public static final Long PASSWORD_FORCE_CHANGE = new Long(-14);
    public static final Long PASSWORD_NUMBER_OF_UNIQUE = new Long(-15);
    public static final Long USERNAME_MIN_LENGTH = new Long(-20);
    public static final Long USERNAME_MAX_LENGTH = new Long(-21);
    public static final Long USERNAME_ALPHA_ONLY = new Long(-22);
    public static final Long MAX_NUMBER_FAILED_LOGIN_ATTEMPTS = new Long(-30);
}

package com.zynap.talentstudio.workflow;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;


/** @author Hibernate CodeGenerator */
public class MessageAttribute implements Serializable {

    /** identifier field */
    private MessageAttributePK compositeId;

    /** persistent field */
    private Long sequence;

    /** persistent field */
    private String type;

    /** persistent field */
    private String subtype;

    /** nullable persistent field */
    private String attach;

    /** persistent field */
    private String valueType;

    /** persistent field */
    private Long protectLevel;

    /** persistent field */
    private Long customLevel;

    /** nullable persistent field */
    private String format;

    /** nullable persistent field */
    private String textDefault;

    /** nullable persistent field */
    private Long numberDefault;

    /** nullable persistent field */
    private Date dateDefault;

    /** full constructor */
    public MessageAttribute(MessageAttributePK comp_id, Long sequence, String type, String subtype, String attach, String valueType, Long protectLevel, Long customLevel, String format, String textDefault, Long numberDefault, Date dateDefault) {
        this.compositeId = comp_id;
        this.sequence = sequence;
        this.type = type;
        this.subtype = subtype;
        this.attach = attach;
        this.valueType = valueType;
        this.protectLevel = protectLevel;
        this.customLevel = customLevel;
        this.format = format;
        this.textDefault = textDefault;
        this.numberDefault = numberDefault;
        this.dateDefault = dateDefault;
    }

    /** default constructor */
    public MessageAttribute() {
    }

    /** minimal constructor */
    public MessageAttribute(MessageAttributePK comp_id, Long sequence, String type, String subtype, String valueType, Long protectLevel, Long customLevel) {
        this.compositeId = comp_id;
        this.sequence = sequence;
        this.type = type;
        this.subtype = subtype;
        this.valueType = valueType;
        this.protectLevel = protectLevel;
        this.customLevel = customLevel;
    }

    public MessageAttributePK getCompositeId() {
        return this.compositeId;
    }

    public void setCompositeId(MessageAttributePK compositeId) {
        this.compositeId = compositeId;
    }

    public Long getSequence() {
        return this.sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubtype() {
        return this.subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getAttach() {
        return this.attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getValueType() {
        return this.valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public Long getProtectLevel() {
        return this.protectLevel;
    }

    public void setProtectLevel(Long protectLevel) {
        this.protectLevel = protectLevel;
    }

    public Long getCustomLevel() {
        return this.customLevel;
    }

    public void setCustomLevel(Long customLevel) {
        this.customLevel = customLevel;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getTextDefault() {
        return this.textDefault;
    }

    public void setTextDefault(String textDefault) {
        this.textDefault = textDefault;
    }

    public Long getNumberDefault() {
        return this.numberDefault;
    }

    public void setNumberDefault(Long numberDefault) {
        this.numberDefault = numberDefault;
    }

    public Date getDateDefault() {
        return this.dateDefault;
    }

    public void setDateDefault(Date dateDefault) {
        this.dateDefault = dateDefault;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("compositeId", getCompositeId())
            .append("sequence", getSequence())
            .append("type", getType())
            .append("subtype", getSubtype())
            .append("attach", getAttach())
            .append("valueType", getValueType())
            .append("protectLevel", getProtectLevel())
            .append("customLevel", getCustomLevel())
            .append("format", getFormat())
            .append("textDefault", getTextDefault())
            .append("numberDefault", getNumberDefault())
            .append("dateDefault", getDateDefault())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof MessageAttribute) ) return false;
        MessageAttribute castOther = (MessageAttribute) other;
        return new EqualsBuilder()
            .append(this.getCompositeId(), castOther.getCompositeId())
            .append(this.getSequence(), castOther.getSequence())
            .append(this.getType(), castOther.getType())
            .append(this.getSubtype(), castOther.getSubtype())
            .append(this.getAttach(), castOther.getAttach())
            .append(this.getValueType(), castOther.getValueType())
            .append(this.getProtectLevel(), castOther.getProtectLevel())
            .append(this.getCustomLevel(), castOther.getCustomLevel())
            .append(this.getFormat(), castOther.getFormat())
            .append(this.getTextDefault(), castOther.getTextDefault())
            .append(this.getNumberDefault(), castOther.getNumberDefault())
            .append(this.getDateDefault(), castOther.getDateDefault())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getCompositeId())
            .append(getSequence())
            .append(getType())
            .append(getSubtype())
            .append(getAttach())
            .append(getValueType())
            .append(getProtectLevel())
            .append(getCustomLevel())
            .append(getFormat())
            .append(getTextDefault())
            .append(getNumberDefault())
            .append(getDateDefault())
            .toHashCode();
    }

}

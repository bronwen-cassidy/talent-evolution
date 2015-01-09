package com.zynap.talentstudio.workflow;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;


/** @author Hibernate CodeGenerator */
public class MessageAttributePK implements Serializable {

    /** identifier field */
    private String messageType;

    /** identifier field */
    private String messageName;

    /** identifier field */
    private String name;

    /** full constructor
     * @param messageType
     * @param messageName
     * @param name
     * */
    public MessageAttributePK(String messageType, String messageName, String name) {
        this.messageType = messageType;
        this.messageName = messageName;
        this.name = name;
    }

    /** default constructor */
    public MessageAttributePK() {
    }

    public String getMessageType() {
        return this.messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageName() {
        return this.messageName;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("messageType", getMessageType())
            .append("messageName", getMessageName())
            .append("name", getName())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof MessageAttributePK) ) return false;
        MessageAttributePK castOther = (MessageAttributePK) other;
        return new EqualsBuilder()
            .append(this.getMessageType(), castOther.getMessageType())
            .append(this.getMessageName(), castOther.getMessageName())
            .append(this.getName(), castOther.getName())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getMessageType())
            .append(getMessageName())
            .append(getName())
            .toHashCode();
    }

}

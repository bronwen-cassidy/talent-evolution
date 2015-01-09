package com.zynap.talentstudio.help;

import com.zynap.domain.ZynapDomainObject;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * @author amark
 */
public final class HelpTextItem extends ZynapDomainObject {

    /**
     * Default constructor.
     */
    public HelpTextItem() {
    }

    /**
     * Constructor.
     *
     * @param id
     * @param blobValue
     */
    public HelpTextItem(Long id, byte[] blobValue) {
        this.id = id;
        this.blobValue = blobValue;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public byte[] getBlobValue() {
        return this.blobValue;
    }

    public void setBlobValue(byte[] blobValue) {
        this.blobValue = blobValue;
    }

    public String getContentAsString() {
        return new String(blobValue);
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .toString();
    }

    public String getHelpText() {
        return new String(getBlobValue());
    }

    /**
     * persistent field.
     */
    private byte[] blobValue;

    /**
     * Version field.
      */
    private Integer version;
    private static final long serialVersionUID = 5085417758734870743L;
}
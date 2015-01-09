package com.zynap.talentstudio.organisation.attributes;



/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 16-Jun-2005
 * Time: 12:19:05
 * Subclass of NodeExtendedAttribute for use with image dynamic attributes.
 */
public class NodeExtendedAttributeFile extends NodeExtendedAttribute {

    public NodeExtendedAttributeFile() {
        super();
    }

    public NodeExtendedAttributeFile(DynamicAttribute attributeDefinition) {
        super(null, attributeDefinition);
    }

    public byte[] getBlobValue() {
        return blobValue;
    }

    public void setBlobValue(byte[] blobValue) {
        this.blobValue = blobValue;
    }

    public void copyValues(NodeExtendedAttribute newNodeExtendedAttribute) {
        
        super.copyValues(newNodeExtendedAttribute);

        if (newNodeExtendedAttribute instanceof NodeExtendedAttributeFile) {
            final NodeExtendedAttributeFile nodeExtendedAttributeFile = (NodeExtendedAttributeFile) newNodeExtendedAttribute;
            setBlobValue(nodeExtendedAttributeFile.getBlobValue());
        }
    }

    /**
     * persistent field.
     */
    private byte[] blobValue;
}

package com.zynap.talentstudio.organisation.attributes;

/**
 * Subclass of AttributeValue to be used with NodeExtendedAttributeFiles only.
 *
 * User: jsueiras
 * Date: 16-Jun-2005
 * Time: 12:19:05
 */
public class AttributeValueFile extends AttributeValue {

    AttributeValueFile(DynamicAttribute dynamicAttribute) {
        super(dynamicAttribute);
    }

    public void initialiseNodeExtendedAttributes(String newValue) {
        setValue(newValue);
        getNodeExtendedAttributeFile().setValue(newValue);
    }

    public byte[] getBlobValue() {
        return getNodeExtendedAttributeFile().getBlobValue();
    }

    public void setBlobValue(byte[] blobValue) {
        getNodeExtendedAttributeFile().setBlobValue(blobValue);
    }

    /**
     * Overriden version as attribute value files only ever return the value (which is the filename) as their display value.
     *
     * @return
     */
    public String getDisplayValue() {

        String value = getValue();
        if (value == null) value = "";

        return value;
    }

    private NodeExtendedAttributeFile getNodeExtendedAttributeFile() {

        final NodeExtendedAttributeFile nodeExtendedAttributeFile;
        if (getNodeExtendedAttributes().isEmpty()) {
            nodeExtendedAttributeFile = new NodeExtendedAttributeFile(getDynamicAttribute());
            nodeExtendedAttributeFile.setDisabled(isDisabled());
            addValue(nodeExtendedAttributeFile, true);
        } else {
            nodeExtendedAttributeFile = (NodeExtendedAttributeFile) getNodeExtendedAttributes().iterator().next();
            setDisabled(nodeExtendedAttributeFile.isDisabled());
        }

        return nodeExtendedAttributeFile;
    }
}
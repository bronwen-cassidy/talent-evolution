/**
 * RespondentContentType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.zynap.talentstudio.middleware.soap.taapi.client;

public class RespondentContentType  implements java.io.Serializable {
    private java.lang.String respondentID;

    private java.lang.String contentKey;

    private java.lang.String format;

    private java.lang.String text;

    public RespondentContentType() {
    }

    public RespondentContentType(
           java.lang.String respondentID,
           java.lang.String contentKey,
           java.lang.String format,
           java.lang.String text) {
           this.respondentID = respondentID;
           this.contentKey = contentKey;
           this.format = format;
           this.text = text;
    }


    /**
     * Gets the respondentID value for this RespondentContentType.
     * 
     * @return respondentID
     */
    public java.lang.String getRespondentID() {
        return respondentID;
    }


    /**
     * Sets the respondentID value for this RespondentContentType.
     * 
     * @param respondentID
     */
    public void setRespondentID(java.lang.String respondentID) {
        this.respondentID = respondentID;
    }


    /**
     * Gets the contentKey value for this RespondentContentType.
     * 
     * @return contentKey
     */
    public java.lang.String getContentKey() {
        return contentKey;
    }


    /**
     * Sets the contentKey value for this RespondentContentType.
     * 
     * @param contentKey
     */
    public void setContentKey(java.lang.String contentKey) {
        this.contentKey = contentKey;
    }


    /**
     * Gets the format value for this RespondentContentType.
     * 
     * @return format
     */
    public java.lang.String getFormat() {
        return format;
    }


    /**
     * Sets the format value for this RespondentContentType.
     * 
     * @param format
     */
    public void setFormat(java.lang.String format) {
        this.format = format;
    }


    /**
     * Gets the text value for this RespondentContentType.
     * 
     * @return text
     */
    public java.lang.String getText() {
        return text;
    }


    /**
     * Sets the text value for this RespondentContentType.
     * 
     * @param text
     */
    public void setText(java.lang.String text) {
        this.text = text;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RespondentContentType)) return false;
        RespondentContentType other = (RespondentContentType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.respondentID==null && other.getRespondentID()==null) || 
             (this.respondentID!=null &&
              this.respondentID.equals(other.getRespondentID()))) &&
            ((this.contentKey==null && other.getContentKey()==null) || 
             (this.contentKey!=null &&
              this.contentKey.equals(other.getContentKey()))) &&
            ((this.format==null && other.getFormat()==null) || 
             (this.format!=null &&
              this.format.equals(other.getFormat()))) &&
            ((this.text==null && other.getText()==null) || 
             (this.text!=null &&
              this.text.equals(other.getText())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getRespondentID() != null) {
            _hashCode += getRespondentID().hashCode();
        }
        if (getContentKey() != null) {
            _hashCode += getContentKey().hashCode();
        }
        if (getFormat() != null) {
            _hashCode += getFormat().hashCode();
        }
        if (getText() != null) {
            _hashCode += getText().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RespondentContentType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "RespondentContentType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("respondentID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "RespondentID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contentKey");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ContentKey"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("format");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Format"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("text");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Text"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}

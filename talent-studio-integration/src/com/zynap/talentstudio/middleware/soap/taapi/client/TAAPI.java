/**
 * TAAPI.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.zynap.talentstudio.middleware.soap.taapi.client;

public interface TAAPI extends javax.xml.rpc.Service {
    public java.lang.String getTAAPIPortAddress();

    public com.zynap.talentstudio.middleware.soap.taapi.client.TAAPIPortType getTAAPIPort() throws javax.xml.rpc.ServiceException;

    public com.zynap.talentstudio.middleware.soap.taapi.client.TAAPIPortType getTAAPIPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}

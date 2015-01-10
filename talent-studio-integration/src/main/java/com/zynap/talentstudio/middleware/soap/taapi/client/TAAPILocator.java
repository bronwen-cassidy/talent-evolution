/**
 * TAAPILocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.zynap.talentstudio.middleware.soap.taapi.client;

public class TAAPILocator extends org.apache.axis.client.Service implements com.zynap.talentstudio.middleware.soap.taapi.client.TAAPI {

    public TAAPILocator() {
    }


    public TAAPILocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public TAAPILocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for TAAPIPort
    private java.lang.String TAAPIPort_address = "https://api.talentanalytics.com/";

    public java.lang.String getTAAPIPortAddress() {
        return TAAPIPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String TAAPIPortWSDDServiceName = "TAAPIPort";

    public java.lang.String getTAAPIPortWSDDServiceName() {
        return TAAPIPortWSDDServiceName;
    }

    public void setTAAPIPortWSDDServiceName(java.lang.String name) {
        TAAPIPortWSDDServiceName = name;
    }

    public com.zynap.talentstudio.middleware.soap.taapi.client.TAAPIPortType getTAAPIPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(TAAPIPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getTAAPIPort(endpoint);
    }

    public com.zynap.talentstudio.middleware.soap.taapi.client.TAAPIPortType getTAAPIPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.zynap.talentstudio.middleware.soap.taapi.client.TAAPIBindingStub _stub = new com.zynap.talentstudio.middleware.soap.taapi.client.TAAPIBindingStub(portAddress, this);
            _stub.setPortName(getTAAPIPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setTAAPIPortEndpointAddress(java.lang.String address) {
        TAAPIPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.zynap.talentstudio.middleware.soap.taapi.client.TAAPIPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.zynap.talentstudio.middleware.soap.taapi.client.TAAPIBindingStub _stub = new com.zynap.talentstudio.middleware.soap.taapi.client.TAAPIBindingStub(new java.net.URL(TAAPIPort_address), this);
                _stub.setPortName(getTAAPIPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("TAAPIPort".equals(inputPortName)) {
            return getTAAPIPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "TAAPI");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "TAAPIPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("TAAPIPort".equals(portName)) {
            setTAAPIPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}

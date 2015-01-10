/**
 * TAAPIBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.zynap.talentstudio.middleware.soap.taapi.client;

public class TAAPIBindingStub extends org.apache.axis.client.Stub implements com.zynap.talentstudio.middleware.soap.taapi.client.TAAPIPortType {

    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc[] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[8];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1() {
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("createRespondent");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "RespondentInfo"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "RespondentInfoType"), com.zynap.talentstudio.middleware.soap.taapi.client.RespondentInfoType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getRespondentInfo");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "RespondentIDList"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "ObjectIDListType"), java.lang.String[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "FieldList"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "FieldListType"), java.lang.String[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "RespondentInfoListType"));
        oper.setReturnClass(com.zynap.talentstudio.middleware.soap.taapi.client.RespondentInfoType[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "RespondentInfoList"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("updateRespondentInfo");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "RespondentID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "ObjectIDType"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "RespondentInfo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "RespondentInfoType"), com.zynap.talentstudio.middleware.soap.taapi.client.RespondentInfoType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "Success"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("setRespondentLabels");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "RespondentIDList"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "ObjectIDListType"), java.lang.String[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "RespondentLabelList"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "LabelListType"), java.lang.String[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "Success"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("removeRespondentLabels");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "RespondentIDList"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "ObjectIDListType"), java.lang.String[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "RespondentLabelList"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "LabelListType"), java.lang.String[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "Success"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getRespondentLabels");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "RespondentID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "ObjectIDType"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "LabelListType"));
        oper.setReturnClass(java.lang.String[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "RespondentLabelList"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getContent");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "RespondentIDList"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "ObjectIDListType"), java.lang.String[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ContentKey"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "ContentKeyType"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Format"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "ContentFormatType"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "RespondentContentListType"));
        oper.setReturnClass(com.zynap.talentstudio.middleware.soap.taapi.client.RespondentContentType[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "RespondentContentList"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("findRespondents");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "RespondentInfo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "RespondentInfoType"), com.zynap.talentstudio.middleware.soap.taapi.client.RespondentInfoType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "RespondentInfoListType"));
        oper.setReturnClass(com.zynap.talentstudio.middleware.soap.taapi.client.RespondentInfoType[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "RespondentInfoList"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[7] = oper;

    }

    public TAAPIBindingStub() throws org.apache.axis.AxisFault {
        this(null);
    }

    public TAAPIBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        this(service);
        super.cachedEndpoint = endpointURL;
    }

    public TAAPIBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service) super.service).setTypeMappingVersion("1.1");
        java.lang.Class cls;
        javax.xml.namespace.QName qName;
        javax.xml.namespace.QName qName2;
        java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
        java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
        java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
        java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
        java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
        java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
        java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
        java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
        java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
        java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
        qName = new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "ContentFormatType");
        cachedSerQNames.add(qName);
        cls = java.lang.String.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
        cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

        qName = new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "ContentKeyType");
        cachedSerQNames.add(qName);
        cls = java.lang.String.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
        cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

        qName = new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "FieldListType");
        cachedSerQNames.add(qName);
        cls = java.lang.String[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string");
        qName2 = null;
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "GenderType");
        cachedSerQNames.add(qName);
        cls = com.zynap.talentstudio.middleware.soap.taapi.client.GenderType.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);

        qName = new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "LabelListType");
        cachedSerQNames.add(qName);
        cls = java.lang.String[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string");
        qName2 = null;
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "ObjectIDListType");
        cachedSerQNames.add(qName);
        cls = java.lang.String[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "ObjectIDType");
        qName2 = null;
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "ObjectIDType");
        cachedSerQNames.add(qName);
        cls = java.lang.String.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
        cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

        qName = new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "RespondentContentListType");
        cachedSerQNames.add(qName);
        cls = com.zynap.talentstudio.middleware.soap.taapi.client.RespondentContentType[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "RespondentContentType");
        qName2 = null;
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "RespondentContentType");
        cachedSerQNames.add(qName);
        cls = com.zynap.talentstudio.middleware.soap.taapi.client.RespondentContentType.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "RespondentInfoListType");
        cachedSerQNames.add(qName);
        cls = com.zynap.talentstudio.middleware.soap.taapi.client.RespondentInfoType[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "RespondentInfoType");
        qName2 = null;
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "RespondentInfoType");
        cachedSerQNames.add(qName);
        cls = com.zynap.talentstudio.middleware.soap.taapi.client.RespondentInfoType.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
                    _call.setEncodingStyle(org.apache.axis.Constants.URI_SOAP11_ENC);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                    cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                    cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        } else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                    cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                    cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        } catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public void createRespondent(com.zynap.talentstudio.middleware.soap.taapi.client.holders.RespondentInfoTypeHolder respondentInfo) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://api.talentanalytics.com/2012-01-01#createRespondent");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "createRespondent"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[]{respondentInfo.value});

            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                java.util.Map _output;
                _output = _call.getOutputParams();
                try {
                    respondentInfo.value = (com.zynap.talentstudio.middleware.soap.taapi.client.RespondentInfoType) _output.get(new javax.xml.namespace.QName("", "RespondentInfo"));
                } catch (java.lang.Exception _exception) {
                    respondentInfo.value = (com.zynap.talentstudio.middleware.soap.taapi.client.RespondentInfoType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "RespondentInfo")), com.zynap.talentstudio.middleware.soap.taapi.client.RespondentInfoType.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public com.zynap.talentstudio.middleware.soap.taapi.client.RespondentInfoType[] getRespondentInfo(java.lang.String[] respondentIDList, java.lang.String[] fieldList) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://api.talentanalytics.com/2012-01-01#getRespondentInfo");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "getRespondentInfo"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[]{respondentIDList, fieldList});

            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (com.zynap.talentstudio.middleware.soap.taapi.client.RespondentInfoType[]) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.zynap.talentstudio.middleware.soap.taapi.client.RespondentInfoType[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.zynap.talentstudio.middleware.soap.taapi.client.RespondentInfoType[].class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public boolean updateRespondentInfo(java.lang.String respondentID, com.zynap.talentstudio.middleware.soap.taapi.client.RespondentInfoType respondentInfo) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://api.talentanalytics.com/2012-01-01#updateRespondentInfo");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "updateRespondentInfo"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[]{respondentID, respondentInfo});

            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return ((java.lang.Boolean) _resp).booleanValue();
                } catch (java.lang.Exception _exception) {
                    return ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_resp, boolean.class)).booleanValue();
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public boolean setRespondentLabels(java.lang.String[] respondentIDList, java.lang.String[] respondentLabelList) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://api.talentanalytics.com/2012-01-01#setRespondentLabels");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "setRespondentLabels"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[]{respondentIDList, respondentLabelList});

            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return ((java.lang.Boolean) _resp).booleanValue();
                } catch (java.lang.Exception _exception) {
                    return ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_resp, boolean.class)).booleanValue();
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public boolean removeRespondentLabels(java.lang.String[] respondentIDList, java.lang.String[] respondentLabelList) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://api.talentanalytics.com/2012-01-01#removeRespondentLabels");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "removeRespondentLabels"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[]{respondentIDList, respondentLabelList});

            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return ((java.lang.Boolean) _resp).booleanValue();
                } catch (java.lang.Exception _exception) {
                    return ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_resp, boolean.class)).booleanValue();
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public java.lang.String[] getRespondentLabels(java.lang.String respondentID) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://api.talentanalytics.com/2012-01-01#getRespondentLabels");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "getRespondentLabels"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[]{respondentID});

            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (java.lang.String[]) _resp;
                } catch (java.lang.Exception _exception) {
                    return (java.lang.String[]) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String[].class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public com.zynap.talentstudio.middleware.soap.taapi.client.RespondentContentType[] getContent(java.lang.String[] respondentIDList, java.lang.String contentKey, java.lang.String format) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://api.talentanalytics.com/2012-01-01#getContent");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "getContent"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[]{respondentIDList, contentKey, format});

            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (com.zynap.talentstudio.middleware.soap.taapi.client.RespondentContentType[]) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.zynap.talentstudio.middleware.soap.taapi.client.RespondentContentType[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.zynap.talentstudio.middleware.soap.taapi.client.RespondentContentType[].class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public com.zynap.talentstudio.middleware.soap.taapi.client.RespondentInfoType[] findRespondents(com.zynap.talentstudio.middleware.soap.taapi.client.RespondentInfoType respondentInfo) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://api.talentanalytics.com/2012-01-01#findRespondents");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "findRespondents"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[]{respondentInfo});

            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (com.zynap.talentstudio.middleware.soap.taapi.client.RespondentInfoType[]) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.zynap.talentstudio.middleware.soap.taapi.client.RespondentInfoType[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.zynap.talentstudio.middleware.soap.taapi.client.RespondentInfoType[].class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

}

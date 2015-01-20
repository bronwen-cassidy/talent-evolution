/**
 * TAAPITestCase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.zynap.talentstudio.middleware.soap.taapi.client;

import junit.framework.TestCase;

import com.zynap.talentstudio.middleware.soap.taapi.client.holders.RespondentInfoTypeHolder;

import org.apache.axis.message.SOAPHeaderElement;

import javax.xml.rpc.Service;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.HandlerRegistry;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import java.util.List;

public class TAAPITestCase extends TestCase {

    public TAAPITestCase(java.lang.String name) {
        super(name);
    }

    public void testTAAPIPortWSDL() throws Exception {
        javax.xml.rpc.ServiceFactory serviceFactory = javax.xml.rpc.ServiceFactory.newInstance();
        java.net.URL url = new java.net.URL(new com.zynap.talentstudio.middleware.soap.taapi.client.TAAPILocator().getTAAPIPortAddress() + "doc/2012-01-01/taapi.wsdl");
        javax.xml.rpc.Service service = serviceFactory.createService(url, new com.zynap.talentstudio.middleware.soap.taapi.client.TAAPILocator().getServiceName());
        assertTrue(service != null);
    }

    public void test1TAAPIPortCreateRespondent() throws Exception {
        com.zynap.talentstudio.middleware.soap.taapi.client.TAAPIBindingStub binding;
        try {
            binding = (com.zynap.talentstudio.middleware.soap.taapi.client.TAAPIBindingStub)
                          new com.zynap.talentstudio.middleware.soap.taapi.client.TAAPILocator().getTAAPIPort();

        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // authentication
        addAuth(binding);

        RespondentInfoType value = new RespondentInfoType();
        value.setFirstName("Brendan");
        value.setLastName("Wright");
        value.setEMail("Brendan.Wight@gmail.com");
        //value.setRespondentID("aaaadddddcdfg");
        value.setExtID("11483");
        //value.setCallbackURL("https://www.ynshosting.com/nhs/updatesubject.htm?ref_id=yns-0111");
        value.setGender(GenderType.M);
        //value.setStatus("complete");
        value.setLabels(new String[] {"Manager"});
        RespondentInfoTypeHolder holder = new RespondentInfoTypeHolder(value);
        binding.createRespondent(holder);

        String refId = holder.value.getRespondentID();
        System.out.println("refId");
        assertNotNull(refId);
        // TBD - validate results
    }

    private void addAuth(TAAPIBindingStub binding) throws SOAPException {
        SOAPHeaderElement authHeader = new SOAPHeaderElement("", "AuthHeader");
        SOAPElement node = authHeader.addChildElement("Username");
        node.addTextNode("bronwen.cassidy@talentscope.com");
        SOAPElement password = authHeader.addChildElement("Password");
        password.addTextNode("4321TS1234");

        binding.setHeader(authHeader);
    }

    public void test2TAAPIPortGetRespondentInfo() throws Exception {
        com.zynap.talentstudio.middleware.soap.taapi.client.TAAPIBindingStub binding;
        try {
            binding = (com.zynap.talentstudio.middleware.soap.taapi.client.TAAPIBindingStub)
                          new com.zynap.talentstudio.middleware.soap.taapi.client.TAAPILocator().getTAAPIPort();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);
        addAuth(binding);

        // Test operation
        RespondentInfoType[] value = null;
        String[] respondentIDList = new String[] {"BP0TJ56645"};
        String[] fieldList = new String[0];// {"#Manager", "lastName", "firstName", "extID"};
        //passing in fieldList as null returns all the values
        value = binding.getRespondentInfo(respondentIDList, null);
        assertEquals(1, value.length);
    }

    public void test3TAAPIPortUpdateRespondentInfo() throws Exception {
        com.zynap.talentstudio.middleware.soap.taapi.client.TAAPIBindingStub binding;
        try {
            binding = (com.zynap.talentstudio.middleware.soap.taapi.client.TAAPIBindingStub)
                          new com.zynap.talentstudio.middleware.soap.taapi.client.TAAPILocator().getTAAPIPort();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        boolean value = false;
        value = binding.updateRespondentInfo(new java.lang.String(), new com.zynap.talentstudio.middleware.soap.taapi.client.RespondentInfoType());
        // TBD - validate results
    }

    public void test4TAAPIPortSetRespondentLabels() throws Exception {
        com.zynap.talentstudio.middleware.soap.taapi.client.TAAPIBindingStub binding;
        try {
            binding = (com.zynap.talentstudio.middleware.soap.taapi.client.TAAPIBindingStub)
                          new com.zynap.talentstudio.middleware.soap.taapi.client.TAAPILocator().getTAAPIPort();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        boolean value = false;
        value = binding.setRespondentLabels(new String[]{}, new String[] {"manager"});
        // TBD - validate results
    }

    public void test5TAAPIPortRemoveRespondentLabels() throws Exception {
        com.zynap.talentstudio.middleware.soap.taapi.client.TAAPIBindingStub binding;
        try {
            binding = (com.zynap.talentstudio.middleware.soap.taapi.client.TAAPIBindingStub)
                          new com.zynap.talentstudio.middleware.soap.taapi.client.TAAPILocator().getTAAPIPort();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        boolean value = false;
        value = binding.removeRespondentLabels(new String[0], new String[] {"manager"});
        // TBD - validate results
    }

    public void test6TAAPIPortGetRespondentLabels() throws Exception {
        com.zynap.talentstudio.middleware.soap.taapi.client.TAAPIBindingStub binding;
        try {
            binding = (com.zynap.talentstudio.middleware.soap.taapi.client.TAAPIBindingStub)
                          new com.zynap.talentstudio.middleware.soap.taapi.client.TAAPILocator().getTAAPIPort();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        String[] value = null;
        value = binding.getRespondentLabels(new java.lang.String());
        // TBD - validate results
    }

    public void test7TAAPIPortGetContent() throws Exception {
        com.zynap.talentstudio.middleware.soap.taapi.client.TAAPIBindingStub binding;
        try {
            binding = (com.zynap.talentstudio.middleware.soap.taapi.client.TAAPIBindingStub)
                          new com.zynap.talentstudio.middleware.soap.taapi.client.TAAPILocator().getTAAPIPort();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        com.zynap.talentstudio.middleware.soap.taapi.client.RespondentContentType[] value = null;
        value = binding.getContent(new String[0], new java.lang.String(), new java.lang.String());
        // TBD - validate results
    }

    public void test8TAAPIPortFindRespondents() throws Exception {
        com.zynap.talentstudio.middleware.soap.taapi.client.TAAPIBindingStub binding;
        try {
            binding = (com.zynap.talentstudio.middleware.soap.taapi.client.TAAPIBindingStub)
                          new com.zynap.talentstudio.middleware.soap.taapi.client.TAAPILocator().getTAAPIPort();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);
        Service service = binding._getService();
        HandlerRegistry hr = service.getHandlerRegistry();

        List handlerChain = hr.getHandlerChain(binding.getPortName());

        HandlerInfo handlerInfo = new HandlerInfo();
        handlerInfo.setHandlerClass(RequestResponsePrinter.class);
        handlerChain.add(handlerInfo);

        // Time out after a minute
        binding.setTimeout(60000);
        addAuth(binding);

        // Test operation
        com.zynap.talentstudio.middleware.soap.taapi.client.RespondentInfoType[] value = null;

        RespondentInfoType type = new RespondentInfoType();
        type.setGender(GenderType.M);
        //type.setRespondentID("BP0TJ5664z5");
        //type.setEMail("Brendan.Wight@gmail.com");
        //type.setFirstName("@gmail.com");
        value = binding.findRespondents(type);
        assertTrue(value.length > 1);
        // TBD - validate results
    }

    /**
     * public void find() throws Exception {
     String endpoint = "https://api.talentanalytics.com/doc/2012-01-01/taapi.wsdl";

     String method = "findRespondents";
     //String in0 = "FirstName";
     //String in1 = "LastName";
     //String in2 = "Email";

     Service service = new Service();
     Call call = service.createCall();


     call.setTargetEndpointAddress(endpoint);
     call.setOperationName(new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", method));

     //call.addParameter("Bronwen", XMLType.XSD_STRING, ParameterMode.IN);
     //call.addParameter("Cassidy", XMLType.XSD_STRING, ParameterMode.IN);
     //call.addParameter("bronwen.cassidy@gmail.com", XMLType.XSD_STRING, ParameterMode.IN);

     call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);

     String ret = (String)call.invoke( new Object[0]);
     System.out.println("ret = " + ret);
     }

     public void findSubject() throws Exception {

     String message = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
     "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
     "xmlns:ns1=\"https://api.talentanalytics.com/\"\n" +
     "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
     "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:SOAPENC=\"" +
     "http://schemas.xmlsoap.org/soap/encoding/\" SOAPENV:" +
     "encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n" +
     "<SOAP-ENV:Body>\n" +
     "<ns1:getRespondentInfo>\n" +
     "<RespondentID " +
     "xsi:type=\"ns1:RespondentIDType\">BRC81TGTYS7729</RespondentID>\n" +
     "<FieldList xsi:nil=\"true\" xsi:type=\"ns1:FieldListType\"/>\n" +
     "</ns1:getRespondentInfo>\n" +
     "</SOAP-ENV:Body>\n" +
     "</SOAP-ENV:Envelope>";
     System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
     Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
     String soapUrl = "https://api.talentanalytics.com";
     //invoke business method
     //service.preparedOperation();
     URL url = new URL(soapUrl);
     URLConnection connection = url.openConnection();
     HttpURLConnection httpConn = (HttpURLConnection) connection;
     byte[] b = message.getBytes();
     httpConn.setRequestProperty("Content-Length",
     String.valueOf(b.length));
     httpConn.setRequestProperty("Content-Type","text/xml; charset=utf-8");
     httpConn.setRequestProperty("SOAPAction","https://api.talentanalytics.com/2012-01-01#getRespondentInfo");
     httpConn.setRequestMethod( "POST" );
     httpConn.setDoOutput(true);
     httpConn.setDoInput(true);
     OutputStream out = httpConn.getOutputStream();
     out.write( b );
     out.flush();
     out.close();

     InputStream inputStream = httpConn.getInputStream();
     InputStreamReader isr =
     new InputStreamReader(inputStream);
     BufferedReader in = new BufferedReader(isr);

     String inputLine;

     while ((inputLine = in.readLine()) != null)
     System.out.println(inputLine);

     in.close();
     }

     public static void main(String[] args) {
     try {
     //new TaapiClient().findSubject();
     new TaapiClient().find();
     //java org.apache.axis.wsdl.WSDL2Java http://someurl?WSDL
     } catch (Exception e) {
     e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
     }
     }

     public void setTemplate(WebServiceTemplate template) {
     this.template = template;
     }

     private WebServiceTemplate template;
     private String wsdlUrl = "https://api.talentanalytics.com/doc/1012-01-011/taapi.wsdl";
     private String conn = "https://api.talentanalytics.com/";
     */

}

package com.zynap.talentstudio.middleware.soap.taapi.client;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.apache.axis.MessageContext;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPMessage;

import java.util.Iterator;

/**
 * Created by bronwen.
 * Date: 11/03/12
 * Time: 20:58
 */
public class RequestResponsePrinter extends GenericHandler {

    public RequestResponsePrinter() {
        System.out.println("this = " + this);
    }

    public QName[] getHeaders() {
        return null;
    }

    public boolean handleRequest(javax.xml.rpc.handler.MessageContext context) {
        try {
            //SOAPMessageContext smc = (SOAPMessageContext) context;
            SOAPMessage msg = ((MessageContext) context).getMessage();
            System.out.println("request:");
            msg.writeTo(System.out);
            System.out.println("");

        } catch (Exception ex) {
            ex.printStackTrace();
//throw new RuntimeException( ex );
        }
        return true;
    }

    @Override
    public boolean handleResponse(javax.xml.rpc.handler.MessageContext context) {

        try {
            //SOAPMessageContext smc = (SOAPMessageContext) context;
            SOAPMessage msg = ((MessageContext) context).getMessage();
            System.out.println("response:");
            msg.writeTo(System.out);
            System.out.println("");

        } catch (Exception ex) {
            ex.printStackTrace();
//throw new RuntimeException( ex );
        }
        return true;
    }
}

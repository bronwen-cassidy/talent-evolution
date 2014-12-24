package com.zynap.talentstudio.integration.adapter;

import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.talentstudio.integration.common.IntegrationConstants;

import javax.xml.soap.DetailEntry;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;


/**
 * User: amark
 * Date: 19-Oct-2005
 * Time: 15:26:14
 */
public final class DomainObjectNotFoundExceptionHandler implements ExceptionHandler {

    /**
     * Process errors.
     *
     * @param entry
     * @param t
     * @param soapFactory
     * @throws javax.xml.soap.SOAPException
     */
    public void handle(DetailEntry entry, Throwable t, SOAPFactory soapFactory) throws SOAPException {

        entry.setAttribute(IntegrationConstants.CODE_ATTRIBUTE, IntegrationConstants.OBJECT_NOT_FOUND_CODE);
        final SOAPElement errorElement = soapFactory.createElement(IntegrationConstants.ERROR_NODE);

        // add id to message
        String id = ((DomainObjectNotFoundException) t).getId().toString();
        errorElement.addTextNode(id);
        entry.addChildElement(errorElement);
    }
}

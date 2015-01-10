package com.zynap.talentstudio.integration.adapter;

import javax.xml.soap.DetailEntry;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;

/**
 * User: amark
 * Date: 19-Oct-2005
 * Time: 16:17:27
 *
 * Interface that classes that handle exceptions for web services.
 */
public interface ExceptionHandler {

    /**
     * Process errors.
     * @param entry
     * @param t
     * @param soapFactory
     * @throws SOAPException
     */
    void handle(DetailEntry entry, Throwable t, SOAPFactory soapFactory) throws SOAPException;
}

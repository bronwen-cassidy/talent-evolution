package com.zynap.talentstudio.integration.adapter;

import com.zynap.common.util.StringUtil;
import com.zynap.talentstudio.integration.common.IntegrationConstants;

import javax.xml.soap.DetailEntry;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;

/**
 * User: amark
 * Date: 19-Oct-2005
 * Time: 15:26:14
 *
 * Default implementation of ExceptionHandler.
 */
public class DefaultExceptionHandler implements ExceptionHandler {


    /**
     * Process errors.
     *
     * @param entry
     * @param t
     * @param soapFactory
     * @throws SOAPException
     */
    public void handle(DetailEntry entry, Throwable t, SOAPFactory soapFactory) throws SOAPException {

        entry.setAttribute(IntegrationConstants.CODE_ATTRIBUTE, IntegrationConstants.SYSTEM_ERROR_CODE);
        final SOAPElement errorElement = soapFactory.createElement(IntegrationConstants.ERROR_NODE);

        String message = t.getMessage();
        if (message == null) message = StringUtil.getStackTraceAsString(t);

        errorElement.addTextNode(message);
        entry.addChildElement(errorElement);
    }
}

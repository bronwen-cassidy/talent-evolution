package com.zynap.talentstudio.integration.adapter;

import com.zynap.talentstudio.integration.common.IntegrationConstants;
import com.zynap.common.util.StringUtil;

import javax.xml.soap.DetailEntry;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPElement;


/**
 * User: amark
 * Date: 19-Oct-2005
 * Time: 15:26:14
 */
public final class NoDataForCommandExceptionHandler implements ExceptionHandler {

    /**
     * Process errors.
     *
     * @param entry
     * @param t
     * @param soapFactory
     * @throws javax.xml.soap.SOAPException
     */
    public void handle(DetailEntry entry, Throwable t, SOAPFactory soapFactory) throws SOAPException {

        entry.setAttribute(IntegrationConstants.CODE_ATTRIBUTE, IntegrationConstants.NO_DATA_CODE);
        final SOAPElement errorElement = soapFactory.createElement(IntegrationConstants.ERROR_NODE);

        String message = t.getMessage();
        if (message == null) message = StringUtil.getStackTraceAsString(t);

        errorElement.addTextNode(message);
        entry.addChildElement(errorElement);
    }
}

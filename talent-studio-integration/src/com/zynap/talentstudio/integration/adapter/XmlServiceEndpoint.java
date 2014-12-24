package com.zynap.talentstudio.integration.adapter;

import com.zynap.talentstudio.integration.common.IntegrationConstants;

import org.springframework.remoting.jaxrpc.ServletEndpointSupport;

import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.Detail;
import javax.xml.soap.DetailEntry;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPFactory;

import java.rmi.RemoteException;
import java.security.Principal;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 14-Oct-2005
 * Time: 14:10:48
 *
 * End point for web service - provides facade to access {@link com.zynap.talentstudio.integration.adapter.IXmlAdapter}.
 */
public final class XmlServiceEndpoint extends ServletEndpointSupport implements IRemoteXmlService {

    /**
     * Process the XML recieved as commands.
     * @param input A String of XML
     * @param attachment An array of byte arrays - each byte array contains the binary data for an attachment
     * @return String The response (XML)
     * @throws RemoteException
     */
    public String execute(String input, byte[][] attachment) throws RemoteException {

        final Principal userPrincipal = getServletEndpointContext().getUserPrincipal();

        String userName = "webserviceuser";
        if (userPrincipal != null) {
            userName = userPrincipal.getName();
        }
        logger.info("Received request *** " + input + " *** from user: " + userName);
        try {
            return adapter.execute(input, attachment, userName);
        } catch (Throwable t) {
            logger.error("Failed to process request *** " + input + " *** because of:", t);
            raiseSOAPException(t);
        }

        return null;
    }

    /**
     * Callback for custom initialization after the context has been set up.
     * <br/> Looks up IXmlAdapter and ExceptionHandlerFactory from spring application context.
     */
    protected void onInit() {
        adapter = (IXmlAdapter) getWebApplicationContext().getBean("xmlAdapter");
        exceptionHandlerFactory = (ExceptionHandlerFactory) getWebApplicationContext().getBean("exceptionHandlerFactory");
    }

    /**
     * Takes a throwable and look up the appropriate {@link ExceptionHandler} implementation to generate the XML to include in the exception.
     * <br/> Then throws a {@link javax.xml.rpc.soap.SOAPFaultException} with the XML as the detail.
     * @param t The throwable
     * @throws SOAPFaultException
     */
    private void raiseSOAPException(Throwable t) throws SOAPFaultException {

        Detail detail;
        try {

            final SOAPFactory soapFactory = SOAPFactory.newInstance();

            // build the root errors element
            detail = soapFactory.createDetail();
            final Name entryName = soapFactory.createName(IntegrationConstants.ERRORS_NODE);
            final DetailEntry entry = detail.addDetailEntry(entryName);

            // get the correct ExceptionHandler and use it 
            final ExceptionHandler exceptionHandler = exceptionHandlerFactory.getExceptionHandler(t);
            exceptionHandler.handle(entry, t, soapFactory);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        throw new SOAPFaultException(null, t.getMessage(), null, detail);
    }

    private IXmlAdapter adapter;

    private ExceptionHandlerFactory exceptionHandlerFactory;
}

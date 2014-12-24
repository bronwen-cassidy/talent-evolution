package com.zynap.talentstudio.web.integration;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.zynap.common.util.XmlUtils;
import com.zynap.talentstudio.ZynapTestCase;
import com.zynap.talentstudio.integration.common.IZynapCommand;
import com.zynap.talentstudio.integration.common.IntegrationConstants;

import org.apache.axis.AxisFault;
import org.apache.axis.AxisProperties;
import org.apache.axis.Constants;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.rmi.RemoteException;

/**
 * User: amark
 * Date: 19-Oct-2005
 * Time: 13:33:02
 */
public class TestXmlServiceEndpoint extends ZynapTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        // ensures that AXIS accepts servers with untrusted certificates like the Build server  
        AxisProperties.setProperty("org.apache.axis.components.net.SecureSocketFactory", "org.apache.axis.components.net.SunFakeTrustSocketFactory");

        Service service = new Service();
        call = (Call) service.createCall();
        call.setReturnType(Constants.XSD_STRING);

        final String username = System.getProperty("test.webservice.username");
        final String password = System.getProperty("test.webservice.password");

        final String baseURL = System.getProperty("test.base.url");
        //final String baseURL = "https://213.174.199.53/TS5";


        // set credentials
        call.setUsername(username);
        call.setPassword(password);

        // set url
        String endpoint = baseURL + END_POINT_URL;
        call.setTargetEndpointAddress(new URL(endpoint));
        call.setOperationName(new QName("execute"));

        call.addParameter("input", Constants.XSD_STRING, ParameterMode.IN);

        //logger.info("Testing XmlServiceEndpoint using URL:" + baseURL + ", user:" + username + ", password:" + password);
    }

    public void testHit() throws Exception {
        final String action = IZynapCommand.FIND_ACTION;

        String xml = createCommandsXML(action, "<organisationUnit><id>#DEFAULT_ORGUNIT#</id></organisationUnit>");

        final String output = (String) call.invoke(new Object[]{xml});
        System.out.println("output = " + output);
    }

    /**
     * Test with invalid security credentials.
     *
     * @throws Exception
     */
    public void testInvalidSecurityCredentials() throws Exception {

        call.setUsername(null);
        call.setPassword(null);

        try {
            invoke(null);
            fail("Expected failure as security credentials were invalid");
        } catch (RemoteException expected) {
            AxisFault axisFault = (AxisFault) expected;

            final QName faultCode = axisFault.getFaultCode();
            assertEquals(HTTP_FAULT_CODE, faultCode.getLocalPart());

            // HTTP Error code will be in second element - must be 401 (unauthorised)
            final Element[] faultDetails = axisFault.getFaultDetails();
            final Element errorsElement = faultDetails[1];
            final int errorCode = Integer.parseInt(errorsElement.getFirstChild().getNodeValue());
            assertEquals(401, errorCode);
        }
    }

    /**
     * Check what happens when no XML is sent to service.
     *
     * @throws Exception
     */
    public void testNoData() throws Exception {

        try {
            String xml = null;
            invoke(xml);
            fail("Expected failure as no data was supplied in call");
        } catch (RemoteException expected) {
            AxisFault axisFault = (AxisFault) expected;
            checkFaultDetails(axisFault, IntegrationConstants.SYSTEM_ERROR_CODE);
        }
    }

    /**
     * Check what happens when there is XML but no commands.
     *
     * @throws Exception
     */
    public void testNoCommands() throws Exception {

        try {
            String xml = "<commands></commands>";
            invoke(xml);
            fail("Expected failure as no commands were supplied in call");
        } catch (RemoteException expected) {
            AxisFault axisFault = (AxisFault) expected;
            checkFaultDetails(axisFault, IntegrationConstants.INVALID_DATA_CODE);
        }
    }

    /**
     * Check what happens when XML sent is invalid.
     *
     * @throws Exception
     */
    public void testInvalidXml() throws Exception {

        try {
            String xml = "<commands><command action=\"create\"></foo></commands>";
            invoke(xml);
            fail("Expected failure as invalid XML was supplied in call");
        } catch (RemoteException expected) {
            AxisFault axisFault = (AxisFault) expected;
            checkFaultDetails(axisFault, IntegrationConstants.INVALID_DATA_CODE);
        }
    }

    /**
     * Check what happens when command is supplied with no xml inside.
     *
     * @throws Exception
     */
    public void testCommandNoInput() throws Exception {

        try {
            String xml = createCommandsXML(IZynapCommand.CREATE_ACTION, null);
            invoke(xml);
            fail("Expected failure as invalid XML was supplied in call");
        } catch (RemoteException expected) {
            AxisFault axisFault = (AxisFault) expected;
            checkFaultDetails(axisFault, IntegrationConstants.NO_DATA_CODE);
        }
    }

    /**
     * Check that the response is correct.
     *
     * @throws Exception
     */
    public void testResponse() throws Exception {

        final String action = IZynapCommand.FIND_ACTION;

        String xml = createCommandsXML(action, "<organisationUnit><id>#DEFAULT_ORGUNIT#</id></organisationUnit>");

        final String output = (String) call.invoke(new Object[]{xml});
        assertNotNull(output);
        assertTrue(output.length() > 0);

        // check XML
        final Document document = XmlUtils.createFilteredDocument(new InputSource(new StringReader(output)), null);
        final NodeList errorsNodes = document.getElementsByTagName(IntegrationConstants.ERRORS_NODE);
        assertEquals(0, errorsNodes.getLength());

        final NodeList resultsNodes = document.getElementsByTagName(IntegrationConstants.RESULTS_NODE);
        assertEquals(1, resultsNodes.getLength());

        // check action is set on results node and matches the one we supplied
        final Node resultsNode = resultsNodes.item(0);
        final Node resultNode = resultsNode.getFirstChild();
        assertEquals(IntegrationConstants.RESULT_NODE, resultNode.getNodeName());
        final Node actionAttribute = resultNode.getAttributes().getNamedItem(IntegrationConstants.ACTION_ATTRIBUTE);
        assertEquals(action, actionAttribute.getNodeValue());

        // check results node has one child element - the org unit details
        assertEquals(1, resultNode.getChildNodes().getLength());
        final Node ouNode = resultNode.getFirstChild();
        assertEquals("organisationUnit", ouNode.getNodeName());
    }

    /**
     * Test command with invalid action.
     *
     * @throws Exception
     */
    public void testInvalidCommand() throws Exception {

        final String xml = createCommandsXML("foo", "<organisationUnit><id>#DEFAULT_ORGUNIT#</id></organisationUnit>");
        try {
            invoke(xml);
        } catch (RemoteException expected) {
            AxisFault axisFault = (AxisFault) expected;
            checkFaultDetails(axisFault, IntegrationConstants.SYSTEM_ERROR_CODE);
        }
    }

    /**
     * Test adding portfolio item to non-existent Node.
     *
     * @throws Exception
     */
    public void testInvalidPortfolioItem() throws Exception {

        final String action = IZynapCommand.CREATE_ACTION;

        final String xml = createCommandsXML(action, "<portfolioItem><id>PI000002</id><label>Adapter Porfolio Item</label><node><id>-123</id></node><contentType><id>DESC</id></contentType><contentSubType>TEXT</contentSubType><blobValue ref=\"0\"/><scope>PUBLIC</scope></portfolioItem>");
        try {
            invoke(xml);
        } catch (RemoteException expected) {
            AxisFault axisFault = (AxisFault) expected;
            checkFaultDetails(axisFault, IntegrationConstants.SYSTEM_ERROR_CODE);
        }
    }

    /**
     * Test find with invalid id returns error.
     *
     * @throws Exception
     */
    public void testFindWithInvalidId() throws Exception {

        final String action = IZynapCommand.FIND_ACTION;

        final String xml = createCommandsXML(action, "<position><id>-999</id></position>");
        try {
            invoke(xml);
        } catch (RemoteException expected) {
            AxisFault axisFault = (AxisFault) expected;
            checkFaultDetails(axisFault, IntegrationConstants.OBJECT_NOT_FOUND_CODE);
        }
    }

    /**
     * Check CDATA is handled correctly inside org unit and position labels - useful so that we can use characters like "&"
     * without having to replace them with the XML entities.
     *
     * @throws Exception
     */
    public void testCreateWithCDataTag() throws Exception {
        try {
            String xml = loadTestData("com/zynap/talentstudio/integration/adapter/position-test-data.xml");
            invoke(xml);
        } catch (RemoteException e) {
            e.printStackTrace();
            fail("No exception expected");
        }
        // do the delete delta
        try {
            String deleteXml = loadTestData("com/zynap/talentstudio/integration/adapter/delete-position-test-data.xml");
            invoke(deleteXml);
        } catch (IOException e) {
            e.printStackTrace();
            fail("No exception expected");
        }
    }

    private void invoke(String xml) throws RemoteException {
        call.invoke(new Object[]{xml});
    }

    /**
     * Load test data from file.
     *
     * @param filename
     * @return String containing the contents of the file.
     * @throws IOException If the file cannot be found or read.
     */
    private String loadTestData(String filename) throws IOException {
        InputStream testFileUrl = ClassLoader.getSystemResourceAsStream(filename);
        byte[] xmlBytes = new byte[25000];
        int numBytesRead = testFileUrl.read(xmlBytes);
        byte[] realBytes = new byte[numBytesRead];
        System.arraycopy(xmlBytes, 0, realBytes, 0, numBytesRead);
        return new String(realBytes);
    }

    /**
     * Build "commands" XML.
     *
     * @param action
     * @param input
     * @return String
     */
    private String createCommandsXML(String action, String input) {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("<").append(IntegrationConstants.COMMANDS_NODE).append(">");

        stringBuffer.append("<").append(IntegrationConstants.COMMAND_NODE);
        stringBuffer.append(" ").append(IntegrationConstants.ACTION_ATTRIBUTE).append("=\"").append(action).append("\">");

        if (input != null) {
            stringBuffer.append(input);
        }

        stringBuffer.append("</").append(IntegrationConstants.COMMAND_NODE).append(">");

        stringBuffer.append("</").append(IntegrationConstants.COMMANDS_NODE).append(">");

        return stringBuffer.toString();
    }

    /**
     * Check that the AxisFault has the expected fields set.
     * <br/> Also check that the AxisFault has a fault detail element which is of type {@link IntegrationConstants#ERRORS_NODE}
     * and that this element has a {@link IntegrationConstants#CODE_ATTRIBUTE} attribute with the expected value.
     *
     * @param axisFault
     * @param expectedCode
     */
    private void checkFaultDetails(final AxisFault axisFault, final String expectedCode) {

        final QName faultCode = axisFault.getFaultCode();
        assertEquals(USER_FAULT_CODE, faultCode.getLocalPart());

        assertNotNull(faultCode);
        assertNotNull(axisFault.getFaultString());
        assertNull(axisFault.getFaultActor());

        // the first element must be the errors element
        final Element[] faultDetails = axisFault.getFaultDetails();
        final Element errorsElement = faultDetails[0];
        assertEquals(IntegrationConstants.ERRORS_NODE, errorsElement.getNodeName());
        final String code = errorsElement.getAttribute(IntegrationConstants.CODE_ATTRIBUTE);
        assertEquals(expectedCode, code);

        // the error element must have 1 child node - the error node
        final NodeList errorNodes = errorsElement.getChildNodes();
        assertEquals(1, errorNodes.getLength());

        // check that the error node has the correct name
        final Node errorNode = errorNodes.item(0);
        assertEquals(IntegrationConstants.ERROR_NODE, errorNode.getNodeName());

        //  check that the error node has 1 child node - a text node with a node value (the value will be the stack trace or the application message)
        final NodeList childNodes = errorNode.getChildNodes();
        assertEquals(1, childNodes.getLength());
        final Node errorMessageNode = childNodes.item(0);
        assertNotNull(errorMessageNode.getNodeValue());
        assertEquals(Node.TEXT_NODE, errorMessageNode.getNodeType());
    }

    private Call call;

    private static final String END_POINT_URL = "/axis/XmlService";
    private static final String USER_FAULT_CODE = "Server.userException";
    private static final String HTTP_FAULT_CODE = "HTTP";
}

/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.integration.adapter;

import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.integration.BaseIntegrationTest;
import com.zynap.talentstudio.integration.common.IZynapCommand;
import com.zynap.talentstudio.organisation.IOrganisationUnitService;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.organisation.positions.IPositionService;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.positions.PositionAssociation;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.io.FileWriter;
import java.io.Reader;
import java.io.StringReader;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TestXmlAdaptor extends BaseIntegrationTest {

    protected void setUp() throws Exception {
        super.setUp();
        xmlAdapter = (IXmlAdapter) applicationContext.getBean("xmlAdapter");
    }

    public void testExecute() throws Exception {
        xmlAdapter.execute(SIMPLE_XML, null, USER_NAME);
    }

    public void testExecuteToFile() throws Exception {
        Writer writer = new FileWriter("organisations.xml");
        Reader reader = new StringReader(FIND_ALL_OU_XML);
        xmlAdapter.execute(writer, reader, null, "webserviceuser");
    }

    public void testFindOU() throws Exception {
        final String output = xmlAdapter.execute(FIND_DEFAULT_OU_XML, null, USER_NAME);
        System.out.println("output = " + output);
    }

    public void testFindAllOU() throws Exception {
        final String output = xmlAdapter.execute(FIND_ALL_OU_XML, null, USER_NAME);
        System.out.println("output = " + output);
    }

    public void testFindPosition() throws Exception {
        final String output = xmlAdapter.execute(FIND_DEFAULT_POSITION_XML, null, USER_NAME);
        System.out.println("output = " + output);
    }

    public void testFindNonExistentPosition() throws Exception {
        try {
            xmlAdapter.execute(FIND_INVALID_POSITION_XML, null, USER_NAME);
            fail("Should have failed due to invalid position id");
        } catch (DomainObjectNotFoundException ignored) {
        }
    }

    public void testFindAllPosition() throws Exception {
        final String output = xmlAdapter.execute(FIND_ALL_POSITION_XML, null, USER_NAME);
        System.out.println("output = " + output);
    }

    public void testCreateOrgUnits() throws Exception {
        xmlAdapter.execute(CREATE_ORG_UNITS_XML, ATTACHMENTS, USER_NAME);

        IOrganisationUnitService organisationUnitService = (IOrganisationUnitService) getBean("organisationUnitService");
        final List hierarchy = organisationUnitService.findOrgUnitTree(OrganisationUnit.ROOT_ORG_UNIT_ID);

        // find grand child
        OrganisationUnit selectedOrganisationUnit = null;
        for (Iterator iterator = hierarchy.iterator(); iterator.hasNext();) {
            OrganisationUnit child = (OrganisationUnit) iterator.next();
            if (child.getLabel().equals("Finance")) {
                selectedOrganisationUnit = child;
                break;
            }
        }

        // check has correct parent
        assertNotNull(selectedOrganisationUnit);
        final OrganisationUnit parent = organisationUnitService.findByID(selectedOrganisationUnit.getParent().getId());
        assertEquals(DEFAULT_ORG_UNIT_ID, parent.getParent().getId());
    }

    public void testCreateSubject() throws Exception {

        xmlAdapter.execute(CREATE_SUBJECT_XML, ATTACHMENTS, USER_NAME);

        Subject selectedSubject = null;
        ISubjectService subjectService = (ISubjectService) getBean("subjectService");
        final List all = subjectService.findAll();
        for (Iterator iterator = all.iterator(); iterator.hasNext();) {
            Subject subject = (Subject) iterator.next();
            if (subject.getFirstName().equals(FIRST_NAME)) {
                selectedSubject = subject;
                break;
            }
        }

        // check that the subject has been added
        assertNotNull(selectedSubject);

        // check subject associations
        assertFalse(selectedSubject.getSubjectAssociations().isEmpty());

    }

    public void testCreatePosition() throws Exception {
        xmlAdapter.execute(CREATE_POSITION_XML, ATTACHMENTS, USER_NAME);

        Position selectedPosition = null;
        IPositionService positionService = (IPositionService) getBean("positionService");

        final List all = positionService.findAll();
        for (Iterator iterator = all.iterator(); iterator.hasNext();) {
            Position position = (Position) iterator.next();
            if (position.getLabel().equals(POS_LABEL)) {
                selectedPosition = position;
                break;
            }
        }

        // check position - must have default org unit and position
        assertNotNull(selectedPosition);
        assertTrue(selectedPosition.getOrganisationUnit().isDefault());
        assertTrue(selectedPosition.getParent().isDefault());

        // check target associations
        assertTrue(selectedPosition.getTargetAssociations().isEmpty());

        // check source association
        final Collection sourceAssociations = selectedPosition.getSourceAssociations();
        assertEquals(1, sourceAssociations.size());
        final PositionAssociation positionAssociation = (PositionAssociation) sourceAssociations.iterator().next();
        assertEquals(selectedPosition.getParent(), positionAssociation.getTarget());
        assertEquals(selectedPosition, positionAssociation.getSource());
        assertTrue(positionAssociation.isPrimary());

        // check portfolio items
        assertEquals(1, selectedPosition.getPortfolioItems().size());

        // check extended attributes - must be 2
        final Set extendedAttributes = selectedPosition.getExtendedAttributes();
        assertEquals(2, extendedAttributes.size());
        for (Iterator iterator = extendedAttributes.iterator(); iterator.hasNext();) {
            NodeExtendedAttribute nodeExtendedAttribute = (NodeExtendedAttribute) iterator.next();
            assertEquals(selectedPosition, nodeExtendedAttribute.getNode());
            assertNotNull(nodeExtendedAttribute.getValue());
        }
    }

    public void testCreateTwoPositions() throws Exception {
        xmlAdapter.execute(CREATE_TWO_POSITIONS_XML, ATTACHMENTS, USER_NAME);
    }

    public void testUpdatePosition() throws Exception {
        xmlAdapter.execute(UPDATE_POSITION_XML, ATTACHMENTS, USER_NAME);
    }

    public void testCreatePortfolioItem() throws Exception {

        xmlAdapter.execute(CREATE_PORTFOLIO_XML, ATTACHMENTS, USER_NAME);
        
    }

    public void testExistsCommand() throws Exception {

        //use find position XML but change action to "exists" from "find"
        final String xml = FIND_DEFAULT_POSITION_XML.replaceFirst(IZynapCommand.FIND_ACTION, IZynapCommand.EXISTS_ACTION);
        final String output = xmlAdapter.execute(xml, ATTACHMENTS, USER_NAME);
        assertNotNull(output);

        // do a find and then check the only difference is the action
        final String result = xmlAdapter.execute(FIND_DEFAULT_POSITION_XML, ATTACHMENTS, USER_NAME);
        final String expected = result.replaceFirst(IZynapCommand.FIND_ACTION, IZynapCommand.EXISTS_ACTION);
        assertEquals(expected, output);
    }

    public void testExistsCommandInvalidId() throws Exception {

        // use find invalid position XML but change action to "exists" from "find"
        final String xml = FIND_INVALID_POSITION_XML.replaceFirst(IZynapCommand.FIND_ACTION, IZynapCommand.EXISTS_ACTION);
        final String output = xmlAdapter.execute(xml, ATTACHMENTS, USER_NAME);
        assertNotNull(output);
        assertTrue(output.indexOf(IZynapCommand.EXISTS_ACTION) > 0);
    }

    public void testNoAction() throws Exception {

        String[] commands = new String[]{""};
        String xml = createComplexXML(commands, 1);

        try {
            xmlAdapter.execute(xml, null, USER_NAME);
            fail("Exception expected as command had no action");
        } catch (InvalidDataException expected) {
        }
    }

    public void testInvalidNumberOfCommandInputs() throws Exception {

        String[] commands = {IZynapCommand.CREATE_ACTION};
        int numberOfElementsPerCommand = 2;
        String xml = createComplexXML(commands, numberOfElementsPerCommand);
        try {
            xmlAdapter.execute(xml, null, USER_NAME);
            fail("Exception expected as more than one input item was supplied for the command");
        } catch (InvalidDataException expected) {
        }
    }

    public void testExecuteErrors() throws Exception {
        String[] commands = {IZynapCommand.CREATE_ACTION, IZynapCommand.UPDATE_ACTION, IZynapCommand.DELETE_ACTION, IZynapCommand.FIND_ACTION};
        int numberOfElementsPerCommand = 0;
        String xml = createComplexXML(commands, numberOfElementsPerCommand);
        try {
            xmlAdapter.execute(xml, null, USER_NAME);
            fail("Exception expected as no data was supplied for commands");
        } catch (TalentStudioException expected) {
        }
    }

    public void testCreateUpdatePosition() throws Exception {
        try {
            loadAndExecute("com/zynap/talentstudio/integration/adapter/position-test-data.xml");
            String targetPositionTitle = "Manager-Employment/Recruitment/Adapter & Test";
            Position found = findPosition(targetPositionTitle);
            assertNotNull(found);
            assertEquals(3, found.getExtendedAttributes().size());
            assertEquals(1, found.getSourceAssociations().size());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testCreateUpdatePositionAssociations() throws Exception {
        try {
            loadAndExecute("com/zynap/talentstudio/integration/adapter/position-test-data.xml");
            String targetPositionTitle = "Manager-Employment/Recruitment/Adapter & Test";

            Position found = findPosition(targetPositionTitle);
            final Set<PositionAssociation> associations = found.getSourceAssociations();
            assertEquals(1, associations.size());
            
            final PositionAssociation positionAssociation = associations.iterator().next();
            assertFalse(new Long(1).equals(positionAssociation.getTarget().getId()));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testCreatePositionAssociations() throws Exception {
        try {
            loadAndExecute("com/zynap/talentstudio/integration/adapter/position-create-test-data.xml");
            String targetPositionTitle = "ManagerHead-Employment/Recruitment/Adapter & Sales";
            Position found = findPosition(targetPositionTitle);
            // todo assert the parent is not null
            assertNotNull(found.getParent());
            assertNotNull(found.getParentId());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testCreateUpdateSubjectAssociationsAndDas() throws Exception {
        try {
            loadAndExecute("com/zynap/talentstudio/integration/adapter/subject-test-data.xml");
            String targetSubjectSurname = "Xanado";
            Subject found = findSubject(targetSubjectSurname);
            assertNotNull(found);
            assertEquals(8, found.getExtendedAttributes().size());
            assertEquals(1, found.getSubjectAssociations().size());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testCreateUpdateSubjectDasAssociationsRemain() throws Exception {
        try {
            loadAndExecute("com/zynap/talentstudio/integration/adapter/subject-test-data.xml");
            String targetSubjectSurname = "Xanado";
            Subject found = findSubject(targetSubjectSurname);            
            assertEquals(1, found.getSubjectAssociations().size());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    private Subject findSubject(String targetSubjectSurname) throws Exception {
        ISubjectService subjectService = (ISubjectService) getBean("subjectService");
        final List all = subjectService.findAll();
        Subject found = null;
        for (int i = 0; i < all.size(); i++) {
            Subject subject = (Subject) all.get(i);
            if (targetSubjectSurname.equals(subject.getCoreDetail().getSecondName())) {
                found = subject;
                break;
            }
        }
        return found;
    }

    private Position findPosition(String targetPositionTitle) throws Exception {
        IPositionService positionService = (IPositionService) getBean("positionService");
        final List all = positionService.findAll();
        Position found = null;
        for (int i = 0; i < all.size(); i++) {
            Position position = (Position) all.get(i);
            if (targetPositionTitle.equals(position.getTitle())) {
                found = position;
                break;
            }
        }
        return found;
    }

    private void loadAndExecute(String filename) throws IOException {
        String xml = getXmlString(filename);
        try {
            final String result = xmlAdapter.execute(xml, null, USER_NAME);
            System.out.println("result = " + result);
        } catch (Exception e) {
            e.printStackTrace();
            fail("No exception expected");
        }
    }

    public static String getXmlString(String filename) throws IOException {
        InputStream testFileUrl = ClassLoader.getSystemResourceAsStream(filename);
        byte[] xmlBytes = new byte[25000];
        int numBytesRead = testFileUrl.read(xmlBytes);
        byte[] realBytes = new byte[numBytesRead];
        System.arraycopy(xmlBytes, 0, realBytes, 0, numBytesRead);
        return new String(realBytes);
    }

    private String createComplexXML(String[] commands, int elementCount) {
        StringBuffer x = new StringBuffer(START_ROOT_XML);
        for (int i = 0; i < commands.length; i++) {
            String command = commands[i];
            x.append("<command action=\"").append(command).append("\">");
            for (int j = 0; j < elementCount; j++) {
                x.append("<organisationUnit><label>label_").append(i).append("</label><parent><id>0</id></parent></organisationUnit>");
            }
            x.append("</command>");
        }
        x.append(END_ROOT_XML);
        return x.toString();
    }

    private IXmlAdapter xmlAdapter;


    private static final String START_ROOT_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><commands>";
    private static final String END_ROOT_XML = "</commands>";


    private static final String POS_LABEL = "Adapter Position";
    private static final String OU_LABEL = "Adapter Org Unit";

    private static final String SIMPLE_XML = START_ROOT_XML + "<command action=\"create\"><organisationUnit><id>funnyName</id><label>" + OU_LABEL + "</label><parent><id>#DEFAULT_ORGUNIT#</id></parent></organisationUnit></command></commands>";

    // private static final String SECURITY_ATTRIBUTES_XML = "<securityAttribute><publicRead>T</publicRead><managerRead>T</managerRead><individualRead>T</individualRead><managerWrite>T</managerWrite><individualWrite>T</individualWrite></securityAttribute>";
    private static final String SECURITY_ATTRIBUTES_XML = "";
    private static final String PORTFOLIO_ITEM_XML = "<portfolioItem><label>Adapter Portfolio Item</label><comments>Comments</comments><node><id>1</id></node><contentType><id>DESC</id></contentType><contentSubType>TEXT</contentSubType><blobValue ref=\"0\"/><scope>PUBLIC</scope>"+SECURITY_ATTRIBUTES_XML+"</portfolioItem>";




    private static final String POSITION_UPD_XML = "<position><id>#DEFAULT_POSITION#</id><organisationUnit><id>#DEFAULT_ORGUNIT#</id></organisationUnit><title>Adapter Position</title><competency>Competency description</competency><keyposition>YES</keyposition></position>";
    private static final String FIRST_NAME = "Pepe";

    private static final String POSITION_XML = "<position><id>posName</id><organisationUnit><id>#DEFAULT_ORGUNIT#</id></organisationUnit><title>" + POS_LABEL + "</title><competency>Competency description</competency><keyposition>YES</keyposition><portfolioItems>" + PORTFOLIO_ITEM_XML + "</portfolioItems><sourceAssociations><ptpAssociation type=\"DIRECT\"><target><id>#DEFAULT_POSITION#</id></target></ptpAssociation></sourceAssociations></position>";
    private static final String SUBJECT_XML = "<person><id>subName</id><title>Mr</title><firstName>"+FIRST_NAME+"</firstName><secondName>Pepe</secondName><highpotential>YES</highpotential><subjectAssociations><stpAssociation type=\"ACTING\"><target><id>#DEFAULT_POSITION#</id></target></stpAssociation></subjectAssociations></person>";


    private static final String CREATE_PORTFOLIO_XML = START_ROOT_XML + "<command action=\"create\">" + PORTFOLIO_ITEM_XML + "</command></commands>";


    private static final String CREATE_TWO_POSITIONS_XML = START_ROOT_XML + "<command action=\"create\">" + POSITION_XML + "</command>" +
            "<command action=\"create\"><position><id>childPos</id><organisationUnit><id>#DEFAULT_ORGUNIT#</id></organisationUnit><title>Child Position</title><competency>Competency description</competency><sourceAssociations><ptpAssociation type=\"DIRECT\"><target><id>posName</id></target></ptpAssociation></sourceAssociations></position></command>" +
            END_ROOT_XML;

    private static final String CREATE_ORG_UNITS_XML = START_ROOT_XML + "<command action=\"create\"><organisationUnit><id>1</id><label>Corporate</label><parent><id>#DEFAULT_ORGUNIT#</id></parent></organisationUnit></command>" +
            "<command action=\"create\"><organisationUnit><id>2</id><label>Finance</label><parent><id>1</id></parent></organisationUnit></command>" +
            END_ROOT_XML;

    private static final String UPDATE_POSITION_XML = START_ROOT_XML + "<command action=\"update\">" + POSITION_UPD_XML + "</command></commands>";


    private static final String CREATE_POSITION_XML = START_ROOT_XML + "<command action=\"create\">" + POSITION_XML + "</command></commands>";
    private static final String CREATE_SUBJECT_XML = START_ROOT_XML + "<command action=\"create\">" + SUBJECT_XML + "</command></commands>";



    private static final String FIND_DEFAULT_OU_XML = START_ROOT_XML + "<command action=\"find\"><organisationUnit><id>#DEFAULT_ORGUNIT#</id></organisationUnit></command></commands>";
    private static final String FIND_ALL_OU_XML = START_ROOT_XML + "<command action=\"findAll\"><organisationUnit/></command></commands>";
    private static final String FIND_DEFAULT_POSITION_XML = START_ROOT_XML + "<command action=\"find\"><position><id>#DEFAULT_POSITION#</id></position></command></commands>";
    private static final String FIND_INVALID_POSITION_XML = START_ROOT_XML + "<command action=\"find\"><position><id>-999</id></position></command></commands>";
    private static final String FIND_ALL_POSITION_XML = START_ROOT_XML + "<command action=\"findAll\"><position/></command></commands>";
    private static final String USER_NAME = "webserviceuser";
    private static final byte[][] ATTACHMENTS = new byte[][]{"Test of byte".getBytes(), "Hello World".getBytes()};
}

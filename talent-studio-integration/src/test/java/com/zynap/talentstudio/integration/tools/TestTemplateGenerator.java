package com.zynap.talentstudio.integration.tools;
/**
 * Class or Interface description.
 *
 * @author syeoh
 * @since 13-Jul-2007 11:12:48
 * @version 0.1
 */

import com.zynap.talentstudio.integration.BaseIntegrationTest;
import com.zynap.talentstudio.organisation.Node;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class TestTemplateGenerator extends BaseIntegrationTest {

    protected void setUp() throws Exception {
        super.setUp();
        templateGenerator = (ITemplateGenerator) getBean("templateGenerator");
    }

    public void testGenerate() throws Exception {

        templateGenerator.generate();

        // assert the subject file was created
        final String subjectFileName = Node.SUBJECT_UNIT_TYPE_ + TemplateGenerator.END_OF_FILENAME;
        File subjectFile = new File(subjectFileName);
        try {
            String firstLine = new BufferedReader(new FileReader(subjectFile)).readLine();
            assertNotNull(firstLine);
            assertTrue(EXPECTED_SUBJECT_HEADER.indexOf(firstLine) != -1);
        } catch (Exception e) {
            fail("Problems encountered accessing: \"" + subjectFileName + "\".\n " + e);
        }

        // assert the position file was created
        final String positionFileName = Node.POSITION_UNIT_TYPE_ + TemplateGenerator.END_OF_FILENAME;
        File positionFile = new File(positionFileName);
        try {
            String firstLine = new BufferedReader(new FileReader(positionFile)).readLine();
            assertNotNull(firstLine);
            assertTrue(EXPECTED_POSITION_HEADER.indexOf(firstLine) != -1);
        } catch (Exception e) {
            fail("Problems encountered accessing: \"" + positionFileName + "\".\n " + e);
        }

        // assert the organisations file was created
        final String organisationsFileName = Node.ORG_UNIT_TYPE_ + TemplateGenerator.END_OF_FILENAME;
        File organisationsFile = new File(organisationsFileName);
        try {
            String firstLine = new BufferedReader(new FileReader(organisationsFile)).readLine();
            assertNotNull(firstLine);
            assertEquals(firstLine, EXPECTED_ORGANISATION_HEADER);
        } catch (Exception e) {
            fail("Problems encountered accessing: \"" + organisationsFileName + "\".\n " + e);
        }
    }

    private final String EXPECTED_SUBJECT_HEADER = "id,subjectAssociations.stpAssociation.target.id,subjectAssociations.stpAssociation@type,firstName,secondName,email,telephone,dateOfBirth";
    private final String EXPECTED_POSITION_HEADER = "id,title,organisationUnit.id,sourceAssociations.ptpAssociation.target.id,sourceAssociations.ptpAssociation@type";
    private final String EXPECTED_ORGANISATION_HEADER = "id,parent.id,label";

    public void setTemplateGenerator(ITemplateGenerator templateGenerator) {
        this.templateGenerator = templateGenerator;
    }

    private ITemplateGenerator templateGenerator;
}

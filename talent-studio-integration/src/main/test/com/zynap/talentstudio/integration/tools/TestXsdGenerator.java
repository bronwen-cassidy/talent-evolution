package com.zynap.talentstudio.integration.tools;

/**
 * User: amark
 * Date: 02-Nov-2005
 * Time: 11:21:25
 */

import com.zynap.common.util.XmlUtils;
import com.zynap.talentstudio.integration.BaseIntegrationDatabaseTest;

import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.File;
import java.net.URL;
import java.net.URI;

public class TestXsdGenerator extends BaseIntegrationDatabaseTest {

    protected void setUp() throws Exception {

        super.setUp();
        xsdGenerator = (IXsdGenerator) getBean("xsdGenerator");
    }

    public void testGenerateSchema() throws Exception {

        final String output = xsdGenerator.generateSchema();

        assertNotNull(output);

        // build doc to check that XML is valid
        XmlUtils.buildDocument(output);

        // TS-2362 validate that questionnaires xsd can validate a valid sample questionnaire xml ;-)
        final String XSD_FILENAME = "xsdFile.xsd";
        final String XSD_RELATIVE_FILENAME = "com/zynap/talentstudio/integration/tools/" + XSD_FILENAME;
        final String XML_TESTFILE = "com/zynap/talentstudio/integration/tools/questionnaire-test-data.xml";

        try {

            // a slight hack to get the complete file path
            URL url = getClass().getResource("TestXsdGenerator.class");
            URI uri = new URI(url.toString());
            File file = new File(uri);

            // contruct full path for xsd file we want to generate
            final String XSD_FILEPATH = file.getParentFile().getCanonicalPath() + File.separator + XSD_FILENAME;

            // write the xsd file to disk
            BufferedWriter out = new BufferedWriter(new FileWriter(XSD_FILEPATH));
            out.write(output);
            out.close();

            // validate the test xml with the xsd file we just generated
            XmlUtils.createDocument(XML_TESTFILE, XSD_RELATIVE_FILENAME);

            boolean success = (new File(XSD_FILEPATH)).delete();
            if (success) logger.info("File : " + XSD_FILEPATH + "' has been successfully deleted.");
            else logger.error("File : " + XSD_FILEPATH + "' could not be deleted.");

        }
        catch (IOException e) {
            logger.error(e);
        }
    }

    protected String getDataSetFileName() {
        return "test-xsdgenerator-data.xml";
    }

    IXsdGenerator xsdGenerator;
}
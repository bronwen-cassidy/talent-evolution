/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.utils.build.translate;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.zynap.talentstudio.utils.build.translate.elements.Actions;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSet;
import org.apache.commons.digester.xmlrules.FromXmlRuleSet;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 16-Jun-2006 12:17:48
 */
public class ActionsFactory {

    public ActionsFactory() throws ParserConfigurationException, SAXException {

        URL rulesURL = ActionsFactory.class.getClassLoader().getResource(RULES_FILE_PATH);
        SAXParserFactory f = SAXParserFactory.newInstance();
        javax.xml.parsers.SAXParser p = f.newSAXParser();
        XMLReader xmlReader = p.getXMLReader();
        RuleSet ruleSet = new FromXmlRuleSet(rulesURL);
        digester = new Digester(p);
        digester.addRuleSet(ruleSet);

        xmlReader.setContentHandler(digester);
        xmlReader.setEntityResolver(digester);
    }

    public Actions parse(String testFilename) throws Exception {

        InputStream testFileUrl = null;
        try {
            testFileUrl = new FileInputStream(testFilename);
            byte[] xmlBytes = new byte[25000];
            int i = testFileUrl.read(xmlBytes);
            byte[] real = new byte[i];
            System.arraycopy(xmlBytes, 0, real, 0, i);

            String testDef = new String(real).trim();
            return (Actions) digester.parse(new InputSource(new StringReader(testDef)));
        } catch (Exception e) {
            throw new Exception("Failed to parse: " + testFilename + " because of: " + e.getMessage(), e);
        } finally {
            if (testFileUrl != null) {
                testFileUrl.close();
            }
        }
    }

    private final Digester digester;

    private static final String RULES_FILE_PATH = "com/zynap/talentstudio/utils/build/translate/rules.xml";
}

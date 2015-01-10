/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.integration.adapter;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.zynap.common.util.XmlUtils;
import com.zynap.talentstudio.integration.common.Command;
import com.zynap.talentstudio.integration.common.IZynapCommand;
import com.zynap.talentstudio.integration.common.IZynapCommandResult;
import com.zynap.talentstudio.integration.common.IZynapTransformer;
import com.zynap.talentstudio.integration.common.IntegrationConstants;
import com.zynap.talentstudio.integration.delegate.IZynapBusinessDelegate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Converts XML to command objects and then uses the {@link IZynapBusinessDelegate} to execute the commands.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class XmlAdapter implements IXmlAdapter {

    /**
     * Default constructor.
     */
    public XmlAdapter() {
    }

    /**
     * Executes the given data, expected format of the data is a string of xml.
     *
     * @param data a string of xml
     * @param attachments
     * @return String containing XML indicating the results of the commands (if any)
     * @throws Exception
     */
    public String execute(String data, byte[][] attachments, String username) throws Exception {
        Document document = buildDocument(new InputSource(new StringReader(data)));
        List commands = buildCommands(document, attachments);
        final List results = executeCommands(commands, username);
        return transformer.serialize(results);
    }

    /**
     * Executes the given data, expected format of the data is a string of xml.
     *
     * @param outputWriter Where to write results
     * @param inputData a Reader
     * @param attachments
     * @param username Who to execute as
     * @throws Exception
     */
    public void execute(Writer outputWriter, Reader inputData, byte[][] attachments, String username) throws Exception {
        Document document = buildDocument(new InputSource(inputData));
        List<IZynapCommand> commands = buildCommands(document, attachments);
        final List<IZynapCommandResult> results = executeCommands(commands, username);
        transformer.serialize(outputWriter, results);
    }

    /**
     * Build document from XML.
     *
     * @param data a string of xml
     * @return Document
     * @throws InvalidDataException
     */
    private Document buildDocument(InputSource data) throws InvalidDataException {
        try {
            return XmlUtils.createFilteredDocument(data, schema);
        } catch (ParserConfigurationException e) {
            throw new InvalidDataException(e);
        } catch (SAXException e) {
            throw new InvalidDataException(e);
        } catch (TransformerException e) {
            throw new InvalidDataException(e);
        }
    }

    /**
     * Extract the commands for the xml and build IZynapCommand objects.
     *
     * @param document the document to parse.
     * @param attachments
     * @return List of IZynapCommand objects
     * @throws InvalidDataException
     */
    private List<IZynapCommand> buildCommands(Document document, byte[][] attachments) throws InvalidDataException {

        List<IZynapCommand> commands = new ArrayList<IZynapCommand>();

        NodeList nodeList = document.getElementsByTagName(IntegrationConstants.COMMAND_NODE);

        for (int i = 0; i < nodeList.getLength(); i++) {
            Element node = (Element) nodeList.item(i);
            IZynapCommand nodeValue = new Command(node, attachments);
            commands.add(nodeValue);
        }

        if (commands.isEmpty()) {
            throw new InvalidDataException("Must specify at at least one command");
        }

        return commands;
    }

    /**
     * Executes the map of commands on the given IZynapBusinessDelegate.
     *
     * @throws Exception
     */
    private List<IZynapCommandResult> executeCommands(List<IZynapCommand> commands, String username) throws Exception {

        List<IZynapCommandResult> results = new ArrayList<IZynapCommandResult>();

        for (Iterator iterator = commands.iterator(); iterator.hasNext();) {
            IZynapCommand iZynapCommand = (IZynapCommand) iterator.next();
            IZynapCommandResult result;
            try {
                result = zynapBusinessDelegate.invoke(iZynapCommand, username);
            } catch (Exception e) {

                logger.error("Failed to process command:\r\n " + iZynapCommand + " \r\nbecause of:", e);
                throw e;
            }
            results.add(result);
        }

        return results;
    }

    public void setZynapBusinessDelegate(IZynapBusinessDelegate zynapBusinessDelegate) {
        this.zynapBusinessDelegate = zynapBusinessDelegate;
    }

    public void setTransformer(IZynapTransformer transformer) {
        this.transformer = transformer;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    private IZynapBusinessDelegate zynapBusinessDelegate;
    private IZynapTransformer transformer;
    private String schema;

    /**
     * Logger.
     */
    private static final Log logger = LogFactory.getLog(XmlAdapter.class);
}

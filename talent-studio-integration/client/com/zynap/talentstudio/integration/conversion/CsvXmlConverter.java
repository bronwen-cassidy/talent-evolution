/*
 * Copyright (c) 2006 Zynap Limited. All rights reserved.
 */

package com.zynap.talentstudio.integration.conversion;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.zynap.common.util.XmlUtils;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */

public class CsvXmlConverter {

    /**
     * Main program entry point
     *
     * @param argv args passed in from command line
     * @throws Exception throws exception
     */
    public static void main(String[] argv) throws Exception {

        String argPropFile = null;
        boolean displayUsage = false;

        // Process arguments
        for (int i = 0; (i < argv.length) && !displayUsage; i++) {
            String arg = argv[i];

            if (arg.startsWith("-")) {
                if (arg.equals("-help") || arg.equals("-h")) {
                    displayUsage = true;
                } else {
                    System.err.println("Invalid Argument: " + arg);
                    displayUsage = true;
                }
            } else {
                if (argPropFile != null) {
                    displayUsage = true;
                } else {
                    argPropFile = argv[i];
                }
            }
        }

        // Is there anything to do

        if (displayUsage || (argPropFile == null)) {
            printUsage();
            System.exit(1);
        }

        // Do the work

        try {
            doWork(argPropFile);
        }
        catch (Exception e) {
            System.err.println("Execution failed: " + e.getMessage());
            System.exit(2);
        }

        System.exit(0);
    }

    private static void doWork(String propertiesFile) throws Exception {
        // Loop through all the files specified in the properties file

        File propFile = new File(propertiesFile);
        String rootPath = propFile.getParent();
        Properties properties = new Properties();
        properties.load(new FileInputStream(propFile));

        Enumeration<?> enumeration = properties.propertyNames();

        while (enumeration.hasMoreElements()) {
            String objectName = (String) enumeration.nextElement();
            String fileName = properties.getProperty(objectName);

            System.out.println("Processing " + objectName + " = " + fileName);
            System.out.flush();

            Document document = XmlUtils.newDocument();
            Element commands = document.createElement("commands");
            document.appendChild(commands);

            processInput(objectName, fileName, commands, rootPath);
        }
    }

    public static void processInput(String objectName, String fileName, Element commands, String rootPath) throws Exception {
        File inputFile = new File(fileName);

        if (inputFile.getParent() == null) {
            inputFile = new File(rootPath, fileName);
        }

        System.out.println("    CSV File: " + inputFile.toString());
        System.out.flush();

        CSV doc = new CSV(inputFile);

        ArrayList headings = doc.getHeadings();
        List<ISortNode> itemsToCreate = new ArrayList<ISortNode>();
        String action = System.getProperty(ACTION_PROPERTY);
        if(!StringUtils.hasText(action)) {
            System.err.println("Must be a defined action system property of either \'create\', \'update\', \'delete\', \'find\' or \'findAll\' EXAMPLE -Daction=update");
            System.exit(-1);    
        }
        if (!(CREATE_ACTION.equals(action) || UPDATE_ACTION.equals(action) || DELETE_ACTION.equals(action) || FIND_ACTION.equals(action) || FIND_ALL_ACTION.equals(action))) {
            System.err.println("Must be a defined action system property of either \'create\', \'update\', \'delete\', \'find\' or \'findAll\' EXAMPLE -Daction=update");
            System.exit(-1);
        }
        while (doc.readNextRow()) {
            //cycle through columns
            Record record = new Record(doc, headings, objectName, commands.getOwnerDocument(), action);
            itemsToCreate.add(record);
        }

        List<? extends ISortNode> sortedItems = SortUtil.sortByTreeLevel(itemsToCreate);

        for (Object sortedItem : sortedItems) {
            Record record = (Record) sortedItem;
            try {
                //cycle through columns
                commands.appendChild(record.getElement());
            }
            catch (Exception e) {
                System.err.println("Error processing line: " + doc.getLineCount());
                throw e;
            }
        }

        // Write document to output file
        String outputPath = inputFile.getParent();

        if (outputPath == null) {
            outputPath = rootPath;
        }
        if (outputPath == null) {
            outputPath = "";
        }
        if (outputPath.length() > 1) {
            outputPath += File.separator;
        }

        File outputFile = new File(outputPath + objectName + ".xml");
        final FileWriter fileWriter = new FileWriter(outputFile);

        OutputFormat outputFormat = new OutputFormat(commands.getOwnerDocument(), "UTF-8", true);
        XMLSerializer serializer = new XMLSerializer(fileWriter, outputFormat);
        serializer.serialize(commands.getOwnerDocument());

        fileWriter.close();
        doc.Close();
    }


    private static void printUsage() {
        System.err.println();
        System.err.println("usage: CsvToXml propertiesFile");
        System.err.println();

        System.err.println("options:");
        System.err.println("  -help                 This help display.");
        System.err.println();
    }

    private static final String CREATE_ACTION = "create";
    private static final String UPDATE_ACTION = "update";
    private static final String DELETE_ACTION = "delete";
    private static final String FIND_ACTION = "find";
    private static final String FIND_ALL_ACTION = "findAll";
    
    private static final String ACTION_PROPERTY = "action";
}

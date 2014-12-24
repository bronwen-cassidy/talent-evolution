/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.integration.client;

import com.zynap.talentstudio.integration.tools.IXsdGenerator;

import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Main class for java integration clients - suitable for local instances only.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */

public class GetXsd extends IntegrationMain {

    /**
     * Main program entry point
     * @param argv args passed in from command line
     * @throws Exception generic exception thrown
     */
    public static void main(String[] argv) throws Exception {
        String argXsdFile = processArguments(argv);

        // Do the work

        try {
            loadApp();
            generateSchema(argXsdFile);
            shutdown();
        } catch (Exception e) {
            System.err.println("Execution failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(2);
        }

        System.exit(0);
    }

    private static void generateSchema(String xsdFile) throws Exception {

        IXsdGenerator xsdGenerator = (IXsdGenerator) getBean("xsdGenerator");

        if (xsdFile == null) {
            PrintWriter pw = new PrintWriter(System.out);
            xsdGenerator.generateSchema(pw);
            pw.close();
        } else {
            FileWriter fileWriter = null;

            try {
                fileWriter = new FileWriter(xsdFile);
                xsdGenerator.generateSchema(fileWriter);
                fileWriter.flush();
            } finally {
                if (fileWriter != null) {
                    fileWriter.close();
                }
            }
        }
    }

}

/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.integration.client;

import com.zynap.talentstudio.integration.adapter.IXmlAdapter;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Main class for java integration clients - suitable for local instances only.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */

public class ExecuteXml extends IntegrationMain {

    /**
     * Main program entry point
     * @param argv
     * @throws Exception
     */
    public static void main(String[] argv) throws Exception {

        String argXmlFile = null;
        String argDocsFile = null;
        String tsUserName = "webserviceuser";
        String dbUser = null;
        String dbPasswd = null;
        boolean displayUsage = false;

        // Process arguments
        for (int i = 0; (i < argv.length) && !displayUsage; i++) {
            String arg = argv[i];
            if (arg.equals("-help") || arg.equals("-h")) {
                displayUsage = true;
            } else if (arg.startsWith("-")) {
                if (++i < argv.length) {
                    if (arg.equals("-uploads")) {
                        argDocsFile = argv[i];
                    } else if (arg.equals("-host")) {
                        setDBHost(argv[i]);
                    } else if (arg.equals("-sid")) {
                        setDBSid(argv[i]);
                    } else if (arg.equals("-dbuser")) {
                        System.out.println(">>>>>>>>>>>>>>> dbuser=: " + argv[i]);
                        dbUser = argv[i];
                    } else if (arg.equals("-dbpasswd")) {
                        System.out.println(">>>>>>>>>>>>>>> dbpasswd=: " + argv[i]);
                        dbPasswd = argv[i];
                    } else if (arg.equals("-tsuser")) {
                        System.out.println(">>>>>>>>>>>>>>> ts user=: " + argv[i]);
                        tsUserName = argv[i];
                    } else {
                        System.err.println("Invalid Argument: " + arg);
                        displayUsage = true;
                    }
                } else {
                    displayUsage = true;
                }
            } else {
                if (argXmlFile != null) {
                    displayUsage = true;
                } else {
                    argXmlFile = argv[i];
                }
            }
        }

        // Check for missing arguments
        displayUsage = checkMissingArgs(dbUser, dbPasswd);
        if (displayUsage || (argXmlFile == null)) {
            printUsage();
            System.exit(1);
        }

        // Do the work

        try {
            doWork(argXmlFile, argDocsFile, tsUserName);
        } catch (Exception e) {
            System.err.println("Execution failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(2);
        }

        System.exit(0);
    }

    private static void doWork(String xmlFile, String docsFile, String tsUserName) throws Exception {
        byte[][] attachments = null;

        // Open input file - throws exception if it does not exist.

        FileReader input = new FileReader(xmlFile);

        // Load the documents, if there are any

        if (docsFile != null) {
            ArrayList documents = new ArrayList();
            String line;
            BufferedReader br;

            br = new BufferedReader(new FileReader(new File(docsFile)));

            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();

                if (trimmed.length() != 0) {
                    System.out.println("Reading file '" + line + "'");
                    documents.add(readBytes(new File(trimmed)));
                }
            }

            br.close();

            attachments = (byte[][]) documents.toArray(new byte[1][]);
        }

        // Execute XML

        loadApp();
        execute(input, attachments, tsUserName);
        shutdown();
    }

    private static byte[] readBytes(File f) throws IOException {
        FileInputStream reader = new FileInputStream(f);
        BufferedInputStream bufferedReader = new BufferedInputStream(reader);
        final int fileLength = new Long(f.length()).intValue();
        byte[] bytes = new byte[fileLength];
        final int bytesRead = bufferedReader.read(bytes);

        if (fileLength != bytesRead) {
            byte[] read = new byte[bytesRead];
            System.arraycopy(bytes, 0, read, 0, bytesRead);
            bytes = read;
        }
        bufferedReader.close();
        return bytes;
    }

    private static void execute(FileReader inputReader, byte[][] attachments, String username)
            throws Exception {
        IXmlAdapter xmlAdapter = (IXmlAdapter) getBean("xmlAdapter");
        xmlAdapter.execute(new PrintWriter(System.out), inputReader, attachments, username);
    }

    protected static void printUsage() {
        System.err.println();
        System.err.println("usage: ExecuteXml -dbuser <db user name> [(options)] <xmlFile>");
        System.err.println();

        System.err.println("options:");
        System.err.println("  -uploads <docs file>  File of document paths.");
        System.err.println("  -host                 Database host name. Defaults to \"localhost\"");
        System.err.println("  -sid                  Database sid. Defaults to \"OraDB\"");
        System.err.println("  -dbuser               Database user/schema name.");
        System.err.println("  -dbpasswd             Database password name. Defaults to db user name.");
        System.err.println("  -tsuser               TalentStudio user name.");
        System.err.println("  -help                 This help display.");
        System.err.println();
    }
}

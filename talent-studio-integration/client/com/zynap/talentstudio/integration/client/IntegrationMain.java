/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.integration.client;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Abstract Main class for java integration clients - suitable for local instances only.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */

abstract class IntegrationMain {
    
    protected static String processArguments(String[] argv) {
        String argXsdFile = null;
        String dbUser = null;
        String dbPasswd = null;
        boolean displayUsage = false;

        // Process arguments

        for (int i = 0; (i < argv.length) && !displayUsage; i++) {
            String arg = argv[i];

            if (arg.startsWith("-")) {
                if (arg.equals("-host")) {
                    if (++i < argv.length) {
                        setDBHost(argv[i]);
                    } else {
                        displayUsage = true;
                    }
                } else if (arg.equals("-sid")) {
                    if (++i < argv.length) {
                        setDBSid(argv[i]);
                    } else {
                        displayUsage = true;
                    }
                } else if (arg.equals("-dbuser")) {
                    if (++i < argv.length) {
                        dbUser = argv[i];
                    } else {
                        displayUsage = true;
                    }
                } else if (arg.equals("-dbpasswd")) {
                    if (++i < argv.length) {
                        dbPasswd = argv[i];
                    } else {
                        displayUsage = true;
                    }
                } else if (arg.equals("-help") || arg.equals("-h")) {
                    displayUsage = true;
                } 
            } else {
                if (argXsdFile != null) {
                    displayUsage = true;
                } else {
                    argXsdFile = argv[i];
                }
            }
        }

        // Check for missing arguments
        displayUsage = checkMissingArgs(dbUser, dbPasswd);
        if (displayUsage || (argXsdFile == null)) {
            printUsage();
            System.exit(1);
        }
        return argXsdFile;
    }

    protected static void printUsage() {
        System.err.println();
        System.err.println("usage: GetXsd -dbuser <username> [(options)] <xsdFile>");
        System.err.println();

        System.err.println("options:");
        System.err.println("  -host                 Database host name. Defaults to \"localhost\"");
        System.err.println("  -sid                  Database sid. Defaults to \"OraDB\"");
        System.err.println("  -dbuser               Database user/schema name.");
        System.err.println("  -dbpasswd             Database password name. Defaults to db user name.");
        System.err.println("  -help                 This help display.");
        System.err.println();
    }

    private static ClassPathXmlApplicationContext applicationContext = null;

    private final static String[] configLocations =
            {
                "config/spring/testApplicationContext-jdbc.xml",
                "config/spring/applicationContext-hibernate.xml",
                "config/spring/applicationContext-tx.xml",
                "config/spring/applicationContext.xml",
                "config/spring/testApplicationContext-mail.xml",
                "config/integrationContext.xml"
            };

    public static String dbSid = "OraDB";
    public static String dbHost = "localhost";

    protected static void loadApp() {
        //System.setProperty("test.db.url", "jdbc:oracle:thin:@" + dbHost + ":1521:" + dbSid);
        System.setProperty("autonomy.aciport", "7009");
        System.setProperty("autonomy.indexport", "7008");
        System.setProperty("autonomy.retries", "0");
        System.setProperty("autonomy.connection.timeout", "0");
        System.setProperty("autonomy.position.database", "positions");
        System.setProperty("autonomy.subject.database", "subjects");
        System.setProperty("autonomy.searcher", "mockAutonomySearcher");
        System.setProperty("sender.email", "zynap@zynap.com");
        System.setProperty("server.url", "https://zynaphosting.com");
        System.setProperty("search.gateway", "autonomyGateway");
        System.setProperty("result.mapper", "xmlMapper");

        System.out.println(">>>>>>>>> System.getProperty(\"test.db.url\") = " + System.getProperty("test.db.url"));

        // Start application

        applicationContext = new ClassPathXmlApplicationContext(configLocations);
        applicationContext.refresh();
    }

    protected static boolean checkMissingArgs(String dbUser, String dbPasswd) {
        if (dbUser == null) {
            return true;
        } else {
            setDBUser(dbUser);
            setDBPasswd(dbPasswd == null ? dbUser : dbPasswd);
        }
        return false;
    }

    protected static Object getBean(String name) {
        if (applicationContext == null) {
            loadApp();
        }

        return applicationContext.getBean(name);
    }

    protected static void shutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                applicationContext.close();
            }
        });
    }

    protected static void setDBUser(String dbUser) {
        System.setProperty("test.db.username", dbUser);
    }

    protected static void setDBPasswd(String dbPasswd) {
        System.setProperty("test.db.password", dbPasswd);
    }

    protected static void setDBSid(String sid) {
        dbSid = sid;
    }

    protected static void setDBHost(String host) {
        dbHost = host;
    }
}

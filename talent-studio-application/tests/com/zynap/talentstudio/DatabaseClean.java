/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;

import java.io.File;

/**
 * User: bcassidy
 * Date: 25-Nov-2005
 * Time: 14:19:54
 */
public class DatabaseClean {

    public static void main(String[] args) throws Exception {

        if (args == null || args.length != 4) {
            printUsage();
            System.exit(-1);
        }

        final String url = args[0];
        final String username = args[1];
        final String password = args[2];
        final String fileName = args[3];

        IDatabaseConnection connection = DBUnitUtils.getConnection("oracle.jdbc.driver.OracleDriver", url, username, password);
        DBUnitUtils.executeSetUp(connection, new FlatXmlDataSet(new File(fileName)), DatabaseOperation.DELETE);

        System.out.println("Successfully deleted contents of: " + fileName + " from: " + url + " using username: " + username + " and password: " + password);
    }

    private static void printUsage() {
        System.out.println();
        System.err.println("usage: DatabaseClean <host> <username> <password> <fileName>");
        System.err.println();
    }
}

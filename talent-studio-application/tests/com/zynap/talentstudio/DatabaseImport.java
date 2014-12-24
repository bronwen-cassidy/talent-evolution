/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;

import java.io.File;

/**
 * User: bcassidy
 * Date: 25-Nov-2005
 * Time: 14:19:54
 */
public class DatabaseImport {

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
        final FlatXmlDataSet xmlDataSet = new FlatXmlDataSet(new File(fileName));

        final ReplacementDataSet dataSet = new ReplacementDataSet(xmlDataSet);
        dataSet.addReplacementObject(DBUnitUtils.NULL_VALUE, null);

        DBUnitUtils.executeSetUp(connection, dataSet, DatabaseOperation.REFRESH);

        System.out.println("Successfully imported contents of: " + fileName + " into: " + url + " using username: " + username + " and password: " + password);
    }

    private static void printUsage() {
        System.out.println();
        System.err.println("usage: DatabaseImport <host> <username> <password> <fileName>");
        System.err.println();
    }
}

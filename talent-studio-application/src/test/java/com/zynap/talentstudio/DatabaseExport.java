package com.zynap.talentstudio;

import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * User: amark
 * Date: 25-Nov-2005
 * Time: 14:19:54
 */
public class DatabaseExport {

    public static void main(String[] args) throws Exception {

        if (args == null || args.length != 5) {
            printUsage();
            System.exit(-1);
        }

        // load connection details
        final String url = args[0];
        final String username = args[1];
        final String password = args[2];
        // establish connection and create dataset
        final IDatabaseConnection connection = DBUnitUtils.getConnection("oracle.jdbc.OracleDriver", url, username, password);
        final QueryDataSet databaseDataset = new QueryDataSet(connection);

        // load tables and queries from properties file
        final String tableFileName = args[4];
        final File file = new File(tableFileName);
        Properties properties = new Properties();
        properties.load(new FileInputStream(file));

        // iterate properties and add to list of tables
        final Set set = properties.entrySet();
        for (Iterator iterator = set.iterator(); iterator.hasNext();) {
            Map.Entry entry = (Map.Entry) iterator.next();
            final String tableName = entry.getKey().toString().toUpperCase();
            final String query = (String) entry.getValue();

            if (query == null || query.length() == 0) {
               databaseDataset.addTable(tableName);
            } else {
               databaseDataset.addTable(tableName, query);
            }
        }

        if (databaseDataset.getTableNames().length == 0) {
            System.err.println("You must specify at least one table to export data from");
            System.exit(-1);
        }

        System.out.println("Data export started from: " + url + " using username: " + username + " and password: " + password );

        final String fileName = args[3];
        ITableFilter filter = new DatabaseSequenceFilter(connection, databaseDataset.getTableNames());
        IDataSet filteredDataSet = new FilteredDataSet(filter, databaseDataset);
        ReplacementDataSet dataSet = new ReplacementDataSet(filteredDataSet);
        dataSet.addReplacementObject(null, DBUnitUtils.NULL_VALUE);

        FlatXmlDataSet.write(dataSet, new FileOutputStream(fileName));

        String[] actualFiltered = filteredDataSet.getTableNames();
        System.out.println("Successfully exported content of tables: " + Arrays.asList(actualFiltered)+ " to file: " + fileName);
    }


    private static void printUsage() {
        System.out.println();
        System.err.println("usage: DatabaseExport <host> <username> <password> <fileName> <tableFileName>");
        System.err.println();
    }
}

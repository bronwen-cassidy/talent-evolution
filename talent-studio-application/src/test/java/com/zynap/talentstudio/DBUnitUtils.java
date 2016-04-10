package com.zynap.talentstudio;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.filter.IColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.ext.oracle.OracleDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;

import javax.sql.DataSource;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: amark
 * Date: 01-Dec-2005
 * Time: 14:00:07
 */
public class DBUnitUtils {

    private DBUnitUtils() {
    }

    public static IDatabaseConnection getConnection(final String className, final String url, final String user, final String password) throws Exception {
        Class.forName(className);
        Connection jdbcConnection = DriverManager.getConnection(url, user, password);
        return getDatabaseConnection(jdbcConnection, user);
    }

    public static IDatabaseConnection getConnection(final DataSource dataSource) throws Exception {
        String schema = null;
        try {
            dataSource.getConnection().getSchema();
        } catch (Throwable t) {
            // ignored we are dealing with the pool
        }
        return getDatabaseConnection(dataSource.getConnection(), schema);
    }

    public static IDataSet getDataSetFromXMLFile(final URL url) throws Exception {
        return new FlatXmlDataSet(url);
    }

    public static void executeSetUp(DataSource dataSource, URL url, DatabaseOperation setUpOperation) throws Exception {

        IDatabaseConnection connection = getConnection(dataSource);
        IDataSet dataSet = getDataSetFromXMLFile(url);
        executeSetUp(connection, dataSet, setUpOperation);
    }

    public static void executeSetUp(IDatabaseConnection connection, IDataSet dataSet, DatabaseOperation setUpOperation) throws Exception {

        final ReplacementDataSet replacementDataSet = new ReplacementDataSet(dataSet);
        replacementDataSet.addReplacementObject(NULL_VALUE, null);

        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        replacementDataSet.addReplacementObject(CURRENT_DATE, calendar.getTime());
        setUpOperation.execute(connection, replacementDataSet);
    }

    public static void executeTearDown(DataSource dataSource, URL url, DatabaseOperation tearDownOperation) throws Exception {

        IDatabaseConnection connection = getConnection(dataSource);
        IDataSet dataSet = getDataSetFromXMLFile(url);
        executeTearDown(connection, dataSet, tearDownOperation);
    }

    public static void executeTearDown(IDatabaseConnection connection, IDataSet dataSet, DatabaseOperation tearDownOperation) throws Exception {
        try {
            tearDownOperation.execute(connection, dataSet);
        } finally {
            connection.close();
        }
    }

    private static IDatabaseConnection getDatabaseConnection(Connection connection, String schema) {

        IDatabaseConnection databaseConnection = null;
        DatabaseConfig config;
        try {
            if (schema != null) {
                databaseConnection = new DatabaseConnection(connection, schema);
                config = databaseConnection.getConfig();
                config.setFeature(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, Boolean.TRUE);
            } else {
                databaseConnection = new DatabaseConnection(connection);
                config = databaseConnection.getConfig();
            }
            config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new OracleDataTypeFactory());
            config.setProperty(DatabaseConfig.PROPERTY_PRIMARY_KEY_FILTER, new ZynapColumnFilter());
        } catch (DatabaseUnitException e) {
            e.printStackTrace();
        }
        return databaseConnection;
    }

    public static final String NULL_VALUE = "[NULL]";
    public static final String CURRENT_DATE = "[CURRENT_DATE]";

    private static class ZynapColumnFilter implements IColumnFilter {

        Map<String, List<String>> tablePrimaryKeyMap = new HashMap<String, List<String>>();

        {
            tablePrimaryKeyMap.put("USER_NODE_DOMAIN_PERMITS", Arrays.asList("USER_ID", "NODE_ID", "PERMIT_ID"));
            tablePrimaryKeyMap.put("APP_ROLES_USERS", Arrays.asList("USER_ID", "ROLE_ID"));
            tablePrimaryKeyMap.put("QUE_WF_PARTICIPANTS", Arrays.asList("SUBJECT_ID", "QUE_WF_ID"));
            tablePrimaryKeyMap.put("LOGINS", Arrays.asList("USER_ID"));
            tablePrimaryKeyMap.put("QUE_DEFINITION_MODELS", Arrays.asList("QUE_DEF_ID"));
        }

        public boolean accept(String tableName, Column column) {
            if (tablePrimaryKeyMap.containsKey(tableName))
                return tablePrimaryKeyMap.get(tableName).contains(column.getColumnName());

            return column.getColumnName().equalsIgnoreCase("id") || column.getColumnName().equalsIgnoreCase("node_id");
        }
    }
}

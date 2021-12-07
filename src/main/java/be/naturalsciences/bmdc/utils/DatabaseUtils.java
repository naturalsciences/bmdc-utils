/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;

/**
 *
 * @author thomas
 */
public class DatabaseUtils {

    public static Connection getPostgresConnection(String jdbcString, String user, String password) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, "An exception occured.", ex);
            return null;
        }
        String url = jdbcString;
        Properties props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", password);
        try {
            return DriverManager.getConnection(url, props);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, "An exception occured.", ex);
            return null;
        }
    }

    public static OracleConnection getOracleConnection(String databaseName, String server, String user, String password) {
        try {
            OracleDataSource ods = new OracleDataSource();
            ods.setDriverType("thin");
            ods.setDatabaseName(databaseName);
            ods.setServerName(server);
            ods.setPortNumber(1521);
            ods.setUser(user);
            ods.setPassword(password);

            return (OracleConnection) ods.getConnection();

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, "An exception occured.", ex);
            return null;
        }
    }
}

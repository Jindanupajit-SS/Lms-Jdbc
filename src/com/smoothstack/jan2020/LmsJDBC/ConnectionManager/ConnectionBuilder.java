package com.smoothstack.jan2020.LmsJDBC.ConnectionManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionBuilder implements DatabaseConnection {

    private static Properties defaultProperties = null;
    private String driver;
    private String url;
    private String username;
    private String password;

    public ConnectionBuilder() {
        // if defaultProperties was set, use the defaultProperties
        this(defaultProperties==null?new Properties():defaultProperties);
    }

    // Use userDefined properties
    public ConnectionBuilder(Properties connectionProperties) {
        driver = connectionProperties.getProperty("driver","com.mysql.cj.jdbc.Driver");
        url = connectionProperties.getProperty("url", "jdbc:mysql://localhost:3306/library?useSSL=false");
        username = connectionProperties.getProperty("username", "root");
        password = connectionProperties.getProperty("password", "root");
    }

    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        Connection connection = DriverManager.getConnection(url, username, password);
        connection.setAutoCommit(Boolean.FALSE);
        return connection;
    }

    public static Properties setDefaultProperties(Properties properties) {
        return ConnectionBuilder.defaultProperties = properties;
    }

}

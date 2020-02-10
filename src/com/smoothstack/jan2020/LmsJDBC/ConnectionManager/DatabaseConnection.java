package com.smoothstack.jan2020.LmsJDBC.ConnectionManager;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseConnection {

    Connection getConnection() throws ClassNotFoundException, SQLException;


}

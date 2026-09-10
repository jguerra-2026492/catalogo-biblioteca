package com.jguerrakinal.abarroteriakinal.config;

import com.jguerrakinal.abarroteriakinal.config.Credentials;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {
    
private static Connection conn;

public static Connection getConnection() throws SQLException{
    
    if (conn == null || conn.isClosed()){
     
        conn = DriverManager.getConnection(Credentials.URL_DB, Credentials.USER_DB, Credentials.PASS_DB);        
    }
    return conn;
}
    
}

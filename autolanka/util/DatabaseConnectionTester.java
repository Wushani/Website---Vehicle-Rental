package org.web.autolanka.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DatabaseConnectionTester {

    @Autowired
    private DataSource dataSource;

    public boolean testConnection() {
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("=====================================");
            System.out.println("DATABASE CONNECTION TEST");
            System.out.println("=====================================");
            System.out.println("Database URL: " + connection.getMetaData().getURL());
            System.out.println("Database Product: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("Database Version: " + connection.getMetaData().getDatabaseProductVersion());
            System.out.println("Driver Name: " + connection.getMetaData().getDriverName());
            System.out.println("Driver Version: " + connection.getMetaData().getDriverVersion());
            System.out.println("Connection Successful!");
            System.out.println("=====================================");
            return true;
        } catch (SQLException e) {
            System.err.println("=====================================");
            System.err.println("DATABASE CONNECTION FAILED");
            System.err.println("=====================================");
            System.err.println("Error: " + e.getMessage());
            System.err.println("=====================================");
            return false;
        }
    }
}
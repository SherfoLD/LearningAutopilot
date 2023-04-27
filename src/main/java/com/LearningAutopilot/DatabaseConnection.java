package com.LearningAutopilot;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static DatabaseConnection databaseConnection;
    @Getter
    private static Connection dbConnection;
    private String databaseHostName;
    private int databasePort;
    private String databaseName;

    public static DatabaseConnection getInstance() {
        if (databaseConnection == null) {
            databaseConnection = new DatabaseConnection();
        }
        return databaseConnection;
    }

    private DatabaseConnection() {}

    public void initializeConnection(String databaseUserLogin, String databaseUserPassword) throws SQLException {
        databaseHostName = DatabaseConfig.getInstance().getDatabaseHostName();
        databasePort = DatabaseConfig.getInstance().getDatabasePort();
        databaseName = DatabaseConfig.getInstance().getDatabaseName();

        String url = "jdbc:postgresql://";
        Properties dbConnectionProps = new Properties();
        dbConnectionProps.setProperty("host", databaseHostName);
        dbConnectionProps.setProperty("port", String.valueOf(databasePort));
        dbConnectionProps.setProperty("database", databaseName);
        dbConnectionProps.setProperty("user", databaseUserLogin);
        dbConnectionProps.setProperty("password", databaseUserPassword);

        dbConnection = DriverManager.getConnection(url, dbConnectionProps);
    }
}

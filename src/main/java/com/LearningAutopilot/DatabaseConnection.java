package com.LearningAutopilot;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static DatabaseConnection databaseConnection;
    @Getter
    private Connection dbConnection;

    public static DatabaseConnection getInstance() {
        if (databaseConnection == null) {
            databaseConnection = new DatabaseConnection();
        }
        return databaseConnection;
    }

    private DatabaseConnection() {
    }

    public void initializeConnection(String databaseUserLogin, String databaseUserPassword) throws SQLException {
        String databaseHostName = DatabaseConfig.getInstance().getDatabaseHostName();
        int databasePort = DatabaseConfig.getInstance().getDatabasePort();
        String databaseName = DatabaseConfig.getInstance().getDatabaseName();

        String url = "jdbc:postgresql://" + databaseHostName + ":" + databasePort + "/" + databaseName;

        Properties dbConnectionProps = new Properties();
        dbConnectionProps.setProperty("user", databaseUserLogin);
        dbConnectionProps.setProperty("password", databaseUserPassword);

        dbConnection = DriverManager.getConnection(url, dbConnectionProps);
    }

    public void closeConnection() throws SQLException {
        dbConnection.close();
    }
}

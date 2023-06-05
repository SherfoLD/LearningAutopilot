package com.LearningAutopilot;

import com.LearningAutopilot.Exceptions.SQLExceptionMessageWrapper;

import lombok.Getter;
import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConnection {
    private static DatabaseConnection databaseConnection;
    @Getter
    private Connection dbConnection;
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);

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
        //dbConnection = DriverManager.getConnection("jdbc:postgresql://localhost/LearningAutopilotDB?user=ViewUser&password=secret");
        logger.info("Successful connection to: " + url);
    }

    public void closeConnection() {
        try {
            dbConnection.close();
        } catch (SQLException ex) {
            logger.error("SQL State: " + ex.getSQLState() + " Message: " + ex.getMessage());
            JOptionPane.showMessageDialog(Main.mainFrame,
                    SQLExceptionMessageWrapper.getWrappedSQLStateMessage(ex.getSQLState(), ex.getMessage()),
                    "Ошибка закрытия соединения",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}

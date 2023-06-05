package com.LearningAutopilot.UI.Dialogs;

import com.LearningAutopilot.DatabaseConnection;
import com.LearningAutopilot.Exceptions.SQLExceptionMessageWrapper;
import com.LearningAutopilot.Main;
import com.LearningAutopilot.SQLHelper.ITableSQLHelper;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class RecordDeleteDialog extends JDialog {
    private JPanel contentPane;
    private JScrollPane scrollPane;
    private JLabel userConfirmationLabel;
    private JButton userConfirmationNoButton;
    private JButton userConfirmationYesButton;
    private final ITableSQLHelper tableSQLHelper;
    private final String record_ID;
    private static final Logger logger = LoggerFactory.getLogger(RecordDeleteDialog.class);

    public RecordDeleteDialog(ITableSQLHelper tableSQLHelper, String record_ID) {
        super(Main.mainFrame, "Удаление записи");
        this.tableSQLHelper = tableSQLHelper;
        this.record_ID = record_ID;

        setContentPane(contentPane);
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        userConfirmationNoButton.addActionListener(e -> dispose());
        userConfirmationYesButton.addActionListener(e -> {
            try {
                executeDelete();
            } catch (SQLException ex) {
                logger.error("SQL State: " + ex.getSQLState() + " Message: " + ex.getMessage());
                JOptionPane.showMessageDialog(Main.mainFrame,
                        SQLExceptionMessageWrapper.getWrappedSQLStateMessage(ex.getSQLState(), ex.getMessage()),
                        "Ошибка удаления записи",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                dispose();
            }
        });
    }

    private void executeDelete() throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getDbConnection();
        Statement stmt = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);

        String deleteProcedure = tableSQLHelper.getDeleteProcedure() + "('" + record_ID + "')";
        stmt.executeUpdate(deleteProcedure);

        logger.info("Query executed: " + deleteProcedure);
    }
}

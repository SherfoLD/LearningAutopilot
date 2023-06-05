package com.LearningAutopilot.UI.Dialogs;

import com.LearningAutopilot.DatabaseConnection;
import com.LearningAutopilot.Exceptions.SQLExceptionMessageWrapper;
import com.LearningAutopilot.Main;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateBalanceCostDialog extends JDialog {
    private JPanel contentPane;
    private JLabel userConfirmationLabel;
    private JButton userConfirmationYesButton;
    private JButton userConfirmationNoButton;
    private JScrollPane scrollPane;
    private static final Logger logger = LoggerFactory.getLogger(UpdateBalanceCostDialog.class);

    public UpdateBalanceCostDialog() {
        super(Main.mainFrame, "Удаление записи");

        setContentPane(contentPane);
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        userConfirmationNoButton.addActionListener(e -> dispose());
        userConfirmationYesButton.addActionListener(e -> {
            try {
                updateBalanceCost();
            } catch (SQLException ex) {
                logger.error("SQL State: " + ex.getSQLState() + " Message: " + ex.getMessage());
                JOptionPane.showMessageDialog(Main.mainFrame,
                        SQLExceptionMessageWrapper.getWrappedSQLStateMessage(ex.getSQLState(), ex.getMessage()),
                        "Ошибка обновления стоимостей",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                dispose();
            }
        });
    }

    private void updateBalanceCost() throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getDbConnection();
        Statement stmt = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);

        String finalProcedureQuery = "CALL \"Update_Balance_Cost\"()";
        stmt.executeUpdate(finalProcedureQuery);

        logger.info("Query executed: " + finalProcedureQuery);
    }
}

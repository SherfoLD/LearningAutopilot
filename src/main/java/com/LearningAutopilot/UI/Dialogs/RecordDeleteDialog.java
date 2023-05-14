package com.LearningAutopilot.UI.Dialogs;

import com.LearningAutopilot.DatabaseConnection;
import com.LearningAutopilot.Main;
import com.LearningAutopilot.SQLHelper.ITableSQLHelper;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RecordDeleteDialog extends JDialog {
    private JPanel contentPane;
    private JScrollPane scrollPane;
    private JLabel userConfirmationLabel;
    private JButton userConfirmationNoButton;
    private JButton userConfirmationYesButton;
    private final ITableSQLHelper tableSQLHelper;
    private final String record_ID;

    public RecordDeleteDialog(ITableSQLHelper tableSQLHelper, String record_ID) {
        super(Main.mainFrame, "Удаление записи");
        this.tableSQLHelper = tableSQLHelper;
        this.record_ID = record_ID;

        setContentPane(contentPane);
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        userConfirmationNoButton.addActionListener(e -> dispose());
        userConfirmationYesButton.addActionListener(e -> executeDelete());
    }

    private void executeDelete() {
        try {
            Connection conn = DatabaseConnection.getInstance().getDbConnection();
            Statement stmt = conn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            String deleteProcedure = tableSQLHelper.getDeleteProcedure() + "('" + record_ID + "')";
            stmt.executeUpdate(deleteProcedure);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(Main.mainFrame,
                    e.getMessage(),
                    "Ошибка удаления записи",
                    JOptionPane.ERROR_MESSAGE);
        }
        dispose();
    }


    public static void main(String[] args) {
       /* RecordDeleteDialog dialog = new RecordDeleteDialog(new EquipmentCategoriesSQLHelper(), "3869a5e2-9dec-4d44-91d3-cdadf5253401");
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);*/
    }
}

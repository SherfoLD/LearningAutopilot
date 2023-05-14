package com.LearningAutopilot.UI.Forms;

import com.LearningAutopilot.DatabaseConnection;
import com.LearningAutopilot.Main;
import com.LearningAutopilot.SQLHelper.EquipmentCategoriesSQLHelper;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseInteractionForm {
    @Getter
    private JPanel mainPanel;
    private JScrollPane scrollPane;
    private JPanel availableDatabasesJPanel;
    private JPanel componentsJPanel;

    public DatabaseInteractionForm() {
        initAvailableDatabasesJPanel();

        ArrayList<String> databaseTablesNames;
        try {
            databaseTablesNames = getDatabaseTablesNames();
            for (int i = 0; i < databaseTablesNames.size(); i++) {
                String tableName = databaseTablesNames.get(i);
                createTableButton(tableName, i);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createTableButton(String tableName, int tableNumber) {
        JButton tableButton = new JButton(tableName);

        tableButton.setFont(new Font(null, Font.PLAIN, 16));
        tableButton.setPreferredSize(new Dimension(-1, 40));
        tableButton.addActionListener(e -> goToTableInteractionForm(tableName));

        availableDatabasesJPanel.add(tableButton, new GridConstraints(tableNumber, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    private void goToTableInteractionForm(String tableName) {
        try {
            if (tableName.equals("EquipmentCategories")){
                EquipmentCategoriesSQLHelper tableSQLHelper = new EquipmentCategoriesSQLHelper();
                TableInteractionForm tableInteractionForm = new TableInteractionForm(tableSQLHelper);

                Main.mainFrame.getContentPane().removeAll();
                Main.mainFrame.add(tableInteractionForm.getMainPanel());
                Main.mainFrame.setVisible(true);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(Main.mainFrame,
                    "Невозможно создать таблицу",
                    "Ошибка обращения к таблице",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private ArrayList<String> getDatabaseTablesNames() throws SQLException {
        ArrayList<String> databaseTablesNames = new ArrayList<>();

        Connection connection = DatabaseConnection.getInstance().getDbConnection();
        DatabaseMetaData dbMetaData = connection.getMetaData();
        ResultSet databaseTables = dbMetaData.getTables(null, null, "%", new String[]{"TABLE"});

        while (databaseTables.next()) {
            databaseTablesNames.add(databaseTables.getString("TABLE_NAME"));
        }
        return databaseTablesNames;
    }

    private void initAvailableDatabasesJPanel() {
        int rowCount = 0;
        try {
            rowCount = getDatabaseTablesNames().size();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        availableDatabasesJPanel.setLayout(new GridLayoutManager(rowCount, 1, new Insets(0, 0, 0, 0), -1, -1));
        //componentsJPanel.add(availableDatabasesJPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }
}

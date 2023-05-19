package com.LearningAutopilot.UI.Forms;

import com.LearningAutopilot.Main;
import com.LearningAutopilot.SQLHelper.EquipmentCategoriesSQLHelper;
import com.LearningAutopilot.SQLHelper.EquipmentSQLHelper;
import com.LearningAutopilot.SQLHelper.ITableSQLHelper;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class DatabaseInteractionForm {
    @Getter
    private JPanel mainPanel;
    private JScrollPane scrollPane;
    private JPanel availableTablesPanel;
    private JPanel componentsPanel;
    private JButton classroomsTableButton;
    private JButton equipmentTableButton;
    private JButton equipmentCategoriesTableButton;
    private JButton maintenanceRecordsTableButton;
    private JButton recordTypesTableButton;
    private JButton staffTableButton;
    private final int TABLES_COUNT = 6;

    public DatabaseInteractionForm() {
        initAvailableTablesPanel();
        initButtons();
    }

    private void initButtons() {
        classroomsTableButton = new JButton();
        equipmentTableButton = new JButton();
        equipmentCategoriesTableButton = new JButton();
        maintenanceRecordsTableButton = new JButton();
        recordTypesTableButton = new JButton();
        staffTableButton = new JButton();
        
        initButtonsFont();
        initButtonsText();
        initButtonsAction();
        initButtonsPlacement();
    }


    private void initButtonsFont() {
        Font buttonFont = new Font(null, Font.PLAIN, 16);

        classroomsTableButton.setFont(buttonFont);
        equipmentTableButton.setFont(buttonFont);
        equipmentCategoriesTableButton.setFont(buttonFont);
        maintenanceRecordsTableButton.setFont(buttonFont);
        recordTypesTableButton.setFont(buttonFont);
        staffTableButton.setFont(buttonFont);
    }

    private void initButtonsText(){
        classroomsTableButton.setText("Classrooms (Кабинеты)");
        equipmentTableButton.setText("Equipment (Оборудование)");
        equipmentCategoriesTableButton.setText("Equipment Categories (Категории Оборудования)");
        maintenanceRecordsTableButton.setText("Maintenance Records (Журнал Обслуживания)");
        recordTypesTableButton.setText("Record Types (Тип Обслуживания)");
        staffTableButton.setText("Staff (Работники)");
    }

    private void initButtonsAction() {
        equipmentTableButton.addActionListener(e -> goToTableInteractionForm(new EquipmentSQLHelper()));
        equipmentCategoriesTableButton.addActionListener(e -> goToTableInteractionForm(new EquipmentCategoriesSQLHelper()));
    }

    private void initButtonsPlacement(){
        availableTablesPanel.add(classroomsTableButton, getGridConstraints(0));
        availableTablesPanel.add(equipmentTableButton, getGridConstraints(1));
        availableTablesPanel.add(equipmentCategoriesTableButton, getGridConstraints(2));
        availableTablesPanel.add(maintenanceRecordsTableButton, getGridConstraints(3));
        availableTablesPanel.add(recordTypesTableButton, getGridConstraints(4));
        availableTablesPanel.add(staffTableButton, getGridConstraints(5));
    }

    private void initAvailableTablesPanel() {
        availableTablesPanel.setLayout(new GridLayoutManager(6, 1, new Insets(0, 10, 0, 10), -1, -1));
        componentsPanel.add(availableTablesPanel, getGridConstraints(1));
    }

    private GridConstraints getGridConstraints(int gridRow) {
        return new GridConstraints(gridRow, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false);
    }


    private void goToTableInteractionForm(ITableSQLHelper tableSQLHelper) {
        try {
            TableInteractionForm tableInteractionForm = new TableInteractionForm(tableSQLHelper);

            Main.mainFrame.getContentPane().removeAll();
            Main.mainFrame.add(tableInteractionForm.getMainPanel());
            Main.mainFrame.setVisible(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(Main.mainFrame,
                    "Невозможно создать таблицу",
                    "Ошибка обращения к таблице",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}

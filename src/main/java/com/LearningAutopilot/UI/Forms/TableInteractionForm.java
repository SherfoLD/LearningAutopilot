package com.LearningAutopilot.UI.Forms;

import com.LearningAutopilot.Main;
import com.LearningAutopilot.UI.TableHelper.DatabaseTableModel;
import com.LearningAutopilot.UI.TableHelper.ITableActionEvent;
import com.LearningAutopilot.UI.TableHelper.TableActionCellEditor;
import com.LearningAutopilot.UI.TableHelper.TableActionCellRenderer;
import com.intellij.uiDesigner.core.GridConstraints;
import lombok.Getter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;


public class TableInteractionForm {
    @Getter
    private JPanel mainPanel;
    private JScrollPane scrollPane;
    private JPanel tablePanel;
    private JLabel databaseTableLabel;
    private JPanel databaseTablePanel;
    private JButton goBackButton;
    private JTable databaseTable;
    private JLabel tableEditLabel;
    private JLabel tableDeleteLabel;
    private final String tableName;


    public TableInteractionForm(String tableName) throws SQLException {
        this.tableName = tableName;

        initLabel();
        initDatabaseTable();

        goBackButton.addActionListener(e -> goToDatabaseInteractionForm());
    }

    private void goToDatabaseInteractionForm() {
        Main.mainFrame.getContentPane().removeAll();

        DatabaseInteractionForm databaseInteractionForm = new DatabaseInteractionForm();
        Main.mainFrame.add(databaseInteractionForm.getMainPanel());
        Main.mainFrame.setVisible(true);
    }

    private void initDatabaseTable() {

        DefaultTableModel databaseTableModel = new DatabaseTableModel(tableName);
        databaseTable = new JTable(databaseTableModel);
        ITableActionEvent event = new ITableActionEvent() {
            @Override
            public void onEdit(int row) {
                System.out.println(databaseTableModel.getValueAt(row, 0));
            }

            @Override
            public void onDelete(int row) {
                System.out.println(databaseTableModel.getValueAt(row, 0));
            }
        };
        databaseTable.setRowHeight(40);
        databaseTable.setAutoCreateRowSorter(true);
        databaseTable.setFont(new Font(null, Font.PLAIN, 14));
        databaseTable.getTableHeader().setReorderingAllowed(false);
        databaseTable.getTableHeader().setFont(new Font(null, Font.BOLD, 14));
        //Setup for PanelAction
        int panelActionColumn = databaseTableModel.getColumnCount() - 1;
        databaseTable.getColumnModel().getColumn(panelActionColumn).setMaxWidth(110);
        databaseTable.getColumnModel().getColumn(panelActionColumn).setCellRenderer(new TableActionCellRenderer());
        databaseTable.getColumnModel().getColumn(panelActionColumn).setCellEditor(new TableActionCellEditor(event));
        //Column with ID removed
        databaseTable.removeColumn(databaseTable.getColumnModel().getColumn(0));

        databaseTablePanel.add(new JScrollPane(databaseTable), new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    private void initLabel() {
        databaseTableLabel.setText("Таблица " + tableName);
    }
}
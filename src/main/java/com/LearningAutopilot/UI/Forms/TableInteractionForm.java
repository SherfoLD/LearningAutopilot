package com.LearningAutopilot.UI.Forms;

import com.LearningAutopilot.Main;
import com.LearningAutopilot.SQLHelper.ITableSQLHelper;
import com.LearningAutopilot.UI.ComponentUtil;
import com.LearningAutopilot.UI.Dialogs.RecordDeleteDialog;
import com.LearningAutopilot.UI.Dialogs.RecordUpdateOrInsertDialog;
import com.LearningAutopilot.UI.TableHelper.DatabaseTableModel;
import com.LearningAutopilot.UI.TableHelper.ITableActionEvent;
import com.LearningAutopilot.UI.TableHelper.TableActionCellEditor;
import com.LearningAutopilot.UI.TableHelper.TableActionCellRenderer;
import com.intellij.uiDesigner.core.GridConstraints;
import lombok.Getter;

import javax.swing.*;
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
    private JButton insertRecordButton;
    private JTable databaseTable;
    private JLabel tableEditLabel;
    private JLabel tableDeleteLabel;
    private final ITableSQLHelper tableSQLHelper;


    public TableInteractionForm(ITableSQLHelper tableSQLHelper) throws SQLException {
        this.tableSQLHelper = tableSQLHelper;

        initLabel();
        initDatabaseTableModel();

        goBackButton.addActionListener(e -> goToDatabaseInteractionForm());
        insertRecordButton.addActionListener(e -> insertRecord());
    }

    private void insertRecord() {
        String ZERO_UUID = "00000000-0000-0000-0000-000000000000";
        RecordUpdateOrInsertDialog recordUpdateOrInsertDialog = new RecordUpdateOrInsertDialog(tableSQLHelper, ZERO_UUID);
        recordUpdateOrInsertDialog.pack();
        ComponentUtil.locateToCenter(recordUpdateOrInsertDialog);
        recordUpdateOrInsertDialog.setVisible(true);
    }

    private void goToDatabaseInteractionForm() {
        Main.mainFrame.getContentPane().removeAll();

        DatabaseInteractionForm databaseInteractionForm = new DatabaseInteractionForm();
        Main.mainFrame.add(databaseInteractionForm.getMainPanel());
        Main.mainFrame.setVisible(true);
    }

    private void initDatabaseTableModel() {
        DatabaseTableModel databaseTableModel = new DatabaseTableModel(tableSQLHelper);
        databaseTable = new JTable(databaseTableModel);
        int panelActionColumn = databaseTableModel.getColumnCount() - 1;
        ITableActionEvent event = new ITableActionEvent() {
            @Override
            public void onEdit(int row) {
                String record_ID = databaseTableModel.getValueAt(row, 0).toString();

                RecordUpdateOrInsertDialog recordUpdateOrInsertDialog = new RecordUpdateOrInsertDialog(tableSQLHelper, record_ID);
                recordUpdateOrInsertDialog.pack();
                ComponentUtil.locateToCenter(recordUpdateOrInsertDialog);
                recordUpdateOrInsertDialog.setVisible(true);

                databaseTableModel.refresh();
                initDatabaseTableProperties(panelActionColumn, this);
            }

            @Override
            public void onDelete(int row) {
                String record_ID = databaseTableModel.getValueAt(row, 0).toString();

                RecordDeleteDialog recordDeleteDialog = new RecordDeleteDialog(tableSQLHelper, record_ID);
                recordDeleteDialog.pack();
                ComponentUtil.locateToCenter(recordDeleteDialog);
                recordDeleteDialog.setVisible(true);

                databaseTableModel.refresh();
                initDatabaseTableProperties(panelActionColumn, this);
            }
        };
        initDatabaseTableProperties(panelActionColumn, event);

        databaseTablePanel.add(new JScrollPane(databaseTable), new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    public void initDatabaseTableProperties(int panelActionColumn, ITableActionEvent event) {
        databaseTable.setRowHeight(40);
        databaseTable.setAutoCreateRowSorter(true);
        databaseTable.setFont(new Font(null, Font.PLAIN, 14));
        databaseTable.getTableHeader().setReorderingAllowed(false);
        databaseTable.getTableHeader().setFont(new Font(null, Font.BOLD, 14));
        //Setup for PanelAction
        databaseTable.getColumnModel().getColumn(panelActionColumn).setMaxWidth(110);
        databaseTable.getColumnModel().getColumn(panelActionColumn).setCellRenderer(new TableActionCellRenderer());
        databaseTable.getColumnModel().getColumn(panelActionColumn).setCellEditor(new TableActionCellEditor(event));
        //Column with ID removed
        databaseTable.removeColumn(databaseTable.getColumnModel().getColumn(0));
    }

    private void initLabel() {
        databaseTableLabel.setText("Таблица " + tableSQLHelper.getTableName());
    }
}
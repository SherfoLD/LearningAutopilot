package com.LearningAutopilot.UI.Forms;

import com.LearningAutopilot.Exceptions.SQLExceptionMessageWrapper;
import com.LearningAutopilot.Main;
import com.LearningAutopilot.SQLHelper.EquipmentSQLHelper;
import com.LearningAutopilot.SQLHelper.ITableSQLHelper;
import com.LearningAutopilot.UI.Dialogs.RecordDeleteDialog;
import com.LearningAutopilot.UI.Dialogs.RecordUpdateOrInsertDialog;
import com.LearningAutopilot.UI.Dialogs.UpdateBalanceCostDialog;
import com.LearningAutopilot.UI.TableHelper.DatabaseTableModel;
import com.LearningAutopilot.UI.TableHelper.ITableActionEvent;
import com.LearningAutopilot.UI.TableHelper.TableActionCellEditor;
import com.LearningAutopilot.UI.TableHelper.TableActionCellRenderer;
import com.intellij.uiDesigner.core.GridConstraints;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TableInteractionForm {
    @Getter
    private JPanel mainPanel;
    private JScrollPane scrollPane;
    private JPanel tablePanel;
    private JLabel databaseTableLabel;
    private JPanel databaseTablePanel;
    private JButton goBackButton;
    private JButton insertRecordButton;
    private JButton updateBalanceCostButton;
    private JTable databaseTable;
    DatabaseTableModel databaseTableModel;
    private JLabel tableEditLabel;
    private JLabel tableDeleteLabel;
    private final ITableSQLHelper tableSQLHelper;
    private static final Logger logger = LoggerFactory.getLogger(TableInteractionForm.class);


    public TableInteractionForm(ITableSQLHelper tableSQLHelper) throws SQLException {
        this.tableSQLHelper = tableSQLHelper;

        setupUpdateBalanceCostButton();
        initLabel();
        initDatabaseTableModel();

        goBackButton.addActionListener(e -> goToDatabaseInteractionForm());
        updateBalanceCostButton.addActionListener(e -> updateBalanceCost());
        insertRecordButton.addActionListener(e -> {
            try {
                insertRecord();
            } catch (SQLException ex) {
                logger.error("SQL State: " + ex.getSQLState() + " Message: " + ex.getMessage());
                JOptionPane.showMessageDialog(Main.mainFrame,
                        SQLExceptionMessageWrapper.getWrappedSQLStateMessage(ex.getSQLState(), ex.getMessage()),
                        "Ошибка добавления/изменения записи",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void insertRecord() throws SQLException {
        String ZERO_UUID = "00000000-0000-0000-0000-000000000000";
        RecordUpdateOrInsertDialog recordUpdateOrInsertDialog = new RecordUpdateOrInsertDialog(tableSQLHelper, ZERO_UUID);
        recordUpdateOrInsertDialog.pack();
        recordUpdateOrInsertDialog.setLocationRelativeTo(Main.mainFrame);
        recordUpdateOrInsertDialog.setVisible(true);

        databaseTableModel.refresh();
        int panelActionColumn = databaseTableModel.getColumnCount() - 1;
        initDatabaseTableProperties(panelActionColumn, getPanelActionEvent(panelActionColumn));
    }

    private ITableActionEvent getPanelActionEvent(int panelActionColumn) {
        return new ITableActionEvent() {
            @Override
            public void onEdit(int row) throws SQLException {
                int rowInModel = databaseTable.convertRowIndexToModel(row);
                String record_ID = databaseTableModel.getValueAt(rowInModel, 0).toString();

                RecordUpdateOrInsertDialog recordUpdateOrInsertDialog = new RecordUpdateOrInsertDialog(tableSQLHelper, record_ID);
                recordUpdateOrInsertDialog.pack();
                recordUpdateOrInsertDialog.setLocationRelativeTo(Main.mainFrame);
                recordUpdateOrInsertDialog.setVisible(true);

                databaseTableModel.refresh();
                initDatabaseTableProperties(panelActionColumn, this);
            }

            @Override
            public void onDelete(int row) {
                int rowInModel = databaseTable.convertRowIndexToModel(row);
                String record_ID = databaseTableModel.getValueAt(rowInModel, 0).toString();

                RecordDeleteDialog recordDeleteDialog = new RecordDeleteDialog(tableSQLHelper, record_ID);
                recordDeleteDialog.pack();
                recordDeleteDialog.setLocationRelativeTo(Main.mainFrame);
                recordDeleteDialog.setVisible(true);

                databaseTableModel.refresh();
                initDatabaseTableProperties(panelActionColumn, this);
            }
        };
    }

    private void goToDatabaseInteractionForm() {
        Main.mainFrame.getContentPane().removeAll();

        DatabaseInteractionForm databaseInteractionForm = new DatabaseInteractionForm();
        Main.mainFrame.add(databaseInteractionForm.getMainPanel());
        Main.mainFrame.setVisible(true);
    }

    private void updateBalanceCost() {
        UpdateBalanceCostDialog updateBalanceCostDialog = new UpdateBalanceCostDialog();
        updateBalanceCostDialog.pack();
        updateBalanceCostDialog.setLocationRelativeTo(Main.mainFrame);
        updateBalanceCostDialog.setVisible(true);

        databaseTableModel.refresh();
        int panelActionColumn = databaseTableModel.getColumnCount() - 1;
        initDatabaseTableProperties(panelActionColumn, getPanelActionEvent(panelActionColumn));
    }

    private void initDatabaseTableModel() throws SQLException {
        databaseTableModel = new DatabaseTableModel(tableSQLHelper);
        databaseTable = new JTable(databaseTableModel);
        int panelActionColumn = databaseTableModel.getColumnCount() - 1;
        initDatabaseTableProperties(panelActionColumn, getPanelActionEvent(panelActionColumn));

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

    private void setupUpdateBalanceCostButton() {
        if (!(tableSQLHelper instanceof EquipmentSQLHelper))
            updateBalanceCostButton.setVisible(false);
    }

    private void initLabel() {
        databaseTableLabel.setText("Таблица " + tableSQLHelper.getTableName());
    }
}
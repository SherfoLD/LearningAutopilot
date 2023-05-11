package com.LearningAutopilot.UI.Forms;

import com.LearningAutopilot.DatabaseConnection;
import com.LearningAutopilot.Main;
import com.LearningAutopilot.UI.TableHelper.ITableActionEvent;
import com.LearningAutopilot.UI.TableHelper.PanelAction;
import com.LearningAutopilot.UI.TableHelper.TableActionCellEditor;
import com.LearningAutopilot.UI.TableHelper.TableActionCellRenderer;
import com.intellij.uiDesigner.core.GridConstraints;
import lombok.Getter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


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
    private DefaultTableModel databaseTableModel;
    private final String tableName;


    public TableInteractionForm(String tableName) throws SQLException {
        this.tableName = tableName;

        initLabel();
        initDatabaseTableModel();
        initDatabaseTable();

        goBackButton.addActionListener(e -> goToDatabaseInteractionForm());
    }

    private void goToDatabaseInteractionForm() {
        Main.mainFrame.getContentPane().removeAll();

        DatabaseInteractionForm databaseInteractionForm = new DatabaseInteractionForm();
        Main.mainFrame.add(databaseInteractionForm.getMainPanel());
        Main.mainFrame.setVisible(true);
    }

    private void initDatabaseTableModel() throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getDbConnection();
        Statement stmt = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);

        String query = "SELECT * FROM " + "\"" + tableName + "\"";
        ResultSet rs = stmt.executeQuery(query);

        int columnCount = rs.getMetaData().getColumnCount();

        ArrayList<String> columnNamesArray = new ArrayList<>();
        for (int i = 1; i < columnCount + 1; i++) {
            columnNamesArray.add(rs.getMetaData().getColumnName(i));
        }
        columnNamesArray.add("Actions");

        String[] columnNames = columnNamesArray.toArray(new String[0]);

        databaseTableModel = new DefaultTableModel(columnNames, 0) {
            /*@Override
            public boolean isCellEditable(int row, int column) {

                return false;
            }*/
        };

        while (rs.next()) {
            ArrayList<Object> rowData = new ArrayList<>();
            for (int i = 1; i < columnCount + 1; i++) {
                rowData.add(rs.getString(i));
            }
            rowData.add(new PanelAction());

            Object[] row = rowData.toArray();
            databaseTableModel.addRow(row);
        }

    }

    private void initDatabaseTable() {
        ITableActionEvent event = new ITableActionEvent() {
            @Override
            public void onEdit(int row) {
                System.out.println("Edit row" + row);
            }

            @Override
            public void onDelete(int row) {
                System.out.println("Delete row" + row);
            }
        };
        databaseTable = new JTable(databaseTableModel);
        databaseTable.getColumnModel().getColumn(3).setCellRenderer(new TableActionCellRenderer());
        databaseTable.getColumnModel().getColumn(3).setCellEditor(new TableActionCellEditor(event));
        databaseTable.getTableHeader().setReorderingAllowed(false);
        databaseTable.setAutoCreateRowSorter(true);

        databaseTablePanel.add(new JScrollPane(databaseTable), new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    private void initLabel() {
        databaseTableLabel.setText("Таблица " + tableName);
    }
}
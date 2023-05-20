package com.LearningAutopilot.UI.TableHelper;

import com.LearningAutopilot.DatabaseConnection;
import com.LearningAutopilot.SQLHelper.ITableSQLHelper;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DatabaseTableModel extends DefaultTableModel {
    private final ITableSQLHelper tableSQLHelper;
    private int initialColumnCount;
    Object[][] tableData;
    Object[] columnIdentifiers;

    public DatabaseTableModel(ITableSQLHelper tableSQLHelper) throws SQLException {
        this.tableSQLHelper = tableSQLHelper;
        prepareTableData();

        super.setDataVector(tableData, columnIdentifiers);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == initialColumnCount; //Last column is PanelAction
    }

    public void refresh(){
        try {
            prepareTableData();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        super.setDataVector(tableData, columnIdentifiers);
    }

    public ResultSet getResultSet() throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getDbConnection();
        Statement stmt = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);

        String query = tableSQLHelper.getClientView();
        return stmt.executeQuery(query);
    }

    private void prepareTableData() throws SQLException {
        ResultSet rs = getResultSet();
        initialColumnCount = rs.getMetaData().getColumnCount();

        //Setup Column Identifiers
        ArrayList<String> columnNamesArray = new ArrayList<>();
        for (int i = 1; i < initialColumnCount + 1; i++) {
            columnNamesArray.add(rs.getMetaData().getColumnName(i));
        }
        columnNamesArray.add("Actions");
        columnIdentifiers = columnNamesArray.toArray(new String[0]);

        //Setup Table Data
        ArrayList<Object[]> ArrayListTableData = new ArrayList<>();
        while (rs.next()) {
            Object[] rowData = new Object[initialColumnCount + 1]; //One more column for PanelAction
            for (int i = 1; i < initialColumnCount + 1; i++) {
                rowData[i - 1] = rs.getString(i);
            }
            rowData[initialColumnCount] = new PanelAction();

            ArrayListTableData.add(rowData);
        }
        tableData = ArrayListTableData.toArray(new Object[ArrayListTableData.size()][]);
    }
}

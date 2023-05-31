package com.LearningAutopilot.UI.TasksHelper;

import com.LearningAutopilot.DatabaseConnection;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TaskTableModel extends DefaultTableModel {
    private String sqlQuery;

    public TaskTableModel() {
        setPlaceholderData();
    }

    public ResultSet getResultSet() throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getDbConnection();
        Statement stmt = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);

        return stmt.executeQuery(sqlQuery);
    }

    private void setPlaceholderData() {
        super.setDataVector(new Object[][]{{"Выберите пункт"}}, new Object[]{"Ожидание"});
    }

    private void setTableData() throws SQLException {
        ResultSet rs = getResultSet();
        int initialColumnCount = rs.getMetaData().getColumnCount();

        //Setup Column Identifiers
        ArrayList<String> columnNamesArray = new ArrayList<>();
        for (int i = 1; i < initialColumnCount + 1; i++) {
            columnNamesArray.add(rs.getMetaData().getColumnName(i));
        }
        Object[] columnIdentifiers = columnNamesArray.toArray(new String[0]);

        //Setup Table Data
        ArrayList<Object[]> ArrayListTableData = new ArrayList<>();
        while (rs.next()) {
            Object[] rowData = new Object[initialColumnCount];
            for (int i = 1; i < initialColumnCount + 1; i++) {
                rowData[i - 1] = rs.getString(i);
            }
            ArrayListTableData.add(rowData);
        }
        Object[][] tableData = ArrayListTableData.toArray(new Object[ArrayListTableData.size()][]);

        super.setDataVector(tableData, columnIdentifiers);
    }

    public void updateModel(String newSqlQuery) throws SQLException {
        this.sqlQuery = newSqlQuery;

        setTableData();
    }
}

package com.LearningAutopilot.UI.TableHelper;

import com.LearningAutopilot.DatabaseConnection;
import com.LearningAutopilot.Exceptions.SQLExceptionMessageWrapper;
import com.LearningAutopilot.Main;
import com.LearningAutopilot.SQLHelper.ITableSQLHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseTableModel extends DefaultTableModel {
    private final ITableSQLHelper tableSQLHelper;
    private int initialColumnCount;
    private static final Logger logger = LoggerFactory.getLogger(DatabaseTableModel.class);

    public DatabaseTableModel(ITableSQLHelper tableSQLHelper) throws SQLException {
        this.tableSQLHelper = tableSQLHelper;

        setTableData();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == initialColumnCount; //Last column is PanelAction
    }

    public void refresh(){
        try {
            setTableData();
        } catch (SQLException ex) {
            logger.error("SQL State: " + ex.getSQLState() + " Message: " + ex.getMessage());
            JOptionPane.showMessageDialog(Main.mainFrame,
                    SQLExceptionMessageWrapper.getWrappedSQLStateMessage(ex.getSQLState(), ex.getMessage()),
                    "Ошибка обновления таблицы",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public ResultSet getStarteResultSet() throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getDbConnection();
        Statement stmt = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);

        String query = tableSQLHelper.getClientView();
        return stmt.executeQuery(query);
    }

    private void setTableData() throws SQLException {
        ResultSet rs = getStarteResultSet();
        initialColumnCount = rs.getMetaData().getColumnCount();

        //Setup Column Identifiers
        ArrayList<String> columnNamesArray = new ArrayList<>();
        for (int i = 1; i < initialColumnCount + 1; i++) {
            columnNamesArray.add(rs.getMetaData().getColumnName(i));
        }
        columnNamesArray.add("Actions");
        Object[] columnIdentifiers = columnNamesArray.toArray(new String[0]);

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
        Object[][] tableData = ArrayListTableData.toArray(new Object[ArrayListTableData.size()][]);

        super.setDataVector(tableData, columnIdentifiers);
    }
}

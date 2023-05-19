package com.LearningAutopilot.UI.Dialogs;

import com.LearningAutopilot.DatabaseConnection;
import com.LearningAutopilot.Main;
import com.LearningAutopilot.SQLHelper.ITableSQLHelper;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ForeignKeysParser {
    private ITableSQLHelper tableSQLHelper;
    private HashMap<String, ArrayList<String>> foreignKeysFullInfo = new HashMap<>();
    private ArrayList<String> foreignKeys = new ArrayList<>();

    public ForeignKeysParser(ITableSQLHelper tableSQLHelper) {
        this.tableSQLHelper = tableSQLHelper;
        try {
            initFullForeignKeysInfo();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(Main.mainFrame,
                    e.getMessage(),
                    "Ошибка добавления/изменения записи",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public ArrayList<String> getForeignKeys() {
        return foreignKeys;
    }

    public HashMap<String, String> getForeignKeyCorrelation(String foreignKeyName) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getDbConnection();
        Statement stmt = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);

        ArrayList<String> foreignKeyFullInfo = foreignKeysFullInfo.get(foreignKeyName);
        String pkColumnName = foreignKeyFullInfo.get(0);
        String pkReplacement = foreignKeyFullInfo.get(1);
        String pkTable = foreignKeyFullInfo.get(2);

        String foreignKeyCorrelationQuery = "SELECT \"" + pkColumnName + "\", \"" + pkReplacement + "\" FROM \"" + pkTable + "\"";
        ResultSet finalFK = stmt.executeQuery(foreignKeyCorrelationQuery);
        HashMap<String, String> foreignKeyCorrelation = new HashMap<>();
        while (finalFK.next()) {
            String UUID = finalFK.getString(1);
            String data = finalFK.getString(2);
            foreignKeyCorrelation.put(UUID, data);
        }
        return foreignKeyCorrelation;
    }

    public String[] getForeignKeyValuePair(String foreignKeyName, String foreignKeyData) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getDbConnection();
        Statement stmt = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);

        ArrayList<String> foreignKeyFullInfo = foreignKeysFullInfo.get(foreignKeyName);
        String pkColumnName = foreignKeyFullInfo.get(0);
        String pkReplacement = foreignKeyFullInfo.get(1);
        String pkTable = foreignKeyFullInfo.get(2);

        String foreignKeyDataQuery = "SELECT \"" + pkColumnName + "\", \"" + pkReplacement + "\" FROM \"" + pkTable + "\" WHERE \"" + pkColumnName + "\" = '" + foreignKeyData + "'";
        ResultSet finalFK = stmt.executeQuery(foreignKeyDataQuery);
        finalFK.next();
        String UUID = finalFK.getString(1);
        String data = finalFK.getString(2);

        return new String[]{UUID, data};
    }

    private void initFullForeignKeysInfo() throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getDbConnection();

        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet foreignKeysRS = metaData.getImportedKeys(conn.getCatalog(), null, tableSQLHelper.getTableName());
        while (foreignKeysRS.next()) {
            String pkTableName = foreignKeysRS.getString("PKTABLE_NAME");
            String pkColumnName = foreignKeysRS.getString("PKCOLUMN_NAME");
            String fkColumnName = foreignKeysRS.getString("FKCOLUMN_NAME");

            foreignKeys.add(fkColumnName);

            ArrayList<String> foreignKeyFullInfo = new ArrayList<>();//toReplace, replaceTo, table
            foreignKeyFullInfo.add(pkColumnName);
            String pkReplacement = getPrimaryKeyReplacement(pkColumnName);
            foreignKeyFullInfo.add(pkReplacement);
            foreignKeyFullInfo.add(pkTableName);

            foreignKeysFullInfo.put(fkColumnName, foreignKeyFullInfo);
        }
    }

    private String getPrimaryKeyReplacement(String pkColumnName) {
        for (String[] primaryKeyCorrelation : tableSQLHelper.getPrimaryKeyReplacement())
            if (pkColumnName.equals(primaryKeyCorrelation[0]))
                return primaryKeyCorrelation[1];

        return "";
    }


}

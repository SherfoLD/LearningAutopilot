package com.LearningAutopilot.SQLHelper;

public interface ITableSQLHelper {
    String[][] getPrimaryKeyReplacement();
    String getTableName();
    String getClientView();
    String getRawView();
    String getDeleteProcedure();
    String getUpdateOrInsertProcedure();
    String getByID();
}

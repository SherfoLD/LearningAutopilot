package com.LearningAutopilot.SQLHelper;

public interface ITableSQLHelper {
    String getTableName();
    String getView();
    String getDeleteProcedure();
    String getUpdateOrInsertProcedure();
    String getByID();
}

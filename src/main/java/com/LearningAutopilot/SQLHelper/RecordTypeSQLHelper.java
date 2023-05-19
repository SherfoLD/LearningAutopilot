package com.LearningAutopilot.SQLHelper;

import lombok.Getter;

@Getter
public class RecordTypeSQLHelper implements ITableSQLHelper {
    private final String[][] primaryKeyReplacement = {{}};
    private final String tableName = "RecordTypes";
    private final String clientView = "SELECT * FROM \"RecordTypes_ClientView\"";
    private final String rawView = "SELECT * FROM \"RecordTypes_RawView\"";
    private final String deleteProcedure = "CALL \"RecordTypes_Delete\"";
    private final String updateOrInsertProcedure = "CALL \"RecordTypes_UpdateOrInsert\"('field1', 'field2')";
    private final String byID = "SELECT * FROM \"RecordTypes\" WHERE \"ID_Type\" = ";
}

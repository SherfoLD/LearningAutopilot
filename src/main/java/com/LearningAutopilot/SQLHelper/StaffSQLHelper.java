package com.LearningAutopilot.SQLHelper;

import lombok.Getter;

@Getter
public class StaffSQLHelper implements ITableSQLHelper {
    private final String[][] primaryKeyReplacement = {{}};
    private final String tableName = "Staff";
    private final String clientView = "SELECT * FROM \"Staff_ClientView\"";
    private final String rawView = "SELECT * FROM \"Staff_RawView\"";
    private final String deleteProcedure = "CALL \"Staff_Delete\"";
    private final String updateOrInsertProcedure = "CALL \"Staff_UpdateOrInsert\"('field1', 'field2', 'field3')";
    private final String byID = "SELECT * FROM \"Staff\" WHERE \"ID_Staff\" = ";
}

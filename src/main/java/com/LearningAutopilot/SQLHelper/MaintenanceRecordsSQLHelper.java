package com.LearningAutopilot.SQLHelper;

import lombok.Getter;

@Getter
public class MaintenanceRecordsSQLHelper implements ITableSQLHelper {
    private final String[][] primaryKeyReplacement = {{"ID_Equipment", "Invent_Number"}, {"ID_Type", "Record_Type"}};
    private final String tableName = "MaintenanceRecords";
    private final String clientView = "SELECT * FROM \"MaintenanceRecords_ClientView\"";
    private final String rawView = "SELECT * FROM \"MaintenanceRecords_RawView\"";
    private final String deleteProcedure = "CALL \"MaintenanceRecords_Delete\"";
    private final String updateOrInsertProcedure = "CALL \"MaintenanceRecords_UpdateOrInsert\"('field1', 'field2', 'field3', 'field4', 'field5')";
    private final String byID = "SELECT * FROM \"MaintenanceRecords_RawView\" WHERE \"ID_Record\" = ";

}
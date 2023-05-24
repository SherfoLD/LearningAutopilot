package com.LearningAutopilot.SQLHelper;

import lombok.Getter;

@Getter
public class ClassroomsSQLHelper implements ITableSQLHelper {
    private final String[][] primaryKeyReplacement = {{"ID_Staff", "FIO"}};
    private final String tableName = "Classrooms";
    private final String clientView = "SELECT * FROM \"Classrooms_ClientView\"";
    private final String rawView = "SELECT * FROM \"Classrooms_RawView\"";
    private final String deleteProcedure = "CALL \"Classrooms_Delete\"";
    private final String updateOrInsertProcedure = "CALL \"Classrooms_UpdateOrInsert\"('field1', field2, 'field3', 'field4')";
    private final String byID = "SELECT * FROM \"Classrooms_RawView\" WHERE \"ID_Classroom\" = ";

}
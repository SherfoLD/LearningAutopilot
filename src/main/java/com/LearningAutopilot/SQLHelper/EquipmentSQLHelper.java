package com.LearningAutopilot.SQLHelper;

import lombok.Getter;

@Getter
public class EquipmentSQLHelper implements ITableSQLHelper {
    private final String[][] primaryKeyReplacement = {{"ID_Category", "Category"}, {"ID_Classroom", "Office_Number"}};
    private final String tableName = "Equipment";
    private final String clientView = "SELECT * FROM \"Equipment_ClientView\"";
    private final String rawView = "SELECT * FROM \"Equipment_RawView\"";
    private final String deleteProcedure = "CALL \"Equipment_Delete\"";
    private final String updateOrInsertProcedure = "CALL \"Equipment_UpdateOrInsert\"('field1', field2, 'field3', 'field4', 'field5', field6, field7, 'field8', 'field9')";
    private final String byID = "SELECT * FROM \"Equipment_RawView\" WHERE \"ID_Equipment\" = ";

}

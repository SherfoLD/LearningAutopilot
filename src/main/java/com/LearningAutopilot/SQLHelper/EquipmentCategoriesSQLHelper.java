package com.LearningAutopilot.SQLHelper;

import lombok.Getter;

@Getter
public class EquipmentCategoriesSQLHelper implements ITableSQLHelper {
    private final String[][] primaryKeyReplacement = {{}};
    private final String tableName = "EquipmentCategories";
    private final String rawView = "SELECT * FROM \"EquipmentCategories_RawView\"";
    private final String clientView = "SELECT * FROM \"EquipmentCategories_ClientView\"";
    private final String deleteProcedure = "CALL \"EquipmentCategories_Delete\"";
    private final String updateOrInsertProcedure = "CALL \"EquipmentCategories_UpdateOrInsert\"('field1', 'field2', 'field3')";
    private final String byID = "SELECT * FROM \"EquipmentCategories_RawView\" WHERE \"ID_Category\" = ";

}

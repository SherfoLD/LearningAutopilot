package com.LearningAutopilot.SQLHelper;

import lombok.Getter;

@Getter
public class EquipmentCategoriesSQLHelper implements ITableSQLHelper {
    private final String tableName = "EquipmentCategories";
    private final String view = "SELECT * FROM \"EquipmentCategories_View\"";
    private final String deleteProcedure = "CALL \"EquipmentCategories_Delete\"";
    private final String updateOrInsertProcedure = "CALL \"EquipmentCategories_UpdateOrInsert\"('field1', 'field2', 'field3')";
    private final String byID = "SELECT * FROM \"EquipmentCategories_View\" WHERE \"ID_Category\" = ";

}

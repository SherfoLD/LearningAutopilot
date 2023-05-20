package com.LearningAutopilot.UI.TableHelper;

import java.sql.SQLException;

public interface ITableActionEvent {
    void onEdit(int row) throws SQLException;
    void onDelete(int row);
}

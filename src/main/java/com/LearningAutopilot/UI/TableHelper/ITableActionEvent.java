package com.LearningAutopilot.UI.TableHelper;

public interface ITableActionEvent {
    void onEdit(int row);
    void onDelete(int row);
}

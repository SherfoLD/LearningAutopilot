package com.LearningAutopilot.UI.TasksHelper;

import lombok.Getter;

public class TaskInfo {
    private final String name;
    @Getter
    private final String sqlQuery;

    public TaskInfo(String name, String sqlQuery) {
        this.name = name;
        this.sqlQuery = sqlQuery;
    }

    public String toString(){
        return name;
    }
}

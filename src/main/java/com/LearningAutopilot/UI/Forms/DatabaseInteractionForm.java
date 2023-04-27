package com.LearningAutopilot.UI.Forms;

import lombok.Getter;

import javax.swing.*;

@Getter
public class DatabaseInteractionForm {
    private static DatabaseInteractionForm databaseInteractionForm;
    private JPanel mainPanel;
    private JScrollPane scrollPane;
    private JTable selectedDatabaseTable;

    public static DatabaseInteractionForm getInstance() {
        if (databaseInteractionForm == null) {
            databaseInteractionForm = new DatabaseInteractionForm();
        }
        return databaseInteractionForm;
    }

    public static void init() {
        databaseInteractionForm = getInstance();
    }

}

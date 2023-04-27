package com.LearningAutopilot.UI;

import com.LearningAutopilot.UI.Dialogs.DatabaseConnectionDialog;

import javax.swing.*;

public class TopMenuBar extends JMenuBar {

    private static TopMenuBar menuBar;

    private TopMenuBar() {
    }

    public static TopMenuBar getInstance() {
        if (menuBar == null) {
            menuBar = new TopMenuBar();
        }
        return menuBar;
    }

    public void init() {
        TopMenuBar topMenuBar = getInstance();

        // --------- Settings TopBar
        JMenu appMenu = new JMenu();
        appMenu.setText("Настройки");

        // Change DB Config
        JMenuItem databaseConnectionConfigItem = new JMenuItem();
        databaseConnectionConfigItem.setText("Изменить подключение");
        databaseConnectionConfigItem.addActionListener(e -> databaseConnectionConfigActionPerformed());
        appMenu.add(databaseConnectionConfigItem);

        topMenuBar.add(appMenu);
    }

    private void databaseConnectionConfigActionPerformed() {
        DatabaseConnectionDialog dialog = new DatabaseConnectionDialog();

        dialog.pack();
        ComponentUtil.locateToCenter(dialog);
        dialog.setVisible(true);
    }
}
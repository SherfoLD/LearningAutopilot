package com.LearningAutopilot.UI;

import com.LearningAutopilot.DatabaseConnection;
import com.LearningAutopilot.Exceptions.SQLExceptionMessageWrapper;
import com.LearningAutopilot.Main;
import com.LearningAutopilot.UI.Dialogs.DatabaseConnectionDialog;
import com.LearningAutopilot.UI.Forms.LoginForm;

import javax.swing.*;
import java.sql.SQLException;

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
        databaseConnectionConfigItem.addActionListener(e -> showDatabaseConnectionDialog());
        appMenu.add(databaseConnectionConfigItem);

        // Log Out from DB
        JMenuItem databaseLogOutItem = new JMenuItem();
        databaseLogOutItem.setText("Выйти из учетной записи");
        databaseLogOutItem.addActionListener(e -> {
            try {
                executeLogOut();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(Main.mainFrame,
                        SQLExceptionMessageWrapper.getWrappedSQLStateMessage(ex.getSQLState(), ex.getMessage()),
                        "Ошибка закрытия соединения",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        appMenu.add(databaseLogOutItem);

        topMenuBar.add(appMenu);
    }

    private void showDatabaseConnectionDialog() {
        DatabaseConnectionDialog dialog = new DatabaseConnectionDialog();

        dialog.pack();
        ComponentUtil.locateToCenter(dialog);
        dialog.setVisible(true);
    }

    private void executeLogOut() throws SQLException {
        DatabaseConnection.getInstance().closeConnection();

        Main.mainFrame.getContentPane().removeAll();
        LoginForm loginForm = new LoginForm();
        Main.mainFrame.add(loginForm.getMainPanel());
        Main.mainFrame.setVisible(true);
    }
}
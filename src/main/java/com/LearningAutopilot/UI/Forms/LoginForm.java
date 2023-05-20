package com.LearningAutopilot.UI.Forms;

import com.LearningAutopilot.DatabaseConnection;
import com.LearningAutopilot.Exceptions.SQLExceptionMessageWrapper;
import com.LearningAutopilot.Main;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.sql.SQLException;

public class LoginForm {
    @Getter
    private JPanel mainPanel;
    private JScrollPane scrollPane;
    private JTextField userLoginField;
    private JLabel userLoginLabel;
    private JPanel userPasswordLabel;
    private JButton userConfirmButton;
    private JLabel loginPictureLabel;
    private JPasswordField userPasswordField;

    public LoginForm() {
        initIcon();
        userConfirmButton.addActionListener(e -> {
            try {
                setupConnection();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(Main.mainFrame,
                        SQLExceptionMessageWrapper.getWrappedSQLStateMessage(ex.getSQLState(), ex.getMessage()),
                        "Ошибка подключения к системе",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void setupConnection() throws SQLException {
        String userLogin = userLoginField.getText();
        String userPassword = new String(userPasswordField.getPassword());
        DatabaseConnection.getInstance().initializeConnection(userLogin, userPassword);

        goToDatabaseInteractionForm();
    }

    private void goToDatabaseInteractionForm() {
        Main.mainFrame.getContentPane().removeAll();

        DatabaseInteractionForm databaseInteractionForm = new DatabaseInteractionForm();
        Main.mainFrame.add(databaseInteractionForm.getMainPanel());
        Main.mainFrame.setVisible(true);
    }

    private void initIcon() {
        URL databaseIconURL = LoginForm.class.getResource("/pictures/database_login_icon.png");
        ImageIcon databaseIcon = new ImageIcon(databaseIconURL);

        Image databaseImage = databaseIcon.getImage();
        Image scaledDatabaseImage = databaseImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        databaseIcon = new ImageIcon(scaledDatabaseImage);

        loginPictureLabel.setIcon(databaseIcon);
    }

}

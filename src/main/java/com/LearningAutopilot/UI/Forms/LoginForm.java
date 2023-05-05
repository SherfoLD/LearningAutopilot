package com.LearningAutopilot.UI.Forms;

import com.LearningAutopilot.DatabaseConnection;
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

        userConfirmButton.addActionListener(e -> setupConnection());
    }

    private void setupConnection() {
        String userLogin = userLoginField.getText();
        String userPassword = new String(userPasswordField.getPassword());

        try {
            DatabaseConnection.getInstance().initializeConnection(userLogin, userPassword);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(Main.mainFrame,
                    ex.getMessage(),
                    "Сбой подключения",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

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

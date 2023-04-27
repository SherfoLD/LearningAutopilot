package com.LearningAutopilot.UI.Forms;

import com.LearningAutopilot.DatabaseConnection;
import com.LearningAutopilot.Main;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.SQLException;

@Getter
public class LoginForm {
    private static LoginForm loginForm;
    private JPanel mainPanel;
    private JScrollPane scrollPane;
    private JTextField userLoginField;
    private JLabel userLoginLabel;
    private JPanel userPasswordLabel;
    private JButton userConfirmButton;
    private JLabel loginPictureLabel;
    private JPasswordField userPasswordField;

    private LoginForm() {
        userConfirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userLogin = userLoginField.getText();
                String userPassword = new String(userPasswordField.getPassword());
                try {
                    DatabaseConnection.getInstance().initializeConnection(userLogin, userPassword);

                    Main.mainFrame.getContentPane().removeAll();
                    Main.mainFrame.add(DatabaseInteractionForm.getInstance().getMainPanel());
                    Main.mainFrame.setVisible(true);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(Main.mainFrame,
                            ex.getMessage(),
                            "Inane error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static LoginForm getInstance() {
        if (loginForm == null) {
            loginForm = new LoginForm();
        }
        return loginForm;
    }

    public static void init() {
        loginForm = getInstance();

        initIcon();
    }

    private static void initIcon() {
        URL databaseIconURL = LoginForm.class.getResource("/pictures/database_login_icon.png");
        ImageIcon databaseIcon = new ImageIcon(databaseIconURL);

        Image databaseImage = databaseIcon.getImage();
        Image scaledDatabaseImage = databaseImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        databaseIcon = new ImageIcon(scaledDatabaseImage);

        loginForm.loginPictureLabel.setIcon(databaseIcon);
    }

}

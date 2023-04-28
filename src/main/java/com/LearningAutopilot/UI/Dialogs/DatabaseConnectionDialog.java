package com.LearningAutopilot.UI.Dialogs;

import com.LearningAutopilot.DatabaseConfig;
import com.LearningAutopilot.Main;
import com.LearningAutopilot.UI.Forms.LoginForm;

import javax.swing.*;

public class DatabaseConnectionDialog extends JDialog {
    private JPanel contentPane;
    private JScrollPane scrollPane;
    private JLabel databaseHostNameLabel;
    private JTextField databaseHostNameField;
    private JLabel databasePortLabel;
    private JTextField databaseNameField;
    private JLabel databaseNameLabel;
    private JButton databaseSaveConfigConfirmButton;
    private JButton databaseSaveConfigCancelButton;
    private JSpinner databasePortSpinner;

    public static void main(String[] args) {
    }

    public DatabaseConnectionDialog() {
        super(Main.mainFrame, "Подключение к БД");
        setPreviousData();
        setContentPane(contentPane);
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        databaseSaveConfigCancelButton.addActionListener(e -> dispose());
        databaseSaveConfigConfirmButton.addActionListener(e -> saveConfig());
    }

    private void setPreviousData() {
        DatabaseConfig dbConfig = DatabaseConfig.getInstance();
        String previousHostName = dbConfig.getDatabaseHostName();
        int previousPort = dbConfig.getDatabasePort();
        String previousName = dbConfig.getDatabaseName();

        databaseHostNameField.setText(previousHostName);
        databasePortSpinner.setValue(previousPort);
        databaseNameField.setText(previousName);
    }

    private void saveConfig() {
        DatabaseConfig dbConfig = DatabaseConfig.getInstance();

        dbConfig.setConfig(databaseHostNameField.getText(),
                (Integer) databasePortSpinner.getValue(),
                databaseNameField.getText());
        dispose();

        goToLoginForm();
    }

    private void goToLoginForm() {
        Main.mainFrame.getContentPane().removeAll();

        LoginForm loginForm = new LoginForm();
        Main.mainFrame.add(loginForm.getMainPanel());
        Main.mainFrame.setVisible(true);
    }

    private void createUIComponents() {
        SpinnerNumberModel portSpinnerModel = new SpinnerNumberModel(
                5432, //default
                1, //min
                65535, //max
                2 //step
        );
        databasePortSpinner = new JSpinner(portSpinnerModel);
    }
}

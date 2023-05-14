package com.LearningAutopilot.UI.Dialogs;

import com.LearningAutopilot.DatabaseConnection;
import com.LearningAutopilot.Main;
import com.LearningAutopilot.SQLHelper.ITableSQLHelper;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecordUpdateOrInsertDialog extends JDialog {
    private JPanel contentPane;
    private JScrollPane scrollPane;
    private JButton recordCancelButton;
    private JButton recordSaveButton;
    private JPanel recordFieldsPanel;
    private final ITableSQLHelper tableSQLHelper;
    private final String record_ID;
    private ArrayList<JTextField> recordTextFields = new ArrayList<>();
    private final String ZERO_UUID = "00000000-0000-0000-0000-000000000000";


    public RecordUpdateOrInsertDialog(ITableSQLHelper tableSQLHelper, String record_ID) {
        super(Main.mainFrame, "Добавления/Изменение записи");
        this.tableSQLHelper = tableSQLHelper;
        this.record_ID = record_ID;

        try {
            init();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(Main.mainFrame,
                    e.getMessage(),
                    "Ошибка добавления/изменения записи",
                    JOptionPane.ERROR_MESSAGE);
        }
        recordCancelButton.addActionListener(e -> dispose());
        recordSaveButton.addActionListener(e -> updateRecord());

        setContentPane(contentPane);
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public ResultSet getStarterResultSet() throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getDbConnection();
        Statement stmt = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);

        String query = tableSQLHelper.getByID() + "'" + record_ID + "'";
        if (record_ID.equals(ZERO_UUID)) // For INSERT
            query = tableSQLHelper.getView();

        return stmt.executeQuery(query);
    }

    private void init() throws SQLException {
        ResultSet rs = getStarterResultSet();
        int columnCount = rs.getMetaData().getColumnCount();
        initMainPanel(columnCount * 2);

        rs.next();
        for (int i = 2; i < columnCount + 1; i++) { //ID field not parsed
            String fieldName = rs.getMetaData().getColumnName(i);
            addRecordLabel(fieldName, i * 2 - 1 - 1);

            String fieldData = rs.getString(i);
            addRecordField(fieldData, i * 2 - 1);
        }
    }

    private void updateRecord() {
        try {
            Connection conn = DatabaseConnection.getInstance().getDbConnection();
            Statement stmt = conn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            ArrayList<String> updatedFieldsData = new ArrayList<>();
            updatedFieldsData.add(record_ID); //First parameter is ID always
            for (JTextField recordTextField : recordTextFields) {
                String updatedFieldData = recordTextField.getText();
                updatedFieldsData.add(updatedFieldData);
            }

            String finalProcedureQuery = getFinalProcedure(updatedFieldsData);
            stmt.executeUpdate(finalProcedureQuery);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(Main.mainFrame,
                    e.getMessage(),
                    "Ошибка добавления/изменения записи",
                    JOptionPane.ERROR_MESSAGE);
        }
        dispose();
    }

    private String getFinalProcedure(ArrayList<String> updatedFieldsData) {
        String originProcedureQuery = tableSQLHelper.getUpdateOrInsertProcedure();

        Pattern pattern = Pattern.compile("'(.*?)'");
        Matcher matcher = pattern.matcher(originProcedureQuery);
        StringBuilder finalProcedureQuery = new StringBuilder();

        int i = 0;
        while (matcher.find()) {
            String updatedFieldData = updatedFieldsData.get(i++);
            matcher.appendReplacement(finalProcedureQuery, "'" + updatedFieldData + "'");
        }
        matcher.appendTail(finalProcedureQuery);

        return finalProcedureQuery.toString();
    }

    private void addRecordLabel(String fieldName, int gridRow) {
        JLabel recordLabel = new JLabel(fieldName);
        recordLabel.setFont(new Font(null, Font.PLAIN, 16));

        recordFieldsPanel.add(recordLabel, new GridConstraints(gridRow, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    private void addRecordField(String fieldData, int gridRow) {
        JTextField recordField = new JTextField();
        recordField.setFont(new Font(null, Font.PLAIN, 16));
        if (!record_ID.equals(ZERO_UUID))
            recordField.setText(fieldData);
        recordTextFields.add(recordField);

        recordFieldsPanel.add(recordField, new GridConstraints(gridRow, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    private void initMainPanel(int gridRowCount) {
        recordFieldsPanel.setLayout(new GridLayoutManager(gridRowCount, 1, new Insets(0, 0, 0, 0), -1, -1));
    }


}

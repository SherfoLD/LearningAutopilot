package com.LearningAutopilot.UI.Dialogs;

import com.LearningAutopilot.DatabaseConnection;
import com.LearningAutopilot.Exceptions.InvalidFieldDataException;
import com.LearningAutopilot.Exceptions.SQLExceptionMessageWrapper;
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
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecordUpdateOrInsertDialog extends JDialog {
    private JPanel contentPane;
    private JScrollPane scrollPane;
    private JButton recordCancelButton;
    private JButton recordSaveButton;
    private JPanel recordFieldsPanel;
    private final ITableSQLHelper tableSQLHelper;
    private final String record_ID;
    private final String ZERO_UUID = "00000000-0000-0000-0000-000000000000";
    private static final Logger logger = LoggerFactory.getLogger(RecordUpdateOrInsertDialog.class);


    public RecordUpdateOrInsertDialog(ITableSQLHelper tableSQLHelper, String record_ID) throws SQLException {
        super(Main.mainFrame, "Добавление/Изменение записи");
        this.tableSQLHelper = tableSQLHelper;
        this.record_ID = record_ID;

        init();

        recordCancelButton.addActionListener(e -> dispose());
        recordSaveButton.addActionListener(e -> {
            try {
                updateRecord();
            } catch (SQLException ex) {
                logger.error("SQL State: " + ex.getSQLState() + " Message: " + ex.getMessage());
                JOptionPane.showMessageDialog(Main.mainFrame,
                        SQLExceptionMessageWrapper.getWrappedSQLStateMessage(ex.getSQLState(), ex.getMessage()),
                        "Ошибка добавления/изменения записи",
                        JOptionPane.ERROR_MESSAGE);

            } catch (InvalidFieldDataException ex) {
                logger.error("Message" + ex.getMessage());
                JOptionPane.showMessageDialog(Main.mainFrame,
                        "Поля должны содержать только буквы, цифры, тире и знаки препинания" +
                                "\n Вы ввели: " + ex.getMessage(),
                        "Ошибка добавления/изменения записи",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        setContentPane(contentPane);
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void init() throws SQLException {
        ResultSet rs = getStarterResultSet();
        int columnCount = rs.getMetaData().getColumnCount();
        initMainPanel(columnCount * 2);

        ForeignKeysParser foreignKeysParser = new ForeignKeysParser(tableSQLHelper);
        ArrayList<String> foreignKeys = foreignKeysParser.getForeignKeys();

        rs.next();
        for (int i = 2; i < columnCount + 1; i++) { //ID field not parsed
            //Add Label
            String fieldName = rs.getMetaData().getColumnName(i);
            addRecordLabel(fieldName, i * 2 - 1 - 1);

            //Check Insert Or Update
            String fieldData = "";
            String[] selectedBefore = new String[2];
            if (!record_ID.equals(ZERO_UUID)) {
                fieldData = rs.getString(i);
                if (foreignKeys.contains(fieldName))
                    selectedBefore = foreignKeysParser.getForeignKeyValuePair(fieldName, fieldData);
            }

            //Add ComboBox or TextField
            if (foreignKeys.contains(fieldName)) {
                HashMap<String, String> foreignKeyCorrelation = foreignKeysParser.getForeignKeyCorrelation(fieldName);
                addRecordComboBox(foreignKeyCorrelation, selectedBefore, i * 2 - 1);
            } else
                addRecordField(fieldData, i * 2 - 1);
        }
    }

    private void updateRecord() throws SQLException, InvalidFieldDataException {
        Connection conn = DatabaseConnection.getInstance().getDbConnection();
        Statement stmt = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);

        ArrayList<String> updatedFieldsData = new ArrayList<>();
        updatedFieldsData.add(record_ID); //First parameter is ID always
        Component[] components = recordFieldsPanel.getComponents();

        //Parse through components and retrieve data from them
        for (Component component : components) {
            if (component instanceof JTextField textField) {
                String updatedFieldData = textField.getText();

                if (isContainsStrangeSymbols(updatedFieldData))
                    throw new InvalidFieldDataException(updatedFieldData);
                if (updatedFieldData.equals(""))
                    updatedFieldData = "null";

                updatedFieldsData.add(updatedFieldData);

            } else if (component instanceof JComboBox comboBox) {
                String[] FKCorrelation = (String[]) comboBox.getSelectedItem();
                String UUID = FKCorrelation[0];

                updatedFieldsData.add(UUID);
            }
        }
        String finalProcedureQuery = getFinalProcedure(updatedFieldsData);
        stmt.executeUpdate(finalProcedureQuery);

        dispose();

        logger.info("Query executed: " + finalProcedureQuery);
    }

    private boolean isContainsStrangeSymbols(String fieldData) {
        String pattern = "[a-zA-Zа-яА-Я0-9\\s-+().,]*";

        return !Pattern.matches(pattern, fieldData);
    }

    private ResultSet getStarterResultSet() throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getDbConnection();
        Statement stmt = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);

        String query = tableSQLHelper.getByID() + "'" + record_ID + "'";
        if (record_ID.equals(ZERO_UUID)) // For INSERT
            query = tableSQLHelper.getRawView();

        return stmt.executeQuery(query);
    }

    private String getFinalProcedure(ArrayList<String> updatedFieldsData) {
        String originProcedureQuery = tableSQLHelper.getUpdateOrInsertProcedure();

        Pattern fieldsPattern = Pattern.compile("\\(.*"); //('field','field', field)
        Matcher fieldsMatcher = fieldsPattern.matcher(originProcedureQuery);
        fieldsMatcher.find();
        String nonFieldsQuery = originProcedureQuery.replace(fieldsMatcher.group(0), "");

        String foundFields = fieldsMatcher.group(0);
        Pattern fieldPattern = Pattern.compile("\\w+"); //field field field
        Matcher fieldMatcher = fieldPattern.matcher(foundFields);

        StringBuilder finalProcedureQuery = new StringBuilder();
        for (int i = 0; fieldMatcher.find(); i++) {
            String updatedFieldData = updatedFieldsData.get(i);
            fieldMatcher.appendReplacement(finalProcedureQuery, updatedFieldData); //Replacing field with parsed data
        }
        fieldMatcher.appendTail(finalProcedureQuery);
        finalProcedureQuery.insert(0, nonFieldsQuery);
        String finalProcedureQueryString = unquoteNullValues(finalProcedureQuery); //Unquoting null values

        return finalProcedureQueryString;
    }

    private static String unquoteNullValues(StringBuilder queryBuilder) {
        String query = queryBuilder.toString();
        return query.replace("'null'", "null");
    }

    private void addRecordLabel(String fieldName, int gridRow) {
        JLabel recordLabel = new JLabel(fieldName);
        recordLabel.setFont(new Font(null, Font.PLAIN, 16));

        recordFieldsPanel.add(recordLabel, getGridConstraints(gridRow));
    }

    private void addRecordField(String fieldData, int gridRow) {
        JTextField recordField = new JTextField();
        recordField.setFont(new Font(null, Font.PLAIN, 16));
        recordField.setText(fieldData);

        recordFieldsPanel.add(recordField, getGridConstraints(gridRow));
    }

    private void addRecordComboBox(HashMap<String, String> foreignKeyCorrelation, String[] selectedBefore, int gridRow) {
        String[][] toArray = new String[foreignKeyCorrelation.size()][2];
        int index = 0;
        int selectionIndex = 0;
        for (Map.Entry<String, String> entry : foreignKeyCorrelation.entrySet()) {
            toArray[index][0] = entry.getKey();
            toArray[index][1] = entry.getValue();
            if (toArray[index][0].equals(selectedBefore[0]))
                selectionIndex = index;
            index++;
        }

        JComboBox<String[]> recordComboBox = new JComboBox<>(toArray);
        recordComboBox.setFont(new Font(null, Font.PLAIN, 16));
        recordComboBox.setRenderer(new ComboBoxRenderer());
        recordComboBox.setSelectedIndex(selectionIndex);

        recordFieldsPanel.add(recordComboBox, getGridConstraints(gridRow));
    }

    private void initMainPanel(int gridRowCount) {
        recordFieldsPanel.setLayout(new GridLayoutManager(gridRowCount, 1, new Insets(0, 0, 0, 0), -1, -1));
    }

    private GridConstraints getGridConstraints(int gridRow) {
        return new GridConstraints(gridRow, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false);
    }
}

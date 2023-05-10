package com.LearningAutopilot.UI.Forms;

import com.LearningAutopilot.DatabaseConnection;
import com.LearningAutopilot.Main;
import com.intellij.uiDesigner.core.GridConstraints;
import lombok.Getter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TableInteractionForm {
    @Getter
    private JPanel mainPanel;
    private JScrollPane scrollPane;
    private JPanel tablePanel;
    private JLabel databaseTableLabel;
    private JPanel databaseTablePanel;
    private JButton goBackButton;
    private JTable databaseTable;
    private JLabel tableEditLabel;
    private JLabel tableDeleteLabel;
    private DefaultTableModel databaseTableModel;
    private final String tableName;


    public TableInteractionForm(String tableName) throws SQLException {
        this.tableName = tableName;

        initLabel();
        initDatabaseTableModel();
        initDatabaseTable();

        goBackButton.addActionListener(e -> goToDatabaseInteractionForm());
    }

    private void goToDatabaseInteractionForm() {
        Main.mainFrame.getContentPane().removeAll();

        DatabaseInteractionForm databaseInteractionForm = new DatabaseInteractionForm();
        Main.mainFrame.add(databaseInteractionForm.getMainPanel());
        Main.mainFrame.setVisible(true);
    }

    private void initDatabaseTableModel() throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getDbConnection();
        Statement stmt = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);

        String query = "SELECT * FROM " + "\"" + tableName + "\"";
        ResultSet rs = stmt.executeQuery(query);

        int columnCount = rs.getMetaData().getColumnCount();

        ArrayList<String> columnNamesArray = new ArrayList<>();
        for (int i = 1; i < columnCount + 1; i++) {
            columnNamesArray.add(rs.getMetaData().getColumnName(i));
        }
        columnNamesArray.add("Edit");
        columnNamesArray.add("Delete");
        initTableEditIcon();
        initTableDeleteIcon();

        String[] columnNames = columnNamesArray.toArray(new String[0]);

        databaseTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        while (rs.next()) {
            ArrayList<String> rowData = new ArrayList<>();
            for (int i = 1; i < columnCount + 1; i++) {
                rowData.add(rs.getString(i));
            }

            Object[] row = rowData.toArray();
            databaseTableModel.addRow(row);
        }

    }

    private void initTableDeleteIcon() {
        URL tableDeleteIconURL = TableInteractionForm.class.getResource("/pictures/table_delete_icon.png");
        ImageIcon tableDeleteIcon = new ImageIcon(tableDeleteIconURL);

        Image tableDeleteImage = tableDeleteIcon.getImage();
        Image scaledTableDeleteImage = tableDeleteImage.getScaledInstance(10, 10, Image.SCALE_SMOOTH);
        tableDeleteIcon = new ImageIcon(scaledTableDeleteImage);

        tableDeleteLabel = new JLabel();
        tableDeleteLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                System.out.println("CLICKED");
            }
        });
        tableDeleteLabel.setIcon(tableDeleteIcon);
    }

    private void initTableEditIcon() {
        URL tableEditIconURL = TableInteractionForm.class.getResource("/pictures/table_edit_icon.png");
        ImageIcon tableEditIcon = new ImageIcon(tableEditIconURL);

        Image tableEditImage = tableEditIcon.getImage();
        Image scaledTableEditImage = tableEditImage.getScaledInstance(10, 10, Image.SCALE_SMOOTH);
        tableEditIcon = new ImageIcon(scaledTableEditImage);

        tableEditLabel = new JLabel();
        tableEditLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                System.out.println("CLICKED");
            }
        });
        tableEditLabel.setIcon(tableEditIcon);
    }

    private void initDatabaseTable() {
        databaseTable = new JTable(databaseTableModel);
        databaseTable.getTableHeader().setReorderingAllowed(false);
        databaseTable.setAutoCreateRowSorter(true);

        databaseTablePanel.add(new JScrollPane(databaseTable), new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    private void initLabel() {
        databaseTableLabel.setText("Таблица " + tableName);
    }
}

package com.LearningAutopilot.UI.Forms;

import com.LearningAutopilot.Exceptions.SQLExceptionMessageWrapper;
import com.LearningAutopilot.Main;
import com.LearningAutopilot.UI.TasksHelper.TaskInfo;
import com.LearningAutopilot.UI.TasksHelper.TaskTableModel;
import com.intellij.uiDesigner.core.GridConstraints;
import lombok.Getter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.sql.SQLException;

public class TasksForm {
    @Getter
    private JPanel mainPanel;
    private JScrollPane scrollPane;
    private JButton goBackButton;
    private JPanel tablePanel;
    private JPanel treePanel;
    private JTree tasksTree;
    private JTable taskTable;
    TaskTableModel taskTableModel;

    public TasksForm() {
        initTree();
        initTaskTable();
        goBackButton.addActionListener(e -> goToDatabaseInteractionForm());
    }

    private void initTree() {
        DefaultMutableTreeNode mainTreeNode = new DefaultMutableTreeNode("Пункты заданий");
        createNodes(mainTreeNode);
        tasksTree = new JTree(mainTreeNode);
        tasksTree.setFont(new Font(null, Font.PLAIN, 14));
        tasksTree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);

        tasksTree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                    tasksTree.getLastSelectedPathComponent();

            if (node == null)
                return;

            Object nodeInfo = node.getUserObject();
            if (node.isLeaf() && nodeInfo instanceof TaskInfo task) {
                try {
                    taskTableModel.updateModel(task.getSqlQuery());
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(Main.mainFrame,
                            SQLExceptionMessageWrapper.getWrappedSQLStateMessage(ex.getSQLState(), ex.getMessage()),
                            "Ошибка построения таблицы",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JScrollPane treeView = new JScrollPane(tasksTree);
        treeView.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));

        treePanel.add(treeView, getGridConstraints(0));
    }


    private void initTaskTable() {
        taskTableModel = new TaskTableModel();
        taskTable = new JTable(taskTableModel);
        initTaskTableProperties();

        tablePanel.add(new JScrollPane(taskTable), new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    private void initTaskTableProperties() {
        taskTable.setRowHeight(25);
        taskTable.setAutoCreateRowSorter(true);
        taskTable.setFont(new Font(null, Font.PLAIN, 14));
        taskTable.getTableHeader().setReorderingAllowed(false);
        taskTable.getTableHeader().setFont(new Font(null, Font.BOLD, 14));
        //Column with ID removed
        //taskTable.removeColumn(databaseTable.getColumnModel().getColumn(0));
    }

    private void createNodes(DefaultMutableTreeNode mainTreeNode) {
        DefaultMutableTreeNode subTree;
        DefaultMutableTreeNode itemInTree;
        DefaultMutableTreeNode subTreeItem;

        //-----Пункт 3-----
        subTree = new DefaultMutableTreeNode("Пункт 3");
        mainTreeNode.add(subTree);
        //---a---
        itemInTree = new DefaultMutableTreeNode(
                new TaskInfo("a. Составной многотабличный запрос с CASE-выражением",
                        "SELECT * FROM \"Staff\"")
        );
        subTree.add(itemInTree);
        //---c---
        subTreeItem = new DefaultMutableTreeNode("c. Запросы, содержащие подзапрос");
        subTree.add(subTreeItem);
        //c-1
        itemInTree = new DefaultMutableTreeNode("В разделе SELECT");
        subTreeItem.add(itemInTree);
        //c-2
        itemInTree = new DefaultMutableTreeNode("В разделе FROM");
        subTreeItem.add(itemInTree);
        //c-3
        itemInTree = new DefaultMutableTreeNode("В разделе WHERE");
        subTreeItem.add(itemInTree);
        //---d---
        subTreeItem = new DefaultMutableTreeNode("d. Коррелированные подзапросы");
        subTree.add(subTreeItem);
        //d-1
        itemInTree = new DefaultMutableTreeNode("Первый");
        subTreeItem.add(itemInTree);
        //d-2
        itemInTree = new DefaultMutableTreeNode("Второй");
        subTreeItem.add(itemInTree);
        //d-3
        itemInTree = new DefaultMutableTreeNode("Третий");
        subTreeItem.add(itemInTree);
        //---e---
        itemInTree = new DefaultMutableTreeNode("e. Многотабличный запрос, содержащий группировку записей, агрегатные функции и параметр, используемый в разделе HAVING;");
        subTree.add(itemInTree);
        //---f---
        itemInTree = new DefaultMutableTreeNode("f. Запросы, содержащий предикат ANY(SOME)  или ALL (для каждого предиката);");
        subTree.add(itemInTree);

        //-----Пункт 7-----
        subTree = new DefaultMutableTreeNode("Пункт 4");
        mainTreeNode.add(subTree);
        //---4---
        itemInTree = new DefaultMutableTreeNode("Провести транзакцию");
        subTree.add(itemInTree);
    }

    private void goToDatabaseInteractionForm() {
        Main.mainFrame.getContentPane().removeAll();

        DatabaseInteractionForm databaseInteractionForm = new DatabaseInteractionForm();
        Main.mainFrame.add(databaseInteractionForm.getMainPanel());
        Main.mainFrame.setVisible(true);
    }

    private GridConstraints getGridConstraints(int gridRow) {
        return new GridConstraints(gridRow, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false);
    }
}

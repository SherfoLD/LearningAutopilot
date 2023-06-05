package com.LearningAutopilot.UI.TableHelper;

import com.LearningAutopilot.Exceptions.SQLExceptionMessageWrapper;
import com.LearningAutopilot.Main;
import com.LearningAutopilot.UI.Forms.TableInteractionForm;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.sql.SQLException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class PanelAction extends JPanel {
    private ActionButton tableEditButton;
    private ActionButton tableDeleteButton;
    private static ImageIcon tableDeleteIcon;
    private static ImageIcon tableEditIcon;
    private static final Logger logger = LoggerFactory.getLogger(PanelAction.class);

    static {
        // Delete Icon initialization
        URL tableDeleteIconURL = TableInteractionForm.class.getResource("/pictures/table_delete_icon.png");
        tableDeleteIcon = new ImageIcon(tableDeleteIconURL);

        Image tableDeleteImage = tableDeleteIcon.getImage();
        Image scaledTableDeleteImage = tableDeleteImage.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        tableDeleteIcon = new ImageIcon(scaledTableDeleteImage);

        // Edit Icon initialization
        URL tableEditIconURL = TableInteractionForm.class.getResource("/pictures/table_edit_icon.png");
        tableEditIcon = new ImageIcon(tableEditIconURL);

        Image tableEditImage = tableEditIcon.getImage();
        Image scaledTableEditImage = tableEditImage.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        tableEditIcon = new ImageIcon(scaledTableEditImage);
    }

    public PanelAction() {
        initTableEditButton();
        initTableDeleteButton();
        initLayout();
    }

    public void initEvent(ITableActionEvent event, int row){
        tableEditButton.addActionListener(e -> {
            try {
                event.onEdit(row);
            } catch (SQLException ex) {
                logger.error("SQL State: " + ex.getSQLState() + " Message: " + ex.getMessage());
                JOptionPane.showMessageDialog(Main.mainFrame,
                        SQLExceptionMessageWrapper.getWrappedSQLStateMessage(ex.getSQLState(), ex.getMessage()),
                        "Ошибка добавления/изменения записи",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        tableDeleteButton.addActionListener(e -> event.onDelete(row));
    }

    public void initTableEditButton() {
        tableEditButton = new ActionButton();
        tableEditButton.setIcon(tableEditIcon);
    }

    public void initTableDeleteButton() {
        tableDeleteButton = new ActionButton();
        tableDeleteButton.setIcon(tableDeleteIcon);
    }

    private void initLayout() {
        this.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        this.add(tableEditButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        this.add(tableDeleteButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }
}

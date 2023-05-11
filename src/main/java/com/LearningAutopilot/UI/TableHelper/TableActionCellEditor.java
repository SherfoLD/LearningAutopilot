package com.LearningAutopilot.UI.TableHelper;

import javax.swing.*;
import java.awt.*;

public class TableActionCellEditor extends DefaultCellEditor {
    private ITableActionEvent event;

    public TableActionCellEditor(ITableActionEvent event) {
        super(new JCheckBox());
        this.event = event;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        PanelAction action = new PanelAction();
        action.initEvent(event, row);
        action.setBackground(table.getSelectionBackground());
        return action;
    }
}

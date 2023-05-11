package com.LearningAutopilot.UI.TableHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TableActionCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component =  super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        PanelAction panelAction = new PanelAction();
        panelAction.setBackground(component.getBackground());
        return panelAction;
    }
}

package com.LearningAutopilot.UI.Dialogs;


import javax.swing.*;
import java.awt.*;

class ComboBoxRenderer extends JLabel implements ListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        String[] foreignKeyCorrelation = (String[]) value;
        setText(foreignKeyCorrelation[1]); //uuid, data

        return this;
    }
}
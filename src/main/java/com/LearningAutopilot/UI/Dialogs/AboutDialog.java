package com.LearningAutopilot.UI.Dialogs;

import com.LearningAutopilot.Main;

import javax.swing.*;

public class AboutDialog extends JDialog {
    private JPanel contentPane;
    private JLabel aboutInfoLabel;

    public static void main(String[] args) {

    }

    public AboutDialog() {
        super(Main.mainFrame, "О программе");
        setContentPane(contentPane);
        setModal(true);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        init();
    }

    private void init() {
        final String aboutInfoText = "Программа, осуществляющая материально-техническое обеспечение учебного процесса";
        aboutInfoLabel.setText("<html><p style=\"width:200px\">" + aboutInfoText + "</p></html>");
    }

}


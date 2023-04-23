package com.LearningAutopilot;

import com.LearningAutopilot.UI.*;
import com.formdev.flatlaf.intellijthemes.FlatGruvboxDarkSoftIJTheme;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static MainFrame mainFrame;

    public static void main(String[] args) {
        FlatGruvboxDarkSoftIJTheme.setup();

        mainFrame = new MainFrame();
        mainFrame.init();
        //mainFrame.add(something);
        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        /*Init.initGlobalFont();
        mainFrame.setContentPane(MainWindow.getInstance().getMainPanel());
        MainWindow.getInstance().init();
        Init.initAllTab();
        Init.initOthers();
        mainFrame.remove(loadingPanel);*/
    }
}

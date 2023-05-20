package com.LearningAutopilot;

import com.LearningAutopilot.Exceptions.InvalidConfigException;
import com.LearningAutopilot.UI.ComponentUtil;
import com.LearningAutopilot.UI.Dialogs.AboutDialog;
import com.LearningAutopilot.UI.Dialogs.DatabaseConnectionDialog;
import com.LearningAutopilot.UI.Forms.LoginForm;
import com.LearningAutopilot.UI.MainFrame;
import com.LearningAutopilot.UI.UiConsts;
import com.formdev.flatlaf.extras.FlatDesktop;
import com.formdev.flatlaf.intellijthemes.FlatXcodeDarkIJTheme;
import com.formdev.flatlaf.util.SystemInfo;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static MainFrame mainFrame;

    public static void main(String[] args) {
        if (SystemInfo.isMacOS) {
            prepareForMacOS();
        }

        FlatXcodeDarkIJTheme.setup();
        setupUIColors();

        mainFrame = new MainFrame();
        mainFrame.init();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        LoginForm loginForm = new LoginForm();
        mainFrame.add(loginForm.getMainPanel());
        mainFrame.setVisible(true);

        restoreDatabaseConfig();
    }

    private static void setupUIColors() {
        Color fancyGreyTableSelection = new Color(75, 77, 77);
        UIManager.put("Table.selectionBackground", fancyGreyTableSelection);
        UIManager.put("Table.cellFocusColor", fancyGreyTableSelection);
        UIManager.put("Table.showHorizontalLines", true);

        UIManager.put("Component.focusWidth", 0);
        //UIManager.put("Component.innerFocusWidth", 4);
        UIManager.put("Component.borderWidth", 2);

        //UIManager.put("Component.focusColor", Color.red);
        Color fancyWhiteBorder = new Color(243, 242, 237);
        UIManager.put("Component.focusedBorderColor", fancyWhiteBorder);
        UIManager.put("Button.hoverBorderColor", fancyWhiteBorder);
    }

    private static void restoreDatabaseConfig() {
        try {
            DatabaseConfig.getInstance().restoreConfig();
        } catch (InvalidConfigException e) {
            DatabaseConnectionDialog dialog = new DatabaseConnectionDialog();

            dialog.pack();
            ComponentUtil.locateToCenter(dialog);
            dialog.setVisible(true);
        }
    }

    public static void prepareForMacOS() {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("apple.awt.application.name", UiConsts.APP_NAME);
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", UiConsts.APP_NAME);
        System.setProperty("apple.awt.application.appearance", "system");

        FlatDesktop.setAboutHandler(() -> {
            AboutDialog dialog = new AboutDialog();

            dialog.pack();
            ComponentUtil.locateToCenter(dialog);
            dialog.setVisible(true);
        });
    }
}

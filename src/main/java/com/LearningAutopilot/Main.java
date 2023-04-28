package com.LearningAutopilot;

import com.LearningAutopilot.Exceptions.InvalidConfigException;
import com.LearningAutopilot.UI.ComponentUtil;
import com.LearningAutopilot.UI.Dialogs.AboutDialog;
import com.LearningAutopilot.UI.Dialogs.DatabaseConnectionDialog;
import com.LearningAutopilot.UI.Forms.LoginForm;
import com.LearningAutopilot.UI.MainFrame;
import com.LearningAutopilot.UI.UiConsts;
import com.formdev.flatlaf.extras.FlatDesktop;
import com.formdev.flatlaf.intellijthemes.FlatGruvboxDarkSoftIJTheme;
import com.formdev.flatlaf.util.SystemInfo;

import javax.swing.*;

public class Main {
    public static MainFrame mainFrame;

    public static void main(String[] args) {
        if (SystemInfo.isMacOS) {
            prepareForMacOS();
        }
        FlatGruvboxDarkSoftIJTheme.setup();

        mainFrame = new MainFrame();
        mainFrame.init();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        LoginForm loginForm = new LoginForm();
        mainFrame.add(loginForm.getMainPanel());
        mainFrame.setVisible(true);

        restoreDatabaseConfig();
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

        FlatDesktop.setAboutHandler(() -> {
            AboutDialog dialog = new AboutDialog();

            dialog.pack();
            ComponentUtil.locateToCenter(dialog);
            dialog.setVisible(true);
        });
    }
}

package com.LearningAutopilot.UI;

import javax.swing.*;
import java.net.URL;

public class MainFrame extends JFrame {

    public void init() {
        this.setName(UiConsts.APP_NAME);
        this.setTitle(UiConsts.APP_NAME);

        URL minimizedIconURL = getClass().getResource("/pictures/minimized_app_icon.png");
        ImageIcon minimizedIcon = new ImageIcon(minimizedIconURL);
        setIconImage(minimizedIcon.getImage());

        TopMenuBar topMenuBar = TopMenuBar.getInstance();
        topMenuBar.init();
        setJMenuBar(topMenuBar);

        ComponentUtil.setPreferSizeAndLocateToCenter(this, 0.6, 0.4);
    }

}

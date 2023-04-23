package com.LearningAutopilot.UI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.net.URL;

/**
 * Main Frame
 *
 * @author <a href="https://github.com/rememberber">RememBerBer</a>
 * @since 2021/11/08.
 */
public class MainFrame extends JFrame {

    public void init() {
        this.setName(UiConsts.APP_NAME);
        this.setTitle(UiConsts.APP_NAME);
//        FrameUtil.setFrameIcon(this);
        URL iconURL = getClass().getResource("/pictures/cpu.svg");
        ImageIcon icon = new ImageIcon(iconURL);
        setIconImage(icon.getImage());
        ComponentUtil.setPreferSizeAndLocateToCenter(this, 0.6, 0.4);
    }

}

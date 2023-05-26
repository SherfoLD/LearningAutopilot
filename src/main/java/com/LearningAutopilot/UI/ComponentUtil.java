package com.LearningAutopilot.UI;

import java.awt.*;

public class ComponentUtil {
    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int screenHeight = screenSize.height;
    private static final int screenWidth = screenSize.width;

    public static void setPreferSizeAndLocateToCenter(Component component, double preferWidthPercent, double preferHeightPercent) {
        int preferWidth = (int) (screenWidth * preferWidthPercent);
        int preferHeight = (int) (screenWidth * preferHeightPercent);

        component.setBounds((screenWidth - preferWidth) / 2, (screenHeight - preferHeight) / 2,
                preferWidth, preferHeight);
        Dimension preferSize = new Dimension(preferWidth, preferHeight);
        component.setPreferredSize(preferSize);
    }
}

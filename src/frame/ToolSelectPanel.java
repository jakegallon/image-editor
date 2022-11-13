package frame;

import tool.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.tools.Tool;
import java.awt.*;
import java.util.ArrayList;

public class ToolSelectPanel extends JPanel {

    private JPanel toolSettings;

    public ToolSelectPanel(JPanel toolSettings) {
        this.toolSettings = toolSettings;
        init();
    }

    private static final MoveTool moveTool = new MoveTool();
    private static final PenTool penTool = new PenTool();
    private static final EraseTool eraseTool = new EraseTool();
    private static final FillTool fillTool = new FillTool();
    private static final EyeTool eyeTool = new EyeTool();

    private void init() {
        setBorder(new LineBorder(new Color(49, 49, 49), 1));
        setPreferredSize(new Dimension(40, 0));
        SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);

        BaseTool[] tools = {moveTool, penTool, eraseTool, fillTool, eyeTool};
        int buttonSize = getPreferredSize().width - 2;
        int offset = 0;
        for(BaseTool tool : tools) {
            Button toolButton = new Button(tool.name);
            toolButton.setPreferredSize(new Dimension(buttonSize, buttonSize));
            add(toolButton);
            toolButton.addActionListener(e -> Controller.setActiveTool(tool));
            springLayout.putConstraint(SpringLayout.NORTH, toolButton, offset, SpringLayout.NORTH,this);
            offset += getPreferredSize().width;
        }
    }
}

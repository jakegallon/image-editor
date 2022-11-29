package frame;

import tool.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ToolSelectPanel extends JPanel {

    public ToolSelectPanel() {
        init();
    }

    private static final MoveTool moveTool = new MoveTool();
    private static final PenTool penTool = new PenTool();
    private static final EraseTool eraseTool = new EraseTool();
    private static final FillTool fillTool = new FillTool();
    private static final EyeTool eyeTool = new EyeTool();

    private static JButton selectedButton;

    private void init() {
        setBorder(new LineBorder(new Color(49, 49, 49), 1));
        setPreferredSize(new Dimension(40, 0));
        SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);

        BaseTool[] tools = {moveTool, penTool, eraseTool, fillTool, eyeTool};
        int buttonSize = getPreferredSize().width - 2;
        int offset = 0;
        for(BaseTool tool : tools) {
            JButton toolButton = new JButton();
            toolButton.setPreferredSize(new Dimension(buttonSize, buttonSize));

            toolButton.setIcon(tool.icon);
            toolButton.setBorderPainted(false);
            toolButton.setBackground(new Color(0, 0, 0, 0));

            add(toolButton);

            toolButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    if(selectedButton == toolButton) return;
                    if(selectedButton != null) {
                        selectedButton.setBackground(new Color(0, 0, 0, 0));
                    }
                    selectedButton = toolButton;
                    toolButton.setBackground(new Color(70, 106, 146));
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    if(selectedButton != toolButton) {
                        toolButton.setBackground(new Color(78, 80, 82));
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    if(selectedButton != toolButton) {
                        toolButton.setBackground(new Color(0, 0, 0, 0));
                    }
                }
            });

            springLayout.putConstraint(SpringLayout.NORTH, toolButton, offset, SpringLayout.NORTH, this);
            offset += getPreferredSize().width;
            toolButton.addActionListener(e -> Controller.setActiveTool(tool));
        }
    }
}

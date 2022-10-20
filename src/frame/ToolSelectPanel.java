package frame;

import tool.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ToolSelectPanel extends JPanel {

    public ToolSelectPanel() {
        setBorder(new LineBorder(new Color(49, 49, 49), 1));
        setPreferredSize(new Dimension(40, 0));
        SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);

        int buttonSize = getPreferredSize().width - 2;

        Button moveButton = new Button("move");
        moveButton.setPreferredSize(new Dimension(buttonSize, buttonSize));
        add(moveButton);

        moveButton.addActionListener(e -> {
            MoveTool moveTool = new MoveTool();
            Controller.setActiveTool(moveTool);
        });

        Button penButton = new Button("pen");
        penButton.setPreferredSize(new Dimension(buttonSize, buttonSize));

        penButton.addActionListener(e -> {
            PenTool penTool = new PenTool();
            Controller.setActiveTool(penTool);
        });

        add(penButton);

        Button eyeDropperButton = new Button("eye");
        eyeDropperButton.setPreferredSize(new Dimension(buttonSize, buttonSize));
        add(eyeDropperButton);

        eyeDropperButton.addActionListener(e -> {
            EyeTool eyeTool = new EyeTool();
            Controller.setActiveTool(eyeTool);
        });

        Button fillButton = new Button("fill");
        fillButton.setPreferredSize(new Dimension(buttonSize, buttonSize));
        add(fillButton);

        fillButton.addActionListener(e -> {
            FillTool fillTool = new FillTool();
            Controller.setActiveTool(fillTool);
        });

        Button eraseButton = new Button("erase");
        eraseButton.setPreferredSize(new Dimension(buttonSize, buttonSize));
        add(eraseButton);

        eraseButton.addActionListener(e -> {
            EraseTool eraseTool = new EraseTool();
            Controller.setActiveTool(eraseTool);
        });

        springLayout.putConstraint(SpringLayout.NORTH, moveButton, 0, SpringLayout.NORTH,this);
        springLayout.putConstraint(SpringLayout.NORTH, penButton, 0, SpringLayout.SOUTH,moveButton);
        springLayout.putConstraint(SpringLayout.NORTH, eyeDropperButton, 0, SpringLayout.SOUTH,penButton);
        springLayout.putConstraint(SpringLayout.NORTH, fillButton, 0, SpringLayout.SOUTH,eyeDropperButton);
        springLayout.putConstraint(SpringLayout.NORTH, eraseButton, 0, SpringLayout.SOUTH,fillButton);
    }
}

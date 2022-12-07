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

    private static JButton selectedButton;

    private void init() {
        setBorder(new LineBorder(new Color(49, 49, 49), 1));
        setPreferredSize(new Dimension(40, 0));
        SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);

        int buttonSize = getPreferredSize().width - 2;
        int offset = 0;

        for (ToolCategory category : ToolCategory.values()) {
            if(category == ToolCategory.NONE) continue;

            JButton categoryButton = new JButton();
            categoryButton.setPreferredSize(new Dimension(buttonSize, buttonSize));

            categoryButton.setIcon(category.getIcon());
            categoryButton.setBorderPainted(false);
            categoryButton.setBackground(new Color(0, 0, 0, 0));

            add(categoryButton);

            categoryButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    if (selectedButton == categoryButton) return;
                    if (selectedButton != null) {
                        selectedButton.setBackground(new Color(0, 0, 0, 0));
                    }
                    selectedButton = categoryButton;
                    categoryButton.setBackground(new Color(70, 106, 146));
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    if (selectedButton != categoryButton) {
                        categoryButton.setBackground(new Color(78, 80, 82));
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    if (selectedButton != categoryButton) {
                        categoryButton.setBackground(new Color(0, 0, 0, 0));
                    }
                }
            });

            springLayout.putConstraint(SpringLayout.NORTH, categoryButton, offset, SpringLayout.NORTH, this);
            offset += getPreferredSize().width;
            categoryButton.addActionListener(e -> ToolSettings.onNewCategory(category));
        }
    }
}

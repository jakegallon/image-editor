package frame;

import tool.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ToolSelectPanel extends JPanel {

    public ToolSelectPanel() {
        init();
    }

    private static final ArrayList<CategoryButton> categoryButtons = new ArrayList<>();
    private static CategoryButton selectedButton;

    public static void unselectSelectedButton() {
        if (selectedButton != null) {
            selectedButton.setBackground(new Color(0, 0, 0, 0));
            selectedButton = null;
        }
    }

    public static void highlightButtonByCategory(ToolCategory category) {
        for (CategoryButton c : categoryButtons) {
            if(c.category == category){
                unselectSelectedButton();
                selectedButton = c;
                c.setBackground(new Color(70, 106, 146));
            }
        }
    }

    int buttonSize;

    private void init() {
        setBorder(new LineBorder(new Color(49, 49, 49), 1));
        setPreferredSize(new Dimension(40, 0));
        SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);

        buttonSize = getPreferredSize().width - 2;
        int offset = 0;

        for (ToolCategory category : ToolCategory.values()) {
            if(category == ToolCategory.NONE) continue;

            CategoryButton categoryButton = new CategoryButton(category);
            categoryButtons.add(categoryButton);
            add(categoryButton);

            springLayout.putConstraint(SpringLayout.NORTH, categoryButton, offset, SpringLayout.NORTH, this);
            offset += getPreferredSize().width;
            categoryButton.addActionListener(e -> ToolSettings.onNewCategory(category));
        }

        highlightButtonByCategory(ToolCategory.MOVE);
    }

    private class CategoryButton extends JButton {

        private final ToolCategory category;

        private CategoryButton(ToolCategory category) {
            this.category = category;

            setPreferredSize(new Dimension(buttonSize, buttonSize));

            setIcon(category.getIcon());
            setBorderPainted(false);
            setBackground(new Color(0, 0, 0, 0));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    if (selectedButton == e.getSource()) return;
                    if (selectedButton != null) {
                        selectedButton.setBackground(new Color(0, 0, 0, 0));
                    }
                    selectedButton = (CategoryButton) e.getSource();
                    ((CategoryButton) e.getSource()).setBackground(new Color(70, 106, 146));
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    if (selectedButton != e.getSource()) {
                        ((CategoryButton) e.getSource()).setBackground(new Color(78, 80, 82));
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    if (selectedButton != e.getSource()) {
                        ((CategoryButton) e.getSource()).setBackground(new Color(0, 0, 0, 0));
                    }
                }
            });
        }
    }
}

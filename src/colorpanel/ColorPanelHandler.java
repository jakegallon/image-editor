package colorpanel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ColorPanelHandler extends JPanel {

    ColorPanel colorPanel;

    ColorHoldingPanel primaryColorHoldingPanel = new ColorHoldingPanel();
    ColorHoldingPanel secondaryColorHoldingPanel = new ColorHoldingPanel();
    ColorHoldingPanel selectedColorHoldingPanel;

    public ColorPanelHandler(ColorPanel colorPanel) {
        this.colorPanel = colorPanel;
        setPreferredSize(new Dimension(0, 32));

        SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);

        add(primaryColorHoldingPanel);
        primaryColorHoldingPanel.setCurrentColor(Color.white);
        add(secondaryColorHoldingPanel);
        secondaryColorHoldingPanel.setCurrentColor(Color.black);

        selectedColorHoldingPanel = primaryColorHoldingPanel;
        primaryColorHoldingPanel.isSelected = true;
        primaryColorHoldingPanel.highlightBorder();

        springLayout.putConstraint(SpringLayout.WEST, primaryColorHoldingPanel, 3, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.NORTH, primaryColorHoldingPanel, 6, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.SOUTH, primaryColorHoldingPanel, -6, SpringLayout.SOUTH, this);
        springLayout.putConstraint(SpringLayout.EAST, primaryColorHoldingPanel, 40, SpringLayout.WEST, primaryColorHoldingPanel);
        springLayout.putConstraint(SpringLayout.WEST, secondaryColorHoldingPanel, 3, SpringLayout.EAST, primaryColorHoldingPanel);
        springLayout.putConstraint(SpringLayout.NORTH, secondaryColorHoldingPanel, 0, SpringLayout.NORTH, primaryColorHoldingPanel);
        springLayout.putConstraint(SpringLayout.SOUTH, secondaryColorHoldingPanel, 0, SpringLayout.SOUTH, primaryColorHoldingPanel);
        springLayout.putConstraint(SpringLayout.EAST, secondaryColorHoldingPanel, 40, SpringLayout.WEST, secondaryColorHoldingPanel);
    }

    public void setSelectedColor(Color color) {
        selectedColorHoldingPanel.setCurrentColor(color);
        colorPanel.notifyColorChange();
    }

    public Color getSelectedColor() {
        return selectedColorHoldingPanel.getCurrentColor();
    }

    public Color getPrimaryColor() {
        return primaryColorHoldingPanel.getCurrentColor();
    }

    public Color getSecondaryColor() {
        return secondaryColorHoldingPanel.getCurrentColor();
    }

    private class ColorHoldingPanel extends JPanel implements MouseListener {

        private Color currentColor = new Color(255, 0, 0);
        private boolean isSelected = false;

        private ColorHoldingPanel() {
            addMouseListener(this);
            resetBorder();
        }

        public void setCurrentColor(Color color) {
            currentColor = color;
            setBackground(color);
        }

        public Color getCurrentColor() {
            return currentColor;
        }

        public void deselect() {
            isSelected = false;
            resetBorder();
        }

        private void highlightBorder() {
            Color defaultBackgroundColor = javax.swing.UIManager.getDefaults().getColor("Panel.background");
            setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(110, 150, 200), 2), new LineBorder(defaultBackgroundColor, 1)));
        }

        private void resetBorder() {
            Color defaultBackgroundColor = javax.swing.UIManager.getDefaults().getColor("Panel.background");
            setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(87, 99, 101), 2), new LineBorder(defaultBackgroundColor, 1)));
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            selectedColorHoldingPanel = this;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(isSelected) return;
            selectedColorHoldingPanel.deselect();
            selectedColorHoldingPanel = this;
            isSelected = true;
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            highlightBorder();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if(!isSelected) {
                resetBorder();
            }
        }
    }
}

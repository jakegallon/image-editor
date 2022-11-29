package frame;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TabPanel extends JPanel {

    TabWidget activeWidget;

    public TabPanel() {
        setPreferredSize(new Dimension(0, 25));
        setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
    }

    public void createTabForCanvas(Canvas canvas) {
        TabWidget tabWidget = new TabWidget(canvas);
        add(tabWidget);
    }

    public void setActiveWidgetByCanvas(Canvas canvas) {
        for(Component c : getComponents()) {
            TabWidget t = (TabWidget) c;
            if(canvas == t.canvas) {
                t.setFocused();
            }
        }
    }

    private void setActiveWidget(TabWidget tabWidget) {
        if(activeWidget != null) {
            activeWidget.unsetFocused();
        }
        activeWidget = tabWidget;
    }

    private void removeTabWidget(TabWidget tabWidget) {
        remove(tabWidget);
        revalidate();
        repaint();
    }

    private class TabWidget extends JPanel implements MouseListener {

        private final Canvas canvas;

        private TabWidget(Canvas canvas) {
            this.canvas = canvas;
            init();
            addMouseListener(this);
        }

        private void init() {
            setPreferredSize(new Dimension(140, 25));

            SpringLayout springLayout = new SpringLayout();
            setLayout(springLayout);

            JButton closeWidgetButton = createCloseWidgetButton();
            add(closeWidgetButton);

            springLayout.putConstraint(SpringLayout.EAST, closeWidgetButton, -2, SpringLayout.EAST, this);
            springLayout.putConstraint(SpringLayout.NORTH, closeWidgetButton, 2, SpringLayout.NORTH, this);
            springLayout.putConstraint(SpringLayout.WEST, closeWidgetButton, -17, SpringLayout.EAST, closeWidgetButton);
            springLayout.putConstraint(SpringLayout.SOUTH, closeWidgetButton, 17, SpringLayout.NORTH, closeWidgetButton);

            JLabel canvasName = new JLabel();
            canvasName.setText(canvas.getFileName());
            add(canvasName);

            springLayout.putConstraint(SpringLayout.WEST, canvasName, 2, SpringLayout.WEST, this);
            springLayout.putConstraint(SpringLayout.NORTH, canvasName, 0, SpringLayout.NORTH, closeWidgetButton);
            springLayout.putConstraint(SpringLayout.EAST, canvasName, 2, SpringLayout.WEST, closeWidgetButton);
            springLayout.putConstraint(SpringLayout.SOUTH, canvasName, 0, SpringLayout.SOUTH, closeWidgetButton);
        }

        private JButton createCloseWidgetButton() {
            JButton closeWidgetButton = new JButton();
            closeWidgetButton.setText("x");
            closeWidgetButton.setBorderPainted(false);
            closeWidgetButton.setBackground(new Color(0, 0, 0, 0));

            closeWidgetButton.addActionListener(e -> {
                Controller.setActiveCanvas(null);
                removeTabWidget(this);
            });

            closeWidgetButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    closeWidgetButton.setBackground(new Color(196, 43, 28));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    closeWidgetButton.setBackground(new Color(0, 0, 0, 0));
                }
            });

            return closeWidgetButton;
        }

        private void setFocused() {
            setActiveWidget(this);
            setBorder(new MatteBorder(0, 0, 4, 0, new Color(70, 106, 146)));
        }

        private void unsetFocused() {
            setBorder(null);
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            Controller.setActiveCanvas(canvas);
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            setBackground(new Color(50, 50, 50));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setBackground(null);
        }
    }
}

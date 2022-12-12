package colorpanel;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PalettePanel extends JPanel {

    private final ColorSplitPane colorSplitPane;

    int count = 0;

    private static final int GAP = 2;
    private static final Dimension COLOR_HOLDER_SIZE = new Dimension(24, 24);

    private JPanel colorHolderContainer;
    private JPanel scrollView;
    private JScrollPane scrollPane;

    private ColorAdder colorAdder;
    private final ColorHolderPopupMenu colorHolderPopupMenu;

    public PalettePanel(ColorSplitPane colorSplitPane) {
        this.colorSplitPane = colorSplitPane;
        setLayout(new BorderLayout());

        InitializeComponents();
        InitializeColorAdder();
        colorHolderPopupMenu = new ColorHolderPopupMenu();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                recalculateSize();
            }
        });
    }

    private void InitializeComponents() {
        colorHolderContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, GAP, GAP));

        scrollView = new JPanel();
        SpringLayout springLayout = new SpringLayout();
        scrollView.setLayout(springLayout);

        scrollView.add(colorHolderContainer);
        springLayout.putConstraint(SpringLayout.NORTH, colorHolderContainer, 0, SpringLayout.NORTH, scrollView);
        springLayout.putConstraint(SpringLayout.WEST, colorHolderContainer, 0, SpringLayout.WEST, scrollView);

        scrollPane = new JScrollPane(scrollView);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void InitializeColorAdder() {
        count++;
        colorAdder = new ColorAdder();
        colorHolderContainer.add(colorAdder);
    }

    private static final int SCROLL_BAR_SIZE = (Integer) UIManager.get("ScrollBar.width");

    private void recalculateSize() {
        int nCompWidth = (scrollPane.getWidth() - SCROLL_BAR_SIZE) / (COLOR_HOLDER_SIZE.width + GAP);
        int nCompHeight = (int) Math.ceil((float) count / nCompWidth);

        int width = nCompWidth * (COLOR_HOLDER_SIZE.width + GAP);
        int height = nCompHeight * (COLOR_HOLDER_SIZE.height + GAP);
        setPaletteSize(new Dimension(width, height));
    }

    private void setPaletteSize(Dimension d) {
        colorHolderContainer.setPreferredSize(d);
        colorHolderContainer.setMaximumSize(d);
        scrollView.setPreferredSize(d);
    }

    protected void createNewColorHolder(Color color) {
        count++;
        ColorHolder newColorHolder = new ColorHolder(color);
        colorHolderContainer.add(newColorHolder);
        ResetColorAdder();
        colorHolderContainer.revalidate();
    }

    protected void deselectSelectedHolder() {
        if(selectedHolder == null) return;
        selectedHolder.setBorder(defaultBorder);
        selectedHolder = null;
    }

    private void ResetColorAdder() {
        colorHolderContainer.remove(colorAdder);
        colorHolderContainer.add(colorAdder);

        if(colorAdder.getX() + colorAdder.getWidth() >= colorHolderContainer.getWidth()) {
            recalculateSize();
        }
    }

    private ColorHolder selectedHolder;
    private static final CompoundBorder defaultBorder = new CompoundBorder(new LineBorder(Color.gray, 2), new LineBorder(Color.black, 2));
    private static final CompoundBorder selectedBorder = new CompoundBorder(new LineBorder(Color.cyan, 2), new LineBorder(Color.black, 2));

    private class ColorHolder extends JPanel implements MouseListener {

        boolean isHoveringOver = false;
        Color currentColor;

        public ColorHolder(Color color) {
            addMouseListener(this);
            currentColor = color;

            setPreferredSize(COLOR_HOLDER_SIZE);
            setBackground(color);
        }

        public void setColor(Color color) {
            currentColor = color;
            setBackground(color);
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(SwingUtilities.isLeftMouseButton(e)){
                if(isHoveringOver) {
                    if(selectedHolder != this) {
                        colorSplitPane.setSelectedColor(currentColor);
                        if(selectedHolder != null) {
                            selectedHolder.setBorder(defaultBorder);
                        }
                        selectedHolder = this;
                    }
                }
            } else if(SwingUtilities.isRightMouseButton(e)) {
                colorHolderPopupMenu.show(this, e.getX(), e.getY());
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            setBorder(selectedBorder);
            isHoveringOver = true;
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if(selectedHolder != this) {
                setBorder(defaultBorder);
            }
            isHoveringOver = false;
        }
    }

    private class ColorHolderPopupMenu extends JPopupMenu {

        private ColorHolderPopupMenu() {
            JMenuItem deleteMenuItem = new JMenuItem();
            deleteMenuItem.setText("Delete");
            deleteMenuItem.addActionListener(e -> deleteMenuAction());
            add(deleteMenuItem);
            JMenuItem overwriteMenuItem = new JMenuItem();
            overwriteMenuItem.setText("Overwrite");
            overwriteMenuItem.addActionListener(e -> overwriteMenuAction());
            add(overwriteMenuItem);
        }

        private void deleteMenuAction() {
            colorHolderContainer.remove(getInvoker());
            deselectSelectedHolder();
        }

        private void overwriteMenuAction() {
            ColorHolder colorHolder = (ColorHolder) getInvoker();
            colorHolder.setColor(colorSplitPane.getSelectedColor());
        }

        @Override
        public void show(Component invoker, int x, int y) {
            super.show(invoker, x, y);
        }
    }

    private class ColorAdder extends JPanel implements MouseListener {

        boolean isHoveringOver = false;
        private final int desiredWeight;
        private final Point p1;
        private final Point p2;
        private final CompoundBorder adderBorder;

        private ColorAdder() {
            addMouseListener(this);
            setPreferredSize(COLOR_HOLDER_SIZE);

            desiredWeight = (int) (COLOR_HOLDER_SIZE.getWidth()/5);
            p1 = new Point((COLOR_HOLDER_SIZE.width / 2) - (desiredWeight / 2), 0);
            p2 = new Point(0, (COLOR_HOLDER_SIZE.height / 2) - (desiredWeight / 2));

            adderBorder = new CompoundBorder(new LineBorder(new Color(0, 0, 0, 0), 1), new LineBorder(Color.black, desiredWeight - 1));
            setBorder(adderBorder);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.black);
            g.fillRect(p1.x, p1.y + (desiredWeight + 1), desiredWeight, COLOR_HOLDER_SIZE.height - (desiredWeight + 1) * 2);
            g.fillRect(p2.x + (desiredWeight + 1), p2.y, COLOR_HOLDER_SIZE.width - (desiredWeight + 1) * 2, desiredWeight);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(SwingUtilities.isLeftMouseButton(e)){
                if(isHoveringOver) {
                    createNewColorHolder(colorSplitPane.getSelectedColor());
                    setBorder(adderBorder);
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            isHoveringOver = true;
            setBorder(selectedBorder);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            isHoveringOver = false;
            setBorder(adderBorder);
        }
    }
}

package frame;

import colorpanel.ColorSplitPane;
import tool.ToolCategory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

public class Frame extends JFrame {

    JSplitPane leftMiddle, leftMiddleRight;
    JPanel leftPanel, rightPanel;

    public static CanvasPanel canvasPanel = new CanvasPanel();
    static InfoPanel infoPanel = new InfoPanel();
    public static LayerPanel layerPanel = new LayerPanel();
    static ColorSplitPane colorSettings = new ColorSplitPane();
    static MagnificationPanel magnificationPanel = new MagnificationPanel();
    static TabPanel topPanel = new TabPanel();
    public static NotificationPanel notificationPanel = new NotificationPanel();

    public static final Color borderColor = new Color(43, 43 ,43);

    int leftPanelWidth = 310, rightPanelWidth = 310;

    public Frame() {
        addMenuBar();
        initializeWindow();
        createLayout();
        setVisible(true);

        ToolSettings.onNewCategory(ToolCategory.MOVE);

        getRootPane().registerKeyboardAction(e -> Controller.undo(),
                KeyStroke.getKeyStroke(KeyEvent.VK_Z,KeyEvent.CTRL_DOWN_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(e -> Controller.redo(),
                KeyStroke.getKeyStroke(KeyEvent.VK_Y,KeyEvent.CTRL_DOWN_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(e -> Controller.copy(),
                KeyStroke.getKeyStroke(KeyEvent.VK_C,KeyEvent.CTRL_DOWN_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(e -> Controller.cut(),
                KeyStroke.getKeyStroke(KeyEvent.VK_X,KeyEvent.CTRL_DOWN_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(e -> Controller.paste(),
                KeyStroke.getKeyStroke(KeyEvent.VK_V,KeyEvent.CTRL_DOWN_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(e -> Controller.delete(),
                KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0),JComponent.WHEN_IN_FOCUSED_WINDOW);

        getRootPane().registerKeyboardAction(e -> fileMenuSaveAction(),
        KeyStroke.getKeyStroke(KeyEvent.VK_S,KeyEvent.CTRL_DOWN_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW);

        getRootPane().registerKeyboardAction(e -> onSelectTool(ToolCategory.PEN),
                KeyStroke.getKeyStroke('W'),JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(e -> onSelectTool(ToolCategory.PEN),
                KeyStroke.getKeyStroke('w'),JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(e -> onSelectTool(ToolCategory.ERASE),
                KeyStroke.getKeyStroke('E'),JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(e -> onSelectTool(ToolCategory.ERASE),
                KeyStroke.getKeyStroke('e'),JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(e -> onSelectTool(ToolCategory.FILL),
                KeyStroke.getKeyStroke('F'),JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(e -> onSelectTool(ToolCategory.FILL),
                KeyStroke.getKeyStroke('f'),JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(e -> onSelectTool(ToolCategory.SELECT),
                KeyStroke.getKeyStroke('S'),JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(e -> onSelectTool(ToolCategory.SELECT),
                KeyStroke.getKeyStroke('s'),JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private void onSelectTool(ToolCategory category) {
        ToolSelectPanel.highlightButtonByCategory(category);
        ToolSettings.onNewCategory(category);

        if(Controller.getActiveCanvas() == null) return;
        canvasPanel.setCursor(Controller.getActiveTool().toolCursor);
    }

    private void initializeWindow() {
        setTitle("Image Editor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setMinimumSize(new Dimension(1366, 768));
    }

    private void createLayout(){
        Container pane = getContentPane();

        JPanel middlePanel = new JPanel(new BorderLayout());
        middlePanel.setMinimumSize(new Dimension(250, 0));

        SpringLayout springLayout = new SpringLayout();
        JLayeredPane canvasPanelContainer = new JLayeredPane();
        canvasPanelContainer.setLayout(springLayout);

        canvasPanelContainer.add(canvasPanel, 1, 0);
        springLayout.putConstraint(SpringLayout.NORTH, canvasPanel, 0, SpringLayout.NORTH, canvasPanelContainer);
        springLayout.putConstraint(SpringLayout.EAST, canvasPanel, 0, SpringLayout.EAST, canvasPanelContainer);
        springLayout.putConstraint(SpringLayout.SOUTH, canvasPanel, 0, SpringLayout.SOUTH, canvasPanelContainer);
        springLayout.putConstraint(SpringLayout.WEST, canvasPanel, 0, SpringLayout.WEST, canvasPanelContainer);

        canvasPanelContainer.add(notificationPanel, 2, 0);
        springLayout.putConstraint(SpringLayout.NORTH, notificationPanel, 0, SpringLayout.NORTH, canvasPanelContainer);
        springLayout.putConstraint(SpringLayout.EAST, notificationPanel, 0, SpringLayout.EAST, canvasPanelContainer);
        springLayout.putConstraint(SpringLayout.SOUTH, notificationPanel, 25, SpringLayout.NORTH, canvasPanelContainer);
        springLayout.putConstraint(SpringLayout.WEST, notificationPanel, 0, SpringLayout.WEST, canvasPanelContainer);

        middlePanel.add(topPanel, BorderLayout.PAGE_START);
        middlePanel.add(infoPanel, BorderLayout.PAGE_END);
        middlePanel.add(canvasPanelContainer, BorderLayout.CENTER);

        leftPanel = new JPanel(new BorderLayout());
        leftPanel.setMinimumSize(new Dimension(310, 0));

        ToolSelectPanel toolBar = new ToolSelectPanel();
        leftPanel.add(toolBar, BorderLayout.LINE_START);

        JPanel toolPanel = new JPanel(new BorderLayout());
        toolPanel.add(toolBar, BorderLayout.LINE_START);
        ToolSettings toolSettings = new ToolSettings();
        toolPanel.add(toolSettings, BorderLayout.CENTER);
        JSplitPane brushColor = new JSplitPane(JSplitPane.VERTICAL_SPLIT, toolPanel, colorSettings);
        brushColor.setResizeWeight(0.5);
        leftPanel.add(brushColor);

        rightPanel=new JPanel(new BorderLayout());
        rightPanel.setMinimumSize(new Dimension(250, 0));

        SpriteTabbedPane spriteTabbedPane = new SpriteTabbedPane();

        JSplitPane magnificationAnimation = new JSplitPane(JSplitPane.VERTICAL_SPLIT, magnificationPanel, spriteTabbedPane);
        JSplitPane magnificationAnimationLayer = new JSplitPane(JSplitPane.VERTICAL_SPLIT, magnificationAnimation, layerPanel);
        magnificationAnimationLayer.setResizeWeight(1.0);
        rightPanel.setBorder(new MatteBorder(1, 1, 0, 1, borderColor));
        rightPanel.add(magnificationAnimationLayer);

        leftMiddle = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, middlePanel);
        leftMiddle.setDividerLocation(leftPanelWidth);
        leftMiddleRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftMiddle, rightPanel);
        leftMiddleRight.setDividerLocation(pane.getWidth() - rightPanelWidth);
        leftMiddleRight.setResizeWeight(1.0);
        leftMiddleRight.setBorder(new MatteBorder(1, 0, 1, 0, borderColor));
        pane.add(leftMiddleRight);

        leftMiddleRight.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                leftMiddleRight.setDividerLocation(leftMiddleRight.getWidth() - rightPanelWidth);
                leftMiddle.setDividerLocation(leftPanelWidth);
            }
        });

        leftMiddle.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY,
                evt -> leftPanelWidth = (int)evt.getNewValue());

        leftMiddleRight.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY,
                evt -> rightPanelWidth = pane.getWidth() - (int)evt.getNewValue());
    }

    public static void onCanvasSwap(Canvas canvas) {
        canvasPanel.setCanvas(canvas);
        canvasPanel.setCursor(canvasPanel.activeTool.toolCursor);
        layerPanel.onCanvasSwitch();
        magnificationPanel.setCanvas(canvas);
        topPanel.setActiveWidgetByCanvas(canvas);
    }

    private void fileMenuNewAction() {
        NewDialog newDialog = new NewDialog(this);
        newDialog.setVisible(true);
    }

    private void fileMenuOpenAction() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(genericCustomFilter());

        int choice = fileChooser.showOpenDialog(this);
        if(choice == JFileChooser.APPROVE_OPTION) {
            File targetFile = fileChooser.getSelectedFile();

            try {
                FileInputStream fileInputStream = new FileInputStream(targetFile);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

                Canvas canvas = (Canvas) objectInputStream.readObject();
                Controller.setActiveCanvas(canvas);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void fileMenuSaveAction() {
        if(Controller.getActiveCanvas() == null) {
            NotificationPanel.playNotification(NotificationMessage.NO_CANVAS_SAVE);
            return;
        }
        if(Controller.getActiveCanvas().file != null) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(Controller.getActiveCanvas().file);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

                objectOutputStream.writeObject(Controller.getActiveCanvas());

                objectOutputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            fileMenuSaveAsAction();
        }
    }

    private void fileMenuSaveAsAction() {
        if(Controller.getActiveCanvas() == null) {
            NotificationPanel.playNotification(NotificationMessage.NO_CANVAS_SAVE);
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(genericCustomFilter());

        int choice = fileChooser.showSaveDialog(this);
        if(choice == JFileChooser.APPROVE_OPTION) {
            File targetFile = fileChooser.getSelectedFile();
            String fileName = targetFile.getName();

            if(!fileName.endsWith(".jmg")) {
                fileName = fileName.concat(".jmg");
            }

            File file = new File(targetFile.getParentFile(), fileName);
            Controller.getActiveCanvas().file = file;

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

                objectOutputStream.writeObject(Controller.getActiveCanvas());

                objectOutputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private FileFilter genericCustomFilter() {
        return new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else {
                    String fileName = f.getName().toLowerCase();
                    return fileName.endsWith(".jmg");
                }
            }

            @Override
            public String getDescription() {
                return "Custom (*.jmg)";
            }
        };
    }

    private void fileMenuExportAsAction() {
        if(Controller.getActiveCanvas() == null) {
            NotificationPanel.playNotification(NotificationMessage.NO_CANVAS_EXPORT);
            return;
        }
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                String fN = f.getName().toLowerCase();
                if(f.isDirectory()) return true;
                for(FileFormat fileFormat : FileFormat.values())
                    if (fN.endsWith("." + fileFormat.getName())) return true;
                return false;
            }

            @Override
            public String getDescription() {
                return "Compatible";
            }
        });

        for(FileFormat fileFormat : FileFormat.values()) {
            fileChooser.addChoosableFileFilter(fileFormat.getFileFormatFilter());
        }

        int choice = fileChooser.showSaveDialog(this);
        if(choice == JFileChooser.APPROVE_OPTION) {
            File targetFile = fileChooser.getSelectedFile();
            FileFormat fileFormat = ((FileFormat.FileFormatFilter) fileChooser.getFileFilter()).getFileFormat();

            String suffix = fileFormat.getSuffix();
            String fileName = targetFile.getName();

            if(!fileName.endsWith(suffix)) {
                fileName = fileName.concat(suffix);
            }

            File file = new File(targetFile.getParentFile(), fileName);

            try {
                BufferedImage image = Controller.getActiveCanvas().getImage();

                if(fileFormat == FileFormat.JPG || fileFormat == FileFormat.BMP) {
                    BufferedImage rgbImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
                    rgbImage.getGraphics().drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
                    ImageIO.write(rgbImage, fileFormat.name(), file);
                } else {
                        ImageIO.write(image, fileFormat.name(), file);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void fileMenuCloseAction() {
        setState(ICONIFIED);
    }

    private void fileMenuExitAction() {
        dispose();
    }

    private void editMenuUndoAction() {
        Controller.undo();
    }

    private void editMenuRedoAction() {
        Controller.redo();
    }

    private void editMenuCutAction() {
        Controller.cut();
    }

    private void editMenuCopyAction() {
        Controller.copy();
    }

    private void editMenuPasteAction() {
        Controller.paste();
    }

    private void editMenuDeleteAction() {
        Controller.delete();
    }

    private void helpMenuAboutAction() {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/jakegallon/image-editor"));
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void helpMenuGitHubAction() {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/jakegallon/image-editor"));
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void addMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu();
        fileMenu.setText("File");
        JMenuItem fileMenuNew = new JMenuItem();
        fileMenuNew.setText("New");
        fileMenuNew.addActionListener(e -> fileMenuNewAction());
        JMenuItem fileMenuOpen = new JMenuItem();
        fileMenuOpen.setText("Open");
        fileMenuOpen.addActionListener(e -> fileMenuOpenAction());
        JMenuItem fileMenuSave = new JMenuItem();
        fileMenuSave.setText("Save");
        fileMenuSave.addActionListener(e -> fileMenuSaveAction());
        JMenuItem fileMenuSaveAs = new JMenuItem();
        fileMenuSaveAs.setText("Save As");
        fileMenuSaveAs.addActionListener(e -> fileMenuSaveAsAction());
        JMenuItem fileMenuExportAs = new JMenuItem();
        fileMenuExportAs.setText("Export As");
        fileMenuExportAs.addActionListener(e -> fileMenuExportAsAction());
        JMenuItem fileMenuClose = new JMenuItem();
        fileMenuClose.setText("Close");
        fileMenuClose.addActionListener(e -> fileMenuCloseAction());
        JMenuItem fileMenuExit = new JMenuItem();
        fileMenuExit.setText("Exit");
        fileMenuExit.addActionListener(e -> fileMenuExitAction());

        fileMenu.add(fileMenuNew);
        fileMenu.add(fileMenuOpen);
        fileMenu.add(fileMenuSave);
        fileMenu.add(fileMenuSaveAs);
        fileMenu.addSeparator();
        fileMenu.add(fileMenuExportAs);
        fileMenu.addSeparator();
        fileMenu.add(fileMenuClose);
        fileMenu.add(fileMenuExit);

        menuBar.add(fileMenu);

        JMenu editMenu = new JMenu();
        editMenu.setText("Edit");
        JMenuItem editMenuUndo = new JMenuItem();
        editMenuUndo.setText("Undo");
        editMenuUndo.addActionListener(e -> editMenuUndoAction());
        JMenuItem editMenuRedo = new JMenuItem();
        editMenuRedo.setText("Redo");
        editMenuRedo.addActionListener(e -> editMenuRedoAction());
        JMenuItem editMenuCut = new JMenuItem();
        editMenuCut.setText("Cut");
        editMenuCut.addActionListener(e -> editMenuCutAction());
        JMenuItem editMenuCopy = new JMenuItem();
        editMenuCopy.setText("Copy");
        editMenuCopy.addActionListener(e -> editMenuCopyAction());
        JMenuItem editMenuPaste = new JMenuItem();
        editMenuPaste.setText("Paste");
        editMenuPaste.addActionListener(e -> editMenuPasteAction());
        JMenuItem editMenuDelete = new JMenuItem();
        editMenuDelete.setText("Delete");
        editMenuDelete.addActionListener(e -> editMenuDeleteAction());

        editMenu.add(editMenuUndo);
        editMenu.add(editMenuRedo);
        editMenu.addSeparator();
        editMenu.add(editMenuCut);
        editMenu.add(editMenuCopy);
        editMenu.add(editMenuPaste);
        editMenu.add(editMenuDelete);

        menuBar.add(editMenu);

        JMenu helpMenu = new JMenu();
        helpMenu.setText("Help");
        JMenuItem helpMenuAbout = new JMenuItem();
        helpMenuAbout.setText("About");
        helpMenuAbout.addActionListener(e -> helpMenuAboutAction());
        JMenuItem helpMenuGithub = new JMenuItem();
        helpMenuGithub.setText("View on GitHub");
        helpMenuGithub.addActionListener(e -> helpMenuGitHubAction());

        helpMenu.add(helpMenuAbout);
        helpMenu.addSeparator();
        helpMenu.add(helpMenuGithub);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

}

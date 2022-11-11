package frame;

import colorpanel.ColorTabbedPane;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Frame extends JFrame {

    JSplitPane leftMiddle, leftMiddleRight;
    JPanel leftPanel, rightPanel, topPanel;

    public static CanvasPanel canvasPanel = new CanvasPanel();
    static InfoPanel infoPanel = new InfoPanel();
    static LayerPanel layerPanel = new LayerPanel();
    static ColorTabbedPane colorSettings = new ColorTabbedPane();
    static MagnificationPanel magnificationPanel = new MagnificationPanel();

    int leftPanelWidth = 310, rightPanelWidth = 310;

    public Frame() {
        addMenuBar();
        initializeWindow();
        createLayout();
        setVisible(true);

        getRootPane().registerKeyboardAction(e -> Controller.undo(),
                KeyStroke.getKeyStroke(KeyEvent.VK_Z,KeyEvent.CTRL_DOWN_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW );
        getRootPane().registerKeyboardAction(e -> Controller.redo(),
                KeyStroke.getKeyStroke(KeyEvent.VK_Y,KeyEvent.CTRL_DOWN_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW );
    }

    private void initializeWindow() {
        setTitle("Image Editor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setMinimumSize(new Dimension(960, 540));
    }

    private void createLayout(){
        Container pane = getContentPane();

        // Main panel
        JPanel middlePanel=new JPanel();
        middlePanel.setMinimumSize(new Dimension(250, 0));
        middlePanel.setLayout(new BorderLayout());

        topPanel=new JPanel();
        topPanel.setBackground(Color.MAGENTA);
        topPanel.setPreferredSize(new Dimension(pane.getWidth(), 30));
        middlePanel.add(topPanel, BorderLayout.PAGE_START);
        middlePanel.add(infoPanel, BorderLayout.PAGE_END);
        middlePanel.add(canvasPanel, BorderLayout.CENTER);

        // left panel
        // leftPanel - toolBar
        ToolSelectPanel toolBar = new ToolSelectPanel();
        // leftPanel - brushSettings
        ToolSettingsPanel toolSettings = new ToolSettingsPanel();
        toolSettings.setMinimumSize(new Dimension(0, 250));
        // leftPanel - colorSettings
        colorSettings.setMinimumSize(new Dimension(250, 273));

        leftPanel=new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.RED);
        leftPanel.setMinimumSize(new Dimension(310, 0));

        leftPanel.add(toolBar, BorderLayout.LINE_START);

        JPanel toolSelectSettings = new JPanel(new BorderLayout());
        toolSelectSettings.add(toolBar, BorderLayout.LINE_START);
        toolSelectSettings.add(toolSettings, BorderLayout.CENTER);

        JSplitPane brushColor = new JSplitPane(JSplitPane.VERTICAL_SPLIT, toolSelectSettings, colorSettings);
        brushColor.setResizeWeight(1.0);
        leftPanel.add(brushColor);

        // Right panel
        // rightPanel - canvasOverview
        magnificationPanel.setMinimumSize(new Dimension(0, 250));
        // rightPanel - animationPanel
        SpriteTabbedPane spriteTabbedPane = new SpriteTabbedPane();
        spriteTabbedPane.setMinimumSize(new Dimension(0, 250));
        // rightPanel - layerPanel
        layerPanel.setMinimumSize(new Dimension(0, 250));

        rightPanel=new JPanel(new BorderLayout());
        JSplitPane canvasAnimation = new JSplitPane(JSplitPane.VERTICAL_SPLIT, magnificationPanel, spriteTabbedPane);
        JSplitPane canvasAnimationLayer = new JSplitPane(JSplitPane.VERTICAL_SPLIT, canvasAnimation, layerPanel);
        canvasAnimationLayer.setResizeWeight(1.0);

        rightPanel.add(canvasAnimationLayer);
        rightPanel.setMinimumSize(new Dimension(250, 0));

        // Triple SplitPane
        leftMiddle = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, middlePanel);
        leftMiddle.setDividerLocation(leftPanelWidth);
        leftMiddleRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftMiddle, rightPanel);
        leftMiddleRight.setDividerLocation(pane.getWidth() - rightPanelWidth);
        leftMiddleRight.setResizeWeight(1.0);

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

    private void fileMenuNewAction() {
        NewDialog newDialog = new NewDialog(this);
        newDialog.setVisible(true);
    }

    private void fileMenuOpenAction() {
    }

    private void fileMenuSaveAction() {
    }

    private void fileMenuSaveAsAction() {
    }

    private void fileMenuImportLayerAction() {
    }

    private void fileMenuExportAsAction() {
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

    private void fileMenuOptionsAction() {
    }

    private void fileMenuCloseAction() {
        setState(ICONIFIED);
    }

    private void fileMenuExitAction() {
        dispose();
    }

    private void editMenuUndoAction() {
    }

    private void editMenuRedoAction() {
    }

    private void editMenuCutAction() {
    }

    private void editMenuCopyAction() {
    }

    private void editMenuPasteAction() {
    }

    private void editMenuDeleteAction() {
    }

    private void editMenuFlipHorizontalAction() {
    }

    private void editMenuFlipVerticalAction() {
    }

    private void editMenuCanvasSizeAction() {
    }

    private void helpMenuHelpAction() {
    }

    private void helpMenuAboutAction() {
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
        JMenuItem fileMenuImportLayer = new JMenuItem();
        fileMenuImportLayer.setText("Import Layer");
        fileMenuImportLayer.addActionListener(e -> fileMenuImportLayerAction());
        JMenuItem fileMenuExportAs = new JMenuItem();
        fileMenuExportAs.setText("Export As");
        fileMenuExportAs.addActionListener(e -> fileMenuExportAsAction());
        JMenuItem fileMenuOptions = new JMenuItem();
        fileMenuOptions.setText("Options");
        fileMenuOptions.addActionListener(e -> fileMenuOptionsAction());
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
        fileMenu.add(fileMenuImportLayer);
        fileMenu.add(fileMenuExportAs);
        fileMenu.addSeparator();
        fileMenu.add(fileMenuOptions);
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
        JMenuItem editMenuFlipHorizontal = new JMenuItem();
        editMenuFlipHorizontal.setText("Flip Horizontal");
        editMenuFlipHorizontal.addActionListener(e -> editMenuFlipHorizontalAction());
        JMenuItem editMenuFlipVertical = new JMenuItem();
        editMenuFlipVertical.setText("Flip Vertical");
        editMenuFlipVertical.addActionListener(e -> editMenuFlipVerticalAction());
        JMenuItem editMenuCanvasSize = new JMenuItem();
        editMenuCanvasSize.setText("Canvas Size");
        editMenuCanvasSize.addActionListener(e -> editMenuCanvasSizeAction());

        editMenu.add(editMenuUndo);
        editMenu.add(editMenuRedo);
        editMenu.addSeparator();
        editMenu.add(editMenuCut);
        editMenu.add(editMenuCopy);
        editMenu.add(editMenuPaste);
        editMenu.add(editMenuDelete);
        editMenu.addSeparator();
        editMenu.add(editMenuFlipHorizontal);
        editMenu.add(editMenuFlipVertical);
        editMenu.addSeparator();
        editMenu.add(editMenuCanvasSize);

        menuBar.add(editMenu);

        JMenu helpMenu = new JMenu();
        helpMenu.setText("Help");
        JMenuItem helpMenuHelp = new JMenuItem();
        helpMenuHelp.setText("Help");
        helpMenuHelp.addActionListener(e -> helpMenuHelpAction());
        JMenuItem helpMenuAbout = new JMenuItem();
        helpMenuAbout.setText("About");
        helpMenuAbout.addActionListener(e -> helpMenuAboutAction());
        JMenuItem helpMenuGithub = new JMenuItem();
        helpMenuGithub.setText("View on GitHub");
        helpMenuGithub.addActionListener(e -> helpMenuGitHubAction());

        helpMenu.add(helpMenuHelp);
        helpMenu.addSeparator();
        helpMenu.add(helpMenuAbout);
        helpMenu.addSeparator();
        helpMenu.add(helpMenuGithub);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

}

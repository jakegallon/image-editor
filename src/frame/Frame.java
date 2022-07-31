package frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Frame extends JFrame {

    JSplitPane leftMiddle, leftMiddleRight;
    JPanel leftPanel, rightPanel, topPanel;

    CanvasPanel canvasPanel;
    InfoPanel infoPanel;

    int leftPanelWidth = 350, rightPanelWidth = 350;

    public Frame() {
        addMenuBar();
        initializeWindow();
        createLayout();
        setVisible(true);
    }

    private void initializeWindow() {
        setTitle("Image Editor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setMinimumSize(new Dimension(960, 540));
    }

    private void createLayout(){
        Container pane = getContentPane();

        // Temporary panel creations
        // leftPanel - toolBar
        JPanel toolBar = new JPanel();
        toolBar.setBackground(Color.gray);
        toolBar.setPreferredSize(new Dimension(40, 0));
        // leftPanel - brushSettings
        JPanel brushSettings = new JPanel();
        brushSettings.setBackground(Color.cyan);
        brushSettings.setMinimumSize(new Dimension(0, 250));
        // leftPanel - colorSettings
        ColorPanel colorSettings = new ColorPanel();
        colorSettings.setMinimumSize(new Dimension(0, 250));
        // rightPanel - canvasOverview
        JPanel canvasOverview = new JPanel();
        canvasOverview.setBackground(Color.RED);
        canvasOverview.setMinimumSize(new Dimension(0, 250));
        // rightPanel - animationPanel
        JPanel animationPanel = new JPanel();
        animationPanel.setBackground(Color.ORANGE);
        animationPanel.setMinimumSize(new Dimension(0, 250));
        // rightPanel - layerPanel
        JPanel layerPanel = new JPanel();
        layerPanel.setBackground(Color.YELLOW);
        layerPanel.setMinimumSize(new Dimension(0, 250));

        // left panel
        leftPanel=new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.RED);
        leftPanel.setMinimumSize(new Dimension(250, 0));

        leftPanel.add(toolBar, BorderLayout.LINE_START);
        JSplitPane brushColor = new JSplitPane(JSplitPane.VERTICAL_SPLIT, brushSettings, colorSettings);
        brushColor.setResizeWeight(1.0);
        leftPanel.add(brushColor);

        // Right panel
        rightPanel=new JPanel(new BorderLayout());
        JSplitPane canvasAnimation = new JSplitPane(JSplitPane.VERTICAL_SPLIT, canvasOverview, animationPanel);
        JSplitPane canvasAnimationLayer = new JSplitPane(JSplitPane.VERTICAL_SPLIT, canvasAnimation, layerPanel);
        canvasAnimationLayer.setResizeWeight(1.0);

        rightPanel.add(canvasAnimationLayer);
        rightPanel.setMinimumSize(new Dimension(250, 0));

        // Main panel
        JPanel middlePanel=new JPanel();
        middlePanel.setMinimumSize(new Dimension(250, 0));
        middlePanel.setLayout(new BorderLayout());

        topPanel=new JPanel();
        topPanel.setBackground(Color.MAGENTA);
        topPanel.setPreferredSize(new Dimension(pane.getWidth(), 30));
        middlePanel.add(topPanel, BorderLayout.PAGE_START);
        infoPanel = new InfoPanel();
        middlePanel.add(infoPanel, BorderLayout.PAGE_END);
        canvasPanel =new CanvasPanel(infoPanel);
        middlePanel.add(canvasPanel, BorderLayout.CENTER);

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

        leftMiddle.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, evt -> {
            leftPanelWidth = (int)evt.getNewValue();
        });

        leftMiddleRight.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, evt -> {
            rightPanelWidth = pane.getWidth() - (int)evt.getNewValue();
        });
    }

    private void fileMenuNewAction() {
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
    }

    private void fileMenuOptionsAction() {
    }

    private void fileMenuCloseAction() {
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

package frame;

import tool.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ToolSettings extends JPanel {

    private static final JPanel toolPresets = new JPanel();
    private static JButton selectedSubToolButton;
    private static final JPanel toolSettings = new JPanel();

    private static final JScrollPane toolSettingsPane = new JScrollPane(toolSettings);

    private static final MoveCameraTool MOVE_CAMERA_TOOL = new MoveCameraTool();
    private static final MoveLayerTool MOVE_LAYER_TOOL = new MoveLayerTool();
    private static final MoveSelectionTool MOVE_SELECTION_TOOL = new MoveSelectionTool();
    private static final PenTool PEN_TOOL_1 = new PenTool();
    private static final PenTool PEN_TOOL_2 = new PenTool();
    private static final PenTool PEN_TOOL_3 = new PenTool();
    private static final EraseTool ERASE_TOOL = new EraseTool();
    private static final FillTool FILL_TOOL = new FillTool();
    private static final EyeTool EYE_TOOL = new EyeTool();
    private static final SelectTool SELECT_TOOL = new SelectTool();

    private static final BaseTool[] tools = {
            MOVE_CAMERA_TOOL,
            MOVE_LAYER_TOOL,
            MOVE_SELECTION_TOOL,
            PEN_TOOL_1,
            PEN_TOOL_2,
            PEN_TOOL_3,
            ERASE_TOOL,
            FILL_TOOL,
            EYE_TOOL,
            SELECT_TOOL
    };

    public ToolSettings() {
        init();
    }

    public static void onNewTool(BaseTool tool) {
        setToolSettingsFromTool(tool);

        if(tool instanceof AnimationTool) {
            if (selectedSubToolButton != null) {
                ToolSelectPanel.unselectSelectedButton();
            }
            setSubToolsFromCategory(ToolCategory.NONE);
        }
    }

    public static void onNewCategory(ToolCategory category) {
        setSubToolsFromCategory(category);
    }

    private static void setSubToolsFromCategory(ToolCategory category) {
        toolPresets.removeAll();

        if(category == ToolCategory.NONE) {
            toolPresets.revalidate();
            toolPresets.repaint();
            return;
        }

        boolean hasMatchedToolToCategory = false;
        ArrayList<String> takenNames = new ArrayList<>();

        for(BaseTool tool : tools) {
            if(tool.category == category) {
                JButton subToolButton = new JButton();
                if(takenNames.contains(tool.displayName)) {
                    int count = 1;
                    String targetName = tool.displayName + " " + count;
                    while(takenNames.contains(targetName)) {
                        count ++;
                        targetName = tool.displayName + " " + count;
                    }
                    takenNames.add(targetName);
                    subToolButton.setText(targetName);
                } else {
                    takenNames.add(tool.displayName);
                    subToolButton.setText(tool.displayName);
                }

                subToolButton.setBorderPainted(false);
                subToolButton.setBackground(new Color(0, 0, 0, 0));

                subToolButton.setMinimumSize(new Dimension(toolPresets.getWidth(), 40));
                subToolButton.setPreferredSize(new Dimension(toolPresets.getWidth(), 40));
                subToolButton.setMaximumSize(new Dimension(toolPresets.getWidth(), 40));
                subToolButton.setBounds(0, 0, toolPresets.getWidth(), 40);

                toolPresets.add(subToolButton);

                subToolButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        super.mousePressed(e);
                        if (selectedSubToolButton == subToolButton) return;
                        if (selectedSubToolButton != null) {
                            selectedSubToolButton.setBackground(new Color(0, 0, 0, 0));
                        }
                        selectedSubToolButton = subToolButton;
                        subToolButton.setBackground(new Color(70, 106, 146));
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        super.mouseEntered(e);
                        if (selectedSubToolButton != subToolButton) {
                            subToolButton.setBackground(new Color(78, 80, 82));
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        super.mouseExited(e);
                        if (selectedSubToolButton != subToolButton) {
                            subToolButton.setBackground(new Color(0, 0, 0, 0));
                        }
                    }
                });

                if(!hasMatchedToolToCategory) {
                    hasMatchedToolToCategory = true;
                    Controller.setActiveTool(tool);

                    selectedSubToolButton = subToolButton;
                    subToolButton.setBackground(new Color(70, 106, 146));
                }

                subToolButton.addActionListener(e -> Controller.setActiveTool(tool));
            }
        }

        toolPresets.revalidate();
        toolPresets.repaint();
    }

    private static void setToolSettingsFromTool(BaseTool tool) {
        toolSettings.removeAll();

        tool.populateSettingsPanel();

        toolSettings.revalidate();
        toolSettings.repaint();
    }

    private void init() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setMinimumSize(new Dimension(0, 250));

        toolPresets.setLayout(new BoxLayout(toolPresets, BoxLayout.PAGE_AXIS));
        toolSettings.setLayout(new BoxLayout(toolSettings, BoxLayout.PAGE_AXIS));

        addHoldingPanes();
    }

    private boolean isToolSettingsScrollbarVisible = false;

    private void addHoldingPanes() {
        JScrollPane toolPresetsPane = new JScrollPane(toolPresets);

        Dimension minimumSize = new Dimension(0, 146);
        toolPresetsPane.setMinimumSize(minimumSize);
        toolSettingsPane.setMinimumSize(minimumSize);

        toolPresetsPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        toolSettingsPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JSplitPane tPtS = new JSplitPane(JSplitPane.VERTICAL_SPLIT, toolPresetsPane, toolSettingsPane);

        tPtS.setResizeWeight(0.5);
        add(tPtS);

        toolSettingsPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                resizeToolSettingsComponents();
            }
        });

        toolSettingsPane.getViewport().addChangeListener(e -> {
            if(isToolSettingsScrollbarVisible != toolSettingsPane.getVerticalScrollBar().isVisible()){
                isToolSettingsScrollbarVisible = toolSettingsPane.getVerticalScrollBar().isVisible();
                resizeToolSettingsComponents();
            }
        });
    }

    private static void resizeToolSettingsComponents() {
        int width = toolSettingsPane.getWidth();
        width -= toolSettingsPane.getVerticalScrollBar().getWidth();

        for (Component c : toolSettings.getComponents()) {
            c.setBounds(c.getX(), c.getY(), width, c.getHeight());
            c.setMinimumSize(new Dimension(width, c.getHeight()));
            c.setPreferredSize(new Dimension(width, c.getHeight()));
            c.setMaximumSize(new Dimension(width, c.getHeight()));
        }
    }

    public static void addComponentToToolSettings(JPanel component) {
        toolSettings.add(component);
    }

    public static int getCurrentWidth() {
        return toolSettings.getWidth();
    }
}
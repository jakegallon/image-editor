package frame;

import tool.BaseTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ToolSettings extends JPanel {

    private static final JPanel toolPresets = new JPanel();
    private static final JPanel toolSettings = new JPanel();
    private static final JPanel globalSettings = new JPanel();

    private static final JScrollPane toolSettingsPane = new JScrollPane(toolSettings);

    public ToolSettings() {
        init();
    }

    public static void onNewTool(BaseTool tool) {
        setToolPresetsFromTool(tool);
        setToolSettingsFromTool(tool);
    }

    private static void setToolPresetsFromTool(BaseTool tool) {
        toolSettings.removeAll();

        //todo

        toolSettings.revalidate();
        toolSettings.repaint();
    }

    private static void setToolSettingsFromTool(BaseTool tool) {
        toolSettings.removeAll();

        tool.populateSettingsPanel(toolSettings);

        toolSettings.revalidate();
        toolSettings.repaint();
    }

    private void populateGlobalSettings() {
        //todo
    }

    private void init() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        toolPresets.setLayout(new BoxLayout(toolPresets, BoxLayout.PAGE_AXIS));
        toolSettings.setLayout(new BoxLayout(toolSettings, BoxLayout.PAGE_AXIS));
        globalSettings.setLayout(new BoxLayout(globalSettings, BoxLayout.PAGE_AXIS));

        toolSettings.setAlignmentX(Component.LEFT_ALIGNMENT);
        addHoldingPanes();
    }

    private boolean isToolSettingsScrollbarVisible = false;

    private void addHoldingPanes() {
        JScrollPane toolPresetsPane = new JScrollPane(toolPresets);
        JScrollPane globalSettingsPane = new JScrollPane(globalSettings);

        Dimension minimumSize = new Dimension(0, 146);
        toolPresetsPane.setMinimumSize(minimumSize);
        toolSettingsPane.setMinimumSize(minimumSize);
        globalSettingsPane.setMinimumSize(minimumSize);

        toolPresetsPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        toolSettingsPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        globalSettingsPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JSplitPane tPtS = new JSplitPane(JSplitPane.VERTICAL_SPLIT, toolPresetsPane, toolSettingsPane);
        JSplitPane tPtSgS = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tPtS, globalSettingsPane);

        tPtS.setResizeWeight(0.5);
        tPtSgS.setResizeWeight(0.66);
        add(tPtSgS);

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
}
package frame;

import javax.swing.*;
import java.awt.*;

public class Canvas extends JPanel {

    public Canvas() {
        setBounds(0, 0, 200, 200);
        setBackground(Color.white);

        SwingUtilities.invokeLater(() -> {
            CanvasPanel canvasPanel = (CanvasPanel) getParent();
            setLocation((canvasPanel.getWidth() - getWidth())/2, (canvasPanel.getHeight() - getHeight())/2);
        });
    }
}


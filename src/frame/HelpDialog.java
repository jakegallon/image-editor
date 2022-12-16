package frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;

public class HelpDialog extends JDialog {

    public HelpDialog(JPanel contents) {
        init();
        setSize(contents.getSize());
        add(contents, BorderLayout.CENTER);
    }

    final AWTEventListener awtEventListener = awtEvent -> {
        MouseEvent mouseEvent = (MouseEvent) awtEvent;
        if(mouseEvent.getID() == MouseEvent.MOUSE_PRESSED) {
            if(mouseEvent.getComponent() != this) {
                onHelpEnd();
            }
        }
    };

    private void onHelpEnd() {
        Toolkit.getDefaultToolkit().removeAWTEventListener(awtEventListener);
        dispose();
    }

    private void init() {
        setUndecorated(true);
        setResizable(false);
        setLayout(new BorderLayout());

        Toolkit.getDefaultToolkit().addAWTEventListener(awtEventListener, AWTEvent.MOUSE_EVENT_MASK);
    }
}
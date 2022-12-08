package frame;

import javax.swing.*;
import java.awt.*;

public class NotificationPanel extends JPanel {

    private static final JLabel message = new JLabel("");

    private static final Timer timer = new Timer(5000, e -> {
        message.setText("");
        stopTimer();
    });

    public NotificationPanel() {
        setBackground(new Color(0, 0, 0, 0));
        setLayout(new FlowLayout(FlowLayout.LEADING));

        message.setForeground(Color.red);
        message.setFont(message.getFont().deriveFont(16f));

        add(message);
    }

    public static void playNotification(NotificationMessage notificationMessage) {
        message.setText(notificationMessage.getMessage());
        timer.start();
    }

    private static void stopTimer() {
        timer.stop();
    }
}

package tool;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.util.Objects;

public enum ToolCategory {
    NONE(null),
    PEN("pen.png"),
    ERASE("erase.png"),
    SELECT("select.png"),
    MOVE("move.png"),
    EYE("eye.png"),
    FILL("fill.png");

    ImageIcon icon;

    ToolCategory(String iconFileName) {
        if(iconFileName != null) try {
            String fileName = "/res/" + iconFileName;
            icon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource(fileName))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ImageIcon getIcon() {
        return icon;
    }
}


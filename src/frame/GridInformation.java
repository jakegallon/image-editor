package frame;

import java.io.Serializable;

public record GridInformation(int gridX, int gridY, GridStyle gridStyle) implements Serializable {

}


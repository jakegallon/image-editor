package action;

import java.io.Serializable;

public record PixelChange(int x, int y, int oldColor, int newColor) implements Serializable {

}

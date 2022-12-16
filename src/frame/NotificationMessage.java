package frame;

public enum NotificationMessage {
    NO_CANVAS("Cut, Copy, and Paste require a canvas."),
    NO_CANVAS_DELETE("Deleting something requires a canvas."),
    NO_CANVAS_SAVE("You must have a canvas in order to save the canvas."),
    NO_CANVAS_EXPORT("You must have a canvas in order to export the canvas."),
    NO_MORE_UNDO("There are no more edits left to undo..."),
    NO_MORE_REDO("There are no more edits left to redo..."),
    NO_SELECTION_TO_MOVE("There is no selection to move. The Select Tool can be used to create one."),
    TOOL_REQUIRES_CANVAS("There must be a canvas to use this tool."),
    TOOL_PRESS_OOB("This tool must be used on the canvas."),
    TOOL_LAYER_LOCKED("This tool can't be used on a locked layer."),
    CLIPBOARD_NOT_IMAGE("The clipboard does not contain an image."),
    CLIPBOARD_TOO_WIDE("The clipboard image is too wide to paste on the canvas."),
    CLIPBOARD_TOO_TALL("The clipboard image is too tall to paste on the canvas.")
    ;

    private final String message;

    NotificationMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

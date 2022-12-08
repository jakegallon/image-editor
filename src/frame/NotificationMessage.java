package frame;

public enum NotificationMessage {
    NO_MORE_UNDO("There are no more edits left to undo..."),
    NO_MORE_REDO("There are no more edits left to redo..."),
    TOOL_REQUIRES_CANVAS("There must be a canvas to use this tool."),
    TOOL_PRESS_OOB("This tool must be used on the canvas.")
    ;

    private final String message;

    NotificationMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

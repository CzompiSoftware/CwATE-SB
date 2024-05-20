package org.commonmark.ext.czsoft.xmd.alert;

public enum AlertType {
    SUCCESS,
    INFO,
    WARNING,
    DANGER,
    DEFAULT;

    public static AlertType fromString(String text) {
        if (text == null || text.trim().isEmpty()) {
            return DEFAULT;
        }

        return switch (text.toLowerCase()) {
            case "success" -> SUCCESS;
            case "info" -> INFO;
            case "warning" -> WARNING;
            case "danger" -> DANGER;
            case "error" -> DANGER;
            default -> throw new IllegalStateException();
        };
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}

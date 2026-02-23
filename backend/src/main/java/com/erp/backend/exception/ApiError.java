package com.erp.backend.exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ApiError {

    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    public ApiError() {
        this.timestamp = getCurrentTimestamp();
    }

    public ApiError(int status, String error, String message, String path) {
        this.timestamp = getCurrentTimestamp();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    private String getCurrentTimestamp() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }
}
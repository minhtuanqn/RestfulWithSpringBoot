package com.model;

import java.time.LocalDateTime;
import java.util.Map;

public class APIErrorModel {
    private LocalDateTime time;
    private String error;
    private Map<String, String> message;

    public APIErrorModel(LocalDateTime time, String error, Map<String, String> message) {
        this.time = time;
        this.error = error;
        this.message = message;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Map<String, String> getMessage() {
        return message;
    }

    public void setMessage(Map<String, String> message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

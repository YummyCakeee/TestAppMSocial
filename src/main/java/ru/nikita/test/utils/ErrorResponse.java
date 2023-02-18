package ru.nikita.test.utils;

import org.springframework.http.HttpStatus;

public class ErrorResponse {
    private HttpStatus status;
    private String error;

    public ErrorResponse(HttpStatus status, String error) {
        this.status = status;
        this.error = error;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

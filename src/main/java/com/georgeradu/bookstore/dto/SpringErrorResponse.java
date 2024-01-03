package com.georgeradu.bookstore.dto;

import java.util.Objects;

public class SpringErrorResponse {
    public long timestamp;
    public int status;
    public String error;
    public String exception;
    public String message;
    public String path;

    public SpringErrorResponse() {
    }

    public SpringErrorResponse(
            long timestamp, int status, String error, String exception, String message, String path
    ) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.exception = exception;
        this.message = message;
        this.path = path;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpringErrorResponse that = (SpringErrorResponse) o;
        return timestamp == that.timestamp && status == that.status && Objects.equals(error, that.error) &&
               Objects.equals(exception, that.exception) && Objects.equals(message, that.message) &&
               Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, status, error, exception, message, path);
    }

    @Override
    public String toString() {
        return "SpringErrorResponse{" + "timestamp=" + timestamp + ", status=" + status + ", error='" + error + '\'' +
               ", exception='" + exception + '\'' + ", message='" + message + '\'' + ", path='" + path + '\'' + '}';
    }
}

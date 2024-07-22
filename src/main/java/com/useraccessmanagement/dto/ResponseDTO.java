package com.useraccessmanagement.dto;

public class ResponseDTO {
    private String status;
    private String message;
    private Object details;

    public ResponseDTO() {
    }

    public ResponseDTO(String status, String message, Object details) {
        this.status = status;
        this.message = message;
        this.details = details;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getDetails() {
        return details;
    }

    public void setDetails(Object details) {
        this.details = details;
    }
}

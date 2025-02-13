package com.agi.banco.exception;

import lombok.Data;

@Data
public class ErrorResponse {
    private int status;
    private String message;
    private Object errors;

    public ErrorResponse(int status, String message, Object errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }
}
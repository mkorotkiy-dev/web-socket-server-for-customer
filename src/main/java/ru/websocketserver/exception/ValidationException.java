package ru.websocketserver.exception;

import lombok.Getter;

import java.util.List;

public class ValidationException extends AppException {

    @Getter
    private final List<String> errors;

    public ValidationException(List<String> errors) {
        this.errors = errors;
    }

    public ValidationException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

}
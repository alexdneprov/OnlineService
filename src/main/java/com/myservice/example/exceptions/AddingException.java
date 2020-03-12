package com.myservice.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AddingException extends ResponseStatusException {
    public AddingException(HttpStatus status, String reason) {
        super(status, reason);
    }
}

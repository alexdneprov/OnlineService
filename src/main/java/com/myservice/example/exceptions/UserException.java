package com.myservice.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public class UserException extends ResponseStatusException {

    public UserException(HttpStatus status, String reason) {
        super(status, reason);
    }
}

package com.example.demo.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TakenResourceException extends RuntimeException{
    public TakenResourceException(String message) {
        super(message);
    }
}
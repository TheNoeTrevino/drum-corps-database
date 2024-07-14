package com.respec.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PageableException extends RuntimeException{

    public PageableException(String message) {
        super(message);
    }
}

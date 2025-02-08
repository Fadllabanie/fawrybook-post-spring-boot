package com.Fawrybook.Fawrybook.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class commentNotFoundException extends RuntimeException {

    public commentNotFoundException(String message) {
        super(message);
    }
}

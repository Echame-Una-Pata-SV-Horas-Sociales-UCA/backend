package com.echameunapata.backend.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HttpError extends RuntimeException {

    private final HttpStatus status;

    public HttpError(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}

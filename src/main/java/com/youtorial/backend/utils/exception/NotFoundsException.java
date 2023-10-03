package com.youtorial.backend.utils.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NotFoundsException extends RuntimeException {

    public NotFoundsException(String message) {
        super(message);
    }

    public NotFoundsException() {
        super();
    }
}

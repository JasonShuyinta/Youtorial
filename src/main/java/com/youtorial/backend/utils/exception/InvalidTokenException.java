package com.youtorial.backend.utils.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class InvalidTokenException extends Exception{


    public InvalidTokenException() {
        super();
    }

    public InvalidTokenException(String msg) {
        super(msg);
    }
}

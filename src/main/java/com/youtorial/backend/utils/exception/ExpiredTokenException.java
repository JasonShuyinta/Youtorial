package com.youtorial.backend.utils.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ExpiredTokenException extends Exception {

    public ExpiredTokenException() {
        super();
    }

    public ExpiredTokenException(String msg) {
        super(msg);
    }

}

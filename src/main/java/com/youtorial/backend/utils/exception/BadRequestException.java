package com.youtorial.backend.utils.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BadRequestException extends RuntimeException{

    public BadRequestException(String message) { super(message); }

    public BadRequestException() { super(); }
}

package org.neueda.rest.exception;

public class InvalidParamsException extends RuntimeException {
    public InvalidParamsException(String message){
        super(message);
    }
}

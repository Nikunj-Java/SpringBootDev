package org.neueda.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    //custom Exception
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> handleCustomerNotFoundException(CustomerNotFoundException ex){
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    //custom Exception
    @ExceptionHandler(InvalidParamsException.class)
    public ResponseEntity<String> handleInvalidParamsException(InvalidParamsException ex){
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<String> hndleDuplicateEmailException(DuplicateEmailException ex){
        return  ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
    //global Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex){
        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexcepted error occured! "+ex.getMessage());

    }

}

package net.java.portfolio.banking.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle specific exception - Account Exception
    @ExceptionHandler(AccountException.class)
    public ResponseEntity<ErrorDetails> handleAccountException(AccountException exception, WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),exception.getMessage(), webRequest.getDescription(false), exception.getMessage());
        return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
    }

    //Generic exception
    @ExceptionHandler
    public ResponseEntity<ErrorDetails> handleGenericException(Exception exception, WebRequest webRequest){

        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),exception.getMessage() , webRequest.getDescription(false),"INTERNAL SERVER ERROR");
        return new ResponseEntity<>(errorDetails,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

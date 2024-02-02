package org.medic.medic_facility.interceptor;

import org.example.exception.DataAlreadyExistException;
import org.example.exception.DataNotFoundException;
import org.example.exception.ForbiddenException;
import org.example.response.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class Interceptor {

    @ExceptionHandler({
            RuntimeException.class,
    })
    public ResponseEntity<HttpResponse> handleException(RuntimeException ex){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        HttpResponse.sendErrorResponse(ex.getMessage(), false)
                );
    }

    @ExceptionHandler({
            DataNotFoundException.class,
    })
    public ResponseEntity<HttpResponse> handleException(DataNotFoundException ex){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        HttpResponse.sendErrorResponse(ex.getMessage(), true)
                );
    }

    @ExceptionHandler({
            DataAlreadyExistException.class
    })
    public ResponseEntity<HttpResponse> handleException(DataAlreadyExistException ex){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        HttpResponse.sendErrorResponse(ex.getMessage(), false)
                );
    }

    @ExceptionHandler({
            ForbiddenException.class
    })
    public ResponseEntity<HttpResponse> handleException(ForbiddenException ex){
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(
                        HttpResponse.sendErrorResponse(ex.getMessage(), false)
                );
    }
}

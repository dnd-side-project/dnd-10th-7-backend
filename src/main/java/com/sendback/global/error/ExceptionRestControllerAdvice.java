package com.sendback.global.error;

import com.sendback.global.error.type.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ExceptionRestControllerAdvice {


    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatusCode statusCode, WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        List<ObjectError> objectErrors = bindingResult.getAllErrors();
        List<String> errorMessages = new ArrayList<>();
        for (ObjectError objectError : objectErrors) {
            errorMessages.add(objectError.getDefaultMessage());
        }
        String errorMessage = String.join(" ", errorMessages);
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundException(NotFoundException ex) {

        return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND.value(),
                ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> badRequestException(BadRequestException ex) {

        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ErrorResponse> unauthorizedUserException(UnAuthorizedException ex) {

        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> forbiddenRequestException(ForbiddenException ex) {

        return new ResponseEntity<>(new ErrorResponse(HttpStatus.FORBIDDEN.value(),
                ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception ex) {

        return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
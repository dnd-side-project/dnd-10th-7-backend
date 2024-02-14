package com.sendback.global.exception;

import com.sendback.global.exception.response.ErrorResponse;
import com.sendback.global.exception.response.ExceptionResponse;
import com.sendback.global.exception.type.*;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(final Exception e) {
        log.error("[" + e.getClass() + "] : " + e.getMessage());
        return ResponseEntity.internalServerError()
                .body(ExceptionResponse.of(100, "알 수 없는 서버 에러가 발생했습니다."));
    }

    //valid 검증
    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException e
    ) {

        BindingResult bindingResult = e.getBindingResult();
        List<ObjectError> objectErrors = bindingResult.getAllErrors();
        List<String> errorMessages = new ArrayList<>();
        for (ObjectError objectError : objectErrors) {
            errorMessages.add(objectError.getDefaultMessage());
        }
        String errorMessage = String.join(" ", errorMessages);
        return ResponseEntity.badRequest()
                .body(ExceptionResponse.of(300, errorMessage));
    }



    //requestParam 검증
    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> constraintViolationException(final ConstraintViolationException e) {
        final List<ErrorResponse> errorResponses = e.getConstraintViolations()
                .stream()
                .map(error -> new ErrorResponse(error.getPropertyPath().toString(), error.getMessage()))
                .toList();
        log.warn("[" + e.getClass() + "] " + errorResponses);
        return ResponseEntity.badRequest()
                .body(ExceptionResponse.of(301, errorResponses.toString()));
    }

    //requestPart 검증
    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> unAuthorizedException(final MissingServletRequestPartException e) {

        return ResponseEntity.badRequest()
                .body(ExceptionResponse.of(302, e.getMessage()));
    }

    // login 검증
    @ExceptionHandler(SignInException.class)
    public ResponseEntity<?> handleSignInException(SignInException e) {
        return ResponseEntity.badRequest()
                .body(ExceptionResponse.of(e.getExceptionType(), e.data));
    }

    // 인증 처리 검증
    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> unAuthorizedException(final UnAuthorizedException e) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ExceptionResponse.from(e.getExceptionType()));
    }

    // request 검증
    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleBadRequestException(final BadRequestException e) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.from(e.getExceptionType()));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleForbiddenException(final ForbiddenException e) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ExceptionResponse.from(e.getExceptionType()));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleNotFoundException(final NotFoundException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponse.from(e.getExceptionType()));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleImageException(final ImageException e) {
        log.warn("[" + e.getClass() + "] : " + e.getMessage());
        return ResponseEntity.badRequest()
                .body(ExceptionResponse.from(e.getExceptionType()));
    }

}

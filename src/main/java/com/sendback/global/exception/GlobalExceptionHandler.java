package com.sendback.global.exception;

import com.sendback.global.exception.response.ErrorResponse;
import com.sendback.global.exception.response.ExceptionResponse;
import com.sendback.global.exception.type.BadRequestException;
import com.sendback.global.exception.type.ForbiddenException;
import com.sendback.global.exception.type.NotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(final Exception e) {
        log.error("[" + e.getClass() + "] : " + e.getMessage());
        return ResponseEntity.internalServerError()
                .body(new ExceptionResponse(100, "알 수 없는 서버 에러가 발생했습니다."));
    }

    //valid 검증
    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException e
    ) {
        final List<ErrorResponse> errorResponses = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ErrorResponse(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();
        log.warn("[" + e.getClass() + "] " + errorResponses);
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(200, errorResponses.toString()));
    }

    //requestParam 검증
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse> constraintViolationException(final ConstraintViolationException e) {
        final List<ErrorResponse> errorResponses = e.getConstraintViolations()
                .stream()
                .map(error -> new ErrorResponse(error.getPropertyPath().toString(), error.getMessage()))
                .toList();
        log.warn("[" + e.getClass() + "] " + errorResponses);
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(201, errorResponses.toString()));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleBadRequestException(final BadRequestException e) {
        log.warn("[" + e.getClass() + "] : " + e.getMessage());
        return ResponseEntity.badRequest()
                .body(ExceptionResponse.from(e));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleForbiddenException(final ForbiddenException e) {
        log.warn("[" + e.getClass() + "] : " + e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ExceptionResponse.from(e));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleNotFoundException(final NotFoundException e) {
        log.warn("[" + e.getClass() + "] : " + e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponse.from(e));
    }

}

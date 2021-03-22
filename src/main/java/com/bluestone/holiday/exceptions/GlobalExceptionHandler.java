package com.bluestone.holiday.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(TechnicalException.class)
    public ResponseEntity<String> handleTechnicalException(TechnicalException exception) {
        log.error("Technical error {}", exception.getMessage());
        return new ResponseEntity<>(exception.getInfo(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusinessException(BusinessException exception) {
        log.error("Business error {}", exception.getInfo());
        return new ResponseEntity<>(exception.getInfo(), HttpStatus.BAD_REQUEST);
    }
}


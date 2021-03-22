package com.bluestone.holiday.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void testHandleTechnicalException() {
        // given
        String info = "Holidays API did not respond";
        String exceptionMessage = "Error at...";

        TechnicalException exception = new TechnicalException(info, exceptionMessage);

        // when
        ResponseEntity<String> result = globalExceptionHandler.handleTechnicalException(exception);

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals(info, result.getBody());
    }

    @Test
    void testHandleBusinessException() {
        // given
        String info = "Country code PLPL is invalid";

        BusinessException exception = new BusinessException(info);

        // when
        ResponseEntity<String> result = globalExceptionHandler.handleBusinessException(exception);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(info, result.getBody());
    }
}

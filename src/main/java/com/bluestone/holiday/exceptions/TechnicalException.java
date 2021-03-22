package com.bluestone.holiday.exceptions;

import lombok.Getter;

@Getter
public class TechnicalException extends RuntimeException {

    private final String info;

    public TechnicalException(String info, String exceptionMessage) {
        super(exceptionMessage);
        this.info = info;
    }
}

package com.bluestone.holiday.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TechnicalException extends RuntimeException {

    private final String info;

    public TechnicalException(String info, String exceptionMessage) {
        super(exceptionMessage);
        this.info = info;
    }
}

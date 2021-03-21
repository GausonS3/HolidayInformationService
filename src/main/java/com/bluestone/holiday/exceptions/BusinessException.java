package com.bluestone.holiday.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final String info;

    public BusinessException(String info) {
        super();
        this.info = info;
    }
}

package com.bluestone.holiday.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {

    private final String info;

    public BusinessException(String info) {
        super();
        this.info = info;
    }
}

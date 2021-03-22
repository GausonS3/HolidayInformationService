package com.bluestone.holiday.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 4139160142326728224L;

    private String key;
    private String value;
}

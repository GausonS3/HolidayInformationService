package com.bluestone.holiday.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicHolidayDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -5328890921130997452L;

    private LocalDate date;
    private String localName;
}

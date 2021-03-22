package com.bluestone.holiday.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
public class MatchingHolidayDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 6590376786486868559L;

    private LocalDate date;
    private String name1;
    private String name2;
}

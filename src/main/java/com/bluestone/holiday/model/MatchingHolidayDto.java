package com.bluestone.holiday.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class MatchingHolidayDto {

    private LocalDate date;
    private String name1;
    private String name2;
}

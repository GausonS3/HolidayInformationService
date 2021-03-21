package com.bluestone.holiday.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PublicHolidayDto {

    private final LocalDate date;
    private final String localName;
}

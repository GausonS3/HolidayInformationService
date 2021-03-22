package com.bluestone.holiday.service;

import com.bluestone.holiday.model.CountryDto;
import com.bluestone.holiday.model.PublicHolidayDto;

import java.util.List;

public interface HolidayApi {

    List<CountryDto> getAvailableCountryCodes();
    List<PublicHolidayDto> getHolidaysFor(int year, String country);
}

package com.bluestone.holiday.service;

import com.bluestone.holiday.model.CountryDto;
import com.bluestone.holiday.model.MatchingHolidayDto;
import com.bluestone.holiday.model.PublicHolidayDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HolidayServiceTest {

    @Mock
    private HolidayApi holidayApi;

    @InjectMocks
    private HolidayService holidayService;

    @Test
    void getAvailableCountryCodes() {
        // given
        CountryDto pl = new CountryDto("PL", "Poland");
        CountryDto cn = new CountryDto("CN", "China");

        List<CountryDto> availableCountryCodes = List.of(pl, cn);

        when(holidayApi.getAvailableCountryCodes()).thenReturn(availableCountryCodes);

        // when
        List<CountryDto> result = holidayService.getAvailableCountryCodes();

        // then
        assertEquals(availableCountryCodes, result);
        verify(holidayApi, times(1)).getAvailableCountryCodes();
    }

    @Test
    void getNextMatchingHolidayInCurrentYear() {
        // given
        String firstCountryCode = "PL";
        String secondCountryCode = "CN";
        LocalDate date = LocalDate.of(2020, 1, 1);

        String plTestHolidayName = "TestHoliday";
        String cnTestHolidayName = "新年, 中國傳統新年";

        LocalDate dateOfMatchingHoliday = LocalDate.of(2020, 4, 4);

        PublicHolidayDto plNewYear = new PublicHolidayDto(dateOfMatchingHoliday, plTestHolidayName);
        PublicHolidayDto cnNewYear = new PublicHolidayDto(dateOfMatchingHoliday, cnTestHolidayName);

        when(holidayApi.getHolidaysFor(date.getYear(), firstCountryCode)).thenReturn(List.of(plNewYear));
        when(holidayApi.getHolidaysFor(date.getYear(), secondCountryCode)).thenReturn(List.of(cnNewYear));

        // when
        Optional<MatchingHolidayDto> result = holidayService.getNextMatchingHoliday(firstCountryCode, secondCountryCode, date);

        // then
        assertTrue(result.isPresent());
        assertEquals(dateOfMatchingHoliday, result.get().getDate());
        assertEquals(plTestHolidayName, result.get().getName1());
        assertEquals(cnTestHolidayName, result.get().getName2());

        verify(holidayApi, times(1)).getHolidaysFor(date.getYear(), firstCountryCode);
        verify(holidayApi, times(1)).getHolidaysFor(date.getYear(), secondCountryCode);
    }

    @Test
    void getNextMatchingHolidayInFollowingYear() {
        // given
        String firstCountryCode = "PL";
        String secondCountryCode = "CN";
        LocalDate date = LocalDate.of(2020, 6, 6);

        String plTestHolidayName = "TestHoliday";
        String cnTestHolidayName = "新年, 中國傳統新年";

        LocalDate dateOfMatchingHolidayInCurrentYear = LocalDate.of(2020, 4, 4);

        PublicHolidayDto plTestHolidayInCurrentYear = new PublicHolidayDto(dateOfMatchingHolidayInCurrentYear, plTestHolidayName);
        PublicHolidayDto cnTestHolidayInCurrentYear = new PublicHolidayDto(dateOfMatchingHolidayInCurrentYear, cnTestHolidayName);

        when(holidayApi.getHolidaysFor(date.getYear(), firstCountryCode)).thenReturn(List.of(plTestHolidayInCurrentYear));
        when(holidayApi.getHolidaysFor(date.getYear(), secondCountryCode)).thenReturn(List.of(cnTestHolidayInCurrentYear));

        String plTestHolidayNameInFollowingYear = "FollowingTestHoliday";
        String cnTestHolidayNameInFollowingYear = "中國傳統新年";

        LocalDate dateOfMatchingHolidayInFollowingYear = LocalDate.of(2021, 1, 1);

        PublicHolidayDto plNewYearInFollowingYear = new PublicHolidayDto(dateOfMatchingHolidayInFollowingYear, plTestHolidayNameInFollowingYear);
        PublicHolidayDto cnNewYeaInFollowingYearr = new PublicHolidayDto(dateOfMatchingHolidayInFollowingYear, cnTestHolidayNameInFollowingYear);

        when(holidayApi.getHolidaysFor(date.plusYears(1).getYear(), firstCountryCode)).thenReturn(List.of(plNewYearInFollowingYear));
        when(holidayApi.getHolidaysFor(date.plusYears(1).getYear(), secondCountryCode)).thenReturn(List.of(cnNewYeaInFollowingYearr));

        // when
        Optional<MatchingHolidayDto> result = holidayService.getNextMatchingHoliday(firstCountryCode, secondCountryCode, date);

        // then
        assertTrue(result.isPresent());
        assertEquals(dateOfMatchingHolidayInFollowingYear, result.get().getDate());
        assertEquals(plTestHolidayNameInFollowingYear, result.get().getName1());
        assertEquals(cnTestHolidayNameInFollowingYear, result.get().getName2());

        verify(holidayApi, times(1)).getHolidaysFor(date.getYear(), firstCountryCode);
        verify(holidayApi, times(1)).getHolidaysFor(date.getYear(), secondCountryCode);
        verify(holidayApi, times(1)).getHolidaysFor(date.plusYears(1).getYear(), firstCountryCode);
        verify(holidayApi, times(1)).getHolidaysFor(date.plusYears(1).getYear(), secondCountryCode);
    }

    @Test
    void getNextMatchingHolidayWithNoResult() {
        // given
        String firstCountryCode = "PL";
        String secondCountryCode = "CN";
        LocalDate date = LocalDate.of(2020, 6, 6);

        when(holidayApi.getHolidaysFor(date.getYear(), firstCountryCode)).thenReturn(Collections.emptyList());
        when(holidayApi.getHolidaysFor(date.getYear(), secondCountryCode)).thenReturn(Collections.emptyList());
        when(holidayApi.getHolidaysFor(date.plusYears(1).getYear(), firstCountryCode)).thenReturn(Collections.emptyList());
        when(holidayApi.getHolidaysFor(date.plusYears(1).getYear(), secondCountryCode)).thenReturn(Collections.emptyList());

        // when
        Optional<MatchingHolidayDto> result = holidayService.getNextMatchingHoliday(firstCountryCode, secondCountryCode, date);

        // then
        assertTrue(result.isEmpty());
        verify(holidayApi, times(1)).getHolidaysFor(date.getYear(), firstCountryCode);
        verify(holidayApi, times(1)).getHolidaysFor(date.getYear(), secondCountryCode);
        verify(holidayApi, times(1)).getHolidaysFor(date.plusYears(1).getYear(), firstCountryCode);
        verify(holidayApi, times(1)).getHolidaysFor(date.plusYears(1).getYear(), secondCountryCode);
    }
}

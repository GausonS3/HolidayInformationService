package com.bluestone.holiday.controller;

import com.bluestone.holiday.model.CountryDto;
import com.bluestone.holiday.model.MatchingHolidayDto;
import com.bluestone.holiday.service.CountryValidationService;
import com.bluestone.holiday.service.HolidayService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HolidayInformationControllerTest {

    @Mock
    private HolidayService holidayService;

    @Mock
    private CountryValidationService countryValidationService;

    @InjectMocks
    private HolidayInformationController holidayInformationController;

    @Test
    void getNextMatchingHolidayWithResult() {
        // given
        String firstCountryCode = "PL";
        String secondCountryCode = "CN";
        LocalDate date = LocalDate.now();

        MatchingHolidayDto matchingHolidayDto = MatchingHolidayDto.builder().build();

        when(holidayService.getNextMatchingHoliday(firstCountryCode, secondCountryCode, date))
                .thenReturn(Optional.of(matchingHolidayDto));
        // when
        ResponseEntity<MatchingHolidayDto> result =
                holidayInformationController.getNextMatchingHoliday(firstCountryCode, secondCountryCode, date);

        // then
        verify(countryValidationService, times(1)).validateCountryCodes(firstCountryCode, secondCountryCode);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(matchingHolidayDto, result.getBody());
    }

    @Test
    void getNextMatchingHolidayWithoutResult() {
        // given
        String firstCountryCode = "PL";
        String secondCountryCode = "CN";
        LocalDate date = LocalDate.now();
        when(holidayService.getNextMatchingHoliday(firstCountryCode, secondCountryCode, date))
                .thenReturn(Optional.empty());
        // when
        ResponseEntity<MatchingHolidayDto> result =
                holidayInformationController.getNextMatchingHoliday(firstCountryCode, secondCountryCode, date);

        // then
        verify(countryValidationService, times(1)).validateCountryCodes(firstCountryCode, secondCountryCode);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertTrue(Objects.isNull(result.getBody()));
    }

    @Test
    void listAvailableCountryCodes() {
        // given
        // when
        ResponseEntity<List<CountryDto>> result = holidayInformationController.listAvailableCountryCodes();

        // then
        verify(holidayService, times(1)).getAvailableCountryCodes();
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}

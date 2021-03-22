package com.bluestone.holiday.service;

import com.bluestone.holiday.exceptions.BusinessException;
import com.bluestone.holiday.model.CountryDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CountryValidationServiceTest {

    @Mock
    private NagerApi nagerApi;

    @InjectMocks
    private CountryValidationService countryValidationService;

    @Test
    void validateCountryCodesWithoutException() {
        // given
        String firstCountryCode = "PL";
        String secondCountryCode = "CN";

        CountryDto pl = new CountryDto(firstCountryCode, "Poland");
        CountryDto cn = new CountryDto(secondCountryCode, "China");

        List<CountryDto> availableCountryCodes = List.of(pl, cn);

        when(nagerApi.getAvailableCountryCodes()).thenReturn(availableCountryCodes);

        // when, then
        assertDoesNotThrow(() -> countryValidationService.validateCountryCodes(firstCountryCode, secondCountryCode));
    }

    @Test
    void validateCountryCodesWithException() {
        // given
        String firstCountryCode = "PL";
        String secondCountryCode = "CN";

        when(nagerApi.getAvailableCountryCodes()).thenReturn(Collections.emptyList());

        // when, then
        assertThrows(BusinessException.class,
                () -> countryValidationService.validateCountryCodes(firstCountryCode, secondCountryCode));
    }
}
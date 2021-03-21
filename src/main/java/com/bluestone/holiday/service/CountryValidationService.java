package com.bluestone.holiday.service;

import com.bluestone.holiday.exceptions.BusinessException;
import com.bluestone.holiday.model.CountryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CountryValidationService {

    private final NagerApi nagerApi;

    public void validateCountryCodes(String firstCountryCode, String secondCountryCode) {
        log.info("Validating following country codes {} {}", firstCountryCode, secondCountryCode);
        List<CountryDto> availableCountryCodes = nagerApi.getAvailableCountryCodes();

        throwIfCountryMissing(availableCountryCodes, firstCountryCode);
        throwIfCountryMissing(availableCountryCodes, secondCountryCode);

        log.info("No errors during country codes validation.");
    }

    public void throwIfCountryMissing(List<CountryDto> availableCountryCodes, String countryCode) {
        availableCountryCodes.stream()
                .map(CountryDto::getKey)
                .filter(i -> i.equals(countryCode))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Country code " + countryCode + " is invalid"));
    }
}

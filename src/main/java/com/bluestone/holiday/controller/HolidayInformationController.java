package com.bluestone.holiday.controller;

import com.bluestone.holiday.model.CountryDto;
import com.bluestone.holiday.model.MatchingHolidayDto;
import com.bluestone.holiday.service.CountryValidationService;
import com.bluestone.holiday.service.HolidayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
@Slf4j
public class HolidayInformationController {

    private final HolidayService holidayService;
    private final CountryValidationService countryValidationService;

    @GetMapping("/matching-holiday")
    public ResponseEntity<MatchingHolidayDto> getNextMatchingHoliday(@RequestParam(value = "firstCountryCode") String firstCountryCode,
                                                                     @RequestParam(value = "secondCountryCode") String secondCountryCode,
                                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                         @RequestParam(value = "date") LocalDate date) {
        log.info("Request received with following params: firstCountryCode {}, second country code {}, date {}",
                firstCountryCode, secondCountryCode, date);
        countryValidationService.validateCountryCodes(firstCountryCode, secondCountryCode);
        return holidayService.getNextMatchingHoliday(firstCountryCode, secondCountryCode, date)
                .map(i -> new ResponseEntity<>(i, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping("/country-codes")
    public ResponseEntity<List<CountryDto>> listAvailableCountryCodes() {
        log.info("List available country codes request received with following params");
        return new ResponseEntity<>(holidayService.getAvailableCountryCodes(), HttpStatus.OK);
    }
}

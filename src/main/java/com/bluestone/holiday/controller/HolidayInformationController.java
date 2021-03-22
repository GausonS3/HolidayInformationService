package com.bluestone.holiday.controller;

import com.bluestone.holiday.model.CountryDto;
import com.bluestone.holiday.model.MatchingHolidayDto;
import com.bluestone.holiday.service.CountryValidationService;
import com.bluestone.holiday.service.HolidayService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @ApiOperation(
            value = "Accepts two country codes and a date and return next holiday after the given date that will happen on the same \n" +
                    "day in both countries in the given or following year. ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "No holiday found in given or following year"),
            @ApiResponse(code = 400, message = "One of the provided country code is invalid"),
            @ApiResponse(code = 500, message = "External API Error")
    })
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
    @ApiOperation(
            value = "Returns list of available country codes that user can request matching holiday for")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "External API Error")
    })
    public ResponseEntity<List<CountryDto>> listAvailableCountryCodes() {
        log.info("List available country codes request received with following params");
        return new ResponseEntity<>(holidayService.getAvailableCountryCodes(), HttpStatus.OK);
    }
}

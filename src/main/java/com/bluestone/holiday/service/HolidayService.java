package com.bluestone.holiday.service;

import com.bluestone.holiday.model.CountryDto;
import com.bluestone.holiday.model.MatchingHolidayDto;
import com.bluestone.holiday.model.PublicHolidayDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HolidayService {

    private final NagerApi nagerApi;

    public List<CountryDto> getAvailableCountryCodes() {
        return nagerApi.getAvailableCountryCodes();
    }

    public Optional<MatchingHolidayDto> getNextMatchingHoliday(String firstCountryCode, String secondCountryCode, LocalDate date) {
        log.info("Trying to get common holiday for {} year. ", date.getYear());
        Optional<MatchingHolidayDto> dateOfMatchingHolidayForYear =
                getMatchingHolidayForGivenYear(firstCountryCode, secondCountryCode, date);

        if (dateOfMatchingHolidayForYear.isEmpty()) {
            log.info("There are no common holidays for {} year. Trying the following one.", date.getYear());
            dateOfMatchingHolidayForYear = getMatchingHolidayForFollowingYear(firstCountryCode, secondCountryCode, date);
        }

        dateOfMatchingHolidayForYear
                .ifPresentOrElse(i -> log.info("Found common holiday {}", i),
                        () -> log.info("There are no common holidays within 2 years"));

        return dateOfMatchingHolidayForYear;
    }

    private Optional<MatchingHolidayDto> getMatchingHolidayForGivenYear(String firstCountryCode, String secondCountryCode, LocalDate date) {
        var holidaysForFirstCountry = nagerApi.getHolidaysFor(date.getYear(), firstCountryCode);
        var holidaysForSecondCountry = nagerApi.getHolidaysFor(date.getYear(), secondCountryCode);

        var upcomingHolidaysForFirstCountry = filterPastHolidays(date, holidaysForFirstCountry);
        var upcomingHolidaysForSecondCountry = filterPastHolidays(date, holidaysForSecondCountry);

        return getMatchingHolidayForYear(upcomingHolidaysForFirstCountry, upcomingHolidaysForSecondCountry);
    }

    private Optional<MatchingHolidayDto> getMatchingHolidayForFollowingYear(String firstCountryCode, String secondCountryCode, LocalDate date) {
        int upcomingYear = date.plusYears(1).getYear();

        var holidaysInNextYearForFirstCountry = nagerApi.getHolidaysFor(upcomingYear, firstCountryCode);
        var holidaysInNextYearForSecondCountry = nagerApi.getHolidaysFor(upcomingYear, secondCountryCode);

        return getMatchingHolidayForYear(holidaysInNextYearForFirstCountry, holidaysInNextYearForSecondCountry);
    }

    private Optional<MatchingHolidayDto> getMatchingHolidayForYear(List<PublicHolidayDto> upcomingHolidaysForFirstCountry,
                                                                   List<PublicHolidayDto> upcomingHolidaysForSecondCountry) {
        Optional<LocalDate> dateOfMatchingHoliday =
                findDateOfMatchingHoliday(upcomingHolidaysForFirstCountry, upcomingHolidaysForSecondCountry);

        return dateOfMatchingHoliday.map(dateOfHoliday ->
                MatchingHolidayDto.builder()
                        .date(dateOfHoliday)
                        .name1(getNameOfHolidayForDate(upcomingHolidaysForFirstCountry, dateOfHoliday))
                        .name2(getNameOfHolidayForDate(upcomingHolidaysForSecondCountry, dateOfHoliday))
                        .build());
    }

    private String getNameOfHolidayForDate(List<PublicHolidayDto> upcomingHolidaysForFirstCountry, LocalDate dateOfHoliday) {
        return upcomingHolidaysForFirstCountry.stream().filter(i -> i.getDate().equals(dateOfHoliday)).map(PublicHolidayDto::getLocalName).findFirst().orElse("");
    }

    private List<PublicHolidayDto> filterPastHolidays(LocalDate date, List<PublicHolidayDto> holidays) {
        return holidays.stream().filter(holiday -> holiday.getDate().isAfter(date)).collect(Collectors.toList());
    }

    private Optional<LocalDate> findDateOfMatchingHoliday(List<PublicHolidayDto> upcomingHolidaysForFirstCountry,
                                                          List<PublicHolidayDto> upcomingHolidaysForSecondCountry) {
        return upcomingHolidaysForFirstCountry.stream()
                .map(PublicHolidayDto::getDate)
                .filter(holiday -> upcomingHolidaysForSecondCountry
                        .stream()
                        .map(PublicHolidayDto::getDate)
                        .collect(Collectors.toList())
                        .contains(holiday))
                .findFirst();
    }
}
